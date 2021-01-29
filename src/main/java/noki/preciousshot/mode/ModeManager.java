package noki.preciousshot.mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import noki.preciousshot.PreciousShotConf;
import noki.preciousshot.PreciousShotCore;
import org.lwjgl.glfw.GLFW;

import static noki.preciousshot.PreciousShotConf.PSOption.PANORAMA;


/**********
 * @class ModeManager
 *
 * @description 画面切り替え等をコントロールするクラスです。
 * @description_en
 */
public class ModeManager {
	
	//******************************//
	// define member variables.
	//******************************//
	public static ModeManager instance;
	public static ModeEventShooting modeShooting;
	public static ModeEventShooting modePanorama;
	
	public static KeyBinding keyF2 = new KeyBinding("preciousshot.key.f2", GLFW.GLFW_KEY_F2, "System");
	public static KeyBinding keyEsc = new KeyBinding("preciousshot.key.esc", GLFW.GLFW_KEY_ESCAPE, "System");
	public static KeyBinding key = new KeyBinding("preciousshot.key.on", PreciousShotConf.keyNum.get(), "System");
	
	
	
	//******************************//
	// define member methods.
	//******************************//
	//F2キーを押されたとき、撮影モード・パノラマモードでなければ通常のスクリーンショットを行う。
/*	@SubscribeEvent
	public void onScreenshot(ScreenshotEvent event) {
		
		PreciousShotCore.log("on screen shot.");
		
		if(!modeShooting.isOpen() && !modePanorama.isOpen()) {
			// default screen shot.
			Minecraft mc = Minecraft.getMinecraft();
			ITextComponent res = net.minecraft.util.ScreenShotHelper.saveScreenshot(
					mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
			mc.ingameGUI.getChatGUI().printChatMessage(res);
			//nothing to do.
		}
		else {
			event.setCanceled(true);
		}
		
	}*/
	
	//Lキーを押されたとき、通常画面からなら撮影・パノラマモードへ、撮影・パノラマモードなら設定モードへ移行する。
	//1.12から、LキーがAdvancementsになったのでJキーへ。
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		
		PreciousShotCore.log("on key input/ {} / {} / J={}.", event.getKey(), PreciousShotConf.keyNum.get(), GLFW.GLFW_KEY_J);
		
//		if(key.isPressed() == false) {
/*		if(!key.isKeyDown()) {
			PreciousShotCore.log("key is not pressed:"+event.hashCode());
			PreciousShotCore.log("false");
			return;
		}*/
//		if(event.getKey() != GLFW.GLFW_KEY_J) {

		boolean keyFlag = key.isKeyDown();
		boolean escFlag = keyEsc.isKeyDown();
		if(!keyFlag && !escFlag) {
			PreciousShotCore.log("key is not pressed:"+event.hashCode());
			PreciousShotCore.log("false");
			return;
		}
		PreciousShotCore.log("true");
		
		ModeEventShooting currentMode = modeShooting;
		if(PANORAMA.isEnable()) {
			currentMode = modePanorama;
		}

		//if j key is pressed.
		if(keyFlag) {
			if(currentMode.isOpen()) {
				PreciousShotCore.log("close mode.");
				currentMode.closeMode();
				Minecraft.getInstance().displayGuiScreen(new ModeGuiSetting());
			}
			else {
				PreciousShotCore.log("open mode.");
				currentMode.openMode();
			}
			return;
		}

		//if esc key is pressed when the current mode is open.
		if(currentMode.isOpen()) {
			currentMode.closeMode();
//			event.setCanceled(true);
		}

	}
	
	//チャットがクリックされたとき、閲覧モードを開く設定のチャット文なら、閲覧モードの個別表示に直接移行する。
/*	@SubscribeEvent
	public void onChatClick(ChatClickEvent event) {
		
		String fileName = LangHelper.getViewOpenString(event.clickEvent);
		if(fileName == null) {
			return;
		}
		
		int index = ResourceManager.getResourceIndex(fileName);
		if(index == -1) {
			index = 0;
		}
		ModeGuiViewing gui = new ModeGuiViewing();
		Minecraft.getMinecraft().displayGuiScreen(gui);
		gui.setEachView(index);
		
		event.setCanceled(true);
		
	}*/
	
	
	//----------
	//Static Method.
	//----------
	public static void init() {
		
		ClientRegistry.registerKeyBinding(ModeManager.keyF2);
		ClientRegistry.registerKeyBinding(ModeManager.keyEsc);
		ClientRegistry.registerKeyBinding(ModeManager.key);
		
		instance = new ModeManager();
		modeShooting = new ModeEventShooting();
		modePanorama = new ModeEventPanorama();
		
		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(modeShooting);
		MinecraftForge.EVENT_BUS.register(modePanorama);
		
	}
	
}
