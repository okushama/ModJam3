package okushama.modjam;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class Overlay {
	
	public static ResourceLocation iconPlay, iconStop, iconPause, iconRecord, iconLoop, iconTab, iconLine, iconNib;
	
	public static long tick = 0L;
	public static long stopStamp = 0L;
	public static boolean isVisible = true;
	
	static{
		iconPlay = new ResourceLocation("mocap", "play.png");
		iconStop = new ResourceLocation("mocap", "stop.png");
		iconPause = new ResourceLocation("mocap", "pause.png");
		iconRecord = new ResourceLocation("mocap", "record.png");
		iconLoop = new ResourceLocation("mocap", "loop.png");
		iconTab = new ResourceLocation("mocap", "tab.png");
		iconLine = new ResourceLocation("mocap", "line.png");
		iconNib = new ResourceLocation("mocap", "nib.png");
	}
	
	public static void onRenderTick(float pf){
		tick++;
		if(!isVisible || Minecraft.getMinecraft().currentScreen != null) return;
		try{
		glPushMatrix();
		glColor4f(1f,1f,1f,1f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		MoCapRecording r = MoCapHandler.instance().getCurrentRecording();
		if(r == null){
			glPopMatrix();
			return;
		}
		Date d = null;
		try{
			
			d = new Date(r.originTime);
		}catch(Exception e){
			
		}	
		String title = r.title;
		SimpleDateFormat sdf = new SimpleDateFormat();
		if(d != null){
			title = sdf.format(d);
		}
		String cur = title;
		try{
			cur = ((r.currentTime*100)/(r.totalLength-1))+"%";
		}catch(Exception e){
			
		}
		String out = title+" - "+cur;
		Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, out, 10, 10,0xffffff);
		if(MoCapHandler.instance().isRecording){
			if(tick % 100 > 30)
				renderIcon(iconRecord, 6, 20, false);
		}else
		if(MoCapHandler.instance().isPlaying){
			if(MoCapHandler.instance().isLooping){
				renderIcon(iconLoop, 6+32, 20, false);
			}
			renderIcon(iconPlay, 6, 20, MoCapHandler.instance().isReverse);
			if(MoCapHandler.instance().isPaused){
				renderIcon(iconPause, 6+16, 20, false);
			}
		}else{
			if(stopStamp+80 > tick){
				float diff = stopStamp+80 - tick;
				glPushMatrix();
				glColor4f(1f,1f,1f,diff/125);
				renderIcon(iconStop, 6, 20, false);
				glPopMatrix();
			}
		}
		glColor4f(1f,1f,1f,1f);
		for(int i = 0; i < 9; i++){
		renderIcon(iconLine, 12+(i*16), 36, false);
		}
		float perc = 1f;
		
		if(!(r.currentTime == 0L || r.totalLength == 0L)){
			perc = ((float)(r.currentTime*100/(r.totalLength-1)));
		}
		float nibX = ((perc/100)*140f);
		renderIcon(iconNib, 5+nibX, 36, false);
		
		
		renderIcon(iconTab, 4, 36, false);
		renderIcon(iconTab, 146, 36, false);
		glDisable(GL_BLEND);
		glPopMatrix();
		}catch(Exception e){
			//MoCap.log(e.getMessage(), true);
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
