package fluke.treetweaker.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy 
{
	public void init()
	{
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
	}
	
	@SubscribeEvent
	public void decorate(DecorateBiomeEvent.Decorate event) 
	{
		if(event.getType() == Decorate.EventType.TREE)
		{
//			System.out.println("fuck yo tree");
			event.setResult(Result.DENY);
		}
	}
}
