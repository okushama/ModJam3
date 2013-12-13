package okushama.modjam;

import java.util.EnumSet;

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
	
	public void onClientTick(Object... tickData){
		
	}
	
	public void onRenderTick(Object... tickData){
		
	}

}
