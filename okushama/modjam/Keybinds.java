package okushama.modjam;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class Keybinds extends KeyHandler {

	public static KeyBinding play, pause, reverse, slowmo, stop, snapshot, record, next, prev;
	public static List keyBindings = new ArrayList<KeyBinding>();
	public Minecraft mc;

	public static KeyBinding newKeybind(String label, int key) {
		KeyBinding kb = new KeyBinding(label, key);
		keyBindings.add(kb);
		return kb;
	}

	static {
		play = newKeybind("Play", Keyboard.KEY_G);
		pause = newKeybind("Pause", Keyboard.KEY_G);
		reverse = newKeybind("Reverse", Keyboard.KEY_G);
		slowmo = newKeybind("Slow Motion", Keyboard.KEY_G);
		stop = newKeybind("Stop", Keyboard.KEY_G);
		snapshot = newKeybind("Snapshot", Keyboard.KEY_G);
		record = newKeybind("Record", Keyboard.KEY_G);
		next = newKeybind("Next", Keyboard.KEY_G);
		prev = newKeybind("Previous", Keyboard.KEY_G);
	}

	public static boolean[] getParArgs() {
		boolean[] out = new boolean[keyBindings.size()];
		for (int i = 0; i < keyBindings.size(); i++) {
			out[i] = false;
		}
		return out;
	}

	public Keybinds() {
		super((KeyBinding[]) keyBindings.toArray(new KeyBinding[0]),
				getParArgs());
		mc = Minecraft.getMinecraft();
	}

	@Override
	public String getLabel() {
		return "MoCap Keybinds";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {

	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if(kb.keyCode == play.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().getCurrentRecording().play();
		}
		if(kb.keyCode == pause.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().getCurrentRecording().pause();
		}
		if(kb.keyCode == stop.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().getCurrentRecording().stop();
		}
		if(kb.keyCode == reverse.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().getCurrentRecording().reverse();
		}
		if(kb.keyCode == next.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().nextRecording();
		}
		if(kb.keyCode == prev.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().prevRecording();
		}
		if(kb.keyCode == record.keyCode && mc.currentScreen == null && tickEnd){
			if(!MoCapPlayback.instance().isRecording){
				MoCapPlayback.instance().startRecording();
			}else{
				MoCapPlayback.instance().stopRecording();
			}
		}
		if(kb.keyCode == slowmo.keyCode && mc.currentScreen == null && tickEnd){
			MoCapPlayback.instance().isSlowmo = !MoCapPlayback.instance().isSlowmo;
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
