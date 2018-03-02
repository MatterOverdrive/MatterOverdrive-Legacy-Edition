package matteroverdrive.space;

import com.astro.clib.command.CustomTeleporter;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class SpaceHandler {
    private float rotation = 0;
    private Sphere sphere = new Sphere();

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player.dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId()) {
            GlStateManager.pushMatrix();
            RenderUtils.applyColor(Reference.COLOR_HOLO_RED);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            GlStateManager.disableTexture2D();
            GlStateManager.translate(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY + -600, -TileEntityRendererDispatcher.staticPlayerZ);
            GlStateManager.rotate(45, 1, 0, 0);
            GlStateManager.rotate(rotation % 360, 0, 0, 1);
            sphere.draw(600f, 64, 32);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
            rotation += event.getPartialTicks() / 40;
            if (rotation >= 360)
                rotation -= 360;
        }
    }

    @SubscribeEvent
    public void gravitySimulator(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        int dimension = living.dimension;
        double amount = 1;
        if (dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId())
            amount = 0.01;
        if (dimension == 1)
            amount = 0.5;
        if (!living.world.isRemote && amount != 1) {
            if (!(living instanceof EntityPlayer) || !(((EntityPlayer) living).capabilities.isFlying)) {
                living.motionY += 0.0784000015258789;
                living.motionY -= 0.0784000015258789 * amount;
            }
            if (living instanceof EntityPlayer && living.posY <= -3)
                CustomTeleporter.teleportToDimension((EntityPlayer) living, 0, ((EntityPlayer) living).posX, 800, ((EntityPlayer) living).posZ);
        }
    }
}