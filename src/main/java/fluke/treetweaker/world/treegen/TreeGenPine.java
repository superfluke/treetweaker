package fluke.treetweaker.world.treegen;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class TreeGenPine extends WorldGenAbstractTree 
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.PINE;

	public TreeGenPine(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenPine(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeheight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight; 
        int j = treeheight - rand.nextInt(2) - 3;
        int k = treeheight - j;
        int l = 1 + rand.nextInt(k + 1);

        if (position.getY() >= 1 && position.getY() + treeheight + 1 <= 256)
        {
            boolean flag = true;

            for (int i1 = position.getY(); i1 <= position.getY() + 1 + treeheight && flag; ++i1)
            {
                int j1 = 1;

                if (i1 - position.getY() < j)
                {
                    j1 = 0;
                }
                else
                {
                    j1 = l;
                }

                BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1)
                {
                    for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            if (!this.isReplaceable(worldIn, mutableblockpos.setPos(k1, i1, l1)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = position.down();
                IBlockState state = worldIn.getBlockState(down);
                boolean isSoil;
                
                if(treeInfo.validBaseBlock != null)
                	isSoil = (treeInfo.validBaseBlock == state);
                else 
                	isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);

                if (isSoil && position.getY() < 256 - treeheight - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, down, position);
                    int k2 = 0;

                    for (int l2 = position.getY() + treeheight; l2 >= position.getY() + j; --l2)
                    {
                        for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; ++j3)
                        {
                            int k3 = j3 - position.getX();

                            for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; ++i2)
                            {
                                int j2 = i2 - position.getZ();

                                if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                {
                                    BlockPos blockpos = new BlockPos(j3, l2, i2);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, treeInfo.leaf);
                                    }
                                }
                            }
                        }

                        if (k2 >= 1 && l2 == position.getY() + j + 1)
                        {
                            --k2;
                        }
                        else if (k2 < l)
                        {
                            ++k2;
                        }
                    }

                    for (int i3 = 0; i3 < treeheight - 1; ++i3)
                    {
                        BlockPos upN = position.up(i3);
                        state = worldIn.getBlockState(upN);

                        if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN))
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(i3), treeInfo.log);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

}
