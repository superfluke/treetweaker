//Adapted from the Highlands mod by sdj64
//https://github.com/sdj64/Highlands/blob/master/src/main/java/com/sdj64/highlands/generator/WorldGenTreePalm.java

package fluke.treetweaker.world.treegen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;

public class TreeGenPalm extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.PALM;
    
    //this array is the 8 directions of x and y, used for palm trees.
    private int[][]directions = {{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};

	public TreeGenPalm(boolean doNotify) 
	{
		super(doNotify);
	}
	
	public TreeGenPalm(TreeRepresentation tree)
    {
    	super(false);
    	treeInfo = tree;
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        //generates trunk
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
	        
	        
	    	for(int i = 0; i < treeHeight; i++)
	    	{
	    		placeLogAt(worldIn, position.up(i));
	    	}
	    	//generates leaves
	    	int h = locY + treeHeight;
	    	BlockPos pos2 = new BlockPos(locX, h, locZ);
	    	placeLeafAt(worldIn, pos2);
	    	placeLeafAt(worldIn, pos2.up(1));
	    	int r = 1;
	    	genLeafAllDirections(worldIn, pos2, r);
	    	placeLeafAt(worldIn, pos2.up(1).east(1));
	    	placeLeafAt(worldIn, pos2.up(1).west(1));
	    	placeLeafAt(worldIn, pos2.up(1).north(1));
	    	placeLeafAt(worldIn, pos2.up(1).south(1));
	    	r++;
	    	genLeafAllDirections(worldIn, pos2, r);


	    	r++;
	    	genLeafAllDirections(worldIn, pos2, r);
	    	genLeafAllDirections(worldIn, pos2.down(), r);
	    	
	    	r++;
	    	genLeafAllDirections(worldIn, pos2.down(), r);
	
			return true;
        }
        else
        {
            return false;
        }
    }
	
	private void genLeafAllDirections(World world, BlockPos pos, int r)
	{
		for(int i = 0; i < directions.length; i++)
		{
			placeLeafAt(world, pos.east(directions[i][0]*r).north(directions[i][1]*r));
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
}
