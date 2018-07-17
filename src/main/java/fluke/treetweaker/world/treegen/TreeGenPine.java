package fluke.treetweaker.world.treegen;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class TreeGenPine extends WorldGenHugeTrees
{
    protected TreeType treeType = TreeType.PINE;
	
	public TreeGenPine(TreeRepresentation tree)
    {
		super(false, tree.minTreeHeight, tree.extraTreeHeight, tree.log, tree.leaf);
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeheight = rand.nextInt(extraRandomHeight) + baseHeight;

        if (!this.ensureGrowable(worldIn, rand, position, treeheight))
        {
            return false;
        }
        else
        {
            this.createCrown(worldIn, position.getX(), position.getZ(), position.getY() + treeheight, 0, rand);

            for (int j = 0; j < treeheight; ++j)
            {
                if (isAirLeaves(worldIn, position.up(j)))
                {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
                }

                if (j < treeheight - 1)
                {
                    if (isAirLeaves(worldIn, position.add(1, j, 0)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
                    }

                    if (isAirLeaves(worldIn, position.add(1, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
                    }


                    if (isAirLeaves(worldIn, position.add(0, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }
	
	private void createCrown(World worldIn, int x, int z, int y, int trunktop, Random rand)
    {
        int i = rand.nextInt(5) + baseHeight;
        int j = 0;

        for (int k = y - i; k <= y; ++k)
        {
            int l = y - k;
            int i1 = trunktop + MathHelper.floor((float)l / (float)i * 3.5F);
            this.growLeavesLayerStrict(worldIn, new BlockPos(x, k, z), i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
            j = i1;
        }
    }

    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
    }

}
