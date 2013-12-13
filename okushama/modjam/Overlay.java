package okushama.modjam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import static org.lwjgl.opengl.GL11.*;

public class Overlay {
	
	public static ResourceLocation iconPlay, iconStop, iconPause, iconRecord, iconLoop;
	
	public static long tick = 0L;
	public static long stopStamp = 0L;
	
	static{
		iconPlay = new ResourceLocation("mocap", "play.png");
		iconStop = new ResourceLocation("mocap", "stop.png");
		iconPause = new ResourceLocation("mocap", "pause.png");
		iconRecord = new ResourceLocation("mocap", "record.png");
		iconLoop = new ResourceLocation("mocap", "loop.png");
	}
	
	public static void onRenderTick(float pf){
		tick++;
		try{
		glPushMatrix();
		glColor4f(1f,1f,1f,1f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		MoCapRecording r = MoCapPlayback.instance().getCurrentRecording();
		if(r == null){
			glPopMatrix();
			return;
		}
		String out = r.title+" "+(r.currentTime/160)+"/"+(r.totalLength/160);
		Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, out, 10, 10,0xffffff);
		if(MoCapPlayback.instance().isRecording){
			if(tick % 100 > 30)
				renderIcon(iconRecord, 6, 20, false);
		}else
		if(MoCapPlayback.instance().isPlaying){
			if(MoCapPlayback.instance().isLooping){
				renderIcon(iconLoop, 6+32, 20, false);
			}
			renderIcon(iconPlay, 6, 20, MoCapPlayback.instance().isReverse);
			if(MoCapPlayback.instance().isPaused){
				renderIcon(iconPause, 6+16, 20, false);
			}
		}else{
			if(stopStamp+80 > tick){
				float diff = stopStamp+80 - tick;
				glColor4f(1f,1f,1f,diff/125);
				renderIcon(iconStop, 6, 20, false);
			}
		}
		glDisable(GL_BLEND);
		glPopMatrix();
		}catch(Exception e){
			MoCap.log(e.getMessage(), true);
		}
	}
	
	public static void renderIcon(ResourceLocation rl, float x, float y, boolean flip){
		glPushMatrix();
		glTranslatef(x, y, 0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(rl);
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		int size = 1;
		if(flip) size = -1;
		t.addVertexWithUV(16, 0, 0, size, 0);
		t.addVertexWithUV(0, 0, 0, 0, 0);
		t.addVertexWithUV(0, 16, 0, 0, 1);
		t.addVertexWithUV(16, 16, 0, size, 1);
		
		
		t.draw();
		glPopMatrix();
		
	}

}
