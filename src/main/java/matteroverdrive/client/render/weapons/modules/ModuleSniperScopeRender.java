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
package matteroverdrive.client.render.weapons.modules;

import com.google.common.collect.ImmutableMap;
import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.weapons.WeaponRenderHandler;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 2/18/2016.
 */
public class ModuleSniperScopeRender extends ModuleRenderAbstract {
    private ResourceLocation scopeModelLocation = new ResourceLocation(Reference.PATH_MODEL + "item/sniper_scope.obj");
    private OBJModel scopeModel;
    private IBakedModel scopeBakedModelBase;
    private IBakedModel scopeBakedModelWindow;

    public ModuleSniperScopeRender(WeaponRenderHandler weaponRenderer) {
        super(weaponRenderer);
    }

    @Override
    public void renderModule(WeaponMetadataSection weaponMeta, ItemStack weaponStack, ItemStack moduleStack, float ticks) {
        Vec3d scopePos = weaponMeta.getModulePosition("default_scope");
        if (scopePos != null) {
            GlStateManager.translate(scopePos.x, scopePos.y, scopePos.z);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            weaponRenderer.renderModel(scopeBakedModelWindow, weaponStack);
            GlStateManager.disableBlend();

            weaponRenderer.renderModel(scopeBakedModelBase, weaponStack);
        }
    }

    @Override
    public void transformWeapon(WeaponMetadataSection weaponMeta, ItemStack weaponStack, ItemStack moduleStack, float ticks, float zoomValue) {
        Vec3d scopePos = weaponMeta.getModulePosition("default_scope");
        if (scopePos != null) {
            GlStateManager.translate(MOMathHelper.Lerp(0, -0.0005, zoomValue), MOMathHelper.Lerp(0, -scopePos.y / 5.5f, zoomValue), 0);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onModelBake(TextureMap textureMap, RenderHandler renderHandler) {
        if (scopeModel == null)
            scopeModel = renderHandler.getObjModel(scopeModelLocation, new ImmutableMap.Builder<String, String>().put("flip-v", "true").put("ambient", "false").build());
        List<String> visibleGroups = new ArrayList<>();
        visibleGroups.add("sniper_scope");
        scopeBakedModelBase = scopeModel.bake(new OBJModel.OBJState(visibleGroups, true), DefaultVertexFormats.ITEM, RenderHandler.modelTextureBakeFunc);
        visibleGroups.clear();
        visibleGroups.add("sniper_scope_window");
        scopeBakedModelWindow = scopeModel.bake(new OBJModel.OBJState(visibleGroups, true), DefaultVertexFormats.ITEM, RenderHandler.modelTextureBakeFunc);
    }

    @Override
    public void onTextureStich(TextureMap textureMap, RenderHandler renderHandler) {
        if (scopeModel == null)
            scopeModel = renderHandler.getObjModel(scopeModelLocation, new ImmutableMap.Builder<String, String>().put("flip-v", "true").put("ambient", "false").build());
        renderHandler.registerModelTextures(textureMap, scopeModel);
    }
}
