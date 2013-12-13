package okushama.modjam;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class PlayerState extends State{
	
	public boolean isSwinging, isUsingItem;
	public int currentSlot;
	@Override
	public void readEntityData(){
		super.readEntityData();
		if(MoCapPlayback.target instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)MoCapPlayback.target;
			isSwinging = player.isSwingInProgress;
			isUsingItem = player.isUsingItem();
			currentSlot = player.inventory.currentItem;
		}
	}
	
	@Override
	public void setEntityData(float partialTick){
		super.setEntityData(partialTick);
		if(MoCapPlayback.target instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)MoCapPlayback.target;
			if(isSwinging && !player.isSwingInProgress){
				player.swingItem();
			}
			player.inventory.currentItem = currentSlot;
			if(isUsingItem){
				ItemStack itemInUse = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, "itemInUse", "field_71074_e");
				if(itemInUse != null){
					int itemInUseCount = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, "itemInUseCount", "field_71072_f");
					itemInUse.getItem().onUsingItemTick(itemInUse, player, itemInUseCount);
				}
				
			}
		}
	}

}
