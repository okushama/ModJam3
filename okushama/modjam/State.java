package okushama.modjam;

import java.util.Random;

import net.minecraft.block.material.Material;
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
		MoCapHandler.target = t;
		return this;
	}
	
	public void readEntityData(){
		if(MoCapHandler.target != null){
			posX = MoCapHandler.target.posX;
			posY = MoCapHandler.target.posY;
			posZ = MoCapHandler.target.posZ;
			prevPosX = MoCapHandler.target.prevPosX;
			prevPosY = MoCapHandler.target.prevPosY;
			prevPosZ = MoCapHandler.target.prevPosZ;
			pitch = MoCapHandler.target.rotationPitch;
			yaw = MoCapHandler.target.rotationYaw;
			prevPitch = MoCapHandler.target.prevRotationPitch;
			prevYaw = MoCapHandler.target.prevRotationYaw;
			yawHead = MoCapHandler.target.rotationYawHead;
			prevYawHead = MoCapHandler.target.prevRotationYawHead;
			motionX = MoCapHandler.target.motionX;
			motionY = MoCapHandler.target.motionY;
			motionZ = MoCapHandler.target.motionZ;
			isSneaking = MoCapHandler.target.isSneaking();
			isSprinting = MoCapHandler.target.isSprinting();
		}
	}
	
	public static float movementOffset = 20;
	
	
	public void setEntityData(float partialTick){
		if(MoCapHandler.target != null){
			MoCapHandler.target.prevRotationPitch = prevPitch;
			MoCapHandler.target.prevRotationYaw = prevYaw;
			MoCapHandler.target.prevRotationYawHead = prevYawHead;
			MoCapHandler.target.rotationPitch = pitch;
			MoCapHandler.target.rotationYaw = yaw;
			MoCapHandler.target.rotationYawHead = yawHead;
		
			movementOffset = 1;
			if(MoCapHandler.isModulated){
				movementOffset = 20+new Random().nextInt(2000);
			}
			if(Ticker.tick > movementOffset){
				//Ticker.tick = 0;
			}
			if(Ticker.tick % movementOffset == 0)
			{
				MoCapHandler.target.motionX = motionX/0.76;
				MoCapHandler.target.motionY = motionY/0.76;
				MoCapHandler.target.motionZ = motionZ/0.76;
				MoCapHandler.target.setPosition(posX, posY, posZ);
				MoCapHandler.target.prevPosX = prevPosX;
				MoCapHandler.target.prevPosY = prevPosY;
				MoCapHandler.target.prevPosZ = prevPosZ;
			//	MoCapPlayback.target.setLocationAndAngles(posX, posY-MoCapPlayback.target.yOffset, posZ, yaw, pitch);
				MoCapHandler.target.setPositionAndUpdate(posX, posY-MoCapHandler.target.yOffset, posZ);

				MoCapHandler.target.setSneaking(isSneaking);
				MoCapHandler.target.setSprinting(isSprinting);
				if(MoCapHandler.target == Minecraft.getMinecraft().thePlayer){
					Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = isSneaking;
				}
			}
		}
	}
}
