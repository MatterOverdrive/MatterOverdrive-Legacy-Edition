package matteroverdrive.items;

import matteroverdrive.init.OverdriveFluids;
import matteroverdrive.items.includes.EnergyContainer;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Simeon on 1/9/2016.
 */
public class PortableDecomposer extends MOItemEnergyContainer {
    private int defaultMatter;
    private float defaultMatterRatio;

    public PortableDecomposer(String name, int defaultMatter, float defaultMatterRatio) {
        super(name);
        this.defaultMatter = defaultMatter;
        this.defaultMatterRatio = defaultMatterRatio;
    }

    @Override
    protected int getCapacity() {
        return 128000;
    }

    @Override
    protected int getInput() {
        return 256;
    }

    @Override
    protected int getOutput() {
        return 256;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        infos.add(String.format("%s/%s %s", DecimalFormat.getIntegerInstance().format(getMatter(itemstack)), getMaxMatter(itemstack), MatterHelper.MATTER_UNIT));
        if (itemstack.getTagCompound() != null) {
            ItemStack s;
            NBTTagList list = itemstack.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                s = new ItemStack(list.getCompoundTagAt(i));
                infos.add(TextFormatting.GRAY + s.getDisplayName());
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
            FluidStack fluid = new FluidStack(OverdriveFluids.matterPlasma, getMatter(stack));
            int filled = handler.fill(fluid, true);
            setMatter(stack, Math.max(0, fluid.amount - filled));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    public int getMatter(ItemStack itemStack) {
        if (itemStack.getTagCompound() != null) {
            return itemStack.getTagCompound().getInteger("Matter");
        }
        return 0;
    }

    public void setMatter(ItemStack itemStack, float matter) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setFloat("Matter", matter);
    }

    public float getMaxMatter(ItemStack itemStack) {
        if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("MaxMatter")) {
            return itemStack.getTagCompound().getFloat("MaxMatter");
        }
        return defaultMatter;
    }

    public boolean isStackListed(ItemStack decomposer, ItemStack itemStack) {
        if (decomposer.getTagCompound() != null && MatterHelper.containsMatter(itemStack)) {
            NBTTagList stackList = decomposer.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < stackList.tagCount(); i++) {
                ItemStack s = new ItemStack(stackList.getCompoundTagAt(i));
                if (s.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(s, itemStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addStackToList(ItemStack decomposer, ItemStack itemStack) {
        if (decomposer.getTagCompound() == null) {
            decomposer.setTagCompound(new NBTTagCompound());
        }

        NBTTagList list = decomposer.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
        if (MatterHelper.containsMatter(itemStack)) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            itemStack.writeToNBT(tagCompound);
            list.appendTag(tagCompound);
        }
        decomposer.getTagCompound().setTag("Items", list);
    }

    public void decomposeItem(ItemStack decomposer, ItemStack itemStack) {
        if (MatterHelper.containsMatter(itemStack) && isStackListed(decomposer, itemStack)) {
            EnergyContainer storage = getStorage(itemStack);
            float matterFromItem = MatterHelper.getMatterAmountFromItem(itemStack) * defaultMatterRatio;
            int energyForItem = MathHelper.ceil(matterFromItem / defaultMatterRatio);
            float freeMatter = getMaxMatter(decomposer) - getMatter(decomposer);
            if (freeMatter > 0 && storage.getEnergyStored() > energyForItem) {
                int canTakeCount = (int) (freeMatter / matterFromItem);
                int itemsTaken = Math.min(canTakeCount, itemStack.getCount());
                itemsTaken = Math.min(itemsTaken, storage.getEnergyStored() / energyForItem);
                storage.setEnergy(storage.getEnergyStored() - (itemsTaken * energyForItem));
                setMatter(decomposer, getMatter(decomposer) + itemsTaken * matterFromItem);
                itemStack.setCount(Math.max(0, itemStack.getCount() - itemsTaken));
            }
        }
    }
}
