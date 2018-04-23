package matteroverdrive.raytrace;

import net.minecraft.util.math.AxisAlignedBB;

public class Cuboid {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    public final int identifier;
    public final boolean collidable;

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int identifier) {
        this(minX, minY, minZ, maxX, maxY, maxZ, identifier, true);
    }

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(minX, minY, minZ, maxX, maxY, maxZ, 0, true);
    }

    public Cuboid(AxisAlignedBB bb) {
        this(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, 0, true);
    }

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int identifier, boolean collidable) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.identifier = identifier;
        this.collidable = collidable;
    }

    public AxisAlignedBB aabb() {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}