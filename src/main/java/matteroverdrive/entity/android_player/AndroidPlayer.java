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

package matteroverdrive.entity.android_player;

import com.google.common.collect.Multimap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IAndroid;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.events.MOEventAndroid;
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.gui.GuiAndroidHud;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.network.packet.client.PacketAndroidTransformation;
import matteroverdrive.network.packet.client.PacketSendAndroidEffects;
import matteroverdrive.network.packet.client.PacketSendMinimapInfo;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.server.PacketAndroidChangeAbility;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Simeon on 5/26/2015.
 */
public class AndroidPlayer implements IEnergyStorage, IAndroid {
    public static final int EFFECT_GLITCH_TIME = 0;
    public static final int EFFECT_CLOAKED = 1;
    public static final int EFFECT_SHIELD = 2;
    public static final int EFFECT_SHIELD_LAST_USE = 3;
    public static final int EFFECT_NIGHTVISION = 4;
    public static final int EFFECT_SHOCK_LAST_USE = 5;
    public static final int EFFECT_LAST_TELEPORT = 6;
    public static final int EFFECT_WIRELESS_CHARGING = 7;
    public static final int EFFECT_HIGH_JUMP = 8;
    public static final int EFFECT_TURNNING = 9;
    public static final int EFFECT_ITEM_MAGNET = 10;
    public static final String NBT_ACTIVE_ABILITY = "AA";
    public static final String NBT_STATS = "S";
    public final static short TRANSFORM_TIME = 20 * 34;
    public final static int MINIMAP_SEND_TIMEOUT = 20 * 2;
    private final static int BUILTIN_ENERGY_TRANSFER = 1024;
    private final static int ENERGY_WATCHER_DEFAULT = 29;
    private final static int ENERGY_PER_JUMP = 512;
    private final static AttributeModifier outOfPowerSpeedModifier = new AttributeModifier(UUID.fromString("ec778ddc-9711-498b-b9aa-8e5adc436e00"), "Android Out of Power", -0.5, 2).setSaved(false);
    private static final List<IBioticStat> wheelStats = new ArrayList<>();
    private static final Map<Integer, MinimapEntityInfo> entityInfoMap = new HashMap<>();
    @CapabilityInject(AndroidPlayer.class)
    public static Capability<AndroidPlayer> CAPABILITY;
    private static int RECHARGE_AMOUNT_ON_RESPAWN = 64000;
    private static boolean HURT_GLITCHING = true;
    private static DataParameter<Integer> ENERGY;
    private static boolean TRANSFORMATION_DEATH = true;
    private static boolean REMOVE_POTION_EFFECTS = true;
    private static List<String> POTION_REMOVAL_BLACKLIST = new ArrayList<>();
    private final int ENERGY_SLOT;
    private final Inventory inventory;
    private NonNullList<ItemStack> previousBionicParts = NonNullList.withSize(5, ItemStack.EMPTY);
    private EntityPlayer player;
    private IBioticStat activeStat;
    private NBTTagCompound unlocked;
    private int maxEnergy;
    private boolean isAndroid;
    private boolean hasRunOutOfPower;
    private AndroidEffects androidEffects;

    public AndroidPlayer(EntityPlayer player) {
        this.maxEnergy = 512000;
        inventory = new Inventory("Android");
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_HEAD));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_ARMS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_LEGS));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_CHEST));
        inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_OTHER));
        ENERGY_SLOT = inventory.AddSlot(new EnergySlot(false));
        unlocked = new NBTTagCompound();
        androidEffects = new AndroidEffects(this);
        registerEffects(androidEffects);

        init(player);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(AndroidPlayer.class, new Capability.IStorage<AndroidPlayer>() {
            @Override
            public NBTBase writeNBT(Capability<AndroidPlayer> capability, AndroidPlayer instance, EnumFacing side) {
                NBTTagCompound data = new NBTTagCompound();
                instance.writeToNBT(data, EnumSet.allOf(IAndroid.DataType.class));
                return data;
            }

            @Override
            public void readNBT(Capability<AndroidPlayer> capability, AndroidPlayer instance, EnumFacing side, NBTBase nbt) {
                instance.readFromNBT((NBTTagCompound) nbt, EnumSet.allOf(IAndroid.DataType.class));
            }
        }, AndroidPlayer.class);
    }

    public static void loadConfigs(ConfigurationHandler configurationHandler) {
        TRANSFORMATION_DEATH = configurationHandler.getBool("transformation_death", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, true, "Should the player die after an Android transformation");
        REMOVE_POTION_EFFECTS = configurationHandler.getBool("remove_potion_effects", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, true, "Remove all potion effects while an Android");
        HURT_GLITCHING = configurationHandler.getBool("hurt_glitching", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, true, "Should the glitch effect be displayed every time the player gets hurt");
        RECHARGE_AMOUNT_ON_RESPAWN = configurationHandler.getInt("recharge_amount_on_respawn", ConfigurationHandler.CATEGORY_ANDROID_PLAYER, RECHARGE_AMOUNT_ON_RESPAWN, "How much does the android player recharge after respawning");
        POTION_REMOVAL_BLACKLIST = Arrays.asList(configurationHandler.getStringList(ConfigurationHandler.CATEGORY_ANDROID_PLAYER, "potion_removal_blacklist", "Collection of potion ids that won't get removed while being an android. Example: minecraft:wither"));
    }

    public static boolean isVisibleOnMinimap(EntityLivingBase entityLivingBase, EntityPlayer player, Vec3d relativePosition) {
        return !entityLivingBase.isInvisible() && Math.abs(relativePosition.y) < 16 && isInRangeToRenderDist(entityLivingBase, 256);
    }

    private static boolean isInRangeToRenderDist(EntityLivingBase entity, double distance) {
        double d1 = entity.getEntityBoundingBox().getAverageEdgeLength() * 64;
        return distance < d1 * d1;
    }

    @SideOnly(Side.CLIENT)
    public static void setMinimapEntityInfo(List<MinimapEntityInfo> entityInfo) {
        entityInfoMap.clear();
        for (MinimapEntityInfo info : entityInfo) {
            entityInfoMap.put(info.getEntityID(), info);
        }
    }

    @SideOnly(Side.CLIENT)
    public static MinimapEntityInfo getMinimapEntityInfo(EntityLivingBase entityLivingBase) {
        return entityInfoMap.get(entityLivingBase.getEntityId());
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    public void init(EntityPlayer player) {
        if (ENERGY == null) {
            ENERGY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
        }
        player.getDataManager().register(ENERGY, maxEnergy);
        registerAttributes(player);
        this.player = player;
    }

    /*@Override
	public void init(Entity entity, World world)
    {
        manageStatAttributeModifiers();
        manageEquipmentAttributeModifiers();
    }*/

    private void registerEffects(AndroidEffects effects) {
        effects.registerEffect(EFFECT_GLITCH_TIME, 0, true, false);
        effects.registerEffect(EFFECT_CLOAKED, false, true, true);
        effects.registerEffect(EFFECT_SHIELD, false, true, true);
        effects.registerEffect(EFFECT_SHIELD_LAST_USE, 0L, true, false);
        effects.registerEffect(EFFECT_NIGHTVISION, false, true, false);
        effects.registerEffect(EFFECT_SHOCK_LAST_USE, 0L, true, false);
        effects.registerEffect(EFFECT_LAST_TELEPORT, 0L, true, false);
        effects.registerEffect(EFFECT_WIRELESS_CHARGING, false, true, false);
        effects.registerEffect(EFFECT_HIGH_JUMP, false, true, false);
        effects.registerEffect(EFFECT_TURNNING, (short) -1, true, false);
        effects.registerEffect(EFFECT_ITEM_MAGNET, false, true, false);
    }

    private void registerAttributes(EntityPlayer player) {
        player.getAttributeMap().registerAttribute(AndroidAttributes.attributeGlitchTime);
        player.getAttributeMap().registerAttribute(AndroidAttributes.attributeBatteryUse);
    }

    public void writeToNBT(NBTTagCompound compound, EnumSet<DataType> dataTypes) {
        NBTTagCompound prop = new NBTTagCompound();
        if (dataTypes.contains(DataType.ENERGY)) {
            prop.setInteger("Energy", player.getDataManager().get(ENERGY));
            prop.setInteger("MaxEnergy", this.maxEnergy);
        }
        if (dataTypes.contains(DataType.DATA)) {
            prop.setBoolean("isAndroid", isAndroid);
        }
        if (dataTypes.contains(DataType.STATS)) {
            prop.setTag(NBT_STATS, unlocked);
        }
        NBTTagCompound effects = new NBTTagCompound();
        getAndroidEffects().writeToNBT(effects);
        prop.setTag("effects", effects);

        if (dataTypes.contains(DataType.ACTIVE_ABILITY)) {
            if (activeStat != null) {
                prop.setString(NBT_ACTIVE_ABILITY, activeStat.getUnlocalizedName());
            }
        }
        if (dataTypes.contains(DataType.INVENTORY)) {
            inventory.writeToNBT(prop, true);
        } else if (dataTypes.contains(DataType.BATTERY)) {
            if (!inventory.getStackInSlot(ENERGY_SLOT).isEmpty()) {
                NBTTagCompound batteryTag = new NBTTagCompound();
                inventory.getStackInSlot(ENERGY_SLOT).writeToNBT(batteryTag);
                compound.setTag("Battery", batteryTag);
            }
        }
        compound.setTag(EXT_PROP_NAME, prop);
    }

    public void readFromNBT(NBTTagCompound compound, EnumSet<DataType> dataTypes) {
        NBTTagCompound prop = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        if (prop != null) {
            boolean initFlag = false;
            if (dataTypes.contains(DataType.ENERGY)) {
                player.getDataManager().set(ENERGY, prop.getInteger("Energy"));
                this.maxEnergy = prop.getInteger("MaxEnergy");
            }
            if (dataTypes.contains(DataType.DATA)) {
                this.isAndroid = prop.getBoolean("isAndroid");
                initFlag = true;
            }
            if (dataTypes.contains(DataType.STATS)) {
                unlocked = prop.getCompoundTag(NBT_STATS);
            }
            if (dataTypes.contains(DataType.EFFECTS)) {
                NBTTagCompound effects = prop.getCompoundTag("effects");
                getAndroidEffects().readFromNBT(effects);
            }
            if (dataTypes.contains(DataType.ACTIVE_ABILITY)) {
                if (prop.hasKey(NBT_ACTIVE_ABILITY)) {
                    activeStat = MatterOverdrive.STAT_REGISTRY.getStat(prop.getString(NBT_ACTIVE_ABILITY));
                }
            }
            if (dataTypes.contains(DataType.INVENTORY)) {
                this.inventory.clearItems();
                this.inventory.readFromNBT(prop);
                initFlag = true;
            } else if (dataTypes.contains(DataType.BATTERY)) {
                inventory.setInventorySlotContents(ENERGY_SLOT, ItemStack.EMPTY);
                if (compound.hasKey("Battery", Constants.NBT.TAG_COMPOUND)) {
                    ItemStack battery = new ItemStack(compound.getCompoundTag("Battery"));
                    inventory.setInventorySlotContents(ENERGY_SLOT, battery);
                }
            }
            if (initFlag) {
                //init(this.player, this.player.world);
            }
        }
    }

    public int extractEnergyRaw(int amount, boolean simulate) {
        int energyExtracted;
        if (player.capabilities.isCreativeMode) {
            return amount;
        }

        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).hasCapability(CapabilityEnergy.ENERGY, null)) {
            ItemStack battery = getStackInSlot(ENERGY_SLOT);
            IEnergyStorage energyContainerItem = battery.getCapability(CapabilityEnergy.ENERGY, null);
            energyExtracted = energyContainerItem.extractEnergy(amount, simulate);
            if (energyExtracted > 0 && !simulate) {
                sync(EnumSet.of(DataType.BATTERY));
            }
        } else {
            int energy = this.player.getDataManager().get(ENERGY);
            energyExtracted = Math.min(Math.min(energy, amount), BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy -= energyExtracted;
                energy = MathHelper.clamp(energy, 0, getMaxEnergyStored());
                this.player.getDataManager().set(ENERGY, energy);
            }
        }

        return energyExtracted;
    }

    public void extractEnergyScaled(int amount) {
        double percent = getPlayer().getAttributeMap().getAttributeInstance(AndroidAttributes.attributeBatteryUse).getAttributeValue();
        extractEnergyRaw((int) (amount * percent), false);
    }

    public boolean hasEnoughEnergyScaled(int energy) {
        double percent = getPlayer().getAttributeMap().getAttributeInstance(AndroidAttributes.attributeBatteryUse).getAttributeValue();
        int newEnergy = (int) Math.ceil(energy * percent);
        return extractEnergyRaw(energy, true) >= newEnergy;
    }

    @Override
    public boolean isUnlocked(IBioticStat stat, int level) {
        return unlocked.hasKey(stat.getUnlocalizedName()) && unlocked.getInteger(stat.getUnlocalizedName()) >= level;
    }

    @Override
    public int getUnlockedLevel(IBioticStat stat) {
        if (unlocked.hasKey(stat.getUnlocalizedName())) {
            return unlocked.getInteger(stat.getUnlocalizedName());
        }
        return 0;
    }

    public boolean tryUnlock(IBioticStat stat, int level) {
        if (stat.canBeUnlocked(this, level)) {
            unlock(stat, level);
            return true;
        }

        return false;
    }

    public void unlock(IBioticStat stat, int level) {
        clearAllStatAttributeModifiers();
        this.unlocked.setInteger(stat.getUnlocalizedName(), level);
        stat.onUnlock(this, level);
        sync(EnumSet.of(DataType.STATS));
        manageStatAttributeModifiers();
    }

    @Override
    public int getEnergyStored() {
        if (player.capabilities.isCreativeMode) {
            return getMaxEnergyStored();
        }


        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).hasCapability(CapabilityEnergy.ENERGY, null)) {
            return (getStackInSlot(ENERGY_SLOT).getCapability(CapabilityEnergy.ENERGY, null)).getEnergyStored();
        } else {
            return this.player.getDataManager().get(ENERGY);
        }
    }

    @Override
    public int getMaxEnergyStored() {
        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).hasCapability(CapabilityEnergy.ENERGY, null)) {
            return (getStackInSlot(ENERGY_SLOT).getCapability(CapabilityEnergy.ENERGY, null)).getMaxEnergyStored();
        } else {
            return maxEnergy;
        }
    }

    public int receiveEnergy(int amount, boolean simulate) {
        int energyReceived;
        if (getStackInSlot(ENERGY_SLOT) != null && getStackInSlot(ENERGY_SLOT).hasCapability(CapabilityEnergy.ENERGY, null)) {
            ItemStack battery = getStackInSlot(ENERGY_SLOT);
            IEnergyStorage energyContainerItem = battery.getCapability(CapabilityEnergy.ENERGY, null);
            energyReceived = energyContainerItem.receiveEnergy(amount, simulate);
            sync(EnumSet.of(DataType.BATTERY));
        } else {
            int energy = this.player.getDataManager().get(ENERGY);
            energyReceived = Math.min(Math.min(getMaxEnergyStored() - energy, amount), BUILTIN_ENERGY_TRANSFER);

            if (!simulate) {
                energy += energyReceived;
                energy = MathHelper.clamp(energy, 0, getMaxEnergyStored());
                this.player.getDataManager().set(ENERGY, energy);
            }
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return extractEnergyRaw(maxExtract, simulate);
    }

    @Override
    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean isAndroid) {
        this.isAndroid = isAndroid;
        sync(EnumSet.allOf(DataType.class));
        if (isAndroid) {
            previousBionicParts.clear();
            manageStatAttributeModifiers();
        } else {
            clearAllStatAttributeModifiers();
            clearAllEquipmentAttributeModifiers();
        }
    }

    public void sync(EnumSet<DataType> part) {
        this.sync(player, part, false);
    }

    public void sync(EnumSet<DataType> part, boolean others) {
        this.sync(player, part, others);
    }

    public void sync(EntityPlayer player, EnumSet<DataType> syncPart, boolean toOthers) {
        if (player instanceof EntityPlayerMP) {
            if (toOthers) {
                MatterOverdrive.NETWORK.sendToAllAround(new PacketSyncAndroid(this, syncPart), player, 64);
            } else {
                MatterOverdrive.NETWORK.sendTo(new PacketSyncAndroid(this, syncPart), (EntityPlayerMP) player);
            }
        }
    }

    public void copy(AndroidPlayer player) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.writeToNBT(tagCompound, EnumSet.allOf(IAndroid.DataType.class));
        readFromNBT(tagCompound, EnumSet.allOf(IAndroid.DataType.class));
        manageStatAttributeModifiers();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int resetUnlocked() {
        int xp = getResetXPRequired();
        this.unlocked = new NBTTagCompound();
        sync(EnumSet.of(DataType.STATS));
        clearAllStatAttributeModifiers();
        return xp;
    }

    public int getResetXPRequired() {
        int calculatedXP = 0;
        for (Object key : this.unlocked.getKeySet()) {
            IBioticStat stat = MatterOverdrive.STAT_REGISTRY.getStat(key.toString());
            int unlocked = this.unlocked.getInteger(key.toString());
            calculatedXP += stat.getXP(this, unlocked);
        }
        return calculatedXP / 2;
    }

    public void reset(IBioticStat stat) {
        if (unlocked.hasKey(stat.getUnlocalizedName())) {
            int level = unlocked.getInteger(stat.getUnlocalizedName());
            stat.onUnlearn(this, level);
            unlocked.removeTag(stat.getUnlocalizedName());
            sync(EnumSet.of(DataType.STATS));
            manageStatAttributeModifiers();
        }
    }

    public void onAndroidTick(Side side) {
        if (side.isServer()) {
            if (isAndroid()) {
                if (getEnergyStored() > 0) {
                    manageHasPower();
                    managePotionEffects();
                    if (hasRunOutOfPower) {
                        manageStatAttributeModifiers();
                        hasRunOutOfPower = false;
                    }
                } else if (getEnergyStored() <= 0) {
                    if (!hasRunOutOfPower) {
                        manageStatAttributeModifiers();
                        hasRunOutOfPower = true;
                    }
                    manageOutOfPower();
                }

                manageCharging();
                manageEquipmentAttributeModifiers();

                if (!getPlayer().world.isRemote) {
                    manageMinimapInfo();
                }
            }

            manageTurning();
            manageEffects();
        }
        if (side.isClient() && isAndroid()) {
            manageAbilityWheel();
            manageSwimming();
        }

        if (isAndroid()) {
            manageGlitch();

            for (IBioticStat stat : MatterOverdrive.STAT_REGISTRY.getStats()) {
                int unlockedLevel = getUnlockedLevel(stat);
                if (unlockedLevel > 0) {
                    if (stat.isEnabled(this, unlockedLevel)) {
                        stat.changeAndroidStats(this, unlockedLevel, true);
                        stat.onAndroidUpdate(this, unlockedLevel);
                    } else {
                        stat.changeAndroidStats(this, unlockedLevel, false);
                    }
                }
            }
        }
    }

    private void manageEffects() {
        if (androidEffects.haveEffectsChanged()) {
            List<AndroidEffects.Effect> changedEffects = androidEffects.getChanged();
            MatterOverdrive.NETWORK.sendTo(new PacketSendAndroidEffects(player.getEntityId(), changedEffects), (EntityPlayerMP) player);

            List<AndroidEffects.Effect> othersEffects = new ArrayList<>();
            for (AndroidEffects.Effect effect : changedEffects) {
                if (effect.isSendToOthers()) {
                    othersEffects.add(effect);
                }
            }
            Set<? extends EntityPlayer> trackers = ((EntityPlayerMP) player).getServerWorld().getEntityTracker().getTrackingPlayers(player);
            for (EntityPlayer entityPlayer : trackers) {
                if (entityPlayer instanceof EntityPlayerMP && entityPlayer != player) {
                    MatterOverdrive.NETWORK.sendTo(new PacketSendAndroidEffects(player.getEntityId(), othersEffects), (EntityPlayerMP) entityPlayer);
                }
            }
        }
    }

    private void clearAllEquipmentAttributeModifiers() {
        for (int j = 0; j < 5; ++j) {
            ItemStack itemstack1 = this.inventory.getStackInSlot(j);

            if (!itemstack1.isEmpty() && itemstack1.getItem() instanceof IBionicPart) {
                Multimap multimap = ((IBionicPart) itemstack1.getItem()).getModifiers(this, itemstack1);
                if (multimap != null) {
                    player.getAttributeMap().removeAttributeModifiers(multimap);
                }
            }
        }
    }

    private void manageEquipmentAttributeModifiers() {
        boolean needsSync = false;

        for (int j = 0; j < 5; ++j) {
            ItemStack itemstack = this.previousBionicParts.get(j);
            ItemStack itemstack1 = this.inventory.getStackInSlot(j);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                //((WorldServer)player.world).getEntityTracker().func_151247_a(player, new S04PacketEntityEquipment(player.getEntityId(), j, itemstack1));

                if (!itemstack.isEmpty() && itemstack.getItem() instanceof IBionicPart) {
                    Multimap multimap = ((IBionicPart) itemstack.getItem()).getModifiers(this, itemstack);
                    if (multimap != null) {
                        player.getAttributeMap().removeAttributeModifiers(multimap);
                    }
                }

                if (!itemstack1.isEmpty() && itemstack1.getItem() instanceof IBionicPart) {
                    Multimap multimap = ((IBionicPart) itemstack1.getItem()).getModifiers(this, itemstack1);
                    if (multimap != null) {
                        player.getAttributeMap().applyAttributeModifiers(multimap);
                    }
                }

                this.previousBionicParts.set(j, itemstack1.copy());
                needsSync = true;
            }
        }

        if (needsSync) {
            sync(EnumSet.of(DataType.INVENTORY), true);
        }
    }

    public void updateStatModifyers(IBioticStat stat) {
        int unlockedLevel = getUnlockedLevel(stat);
        Multimap<String, AttributeModifier> multimap = stat.attributes(this, unlockedLevel);
        if (multimap != null) {
            if (isAndroid()) {
                if (unlockedLevel > 0) {
                    if (stat.isEnabled(this, unlockedLevel)) {
                        player.getAttributeMap().applyAttributeModifiers(multimap);
                    } else {
                        player.getAttributeMap().removeAttributeModifiers(multimap);
                    }
                } else {
                    player.getAttributeMap().removeAttributeModifiers(multimap);
                }
            } else {
                player.getAttributeMap().removeAttributeModifiers(multimap);
            }
        }
    }

    private void clearAllStatAttributeModifiers() {
        for (IBioticStat stat : MatterOverdrive.STAT_REGISTRY.getStats()) {
            int unlockedLevel = getUnlockedLevel(stat);
            Multimap<String, AttributeModifier> multimap = stat.attributes(this, unlockedLevel);
            if (multimap != null) {
                player.getAttributeMap().removeAttributeModifiers(multimap);
            }
        }
    }

    private void manageStatAttributeModifiers() {
        MatterOverdrive.STAT_REGISTRY.getStats().forEach(this::updateStatModifyers);
    }

    private void manageMinimapInfo() {
        if (getPlayer() instanceof EntityPlayerMP && getPlayer().world.getWorldTime() % MINIMAP_SEND_TIMEOUT == 0) {
            List<MinimapEntityInfo> entityList = new ArrayList<>();
            for (Object entityObject : getPlayer().world.loadedEntityList) {
                if (entityObject instanceof EntityLivingBase) {
                    if (isVisibleOnMinimap((EntityLivingBase) entityObject, player, new Vec3d(((EntityLivingBase) entityObject).posX, ((EntityLivingBase) entityObject).posY, ((EntityLivingBase) entityObject).posZ).subtract(new Vec3d(player.posX, player.posY, player.posZ))) && MinimapEntityInfo.hasInfo((EntityLivingBase) entityObject, player)) {
                        entityList.add(new MinimapEntityInfo((EntityLivingBase) entityObject, getPlayer()));
                    }
                }
            }

            if (entityList.size() > 0) {
                MatterOverdrive.NETWORK.sendTo(new PacketSendMinimapInfo(entityList), (EntityPlayerMP) getPlayer());
            }
        }
    }

    private void manageOutOfPower() {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (speed.getModifier(outOfPowerSpeedModifier.getID()) == null) {
            speed.applyModifier(outOfPowerSpeedModifier);
        }

        if (player.world.getWorldTime() % 60 == 0) {
            androidEffects.updateEffect(EFFECT_GLITCH_TIME, 5);
            playGlitchSound(this, player.getRNG(), 0.2f);
        }
    }

    private void manageHasPower() {
        IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (speed.getModifier(outOfPowerSpeedModifier.getID()) != null) {
            speed.removeModifier(outOfPowerSpeedModifier);
        }
    }

    private void manageCharging() {
        //// TODO: 3/24/2016 Add support for off hand
        if (player != null && player.isSneaking() && !player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == MatterOverdrive.ITEMS.battery || player.getHeldItem(EnumHand.MAIN_HAND).getItem() == MatterOverdrive.ITEMS.hc_battery)) {
            int freeEnergy = getMaxEnergyStored() - getEnergyStored();
            int receivedAmount = (player.getHeldItem(EnumHand.MAIN_HAND).getCapability(CapabilityEnergy.ENERGY, null)).extractEnergy(freeEnergy, false);
            receiveEnergy(receivedAmount, false);
        }
    }

    @SideOnly(Side.CLIENT)
    private void manageAbilityWheel() {
        GuiAndroidHud.showRadial = ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_SWITCH_KEY).isKeyDown();

        if (GuiAndroidHud.showRadial) {
            double mag = Math.sqrt(GuiAndroidHud.radialDeltaX * GuiAndroidHud.radialDeltaX + GuiAndroidHud.radialDeltaY * GuiAndroidHud.radialDeltaY);
            double magAcceptance = 0.2D;

            double radialAngle = -720F;
            if (mag > magAcceptance) {
                double aSin = Math.toDegrees(Math.asin(GuiAndroidHud.radialDeltaX));

                if (GuiAndroidHud.radialDeltaY >= 0 && GuiAndroidHud.radialDeltaX >= 0) {
                    radialAngle = aSin;
                } else if (GuiAndroidHud.radialDeltaY < 0 && GuiAndroidHud.radialDeltaX >= 0) {
                    radialAngle = 90D + (90D - aSin);
                } else if (GuiAndroidHud.radialDeltaY < 0 && GuiAndroidHud.radialDeltaX < 0) {
                    radialAngle = 180D - aSin;
                } else if (GuiAndroidHud.radialDeltaY >= 0 && GuiAndroidHud.radialDeltaX < 0) {
                    radialAngle = 270D + (90D + aSin);
                }
            }

            if (mag > 0.9999999D) {
                mag = Math.round(mag);
            }

            wheelStats.clear();
            wheelStats.addAll(MatterOverdrive.STAT_REGISTRY.getStats().stream()
                    .filter(stat -> stat.showOnWheel(this, getUnlockedLevel(stat)) && isUnlocked(stat, 0))
                    .collect(Collectors.toList()));

            if (mag > magAcceptance) {
                GuiAndroidHud.radialAngle = radialAngle;
            }

            if (wheelStats.size() <= 0) {
                GuiAndroidHud.showRadial = false;
                return;
            }

            int i = 0;
            for (IBioticStat stat : wheelStats) {
                float leeway = 360f / wheelStats.size();
                if (mag > magAcceptance && (i == 0 && (radialAngle < (leeway / 2) && radialAngle >= 0F || radialAngle > (360F) - (leeway / 2)) || i != 0 && radialAngle < (leeway * i) + (leeway / 2) && radialAngle > (leeway * i) - (leeway / 2))) {
                    if (activeStat != stat) {
                        activeStat = stat;
                        MatterOverdrive.NETWORK.sendToServer(new PacketAndroidChangeAbility(activeStat.getUnlocalizedName()));
                    }
                    break;
                }
                i++;
            }

        }
    }

    public void startConversion() {
        if (!MinecraftForge.EVENT_BUS.post(new MOEventAndroid.Transformation(this))) {
            if (player.world.isRemote) {
                playTransformMusic();
            } else {
                if (!isAndroid() && !isTurning()) {
                    AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
                    androidPlayer.startTurningToAndroid();
                    if (player instanceof EntityPlayerMP) {
                        MatterOverdrive.NETWORK.sendTo(new PacketAndroidTransformation(), (EntityPlayerMP) player);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void playTransformMusic() {
        MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.musicTransformation, SoundCategory.MUSIC, 1, 1);
        sound.setAttenuationType(ISound.AttenuationType.NONE);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
    }

    private void managePotionEffects() {
        if (isAndroid() && REMOVE_POTION_EFFECTS) {
            for (PotionEffect potionEffect : new ArrayList<>(player.getActivePotionEffects())) {
                if (!POTION_REMOVAL_BLACKLIST.contains(potionEffect.getPotion().getRegistryName().toString())){
                    player.removePotionEffect(potionEffect.getPotion());
                }
            }
        }
    }

    public double getSpeedMultiply() {
        return player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
    }

    private void manageGlitch() {
        if (androidEffects.getEffectInt(EFFECT_GLITCH_TIME) > 0) {
            androidEffects.updateEffect(EFFECT_GLITCH_TIME, androidEffects.getEffectInt(EFFECT_GLITCH_TIME) - 1);
        }
    }

    private int modify(int amount, IAttribute attribute) {
        IAttributeInstance glitchAttribute = player.getEntityAttribute(attribute);
        if (glitchAttribute != null) {
            amount = (int) (amount * glitchAttribute.getAttributeValue());
        }
        return amount;
    }

    public float modify(float amount, IAttribute attribute) {
        IAttributeInstance glitchAttribute = player.getEntityAttribute(attribute);
        if (glitchAttribute != null) {
            amount = (int) (amount * glitchAttribute.getAttributeValue());
        }
        return amount;
    }

    private void manageSwimming() {
        if (player.isInWater()) {
            if (!isUnlocked(OverdriveBioticStats.flotation, 1) || !OverdriveBioticStats.flotation.isEnabled(this, 1)) {
                if (player.motionY > 0) {
                    player.motionY -= 0.06;
                }
            }
        }
    }

    private void manageTurning() {
        short turnningTime = getAndroidEffects().getEffectShort(EFFECT_TURNNING);
        if (turnningTime > 0) {
            DamageSource fake = new DamageSource("android_transformation");
            fake.setDamageIsAbsolute();
            fake.setDamageBypassesArmor();

            getAndroidEffects().updateEffect(EFFECT_TURNNING, --turnningTime);
            //getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionById(9), AndroidPlayer.TRANSFORM_TIME));
            getPlayer().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, AndroidPlayer.TRANSFORM_TIME, 1));
            getPlayer().addPotionEffect(new PotionEffect(MobEffects.HUNGER, AndroidPlayer.TRANSFORM_TIME));
            getPlayer().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, AndroidPlayer.TRANSFORM_TIME));
            getPlayer().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, AndroidPlayer.TRANSFORM_TIME));


            if (turnningTime % 40 == 0) {
                player.attackEntityFrom(fake, 0.1f);
                playGlitchSound(this, player.world.rand, 0.2f);
            }

            if (turnningTime <= 0) {
                setAndroid(true);
                playGlitchSound(this, player.world.rand, 0.8f);
                if (!player.capabilities.isCreativeMode && !player.world.getWorldInfo().isHardcoreModeEnabled() && TRANSFORMATION_DEATH) {
                    player.attackEntityFrom(fake, Integer.MAX_VALUE);
                    player.setDead();
                }
            }

            sync(EnumSet.of(DataType.EFFECTS));
        }
    }

    private void playGlitchSound(AndroidPlayer player, Random random, float amount) {
        player.getPlayer().world.playSound(player.getPlayer().posX, player.getPlayer().posY, player.getPlayer().posZ, MatterOverdriveSounds.guiGlitch, SoundCategory.PLAYERS, amount, 0.9f + random.nextFloat() * 0.2f, true);
    }

    @SideOnly(Side.CLIENT)
    private void playGlitchSoundClient(Random random, float amount) {
        player.world.playSound(null, player.posX, player.posY, player.posZ, MatterOverdriveSounds.guiGlitch, SoundCategory.PLAYERS, amount, 0.9f + random.nextFloat() * 0.2f);
    }

    @Override
    public boolean isTurning() {
        return getAndroidEffects().getEffectShort(EFFECT_TURNNING) > (short) 0;
    }

    //region Events
    public void triggerEventOnStats(LivingEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityLiving());

            if (androidPlayer.isAndroid()) {
                for (IBioticStat stat : MatterOverdrive.STAT_REGISTRY.getStats()) {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer, unlockedLevel)) {
                        stat.onLivingEvent(androidPlayer, unlockedLevel, event);
                    }
                }
            }
        }
    }

    public void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        manageStatAttributeModifiers();
    }

    public void onPlayerRespawn() {
        while (getEnergyStored() < RECHARGE_AMOUNT_ON_RESPAWN) {
            receiveEnergy(RECHARGE_AMOUNT_ON_RESPAWN, false);
        }
    }

    public void onEntityHurt(LivingHurtEvent event) {
        if (!event.isCanceled()) {
            if (HURT_GLITCHING && event.getAmount() > 0) {
                androidEffects.updateEffect(EFFECT_GLITCH_TIME, modify(10, AndroidAttributes.attributeGlitchTime));
                sync(EnumSet.of(DataType.EFFECTS));
                playGlitchSound(this, player.getRNG(), 0.2f);
            }

            triggerEventOnStats(event);
        }
    }

    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (!event.getEntity().world.isRemote) {
            extractEnergyScaled(ENERGY_PER_JUMP);
        }
    }
    //endregion

    public void onEntityFall(LivingFallEvent event) {
        triggerEventOnStats(event);
    }

    public void onWeaponEvent(MOEventEnergyWeapon eventEnergyWeapon) {
        triggerEventOnStats(eventEnergyWeapon);
    }

    //region getters and setters
    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    private void startTurningToAndroid() {
        getAndroidEffects().updateEffect(EFFECT_TURNNING, TRANSFORM_TIME);
        sync(EnumSet.of(DataType.EFFECTS));
    }

    //region Effect Getters and Setters
    public NBTTagCompound getUnlockedNBT() {
        return unlocked;
    }

    //endregion
    @Override
    public IBioticStat getActiveStat() {
        return activeStat;
    }
    //endregion

    public void setActiveStat(IBioticStat stat) {
        this.activeStat = stat;
    }

    @Override
    public void onEffectsUpdate(int effectId) {

    }

    //region inventory
    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        //sync(PacketSyncAndroid.SYNC_INVENTORY,true);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public AndroidEffects getAndroidEffects() {
        return androidEffects;
    }

    //endregion

}