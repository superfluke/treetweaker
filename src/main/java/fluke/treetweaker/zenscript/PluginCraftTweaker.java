package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;

public class PluginCraftTweaker 
{
	public static void init() 
	{
		CraftTweakerAPI.registerClass(TreeFactory.class);
	}
}
