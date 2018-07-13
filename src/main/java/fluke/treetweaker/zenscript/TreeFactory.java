package fluke.treetweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.treetweaker.TreeFactory")
public class TreeFactory 
{
	@ZenMethod
	public static TreeRepresentation createTree(String treeName) 
	{
		return new TreeRepresentation(treeName);
	}
	
}
