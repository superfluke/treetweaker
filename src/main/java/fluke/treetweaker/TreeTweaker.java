package fluke.treetweaker;

import org.apache.logging.log4j.Logger;

import com.teamacronymcoders.base.BaseModFoundation;

import crafttweaker.CraftTweakerAPI;
import fluke.treetweaker.proxy.CommonProxy;
import fluke.treetweaker.zenscript.PluginCraftTweaker;
import fluke.treetweaker.zenscript.TreeRegistrar;
import fluke.treetweaker.zenscript.TreeRepresentation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TreeTweaker.MODID, name = TreeTweaker.NAME, version = TreeTweaker.VERSION, dependencies = TreeTweaker.DEPENDS)
public class TreeTweaker extends BaseModFoundation<TreeTweaker> 
{

	public static final String MODID = "treetweaker";
	public static final String NAME = "TreeTweaker";
	public static final String VERSION = "1.6.1";
	public static final String DEPENDS = "required-after:crafttweaker;required-after:base";

	@Instance(MODID)
	public static TreeTweaker instance;

	@SidedProxy(clientSide = "fluke.treetweaker.proxy.ClientProxy", serverSide = "fluke.treetweaker.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;
	public static boolean preInitDone = false;
	
	public TreeTweaker()
	{
		super(MODID, NAME, VERSION, CreativeTabs.MISC, true);
		PluginCraftTweaker.init();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
		logger = event.getModLog();
		proxy.init();

		//PluginCraftTweaker.init();
		TreeRegistrar.registerSaplings();
		preInitDone = true;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) 
	{
		super.init(event);
		TreeRegistrar.registerTrees();
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		super.postInit(event);
	}
	
	//@Override
	public TreeTweaker getInstance()
	{
		return this;
	}
	
	@Override
	public boolean hasExternalResources() 
	{
        return true;
    }
}
