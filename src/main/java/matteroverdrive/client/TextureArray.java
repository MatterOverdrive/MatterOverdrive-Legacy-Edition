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
package matteroverdrive.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings({"WeakerAccess", "unused"})
@SideOnly(Side.CLIENT)
public class TextureArray {
    protected TextureAtlasSprite particle;
    protected TextureAtlasSprite northTexture;
    protected TextureAtlasSprite southTexture;
    protected TextureAtlasSprite upTexture;
    protected TextureAtlasSprite downTexture;
    protected TextureAtlasSprite eastTexture;
    protected TextureAtlasSprite westTexture;

    public TextureArray(TextureAtlasSprite particle, TextureAtlasSprite northTexture, TextureAtlasSprite southTexture, TextureAtlasSprite upTexture, TextureAtlasSprite downTexture, TextureAtlasSprite eastTexture, TextureAtlasSprite westTexture) {
        this.particle = particle;
        this.northTexture = northTexture;
        this.southTexture = southTexture;
        this.upTexture = upTexture;
        this.downTexture = downTexture;
        this.eastTexture = eastTexture;
        this.westTexture = westTexture;
    }

    public TextureArray(TextureAtlasSprite texture) {
        this(texture, texture, texture, texture, texture, texture, texture);
    }

    public TextureArray() {
        this(Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
    }

    public TextureAtlasSprite getParticleTexture() {
        return particle != null ? particle : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    public TextureArray setParticleTexture(TextureAtlasSprite particle) {
        this.particle = particle;
        return this;
    }

    public TextureAtlasSprite[] getTextureArray() {
        return new TextureAtlasSprite[]{
                getNorthTexture(), getEastTexture(), getSouthTexture(), getWestTexture(), getUpTexture(), getDownTexture()
        };
    }

    public TextureAtlasSprite getNorthTexture() {
        return northTexture;
    }

    public TextureArray setNorthTexture(TextureAtlasSprite northTexture) {
        this.northTexture = northTexture;
        return this;
    }

    public TextureAtlasSprite getSouthTexture() {
        return southTexture;
    }

    public TextureArray setSouthTexture(TextureAtlasSprite southTexture) {
        this.southTexture = southTexture;
        return this;
    }

    public TextureAtlasSprite getUpTexture() {
        return upTexture;
    }

    public TextureArray setUpTexture(TextureAtlasSprite upTexture) {
        this.upTexture = upTexture;
        return this;
    }

    public TextureAtlasSprite getDownTexture() {
        return downTexture;
    }

    public TextureArray setDownTexture(TextureAtlasSprite downTexture) {
        this.downTexture = downTexture;
        return this;
    }

    public TextureAtlasSprite getEastTexture() {
        return eastTexture;
    }

    public TextureArray setEastTexture(TextureAtlasSprite eastTexture) {
        this.eastTexture = eastTexture;
        return this;
    }

    public TextureAtlasSprite getWestTexture() {
        return westTexture;
    }

    public TextureArray setWestTexture(TextureAtlasSprite westTexture) {
        this.westTexture = westTexture;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("particle", particle)
                .append("northTexture", northTexture)
                .append("southTexture", southTexture)
                .append("upTexture", upTexture)
                .append("downTexture", downTexture)
                .append("eastTexture", eastTexture)
                .append("westTexture", westTexture)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TextureArray that = (TextureArray) o;

        return new EqualsBuilder()
                .append(getParticleTexture(), that.getParticleTexture())
                .append(getNorthTexture(), that.getNorthTexture())
                .append(getSouthTexture(), that.getSouthTexture())
                .append(getUpTexture(), that.getUpTexture())
                .append(getDownTexture(), that.getDownTexture())
                .append(getEastTexture(), that.getEastTexture())
                .append(getWestTexture(), that.getWestTexture())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getParticleTexture())
                .append(getNorthTexture())
                .append(getSouthTexture())
                .append(getUpTexture())
                .append(getDownTexture())
                .append(getEastTexture())
                .append(getWestTexture())
                .toHashCode();
    }
}