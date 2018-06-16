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
package matteroverdrive.data;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.init.OverdriveFluids;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MatterStorage extends FluidTank implements IMatterHandler {

    private int maxExtract;
    private int maxReceive;

    public MatterStorage(int capacity) {
        this(capacity, capacity, capacity);
    }

    public MatterStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public MatterStorage(int capacity, int maxExtract, int maxReceive) {
        super(capacity);
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return fluid != null && fluid.getFluid() == OverdriveFluids.matterPlasma;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
        return fluid != null && fluid.getFluid() == OverdriveFluids.matterPlasma;
    }

    @Override
    public int modifyMatterStored(int amount) {
        int lastAmount = getFluid() == null ? 0 : getFluid().amount;
        int newAmount = lastAmount + amount;
        newAmount = MathHelper.clamp(newAmount, 0, getCapacity());
        setMatterStored(newAmount);
        return lastAmount - newAmount;
    }

    @Override
    public int getMatterStored() {
        return getFluidAmount();
    }

    @Override
    public void setMatterStored(int amount) {
        if (amount <= 0) {
            setFluid(null);
        } else {
            drainInternal(getFluidAmount(), true);
            fillInternal(new FluidStack(OverdriveFluids.matterPlasma, amount), true);
        }
    }

    @Override
    public int receiveMatter(int amount, boolean simulate) {
        return fill(new FluidStack(OverdriveFluids.matterPlasma, amount), !simulate);
    }

    @Override
    public int extractMatter(int amount, boolean simulate) {
        FluidStack drained = drain(amount, !simulate);
        if (drained == null) {
            return 0;
        } else {
            return drained.amount;
        }
    }
}
