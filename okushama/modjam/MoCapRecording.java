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
	private boolean isPlaying = false, isPaused = false, isReverse = false;
	
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
		if(!isPlaying){
			if(isReverse){
				currentTime = totalLength-1;
			}
			isPlaying = true;
		}	
	}
	
	public void playback(){
		if(isPlaying){
			if(!isPaused){
				if(recording.size() > currentTime && currentTime > -1){
					recording.get((int)currentTime).setEntityData();
					if(!isReverse){
						currentTime++;
						if(currentTime == totalLength-1){
							stop();
						}
					}else{
						currentTime--;
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
		isPlaying = false;
		if(!isReverse)
			currentTime = 0;
		else
			currentTime = totalLength-1;
	}
	
	public void pause(){
		MoCap.log("Paused");
		if(!isPaused){
			isPaused = true;
		}
	}
	
	public void reverse(){
		MoCap.log("Reversing");
		isReverse = !isReverse;
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
