package matteroverdrive.api.internal;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class Storage<CAP extends INBTSerializable<NBT>, NBT extends NBTBase> implements Capability.IStorage<CAP> {
    @Override
    public NBT writeNBT(Capability<CAP> capability, CAP instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<CAP> capability, CAP instance, EnumFacing side, NBTBase tag) {
        instance.deserializeNBT((NBT) tag);
    }

}