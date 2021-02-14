package noki.preciousshot.mode;

import java.util.ArrayList;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import noki.preciousshot.PreciousShotConf.PSOption;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper.LangKey;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.RenderHelper;
import noki.preciousshot.helper.ScreenShotHelper;
import noki.preciousshot.helper.RenderHelper.FadeStringRender;
import org.lwjgl.glfw.GLFW;

import static noki.preciousshot.PreciousShotConf.PSOption.*;


/**********
 * @class ModeEventShooting
 *
 * @description 撮影モードをコントロールするクラスです。各種イベントにより疑似的なGUIになっています。
 * @descriptoin_en 
 */
public class ModeEventShooting {
	
	//******************************//
	// define member variables.
	//******************************//
	protected boolean bookScreenShot = false;
	protected boolean confirmScreenShot = false;
	protected boolean hideGuiKeep = false;
	protected int counter = 0;
	protected ArrayList<FadeStringRender> fadeStrings = new ArrayList<>();
	protected FadeStringRender fovRender;
	
	protected boolean enable = false;
	
	protected boolean isRightClicked = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	//START: スクショが予約されていた場合、スクショを確約し、描画を消す。
	//END: 各種描画を行う。スクショが確約されていた場合撮影。
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		
		if(!this.enable) {
			return;
		}
		
		switch(event.phase) {
			case START:
				if(this.bookScreenShot) {
					this.confirmScreenShot = true;
					if(HIDE.isEnable()) {
						Minecraft.getInstance().gameSettings.hideGUI = true;
					}
				}
				break;
			case END:
				if(Minecraft.getInstance().currentScreen == null && !this.confirmScreenShot) {
					this.render();
				}
				
				if(this.confirmScreenShot) {
					this.saveScreenshot(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value());
					this.bookScreenShot = false;
					this.confirmScreenShot = false;
					Minecraft.getInstance().gameSettings.hideGUI = hideGuiKeep;
				}
				break;
		}
		
	}
	
	protected void render() {
		
		Minecraft mc = Minecraft.getInstance();

//		mc.entityRenderer.setupOverlayRendering();
//		ScaledResolution resolution = new ScaledResolution(mc);
		int scaledWidth = mc.getMainWindow().getScaledWidth();
		int scaledHeight = mc.getMainWindow().getScaledHeight();
		if(MARGIN.isEnable()) {
			RenderHelper.renderMargin(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), scaledWidth, scaledHeight);
		}
		if(GRID.value() != 0) {
			if(MARGIN.isEnable()) {
				RenderHelper.renderGrid(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), scaledWidth, scaledHeight, GRID.value());
			}
			else {
				RenderHelper.renderGrid(0, 0, 0, 0, scaledWidth, scaledHeight, GRID.value());
			}
		}
		
		for(FadeStringRender each: this.fadeStrings) {
			each.tick();
/*			if(each.isFinished()) {
				this.fadeStrings.remove(each);
			}*/
		}
		
	}
	
	//START: 右クリック状態を初期化。
	//END: 連写のタイミングに達したら撮影。
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		
		if(!this.enable) {
			return;
		}
		
		switch (event.phase) {
			case START:
//				this.isRightClicked = false;
				break;
			case END:
				if(CONT.isEnable() && (ModeManager.keyF2.isKeyDown() || this.isRightClicked)) {
					counter = (counter+1) % (CONT.value()-1);
					if(counter == 0) {
						dealScreenshot();
					}
				}
				else {
					counter = 0;
				}
				break;
		}
		
	}
	
	//通常のF2キーでのスクリーンショットを置換。
	@SubscribeEvent
	public void onScreenShot(ScreenshotEvent event) {

		PreciousShotCore.log("on screen shot.");
		if(!this.enable || !SHOT.isEnable()) {
			PreciousShotCore.log("disabled / {} / {}.", this.enable, SHOT.isEnable());
			return;
		}

		event.setCanceled(true);
		event.setResultMessage(new StringTextComponent(""));
		this.dealScreenshot();
		
	}

	//マウス右クリック＋CTRLでスクリーンショット
	@SubscribeEvent
	public void onMouseInput(InputEvent.RawMouseEvent event) {

		if(!this.enable || !SHOT.isEnable()) {
			return;
		}
		
		if(!PSOption.CLICK.isEnable()) {
			return;
		}

		//右クリック+CTRLで撮影。
		if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && event.getAction() == GLFW.GLFW_PRESS && event.getMods() == GLFW.GLFW_MOD_CONTROL) {
			this.isRightClicked = true;
			this.dealScreenshot();
			event.setCanceled(true);
		}
		if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && event.getAction() == GLFW.GLFW_RELEASE) {
			this.isRightClicked = false;
		}

	}

	//ホイール+CTRLでズーム(fov)
	@SubscribeEvent
	public void onMouseScroll(InputEvent.MouseScrollEvent event) {

		if(!this.enable) {
			return;
		}

		PreciousShotCore.log("on mouse scroll.");
		double wheel = event.getScrollDelta();
		if(wheel != 0 && (ModeManager.keyCtrlLeft.isKeyDown() || ModeManager.keyCtrlRight.isKeyDown())) {
			PreciousShotCore.log("wheel not zero / {}.", wheel);
			if(wheel > 0) {
				FOV.add(2);
			}
			else if(wheel < 0) {
				FOV.dif(2);
			}
			PreciousShotCore.log("fov / {}.", FOV.getDouble());
			RenderHelper.applySettingEffect();
			event.setCanceled(true);

			if(this.fovRender == null) {
				Minecraft mc = Minecraft.getInstance();
				this.fovRender = new FadeStringRender("FOV: "+String.valueOf(FOV.value()), 0xffffff,
						mc.getMainWindow().getScaledWidth()-50, 5, Minecraft.getInstance().fontRenderer, 0, 0, 100, 20);
				this.fadeStrings.add(this.fovRender);
			}
			this.fovRender.text = "FOV: "+String.valueOf(FOV.value());
			this.fovRender.resetPhase();
		}
	}
	
	protected void dealScreenshot() {

		PreciousShotCore.log("on deal screen shot.");
		if(Minecraft.getInstance().gameSettings.hideGUI && GRID.value() == 0) {
			this.saveScreenshot(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value());
		}
		else {
			this.bookScreenShotOnNextTick();
		}

	}
	
	public void bookScreenShotOnNextTick() {

		PreciousShotCore.log("on book screen shot.");

		if(!this.enable) {
			return;
		}
		
		this.bookScreenShot = true;
		this.hideGuiKeep = Minecraft.getInstance().gameSettings.hideGUI;
		
	}
	
	public void saveScreenshot(int top, int right, int bottom, int left) {

		PreciousShotCore.log("on save screen shot.");

		String fileName = ScreenShotHelper.saveScreenshot(top, right, bottom, left);
/*		if(PSOption.CHAT.isEnable()) {
			if(fileName != null) {
				LangHelper.sendChatWithViewOpen(LangKey.SHOOTING_DONE, LangKey.SHOOTING_URL, fileName);
			}
			else {
				LangHelper.sendChat(LangKey.SHOOTING_FAILED);
			}
		}*/
//		return fileName;
	}
	
	public void openMode() {
		
		this.enable = true;
		RenderHelper.keepOriginalEffect();
		RenderHelper.applySettingEffect();
		this.setFadeStrings();
		
	}
	
	public void setFadeStrings() {
		
		this.fadeStrings.clear();
		
		this.fadeStrings.add(new FadeStringRender(LangKey.SHOOTING_MODE.translated(), 0xffffff, 5, 5,
				Minecraft.getInstance().fontRenderer, 0, 0, 100, 20));
		if(!SHOT.isEnable()) {
			this.fadeStrings.add(new FadeStringRender(LangKey.SHOOTING_STOPPED.translated(), 0xff6666, 5, 15,
					Minecraft.getInstance().fontRenderer, 0, 0, 100, 20));
		}
		if(this.fovRender != null) {
			this.fadeStrings.add(this.fovRender);
		}
		
	}
	
	public void closeMode() {
		
		this.enable = false;
		RenderHelper.recoverOriginalEffect();
		
	}
	
	public boolean isOpen() {
		
		return this.enable;
		
	}
	
}
