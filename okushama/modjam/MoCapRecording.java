package okushama.modjam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoCapRecording {
	
	public String title = "Recording";
	public String author;
	public ArrayList<State> recording = new ArrayList<State>();
	public long totalLength, currentTime = 0, originTime;
	
	public MoCapRecording(){
		originTime = System.currentTimeMillis();
	}
	
	public MoCapRecording(String a){
		this();
		author = a;
	}
	
	public MoCapRecording setTitle(String t){
		title = t;
		return this;
	}

	public void play(){
		if(!MoCapHandler.instance().isPlaying && !MoCapHandler.instance().isRecording){
			if(MoCapHandler.instance().isReverse){
				currentTime = totalLength-1;
			}
			MoCapHandler.instance().isPlaying = true;
		}	
	}
	
	public int flipflop = 0;
	
	public void playback(float partialTick){
		if(MoCapHandler.instance().isPlaying){
			if(!MoCapHandler.instance().isPaused){
				if(recording.size() > currentTime && currentTime > -1){
					recording.get((int)currentTime).setEntityData(partialTick);
					if(!MoCapHandler.instance().isReverse){
						if(MoCapHandler.instance().isSlowmo){
							if(flipflop == 0){
								currentTime++;
							}
								flipflop++;
							if(flipflop == 3){
								flipflop = 0;
							}
						}else{
							currentTime++;
						}
						if(currentTime == totalLength-1){
								stop(false);
						}
					}else{
						if(MoCapHandler.instance().isSlowmo){
							if(flipflop == 0){
								currentTime--;
							}
								flipflop++;
							if(flipflop == 3){
								flipflop = 0;
							}
						}else{
							currentTime--;
						}
						if(currentTime == 0){
								stop(false);
						}
					}
				}else{
					stop(false);
				}
			}
		}
	}
	
	public void stop(boolean forced){
		if(MoCapHandler.instance().isPlaying){
			MoCapHandler.instance().isPlaying = false;
			MoCapHandler.instance().isPaused = false;
			if(forced){
				MoCapHandler.instance().isLooping = false;
			}
			Overlay.stopStamp = Overlay.tick;
			if(!MoCapHandler.instance().isReverse){
				currentTime = 0;
			}else{
				currentTime = totalLength-1;
			}
			if(MoCapHandler.isLooping){
				play();
			}
		}
	}
	
	public void pause(){
		if(MoCapHandler.instance().isPlaying){
			MoCapHandler.instance().isPaused = !MoCapHandler.instance().isPaused;
		}
	}
	
	public void reverse(){
		MoCapHandler.instance().isReverse = !MoCapHandler.instance().isReverse;
	}
	
	public void slowmo(){
		MoCapHandler.instance().isSlowmo = !MoCapHandler.instance().isSlowmo;
	}
	
	public void addState(){
		State s = new State();
		if(MoCapHandler.target instanceof EntityPlayer){
			s = new PlayerState();
		}
		s.readEntityData();
		recording.add(s);
		totalLength = recording.size();
	}
	
	public static void save(MoCapRecording mcr){
		if(mcr instanceof MoCapBuffer){
			return;
		}
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String t = mcr.title;
			t = t.replaceAll(" ", "-");
			File fout = new File(MoCapHandler.currentDirectory, t+".json");
			if(!fout.exists()){
				fout.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout)));
			out.write(gson.toJson(mcr));
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static MoCapRecording load(String file){
		file = file.replaceAll(" ", "-");
		file += ".json";
		MoCapRecording out = null;
		try{
			Gson gson = new Gson();
			Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(MoCapHandler.currentDirectory, file))));
			out = gson.fromJson(in, MoCapRecording.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
