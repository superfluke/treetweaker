package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.block.IBlock;
import crafttweaker.mc1120.block.MCBlockDefinition;
import crafttweaker.mc1120.block.MCItemBlock;
import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.world.WorldGenTreesTest;
import fluke.treetweaker.world.treegen.TreeGenAcacia;
import fluke.treetweaker.world.treegen.TreeGenCanopy;
import fluke.treetweaker.world.treegen.TreeGenJungle;
import fluke.treetweaker.world.treegen.TreeGenOak;
import fluke.treetweaker.world.treegen.TreeGenPine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

public class TreeRepresentation 
{
	public static enum TreeType {OAK, JUNGLE, CANOPY, PINE, ACACIA}
	public String treeName;
	public IBlockState log;
	public IBlockState leaf;
	public TreeType treeType;
	
	@ZenProperty
	public int minTreeHeight;
	
	@ZenProperty
	public int extraTreeHeight;
	
	private WorldGenAbstractTree tree;
	
	public TreeRepresentation(String name)
	{
		this.treeName = name;
		this.log = Blocks.GOLD_BLOCK.getDefaultState();
		this.leaf = Blocks.DIAMOND_BLOCK.getDefaultState();
		this.minTreeHeight = 5;
		this.extraTreeHeight = 3;
		this.treeType = TreeType.JUNGLE;
	}
	
	@ZenMethod
	public void register() 
	{
		int generationWeight = 2;
		switch(treeType)
		{
			case OAK:
				this.tree = new TreeGenOak(this);
				break;
			case CANOPY:
				this.tree = new TreeGenCanopy(this);
				break;
			case JUNGLE:
				this.tree = new TreeGenJungle(this);
				break;
			case PINE:
				this.tree = new TreeGenPine(this);
				break;
			case ACACIA:
				this.tree = new TreeGenAcacia(this);
				break;
			default:
				this.tree = new WorldGenTreesTest(this);
		}
		
		CraftTweakerAPI.logInfo("Adding tree '" + this.treeName + "' to world gen");
		GameRegistry.registerWorldGenerator(new FlukeTreeGen(this.tree), generationWeight);
	}
	
	@ZenMethod
	public void setLog(String logBlock)
	{
		this.log =  Block.getBlockFromName(logBlock).getDefaultState();
	}
	
	@ZenMethod
	public void setLeaf(String leafBlock)
	{
		this.leaf =  Block.getBlockFromName(leafBlock).getDefaultState();
	}
	
	@ZenMethod
	public void setMinHeight(int minHeight)
	{
		this.minTreeHeight = minHeight;
	}
	
	@ZenMethod
	public void setExtraHeight(int extraHeight)
	{
		this.minTreeHeight = extraHeight;
	}
}
