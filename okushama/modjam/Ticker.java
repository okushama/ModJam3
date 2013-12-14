package okushama.modjam;

import java.io.File;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Ticker implements ITickHandler {

	public static long tick = 0L;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.PLAYER))) {
			onClientTick(tickData);
		}
		if (type.equals(EnumSet.of(TickType.RENDER))) {
			onPlaybackTick(tickData);
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.RENDER))) {
			onRenderTick(tickData);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER, TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "Ticker";
	}

	public int tickSwitch = 0;

	public void onClientTick(Object... tickData) {
		if (Minecraft.getMinecraft().theWorld != null) {
			if (MoCapHandler.target == null || MoCapHandler.target.isDead) {
				MoCapHandler.target = Minecraft.getMinecraft().thePlayer;
			}
			if(FMLClientHandler.instance() != null && FMLClientHandler.instance().getServer() != null){
				String worldName = FMLClientHandler.instance().getServer().worldServerForDimension(Minecraft.getMinecraft().thePlayer.dimension).getSaveHandler().getWorldDirectoryName();
				String currentDir = MoCapHandler.currentDirectory.getAbsolutePath();
				if(!currentDir.endsWith(worldName+File.separator+"mocap")){
					MoCapHandler.currentDirectory = new File("saves/"+worldName+"/mocap");
					if(!MoCapHandler.currentDirectory.exists()){
						MoCapHandler.currentDirectory.mkdir();
					}
					MoCapHandler.instance().populateRecordings();
				}
			}
		}
	}
	
	public void onPlaybackTick(Object... tickData){
		tick++;
		MoCapHandler.instance().recordTick();
		//MoCapHandler.instance().buffer.addState();
		if (MoCapHandler.instance().getCurrentRecording() != null) {
			if(tickData[0] instanceof Float){
				Float f = (Float)tickData[0];
				MoCapHandler.instance().getCurrentRecording().playback((float)f);
			}
		}
	}
	

	public void onRenderTick(Object... tickData) {	
		if (Minecraft.getMinecraft().theWorld != null) {
			if(testEnt == null || !Minecraft.getMinecraft().theWorld.loadedEntityList.contains(testEnt) || testEnt.isDead){
				testEnt = new EntityTest(Minecraft.getMinecraft().theWorld);
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				testEnt.setPosition(p.posX, p.posY, p.posZ);
				Minecraft.getMinecraft().theWorld.spawnEntityInWorld(testEnt);
			}
			if(tickData[0] instanceof Float){
				Float f = (Float)tickData[0];
				Overlay.onRenderTick(f);
			}
		}
	//	EntityRenderer r = Minecraft.getMinecraft().entityRenderer;
		//r.renderWorld(1f, 0L);
		Minecraft.getMinecraft().renderViewEntity = testEnt;
		if(Minecraft.getMinecraft().objectMouseOver != null){
			if(Minecraft.getMinecraft().objectMouseOver.entityHit != null){
				if(Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase){
					MoCapHandler.target = (EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				}
			}
		}
		MoCapHandler.target = Minecraft.getMinecraft().thePlayer;
		Minecraft.getMinecraft().renderViewEntity = Minecraft.getMinecraft().thePlayer;

	}
	
	public EntityTest testEnt = null;
	
	public static class EntityTest extends EntityLivingBase{

		public EntityTest(World w){
			super(w);
		}
		
		@Override
		protected void entityInit() {
			super.entityInit();
		}
		
		@Override
		public void onUpdate(){
			super.onUpdate();
		}

		@Override
		public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		}

		@Override
		public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		}

		@Override
		public ItemStack getHeldItem() {
			return null;
		}

		@Override
		public ItemStack getCurrentItemOrArmor(int i) {
			return null;
		}

		@Override
		public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
			
		}

		@Override
		public ItemStack[] getLastActiveItems() {
			return null;
		}
		
	}

}
