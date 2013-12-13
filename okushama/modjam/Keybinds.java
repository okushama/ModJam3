package okushama.modjam;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class Keybinds extends KeyHandler{

	public static KeyBinding testBind;
	public static List keyBindings = new ArrayList<KeyBinding>();
	
	public static KeyBinding newKeybind(String label, int key){
		KeyBinding kb = new KeyBinding(label, key);
		keyBindings.add(kb);
		return kb;
	}
	
	static{
		testBind = newKeybind("Test Bind", Keyboard.KEY_G);
	}
	
	public static boolean[] getParArgs(){
		boolean[] out = new boolean[keyBindings.size()];
		for(int i =0; i < keyBindings.size(); i++){
			out[i] = false;
		}
		return out;
	}

	public Keybinds() {
		super((KeyBinding[])keyBindings.toArray(new KeyBinding[0]), getParArgs());
	}
	@Override
	public String getLabel() {
		return "MoCap Keybinds";
	}
	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		
	}
	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {

	}
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
