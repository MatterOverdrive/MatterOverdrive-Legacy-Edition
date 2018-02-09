package matteroverdrive.client.render.tileentity;

import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/19/2015.
 */
public class TileEntityRendererReplicator extends TileEntitySpecialRenderer<TileEntityMachineReplicator> {
    EntityItem itemEntity;

    @Override
    public void render(TileEntityMachineReplicator replicator, double x, double y, double z, float ticks, int destoryStage, float a) {
        GlStateManager.pushMatrix();
        renderItem(replicator, x, y, z);
        GlStateManager.popMatrix();
    }

    private void renderItem(TileEntityMachineReplicator replicator, double x, double y, double z) {
        ItemStack stack = replicator.getStackInSlot(replicator.OUTPUT_SLOT_ID);
        if (!stack.isEmpty()) {
            if (itemEntity == null) {
                itemEntity = new EntityItem(replicator.getWorld(), x, y, z, stack);
            } else if (!ItemStack.areItemStacksEqual(itemEntity.getItem(), stack)) {
                itemEntity.setItem(stack);
            }

            itemEntity.hoverStart = (float) (Math.PI / 2);
            Minecraft.getMinecraft().getRenderManager().renderEntity(itemEntity, x + 0.5d, y + 0.05, z + 0.5, 0, 0, false);
        }
    }
}