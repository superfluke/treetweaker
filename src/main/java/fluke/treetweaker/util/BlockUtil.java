package fluke.treetweaker.util;

import crafttweaker.CraftTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockUtil 
{
	//returns state from string with format of mod:block:meta with meta being optional
	//may return null
	public static IBlockState getStateFromString(String block)
	{
		String[] splitty = block.split(":");
		Block blocky;
		if(splitty.length > 2)
		{
			blocky = Block.getBlockFromName(splitty[0] + ":" + splitty[1]);
			return blocky==null? null : blocky.getStateFromMeta(Integer.valueOf(splitty[2]));
		}
		else
		{
			blocky = Block.getBlockFromName(block);
			return blocky==null? null : blocky.getDefaultState();
		}
	}

}
