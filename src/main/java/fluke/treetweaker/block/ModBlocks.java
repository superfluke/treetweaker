package fluke.treetweaker.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import fluke.treetweaker.TreeTweaker;

@GameRegistry.ObjectHolder(TreeTweaker.MODID)
@Mod.EventBusSubscriber(modid = TreeTweaker.MODID)
public class ModBlocks 
{
	
	@GameRegistry.ObjectHolder(BlockTestSapling.REG_NAME)
    public static BlockTestSapling testSapling;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		IForgeRegistry<Block> reggy = event.getRegistry();
		reggy.register(new BlockTestSapling());
	}
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		IForgeRegistry<Item> reggy = event.getRegistry();
		reggy.register(new ItemBlock(ModBlocks.testSapling).setRegistryName(ModBlocks.testSapling.getRegistryName()));

	}
	
	@SideOnly(Side.CLIENT)
    public static void initModels() 
	{
		testSapling.initModel();
	}

}
