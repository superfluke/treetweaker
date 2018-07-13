package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.block.IBlock;
import crafttweaker.mc1120.block.MCBlockDefinition;
import crafttweaker.mc1120.block.MCItemBlock;
import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.world.WorldGenTreesTest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

public class TreeRepresentation 
{

	public String treeName;
	public IBlockState log;
	public IBlockState leaf;
	
	@ZenProperty
	public int minTreeHeight;
	
	@ZenProperty
	public int extraTreeHeight;
	
	private WorldGenTreesTest tree;
	
	public TreeRepresentation(String name)
	{
		this.treeName = name;
		this.log = Blocks.GOLD_BLOCK.getDefaultState();
		this.leaf = Blocks.DIAMOND_BLOCK.getDefaultState();
		this.minTreeHeight = 5;
		this.extraTreeHeight = 3;
	}
	
	@ZenMethod
	public void register() 
	{
		int generationWeight = 2;
		this.tree = new WorldGenTreesTest(this);
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
