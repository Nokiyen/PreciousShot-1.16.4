package noki.preciousshot.event;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import noki.preciousshot.ModInfo;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.ScreenShotHelper;


/**********
 * @class EventCommon
 *
 * @description 
 * @description_en
 */
public class EventCommon {
	
	private boolean notified = false;

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {

		if(this.notified) {
			return;
		}

		if(!event.getWorld().isRemote) {
			return;
		}

		if(event.getEntity() == null) {
			return;
		}

		if(!(event.getEntity() instanceof PlayerEntity)) {
			return;
		}

		UUID targetID = ((PlayerEntity)event.getEntity()).getGameProfile().getId();
		UUID playerID = Minecraft.getInstance().player.getGameProfile().getId();
		if(!targetID.equals(playerID)) {
			return;
		}

		VersionChecker.CheckResult result = VersionChecker.getResult(ModList.get().getModFileById(ModInfo.ID).getMods().get(0));
		if(result == null) {
			return;
		}

		if(result.status == VersionChecker.Status.UP_TO_DATE) {
			return;
		}

		if(result.target == null) {
			return;
		}

		event.getEntity().sendMessage(
				new TranslationTextComponent(ModInfo.ID.toLowerCase()+".version.notify", result.target.toString()), Util.DUMMY_UUID);
		this.notified = true;

	}

}
