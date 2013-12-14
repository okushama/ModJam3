package okushama.modjam;

import net.minecraft.entity.player.EntityPlayer;

public class MoCapBuffer extends MoCapRecording{

	public long bufferLimit = 200L;
	
	@Override
	public void addState(){
		State s = new State();
		if(MoCapHandler.target instanceof EntityPlayer){
			s = new PlayerState();
		}
		s.readEntityData();
		recording.add(s);
		for(long l = recording.size(); l > bufferLimit; l--){
			recording.remove(l-1);
		}
		totalLength = recording.size();
	}
	
}
