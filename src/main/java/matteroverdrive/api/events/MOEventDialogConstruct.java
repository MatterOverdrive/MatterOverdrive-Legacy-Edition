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
package matteroverdrive.api.events;

import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Simeon on 1/25/2016.
 */
public class MOEventDialogConstruct extends PlayerEvent {
    public final IDialogNpc npc;
    public final IDialogMessage mainMessage;

    public MOEventDialogConstruct(IDialogNpc npc, EntityPlayer player, IDialogMessage mainMessage) {
        super(player);
        this.npc = npc;
        this.mainMessage = mainMessage;
    }

    public static class Pre extends MOEventDialogConstruct {
        public Pre(IDialogNpc npc, EntityPlayer player, IDialogMessage mainMessage) {
            super(npc, player, mainMessage);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends MOEventDialogConstruct {
        public Post(IDialogNpc npc, EntityPlayer player, IDialogMessage mainMessage) {
            super(npc, player, mainMessage);
        }
    }
}
