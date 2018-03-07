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

package matteroverdrive.tile;

import com.astro.clib.network.CEvents;
import matteroverdrive.api.IScannable;
import matteroverdrive.api.events.anomaly.MOEventGravitationalAnomalyConsume;
import matteroverdrive.api.gravity.AnomalySuppressor;
import matteroverdrive.api.gravity.IGravitationalAnomaly;
import matteroverdrive.api.gravity.IGravityEntity;
import matteroverdrive.client.sound.GravitationalAnomalySound;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.fx.GravitationalAnomalyParticle;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.items.MatterItem;
import matteroverdrive.items.SpacetimeEqualizer;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.TimeTracker;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Simeon on 5/11/2015.
 */
public class TileEntityGravitationalAnomaly extends MOTileEntity implements IScannable, IMOTickable, IGravitationalAnomaly, ITickable {
    public static final float MAX_VOLUME = 0.5f;
    public static final int BLOCK_DESTORY_DELAY = 6;
    public static final int MAX_BLOCKS_PER_HARVEST = 6;
    public static final int MAX_LIQUIDS_PER_HARVEST = 32;
    public static final double STREHGTH_MULTIPLYER = 0.00001;
    public static final double G = 6.67384;
    public static final double G2 = G * 2;
    public static final double C = 2.99792458;
    public static final double CC = C * C;
    public static boolean FALLING_BLOCKS = true;
    public static boolean BLOCK_ENTETIES = true;
    public static boolean VANILLA_FLUIDS = true;
    public static boolean FORGE_FLUIDS = true;
    public static boolean BLOCK_DESTRUCTION = true;
    public static boolean GRAVITATION = true;
    private final TimeTracker blockDestoryTimer;
    PriorityQueue<BlockPos> blocks;
    List<AnomalySuppressor> supressors;
    @SideOnly(Side.CLIENT)
    private GravitationalAnomalySound sound;
    private long mass;
    private float suppression;

    //region Constructors
    public TileEntityGravitationalAnomaly() {
        blockDestoryTimer = new TimeTracker();
        this.mass = 2048 + Math.round(Math.random() * 8192);
        supressors = new ArrayList<>();
    }

    public TileEntityGravitationalAnomaly(int mass) {
        this();
        this.mass = mass;
    }
    //endregion


    @Override
    public BlockPos getPosition() {
        return getPos();
    }

    //region Updates
    @Override
    public void update() {
        if (world.isRemote) {
            spawnParticles(world);
            manageSound();
            manageClientEntityGravitation(world);
        }
    }

    @Override
    public void onServerTick(TickEvent.Phase phase, World world) {
        if (world == null) {
            return;
        }

        if (phase.equals(TickEvent.Phase.END)) {
            float tmpSuppression = calculateSuppression();
            if (tmpSuppression != suppression) {
                suppression = tmpSuppression;
            }

            manageEntityGravitation(world, 0);
            manageBlockDestory(world);
        }
    }
    //endregion

    @SideOnly(Side.CLIENT)
    public void spawnParticles(World world) {
        double radius = (float) getBlockBreakRange();
        Vector3f point = MOMathHelper.randomSpherePoint(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, new Vec3d(radius, radius, radius), world.rand);
        GravitationalAnomalyParticle particle = new GravitationalAnomalyParticle(world, point.x, point.y, point.z, new Vec3d(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f));
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @SideOnly(Side.CLIENT)
    public void manageClientEntityGravitation(World world) {
        if (!GRAVITATION) {
            return;
        }

        double rangeSq = getMaxRange() + 1;
        rangeSq *= rangeSq;
        Vec3d blockPos = new Vec3d(getPos());
        blockPos.addVector(0.5, 0.5, 0.5);
        Vec3d entityPos = Minecraft.getMinecraft().player.getPositionVector();

        double distanceSq = entityPos.squareDistanceTo(blockPos);
        if (distanceSq < rangeSq) {
            if ((!Minecraft.getMinecraft().player.inventory.armorItemInSlot(2).isEmpty() && Minecraft.getMinecraft().player.inventory.armorItemInSlot(2).getItem() instanceof SpacetimeEqualizer)
                    || Minecraft.getMinecraft().player.capabilities.isCreativeMode
                    || MOPlayerCapabilityProvider.GetAndroidCapability(Minecraft.getMinecraft().player).isUnlocked(OverdriveBioticStats.equalizer, 0))
                return;

            double acceleration = getAcceleration(distanceSq);
            Vec3d dir = blockPos.subtract(entityPos).normalize();
            Minecraft.getMinecraft().player.addVelocity(dir.x * acceleration, dir.y * acceleration, dir.z * acceleration);
            Minecraft.getMinecraft().player.velocityChanged = true;
        }
    }

    public void manageEntityGravitation(World world, float ticks) {
        if (!GRAVITATION) {
            return;
        }

        double range = getMaxRange() + 1;
        AxisAlignedBB bb = new AxisAlignedBB(getPos().getX() - range, getPos().getY() - range, getPos().getZ() - range, getPos().getX() + range, getPos().getY() + range, getPos().getZ() + range);
        List entities = world.getEntitiesWithinAABB(Entity.class, bb);
        Vec3d blockPos = new Vec3d(getPos()).addVector(0.5, 0.5, 0.5);

        for (Object entityObject : entities) {
            if (entityObject instanceof Entity) {
                Entity entity = (Entity) entityObject;
                if (entity instanceof IGravityEntity) {
                    if (!((IGravityEntity) entity).isAffectedByAnomaly(this)) {
                        continue;
                    }
                }
                Vec3d entityPos = entity.getPositionVector();

                //pos.y += entity.getEyeHeight();
                double distanceSq = entityPos.squareDistanceTo(blockPos);
                double acceleration = getAcceleration(distanceSq);
                double eventHorizon = getEventHorizon();
                Vec3d dir = blockPos.subtract(entityPos).normalize();
                dir = new Vec3d(dir.x * acceleration, dir.y * acceleration, dir.z * acceleration);
                if (intersectsAnomaly(entityPos, dir, blockPos, eventHorizon)) {
                    consume(entity);
                }

                if (entityObject instanceof EntityPlayer) //Players handle this clientside, no need to run on the server for no reason
                    continue;

                if (entityObject instanceof EntityLivingBase) {
                    AtomicBoolean se = new AtomicBoolean(false);
                    ((EntityLivingBase) entityObject).getArmorInventoryList().forEach(i -> {
                        if (!i.isEmpty() && i.getItem() instanceof SpacetimeEqualizer)
                            se.set(true);
                    });
                    if (se.get())
                        continue;
                }

                entity.addVelocity(dir.x, dir.y, dir.z);
            }
        }
    }

    boolean intersectsAnomaly(Vec3d origin, Vec3d dir, Vec3d anomaly, double radius) {
        if (origin.distanceTo(anomaly) <= radius) {
            return true;
        } else {
            Vec3d intersectDir = origin.subtract(anomaly);
            double c = intersectDir.lengthVector();
            double v = intersectDir.dotProduct(dir);
            double d = radius * radius - (c * c - v * v);

            return d >= 0;
        }
    }

    //region Sounds
    @SideOnly(Side.CLIENT)
    public void stopSounds() {
        if (sound != null) {
            sound.stopPlaying();
            FMLClientHandler.instance().getClient().getSoundHandler().stopSound(sound);
            sound = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void playSounds() {
        if (sound == null) {
            sound = new GravitationalAnomalySound(MatterOverdriveSounds.windy, SoundCategory.BLOCKS, getPos(), 0.2f, getMaxRange());
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
        } else if (!FMLClientHandler.instance().getClient().getSoundHandler().isSoundPlaying(sound)) {
            stopSounds();
            sound = new GravitationalAnomalySound(MatterOverdriveSounds.windy, SoundCategory.BLOCKS, getPos(), 0.2f, getMaxRange());
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
        }
    }

    @SideOnly(Side.CLIENT)
    public void manageSound() {
        if (sound == null) {
            playSounds();
        } else {
            sound.setVolume(Math.min(MAX_VOLUME, getBreakStrength(0, (float) getMaxRange()) * 0.1f));
            sound.setRange(getMaxRange());
        }
    }
    //endregion

    //region Super Events

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner) {

    }

    public void onChunkUnload() {
        if (world.isRemote) {
            stopSounds();
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData, MachineNBTCategory.ALL_OPTS, false);
        return new SPacketUpdateTileEntity(getPos(), 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound syncData = pkt.getNbtCompound();
        if (syncData != null) {
            readCustomNBT(syncData, MachineNBTCategory.ALL_OPTS);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (world.isRemote) {
            stopSounds();
        }
    }
    //endregion

    //region Events
    private boolean onEntityConsume(Entity entity, boolean pre) {
        if (entity instanceof IGravityEntity) {
            ((IGravityEntity) entity).onEntityConsumed(this);
        }
        if (pre) {
            MinecraftForge.EVENT_BUS.post(new MOEventGravitationalAnomalyConsume.Pre(entity, getPos()));
        } else {
            MinecraftForge.EVENT_BUS.post(new MOEventGravitationalAnomalyConsume.Post(entity, getPos()));
            CEvents.markForUpdate(getPos(), this);
        }

        return true;
    }
    //endregion

    public void manageBlockDestory(World world) {
        if (!BLOCK_DESTRUCTION) {
            return;
        }

        int solidCount = 0;
        int liquidCount = 0;
        int range = (int) Math.floor(getBlockBreakRange());
        double distance;
        double eventHorizon = getEventHorizon();
        BlockPos blockPos;
        float hardness;
        IBlockState blockState;

        blocks = new PriorityQueue<>(1, new BlockComparitor(getPos()));

        if (blockDestoryTimer.hasDelayPassed(world, BLOCK_DESTORY_DELAY)) {
            for (int x = -range; x < range; x++) {
                for (int y = -range; y < range; y++) {
                    for (int z = -range; z < range; z++) {
                        blockPos = new BlockPos(getPos().getX() + x, getPos().getY() + y, getPos().getZ() + z);
                        blockState = world.getBlockState(blockPos);
                        distance = Math.sqrt(blockPos.distanceSq(getPos()));
                        hardness = blockState.getBlockHardness(world, blockPos);
                        if (blockState.getBlock() instanceof IFluidBlock || blockState.getBlock() instanceof BlockLiquid) {
                            hardness = 1;
                        }

                        float strength = getBreakStrength((float) distance, range);
                        if (blockState != null && blockState.getBlock() != null && blockState.getBlock() != Blocks.AIR && distance <= range && hardness >= 0 && (distance < eventHorizon || hardness < strength)) {
                            blocks.add(blockPos);
                        }
                    }
                }
            }
        }

        for (BlockPos position : blocks) {
            blockState = world.getBlockState(position);

            if (!cleanFlowingLiquids(blockState, position)) {
                if (liquidCount < MAX_LIQUIDS_PER_HARVEST) {
                    if (cleanLiquids(blockState, position)) {
                        liquidCount++;
                        continue;
                    }
                }
                if (solidCount < MAX_BLOCKS_PER_HARVEST) {
                    try {
                        distance = Math.sqrt(position.distanceSq(getPos()));
                        float strength = getBreakStrength((float) distance, range);
                        if (breakBlock(world, position, strength, eventHorizon, range)) {
                            solidCount++;
                        }
                    } catch (Exception e) {
                        MOLog.log(Level.ERROR, e, "There was a problem while trying to brake block %s", blockState.getBlock());
                    }
                }
            }
        }
    }

    //region Consume Type Handlers
    public void consume(Entity entity) {

        if (!entity.isDead && onEntityConsume(entity, true)) {

            boolean consumedFlag = false;

            if (entity instanceof EntityItem) {
                consumedFlag |= consumeEntityItem((EntityItem) entity);
            } else if (entity instanceof EntityFallingBlock) {
                consumedFlag |= consumeFallingBlock((EntityFallingBlock) entity);
            } else if (entity instanceof EntityLivingBase) {
                consumedFlag |= consumeLivingEntity((EntityLivingBase) entity, getBreakStrength((float) entity.getDistance(getPos().getX(), getPos().getY(), getPos().getZ()), (float) getMaxRange()));
            }

            if (consumedFlag) {
                onEntityConsume(entity, false);
            }
        }
    }

    private boolean consumeEntityItem(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getItem();
        if (!itemStack.isEmpty()) {
            try {
                mass = Math.addExact(mass, (long) MatterHelper.getMatterAmountFromItem(itemStack) * (long) itemStack.getCount());
                markDirty();
            } catch (ArithmeticException e) {
                return false;
            }

            entityItem.setDead();
            world.removeEntity(entityItem);

            if (entityItem.getItem().getItem() instanceof MatterItem && MatterItem.getType(entityItem.getItem()) == MatterItem.MatterType.ANTIMATTER) {
                collapse();
            }
            return true;
        }
        return false;
    }

    private boolean consumeFallingBlock(EntityFallingBlock fallingBlock) {
        ItemStack itemStack = new ItemStack(fallingBlock.getBlock().getBlock(), 1, fallingBlock.getBlock().getBlock().damageDropped(fallingBlock.getBlock()));
        if (!itemStack.isEmpty()) {
            try {
                mass = Math.addExact(mass, (long) MatterHelper.getMatterAmountFromItem(itemStack) * (long) itemStack.getCount());
                markDirty();
            } catch (ArithmeticException e) {
                return false;
            }

            fallingBlock.setDead();
            world.removeEntity(fallingBlock);
            return true;
        }
        return false;
    }

    private boolean consumeLivingEntity(EntityLivingBase entity, float strength) {
        try {
            mass = Math.addExact(mass, (long) Math.min(entity.getHealth(), strength));
            markDirty();
        } catch (ArithmeticException e) {
            return false;
        }

        if (entity.getHealth() <= strength && !(entity instanceof EntityPlayer)) {
            entity.setDead();
            world.removeEntity(entity);
        }

        DamageSource damageSource = new DamageSource("blackHole");
        entity.attackEntityFrom(damageSource, strength);
        return true;
    }
    //endregion

    public boolean breakBlock(World world, BlockPos pos, float strength, double eventHorizon, int range) {
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock().isAir(blockState, world, pos)) {
            return true;
        }

        float hardness = blockState.getBlockHardness(world, pos);
        double distance = Math.sqrt(pos.distanceSq(getPos()));
        if (distance <= range && hardness >= 0 && (distance < eventHorizon || hardness < strength)) {
            if (BLOCK_ENTETIES) {

                if (FALLING_BLOCKS) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockState);
                    fallingBlock.fallTime = 5;
                    fallingBlock.noClip = true;
                    world.spawnEntity(fallingBlock);
                } else {
                    ItemStack bStack = blockState.getBlock().getPickBlock(blockState, null, world, pos, null);
                    if (!bStack.isEmpty()) {
                        EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, bStack);
                        world.spawnEntity(item);
                    }
                }

                blockState.getBlock().breakBlock(world, pos, blockState);
                world.playBroadcastSound(2001, pos, Block.getIdFromBlock(blockState.getBlock()));
                world.setBlockToAir(pos);
                return true;
            } else {
                int matter = 0;

                if (blockState.getBlock().canSilkHarvest(world, pos, blockState, null)) {
                    matter += MatterHelper.getMatterAmountFromItem(blockState.getBlock().getPickBlock(blockState, null, world, pos, null));
                } else {
                    for (ItemStack stack : blockState.getBlock().getDrops(world, pos, blockState, 0)) {
                        matter += MatterHelper.getMatterAmountFromItem(stack);
                    }
                }

                world.playBroadcastSound(2001, pos, Block.getIdFromBlock(blockState.getBlock()));

                List<EntityItem> result = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3));
                for (EntityItem entityItem : result) {
                    consumeEntityItem(entityItem);
                }

                try {
                    mass = Math.addExact(mass, matter);
                    markDirty();
                } catch (ArithmeticException e) {
                    return false;
                }

                world.setBlockToAir(pos);
                return true;
            }
        }

        return false;
    }

    //region Helper Methods

    public boolean cleanLiquids(IBlockState blockState, BlockPos pos) {
        if (blockState.getBlock() instanceof IFluidBlock && FORGE_FLUIDS) {
            if (((IFluidBlock) blockState.getBlock()).canDrain(world, pos)) {
                if (FALLING_BLOCKS) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockState);
                    //fallingBlock.field_145812_b = 1;
                    fallingBlock.noClip = true;
                    world.spawnEntity(fallingBlock);
                }

                ((IFluidBlock) blockState.getBlock()).drain(world, pos, true);
                return true;
            }

        } else if (blockState.getBlock() instanceof BlockLiquid && VANILLA_FLUIDS) {
            IBlockState state = world.getBlockState(pos);
            if (world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2)) {
                if (FALLING_BLOCKS) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, state);
                    //fallingBlock.field_145812_b = 1;
                    fallingBlock.noClip = true;
                    world.spawnEntity(fallingBlock);
                }
                return true;
            }
        }

        return false;
    }

    public boolean cleanFlowingLiquids(IBlockState block, BlockPos pos) {
        if (VANILLA_FLUIDS) {
            if (block == Blocks.FLOWING_WATER || block == Blocks.FLOWING_LAVA) {
                return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
        }
        return false;
    }
    //endregion

    //TODO: Rewrite explosion to be more.. explosion
    public void collapse() {
        world.setBlockToAir(getPos());
        world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), (float) getRealMassUnsuppressed(), true);
    }

    @Override
    public void addInfo(World world, double x, double y, double z, List<String> infos) {
        DecimalFormat format = new DecimalFormat("#.##");
        infos.add("Mass: " + mass);
        infos.add("Range: " + format.format(getMaxRange()));
        infos.add("Brake Range: " + format.format(getBlockBreakRange()));
        infos.add("Horizon: " + format.format(getEventHorizon()));
        infos.add("Brake Lvl: " + format.format(getBreakStrength()));
    }

    public void suppress(AnomalySuppressor suppressor) {
        for (AnomalySuppressor s : supressors) {
            if (s.update(suppressor)) {
                return;
            }
        }

        supressors.add(suppressor);
    }

    private float calculateSuppression() {
        float suppression = 1;
        Iterator<AnomalySuppressor> iterator = supressors.iterator();
        while (iterator.hasNext()) {
            AnomalySuppressor s = iterator.next();
            if (!s.isValid()) {
                iterator.remove();
            }
            s.tick();
            suppression *= s.getAmount();
        }
        return suppression;
    }

    //region NBT
    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setLong("Mass", mass);
            nbt.setFloat("Suppression", suppression);
            if (toDisk && this.supressors != null && this.supressors.size() > 0) {
                NBTTagList suppressors = new NBTTagList();
                for (AnomalySuppressor s : this.supressors) {
                    NBTTagCompound suppressorTag = new NBTTagCompound();
                    s.writeToNBT(suppressorTag);
                    suppressors.appendTag(suppressorTag);
                }
                nbt.setTag("suppressors", suppressors);
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            this.supressors.clear();
            mass = nbt.getLong("Mass");
            suppression = nbt.getFloat("Suppression");
            NBTTagList suppressors = nbt.getTagList("suppressors", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < supressors.size(); i++) {
                NBTTagCompound suppressorTag = suppressors.getCompoundTagAt(i);
                AnomalySuppressor s = new AnomalySuppressor(suppressorTag);
                this.supressors.add(s);
            }
        }
    }
    //endregion

    //region Getters and Setters
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Math.max(Math.pow(getMaxRange(), 3), 2048);
    }

    public Block getBlock(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock();
    }

    public double getEventHorizon() {
        return Math.max((G2 * getRealMass()) / CC, 0.5);
    }

    public double getBlockBreakRange() {
        return getMaxRange() / 2;
    }

    public double getMaxRange() {
        return Math.sqrt(getRealMass() * (G / 0.01));
    }

    public double getAcceleration(double distanceSq) {
        return G * (getRealMass() / Math.max(distanceSq, 0.0001f));
    }

    public double getRealMass() {
        return getRealMassUnsuppressed() * suppression;
    }

    public double getRealMassUnsuppressed() {
        return Math.log1p(Math.max(mass, 0) * STREHGTH_MULTIPLYER);
    }

    public float getBreakStrength(float distance, float maxRange) {
        return ((float) getRealMass() * 4 * suppression) * getDistanceFalloff(distance, maxRange);

    }

    public float getDistanceFalloff(float distance, float maxRange) {
        return (1 - (distance / maxRange));
    }

    @Override
    @Deprecated
    public float getBreakStrength() {
        return (float) getRealMass() * 4 * suppression;
    }
    //endregion

    //region Sub Classes

    public static class BlockComparitor implements Comparator<BlockPos> {
        private final BlockPos pos;

        public BlockComparitor(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public int compare(BlockPos o1, BlockPos o2) {
            return Double.compare(o1.distanceSq(pos.getX(), pos.getY(), pos.getZ()), o2.distanceSq(pos.getX(), pos.getY(), pos.getZ()));
        }
    }
    //endregion
}
