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