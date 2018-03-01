package matteroverdrive.blocks.alien;

import matteroverdrive.Reference;
import matteroverdrive.world.dimensions.alien.BiomeAlienColorHelper;
import matteroverdrive.world.dimensions.alien.ColorizerAlien;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BlockStoneAlien extends Block {
    public BlockStoneAlien(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
    }
}
