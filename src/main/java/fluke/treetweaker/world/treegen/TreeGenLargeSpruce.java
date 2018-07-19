package fluke.treetweaker.world.treegen;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class TreeGenLargeSpruce extends WorldGenHugeTrees
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.PINE;
    	
	public TreeGenLargeSpruce(TreeRepresentation tree)
    {
		super(false, tree.minTreeHeight, tree.extraTreeHeight, tree.log, tree.leaf);
		treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeheight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight;

        if (!this.ensureGrowable(worldIn, rand, position, treeheight))
        {
            return false;
        }
        else
        {
            

            for (int j = 0; j < (treeheight*0.75); ++j)
            {
                if (isAirLeaves(worldIn, position.up(j)))
                {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(j), treeInfo.log);
                }

                if (j < treeheight - 1)
                {
                    if (isAirLeaves(worldIn, position.add(1, j, 0)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0),  treeInfo.log);
                    }

                    if (isAirLeaves(worldIn, position.add(1, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1),  treeInfo.log);
                    }


                    if (isAirLeaves(worldIn, position.add(0, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1),  treeInfo.log);
                    }
                }
            }
            this.createCrown(worldIn, position.getX(), position.getZ(), position.getY() + treeheight, 0, treeheight, rand);
            return true;
        }
    }
	
	private void createCrown(World worldIn, int x, int z, int y, int trunktop, int treeheight, Random rand)
    {
        int i = rand.nextInt((int)(treeheight/4)) + (int)(treeheight/2);
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
    
    private boolean ensureDirtsUnderneath(BlockPos pos, World worldIn)
    {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        boolean isSoil;
        
        if(treeInfo.validBaseBlock != null)
        	isSoil = (treeInfo.validBaseBlock == state);
        else
        	isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));

        if (isSoil && pos.getY() >= 2)
        {
            this.onPlantGrow(worldIn, blockpos, pos);
            this.onPlantGrow(worldIn, blockpos.east(), pos);
            this.onPlantGrow(worldIn, blockpos.south(), pos);
            this.onPlantGrow(worldIn, blockpos.south().east(), pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * returns whether or not a tree can grow at a specific position.
     * If it can, it generates surrounding dirt underneath.
     */
    protected boolean ensureGrowable(World worldIn, Random rand, BlockPos treePos, int height)
    {
        return this.isSpaceAt(worldIn, treePos, height) && this.ensureDirtsUnderneath(treePos, worldIn);
    }
    
  //Just a helper macro
    private void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        IBlockState state = world.getBlockState(pos);
        state.getBlock().onPlantGrow(state, world, pos, source);
    }

    /**
     * returns whether or not there is space for a tree to grow at a certain position
     */
    private boolean isSpaceAt(World worldIn, BlockPos leavesPos, int height)
    {
        boolean flag = true;

        if (leavesPos.getY() >= 1 && leavesPos.getY() + height + 1 <= 256)
        {
            for (int i = 0; i <= 1 + height; ++i)
            {
                int j = 2;

                if (i == 0)
                {
                    j = 1;
                }
                else if (i >= 1 + height - 2)
                {
                    j = 2;
                }

                for (int k = -j; k <= j && flag; ++k)
                {
                    for (int l = -j; l <= j && flag; ++l)
                    {
                        if (leavesPos.getY() + i < 0 || leavesPos.getY() + i >= 256 || !this.isReplaceable(worldIn,leavesPos.add(k, i, l)))
                        {
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        }
        else
        {
            return false;
        }
    }

}
