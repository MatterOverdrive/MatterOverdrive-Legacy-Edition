package matteroverdrive.items;

import com.astro.clib.api.wrench.IDismantleable;
import com.astro.clib.api.wrench.IWrenchable;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 5/19/2015.
 */
public class Wrench extends MOBaseItem {
    public Wrench(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        boolean result = false;

        if (!state.getBlock().isAir(state, world, pos)) {
            PlayerInteractEvent e = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));

            if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY) {
                return EnumActionResult.FAIL;
            }

            if (player.isSneaking() && state.getBlock() instanceof IDismantleable && ((IDismantleable) state.getBlock()).canDismantle(player, world, pos)) {
                if (!world.isRemote) {
                    ((IDismantleable) state.getBlock()).dismantleBlock(player, world, pos, false);
                }

                result = true;
            }

            if (!result) {
                if (state.getBlock() instanceof IWrenchable && !world.isRemote) {
                    result = ((IWrenchable) state.getBlock()).onWrenchHit(stack, player, world, pos, side, hitX, hitY, hitZ);
                } else if (!player.isSneaking() && state.getBlock().rotateBlock(world, pos, side)) {
                    result = true;
                }
            }
        }

        if (result) {
            player.swingArm(hand);
        }

        return result && !world.isRemote ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    @Override
    public boolean hasDetails(ItemStack stack) {
        return true;
    }
}
