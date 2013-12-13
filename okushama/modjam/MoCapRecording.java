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
		MoCap.log("Playing");
		if(!MoCapPlayback.instance().isPlaying){
			if(MoCapPlayback.instance().isReverse){
				currentTime = totalLength-1;
			}
			MoCapPlayback.instance().isPlaying = true;
		}	
	}
	
	public int flipflop = 0;
	
	public void playback(float partialTick){
		if(MoCapPlayback.instance().isPlaying){
			if(!MoCapPlayback.instance().isPaused){
				if(recording.size() > currentTime && currentTime > -1){
					recording.get((int)currentTime).setEntityData(partialTick);
					if(!MoCapPlayback.instance().isReverse){
						if(MoCapPlayback.instance().isSlowmo){
							if(flipflop == 0){
								currentTime++;
								flipflop = 1;
							}else{
								flipflop = 0;
							}
						}else{
							currentTime++;
						}
						if(currentTime == totalLength-1){
							stop();
						}
					}else{
						if(MoCapPlayback.instance().isSlowmo){
							if(flipflop == 0){
								currentTime--;
								flipflop = 1;
							}else{
								flipflop = 0;
							}
						}else{
							currentTime--;
						}
						if(currentTime == 0){
							stop();
						}
					}
				}else{
					stop();
				}
			}
		}
	}
	
	public void stop(){
		MoCap.log("Stopping");
		MoCapPlayback.instance().isPlaying = false;
		if(!MoCapPlayback.instance().isReverse)
			currentTime = 0;
		else
			currentTime = totalLength-1;
	}
	
	public void pause(){
		MoCap.log("Paused");
		if(!MoCapPlayback.instance().isPaused){
			MoCapPlayback.instance().isPaused = true;
		}
	}
	
	public void reverse(){
		MoCap.log("Reversing");
		MoCapPlayback.instance().isReverse = !MoCapPlayback.instance().isReverse;
	}
	
	public void slowmo(){
		MoCap.log("Speed changed");
		MoCapPlayback.instance().isSlowmo = !MoCapPlayback.instance().isSlowmo;
	}
	
	public void addState(){
		State s = new State();
		if(MoCapPlayback.target instanceof EntityPlayer){
			s = new PlayerState();
		}
		s.readEntityData();
		recording.add(s);
		totalLength = recording.size();
	}
	
	public static void save(MoCapRecording mcr){
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String t = mcr.title;
			t = t.replaceAll(" ", "-");
			File fout = new File(MoCapPlayback.outFile, t+".json");
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
			Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(MoCapPlayback.outFile, file))));
			out = gson.fromJson(in, MoCapRecording.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
