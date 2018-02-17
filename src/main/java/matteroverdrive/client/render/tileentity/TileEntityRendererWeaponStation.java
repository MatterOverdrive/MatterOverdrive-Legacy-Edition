package matteroverdrive.client.render.tileentity;

import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TileEntityRendererWeaponStation extends TileEntityRendererStation<TileEntityWeaponStation> {

    EntityItem itemEntity;

    public TileEntityRendererWeaponStation() {
        super();
    }

    @Override
    protected void renderHologram(TileEntityWeaponStation tileEntity, double x, double y, double z, float partialTicks, double noise) {
        if (tileEntity.getWorld().isAirBlock(tileEntity.getPos())) {
            return;
        }

        if (isUsable(tileEntity)) {
            ItemStack stack = tileEntity.getStackInSlot(tileEntity.INPUT_SLOT);

            if (!stack.isEmpty()) {
                if (itemEntity == null) {
                    itemEntity = new EntityItem(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), stack);
                } else if (!ItemStack.areItemStacksEqual(itemEntity.getItem(), stack)) {
                    itemEntity.setItem(stack);
                }

                itemEntity.hoverStart = tileEntity.getWorld().getWorldTime();
                GlStateManager.translate(x + 0.5f, y + 0.8f, z + 0.5f);
                GlStateManager.scale(0.5, 0.5, 0.5);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.rotate(getWorld().getWorldTime(), 0, 1, 0);
                RenderUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
                model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

                BufferBuilder wr = Tessellator.getInstance().getBuffer();

                try {
                    wr.finishDrawing();
                } catch (IllegalStateException e) {

                }

                RenderHelper.disableStandardItemLighting();
            }
        } else {
            super.renderHologram(tileEntity, x, y, z, partialTicks, noise);
        }
    }
}

