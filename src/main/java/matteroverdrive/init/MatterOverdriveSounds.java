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
package matteroverdrive.init;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.GameData;

import java.lang.reflect.Field;

/**
 * Created by Simeon on 4/7/2016.
 */
public class MatterOverdriveSounds {
    public final static SoundEvent weaponsPhaserBeam = newEvent("weapons.phaser_beam");
    public final static SoundEvent weaponsPhaserSwitchMode = newEvent("weapons.phaser_switch_mode");
    public final static SoundEvent scannerBeep = newEvent("scanner_beep");
    public final static SoundEvent scannerSuccess = newEvent("scanner_success");
    public final static SoundEvent scannerFail = newEvent("scanner_fail");
    public final static SoundEvent scannerScanning = newEvent("scanner_scanning");
    public final static SoundEvent replicateSuccess = newEvent("replicate_success");
    public final static SoundEvent analyzer = newEvent("analyzer");
    public final static SoundEvent decomposer = newEvent("decomposer");
    public final static SoundEvent machine = newEvent("machine");
    public final static SoundEvent transporter = newEvent("transporter");
    public final static SoundEvent windy = newEvent("windy");
    public final static SoundEvent anomalyConsume = newEvent("anomaly_consume");
    public final static SoundEvent electricMachine = newEvent("electric_machine");
    public final static SoundEvent forceField = newEvent("force_field");
    public final static SoundEvent failedAnimalDie = newEvent("failed_animal_die");
    public final static SoundEvent failedAnimalUdleChicken = newEvent("failed_animal_idle_chicken");
    public final static SoundEvent failedAnimalIdlePig = newEvent("failed_animal_idle_pig");
    public final static SoundEvent failedAnimalIdleCow = newEvent("failed_animal_idle_cow");
    public final static SoundEvent failedAnimalIdleSheep = newEvent("failed_animal_idle_sheep");
    public final static SoundEvent guiButtonSoft = newEvent("gui.button_soft");
    public final static SoundEvent guiButtonLoud = newEvent("gui.button_loud");
    public final static SoundEvent guiButtonExpand = newEvent("gui.button_expand");
    public final static SoundEvent guiBioticStatUnlock = newEvent("gui.biotic_stat_unlock");
    public final static SoundEvent musicTransformation = newEvent("music.transformation");
    public final static SoundEvent guiGlitch = newEvent("gui.glitch");
    public final static SoundEvent androidTeleport = newEvent("android.teleport");
    public final static SoundEvent androidShieldLoop = newEvent("android.shield_loop");
    public final static SoundEvent androidShieldHit = newEvent("android.shield_hit");
    public final static SoundEvent androidCloakOn = newEvent("android.cloak_on");
    public final static SoundEvent androidCloakOff = newEvent("android.cloak_off");
    public final static SoundEvent weaponsPhaserRifleShot = newEvent("weapons.phaser_rifle_shot");
    public final static SoundEvent weaponsOverheatAlarm = newEvent("weapons.overheat_alarm");
    public final static SoundEvent weaponsOverheat = newEvent("weapons.overheat");
    public final static SoundEvent weaponsReload = newEvent("weapons.reload");
    public final static SoundEvent androidNightVision = newEvent("android.night_vision");
    public final static SoundEvent androidPowerDown = newEvent("android.power_down");
    public final static SoundEvent weaponsBoltHit = newEvent("weapons.bolt_hit");
    public final static SoundEvent weaponsSizzle = newEvent("weapons.sizzle");
    public final static SoundEvent weaponsOmniToolHum = newEvent("weapons.omni_tool_hum");
    public final static SoundEvent blocksCrateOpen = newEvent("blocks.crate_open");
    public final static SoundEvent blocksCrateClose = newEvent("blocks.crate_close");
    public final static SoundEvent weaponsLaserRicochet = newEvent("weapons.laser_ricochet");
    public final static SoundEvent weaponsLaserFire = newEvent("weapons.laser_fire");
    public final static SoundEvent guiQuestComplete = newEvent("gui.quest_complete");
    public final static SoundEvent guiQuestStarted = newEvent("gui.quest_started");
    public final static SoundEvent weaponsPlasmaShotgunShot = newEvent("weapons.plasma_shotgun_shot");
    public final static SoundEvent weaponsPlasmaShotgunCharging = newEvent("weapons.plasma_shotgun_charging");
    public final static SoundEvent weaponsSniperRifleFire = newEvent("weapons.sniper_rifle_fire");
    public final static SoundEvent androidShieldPowerUp = newEvent("android.shield_power_up");
    public final static SoundEvent androidShieldPowerDown = newEvent("android.shield_power_down");
    public final static SoundEvent mobsRogueAndroidSay = newEvent("mobs.rogue_android_say");
    public final static SoundEvent mobsRogueAndroidDeath = newEvent("mobs.rogue_android_death");
    public final static SoundEvent androidShockwave = newEvent("android.shockwave");
    public final static SoundEvent fxElectricArc = newEvent("fx.electric_arc");
    public final static SoundEvent blocksPylon = newEvent("blocks.pylon");
    public final static SoundEvent weaponsExplosiveShot = newEvent("weapons.explosive_shot");

    private static SoundEvent newEvent(String name) {
        return new SoundEvent(new ResourceLocation(Reference.MOD_ID, name)).setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
    }

    public static void register() {
        for (Field field : MatterOverdriveSounds.class.getFields()) {
            if (field.getType() == SoundEvent.class) {
                try {
                    SoundEvent event = (SoundEvent) field.get(null);
                    GameData.register_impl(event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
