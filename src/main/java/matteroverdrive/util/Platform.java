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

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumFacing;

public class Platform {
    private static boolean dev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    public static boolean isDev() {
        return dev;
    }

    public static EnumFacing rotateAround(EnumFacing forward, EnumFacing axis) {
        if (axis == null || forward == null) {
            return forward;
        }

        switch (forward) {
            case DOWN:
                switch (axis) {
                    case DOWN:
                        return forward;
                    case UP:
                        return forward;
                    case NORTH:
                        return EnumFacing.EAST;
                    case SOUTH:
                        return EnumFacing.WEST;
                    case EAST:
                        return EnumFacing.NORTH;
                    case WEST:
                        return EnumFacing.SOUTH;
                    default:
                        break;
                }
                break;
            case UP:
                switch (axis) {
                    case NORTH:
                        return EnumFacing.WEST;
                    case SOUTH:
                        return EnumFacing.EAST;
                    case EAST:
                        return EnumFacing.SOUTH;
                    case WEST:
                        return EnumFacing.NORTH;
                    default:
                        break;
                }
                break;
            case NORTH:
                switch (axis) {
                    case UP:
                        return EnumFacing.WEST;
                    case DOWN:
                        return EnumFacing.EAST;
                    case EAST:
                        return EnumFacing.UP;
                    case WEST:
                        return EnumFacing.DOWN;
                    default:
                        break;
                }
                break;
            case SOUTH:
                switch (axis) {
                    case UP:
                        return EnumFacing.EAST;
                    case DOWN:
                        return EnumFacing.WEST;
                    case EAST:
                        return EnumFacing.DOWN;
                    case WEST:
                        return EnumFacing.UP;
                    default:
                        break;
                }
                break;
            case EAST:
                switch (axis) {
                    case UP:
                        return EnumFacing.NORTH;
                    case DOWN:
                        return EnumFacing.SOUTH;
                    case NORTH:
                        return EnumFacing.UP;
                    case SOUTH:
                        return EnumFacing.DOWN;
                    default:
                        break;
                }
            case WEST:
                switch (axis) {
                    case UP:
                        return EnumFacing.SOUTH;
                    case DOWN:
                        return EnumFacing.NORTH;
                    case NORTH:
                        return EnumFacing.DOWN;
                    case SOUTH:
                        return EnumFacing.UP;
                    default:
                        break;
                }
            default:
                break;
        }
        return forward;
    }
}
