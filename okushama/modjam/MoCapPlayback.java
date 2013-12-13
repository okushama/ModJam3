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
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoCapPlayback {

	private static MoCapPlayback instance;
	private int CURRENT = 0;
	public static EntityLivingBase target;
	public static File outFile = new File("mocap");
	public static boolean isRecording = false;
	public static boolean isSlowmo = false;
	public static boolean isReverse = false;
	public static boolean isPlaying = false;
	public static boolean isPaused = false;
	public static boolean isLooping = false;
	
	private ArrayList<MoCapRecording> cachedRecordings = new ArrayList<MoCapRecording>();
	
	public void startRecording(){
		if(!isRecording){
			MoCap.log("Started recording");
			MoCapRecording newRec = new MoCapRecording();
			cachedRecordings.add(newRec);
			CURRENT = cachedRecordings.size()-1;
			isRecording = true;
		}
	}
	
	public void stopRecording(){
		if(isRecording){
			MoCap.log("Stopped recording");
			isRecording = false;
			MoCapRecording.save(this.getCurrentRecording().setTitle(System.currentTimeMillis()+" mocap"));
		}
	}
	
	public void recordTick(){
		if(isRecording){
			if(this.getCurrentRecording() != null){
				this.getCurrentRecording().addState();
			}
		}
	}
	
	public void nextRecording(){
		if(CURRENT+1 < cachedRecordings.size()){
			CURRENT++;
		}else{
			CURRENT = 0;
		}
	}
	
	public void prevRecording(){
		if(CURRENT-1 >= 0){
			CURRENT--;
		}else{
			CURRENT = cachedRecordings.size()-1;
		}
	}
	
	public MoCapRecording getCurrentRecording(){
		if(cachedRecordings.size() == 0){
			return null;
		}
		if(cachedRecordings.size() > CURRENT){
			if(cachedRecordings.get(CURRENT) != null){
				return cachedRecordings.get(CURRENT);
			}
		}
		return null;
	}
	
	
	public void populateRecordings(){
		cachedRecordings.clear();
		File[] files = outFile.listFiles();
		for(File f : files){
			if(f.getName().endsWith(".json")){
				String fname = f.getName().split("\\.")[0];
				MoCapRecording rec = MoCapRecording.load(fname);
				if(rec != null){
					cachedRecordings.add(rec);
				}
			}
		}
	}
	
	public static MoCapPlayback instance(){
		if(instance == null){
			instance = new MoCapPlayback();
			instance.populateRecordings();
		}
		return instance;
	}
	
	static{
		if(!outFile.exists()){
			outFile.mkdir();
			MoCap.log("Created dir! mocap/");
		}
	}
}
