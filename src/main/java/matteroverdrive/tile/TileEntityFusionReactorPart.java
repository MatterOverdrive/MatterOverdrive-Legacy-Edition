/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
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

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.blocks.BlockFusionReactorIO;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.multiblock.IMultiBlockTile;
import matteroverdrive.multiblock.IMultiBlockTileStructure;
import matteroverdrive.multiblock.MultiBlockTileStructureMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

/**
 * Created by Simeon on 10/30/2015.
 */
public class TileEntityFusionReactorPart extends MOTileEntityMachineMatter implements IMultiBlockTile {
    private IMultiBlockTileStructure structure;
    private TileEntityMachineFusionReactorController fusionReactorController;

    public TileEntityFusionReactorPart() {
        super(0);
        energyStorage.setCapacity(0);
        energyStorage.setMaxExtract(0);
        energyStorage.setMaxReceive(0);
    }

    @Override
    public boolean isTileInvalid() {
        return tileEntityInvalid;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean canJoinMultiBlockStructure(IMultiBlockTileStructure structure) {
        return getMultiBlockHandler() == null && structure instanceof MultiBlockTileStructureMachine && ((MultiBlockTileStructureMachine) structure).getMachine() instanceof TileEntityMachineFusionReactorController;
    }

    @Override
    public IMultiBlockTileStructure getMultiBlockHandler() {
        return structure;
    }

    @Override
    public void setMultiBlockTileStructure(IMultiBlockTileStructure structure) {
        this.structure = structure;
        if (structure == null) {
            fusionReactorController = null;
        } else if (structure instanceof MultiBlockTileStructureMachine) {
            fusionReactorController = (TileEntityMachineFusionReactorController) ((MultiBlockTileStructureMachine) structure).getMachine();
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    public void update() {
        super.update();
        if (getBlockType() instanceof BlockFusionReactorIO) {
            if (structure != null && fusionReactorController != null) {
                for (EnumFacing side : EnumFacing.VALUES) {
                    TileEntity tile = world.getTileEntity(getPos().offset(side));
                    if (tile == null || (tile instanceof IMultiBlockTile && structure.containsMultiBlockTile((IMultiBlockTile) tile)))
                        continue;
                    if (tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
                        IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
                        if (storage == null)
                            continue;
                        storage.receiveEnergy(fusionReactorController.energyStorage.extractEnergy(storage.receiveEnergy(512, true), false), false);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return fusionReactorController != null;
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (fusionReactorController != null && capability == CapabilityEnergy.ENERGY)
            return (T) fusionReactorController.energyStorage;
        return super.getCapability(capability, facing);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }
}