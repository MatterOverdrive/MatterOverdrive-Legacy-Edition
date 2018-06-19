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

import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import java.util.Random;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public abstract class StarMapRendererAbstract implements ISpaceBodyHoloRenderer {
    protected final Sphere sphere;
    protected final Disk disk;
    protected final Random random;
    protected final FontRenderer fontRenderer;
    protected TextureAtlasSprite star_icon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.star);
    protected TextureAtlasSprite selectedIcon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.selection);
    protected TextureAtlasSprite currentIcon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.marker);

    public StarMapRendererAbstract() {
        sphere = new Sphere();
        disk = new Disk();
        random = new Random();
		/*try
		{
            sphere_model = OBJLoader.instance.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }
}
