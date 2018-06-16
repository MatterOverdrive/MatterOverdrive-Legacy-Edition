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
package matteroverdrive.util;

import net.minecraft.util.math.MathHelper;

public class Vector3 {
    /**
     * X inate of Vec3D
     */
    public double x;
    /**
     * Y inate of Vec3D
     */
    public double y;
    /**
     * Z inate of Vec3D
     */
    public double z;

    public Vector3(double x, double y, double z) {
        this.set(x, y, z);
    }

    public Vector3 add(Vector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector3 subtract(Vector3 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vector3 scale(double value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    public Vector3 scale(Vector3 value) {
        this.x *= value.x;
        this.y *= value.y;
        this.z *= value.z;
        return this;
    }

    public Vector3 cross(Vector3 value) {
        this.set(this.y * value.z - this.z * value.y, this.z * value.x - this.x * value.z, this.x * value.y - this.y * value.x);
        return this;
    }

    public double distanceTo(Vector3 value) {
        double d0 = value.x - this.x;
        double d1 = value.y - this.y;
        double d2 = value.z - this.z;
        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(Vector3 value) {
        double d0 = value.x - this.x;
        double d1 = value.y - this.y;
        double d2 = value.z - this.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double length() {
        return (double) MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public double dot(Vector3 value) {
        return this.x * value.x + this.y * value.y + this.z * value.z;
    }

    public Vector3 normalize() {
        double d0 = (double) MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (d0 < 1.0E-4D) {
            this.set(0.0D, 0.0D, 0.0D);
        } else {
            this.x /= d0;
            this.y /= d0;
            this.z /= d0;
        }

        return this;
    }

    public Vector3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Rotates the vector around the x axis by the specified angle.
     */
    public void rotateAroundX(float angle) {
        angle *= Math.PI / 180.0;
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.x;
        double d1 = this.y * (double) f1 + this.z * (double) f2;
        double d2 = this.z * (double) f1 - this.y * (double) f2;
        this.set(d0, d1, d2);
    }

    /**
     * Rotates the vector around the y axis by the specified angle.
     */
    public void rotateAroundY(float angle) {
        angle *= Math.PI / 180.0;
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.x * (double) f1 + this.z * (double) f2;
        double d1 = this.y;
        double d2 = this.z * (double) f1 - this.x * (double) f2;
        this.set(d0, d1, d2);
    }

    /**
     * Rotates the vector around the z axis by the specified angle.
     */
    public void rotateAroundZ(float angle) {
        angle *= Math.PI / 180.0;
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.x * (double) f1 + this.y * (double) f2;
        double d1 = this.y * (double) f1 - this.x * (double) f2;
        double d2 = this.z;
        this.set(d0, d1, d2);
    }
}
