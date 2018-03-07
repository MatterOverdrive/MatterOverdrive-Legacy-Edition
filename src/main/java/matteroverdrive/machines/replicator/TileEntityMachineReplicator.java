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

package matteroverdrive.machines.replicator;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.blocks.BlockReplicator;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.inventory.ShieldingSlot;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import java.util.EnumSet;
import java.util.List;

import static matteroverdrive.util.MOBlockHelper.getLeftSide;


public class TileEntityMachineReplicator extends MOTileEntityMachineMatter implements IMatterNetworkClient, IMatterNetworkConnection, IMatterNetworkDispatcher {
    public static final int MATTER_TRANSFER = 128;
    public static final int REPLICATION_ANIMATION_TIME = 60;
    public static final int RADIATION_DAMAGE_DELAY = 5;
    public static final int RADIATION_RANGE = 8;
    private static final EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerStorage, UpgradeTypes.Speed, UpgradeTypes.Fail, UpgradeTypes.PowerUsage, UpgradeTypes.MatterStorage);
    public static int MATTER_STORAGE = 1024;
    public static int ENERGY_STORAGE = 512000;
    public int OUTPUT_SLOT_ID = 0;
    public int SECOND_OUTPUT_SLOT_ID = 1;
    public int DATABASE_SLOT_ID = 2;
    public int SHIELDING_SLOT_ID = 3;
    @SideOnly(Side.CLIENT)
    private boolean isPlayingReplicateAnimation;
    @SideOnly(Side.CLIENT)
    private int replicateAnimationCounter;

    private ComponentMatterNetworkReplicator networkComponent;
    private ComponentTaskProcessingReplicator taskProcessingComponent;
    private ComponentMatterNetworkConfigs matterNetworkConfigs;

    public TileEntityMachineReplicator() {
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_STORAGE);
        this.energyStorage.setMaxReceive(ENERGY_STORAGE);
        this.matterStorage.setCapacity(MATTER_STORAGE);
        this.matterStorage.setMaxExtract(MATTER_TRANSFER);
        this.matterStorage.setMaxExtract(0);
        playerSlotsMain = true;
        playerSlotsHotbar = true;
    }

    @Override
    public BlockPos getPosition() {
        return getPos();
    }

    protected void RegisterSlots(Inventory inventory) {
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false).setSendToClient(true));
        SECOND_OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false));
        DATABASE_SLOT_ID = inventory.AddSlot(new DatabaseSlot(true));
        SHIELDING_SLOT_ID = inventory.AddSlot(new ShieldingSlot(true));
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        networkComponent = new ComponentMatterNetworkReplicator(this);
        matterNetworkConfigs = new ComponentMatterNetworkConfigs(this);
        taskProcessingComponent = new ComponentTaskProcessingReplicator("Replication Tasks", this, 1);
        addComponent(networkComponent);
        addComponent(matterNetworkConfigs);
        addComponent(taskProcessingComponent);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote) {
            manageSpawnParticles();
        }
    }

    @SideOnly(Side.CLIENT)
    public void beginSpawnParticles() {
        replicateAnimationCounter = REPLICATION_ANIMATION_TIME;
    }

    @SideOnly(Side.CLIENT)
    public void manageSpawnParticles() {
        if (replicateAnimationCounter > 0) {
            isPlayingReplicateAnimation = true;
            SpawnReplicateParticles(REPLICATION_ANIMATION_TIME - replicateAnimationCounter);
            replicateAnimationCounter--;
        } else {
            if (isPlayingReplicateAnimation) {
                //sync with server so that the replicated item will be seen
                isPlayingReplicateAnimation = false;
                forceSync();
            }
        }

        if (isActive()) {
            if (getBlockType(BlockReplicator.class).hasVentParticles) {
                SpawnVentParticles(0.05f, getLeftSide(getWorld().getBlockState(getPos()).getValue(MOBlock.PROPERTY_DIRECTION)), 1);
                SpawnVentParticles(0.05f, getLeftSide(getWorld().getBlockState(getPos()).getValue(MOBlock.PROPERTY_DIRECTION)), 1);
            }
        }
    }

    boolean putInOutput(ItemStack item) {
        if (getStackInSlot(OUTPUT_SLOT_ID) == null) {
            setInventorySlotContents(OUTPUT_SLOT_ID, item);
            return true;
        } else {
            if (getStackInSlot(OUTPUT_SLOT_ID).isStackable() && getStackInSlot(OUTPUT_SLOT_ID).getItemDamage() == item.getItemDamage() && getStackInSlot(OUTPUT_SLOT_ID).getItem() == item.getItem()) {
                int newStackSize = getStackInSlot(OUTPUT_SLOT_ID).getCount() + 1;

                if (newStackSize <= getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize()) {
                    getStackInSlot(OUTPUT_SLOT_ID).setCount(newStackSize);
                    return true;
                }
            }
        }

        return false;
    }

    boolean failReplicate(int amount) {
        ItemStack stack = getStackInSlot(SECOND_OUTPUT_SLOT_ID);

        if (stack.isEmpty()) {
            stack = new ItemStack(MatterOverdrive.ITEMS.matter_dust);
            MatterOverdrive.ITEMS.matter_dust.setMatter(stack, amount);
            setInventorySlotContents(SECOND_OUTPUT_SLOT_ID, stack);
            return true;
        } else {
            if (canReplicateIntoSecoundOutput(amount)) {
                stack.grow(1);
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void SpawnReplicateParticles(int startTime) {
        double time = (double) (startTime) / (double) (REPLICATION_ANIMATION_TIME);
        double gravity = MOMathHelper.easeIn(time, 0.02, 0.2, 1);
        int age = (int) Math.round(MOMathHelper.easeIn(time, 2, 10, 1));
        int count = (int) Math.round(MOMathHelper.easeIn(time, 1, 20, 1));

        for (int i = 0; i < count; i++) {
            float speed = 0.05f;

            Vector3f pos = MOMathHelper.randomSpherePoint(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D, new Vec3d(0.5, 0.5, 0.5), this.world.rand);
            Vector3f dir = new Vector3f(random.nextFloat() * 2 - 1, (random.nextFloat() * 2 - 1) * 0.05f, random.nextFloat() * 2 - 1);
            dir.scale(speed);
            ReplicatorParticle replicatorParticle = new ReplicatorParticle(this.world, pos.getX(), pos.getY(), pos.getZ(), dir.getX(), dir.getY(), dir.getZ());
            replicatorParticle.setCenter(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D);

            replicatorParticle.setParticleAge(age);
            replicatorParticle.setPointGravityScale(gravity);
            Minecraft.getMinecraft().effectRenderer.addEffect(replicatorParticle);
        }
    }

    @Override
    public boolean getServerActive() {
        return taskProcessingComponent.isReplicating();
    }

    public void manageRadiation() {
        int shielding = getShielding();

        if (shielding >= 5) {
            return;             //has full shielding
        }

        AxisAlignedBB bb = new AxisAlignedBB(getPos().add(-RADIATION_RANGE, -RADIATION_RANGE, -RADIATION_RANGE), getPos().add(RADIATION_RANGE, RADIATION_RANGE, RADIATION_RANGE));
        List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
        for (Object e : entities) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase l = (EntityLivingBase) e;

                double distance = Math.sqrt(getPos().distanceSq(l.getPosition())) / RADIATION_RANGE;
                distance = MathHelper.clamp(distance, 0, 1);
                distance = 1.0 - distance;
                distance *= 5 - shielding;

                PotionEffect[] effects = new PotionEffect[3];
                //confusion
                effects[0] = new PotionEffect(MobEffects.NAUSEA, (int) Math.round(Math.pow(5, distance)), 0);
                //weakness
                effects[0] = new PotionEffect(MobEffects.WEAKNESS, (int) Math.round(Math.pow(10, distance)), 0);
                //hunger
                effects[1] = new PotionEffect(MobEffects.HUNGER, (int) Math.round(Math.pow(12, distance)), 0);
                //poison
                effects[2] = new PotionEffect(MobEffects.POISON, (int) Math.round(Math.pow(5, distance)), 0);

                for (PotionEffect effect : effects) {
                    if (effect.getDuration() > 0) {
                        l.addPotionEffect(effect);
                    }
                }
            }
        }
    }

    boolean canReplicateIntoOutput(ItemStack itemStack) {
        return !itemStack.isEmpty() && (getStackInSlot(OUTPUT_SLOT_ID).isEmpty() || itemStack.isItemEqual(getStackInSlot(OUTPUT_SLOT_ID)) && ItemStack.areItemStackTagsEqual(itemStack, getStackInSlot(OUTPUT_SLOT_ID)) && getStackInSlot(OUTPUT_SLOT_ID).getCount() < getStackInSlot(OUTPUT_SLOT_ID).getMaxStackSize());
    }

    boolean canReplicateIntoSecoundOutput(int matter) {
        ItemStack stack = getStackInSlot(SECOND_OUTPUT_SLOT_ID);

        if (stack.isEmpty()) {
            return true;
        } else {
            if (stack.getItem() == MatterOverdrive.ITEMS.matter_dust && stack.getItemDamage() == matter && stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return upgradeTypes.contains(type);
    }

    //region Inventory Functions
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{OUTPUT_SLOT_ID, SECOND_OUTPUT_SLOT_ID};
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        ItemStack s = super.decrStackSize(slot, size);
        forceSync();
        return s;
    }
    //endregion

    //region Matter Network functions

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side) {
        return blockState.getValue(MOBlock.PROPERTY_DIRECTION).getOpposite().equals(side);
    }

    @Override
    public BlockPos getNodePos() {
        return getPos();
    }

    @Override
    public boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side) {
        return networkComponent.establishConnectionFromSide(blockState, side);
    }

    @Override
    public void breakConnection(IBlockState blockState, EnumFacing side) {
        networkComponent.breakConnection(blockState, side);
    }

    @Override
    public MatterNetwork getNetwork() {
        return networkComponent.getNetwork();
    }

    @Override
    public void setNetwork(MatterNetwork network) {
        networkComponent.setNetwork(network);
    }

    @Override
    public World getNodeWorld() {
        return getWorld();
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction) {
        return networkComponent.canConnectToNetworkNode(blockState, toNode, direction);
    }

    //endregion

    //region Events

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    //endregion

    //region Getters and Setters
/*    public ItemPattern getInternalPatternStorage()
	{
        return internalPatternStorage;
    }*/
    /*    public void setInternalPatternStorage(ItemPattern internalPatternStorage){this.internalPatternStorage = internalPatternStorage;}*/
    private int getShielding() {
        if (getStackInSlot(SHIELDING_SLOT_ID) != null && getStackInSlot(SHIELDING_SLOT_ID).getItem() == MatterOverdrive.ITEMS.tritanium_plate) {
            return getStackInSlot(SHIELDING_SLOT_ID).getCount();
        }
        return 0;
    }

    @Override
    public SoundEvent getSound() {
        return MatterOverdriveSounds.machine;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public float soundVolume() {
        return 1;
    }

/*    public boolean canCompleteTask(MatterNetworkTaskReplicatePattern taskReplicatePattern)
    {
        return taskReplicatePattern != null
                && internalPatternStorage != null
                && taskReplicatePattern.getPattern().equals(getInternalPatternStorage())
                && taskReplicatePattern.isValid(world);
    }*/

    /*@Override
    public NBTTagCompound getFilter() {
        return componentMatterNetworkConfigs.getFilter();
    }*/
    @Override
    public float getProgress() {
        return taskProcessingComponent.getReplicateProgress();
    }

    public int getTaskReplicateCount() {
        if (taskProcessingComponent.getTaskQueue().peek() != null) {
            return taskProcessingComponent.getTaskQueue().peek().getAmount();
        }
        return 0;
    }

    @Override
    public MatterNetworkComponentClient getMatterNetworkComponent() {
        return networkComponent;
    }

    @Override
    public MatterNetworkTaskQueue getTaskQueue(int queueID) {
        return taskProcessingComponent.getTaskQueue();
    }

    @Override
    public int getTaskQueueCount() {
        return 1;
    }

    public int getEnergyDrainPerTick() {
        return taskProcessingComponent.getEnergyDrainPerTick();
    }

    public int getEnergyDrainMax() {
        return taskProcessingComponent.getEnergyDrainMax();
    }
    //endregion
}
