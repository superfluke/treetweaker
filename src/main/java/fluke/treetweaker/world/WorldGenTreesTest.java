package fluke.treetweaker.world;

import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

public class WorldGenTreesTest extends WorldGenAbstractTree
{
	protected IBlockState log = Blocks.LOG.getDefaultState();
    protected IBlockState leaf = Blocks.LEAVES.getDefaultState();
    protected int minTreeHeight = 5;
    protected int extraTreeHeight = 0;
    protected TreeType treeType = TreeType.OAK;
   
    public WorldGenTreesTest(boolean doNotify) 
    {
        super(doNotify);
    }
    
    public WorldGenTreesTest(TreeRepresentation tree)
    {
    	super(false);
    	this.log = tree.log;
    	this.leaf = tree.leaf;    
    	this.minTreeHeight = tree.minTreeHeight;
    	this.extraTreeHeight = tree.extraTreeHeight;
    	this.treeType = tree.treeType;
    }
    
    @Override
    public boolean generate(World parWorld, Random parRandom, BlockPos parBlockPos)
    {
    	
    	if(this.treeType == TreeType.OAK) 
    	{
    		return generateOak(parWorld, parRandom, parBlockPos);
    	}
    	
        int minHeight = parRandom.nextInt(extraTreeHeight) + minTreeHeight;
        
        // Check if tree fits in world
        if (parBlockPos.getY() >= 1 && parBlockPos.getY() + minHeight + 1 <= parWorld.getHeight())
        {
            if (!isSuitableLocation(parWorld, parBlockPos, minHeight))
            {
                return false;
            }
            else
            {
                IBlockState state = parWorld.getBlockState(parBlockPos.down());

                if (state.getBlock().canSustainPlant(state, parWorld, parBlockPos.down(), EnumFacing.UP, (IPlantable) Blocks.SAPLING) && parBlockPos.getY() < parWorld.getHeight() - minHeight - 1)
                {
                    state.getBlock().onPlantGrow(state, parWorld, parBlockPos.down(), parBlockPos);
                    generateTrunk(parWorld, parBlockPos, minHeight);
                    generateLeaves(parWorld, parBlockPos, minHeight, parRandom);
                    
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

    private void generateLeaves(World parWorld, BlockPos parBlockPos, int height, Random parRandom)
    {
        for (int foliageY = parBlockPos.getY() - 3 + height; foliageY <= parBlockPos.getY() + height; ++foliageY)
        {
            int foliageLayer = foliageY - (parBlockPos.getY() + height);
            int foliageLayerRadius = 1 - foliageLayer / 2;

            for (int foliageX = parBlockPos.getX() - foliageLayerRadius; foliageX <= parBlockPos.getX() + foliageLayerRadius; ++foliageX)
            {
                int foliageRelativeX = foliageX - parBlockPos.getX();

                for (int foliageZ = parBlockPos.getZ() - foliageLayerRadius; foliageZ <= parBlockPos.getZ() + foliageLayerRadius; ++foliageZ)
                {
                    int foliageRelativeZ = foliageZ - parBlockPos.getZ();

                    // Fill in layer with some randomness
                    if (Math.abs(foliageRelativeX) != foliageLayerRadius || Math.abs(foliageRelativeZ) != foliageLayerRadius || parRandom.nextInt(2) != 0 && foliageLayer != 0)
                    {
                        BlockPos blockPos = new BlockPos(foliageX, foliageY, foliageZ);
                        IBlockState state = parWorld.getBlockState(blockPos);

                        if (state.getBlock().isAir(state, parWorld, blockPos) || state.getBlock().isLeaves(state, parWorld, blockPos))
                        {
                            setBlockAndNotifyAdequately(parWorld, blockPos, leaf);
                        }
                    }
                }
            }
        }
    }

    private void generateTrunk(World parWorld, BlockPos parBlockPos, int minHeight)
    {
        for (int height = 0; height < minHeight; ++height)
        {
            BlockPos upN = parBlockPos.up(height);
            IBlockState state = parWorld.getBlockState(upN);

            if (state.getBlock().isAir(state, parWorld, upN) || state.getBlock().isLeaves(state, parWorld, upN))
            {
                setBlockAndNotifyAdequately(parWorld, parBlockPos.up(height), log);
            }
        }
    }
    
    private boolean isSuitableLocation(World parWorld, BlockPos parBlockPos, int minHeight)
    {
        boolean isSuitableLocation = true;
        
        for (int checkY = parBlockPos.getY(); checkY <= parBlockPos.getY() + 1 + minHeight; ++checkY)
        {
            // Handle increasing space towards top of tree
            int extraSpaceNeeded = 1;
            // Handle base location
            if (checkY == parBlockPos.getY())
            {
                extraSpaceNeeded = 0;
            }             
            // Handle top location
            if (checkY >= parBlockPos.getY() + 1 + minHeight - 2)
            {
                extraSpaceNeeded = 2;
            }

            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

            for (int checkX = parBlockPos.getX() - extraSpaceNeeded; checkX <= parBlockPos.getX() + extraSpaceNeeded && isSuitableLocation; ++checkX)
            {
                for (int checkZ = parBlockPos.getZ() - extraSpaceNeeded; checkZ <= parBlockPos.getZ() + extraSpaceNeeded && isSuitableLocation; ++checkZ)
                {
                    isSuitableLocation = isReplaceable(parWorld,blockPos.setPos(checkX, checkY, checkZ));
                }
            }
        }
        
        return isSuitableLocation;
    }
    
	public boolean generateOak(World worldIn, Random rand, BlockPos position)
    {
        int treeHeight = rand.nextInt(this.extraTreeHeight) + this.minTreeHeight;
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

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < worldIn.getHeight())
                        {
                            if (!this.isReplaceable(worldIn,blockpos$mutableblockpos.setPos(l, j, i1)))
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
                IBlockState state = worldIn.getBlockState(position.down());

                if (state.getBlock().canSustainPlant(state, worldIn, position.down(), net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING) && position.getY() < worldIn.getHeight() - treeHeight - 1)
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
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leaf);
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
                            this.setBlockAndNotifyAdequately(worldIn, position.up(j3), this.log);

//                            if (this.vinesGrow && j3 > 0)
//                            {
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, j3, 0)))
//                                {
//                                    this.addVine(worldIn, position.add(-1, j3, 0), BlockVine.EAST);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, j3, 0)))
//                                {
//                                    this.addVine(worldIn, position.add(1, j3, 0), BlockVine.WEST);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j3, -1)))
//                                {
//                                    this.addVine(worldIn, position.add(0, j3, -1), BlockVine.SOUTH);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j3, 1)))
//                                {
//                                    this.addVine(worldIn, position.add(0, j3, 1), BlockVine.NORTH);
//                                }
//                            }
                        }
                    }

//                    if (this.vinesGrow)
//                    {
//                        for (int k3 = position.getY() - 3 + i; k3 <= position.getY() + i; ++k3)
//                        {
//                            int j4 = k3 - (position.getY() + i);
//                            int k4 = 2 - j4 / 2;
//                            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
//
//                            for (int l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4)
//                            {
//                                for (int i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5)
//                                {
//                                    blockpos$mutableblockpos1.setPos(l4, k3, i5);
//
//                                    state = worldIn.getBlockState(blockpos$mutableblockpos1);
//                                    if (state.getBlock().isLeaves(state, worldIn, blockpos$mutableblockpos1))
//                                    {
//                                        BlockPos blockpos2 = blockpos$mutableblockpos1.west();
//                                        BlockPos blockpos3 = blockpos$mutableblockpos1.east();
//                                        BlockPos blockpos4 = blockpos$mutableblockpos1.north();
//                                        BlockPos blockpos1 = blockpos$mutableblockpos1.south();
//
//                                        if (rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos2))
//                                        {
//                                            this.addHangingVine(worldIn, blockpos2, BlockVine.EAST);
//                                        }
//
//                                        if (rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos3))
//                                        {
//                                            this.addHangingVine(worldIn, blockpos3, BlockVine.WEST);
//                                        }
//
//                                        if (rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos4))
//                                        {
//                                            this.addHangingVine(worldIn, blockpos4, BlockVine.SOUTH);
//                                        }
//
//                                        if (rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos1))
//                                        {
//                                            this.addHangingVine(worldIn, blockpos1, BlockVine.NORTH);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (rand.nextInt(5) == 0 && i > 5)
//                        {
//                            for (int l3 = 0; l3 < 2; ++l3)
//                            {
//                                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
//                                {
//                                    if (rand.nextInt(4 - l3) == 0)
//                                    {
//                                        EnumFacing enumfacing1 = enumfacing.getOpposite();
//                                        this.placeCocoa(worldIn, rand.nextInt(3), position.add(enumfacing1.getFrontOffsetX(), i - 5 + l3, enumfacing1.getFrontOffsetZ()), enumfacing);
//                                    }
//                                }
//                            }
//                        }
//                    }

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
