package fluke.treetweaker.zenscript;

import java.util.ArrayList;
import java.util.List;

import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import crafttweaker.CraftTweakerAPI;
import fluke.treetweaker.TreeTweaker;
import fluke.treetweaker.block.BlockTestSapling;
import fluke.treetweaker.world.FlukeTreeGen;
import net.minecraftforge.fml.common.registry.GameRegistry;


//TODO helix tree sapling broken
public class TreeRegistrar 
{
	public static List<TreeRepresentation> treesToRegister = new ArrayList<>();
	//public static List<BlockTestSapling> saplingsToRegister = new ArrayList<>();
	
	public static void registerTrees()
	{
		for(TreeRepresentation treeRep : treesToRegister)
		{
			treeRep.setTreeBlocksFromString(); //convert strings to blocks now that we're in init
			CraftTweakerAPI.logInfo("Adding " + treeRep.treeType.toString() + " tree '" + treeRep.treeName + "' to world gen");
			GameRegistry.registerWorldGenerator(new FlukeTreeGen(treeRep.tree, treeRep.generationFrequency, treeRep.spawnBiome, treeRep.spawnBiomeType, treeRep.dimensionWhitelist, treeRep.generationAttempts, treeRep.restrictSpawnRange), treeRep.generationWeight);

		}
		//GameRegistry.registerWorldGenerator(new FlukeTreeGen(this.tree, generationFrequency, spawnBiome, spawnBiomeType, dimensionWhitelist, generationAttempts, restrictSpawnRange), generationWeight);
	}
	
	public static void registerSaplings()
	{
		for(TreeRepresentation treeRep : treesToRegister)
		{
			if(treeRep.registerSapling)
			{
				CraftTweakerAPI.logInfo("Adding sapling for " + treeRep.treeName);
				TreeTweaker.instance.getRegistry(BlockRegistry.class, "BLOCK").register(new BlockTestSapling(treeRep));
			}
		}
	}

}
