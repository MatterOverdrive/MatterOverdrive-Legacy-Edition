/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.init.MatterOverdriveEnchantments;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.includes.EnergyContainer;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.network.packet.server.PacketReloadEnergyWeapon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.EntityDamageSourcePhaser;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Simeon on 7/26/2015.
 */
public abstract class EnergyWeapon extends MOItemEnergyContainer implements IWeapon {

    public static final String CUSTOM_DAMAGE_TAG = "CustomDamage";
    public static final String CUSTOM_ACCURACY_TAG = "CustomAccuracy";
    public static final String CUSTOM_RANGE_TAG = "CustomRange";
    public static final String CUSTOM_SPEED_TAG = "CustomSpeed";
    public static final String CUSTOM_DAMAGE_MULTIPLY_TAG = "CustomDamageMultiply";
    public static final String CUSTOM_ACCURACY_MULTIPLY_TAG = "CustomAccuracyMultiply";
    public static final String CUSTOM_RANGE_MULTIPLY_TAG = "CustomRangeMultiply";
    public static final String CUSTOM_SPEED_MULTIPLY_TAG = "CustomSpeedMultiply";
    private final int defaultRange;
    private final DecimalFormat damageFormater = new DecimalFormat("#.##");
    protected boolean leftClickFire;

    public EnergyWeapon(String name, int defaultRange) {
        super(name);
        this.defaultRange = defaultRange;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return WeaponHelper.getColor(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.NONE;
    }

    @Override
    public int getItemStackLimit(ItemStack item) {
        return 1;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHolding) {
        if (!world.isRemote) {
            manageCooling(itemStack);
        } else {
            if (entity instanceof EntityPlayer && entity == Minecraft.getMinecraft().player && ((EntityPlayer) entity).getHeldItemMainhand() == itemStack && Minecraft.getMinecraft().currentScreen == null) {
                onShooterClientUpdate(itemStack, world, (EntityPlayer) entity, true);
            }
        }
    }
    //endregion

    //region Tooltips

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack weapon, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(weapon, player, worldIn, infos);
        String energyInfo = TextFormatting.DARK_RED + "Power Use: " + MOEnergyHelper.formatEnergy(null, getEnergyUse(weapon) * 20) + "/s";
        float energyMultiply = (float) getEnergyUse(weapon) / (float) getBaseEnergyUse(weapon);
        if (energyMultiply != 1) {
            energyInfo += " (" + DecimalFormat.getPercentInstance().format(energyMultiply) + ")";
        }
        infos.add(energyInfo);
        infos.add("");

        infos.add(addStatWithMultiplyInfo("Damage", damageFormater.format(getWeaponScaledDamage(weapon, player)), getWeaponScaledDamage(weapon, player) / getWeaponBaseDamage(weapon), ""));
        infos.add(addStatWithMultiplyInfo("DPS", damageFormater.format((getWeaponScaledDamage(weapon, player) / getShootCooldown(weapon)) * 20), 1, ""));
        infos.add(addStatWithMultiplyInfo("Speed", (int) (20d / getShootCooldown(weapon) * 60), (double) getBaseShootCooldown(weapon) / (double) getShootCooldown(weapon), " s/m"));
        infos.add(addStatWithMultiplyInfo("Range", getRange(weapon), (double) getRange(weapon) / (double) defaultRange, "b"));
        infos.add(addStatWithMultiplyInfo("Accuracy", "", 1 / (modifyStatFromModules(Reference.WS_ACCURACY, weapon, 1) * getCustomFloatStat(weapon, CUSTOM_ACCURACY_MULTIPLY_TAG, 1)), ""));

        StringBuilder heatInfo = new StringBuilder(TextFormatting.DARK_RED + "Heat: ");
        double heatPercent = getHeat(weapon) / getMaxHeat(weapon);
        for (int i = 0; i < 32 * heatPercent; i++) {
            heatInfo.append("|");
        }
        infos.add(heatInfo.toString());
        infos.add("");
        addCustomDetails(weapon, player, infos);
        AddModuleDetails(weapon, infos);
    }

    public boolean isEntitySpectator(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && ((EntityPlayer) entity).isSpectator();
    }

    private String addStatWithMultiplyInfo(String statName, Object value, double multiply, String units) {
        String info = String.format("%s: %s%s", statName, TextFormatting.DARK_AQUA, value);
        if (!units.isEmpty()) {
            info += " " + units;
        }
        if (multiply != 1) {
            if (multiply > 1) {
                info += TextFormatting.DARK_GREEN;
            } else {
                info += TextFormatting.DARK_RED;
            }
            info += String.format(" (%s) %s", DecimalFormat.getPercentInstance().format(multiply), TextFormatting.RESET);
        }
        return info;
    }

    private void AddModuleDetails(ItemStack weapon, List infos) {
        ItemStack module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BARREL, weapon);
        if (!module.isEmpty()) {
			/*infos.add(EnumChatFormatting.GRAY + "Barrel:");

            Object statsObject = ((IWeaponModule)module.getItem()).getValue(module);
            if (statsObject instanceof Map)
            {
                for (final Map.Entry<Integer, Double> entry : ((Map<Integer,Double>) statsObject).entrySet())
                {
                    if (entry.getKey() != Reference.WS_DAMAGE && entry.getKey() != Reference.WS_AMMO) {
                        infos.add("    " + MOStringHelper.weaponStatToInfo(entry.getKey(), entry.getValue()));
                    }
                }
            }

            infos.add("");*/
        }
    }
    //endregion

    //region client only

    /**
     * Used to manage the sending of weapons tick to the server.
     * This is tied to {@link #sendShootTickToServer(World, WeaponShot, Vec3d, Vec3d)}.
     * Used to mainly send weapon ticks without weapon shot information, aka. weapon firing.
     *
     * @param world the shooter's world.
     */
    @SideOnly(Side.CLIENT)
    protected void manageClientServerTicks(World world) {
        ClientProxy.instance().getClientWeaponHandler().sendWeaponTickToServer(world, null);
    }

    /**
     * A utility function for sending a weapon tick with a given weapon shot to the server.
     * This is part of the latency compensation system for Phaser bolts.
     *
     * @param world      the shooter's world.
     * @param weaponShot the weapon shot being fired. Can be null.
     * @param dir        the direction of the shot.
     * @param pos        the starting position of the shot.
     */
    @SideOnly(Side.CLIENT)
    protected void sendShootTickToServer(World world, WeaponShot weaponShot, Vec3d dir, Vec3d pos) {
        PacketFirePlasmaShot packetFirePlasmaShot = new PacketFirePlasmaShot(Minecraft.getMinecraft().player.getEntityId(), pos, dir, weaponShot);
        ClientProxy.instance().getClientWeaponHandler().sendWeaponTickToServer(world, packetFirePlasmaShot);
    }

    @SideOnly(Side.CLIENT)
    public void addShootDelay(ItemStack weaponStack) {
        ClientProxy.instance().getClientWeaponHandler().addShootDelay(this, weaponStack);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasShootDelayPassed() {
        return ClientProxy.instance().getClientWeaponHandler().shootDelayPassed(this);
    }

    /**
     * This method is called each weapon update tick,
     * when the player is holding the weapon and is not in any GUI screen.
     * This is manly done for convenience.
     *
     * @param weapon         the weapon stack.
     * @param world          the shooter world.
     * @param entityPlayer   the player shooter.
     * @param sendServerTick should a server weapon tick be sent. This is tied up to {@link #manageClientServerTicks(World)}.
     */
    @SideOnly(Side.CLIENT)
    public void onShooterClientUpdate(ItemStack weapon, World world, EntityPlayer entityPlayer, boolean sendServerTick) {
        if (sendServerTick) {
            manageClientServerTicks(world);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onLeftClick(ItemStack weapon, EntityPlayer entityPlayer) {
        return leftClickFire;
    }
    //endregion

    //region Reloading, Heating and Cooling managment
    public void chargeFromEnergyPack(ItemStack weapon, EntityPlayer player) {
        if (!player.world.isRemote) {
            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack stack = player.inventory.mainInventory.get(i);
                if (!stack.isEmpty() && stack.getItem() instanceof IEnergyPack && stack.getCount() > 0) {
                    stack.shrink(1);
                    IEnergyStorage container = getStorage(weapon);
                    if (container instanceof EnergyContainer)
                        ((EnergyContainer) container).setEnergy(container.getEnergyStored() + ((IEnergyPack) stack.getItem()).getEnergyAmount(stack));
                    //player.inventory.inventoryChanged = true;
                    player.world.playSound(null, player.getPosition(), MatterOverdriveSounds.weaponsReload, SoundCategory.PLAYERS, 0.7f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
                    if (stack.getCount() <= 0) {
                        player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                    }
                    return;
                }
            }
        } else {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (!stack.isEmpty() && stack.getItem() instanceof IEnergyPack && stack.getCount() > 0) {
                    ClientProxy.instance().getClientWeaponHandler().addReloadDelay(this, 40);
                    MatterOverdrive.NETWORK.sendToServer(new PacketReloadEnergyWeapon());
                    return;
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack) || slotChanged;
    }

    protected void manageOverheat(ItemStack itemStack, World world, EntityLivingBase shooter) {
        if (getHeat(itemStack) >= getMaxHeat(itemStack)) {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventEnergyWeapon.Overheat(itemStack, shooter))) {
                setOverheated(itemStack, true);
                world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, MatterOverdriveSounds.weaponsOverheat, SoundCategory.PLAYERS, 1F, 1f);
                world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, MatterOverdriveSounds.weaponsOverheatAlarm, SoundCategory.PLAYERS, 1, 1);
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == MatterOverdriveEnchantments.overclock;
    }

    protected void manageCooling(ItemStack itemStack) {
        float heat = getHeat(itemStack);
        if (heat > 0) {
            float easing = MOEasing.Quart.easeOut(heat / getMaxHeat(itemStack), 0, 4, 1);
            float newHeat = heat - easing;
            if (newHeat < 0.001f) {
                newHeat = 0;
            }
            setHeat(itemStack, Math.max(0, newHeat));
        }

        if (isOverheated(itemStack)) {
            if (getHeat(itemStack) < 2) {
                setOverheated(itemStack, false);
            }
        }
    }
    //endregion

    //region Abstract Functions
    @SideOnly(Side.CLIENT)
    protected abstract void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos);

    protected abstract int getBaseEnergyUse(ItemStack item);

    protected abstract int getBaseMaxHeat(ItemStack item);

    public abstract float getWeaponBaseDamage(ItemStack weapon);

    public abstract float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed);

    public abstract boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter);

    /**
     * Returns the speed on the Phaser Blot or any other projectile shot by the weapon.
     * This is different than the {@link #getBaseShootCooldown(ItemStack)}.
     *
     * @param weapon  the weapon.
     * @param shooter the shooter.
     * @return the speed of the "bullet".
     */
    public abstract float getShotSpeed(ItemStack weapon, EntityLivingBase shooter);

    public abstract int getBaseShootCooldown(ItemStack weapon);

    public abstract float getBaseZoom(ItemStack weapon, EntityLivingBase shooter);

    /**
     * Called when a weapon is fired with a single Weapon Shot.
     * This is different then the {@link #onServerFire(ItemStack, EntityLivingBase, WeaponShot, Vec3d, Vec3d, int)} in that it's called only on the client side.
     *
     * @param weapon   the weapon.
     * @param shooter  the shooter.
     * @param position the starting position of the shot.
     * @param dir      the direction of the shot.
     * @param shot     the Weapon shot info of the shot.
     */
    @SideOnly(Side.CLIENT)
    public abstract void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot);

    /**
     * Called when a Phaser Bolt or another projectile has hit something.
     * This method is only called on the client side.
     *
     * @param hit    the projective hit information.
     * @param weapon the weapon.
     * @param world  the world.
     * @param amount the amount of "hit" there was by the projectile. This is mainly used to control the amount of particles spawned on hit.
     */
    @SideOnly(Side.CLIENT)
    public abstract void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount);
    //endregion


    //region Projectile
    public PlasmaBolt getDefaultProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {
        PlasmaBolt fire = new PlasmaBolt(shooter.world, shooter, position, dir, shot, getShotSpeed(weapon, shooter));
        fire.setWeapon(weapon);
        fire.setFireDamageMultiply(WeaponHelper.modifyStat(Reference.WS_FIRE_DAMAGE, weapon, 0));
        float explosionAmount = WeaponHelper.modifyStat(Reference.WS_EXPLOSION_DAMAGE, weapon, 0);
        if (explosionAmount > 0) {
            fire.setExplodeMultiply(getWeaponBaseDamage(weapon) * 0.3f * explosionAmount);
        }
        if (WeaponHelper.modifyStat(Reference.WS_RICOCHET, weapon, 0) == 1) {
            fire.markRicochetable();
        }

        return fire;
    }

    public PlasmaBolt spawnProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {
        PlasmaBolt fire = getDefaultProjectile(weapon, shooter, position, dir, shot);
        shooter.world.spawnEntity(fire);
        if (shooter.world.isRemote && shooter instanceof EntityPlayer) {
            ((ClientWeaponHandler) MatterOverdrive.PROXY.getWeaponHandler()).addPlasmaBolt(fire);
        }
        return fire;
    }
    //endregion

    //region Getters and setters
    public int getRange(ItemStack weapon) {
        int range = defaultRange;
        range = Math.round(modifyStatFromModules(Reference.WS_RANGE, weapon, range));
        range *= getCustomIntStat(weapon, CUSTOM_RANGE_MULTIPLY_TAG, 1);
        return range;
    }

    public int getShootCooldown(ItemStack weapon) {
        int shootCooldown = getCustomIntStat(weapon, CUSTOM_SPEED_TAG, getBaseShootCooldown(weapon));
        shootCooldown = (int) modifyStatFromModules(Reference.WS_FIRE_RATE, weapon, shootCooldown);
        shootCooldown *= getCustomFloatStat(weapon, CUSTOM_SPEED_MULTIPLY_TAG, 1);
        return shootCooldown;
    }

    @SideOnly(Side.CLIENT)
    public WeaponShot createClientShot(ItemStack weapon, EntityLivingBase shooter, boolean zoomed) {
        return ((ClientWeaponHandler) MatterOverdrive.PROXY.getWeaponHandler()).getNextShot(weapon, this, shooter, zoomed);
    }

    public WeaponShot createServerShot(ItemStack weaponStack, EntityLivingBase shooter, boolean zoomed) {
        return new WeaponShot(itemRand.nextInt(), getWeaponScaledDamage(weaponStack, shooter), getAccuracy(weaponStack, shooter, zoomed), WeaponHelper.getColor(weaponStack), getRange(weaponStack));
    }

    public float modifyStatFromModules(int statID, ItemStack weapon, float original) {
        return WeaponHelper.modifyStat(statID, weapon, original);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack) {
        return true;
    }

    public float getWeaponScaledDamage(ItemStack weapon, EntityLivingBase shooter) {
        float damage = getCustomFloatStat(weapon, CUSTOM_DAMAGE_TAG, getWeaponBaseDamage(weapon));
        damage = modifyStatFromModules(Reference.WS_DAMAGE, weapon, damage);
        damage += damage * EnchantmentHelper.getEnchantmentLevel(MatterOverdriveEnchantments.overclock, weapon) * 0.04f;
        damage *= getCustomFloatStat(weapon, CUSTOM_DAMAGE_MULTIPLY_TAG, 1);
        damage += shooter.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        return damage;
    }

    public DamageSource getDamageSource(ItemStack weapon, EntityPlayer player) {
        DamageSource damageInfo = new EntityDamageSourcePhaser(player);
        if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE, weapon)) {
            damageInfo.setFireDamage();
        } else if (WeaponHelper.hasStat(Reference.WS_HEAL, weapon)) {
            damageInfo.setMagicDamage();
        }

        if (WeaponHelper.hasStat(Reference.WS_EXPLOSION_DAMAGE, weapon)) {
            damageInfo.setExplosion();
        }
        return damageInfo;
    }

    public int getEnergyUse(ItemStack weapon) {
        float energyUse = modifyStatFromModules(Reference.WS_AMMO, weapon, getBaseEnergyUse(weapon));
        energyUse -= energyUse * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("unbreaking"), weapon) * 0.04f;
        return Math.max((int) energyUse, 0);
    }

    public void addHeat(ItemStack itemStack, int amount) {
        if (itemStack.hasTagCompound()) {
            setHeat(itemStack, getHeat(itemStack) + amount);
        }
    }

    public void setHeat(ItemStack item, float amount) {
        item.setTagInfo("heat", new NBTTagFloat(Math.min(amount, getMaxHeat(item) + 1)));
    }

    @Override
    public float getHeat(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getFloat("heat");
        }
        return 0;
    }

    @Override
    public float getMaxHeat(ItemStack weapon) {
        return modifyStatFromModules(Reference.WS_MAX_HEAT, weapon, getBaseMaxHeat(weapon));
    }

    public boolean isOverheated(ItemStack weapon) {
        return weapon.hasTagCompound() && weapon.getTagCompound().getBoolean("Overheated");
    }

    protected void setOverheated(ItemStack weapon, boolean overheated) {
        if (weapon.hasTagCompound()) {
            weapon.getTagCompound().setBoolean("Overheated", overheated);
        }
    }

    @Override
    public int getAmmo(ItemStack weapon) {
        return getStorage(weapon).getEnergyStored();
    }

    public float getAccuracy(ItemStack weapon, EntityLivingBase shooter, boolean zoomed) {
        float accuracy = getWeaponBaseAccuracy(weapon, zoomed);
        accuracy = getCustomFloatStat(weapon, CUSTOM_ACCURACY_TAG, accuracy);
        accuracy += (float) new Vec3d(shooter.motionX, shooter.motionY * 0.1, shooter.motionZ).lengthVector() * 10;
        accuracy *= shooter.isSneaking() ? 0.6f : 1;
        accuracy = modifyStatFromModules(Reference.WS_ACCURACY, weapon, accuracy);
        if (WeaponHelper.hasModule(Reference.MODULE_SIGHTS, weapon)) {
            ItemStack sights = WeaponHelper.getModuleAtSlot(Reference.MODULE_SIGHTS, weapon);
            if (!sights.isEmpty() && sights.getItem() instanceof IWeaponScope) {
                accuracy = ((IWeaponScope) sights.getItem()).getAccuracyModify(sights, weapon, zoomed, accuracy);
            }
        }
        accuracy *= getCustomFloatStat(weapon, CUSTOM_ACCURACY_MULTIPLY_TAG, 1);
        return accuracy;
    }

    public void removeAllCustomStats(ItemStack weapon) {
        if (weapon.hasTagCompound()) {
            weapon.getTagCompound().removeTag(CUSTOM_DAMAGE_TAG);
            weapon.getTagCompound().removeTag(CUSTOM_ACCURACY_TAG);
        }
    }

    public float getCustomFloatStat(ItemStack weapon, String name, float def) {
        if (weapon.hasTagCompound() && weapon.getTagCompound().hasKey(name, Constants.NBT.TAG_FLOAT)) {
            return weapon.getTagCompound().getFloat(name);
        }
        return def;
    }

    public int getCustomIntStat(ItemStack weapon, String name, int def) {
        if (weapon.hasTagCompound() && weapon.getTagCompound().hasKey(name, Constants.NBT.TAG_INT)) {
            return weapon.getTagCompound().getInteger(name);
        }
        return def;
    }

    @Override
    public int getMaxAmmo(ItemStack weapon) {
        return getStorage(weapon).getMaxEnergyStored();
    }

    public boolean needsRecharge(ItemStack weapon) {
        return !DrainEnergy(weapon, getShootCooldown(weapon), true);
    }

    protected boolean DrainEnergy(ItemStack item, float ticks, boolean simulate) {
        IEnergyStorage container = getStorage(item);
        int amount = MathHelper.ceil(getEnergyUse(item) * ticks);
        int hasEnergy = container.getEnergyStored();

        if (hasEnergy >= amount) {
            while (amount > 0) {
                if (container.extractEnergy(amount, true) > 0) {
                    amount -= container.extractEnergy(amount, simulate);
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public int getItemEnchantability() {
        return ToolMaterial.IRON.getEnchantability();
    }

    @Override
    public float getZoomMultiply(EntityPlayer entityPlayer, ItemStack weapon) {
        if (WeaponHelper.hasModule(Reference.MODULE_SIGHTS, weapon)) {
            ItemStack sights = WeaponHelper.getModuleAtSlot(Reference.MODULE_SIGHTS, weapon);
            if (!sights.isEmpty() && sights.getItem() instanceof IWeaponScope) {
                return ((IWeaponScope) sights.getItem()).getZoomAmount(sights, weapon);
            }
        }
        return getBaseZoom(weapon, entityPlayer);
    }
    //endregion


    @Override
    public ICapabilityProvider createProvider(ItemStack stack) {
        return new EnergyProvider(stack, getCapacity(), getInput(), getOutput());
    }

    public static class EnergyProvider implements ICapabilityProvider {
        private EnergyContainer container;
        private ItemStack stack;

        public EnergyProvider(ItemStack stack, int capacity, int in, int out) {
            this.stack = stack;
            this.container = new EnergyContainer(capacity, in, out).setItemStack(stack);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability != CapabilityEnergy.ENERGY) {
                return null;
            }

            if (!stack.isEmpty()) {
                ItemStack battery = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, stack);

                if (battery.hasCapability(capability, null)) {
                    return battery.getCapability(capability, null);
                }
            }

            return CapabilityEnergy.ENERGY.cast(container);
        }
    }
}