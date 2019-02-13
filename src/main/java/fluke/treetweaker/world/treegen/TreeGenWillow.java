//Adapted from the Natura mod by alexbegt/progwml6
package fluke.treetweaker.world.treegen;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class TreeGenWillow extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;

    public TreeGenWillow(TreeRepresentation tree)
    {
    	super(false);
    	this.treeInfo = tree;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int height = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight; ;
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + height + 1 <= 256)
        {
            for (int j = position.getY(); j <= position.getY() + 1 + height; ++j)
            {
                int k = 1;

                if (j == position.getY())
                {
                    k = 0;
                }

                if (j >= position.getY() + 1 + height - 2)
                {
                    k = 3;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < 128)
                        {
                            IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1));
                            Block block = iblockstate.getBlock();

                            if (!iblockstate.getBlock().isAir(iblockstate, worldIn, blockpos$mutableblockpos.setPos(l, j, i1)) && !iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(l, j, i1)))
                            {
                                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER)
                                {
                                    flag = false;
                                }
                                else if (j > position.getY())
                                {
                                    flag = false;
                                }
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
                
                if (isSoil && position.getY() < 256 - height - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, position.down(), position);

                    for (int k1 = position.getY() - 3 + height; k1 <= position.getY() + height; ++k1)
                    {
                        int j2 = k1 - (position.getY() + height);
                        int l2 = 2 - j2 / 2;

                        for (int j3 = position.getX() - l2; j3 <= position.getX() + l2; ++j3)
                        {
                            int k3 = j3 - position.getX();

                            for (int i4 = position.getZ() - l2; i4 <= position.getZ() + l2; ++i4)
                            {
                                int j1 = i4 - position.getZ();

                                if (Math.abs(k3) != l2 || Math.abs(j1) != l2 || rand.nextInt(2) != 0 && j2 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(j3, k1, i4);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
                                    {
                                        this.setBlockAndMetadata(worldIn, blockpos, treeInfo.leaf);
                                    }
                                }
                            }
                        }
                    }

                    for (int l1 = 0; l1 < height; ++l1)
                    {
                        BlockPos upN = position.up(l1);
                        IBlockState iblockstate1 = worldIn.getBlockState(upN);
                        Block block2 = iblockstate1.getBlock();

                        if (block2.isAir(iblockstate1, worldIn, upN) || block2.isLeaves(iblockstate1, worldIn, upN) || block2.isReplaceable(worldIn, upN) || block2 == Blocks.FLOWING_WATER || block2 == Blocks.WATER)
                        {
                            this.setBlockAndMetadata(worldIn, position.up(l1), treeInfo.log);
                        }
                    }

                    for (int i2 = position.getY() - 3 + height; i2 <= position.getY() + height - 1; ++i2)
                    {
                        int k2 = i2 - (position.getY() + height);
                        int i3 = 2 - k2 / 2;
                        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                        for (int l3 = position.getX() - i3; l3 <= position.getX() + i3; ++l3)
                        {
                            for (int j4 = position.getZ() - i3; j4 <= position.getZ() + i3; ++j4)
                            {
                                blockpos$mutableblockpos1.setPos(l3, i2, j4);

                                if (worldIn.getBlockState(blockpos$mutableblockpos1) == treeInfo.leaf)
                                {
                                    BlockPos blockpos3 = blockpos$mutableblockpos1.west();
                                    BlockPos blockpos4 = blockpos$mutableblockpos1.east();
                                    BlockPos blockpos1 = blockpos$mutableblockpos1.north();
                                    BlockPos blockpos2 = blockpos$mutableblockpos1.south();

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos3))
                                    {
                                        this.addDownLeaves(worldIn, blockpos3);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos4))
                                    {
                                        this.addDownLeaves(worldIn, blockpos4);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos1))
                                    {
                                        this.addDownLeaves(worldIn, blockpos1);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos2))
                                    {
                                        this.addDownLeaves(worldIn, blockpos2);
                                    }
                                }
                            }
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

    private void addDownLeaves(World worldIn, BlockPos pos)
    {
        this.setBlockAndMetadata(worldIn, pos, treeInfo.leaf);
        int i = 4;

        for (pos = pos.down(); this.isAir(worldIn, pos) && i > 0; --i)
        {
            this.setBlockAndMetadata(worldIn, pos, treeInfo.leaf);
            pos = pos.down();
        }
    }

    protected void setBlockAndMetadata(World world, BlockPos pos, IBlockState stateNew)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos) || block.canPlaceBlockAt(world, pos) || world.getBlockState(pos) == treeInfo.leaf)
        {
            world.setBlockState(pos, stateNew, 2);
        }
    }

    private boolean isAir(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos);
    }

}