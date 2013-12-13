package okushama.modjam;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "mocap", name="MoCap", version = "friday-0.0.1")
public class MoCap {
	
	@Instance("mocap")
	public static MoCap instance;
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		if(event.getSide() == Side.CLIENT){
			TickRegistry.registerTickHandler(new Ticker(), Side.CLIENT);
		}
	}
	
	public static Logger theLog = Logger.getLogger("MoCap");
	
	public static void log(String s, boolean b){
		if(!theLog.getParent().equals(FMLLog.getLogger())){
			theLog.setParent(FMLLog.getLogger());
		}
		if(!b)
			theLog.log(Level.INFO, s);
		else
			theLog.log(Level.WARNING, s);
	}
	
	public static void log(String s){
		log(s, false);
	}
}
