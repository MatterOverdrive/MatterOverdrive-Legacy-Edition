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
package matteroverdrive.sounds;

public final class MOSounds {

    public enum ShipMisc {

    }

    public enum MiscAi {
        CELESTIAL_IMPACT_EVAC,
        CELESTIAL_IMPACT_DEF,
        CELESTIAL_REMINDER,
        RADATION_MAX,
        BIO_ENEMY_DETECTED,
        NONBIO_ENEMY_DETECTED,
        HP_LOW,
        HP_CRIT
    }

    public enum StationAi {
        STARTUP,
        ARTIFICIAL_GRAV_ACTIVATE,
        ARTIFICIAL_GRAV_DEACTIVATE,
        LIGHTS_ON,
        LIGHTS_OFF,
        STAT_FUEL_LOW
    }

    public enum ShipAi {
        LIFESUPPORT_ACTIVATE,
        LIFESUPPORT_DEACTIVATE,
        POWERUNIT_ACTIVATE,
        POWERUNIT_DEACTIVATE,
        HYDRAULICS_ACTIVATE,
        HYDRAULICS_DEACTIVATE,
        TELEMETRICS_ACTIVATE,
        EXIT_VECTOR_CALCULATED,
        TELEMETRICS_DEACTIVATE,
        COMMS_ACTIVATE,
        COMMS_INCOMMING,
        COMMS_OUTGOING,
        COMMS_DEACTIVATE,
        COMPUTER_ACTIVATE,
        COMPUTER_DEACTIVATE,
        HULL_FAILURE,
        READY_LAUNCH,
        CONFIRM_COORDS,
        COUNTDOWN,
        APPROCH_ORBITAL_DEBRIS,
        APPROCH_UNKNOWN_OBSTICLE,
        ENGAGE_BOOST,
        ENGAGE_WARP,
        DISENGAGE_WARP,
        SHIELDS_ACTIVATE,
        SHIELDS_100,
        SHIELDS_50,
        SHIELDS_25,
        SHIELDS_15,
        SHIELDS_10,
        SHIELDS_5,
        SHIELDS_0,
        SHIP_FUEL_LOW,
        AUTOPILOT_ON,
        AUTOPILOT_OFF,
        POWER_FAILURE,
        MAJOR_HULL_DAMAGE,
        WARP_CORE_CRIT
    }

    public static final class Story {
        public enum Misc {
            INTRO,
        }
    }
}