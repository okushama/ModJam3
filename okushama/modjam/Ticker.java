package okushama.modjam;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Ticker implements ITickHandler {

	public static long tick = 0L;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.CLIENT))) {
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
			if (MoCapPlayback.target == null || MoCapPlayback.target.isDead) {
				MoCapPlayback.target = Minecraft.getMinecraft().thePlayer;
			}
		}
	}
	
	public void onPlaybackTick(Object... tickData){
		tick++;
		MoCapPlayback.instance().recordTick();

		if (MoCapPlayback.instance().getCurrentRecording() != null) {
			//if(tickData[0] instanceof Float){
			//	Float f = (Float)tickData[0];
				MoCapPlayback.instance().getCurrentRecording().playback((float)1f);
			//}
		}
	}

	public void onRenderTick(Object... tickData) {
		if (Minecraft.getMinecraft().theWorld != null) {
			
			//rec/playback
			
			//
			
			String out = "";
			if (MoCapPlayback.instance().isRecording) {
				out = "REC";
			}
			if (MoCapPlayback.instance().getCurrentRecording() != null) {
				out = "Current: "
						+ MoCapPlayback.instance().getCurrentRecording().title;
			}
			
			if(tickData[0] instanceof Float){
				Float f = (Float)tickData[0];
				Overlay.onRenderTick(f);
			}
		}
	}

}
