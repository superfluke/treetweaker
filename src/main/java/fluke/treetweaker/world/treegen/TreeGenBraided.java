package fluke.treetweaker.world.treegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fluke.treetweaker.zenscript.TreeRepresentation;
import fluke.treetweaker.zenscript.TreeRepresentation.TreeType;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

//TODO check soil
public class TreeGenBraided extends WorldGenAbstractTree  
{
	protected TreeRepresentation treeInfo;
    protected TreeType treeType = TreeType.BRAIDED;
    public static double[] swirlCOS;
	public static double[] swirlSIN;
	public static final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	public TreeGenBraided(boolean doNotify) 
	{
		super(doNotify);
		if(swirlCOS == null)
			init_array();
	}
	
	public TreeGenBraided(TreeRepresentation tree)
    {
    	this(false);
    	treeInfo = tree;
    }
	
	//array of cos/sin values used to offset the trunk location, causing the swirl effect
	protected static void init_array()
	{
		double[] scos = new double[40];
		double[] ssin = new double[40];
		for(int n=0; n<40; n++)
		{
			double rads = (n) /( 2 * Math.PI);
			scos[n] = Math.cos(rads); 
			ssin[n] = Math.sin(rads*2);
		}
		swirlCOS = scos;
		swirlSIN = ssin;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
		position = position.up();
		int swirlHeight = rand.nextInt(treeInfo.extraTreeHeight) + treeInfo.minTreeHeight;
		int taperHeight = 5; //needs to be equal to or less than swirl size
		int capHeight = 3;
		int trunkHeight = swirlHeight + taperHeight;
		int treeHeight = swirlHeight+taperHeight+capHeight;
		int period = treeHeight;
		int swirlSize = 2 + (trunkHeight/10); //typically between 4-6. Controls how wide the braid is
		if (swirlSize < 4) 
			swirlSize = 4;
		int swirlSizePlus = swirlSize + 1;
		int baseYlevel = position.getY();
		int radius = 10 + (int)(swirlHeight * 0.16);
		int length = radius;
		if(swirlHeight < 10)
		{
			length = swirlHeight;
		}
		
		if(taperHeight > swirlSize)
		{
			taperHeight = swirlSize; //needs to be equal to or less than swirl size
			trunkHeight = swirlHeight + taperHeight;
			treeHeight = swirlHeight+taperHeight+capHeight;
		}
		
		if (baseYlevel >= 1 && baseYlevel + treeHeight + 1 <= worldIn.getHeight() && baseYlevel-3 > 0)
        {
			if(!isGenerationSpaceValid(worldIn, position, treeHeight, swirlSize, radius, length))
				return false;
			
			BlockPos[] rootLocations = getRootHeights(worldIn, position, swirlSize, swirlSizePlus);
			
			for(int i = 0; i < 3; i++) //make sure we found 3 valid root locations
				if(rootLocations[i] == null)
					return false;
			
			
			//roots
			for(int roots = 0; roots < 3; roots++)
			{
				BlockPos rootPos = rootLocations[roots];
				
				for(int x = -2; x <= 2; x++)
				{
					for(int z = -2; z <= 2; z++)
					{
						if(Math.abs(x)+Math.abs(z) > 2)
							continue;
						
						IBlockState state = worldIn.getBlockState(rootPos.add(x, -1, z));
						if(state != AIR)
						{
							if(treeInfo.log.getBlock() == Blocks.LOG || treeInfo.log.getBlock() == Blocks.LOG2)
								this.setBlockAndNotifyAdequately(worldIn, rootPos.add(x, -1, z), treeInfo.log.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X));
							else
								placeLogAt(worldIn, rootPos.add(x, -1, z));
						}
					}
				}
			}
			
			//start at -3 to account for roots slightly lower than starting position
			for(int treeY = -3; treeY < treeHeight; treeY++) //trunk
			{
				if(treeY < trunkHeight)
				{
					if(treeY >= swirlHeight)//taper swirl near top
					{
						swirlSize--;
						swirlSizePlus--;
					}

					int treeX = (int) (swirlCOS[(40+treeY)%40] * swirlSizePlus + 0.5); 
					int treeZ = (int) (swirlSIN[(40+treeY)%40] * swirlSize + 0.5);
	
					int tree2X = (int) (swirlCOS[(40+treeY+13)%40] * swirlSizePlus + 0.5);
					int tree2Z = (int) (swirlSIN[(40+treeY+13)%40] * swirlSize + 0.5);
					
					int tree3X = (int) (swirlCOS[(40+treeY+26)%40] * swirlSizePlus + 0.5);
					int tree3Z = (int) (swirlSIN[(40+treeY+26)%40] * swirlSize + 0.5);
					
					//draw a cross shape of logs
					for(int x = -1; x <= 1; x++)
					{
						for(int z = -1; z <= 1; z++)
						{
							if(Math.abs(x)+Math.abs(z) > 1)
								continue;
							
							if(treeY + baseYlevel >= rootLocations[0].getY())
							{
								placeLogAt(worldIn, position.add(treeX+x, treeY, treeZ+z));
								placeLogAt(worldIn, position.add(treeX+x, treeY+1, treeZ+z));
							}
							
							if(treeY + baseYlevel >= rootLocations[1].getY())
							{
								placeLogAt(worldIn, position.add(tree2X+x, treeY, tree2Z+z));
								placeLogAt(worldIn, position.add(tree2X+x, treeY+1, tree2Z+z));
							}
							
							if(treeY + baseYlevel >= rootLocations[2].getY())
							{
								placeLogAt(worldIn, position.add(tree3X+x, treeY, tree3Z+z));
								placeLogAt(worldIn, position.add(tree3X+x, treeY+1, tree3Z+z));
							}
						}
					}
				}
				else //draw the single trunk just under the canopy, aka cap height
				{
					for(int x = -1; x <= 1; x++)
					{
						for(int z = -1; z <= 1; z++)
						{
							if(Math.abs(x)+Math.abs(z) > 1)
								continue;
							
							placeLogAt(worldIn, position.add(x, treeY, z));
							
						}
					}
				}
			}
			
			//make the canopy
			drawQuadBezierSkeleton(worldIn, position.add(0, treeHeight, 0), radius, length);
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
	
	protected boolean isGenerationSpaceValid(World world, BlockPos treeCenter, int treeHeight, int swirlSizePlus, int radius, int length)
	{
		for(int y = 0; y < treeHeight; y++)
		{
			//tests the center of the tree a 4 points in a square where trunk will generate
			if(!isReplaceable(world, treeCenter.add(0, y, 0)))
				return false;
			
			if(!isReplaceable(world, treeCenter.add(swirlSizePlus, y, swirlSizePlus)))
				return false;
			
			if(!isReplaceable(world, treeCenter.add(-swirlSizePlus, y, swirlSizePlus)))
				return false;
			
			if(!isReplaceable(world, treeCenter.add(swirlSizePlus, y, -swirlSizePlus)))
				return false;
			
			if(!isReplaceable(world, treeCenter.add(-swirlSizePlus, y, -swirlSizePlus)))
				return false;
			
			if(y>=(treeHeight - length) && y < (treeHeight - length/2))
			{
				//tests furthest outside edge of hanging leaves on 4 cardinal directions
				if(!isReplaceable(world, treeCenter.add(radius+2, y, 0)))
					return false;
				
				if(!isReplaceable(world, treeCenter.add(-radius-2, y, 0)))
					return false;
				
				if(!isReplaceable(world, treeCenter.add(0, y, radius+2)))
					return false;
				
				if(!isReplaceable(world, treeCenter.add(0, y, -radius-2)))
					return false;
			}		
		}
		return true;
	}
	
	public boolean isReplaceable(World world, BlockPos pos)
    {
        net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos) || canGrowInto(state.getBlock());
    }
	
	//will return null Pos for roots where no valid position is found
	//otherwise will return 3 positions the roots should be placed at
	protected BlockPos[] getRootHeights(World world, BlockPos treeCenter, int swirlSize, int swirlSizePlus)
	{
		BlockPos[] rootPos = new BlockPos[3];
		
		for(int root = 0; root < 3; root++)
		{
			boolean foundSolid = false;
			//search +/- 3 blocks vertically for valid root location 
			for(int rootY = -3; rootY <= 3; rootY++)
			{
				int rootX = (int) (swirlCOS[(40+rootY+root*13)%40] * swirlSizePlus + 0.5); 
				int rootZ = (int) (swirlSIN[(40+rootY+root*13)%40] * swirlSize + 0.5);
				
				IBlockState state = world.getBlockState(treeCenter.add(rootX, rootY, rootZ));
				if(state == AIR)
				{
					BlockPos down = treeCenter.add(rootX, rootY-1, rootZ);
					IBlockState downstate = world.getBlockState(down);
					boolean isSoil;
					if(treeInfo.validBaseBlock != null)
	                	isSoil = (treeInfo.validBaseBlock == downstate);
	                else 
	                	isSoil = state.getBlock().canSustainPlant(downstate, world, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));
					
					if(isSoil)
						rootPos[root] = treeCenter.add(rootX, rootY, rootZ);
					break;
				}
				
			}
			
			if (rootPos[root] == null)
				break; //leave early if we didn't find a valid root location
		}
		
		return rootPos;
		
	}
	
	protected void drawQuadBezierSkeleton(World world, BlockPos start, int radius, int length)
	{
		BlockPos curvePoint = start.add(radius+2, 1, 0);
		BlockPos end = start.add(radius, -length, 0);
		int startX = start.getX();
		int startZ = start.getZ();
		int startY = start.getY();
		int shortY =  -(2*length/3);
		
		double xAngleTranslation = 0.7071; //Math.cos(Math.toRadians(45));
		double zAngleTranslation = 0.7071; //Math.sin(Math.toRadians(45));
		
		double xAngleTranslation2 = 0.9238; //Math.cos(Math.toRadians(22.5));
		double zAngleTranslation2 = 0.3826; //Math.sin(Math.toRadians(22.5));
		double xAngleTranslation3 = 0.3826; //Math.cos(Math.toRadians(67.5));
		double zAngleTranslation3 = 0.9238; //Math.sin(Math.toRadians(67.5));

		
		
		BlockPos[] quadArray = getQuadBezierArray(start, curvePoint, end);
		
		//build canopy from bottom up to reduce lighting updates
		for (int index = quadArray.length-1; index >= 0; index--)
		{
			BlockPos pixel = quadArray[index];
			int pxXoffset = pixel.getX() - startX;
			int pxZoffset = pixel.getZ() - startZ;
			int pxYoffset = pixel.getY() - startY;
			int pxDistance = pxXoffset; 
			
			//get x, z positions for branches at 45 degrees
			int angledX = (int)(pxDistance * xAngleTranslation + 0.5);
			int angledZ = (int)(pxDistance * zAngleTranslation + 0.5);
			
			//draw branches using midpoint circle method
			this.drawLogandLeaf(world, start.add(angledX, pxYoffset, angledZ));
			this.drawLogandLeaf(world, start.add(-angledX, pxYoffset, -angledZ));
			this.drawLogandLeaf(world, start.add(-angledZ, pxYoffset, angledX));
			this.drawLogandLeaf(world, start.add(angledZ, pxYoffset, -angledX));

			this.drawLogandLeaf(world, pixel);
			this.drawLogandLeaf(world, start.add(-pxXoffset, pxYoffset, -pxZoffset));
			this.drawLogandLeaf(world, start.add(pxZoffset, pxYoffset, pxXoffset));
			this.drawLogandLeaf(world, start.add(-pxZoffset, pxYoffset, -pxXoffset));
			
			//branches at 22.5 and 67.5 degrees are shorter
			if(pxYoffset > shortY)
			{
				pxXoffset *= 0.9;
				pxZoffset *= 0.9;
				pxDistance = pxXoffset;
				
				int angledX2 = (int)(pxDistance * xAngleTranslation2 + 0.5);
				int angledZ2 = (int)(pxDistance * zAngleTranslation2 + 0.5);
				this.drawLogandLeaf(world, start.add(angledX2, pxYoffset, angledZ2));
				this.drawLogandLeaf(world, start.add(-angledX2, pxYoffset, -angledZ2));
				this.drawLogandLeaf(world, start.add(-angledZ2, pxYoffset, angledX2));
				this.drawLogandLeaf(world, start.add(angledZ2, pxYoffset, -angledX2));
				
				int angledX3 = (int)(pxDistance * xAngleTranslation3 + 0.5);
				int angledZ3 = (int)(pxDistance * zAngleTranslation3 + 0.5);
				this.drawLogandLeaf(world, start.add(angledX3, pxYoffset, angledZ3));
				this.drawLogandLeaf(world, start.add(-angledX3, pxYoffset, -angledZ3));
				this.drawLogandLeaf(world, start.add(-angledZ3, pxYoffset, angledX3));
				this.drawLogandLeaf(world, start.add(angledZ3, pxYoffset, -angledX3));
			}
			
		}
	}
	
	protected void drawLogandLeaf(World world, BlockPos position)
	{
		//draw a cross shape, radius of 2
		for(int x = -2; x <= 2; x++)
		{
			for(int z = -2; z <= 2; z++)
			{
				if(Math.abs(x)+Math.abs(z) > 2)
					continue;
				
				//place log in middle of cross
				if(x == 0 && z == 0)
				{
					placeLogAt(world, position);
				}
				else
				{
					placeLeafAt(world, position.add(x, 0, z));
				}
			}
		}
		
		//place leaves above and below log
		placeLeafAt(world, position.add(0, 1, 0));
		placeLeafAt(world, position.add(0, -1, 0));
	}

	
	//100% black magic
	//draws a line between start and end point, curving the line based on curvePoint location
	//returns array of block positions of points on the final, curved line
	protected BlockPos[] getQuadBezierArray(BlockPos start, BlockPos curvePoint, BlockPos end)
	{  		
		ArrayList<BlockPos> linePoints = new ArrayList<BlockPos>();
		int x0 = start.getX();
		int y0 = start.getY();
		int x1 = curvePoint.getX();
		int y1 = curvePoint.getY();
		int x2 = end.getX();
		int y2 = end.getY();
		int z = start.getZ();
		int sx = x2-x1, sy = y2-y1;
		long xx = x0-x1, yy = y0-y1, xy;         /* relative values for checks */
		double dx, dy, err, ed, cur = xx*sy-yy*sx;                /* curvature */
	
		assert(xx*sx >= 0 && yy*sy >= 0);  /* sign of gradient must not change */
	
		if (sx*(long)sx+sy*(long)sy > xx*xx+yy*yy) 
		{ /* begin with longer part */ 
			x2 = x0; x0 = sx+x1; y2 = y0; y0 = sy+y1; cur = -cur; /* swap P0 P2 */
		}  
		if (cur != 0)
	    {                                                  /* no straight line */
			xx += sx; xx *= sx = x0 < x2 ? 1 : -1;          /* x step direction */
			yy += sy; yy *= sy = y0 < y2 ? 1 : -1;          /* y step direction */
			xy = 2*xx*yy; xx *= xx; yy *= yy;         /* differences 2nd degree */
			if (cur*sx*sy < 0) 
			{                          /* negated curvature? */
				xx = -xx; yy = -yy; xy = -xy; cur = -cur;
			}
			dx = 4.0*sy*(x1-x0)*cur+xx-xy;            /* differences 1st degree */
			dy = 4.0*sx*(y0-y1)*cur+yy-xy;
			xx += xx; yy += yy; err = dx+dy+xy;               /* error 1st step */
			do 
			{                              
				cur = Math.min(dx+xy,-xy-dy);
				ed = Math.max(dx+xy,-xy-dy);           /* approximate error distance */
				ed = 255/(ed+2*ed*cur*cur/(4.*ed*ed+cur*cur)); 
				linePoints.add(new BlockPos(x0, y0, z));          /* plot curve */
				if (x0 == x2 && y0 == y2) 
				{
					return linePoints.toArray(new BlockPos[0]);/* last pixel -> curve finished */
				}
				x1 = x0; cur = dx-err; y1 = 2*err+dy < 0? 1:0;
				if (2*err+dx > 0) 
				{                                    /* x step */
					if (err-dy < ed) 
						linePoints.add(new BlockPos(x0, y0+sy, z));
					x0 += sx; dx -= xy; err += dy += yy;
				}
				if (y1 != 0) 
				{                                              /* y step */
					if (cur < ed) 
						linePoints.add(new BlockPos(x1+sx, y0, z));
					y0 += sy; dy -= xy; err += dx += xx; 
				}
			} while (dy < dx);              /* gradient negates -> close curves */
	    }    
		/* plot remaining needle to end */
		Collections.addAll(linePoints, getBresehnamArrays(start.add(x0, y0, 0), start.add(x2, y2, 0))); 
		return linePoints.toArray(new BlockPos[0]);
	}
	

	//Draws a line from {x1, y1, z1} to {x2, y2, z2}
	protected void drawBresehnam(World world, BlockPos from, BlockPos to, IBlockState state) 
	{
		for (BlockPos pixel : getBresehnamArrays(from, to)) 
		{
			this.setBlockAndNotifyAdequately(world, pixel, state);
		}
	}

	//Get an array of values that represent a line from point A to point B
	public static BlockPos[] getBresehnamArrays(BlockPos src, BlockPos dest) 
	{
		return getBresehnamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
	}

	//Get an array of values that represent a line from point A to point B
	public static BlockPos[] getBresehnamArrays(int x1, int y1, int z1, int x2, int y2, int z2) 
	{
		int i, dx, dy, dz, absDx, absDy, absDz, x_inc, y_inc, z_inc, err_1, err_2, doubleAbsDx, doubleAbsDy, doubleAbsDz;

		BlockPos pixel = new BlockPos(x1, y1, z1);
		BlockPos lineArray[];

		dx = x2 - x1;
		dy = y2 - y1;
		dz = z2 - z1;
		x_inc = (dx < 0) ? -1 : 1;
		absDx = Math.abs(dx);
		y_inc = (dy < 0) ? -1 : 1;
		absDy = Math.abs(dy);
		z_inc = (dz < 0) ? -1 : 1;
		absDz = Math.abs(dz);
		doubleAbsDx = absDx << 1;
		doubleAbsDy = absDy << 1;
		doubleAbsDz = absDz << 1;

		if ((absDx >= absDy) && (absDx >= absDz)) {
			err_1 = doubleAbsDy - absDx;
			err_2 = doubleAbsDz - absDx;
			lineArray = new BlockPos[absDx + 1];
			for (i = 0; i < absDx; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.up(y_inc);
					err_1 -= doubleAbsDx;
				}
				if (err_2 > 0) {
					pixel = pixel.south(z_inc);
					err_2 -= doubleAbsDx;
				}
				err_1 += doubleAbsDy;
				err_2 += doubleAbsDz;
				pixel = pixel.east(x_inc);
			}
		} else if ((absDy >= absDx) && (absDy >= absDz)) {
			err_1 = doubleAbsDx - absDy;
			err_2 = doubleAbsDz - absDy;
			lineArray = new BlockPos[absDy + 1];
			for (i = 0; i < absDy; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.east(x_inc);
					err_1 -= doubleAbsDy;
				}
				if (err_2 > 0) {
					pixel = pixel.south(z_inc);
					err_2 -= doubleAbsDy;
				}
				err_1 += doubleAbsDx;
				err_2 += doubleAbsDz;
				pixel = pixel.up(y_inc);
			}
		} else {
			err_1 = doubleAbsDy - absDz;
			err_2 = doubleAbsDx - absDz;
			lineArray = new BlockPos[absDz + 1];
			for (i = 0; i < absDz; i++) {
				lineArray[i] = pixel;
				if (err_1 > 0) {
					pixel = pixel.up(y_inc);
					err_1 -= doubleAbsDz;
				}
				if (err_2 > 0) {
					pixel = pixel.east(x_inc);
					err_2 -= doubleAbsDz;
				}
				err_1 += doubleAbsDy;
				err_2 += doubleAbsDx;
				pixel = pixel.south(z_inc);
			}
		}
		lineArray[lineArray.length - 1] = pixel;

		return lineArray;
	}

}
