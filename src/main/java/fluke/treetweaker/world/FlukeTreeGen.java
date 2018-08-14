package fluke.treetweaker.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class FlukeTreeGen implements IWorldGenerator
{
	private WorldGenAbstractTree tree;
	private int genFrequency;
	private Biome spawnBiome;
	private BiomeDictionary.Type spawnBiomeType;
	private int[] dimensionWhitelist;
	private int spawnRange;
	private int generationAttempts;
	
	private static Random treeRand = new Random(8008135);
	
	public FlukeTreeGen(WorldGenAbstractTree tree, int frequency, Biome biome, BiomeDictionary.Type biomeType, int[] dimWhitelist, int genAttempts, boolean restrictSpawnRange)
	{
		this.tree = tree;
		this.genFrequency = frequency;
		this.spawnBiome = biome;
		this.spawnBiomeType = biomeType;
		this.dimensionWhitelist = dimWhitelist;
		this.generationAttempts = genAttempts;
		if(restrictSpawnRange)
			spawnRange = 6;
		else
			spawnRange = 16;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		
		if(isValidDim(world.provider.getDimension()))
		{
			if(treeRand.nextInt(genFrequency) == 0)
			{
				for(int attempts = 0; attempts < generationAttempts; attempts++)
				{
					int x = (chunkX * 16) + random.nextInt(spawnRange) + 8;
					int z = (chunkZ * 16) + random.nextInt(spawnRange) + 8;
					BlockPos pos;
					if(world.provider instanceof WorldProviderHell)
					{
						pos = getNetherPos(world, x, treeRand.nextInt(65) + 20, z);
					}
					else
					{
						pos = world.getHeight(new BlockPos(x, 0, z));
					}
					
					int y = pos.getY();
					Biome biome = world.getBiomeForCoordsBody(new BlockPos(x,y,z));
					BiomeDecorator decor = biome.decorator;
		//	    	boolean doGen = TerrainGen.decorate(world, random, pos, Decorate.EventType.TREE);
					if(spawnBiome != null)
					{
						if(biome == spawnBiome)
						{
							tree.generate(world, random, pos);
						}
					}
					else if(spawnBiomeType != null)	
					{
						if(BiomeDictionary.hasType(biome, spawnBiomeType))
						{
							tree.generate(world, random, pos);
						}
					}
					else if(decor.treesPerChunk > 0)
					{
						tree.generate(world, random, pos);
					}
				}
			}
		}
	}
	
	private boolean isValidDim(int currentDim)
	{
		if(dimensionWhitelist == null)
		{
			return true;
		}
		else
		{
			for(int validDim: dimensionWhitelist)
			{
				if(validDim == currentDim)
					return true;
			}
			return false;
		}
	}
	
	BlockPos getNetherPos(World world, int x, int startY, int z)
	{	
		int y;
		boolean foundAir = false;
		
		for(y = startY; y > 0; y--)
		{
			IBlockState state = world.getBlockState(new BlockPos(x, y, z));
			Block block = state.getBlock();
			if(foundAir)
			{
				if (block == Blocks.NETHERRACK)
					break;
				else if (block == Blocks.SOUL_SAND)
					break;
				else if (block == Blocks.MAGMA)
					break;
				else if (block == Blocks.NETHER_BRICK)
					break;
				else if (block == Blocks.GLOWSTONE)
					break;
				
				Material matty = state.getMaterial();
				if(matty == Material.GROUND || matty == Material.GRASS || matty == Material.ROCK)
					break;
			}
			else
			{
				if(block == Blocks.AIR)
					foundAir = true;
			}
		}
		
		return new BlockPos(x, y+1, z);
	}

}
