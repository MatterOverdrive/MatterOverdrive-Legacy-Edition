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
package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRendererPlanet extends StarMapRendererAbstract {

    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float viewerDistance) {
        if (spaceBody instanceof Planet) {
            glLineWidth(1);

            Planet planet = (Planet) spaceBody;
            GlStateManager.pushMatrix();
            renderPlanet(planet, viewerDistance);
            GlStateManager.popMatrix();

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            GlStateManager.enableTexture2D();

        }
    }

    protected void renderPlanet(Planet planet, float viewerDistance) {
        GlStateManager.pushMatrix();
        float size = getClampedSize(planet);
        GlStateManager.rotate(10, 1, 0, 0);

        GlStateManager.rotate(Minecraft.getMinecraft().world.getWorldTime() * 0.1f, 0, 1, 0);
        glPolygonMode(GL_FRONT, GL_LINE);

        GlStateManager.enableCull();
        //region Sphere rotated
        GlStateManager.pushMatrix();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.color(0, 0, 0);
        sphere.draw(size * 0.99f, 64, 32);
        GlStateManager.depthMask(false);

        //region Planet
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 1, 0, 0);

        RenderUtils.applyColorWithMultipy(Planet.getGuiColor(planet), 0.2f * (1f / viewerDistance));
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        sphere.draw(size, 64, 32);
        GlStateManager.popMatrix();
        //endregion
        GlStateManager.popMatrix();
        //endregion

        GlStateManager.disableCull();
        GlStateManager.popMatrix();

        drawPlanetInfoClose(planet);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        GlStateManager.enableTexture2D();
    }

    protected float getClampedSize(Planet planet) {
        return Math.min(Math.max(planet.getSize(), 1f), 2.2f) * 0.5f;
    }

    protected void drawPlanetInfoClose(Planet planet) {
        GlStateManager.pushMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float opacity) {
        if (spaceBody instanceof Planet) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_ONE, GL_ONE);

            Color color = Reference.COLOR_HOLO;
            Planet planet = (Planet) spaceBody;
            int x = 0;
            int y = -16;

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
            ClientProxy.holoIcons.renderIcon("icon_size", x, y);
            x += 18;
            RenderUtils.drawString(DecimalFormat.getPercentInstance().format(planet.getSize()), x, y + 6, Reference.COLOR_HOLO, opacity);

            if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().player)) {
                x = -2;
                y -= 20;

                float happines = planet.getHappiness();
                color = RenderUtils.lerp(Reference.COLOR_HOLO_RED, Reference.COLOR_HOLO, MathHelper.clamp(happines, 0, 1));
                RenderUtils.applyColorWithMultipy(color, opacity);
                ClientProxy.holoIcons.renderIcon("smile", x, y);
                x += 18;
                String info = DecimalFormat.getPercentInstance().format(happines);
                RenderUtils.drawString(info, x, y + 6, color, opacity);
                x += fontRenderer.getStringWidth(DecimalFormat.getPercentInstance().format(happines)) + 4;
                int population = planet.getPopulation();
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
                ClientProxy.holoIcons.renderIcon("sort_random", x, y);
                x += 18;
                info = String.format("%,d", population);
                RenderUtils.drawString(info, x, y + 6, Reference.COLOR_HOLO, opacity);

                x = -3;
                y -= 20;

                int powerProduction = planet.getPowerProduction();
                RenderUtils.applyColorWithMultipy(powerProduction < 0 ? Reference.COLOR_HOLO_RED : Reference.COLOR_HOLO, opacity);
                ClientProxy.holoIcons.renderIcon("battery", x, y);
                x += 18;
                info = Integer.toString(powerProduction) + "m" + MOEnergyHelper.ENERGY_UNIT;
                RenderUtils.drawString(info, x, y + 6, powerProduction < 0 ? Reference.COLOR_HOLO_RED : Reference.COLOR_HOLO, opacity);
            }
        }
    }

    @Override
    public boolean displayOnZoom(int zoom, SpaceBody spaceBody) {
        return zoom == 3;
    }

    @Override
    public double getHologramHeight(SpaceBody spaceBody) {
        return 1.5;
    }
}
