//Adapted from the Highlands mod by sdj64
//https://github.com/sdj64/Highlands/blob/master/src/main/java/com/sdj64/highlands/generator/WorldGenTreeEuca.java

package fluke.treetweaker.world.treegen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;

public class TreeGenEuca extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.EUCA;

	public TreeGenEuca(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenEuca(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int treeHeight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight;
        if(!isCubeClear(worldIn, position.up(2), 1, treeHeight))return false;
        
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
	        
	        //generates trunk
	    	for(int i = 0; i < treeHeight; i++)
	    	{
	    		placeLogAt(worldIn, position.down(-i));
	    	}
	    	//generates leaves
	    	int h = locY+treeHeight - 5;
	    	generateLeafLayerCircleNoise(worldIn, 1.3, locX, locZ, h, rand);
	    	generateLeafLayerCircleNoise(worldIn, 2.0, locX, locZ, (h+1), rand);
	    	int i;
	    	for(i = 1; i < rand.nextInt(2)+2; i++)
	    	{
	        	generateLeafLayerCircleNoise(worldIn, 2.5, locX, locZ, h+i+1, rand);
	    	}
	    	generateLeafLayerCircleNoise(worldIn, 3.5, locX, locZ, h+i+1, rand);
	    	generateLeafLayerCircleNoise(worldIn, 4.5, locX, locZ, h+i+2, rand);
	    	generateLeafLayerCircleNoise(worldIn, 2.5, locX, locZ, h+i+3, rand);
	    	generateLeafLayerCircleNoise(worldIn, 1.5, locX, locZ, h+i+4, rand);
	    	
	    	if(rand.nextBoolean())
	    	{
	    		h = locY + treeHeight - 8;
	    		generateLeafLayerCircleNoise(worldIn, 1.2, locX, locZ, (h-1), rand);
	    		generateLeafLayerCircleNoise(worldIn, 1.8, locX, locZ, h, rand);
	    		generateLeafLayerCircleNoise(worldIn, 1.2, locX, locZ, (h+1), rand);
	    	}
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
	
	//generates a circular disk of leaves around a coordinate block
    //noise means the outer block has a 50% chance of generating
    protected void generateLeafLayerCircleNoise(World world, double radius, int xo, int zo, int h, Random random)
    {
    	for(int x = (int)Math.ceil(xo - radius); x <= (int)Math.ceil(xo + radius); x++)
    	{
			for(int z = (int)Math.ceil(zo - radius); z <= (int)Math.ceil(zo + radius); z++)
			{
				double xfr = z - zo;
				double zfr = x- xo;
				
				if(xfr * xfr + zfr * zfr <= radius * radius){
					if(xfr * xfr + zfr * zfr <= (radius - 1) * (radius - 1) || random.nextInt(2) == 0)
					{
						placeLeafAt(world, new BlockPos(x, h, z));
					}
				}
			}
		}
    }

}
