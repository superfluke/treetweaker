package fluke.treetweaker.world.treegen;


import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class TreeGenOak extends WorldGenAbstractTree 
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.OAK;

	public TreeGenOak(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenOak(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeHeight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight;
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + treeHeight + 1 <= worldIn.getHeight())
        {
            for (int j = position.getY(); j <= position.getY() + 1 + treeHeight; ++j)
            {
                int k = 1;

                if (j == position.getY())
                {
                    k = 0;
                }

                if (j >= position.getY() + 1 + treeHeight - 2)
                {
                    k = 2;
                }

                BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < worldIn.getHeight())
                        {
                            if (!this.isReplaceable(worldIn, mutableblockpos.setPos(l, j, i1)))
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
                	isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));


                if (isSoil && position.getY() < worldIn.getHeight() - treeHeight - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
                    int k2 = 3;
                    int l2 = 0;

                    for (int i3 = position.getY() - 3 + treeHeight; i3 <= position.getY() + treeHeight; ++i3)
                    {
                        int i4 = i3 - (position.getY() + treeHeight);
                        int j1 = 1 - i4 / 2;

                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1)
                        {
                            int l1 = k1 - position.getX();

                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2)
                            {
                                int j2 = i2 - position.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(k1, i3, i2);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().isAir(state, worldIn, blockpos) || state.getBlock().isLeaves(state, worldIn, blockpos) || state.getMaterial() == Material.VINE)
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, treeInfo.leaf);
                                    }
                                }
                            }
                        }
                    }

                    for (int j3 = 0; j3 < treeHeight; ++j3)
                    {
                        BlockPos upN = position.up(j3);
                        state = worldIn.getBlockState(upN);

                        if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN) || state.getMaterial() == Material.VINE)
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(j3), treeInfo.log);

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
