package matteroverdrive.api.weapon;

import javax.annotation.Nonnull;

public enum WeaponStats implements IWeaponStat {
    DAMAGE {
        @Override
        public boolean isPositive(float value) {
            return value == 0;
        }
    },
    BLOCK_DAMAGE {
        @Override
        public boolean isPositive(float value) {
            return true;
        }
    },
    EXPLOSION_DAMAGE,
    FIRE_DAMAGE,
    AMMO,
    EFFECT,
    FIRE_RATE,
    HEAL {
        @Override
        public boolean isPositive(float value) {
            return value > 0;
        }
    },
    ACCURACY,
    MAX_HEAT,
    RANGE,
    RICOCHET;

    @Override
    public boolean isPositive(float value) {
        return false;
    }

    @Override
    @Nonnull
    public String getName() {
        return name().toLowerCase();
    }
}