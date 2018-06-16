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
package matteroverdrive.client.data;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Created by Simeon on 2/20/2016.
 */
public class TextureAtlasSpriteParticle extends TextureAtlasSprite {
    private int frameSize;
    private int speed;
    private int animationCounter;
    private int tickCount;
    private int frameCountPerRow;
    private float frameSizeDeltaWidth;
    private float frameSizeDeltaHeight;

    public TextureAtlasSpriteParticle(String spriteName, int frameSize, int speed) {
        super(spriteName);
        this.frameSize = frameSize;
        this.speed = speed;
    }

    public TextureAtlasSpriteParticle copy() {
        TextureAtlasSpriteParticle sprite = new TextureAtlasSpriteParticle(this.getIconName(), frameSize, speed);
        sprite.copyFrom(this);
        sprite.calculate();
        return sprite;
    }

    private void calculate() {
        frameCountPerRow = getIconWidth() / frameSize;
        frameSizeDeltaWidth = (super.getMaxU() - super.getMinU()) / (float) frameCountPerRow;
        frameSizeDeltaHeight = (super.getMaxV() - super.getMinV()) / (float) frameCountPerRow;
    }

    public void updateParticleAnimation() {
        ++this.tickCount;
        if (tickCount >= speed) {
            tickCount = 0;
            if (animationCounter < (frameCountPerRow * frameCountPerRow) - 1) {
                animationCounter++;
            }
        }
    }

    /**
     * Returns the minimum U inate to use when rendering with this icon.
     */
    public float getMinU() {
        if (frameCountPerRow > 0) {
            return super.getMinU() + ((animationCounter % frameCountPerRow) * frameSizeDeltaWidth);
        }
        return super.getMinU();
    }

    /**
     * Returns the maximum U inate to use when rendering with this icon.
     */
    public float getMaxU() {
        if (frameCountPerRow > 0) {
            return super.getMinU() + ((animationCounter % frameCountPerRow) + 1) * frameSizeDeltaWidth;
        }
        return super.getMaxU();
    }

    /**
     * Returns the minimum V inate to use when rendering with this icon.
     */
    public float getMinV() {
        if (frameCountPerRow > 0) {
            float vOffset = ((float) Math.floor((animationCounter) / frameCountPerRow) * frameSizeDeltaHeight);
            return super.getMinV() + vOffset;
        }
        return super.getMinV();
    }

    /**
     * Returns the maximum V inate to use when rendering with this icon.
     */
    public float getMaxV() {
        if (frameCountPerRow > 0) {
            float vOffset = (((float) Math.floor((animationCounter) / frameCountPerRow) + 1) * frameSizeDeltaHeight);
            return super.getMinV() + vOffset;
        }
        return super.getMaxV();
    }
}
