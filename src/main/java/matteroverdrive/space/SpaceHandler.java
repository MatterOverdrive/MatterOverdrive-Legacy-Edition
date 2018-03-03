package matteroverdrive.space;

import com.astro.clib.command.CustomTeleporter;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.client.sound.SoundMuffled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class SpaceHandler {
    private float rotation = 0;
    private Sphere sphere = new Sphere();

    public static ResourceLocation texture = new ResourceLocation(Reference.PATH_GUI + "map.png");
    public static ResourceLocation clouds = new ResourceLocation(Reference.PATH_GUI + "clouds.png");

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        boolean drawClouds = true;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player.dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId()) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableLighting();
            GlStateManager.translate(0, -TileEntityRendererDispatcher.staticPlayerY + -1310 + (TileEntityRendererDispatcher.staticPlayerY / 2), 0);
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
            rotation += event.getPartialTicks() / 40;
            if (rotation >= 360)
                rotation -= 360;
        }
    }

    @SubscribeEvent
    public void gameOverlay(RenderGameOverlayEvent event) {

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void soundEvent(PlaySoundEvent event) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId())
            event.setResultSound(new SoundMuffled(event.getResultSound(), 15f));
    }

    @SubscribeEvent
    public void gravitySimulator(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        int dimension = living.dimension;
        double amount = 1;
        if (dimension == MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE.getId())
            amount = 0.05;
        if (dimension == 1)
            amount = 0.5;

        if (!(living instanceof EntityPlayer) || !(((EntityPlayer) living).capabilities.isFlying)) {
            living.motionY += 0.0784000015258789;
            living.motionY -= 0.0784000015258789 * amount;
        }

        if (!living.world.isRemote && living.posY <= -3) {
            if (living instanceof EntityPlayer)
                CustomTeleporter.teleportToDimension((EntityPlayer) living, 0, ((EntityPlayer) living).posX, 800, ((EntityPlayer) living).posZ);
            else
                living.attackEntityFrom(DamageSource.OUT_OF_WORLD, 5);
        }
    }
}