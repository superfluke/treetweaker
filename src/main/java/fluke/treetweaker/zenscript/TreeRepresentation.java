package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.block.IBlock;
import crafttweaker.mc1120.block.MCBlockDefinition;
import crafttweaker.mc1120.block.MCItemBlock;
import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.world.treegen.TreeGenAcacia;
import fluke.treetweaker.world.treegen.TreeGenBraided;
import fluke.treetweaker.world.treegen.TreeGenCanopy;
import fluke.treetweaker.world.treegen.TreeGenEuca;
import fluke.treetweaker.world.treegen.TreeGenJungle;
import fluke.treetweaker.world.treegen.TreeGenLargeCanopy;
import fluke.treetweaker.world.treegen.TreeGenLargeOak;
import fluke.treetweaker.world.treegen.TreeGenOak;
import fluke.treetweaker.world.treegen.TreeGenPalm;
import fluke.treetweaker.world.treegen.TreeGenPine;
import fluke.treetweaker.world.treegen.TreeGenSpruce;
import fluke.treetweaker.world.treegen.TreeGenLargePine;
import fluke.treetweaker.world.treegen.TreeGenLargeSpruce;
import fluke.treetweaker.world.treegen.TreeGenMushroom;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import net.minecraftforge.common.BiomeDictionary;

public class TreeRepresentation 
{
	public static enum TreeType {OAK, LARGE_OAK, JUNGLE, CANOPY, LARGE_CANOPY, PINE, LARGE_PINE, SPRUCE, LARGE_SPRUCE, ACACIA, RED_MUSHROOM, BROWN_MUSHROOM, BRAIDED, PALM, EUCA, DEFAULT}
	public String treeName;
	public IBlockState log;
	public IBlockState leaf;
	public IBlockState validBaseBlock;
	public TreeType treeType;
	public int generationWeight;
	public Biome spawnBiome;
	public BiomeDictionary.Type spawnBiomeType;
	public int[] dimensionWhitelist;
	
	@ZenProperty
	public int minTreeHeight;
	@ZenProperty
	public int extraTreeHeight;
	@ZenProperty
	public int generationFrequency;
	@ZenProperty
	public int generationAttempts;
	@ZenProperty
	public boolean extraThick;
	@ZenProperty
	public boolean restrictSpawnRange;
	
	
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
		this.restrictSpawnRange = false;
		this.generationAttempts = 1;
		this.spawnBiome = null;
		this.validBaseBlock = null;
		this.spawnBiomeType = null;
		this.dimensionWhitelist = null;
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
				if(extraThick)
				{
					this.treeType = TreeType.LARGE_CANOPY;
					this.tree = new TreeGenLargeCanopy(this);
				}
				else
					this.tree = new TreeGenCanopy(this);
				break;
			case LARGE_CANOPY:
				this.tree = new TreeGenLargeCanopy(this);
				break;
			case JUNGLE:
				this.tree = new TreeGenJungle(this);
				break;
			case PINE:
				if(extraThick)
				{
					this.treeType = TreeType.LARGE_PINE;
					this.tree = new TreeGenLargePine(this);
				}
				else
					this.tree = new TreeGenPine(this);
				break;
			case LARGE_PINE:
				this.tree = new TreeGenLargePine(this);
				break;
			case SPRUCE:
				if(extraThick)
				{
					this.treeType = TreeType.LARGE_SPRUCE;
					this.tree = new TreeGenLargeSpruce(this);
				}
				else
					this.tree = new TreeGenSpruce(this);
				break;
			case LARGE_SPRUCE:
				this.tree = new TreeGenLargeSpruce(this);
				break;
			case ACACIA:
				this.tree = new TreeGenAcacia(this);
				break;
			case BRAIDED:
				this.restrictSpawnRange = true;
				this.tree = new TreeGenBraided(this);
				break;
			case PALM:
				this.tree = new TreeGenPalm(this);
				break;
			case EUCA:
				this.tree = new TreeGenEuca(this);
				break;
			case RED_MUSHROOM:
				//fall through
			case BROWN_MUSHROOM:
				this.tree = new TreeGenMushroom(this);
				break;
			default:
				CraftTweakerAPI.logWarning("Unknown tree type. Tree " + this.treeName + " defaulting to OAK");
				this.tree = new TreeGenOak(this);
		}
		extraTreeHeight += 1; //so rand function doesnt break if extra height is 0 and so the extra height generates from 0-num inclusive
		CraftTweakerAPI.logInfo("Adding " + this.treeType.toString() + " tree '" + this.treeName + "' to world gen");
		GameRegistry.registerWorldGenerator(new FlukeTreeGen(this.tree, generationFrequency, spawnBiome, spawnBiomeType, dimensionWhitelist, generationAttempts, restrictSpawnRange), generationWeight);
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
	public void setGenAttempts(int genAttempts)
	{
		if(genAttempts > 0)
		{
			this.generationAttempts = genAttempts;
		}
		else
		{
			CraftTweakerAPI.logWarning("generationAttempts must be > 0 for tree "  + this.treeName);
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
	
	@ZenMethod
	public void setBaseBlock(String block)
	{
		this.validBaseBlock = getStateFromString(block);
	}
	
	@ZenMethod
	public void setGenBiomeByTag(String tag)
	{
		this.spawnBiomeType = BiomeDictionary.Type.getType(tag);
	}
	
	@ZenMethod
	public void setDimWhitelist(int[] dims)
	{
		this.dimensionWhitelist = dims;
	}
	
	@ZenMethod
	public void setDimWhitelist(int dim)
	{
		this.dimensionWhitelist = new int[] {dim};
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
				CraftTweakerAPI.logWarning("Could not find block " + block + " for tree " + this.treeName + ". Defaulting to minecraft:dirt");
				return Blocks.DIRT.getDefaultState();
			}
			return blocky.getStateFromMeta(Integer.valueOf(splitty[2]));
		}
		else
		{
			blocky = Block.getBlockFromName(block);
			if(blocky == null)
			{
				CraftTweakerAPI.logWarning("Could not find block " + block + " for tree " + this.treeName + ". Defaulting to minecraft:dirt");
				return Blocks.DIRT.getDefaultState();
			}
			return blocky.getDefaultState();
		}
	}
}
