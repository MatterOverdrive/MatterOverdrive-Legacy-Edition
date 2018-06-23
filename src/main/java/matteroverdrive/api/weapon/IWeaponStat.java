package matteroverdrive.api.weapon;

import net.minecraft.util.IStringSerializable;

public interface IWeaponStat extends IStringSerializable {
    boolean isPositive(float value);
}
