package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.block.IBlock;
import crafttweaker.mc1120.block.MCBlockDefinition;
import crafttweaker.mc1120.block.MCItemBlock;
import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.world.treegen.TreeGenAcacia;
import fluke.treetweaker.world.treegen.TreeGenCanopy;
import fluke.treetweaker.world.treegen.TreeGenJungle;
import fluke.treetweaker.world.treegen.TreeGenLargeOak;
import fluke.treetweaker.world.treegen.TreeGenOak;
import fluke.treetweaker.world.treegen.TreeGenLargePine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

public class TreeRepresentation 
{
	public static enum TreeType {OAK, LARGE_OAK, JUNGLE, CANOPY, PINE, ACACIA, DEFAULT}
	public String treeName;
	public IBlockState log;
	public IBlockState leaf;
	public TreeType treeType;
	public int generationWeight;
	public Biome spawnBiome;
	
	@ZenProperty
	public int minTreeHeight;
	@ZenProperty
	public int extraTreeHeight;
	@ZenProperty
	public int generationFrequency;
	@ZenProperty
	public boolean extraThick;
	
	private WorldGenAbstractTree tree;
	
	public TreeRepresentation(String name)
	{
		this.treeName = name;
		this.log = Blocks.LOG.getDefaultState();
		this.leaf = Blocks.LEAVES.getDefaultState();
		this.minTreeHeight = 5;
		this.extraTreeHeight = 3;
		this.treeType = TreeType.DEFAULT;
		this.generationWeight = 2;
		this.generationFrequency = 5;
		this.spawnBiome = null;
	}
	
	@ZenMethod
	public void register() 
	{
		switch(treeType)
		{
			case OAK:
				this.tree = new TreeGenOak(this);
				break;
			case LARGE_OAK:
				this.tree = new TreeGenLargeOak(this);
				break;
			case CANOPY:
				this.tree = new TreeGenCanopy(this);
				break;
			case JUNGLE:
				this.tree = new TreeGenJungle(this);
				break;
			case PINE:
				this.tree = new TreeGenLargePine(this);
				break;
			case ACACIA:
				this.tree = new TreeGenAcacia(this);
				break;
			default:
				CraftTweakerAPI.logWarning("Unknown tree type. Tree " + this.treeName + " defaulting to OAK");
				this.tree = new TreeGenOak(this);
		}
		extraTreeHeight += 1;
		CraftTweakerAPI.logInfo("Adding tree '" + this.treeName + "' to world gen");
		GameRegistry.registerWorldGenerator(new FlukeTreeGen(this.tree, generationFrequency, spawnBiome), generationWeight);
	}
	
	@ZenMethod
	public void setLog(String logBlock)
	{
		this.log =  getStateFromString(logBlock);
	}
	
	@ZenMethod
	public void setLeaf(String leafBlock)
	{
		this.leaf = getStateFromString(leafBlock);
	}
	
	@ZenMethod
	public void setMinHeight(int minHeight)
	{
		if(minHeight > 0)
		{
			this.minTreeHeight = minHeight;
		}
		else
		{
			CraftTweakerAPI.logWarning("minHeight must be > 0 for tree "  + this.treeName);
		}
	}
	
	@ZenMethod
	public void setExtraHeight(int extraHeight)
	{
		if(extraHeight >= 0)
		{
			this.extraTreeHeight = extraHeight;
		}
		else
		{
			CraftTweakerAPI.logWarning("extraHeight cannot be < 0 for tree "  + this.treeName);
		}
	}
	
	@ZenMethod
	public void setTreeType(String type)
	{	
		try 
		{
			this.treeType = TreeType.valueOf(type);
        } 
		catch (IllegalArgumentException e) 
		{
        	CraftTweakerAPI.logWarning("Invalid type " + type + " for tree "  + this.treeName);
        	this.treeType = TreeType.DEFAULT;
		} 
	}
	
	@ZenMethod
	public void setGenFrequency(int frequency)
	{
		if(frequency > 0)
		{
			this.generationFrequency = frequency;
		}
		else
		{
			CraftTweakerAPI.logWarning("generationFrequency must be > 0 for tree "  + this.treeName);
		}
	}
	
	@ZenMethod
	public void setGenBiome(String biome)
	{
		this.spawnBiome = Biome.REGISTRY.getObject(new ResourceLocation(biome));
		if(this.spawnBiome == null)
		{
			CraftTweakerAPI.logWarning("Could not find biome " + biome + " for tree " + this.treeName);
		}
	}
	
	private IBlockState getStateFromString(String block)
	{
		String[] splitty = block.split(":");
		Block blocky;
		if(splitty.length > 2)
		{
			blocky = Block.getBlockFromName(splitty[0] + ":" + splitty[1]);
			if(blocky == null)
			{
				CraftTweakerAPI.logWarning("Could not find block " + block + " for tree " + this.treeName + ". Defaulting to minecraft:log");
				return Blocks.LOG.getDefaultState();
			}
			return blocky.getStateFromMeta(Integer.valueOf(splitty[2]));
		}
		else
		{
			blocky = Block.getBlockFromName(block);
			if(blocky == null)
			{
				CraftTweakerAPI.logWarning("Could not find block " + block + " for tree " + this.treeName + ". Defaulting to minecraft:log");
				return Blocks.LOG.getDefaultState();
			}
			return blocky.getDefaultState();
		}
	}
}
