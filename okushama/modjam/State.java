package okushama.modjam;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet18Animation;

public class State {

	public double posX, posY, posZ, prevPosX, prevPosY, prevPosZ;
	public float pitch, yaw, prevPitch, prevYaw, yawHead, prevYawHead;
	public double motionX, motionY, motionZ;
	public boolean isSneaking, isSprinting;
	
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
			isSneaking = MoCapPlayback.target.isSneaking();
			isSprinting = MoCapPlayback.target.isSprinting();
		}
	}
	
	
	public void setEntityData(float partialTick){
		if(MoCapPlayback.target != null){
			MoCapPlayback.target.rotationPitch = pitch;
			MoCapPlayback.target.rotationYaw = yaw;
			MoCapPlayback.target.rotationYawHead = yawHead;
			MoCapPlayback.target.prevRotationPitch = prevPitch;
			MoCapPlayback.target.prevRotationYaw = prevYaw;
			MoCapPlayback.target.prevRotationYawHead = prevYawHead;
			MoCapPlayback.target.motionX = motionX/0.76;
			MoCapPlayback.target.motionY = motionY/0.76;
			MoCapPlayback.target.motionZ = motionZ/0.76;
			//MoCapPlayback.target.setPosition(posX, posY, posZ);
		//	MoCapPlayback.target.prevPosX = prevPosX;
		//	MoCapPlayback.target.prevPosY = prevPosY;
		//	MoCapPlayback.target.prevPosZ = prevPosZ;
		//	MoCapPlayback.target.setLocationAndAngles(posX, posY-MoCapPlayback.target.yOffset, posZ, yaw, pitch);
			MoCapPlayback.target.setSneaking(isSneaking);
			MoCapPlayback.target.setSprinting(isSprinting);
			if(MoCapPlayback.target == Minecraft.getMinecraft().thePlayer){
				Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = isSneaking;
			}
		}
	}
}
