package fluke.treetweaker.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	
	public FlukeTreeGen(WorldGenAbstractTree tree, int frequency, Biome biome, BiomeDictionary.Type biomeType)
	{
		this.tree = tree;
		this.genFrequency = frequency;
		this.spawnBiome = biome;
		this.spawnBiomeType = biomeType;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		
		if(random.nextInt(genFrequency) == 0){
			int x = (chunkX * 16) + random.nextInt(16) + 8;
			int z = (chunkZ * 16) + random.nextInt(16) + 8;
			BlockPos pos = world.getHeight(new BlockPos(x, 0, z));
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
