package matteroverdrive.blocks;

import com.google.common.collect.ImmutableList;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.raytrace.Cuboid;
import matteroverdrive.raytrace.DistanceRayTraceResult;
import matteroverdrive.tile.pipes.TileEntityPipe;
import matteroverdrive.util.AABBUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockPipe<TE extends TileEntity> extends MOBlockContainer<TE> {

    public static final ImmutableList<PropertyBool> CONNECTED_PROPERTIES = ImmutableList.of(PropertyBool.create(EnumFacing.DOWN.getName()), PropertyBool.create(EnumFacing.UP.getName()), PropertyBool.create(EnumFacing.NORTH.getName()), PropertyBool.create(EnumFacing.SOUTH.getName()), PropertyBool.create(EnumFacing.WEST.getName()), PropertyBool.create(EnumFacing.EAST.getName()));

    private static final Cuboid CENTER = new Cuboid(0.0625 * 5, 0.0625 * 5, 0.0625 * 5, 0.0625 * 11, 0.0625 * 11, 0.0625 * 11);
    private static final Cuboid DOWN = new Cuboid(0.0625 * 5, 0, 0.0625 * 5, 0.0625 * 11, 0.0625 * 5, 0.0625 * 11);
    private static final List<Cuboid> CUBES = new ArrayList<>();
    private static Cuboid[] FACES;

    static {
        CUBES.add(CENTER);
        FACES = new Cuboid[6];
        for (EnumFacing facing : EnumFacing.VALUES) {
            FACES[facing.getIndex()] = new Cuboid(AABBUtils.rotateFace(DOWN.aabb(), facing));
            CUBES.add(FACES[facing.getIndex()]);
        }
    }

    public BlockPipe(Material material, String name) {
        super(material, name);
        this.useNeighborBrightness = true;
        this.setRotationType(-1);
    }

    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3d start, Vec3d end, Cuboid box) {
        Vec3d startRay = start.subtract(new Vec3d(pos));
        Vec3d endRay = end.subtract(new Vec3d(pos));
        RayTraceResult bbResult = box.aabb().calculateIntercept(startRay, endRay);

        if (bbResult != null) {
            Vec3d hitVec = bbResult.hitVec.add(new Vec3d(pos));
            EnumFacing sideHit = bbResult.sideHit;
            double dist = start.squareDistanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, pos, sideHit, box, dist);
        }
        return null;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), isConnectableSide(facing, world, pos));
        }
        return state;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CENTER.aabb();
    }

    @Nullable
    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        return rayTraceBoxesClosest(start, end, pos, CUBES);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        List<Cuboid> list = new ArrayList<>();
        list.add(CENTER);

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isConnectableSide(facing, worldIn, pos))
                list.add(FACES[facing.getIndex()]);
        }
        return rayTraceBoxesClosest(start, end, pos, list);
    }

    protected RayTraceResult rayTraceBoxesClosest(Vec3d start, Vec3d end, BlockPos pos, List<Cuboid> boxes) {
        List<DistanceRayTraceResult> results = new ArrayList<>();
        for (Cuboid box : boxes) {
            DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
            if (hit != null)
                results.add(hit);
        }
        RayTraceResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (DistanceRayTraceResult hit : results) {
            if (curClosest > hit.dist) {
                closestHit = hit;
                curClosest = hit.dist;
            }
        }
        return closestHit;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean isActualState) {
        AxisAlignedBB center = CENTER.aabb().offset(pos);

        if (mask.intersects(center)) {
            list.add(center);
        }

        for (EnumFacing side : EnumFacing.VALUES) {
            if (isConnectableSide(side, worldIn, pos)) {
                AxisAlignedBB sideBox = FACES[side.getIndex()].aabb().offset(pos);
                if (mask.intersects(sideBox)) {
                    list.add(sideBox);
                }
            }
        }
    }

    public boolean isConnectableSide(EnumFacing dir, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntityPipe && ((TileEntityPipe) tileEntity).isConnectableSide(dir);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

}
