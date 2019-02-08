package fluke.treetweaker;

import net.minecraft.creativetab.CreativeTabs;
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
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import org.apache.logging.log4j.Logger;

import fluke.treetweaker.world.FlukeTreeGen;
import fluke.treetweaker.zenscript.PluginCraftTweaker;
import fluke.treetweaker.zenscript.SaplingScriptParser;
import fluke.treetweaker.block.BlockTestSapling;
import fluke.treetweaker.proxy.CommonProxy;

@Mod(modid = TreeTweaker.MODID, name = TreeTweaker.NAME, version = TreeTweaker.VERSION, dependencies = TreeTweaker.DEPENDS)
public class TreeTweaker extends BaseModFoundation<TreeTweaker> 
{

	public static final String MODID = "treetweaker";
	public static final String NAME = "TreeTweaker";
	public static final String VERSION = "1.6";
	public static final String DEPENDS = "required-after:crafttweaker;required-after:base";

	@Instance(MODID)
	public static TreeTweaker instance;

	@SidedProxy(clientSide = "fluke.treetweaker.proxy.ClientProxy", serverSide = "fluke.treetweaker.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;
	
	public TreeTweaker()
	{
		super(MODID, NAME, VERSION, CreativeTabs.MISC, true);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
		this.getRegistry(BlockRegistry.class, "BLOCK").register(new BlockTestSapling("testo"));
		logger = event.getModLog();
		proxy.init();
		PluginCraftTweaker.init();
		SaplingScriptParser.setupSaplings();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) 
	{
		super.init(event);
		
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
}
