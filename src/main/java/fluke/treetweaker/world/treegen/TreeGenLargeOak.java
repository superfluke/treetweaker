package fluke.treetweaker.world.treegen;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class TreeGenLargeOak extends WorldGenAbstractTree 
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.LARGE_OAK;
    protected Random rand;
    public BlockPos basePos = BlockPos.ORIGIN;
    public int heightLimit;
    public int height;
    public double heightAttenuation = 0.618D;
    public double branchSlope = 0.381D;
    public double scaleWidth = 1.10D;
    public double leafDensity = 1.0D;
    public int trunkSize = 1;
    /** Sets the distance limit for how far away the generator will populate leaves from the base leaf node. */
    public int leafDistanceLimit = 4;
    public List<TreeGenLargeOak.FoliageCoordinates> foliageCoords;

	public TreeGenLargeOak(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenLargeOak(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    	if (tree.extraThick) trunkSize = 2;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        this.basePos = position;
        this.rand = new Random(rand.nextLong());
        
        this.heightLimit = treeInfo.minTreeHeight + this.rand.nextInt(treeInfo.extraTreeHeight);
        if (!this.validTreeLocation(worldIn))
        {
            return false;
        }
        else
        {
            this.generateLeafNodeList(worldIn);
            this.generateLeaves(worldIn);
            this.generateTrunk(worldIn);
            this.generateLeafNodeBases(worldIn);
            return true;
        }
    }
        
    public void generateLeafNodeList(World world)
    {
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit)
        {
            this.height = this.heightLimit - 1;
        }

        int i = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));

        if (i < 1)
        {
            i = 1;
        }

        int j = this.basePos.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;
        this.foliageCoords = Lists.<TreeGenLargeOak.FoliageCoordinates>newArrayList();
        this.foliageCoords.add(new TreeGenLargeOak.FoliageCoordinates(this.basePos.up(k), j));

        for (; k >= 0; --k)
        {
            float f = this.layerSize(k);

            if (f >= 0.0F)
            {
                for (int l = 0; l < i; ++l)
                {
                    double d0 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328D);
                    double d1 = (double)(this.rand.nextFloat() * 2.0F) * Math.PI;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockpos = this.basePos.add(d2, (double)(k - 1), d3);
                    BlockPos blockpos1 = blockpos.up(this.leafDistanceLimit);

                    if (this.checkBlockLine(blockpos, blockpos1, world) == -1)
                    {
                        int i1 = this.basePos.getX() - blockpos.getX();
                        int j1 = this.basePos.getZ() - blockpos.getZ();
                        double d4 = (double)blockpos.getY() - Math.sqrt((double)(i1 * i1 + j1 * j1)) * this.branchSlope;
                        int k1 = d4 > (double)j ? j : (int)d4;
                        BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());

                        if (this.checkBlockLine(blockpos2, blockpos, world) == -1)
                        {
                            this.foliageCoords.add(new TreeGenLargeOak.FoliageCoordinates(blockpos, blockpos2.getY()));
                        }
                    }
                }
            }
        }
    }

    public void crosSection(BlockPos pos, float leafSize, IBlockState leaf, World world)
    {
        int i = (int)((double)leafSize + 0.618D);

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                if (Math.pow((double)Math.abs(j) + 0.5D, 2.0D) + Math.pow((double)Math.abs(k) + 0.5D, 2.0D) <= (double)(leafSize * leafSize))
                {
                    BlockPos blockpos = pos.add(j, 0, k);
                    IBlockState state = world.getBlockState(blockpos);
                    if (state.getBlock().isAir(state, world, blockpos) || state.getBlock().isLeaves(state, world, blockpos))
                    {
                        this.setBlockAndNotifyAdequately(world, blockpos, leaf);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    public float layerSize(int y)
    {
        if ((float)y < (float)this.heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            float f = (float)this.heightLimit / 2.0F;
            float f1 = f - (float)y;
            float f2 = MathHelper.sqrt(f * f - f1 * f1);

            if (f1 == 0.0F)
            {
                f2 = f;
            }
            else if (Math.abs(f1) >= f)
            {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    public float leafSize(int y)
    {
        if (y >= 0 && y < this.leafDistanceLimit)
        {
            return y != 0 && y != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
        }
        else
        {
            return -1.0F;
        }
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    public void generateLeafNode(BlockPos pos, World world)
    {
        for (int i = 0; i < this.leafDistanceLimit; ++i)
        {
            this.crosSection(pos.up(i), this.leafSize(i), treeInfo.leaf, world);
        }
    }

    public void limb(BlockPos pos1, BlockPos pos2, IBlockState blockstate, World world)
    {
        BlockPos blockpos = pos2.add(-pos1.getX(), -pos1.getY(), -pos1.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        for (int j = 0; j <= i; ++j)
        {
            BlockPos blockpos1 = pos1.add((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));
        	this.setBlockAndNotifyAdequately(world, blockpos1, blockstate);      
        }
    }

    /**
     * Returns the absolute greatest distance in the BlockPos object.
     */
    private int getGreatestDistance(BlockPos posIn)
    {
        int i = MathHelper.abs(posIn.getX());
        int j = MathHelper.abs(posIn.getY());
        int k = MathHelper.abs(posIn.getZ());

        if (k > i && k > j)
        {
            return k;
        }
        else
        {
            return j > i ? j : i;
        }
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    public void generateLeaves(World world)
    {
        for (TreeGenLargeOak.FoliageCoordinates foliagecoordinates : this.foliageCoords)
        {
            this.generateLeafNode(foliagecoordinates, world);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    public boolean leafNodeNeedsBase(int y)
    {
        return (double)y >= (double)this.heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated.
     */
    public void generateTrunk(World world)
    {
        BlockPos blockpos = this.basePos;
        BlockPos blockpos1 = this.basePos.up(this.height);

        this.limb(blockpos, blockpos1, treeInfo.log, world);

        if (this.trunkSize == 2)
        {
            this.limb(blockpos.east(), blockpos1.east(), treeInfo.log, world);
            this.limb(blockpos.east().south(), blockpos1.east().south(), treeInfo.log, world);
            this.limb(blockpos.south(), blockpos1.south(), treeInfo.log, world);
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    public void generateLeafNodeBases(World world)
    {
        for (TreeGenLargeOak.FoliageCoordinates foliagecoordinates : this.foliageCoords)
        {
            int i = foliagecoordinates.getBranchBase();
            BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());

            if (!blockpos.equals(foliagecoordinates) && this.leafNodeNeedsBase(i - this.basePos.getY()))
            {
                this.limb(blockpos, foliagecoordinates, treeInfo.log, world);
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    public int checkBlockLine(BlockPos posOne, BlockPos posTwo, World world)
    {
        BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        if (i == 0)
        {
            return -1;
        }
        else
        {
            for (int j = 0; j <= i; ++j)
            {
                BlockPos blockpos1 = posOne.add((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));

                if (!this.isReplaceable(world, blockpos1))
                {
                    return j;
                }
            }

            return -1;
        }
    }
    
    public void setDecorationDefaults()
    {
        this.leafDistanceLimit = 5;
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    public boolean validTreeLocation(World world)
    {
        BlockPos down = this.basePos.down();
        net.minecraft.block.state.IBlockState state = world.getBlockState(down);
        boolean isSoil;
        
        if(treeInfo.validBaseBlock != null)
        	isSoil = (treeInfo.validBaseBlock == state);
        else
        	isSoil = state.getBlock().canSustainPlant(state, world, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));

        if (!isSoil)
        {
            return false;
        }
        else
        {
            int i = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1), world);

            if (i == -1)
            {
                return true;
            }
            else if (i < 6)
            {
                return false;
            }
            else
            {
                this.heightLimit = i;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos
    {
        private final int branchBase;

        public FoliageCoordinates(BlockPos pos, int branchY)
        {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.branchBase = branchY;
        }

        public int getBranchBase()
        {
            return this.branchBase;
        }
    }

}
