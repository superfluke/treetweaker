package fluke.treetweaker.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class FlukeTreeGen implements IWorldGenerator
{
	private WorldGenAbstractTree tree;
	
	public FlukeTreeGen(WorldGenAbstractTree tree)
	{
		this.tree = tree;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		
		if(random.nextInt(5) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			BlockPos pos = world.getHeight(new BlockPos(x, 0, z));
			int y = pos.getY();

			Biome biome = world.getBiomeForCoordsBody(new BlockPos(x,y,z));
			BiomeDecorator decor = biome.decorator;
//	    	boolean doGen = TerrainGen.decorate(world, random, pos, Decorate.EventType.TREE);
			if(decor.treesPerChunk > 0)
			{
				tree.generate(world, random, pos);
			}
		}
	}


}
