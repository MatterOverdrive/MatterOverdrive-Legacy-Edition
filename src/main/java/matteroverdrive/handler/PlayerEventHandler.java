/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.handler;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.MOEventDialogConstruct;
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.events.MOEventScan;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Simeon on 5/9/2015.
 */
public class PlayerEventHandler {
    public final List<EntityPlayerMP> players;
    private final VersionCheckerHandler versionCheckerHandler;

    public PlayerEventHandler(ConfigurationHandler configurationHandler) {
        players = new ArrayList<>();
        versionCheckerHandler = new VersionCheckerHandler();

        configurationHandler.subscribe(versionCheckerHandler);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            if (MatterOverdrive.MATTER_REGISTRY.hasComplitedRegistration) {
                if (!event.player.getServer().isSinglePlayer()) {

                    MatterOverdrive.NETWORK.sendTo(new PacketUpdateMatterRegistry(MatterOverdrive.MATTER_REGISTRY), (EntityPlayerMP) event.player);

                }
            } else {
                players.add((EntityPlayerMP) event.player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //used to stop the item refreshing when using weapons and changing their data
        //this also happens on SMP and stops the beam weapons from working properly
        //Minecraft stops he using of items each time they change their NBT. This makes is so the weapons refresh and jitter.
        if (event.phase == TickEvent.Phase.START && event.player.isHandActive() && event.side == Side.CLIENT && event.player.getActiveHand() == EnumHand.MAIN_HAND) {
            ItemStack itemstack = event.player.inventory.getCurrentItem();
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof IWeapon && event.player.getItemInUseCount() > 0) {
                event.player.resetActiveHand();
                event.player.setActiveHand(EnumHand.MAIN_HAND);
            }
        }

        AndroidPlayer player = MOPlayerCapabilityProvider.GetAndroidCapability(event.player);
        if (player != null && event.phase.equals(TickEvent.Phase.START)) {
            if (event.side == Side.CLIENT) {
                onAndroidTickClient(player, event);

            } else {
                onAndroidServerTick(player, event);
            }
        }

        OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.player);
        if (extendedProperties != null && event.phase.equals(TickEvent.Phase.START)) {
            if (event.side == Side.CLIENT) {
                extendedProperties.update(Side.CLIENT);
            } else {
                extendedProperties.update(Side.SERVER);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void onAndroidTickClient(AndroidPlayer androidPlayer, TickEvent.PlayerTickEvent event) {
        if (event.player == Minecraft.getMinecraft().player) {
            androidPlayer.onAndroidTick(event.side);
        }
    }

    private void onAndroidServerTick(AndroidPlayer androidPlayer, TickEvent.PlayerTickEvent event) {
        androidPlayer.onAndroidTick(event.side);
    }

    @SubscribeEvent
    public void onPlayerLoadFromFile(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event) {
        AndroidPlayer player = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityPlayer());
        if (player != null) {
            player.onPlayerLoad(event);
        }
    }

    @SubscribeEvent
    public void onStartTracking(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer() != null) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityPlayer());
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                androidPlayer.sync(event.getEntityPlayer(), EnumSet.allOf(AndroidPlayer.DataType.class), false);
            }
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.sync(EnumSet.allOf(PlayerQuestData.DataType.class));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent deathEvent) {

    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (MatterOverdrive.MATTER_REGISTRY.hasComplitedRegistration) {
            for (int i = 0; i < MatterOverdrive.PLAYER_EVENT_HANDLER.players.size(); i++) {
                MatterOverdrive.NETWORK.sendTo(new PacketUpdateMatterRegistry(MatterOverdrive.MATTER_REGISTRY), MatterOverdrive.PLAYER_EVENT_HANDLER.players.get(i));
            }

            MatterOverdrive.PLAYER_EVENT_HANDLER.players.clear();
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.player != null) {
            if (event.player.world.isRemote) {
                if (!event.crafting.isEmpty() && event.crafting.getItem() instanceof MOBaseItem) {
                }
            } else {
                OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.player);
                if (extendedProperties != null) {
                    extendedProperties.onEvent(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFlyableFallEvent(PlayerFlyableFallEvent event) {
        if (event.getEntityPlayer() != null) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntity());
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                androidPlayer.triggerEventOnStats(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerScanEvent(MOEventScan event) {
        if (event.getSide() == Side.SERVER && event.getEntityPlayer() != null) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }

    /*@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
    {
            }*/

    @SubscribeEvent
    public void onBioticStatUse(MOEventBionicStat event) {
        if (event.getEntityPlayer().world.isRemote) {
        }
    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilUpdateEvent event) {
        if (!event.getLeft().isEmpty() && !event.getRight().isEmpty() && event.getLeft().getItem() == MatterOverdrive.ITEMS.portableDecomposer) {
            event.setOutput(event.getLeft().copy());
            event.setMaterialCost(1);
            event.setCost(3);
            MatterOverdrive.ITEMS.portableDecomposer.addStackToList(event.getOutput(), event.getRight());
        }
    }

    @SubscribeEvent
    public void onPlayerBlockInteract(PlayerInteractEvent event) {
        if (!event.getWorld().isRemote && event.getEntityPlayer() != null) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerOpenContainer(PlayerContainerEvent event) {
        if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onDialogMessageInteract(MOEventDialogInteract event) {
        if (event.getEntityPlayer() != null && event.side.equals(Side.SERVER)) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onDialogMessageConstruct(MOEventDialogConstruct event) {
        if (event.getEntityPlayer() != null) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }
}
