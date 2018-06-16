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
package matteroverdrive.data.dialog;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 1/27/2016.
 */
public class DialogOptionReference implements IDialogOption {
    ResourceLocation dialogOptionName;

    public DialogOptionReference(ResourceLocation location) {
        this.dialogOptionName = location;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player) {
        IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
        if (message != null) {
            message.onInteract(npc, player);
        }
    }

    @Override
    public String getQuestionText(IDialogNpc npc, EntityPlayer player) {
        IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
        if (message != null) {
            return message.getQuestionText(npc, player);
        }
        return "Unknown";
    }

    @Override
    public boolean canInteract(IDialogNpc npc, EntityPlayer player) {
        IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
        if (message != null) {
            return message.canInteract(npc, player);
        }
        return false;
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
        if (message != null) {
            return message.isVisible(npc, player);
        }
        return true;
    }

    @Override
    public String getHoloIcon(IDialogNpc npc, EntityPlayer player) {
        IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
        if (message != null) {
            return message.getHoloIcon(npc, player);
        }
        return null;
    }

    @Override
    public boolean equalsOption(IDialogOption other) {
        if (other instanceof IDialogMessage) {
            IDialogMessage message = MatterOverdrive.DIALOG_REGISTRY.getMessage(dialogOptionName);
            if (message != null) {
                return message.equals(other);
            }
        } else if (other instanceof DialogOptionReference) {
            return dialogOptionName.equals(((DialogOptionReference) other).dialogOptionName);
        }
        return this.equals(other);
    }
}
