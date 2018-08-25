//Adapted from the Highlands mod by sdj64
//https://github.com/sdj64/Highlands/blob/49b968cf82ca18640f8006c6c676a10cd5b2454f/src/main/java/highlands/worldgen/WorldGenTreeCanopy.java
package fluke.treetweaker.world.treegen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;

public class TreeGenLargeCanopy extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.LARGE_CANOPY;

	public TreeGenLargeCanopy(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenLargeCanopy(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
    	int treeHeight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight;
		if(!isCubeClear(worldIn, position, 2, treeHeight))return false;

		BlockPos down = position.down();
        IBlockState state = worldIn.getBlockState(down);
        boolean isSoil;
        
        if(treeInfo.validBaseBlock != null)
        	isSoil = (treeInfo.validBaseBlock == state);
        else 
        	isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));
        
        if (isSoil && position.getY() < worldIn.getHeight() - treeHeight - 1)
        {
	        int locX = position.getX();
	        int locY = position.getY();
	        int locZ = position.getZ();
			//generates the trunk
	    	genTree(worldIn, rand, locX, locY, locZ, treeHeight);

			treeHeight+= 3;
			genTree(worldIn, rand, locX+1, locY, locZ, treeHeight);
			treeHeight+= 3;
			genTree(worldIn, rand, locX, locY, locZ+1, treeHeight);
			treeHeight+= 3;
			genTree(worldIn, rand, locX+1, locY, locZ+1, treeHeight);

	    	return true;
        }
        else
        {
        	return false;
        }
    }
	
	private void placeLogAt(World worldIn, BlockPos pos)
    {
        this.setBlockAndNotifyAdequately(worldIn, pos, treeInfo.log);
    }
	
	private void placeLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos))
        {
            this.setBlockAndNotifyAdequately(worldIn, pos, treeInfo.leaf);
        }
    }
	
	private boolean genTree(World world, Random random, int locX, int locY, int locZ, int treeHeight)
	{
    	for(int i = 0; i < treeHeight; i++)
    	{
    		placeLogAt(world, new BlockPos(locX, locY + i, locZ));
    	}
    	
    	int h = locY + treeHeight - 1;
    	//generate leaves above trunk
    	generateLeafLayerCircle(world, 5.5, locX, locZ, h);
    	h++;
    	generateLeafLayerCircle(world, 4.5, locX, locZ, h);

		h = locY + treeHeight - 6;
		//generates branch
		BlockPos branch = generateStraightBranch(world, 4, locX, h, locZ, random.nextInt(4));
		generateLeafLayerCircle(world, 4.5, branch.getX(), branch.getZ(), branch.getY());
		generateLeafLayerCircle(world, 3.5, branch.getX(), branch.getZ(), branch.getY()+1);
		return true;
    }
	
	protected boolean isCubeClear (World world, BlockPos pos, int radius, int height)
    {
    	int x = pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();
    	for(int i = x-radius; i <= x+radius; i++)
    	{
    		for(int k = z-radius; k <= z+radius; k++)
    		{
    			for(int j = y; j <= y+height; j++)
    			{
    				BlockPos pos2 = new BlockPos(i, j, k);
    				IBlockState state = world.getBlockState(pos2);
    				if(!(state.getBlock().isAir(state, world, pos2) || state.getBlock().isLeaves(state, world, pos)))
    					return false;
    			}
    		}
    	}
    	return true;
    }
	
	//generates a circular disk of leaves around a coordinate block, only overwriting air blocks.
    protected void generateLeafLayerCircle(World world, double radius, int xo, int zo, int h)
    {
    	for(int x = (int)Math.ceil(xo - radius); x <= (int)Math.ceil(xo + radius); x++)
    	{
			for(int z = (int)Math.ceil(zo - radius); z <= (int)Math.ceil(zo + radius); z++)
			{
				double xfr = z - zo;
				double zfr = x- xo;
				
				if(xfr * xfr + zfr * zfr <= radius * radius)
				{
					placeLeafAt(world, new BlockPos(x, h, z));
				}
			}
		}
    }
    
    //generate a branch, can be any direction
    //startHeight is absolute, not relative to the tree.
    //dir = direction: 0 = north (+z) 1 = east (+x) 2 = south 3 = west
    protected BlockPos generateStraightBranch(World world, int length, int locX, int locY, int locZ, int dir)
    {
    	int direction = -1;
    	if(dir < 2)
    		 direction = 1;
    	if(dir % 2 == 0){
    		//generates branch
    		for(int i = 1; i <= length; i++)
    		{
    			placeLogAt(world, new BlockPos(locX + i*direction, locY+i, locZ));
    		}
    		return new BlockPos(locX+length*direction, locY+length, locZ);
    	}
    	else
    	{
    		for(int i = 1; i <= length; i++)
    		{
    			placeLogAt(world, new BlockPos(locX, locY+i, locZ + i*direction));
    		}
    		return new BlockPos(locX, locY+length, locZ+length*direction);
    	}
    }

}
