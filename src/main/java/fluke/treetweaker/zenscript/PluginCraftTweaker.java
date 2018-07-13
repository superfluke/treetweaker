package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;

public class PluginCraftTweaker 
{
	public static void init() 
	{
		CraftTweakerAPI.registerClass(TreeFactory.class);
	}
}
