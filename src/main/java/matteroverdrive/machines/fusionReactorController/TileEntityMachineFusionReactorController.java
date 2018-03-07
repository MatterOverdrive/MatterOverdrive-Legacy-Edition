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

package matteroverdrive.machines.fusionReactorController;


import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.blocks.BlockFusionReactorController;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.fusionReactorController.components.ComponentComputers;
import matteroverdrive.multiblock.IMultiBlockTile;
import matteroverdrive.multiblock.MultiBlockTileStructureMachine;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.tile.TileEntityFusionReactorPart;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

import static java.lang.Math.round;
import static matteroverdrive.util.MOBlockHelper.getAboveSide;

/**
 * Created by Simeon on 5/14/2015.
 */
/*@Optional.InterfaceList({
		@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.SimpleComponent"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.ManagedPeripheral")
})*/
public class TileEntityMachineFusionReactorController extends MOTileEntityMachineMatter {
    public static final int[] positions = new int[]{0, 5, 1, 0, 2, 0, 3, 1, 4, 2, 5, 3, 5, 4, 5, 5, 5, 6, 5, 7, 4, 8, 3, 9, 2, 10, 1, 10, 0, 10, -1, 10, -2, 10, -3, 9, -4, 8, -5, 7, -5, 6, -5, 5, -5, 4, -5, 3, -4, 2, -3, 1, -2, 0, -1, 0};
    public static final int[] blocks = new int[]{255, 2, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 2};
    public static final int positionsCount = positions.length / 2;
    public static int STRUCTURE_CHECK_DELAY = 40;
    public static int MAX_GRAVITATIONAL_ANOMALY_DISTANCE = 3;
    public static int ENERGY_STORAGE = 100000000;
    public static int MATTER_STORAGE = 2048;
    public static int ENERGY_PER_TICK = 2048;
    public static double MATTER_DRAIN_PER_TICK = 1.0D / 80.0D;
    private final TimeTracker structureCheckTimer;
    private final MultiBlockTileStructureMachine multiBlock;
    private boolean validStructure = false;
    private MonitorInfo monitorInfo = MonitorInfo.INVALID_STRUCTURE;
    private float energyEfficiency;
    private int energyPerTick;
    private BlockPos anomalyPosition;
    private float matterPerTick;
    private float matterDrain;
    private ComponentComputers componentComputers;

    public TileEntityMachineFusionReactorController() {
        super(4);

        structureCheckTimer = new TimeTracker();
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxExtract(ENERGY_STORAGE);
        energyStorage.setMaxReceive(ENERGY_STORAGE);

        matterStorage.setCapacity(MATTER_STORAGE);
        matterStorage.setMaxExtract(0);
        matterStorage.setMaxReceive(MATTER_STORAGE);

        multiBlock = new MultiBlockTileStructureMachine(this);
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setBoolean("ValidStructure", validStructure);
            nbt.setInteger("MonitorInfo", monitorInfo.getMeta());
            nbt.setFloat("EnergyEfficiency", energyEfficiency);
            nbt.setFloat("MatterPerTick", matterPerTick);
            nbt.setInteger("EnergyPerTick", energyPerTick);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            validStructure = nbt.getBoolean("ValidStructure");
            monitorInfo = MonitorInfo.fromMeta(nbt.getInteger("MonitorInfo"));
            energyEfficiency = nbt.getFloat("EnergyEfficiency");
            matterPerTick = nbt.getFloat("MatterPerTick");
            energyPerTick = nbt.getInteger("EnergyPerTick");
        }
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            //System.out.println("Fusion Reactor Update in chunk that is loaded:" + world.getChunkFromBlocks(x,z).isChunkLoaded);
            manageStructure();
            manageEnergyGeneration();
            manageEnergyExtract();
        }
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        componentComputers = new ComponentComputers(this);
        addComponent(componentComputers);
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return isValidStructure() &&
                isGeneratingPower();
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    public Vec3d getPosition(int i, EnumFacing facing) {
        if (i < positionsCount) {
            EnumFacing back = facing.getOpposite();
            Vec3d pos = new Vec3d(TileEntityMachineFusionReactorController.positions[i * 2], 0, TileEntityMachineFusionReactorController.positions[(i * 2) + 1]);

            if (back == EnumFacing.NORTH) {
                pos = pos.rotateYaw((float) Math.PI);
            } else if (back == EnumFacing.WEST) {
                pos = pos.rotateYaw((float) (Math.PI + Math.PI / 2));
            } else if (back == EnumFacing.EAST) {
                pos = pos.rotatePitch((float) (Math.PI / 2));
            } else if (back == EnumFacing.UP) {
                pos = pos.rotatePitch((float) (Math.PI / 2));
            } else if (back == EnumFacing.DOWN) {
                pos = pos.rotatePitch((float) (Math.PI + Math.PI / 2));

            }

            return pos;
        }
        return null;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        multiBlock.invalidate();
    }

    public void manageStructure() {
        if (structureCheckTimer.hasDelayPassed(world, STRUCTURE_CHECK_DELAY)) {
            multiBlock.update();
            EnumFacing side = world.getBlockState(getPos()).getValue(MOBlock.PROPERTY_DIRECTION);
            int anomalyDistance;
            boolean validStructure = true;
            MonitorInfo info = this.monitorInfo;
            float energyEfficiency = this.energyEfficiency;
            float matterPerTick = this.matterPerTick;

            for (int i = 0; i < positionsCount; i++) {
                Vec3d offset = getPosition(i, side);
                BlockPos position = new BlockPos(getPos().getX() + (int) round(offset.x), getPos().getY() + (int) round(offset.y), getPos().getZ() + (int) round(offset.z));

                if (blocks[i] == 255) {
                    BlockPos anomalyOffset = checkForGravitationalAnomaly(position, getAboveSide(side));

                    if (anomalyOffset != null) {
                        anomalyDistance = (int) Math.sqrt((anomalyOffset.getX() * anomalyOffset.getY()) + (anomalyOffset.getY() * anomalyOffset.getY()) + (anomalyOffset.getZ() * anomalyOffset.getZ()));
                        if (anomalyDistance > MAX_GRAVITATIONAL_ANOMALY_DISTANCE) {
                            validStructure = false;
                            info = MonitorInfo.ANOMALY_TOO_FAR;
                            break;
                        }
                        anomalyPosition = anomalyOffset.add(offset.x, offset.y, offset.z);
                    } else {
                        validStructure = false;
                        info = MonitorInfo.NO_ANOMALY;
                        anomalyPosition = null;
                        break;
                    }

                    energyEfficiency = 1f - ((float) anomalyDistance / (float) (MAX_GRAVITATIONAL_ANOMALY_DISTANCE + 1));
                    energyPerTick = (int) Math.round(ENERGY_PER_TICK * getEnergyEfficiency() * getGravitationalAnomalyEnergyMultiply());
                    double energyMultiply = getGravitationalAnomalyEnergyMultiply();
                    matterPerTick = (float) (MATTER_DRAIN_PER_TICK * energyMultiply);
                } else {
                    Block block = world.getBlockState(position).getBlock();
                    TileEntity tileEntity = world.getTileEntity(position);

                    if (block == Blocks.AIR) {
                        validStructure = false;
                        info = MonitorInfo.INVALID_STRUCTURE;
                        break;
                    } else if (block == MatterOverdrive.BLOCKS.machine_hull) {
                        if (blocks[i] == 1) {
                            validStructure = false;
                            info = MonitorInfo.NEED_COILS;
                            break;
                        }
                    } else if (block == MatterOverdrive.BLOCKS.fusion_reactor_coil || tileEntity instanceof IMultiBlockTile) {
                        if (blocks[i] == 0) {
                            validStructure = false;
                            info = MonitorInfo.INVALID_MATERIALS;
                            break;
                        }
                    } else if (block == MatterOverdrive.BLOCKS.decomposer) {
                        if (blocks[i] != 2) {
                            validStructure = false;
                            info = MonitorInfo.INVALID_MATERIALS;
                            break;
                        }
                    } else {
                        validStructure = false;
                        info = MonitorInfo.INVALID_MATERIALS;
                        break;
                    }

                    if (tileEntity instanceof IMultiBlockTile) {
                        multiBlock.addMultiBlockTile((IMultiBlockTile) tileEntity);
                    }
                }
            }

            if (validStructure) {
                info = MonitorInfo.OK;
                /*info = "POWER " + Math.round((1f - ((float) anomalyDistance / (float) (MAX_GRAVITATIONAL_ANOMALY_DISTANCE + 1))) * 100) + "%";
                info += "\nCHARGE " + DecimalFormat.getPercentInstance().format((double) getEnergyStorage().getEnergyStored() / (double) getEnergyStorage().getMaxEnergyStored());
                info += "\nMATTER " + DecimalFormat.getPercentInstance().format((double) matterStorage.getMatterStored() / (double) matterStorage.getCapacity());*/
            } else {
                energyEfficiency = 0;
            }

            if (this.validStructure != validStructure || !this.monitorInfo.equals(info) || this.energyEfficiency != energyEfficiency || this.matterPerTick != matterPerTick) {
                this.validStructure = validStructure;
                this.monitorInfo = info;
                this.energyEfficiency = energyEfficiency;
                this.matterPerTick = matterPerTick;
                forceSync();
            }
        }
    }

    private void manageEnergyGeneration() {
        if (isActive()) {
            int energyPerTick = getEnergyPerTick();
            int energyRecived = energyStorage.modifyEnergyStored(energyPerTick);
            if (energyRecived != 0) {
                matterDrain += getMatterDrainPerTick() * ((float) energyRecived / (float) energyPerTick);
                if (MathHelper.floor(matterDrain) >= 1) {
                    matterStorage.modifyMatterStored(-MathHelper.floor(matterDrain));
                    matterDrain -= MathHelper.floor(matterDrain);
                }
                UpdateClientPower();
            }
        }
    }

    private void manageEnergyExtract() {
        if (energyStorage.getEnergyStored() > 0) {
            for (IMultiBlockTile tile : multiBlock.getTiles()) {
                if (tile instanceof TileEntityFusionReactorPart) {
                    manageExtractFrom((TileEntityFusionReactorPart) tile);
                }
            }
        }

        manageExtractFrom(this);
    }

    private void manageExtractFrom(MOTileEntityMachineEnergy source) {
        TileEntity entity;
        int energy;
        int startDir = random.nextInt(6);

        for (int i = 0; i < 6; i++) {
            energy = Math.min(energyStorage.getEnergyStored(), ENERGY_STORAGE);
            EnumFacing dir = EnumFacing.VALUES[(i + startDir) % 6];
            entity = world.getTileEntity(source.getPos().offset(dir));

            if (entity != null) {
                if (entity.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
                    energyStorage.modifyEnergyStored(-entity.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).receiveEnergy(energy, false));
                }
            }
        }
    }

    @Override
    public boolean isCharging() {
        return !this.inventory.getStackInSlot(energySlotID).isEmpty() && MOEnergyHelper.isEnergyContainerItem(this.inventory.getStackInSlot(energySlotID));
    }

    @Override
    protected void manageCharging() {
        if (isCharging()) {
            if (!this.world.isRemote) {
                int maxExtracted = Math.min((int) energyStorage.getOutputRate(), energyStorage.getEnergyStored());
                int extracted = MOEnergyHelper.insertEnergyIntoContainer(this.inventory.getStackInSlot(energySlotID), maxExtracted, false);
                energyStorage.modifyEnergyStored(extracted);
            }
        }
    }

    public int getEnergyPerTick() {
        return energyPerTick;
    }

    public double getGravitationalAnomalyEnergyMultiply() {
        if (anomalyPosition != null) {
            TileEntity entity = world.getTileEntity(getPos().add(anomalyPosition));
            if (entity instanceof TileEntityGravitationalAnomaly) {
                return ((TileEntityGravitationalAnomaly) entity).getRealMassUnsuppressed();
            }
        }
        return 0;
    }

    public float getMatterDrainPerTick() {
        return matterPerTick;
    }

    public boolean isGeneratingPower() {
        return getEnergyEfficiency() > 0
                && getEnergyStorage().getEnergyStored() < getEnergyStorage().getMaxEnergyStored()
                && matterStorage.getMatterStored() > getMatterDrainPerTick();
    }

    public float getEnergyEfficiency() {
        return energyEfficiency;
    }

    private BlockPos checkForGravitationalAnomaly(BlockPos position, EnumFacing up) {
        for (int i = -MAX_GRAVITATIONAL_ANOMALY_DISTANCE; i < MAX_GRAVITATIONAL_ANOMALY_DISTANCE + 1; i++) {
            Block block = world.getBlockState(position.offset(up, i)).getBlock();
            if (block != null && block == MatterOverdrive.BLOCKS.gravitational_anomaly) {
                return new BlockPos(0, 0, 0).offset(up, i);
            }
        }

        return null;
    }

    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockFusionReactorController))
            return Block.FULL_BLOCK_AABB.offset(getPos());
        EnumFacing backSide = state.getValue(MOBlock.PROPERTY_DIRECTION).getOpposite();
        return new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + backSide.getDirectionVec().getX() * 10, getPos().getY() + backSide.getDirectionVec().getY() * 10, getPos().getZ() + backSide.getDirectionVec().getZ() * 10);
    }

    public boolean isValidStructure() {
        return validStructure;
    }

    public MonitorInfo getMonitorInfo() {
        return monitorInfo;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return type == UpgradeTypes.PowerStorage || type == UpgradeTypes.Range || type == UpgradeTypes.Speed;
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    //region All Computers
	/*//region ComputerCraft
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String getType() {
		return componentComputers.getType();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String[] getMethodNames() {
		return componentComputers.getMethodNames();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return componentComputers.callMethod(computer,context,method,arguments);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void attach(IComputerAccess computer) {componentComputers.attach(computer);}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void detach(IComputerAccess computer) {componentComputers.detach(computer);}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public boolean equals(IPeripheral other) {
		return componentComputers.equals(other);
	}
    //endregion
    //region OpenComputers
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName() {
		return componentComputers.getComponentName();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public String[] methods() {
		return componentComputers.methods();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		return componentComputers.invoke(method,context,args);
	}
    //endregion*/
    //endregion

    public static enum MonitorInfo implements IStringSerializable {
        INVALID_STRUCTURE,
        NEED_COILS,
        INVALID_MATERIALS,
        NO_ANOMALY,
        ANOMALY_TOO_FAR,
        OK;

        public static MonitorInfo[] VALUES = values();

        public static MonitorInfo fromMeta(int meta) {
            return VALUES[MathHelper.clamp(meta, 0, VALUES.length)];
        }

        public int getMeta() {
            return ordinal();
        }

        @SideOnly(Side.CLIENT)
        public String localize() {
            return I18n.format("fusion_reactor.info." + getName());
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}