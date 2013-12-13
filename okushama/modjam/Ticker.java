package okushama.modjam;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Ticker implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.CLIENT))) {
			onClientTick(tickData);
		}
		if (type.equals(EnumSet.of(TickType.RENDER))) {
			onRenderTick(tickData);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Ticker";
	}
	
	public int tickSwitch = 0;
	public void onClientTick(Object... tickData){
		if(MoCapPlayback.target == null){
			MoCapPlayback.target = Minecraft.getMinecraft().thePlayer;
		}
		MoCapPlayback.instance().recordTick();
		
		if(MoCapPlayback.instance().getCurrentRecording() != null){
			MoCapPlayback.instance().getCurrentRecording().playback();
		}
	}
	
	public void onRenderTick(Object... tickData){
		String out = "";
		if(MoCapPlayback.instance().isRecording){
			out = "REC";
		}
		if(MoCapPlayback.instance().getCurrentRecording() != null){
			out = "Current: "+MoCapPlayback.instance().getCurrentRecording().title;
		}
		Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, out, 10, 10, 0xffffff);
	}

}
