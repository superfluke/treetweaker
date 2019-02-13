package fluke.treetweaker.world.treegen;

import fluke.treetweaker.zenscript.TreeRepresentation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public abstract class WorldGenFlukeHugeTrees extends WorldGenHugeTrees
{
	protected TreeRepresentation treeInfo;
	
	public WorldGenFlukeHugeTrees(boolean notify, int baseHeightIn, int extraRandomHeightIn, IBlockState woodMetadataIn, IBlockState leavesMetadataIn) 
	{
		super(notify, baseHeightIn, extraRandomHeightIn, woodMetadataIn, leavesMetadataIn);
	}

	/**
     * grow leaves in a circle with the outsides being within the circle using treeInfo.leaf
     */
	@Override
    protected void growLeavesLayerStrict(World worldIn, BlockPos layerCenter, int width)
    {
        int i = width * width;

        for (int j = -width; j <= width + 1; ++j)
        {
            for (int k = -width; k <= width + 1; ++k)
            {
                int l = j - 1;
                int i1 = k - 1;

                if (j * j + k * k <= i || l * l + i1 * i1 <= i || j * j + i1 * i1 <= i || l * l + k * k <= i)
                {
                    BlockPos blockpos = layerCenter.add(j, 0, k);
                    IBlockState state = worldIn.getBlockState(blockpos);

                    if (state.getBlock().isAir(state, worldIn, blockpos) || state.getBlock().isLeaves(state, worldIn, blockpos))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.treeInfo.leaf);
                    }
                }
            }
        }
    }

    /**
     * grow leaves in a circle using treeInfo.leaf
     */
	@Override
    protected void growLeavesLayer(World worldIn, BlockPos layerCenter, int width)
    {
        int i = width * width;

        for (int j = -width; j <= width; ++j)
        {
            for (int k = -width; k <= width; ++k)
            {
                if (j * j + k * k <= i)
                {
                    BlockPos blockpos = layerCenter.add(j, 0, k);
                    IBlockState state = worldIn.getBlockState(blockpos);

                    if (state.getBlock().isAir(state, worldIn, blockpos) || state.getBlock().isLeaves(state, worldIn, blockpos))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.treeInfo.leaf);
                    }
                }
            }
        }
    }

}
