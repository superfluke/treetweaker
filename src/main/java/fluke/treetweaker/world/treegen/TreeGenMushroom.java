package fluke.treetweaker.world.treegen;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class TreeGenMushroom extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType;


	public TreeGenMushroom(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    	this.treeType = tree.treeType;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeheight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight; 

        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + treeheight + 1 < 256)
        {
            for (int j = position.getY(); j <= position.getY() + 1 + treeheight; ++j)
            {
                int k = 3;

                if (j <= position.getY() + 3)
                {
                    k = 0;
                }

                BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < 256)
                        {
                            IBlockState state = worldIn.getBlockState(mutableblockpos.setPos(l, j, i1));

                            if (!state.getBlock().isAir(state, worldIn, mutableblockpos) && !state.getBlock().isLeaves(state, worldIn, mutableblockpos))
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
            	IBlockState basestate = worldIn.getBlockState(position.down());
                Block block1 = basestate.getBlock();
                boolean isValidBase;
                
                if(treeInfo.validBaseBlock != null)
                {
                	isValidBase = (treeInfo.validBaseBlock == basestate);
                }
                else
                {
                	isValidBase = (block1 == Blocks.DIRT || block1 == Blocks.GRASS || block1 == Blocks.MYCELIUM);
                }
                
                if(!isValidBase)
                {
                	return false;
                }
                else
                {
                    int k2 = position.getY() + treeheight;

                    if (treeType == TreeRepresentation.TreeType.RED_MUSHROOM)
                    {
                        k2 = position.getY() + treeheight - 3;
                    }

                    for (int l2 = k2; l2 <= position.getY() + treeheight; ++l2)
                    {
                        int j3 = 1;

                        if (l2 < position.getY() + treeheight)
                        {
                            ++j3;
                        }

                        if (treeType == TreeRepresentation.TreeType.BROWN_MUSHROOM)
                        {
                            j3 = 3;
                        }

                        int k3 = position.getX() - j3;
                        int l3 = position.getX() + j3;
                        int j1 = position.getZ() - j3;
                        int k1 = position.getZ() + j3;

                        for (int l1 = k3; l1 <= l3; ++l1)
                        {
                            for (int i2 = j1; i2 <= k1; ++i2)
                            {
                                int j2 = 5;

                                if (l1 == k3)
                                {
                                    --j2;
                                }
                                else if (l1 == l3)
                                {
                                    ++j2;
                                }

                                if (i2 == j1)
                                {
                                    j2 -= 3;
                                }
                                else if (i2 == k1)
                                {
                                    j2 += 3;
                                }

                                BlockHugeMushroom.EnumType enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);

                                if (enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + treeheight)
                                {
                                    enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
                                }

                                if (position.getY() >= position.getY() + treeheight - 1 || enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE)
                                {
                                    BlockPos blockpos = new BlockPos(l1, l2, i2);
                                    IBlockState state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, treeInfo.leaf);
                                    }
                                }
                            }
                        }
                    }

                    for (int i3 = 0; i3 < treeheight; ++i3)
                    {
                        IBlockState iblockstate = worldIn.getBlockState(position.up(i3));

                        if (iblockstate.getBlock().canBeReplacedByLeaves(iblockstate, worldIn, position.up(i3)))
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(i3), treeInfo.log);
                        }
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
