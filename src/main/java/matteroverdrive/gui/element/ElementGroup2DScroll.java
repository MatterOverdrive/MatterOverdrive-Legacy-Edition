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
package matteroverdrive.gui.element;

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Simeon on 1/31/2016.
 */
public class ElementGroup2DScroll extends ElementBaseGroup {
    int scrollX;
    int scrollY;
    int lastMouseX;
    int lastMouseY;
    int minScrollX;
    int maxScrollX;
    int minScrollY;
    int maxScrollY;
    boolean isDragging;
    boolean hasDragged;

    public ElementGroup2DScroll(MOGuiBase gui, int posX, int posY, int width, int height) {
        super(gui, posX, posY, width, height);
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (isDragging) {
            int mouseDeltaX = (mouseX - lastMouseX);
            int mouseDeltaY = (mouseY - lastMouseY);

            if (mouseDeltaX != 0 || mouseDeltaY != 0) {
                hasDragged = true;
            }

            setScroll(scrollX + mouseDeltaX, scrollY - mouseDeltaY);

            //moveElementByDelta(mouseDeltaX,mouseDeltaY);
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    private void moveElementByDelta(int deltaX, int deltaY) {
        for (MOElementBase elementBase : elements) {
            if (elementBase instanceof IParallaxElement) {
                ((IParallaxElement) elementBase).move(deltaX, deltaY);
            } else {
                elementBase.setPosition(elementBase.getPosX() + deltaX, elementBase.getPosY() + deltaY);
            }
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        //RenderUtils.beginStencil();
        //RenderUtils.drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        RenderUtils.beginDrawingDepthMask();
        RenderUtils.drawPlane(posX, posY, 200, sizeX, sizeY);
        //GlStateManager.enableTexture2D();

        //GlStateManager.depthMask(false);
        //GlStateManager.colorMask(true,true,true,false);
        //GlStateManager.enableDepth();
        //GlStateManager.depthFunc(GL11.GL_LESS);
        RenderUtils.beginDepthMasking();
        super.drawBackground(mouseX, mouseY, gameTicks);
        RenderUtils.endDepthMask();
        //RenderUtils.endStencil();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        //RenderUtils.beginStencil();
        //RenderUtils.drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        RenderUtils.beginDrawingDepthMask();
        RenderUtils.drawPlane(posX, posY, 200, sizeX, sizeY);
        RenderUtils.beginDepthMasking();
        super.drawForeground(mouseX, mouseY);
        RenderUtils.endDepthMask();
        //RenderUtils.endStencil();
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
        isDragging = true;
        return super.onMousePressed(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        isDragging = false;
        if (!hasDragged) {
            super.onMouseReleased(mouseX, mouseY);
        }
        hasDragged = false;
    }

    public void setScroll(int scrollX, int scrollY) {
        scrollX = MathHelper.clamp(scrollX, -maxScrollX, -minScrollX);
        scrollY = MathHelper.clamp(scrollY, minScrollY, maxScrollY);
        int scrollDeltaX = scrollX - this.scrollX;
        int scrollDeltaY = this.scrollY - scrollY;
        this.scrollX = scrollX;
        this.scrollY = scrollY;
        moveElementByDelta(scrollDeltaX, scrollDeltaY);
    }

    @Override
    public MOElementBase addElement(MOElementBase element) {
        MOElementBase elementBase = super.addElement(element);
        if (elementBase != null) {
            if (elementBase instanceof IParallaxElement) {
                ((IParallaxElement) elementBase).move(+scrollX, -scrollY);
            } else {
                elementBase.setPosition(elementBase.getPosX() + scrollX, elementBase.getPosY() - scrollY);
            }
        }
        return elementBase;
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        int scrollDeltaX = this.scrollX - scrollX;
        this.scrollX = scrollX;
        moveElementByDelta(scrollDeltaX, 0);
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        int scrollDeltaY = this.scrollY - scrollY;
        this.scrollY = scrollY;
        moveElementByDelta(0, scrollDeltaY);
    }

    public void setScrollBounds(int minScrollX, int maxScrollX, int minScrollY, int maxScrollY) {
        this.minScrollX = minScrollX;
        this.minScrollY = minScrollY;
        this.maxScrollX = maxScrollX;
        this.maxScrollY = maxScrollY;
    }
}
