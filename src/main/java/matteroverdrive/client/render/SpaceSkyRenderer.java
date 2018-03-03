package matteroverdrive.client.render;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

/**
 * Created by Simeon on 2/5/2016.
 */
public class SpaceSkyRenderer extends IRenderHandler {
    private float rotation = 0;
    private Sphere sphere = new Sphere();

    public static ResourceLocation texture = new ResourceLocation(Reference.PATH_GUI + "map.png");
    public static ResourceLocation clouds = new ResourceLocation(Reference.PATH_GUI + "clouds.png");

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        boolean drawClouds = true;
        EntityPlayer player = mc.player;

        if (player.dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId()) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableLighting();
            GlStateManager.translate(0, -TileEntityRendererDispatcher.staticPlayerY + -1330 + ((TileEntityRendererDispatcher.staticPlayerY / 3)*2), 0);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(45, 1, 0, 0);
            GlStateManager.rotate(rotation % 360, 0, 0, 1);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(texture);
            sphere.setTextureFlag(true);
            sphere.draw(1300f, 64, 32);
            GlStateManager.popMatrix();
            if (drawClouds) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(45, 1, 0, 0);
                GlStateManager.rotate((rotation * 1.6f) % 360, 0, 0, 1);
                GlStateManager.color(1, 1, 1, 0.7f);
                mc.getTextureManager().bindTexture(clouds);
                sphere.draw(1305f, 64, 32);
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            rotation += partialTicks / 40;
            if (rotation >= 360)
                rotation -= 360;
        }
    }
}
