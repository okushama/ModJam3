package okushama.modjam;

import java.io.File;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.server.FMLServerHandler;

public class Ticker implements ITickHandler {

	public static long tick = 0L;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.PLAYER))) {
			onClientTick(tickData);
		}
		if (type.equals(EnumSet.of(TickType.RENDER))) {
			onPlaybackTick(tickData);
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.RENDER))) {
			onRenderTick(tickData);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER, TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "Ticker";
	}

	public int tickSwitch = 0;

	public void onClientTick(Object... tickData) {
		if (Minecraft.getMinecraft().theWorld != null) {
			if (MoCapHandler.target == null || MoCapHandler.target.isDead) {
				MoCapHandler.target = Minecraft.getMinecraft().thePlayer;
			}
			if(FMLClientHandler.instance() != null && FMLClientHandler.instance().getServer() != null){
				String worldName = FMLClientHandler.instance().getServer().worldServerForDimension(Minecraft.getMinecraft().thePlayer.dimension).getSaveHandler().getWorldDirectoryName();
				String currentDir = MoCapHandler.currentDirectory.getAbsolutePath();
				if(!currentDir.endsWith(worldName+File.separator+"mocap")){
					MoCapHandler.currentDirectory = new File("saves/"+worldName+"/mocap");
					if(!MoCapHandler.currentDirectory.exists()){
						MoCapHandler.currentDirectory.mkdir();
					}
					MoCapHandler.instance().populateRecordings();
				}
			}
		}
	}
	
	public void onPlaybackTick(Object... tickData){
		tick++;
		MoCapHandler.instance().recordTick();
		//MoCapHandler.instance().buffer.addState();
		if (MoCapHandler.instance().getCurrentRecording() != null) {
			if(tickData[0] instanceof Float){
				Float f = (Float)tickData[0];
				MoCapHandler.instance().getCurrentRecording().playback((float)f);
			}
		}
	}

	public void onRenderTick(Object... tickData) {
		if (Minecraft.getMinecraft().theWorld != null) {
			if(tickData[0] instanceof Float){
				Float f = (Float)tickData[0];
				Overlay.onRenderTick(f);
			}
		}
	}

}
