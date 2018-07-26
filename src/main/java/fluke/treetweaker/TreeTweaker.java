package fluke.treetweaker;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.zenscript.PluginCraftTweaker;
import fluke.treetweaker.proxy.CommonProxy;

@Mod(modid = TreeTweaker.MODID, name = TreeTweaker.NAME, version = TreeTweaker.VERSION, dependencies = TreeTweaker.DEPENDS)
public class TreeTweaker 
{

	public static final String MODID = "treetweaker";
	public static final String NAME = "TreeTweaker";
	public static final String VERSION = "1.2";
	public static final String DEPENDS = "required-after:crafttweaker;";

	@Instance(MODID)
	public static TreeTweaker instance;

	@SidedProxy(clientSide = "fluke.treetweaker.proxy.ClientProxy", serverSide = "fluke.treetweaker.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{

		logger = event.getModLog();
		proxy.init();
		PluginCraftTweaker.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) 
	{
//		GameRegistry.registerWorldGenerator(new FlukeTreeGen(), 2);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		
	}
}
