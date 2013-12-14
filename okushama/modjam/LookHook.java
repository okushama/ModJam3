package okushama.modjam;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;

public class LookHook {
	
	public static MovingObjectPosition hook = Minecraft.getMinecraft().objectMouseOver;

	public static Entity getEntity(){
		if(hook == null){
			hook = Minecraft.getMinecraft().objectMouseOver;
			if(hook == null) return null;
		}
		return hook.entityHit;
	}
}
