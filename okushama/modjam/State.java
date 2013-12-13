package okushama.modjam;

import net.minecraft.entity.EntityLivingBase;

public class State {

	public double posX, posY, posZ, prevPosX, prevPosY, prevPosZ;
	public float pitch, yaw, prevPitch, prevYaw, yawHead, prevYawHead;
	public double motionX, motionY, motionZ;
	
	public State setTarget(EntityLivingBase t){
		MoCapPlayback.target = t;
		return this;
	}
	
	public void readEntityData(){
		if(MoCapPlayback.target != null){
			posX = MoCapPlayback.target.posX;
			posY = MoCapPlayback.target.posY;
			posZ = MoCapPlayback.target.posZ;
			prevPosX = MoCapPlayback.target.prevPosX;
			prevPosY = MoCapPlayback.target.prevPosY;
			prevPosZ = MoCapPlayback.target.prevPosZ;
			pitch = MoCapPlayback.target.rotationPitch;
			yaw = MoCapPlayback.target.rotationYaw;
			prevPitch = MoCapPlayback.target.prevRotationPitch;
			prevYaw = MoCapPlayback.target.prevRotationYaw;
			yawHead = MoCapPlayback.target.rotationYawHead;
			prevYawHead = MoCapPlayback.target.prevRotationYawHead;
			motionX = MoCapPlayback.target.motionX;
			motionY = MoCapPlayback.target.motionY;
			motionZ = MoCapPlayback.target.motionZ;

		}
	}
	
	public void setEntityData(){
		if(MoCapPlayback.target != null){
			MoCapPlayback.target.setPosition(posX, posY, posZ);
			MoCapPlayback.target.prevPosX = prevPosX;
			MoCapPlayback.target.prevPosY = prevPosY;
			MoCapPlayback.target.prevPosZ = prevPosZ;
			MoCapPlayback.target.rotationPitch = pitch;
			MoCapPlayback.target.rotationYaw = yaw;
			MoCapPlayback.target.prevRotationPitch = prevPitch;
			MoCapPlayback.target.prevRotationYaw = prevYaw;
			MoCapPlayback.target.rotationYawHead = yawHead;
			MoCapPlayback.target.prevRotationYawHead = prevYawHead;
			MoCapPlayback.target.motionX = motionX;
			MoCapPlayback.target.motionY = motionY;
			MoCapPlayback.target.motionZ = motionZ;
		}
	}
}
