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
package matteroverdrive.compat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a class that is an inter-mod compatibility module.
 *
 * @author shadowfacts
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Compat {

    /**
     * The mod id that this compat module is for.
     */
    String value();

    /**
     * Used to mark a method of a compatibility module to be run in the pre-initialization phase.
     * Any method marked with this annotation must have 1 argument, a {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}.
     * Any method marked with this annotation must be static.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface PreInit {
    }

    /**
     * Used to mark a method of a compatibility module to be run in the initialization phase.
     * Any method marked with this annotation must have 1 argument, a {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}.
     * Any method marked with this annotation must be static.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Init {
    }

    /**
     * Used to mark a method of a compatibility module to be run in the post-initialization phase.
     * Any method marked with this annotation must have 1 argument, a {@link net.minecraftforge.fml.common.event.FMLPostInitializationEvent}.
     * Any method marked with this annotation must be static.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface PostInit {
    }

}
