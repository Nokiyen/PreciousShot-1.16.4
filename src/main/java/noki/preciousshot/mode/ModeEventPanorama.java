package noki.preciousshot.mode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import noki.preciousshot.PreciousShotConf;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.RenderHelper;
import noki.preciousshot.helper.ScreenShotHelper;
import noki.preciousshot.helper.LangHelper.LangKey;
import noki.preciousshot.resource.ResourceManager;
import noki.preciousshot.resource.ResourceManager.ShotResource;
import static noki.preciousshot.PreciousShotConf.PSOption.*;


/**********
 * @class ModeEventPanorama
 *
 * @description パノラマモードをコントロールするクラスです。各種イベントにより疑似的なGUIになっています。
 * @descriptoin_en 
 */
public class ModeEventPanorama extends ModeEventShooting {
	
	//******************************//
	// define member variables.
	//******************************//
	public int panoramaTimes;
	public String currentFileName;
	public ShotResource shotResource;
	public ArrayList<int[]> pixels = new ArrayList<int[]>();
	public ArrayList<byte[]> images = new ArrayList<>();
	public boolean threadStarting = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		
		if(this.enable == false) {
			return;
		}
		super.onRenderTick(event);
		
		switch(event.phase) {
			case START:
				break;
			case END:
				if(this.panoramaTimes == PANORAMA.value() && !this.threadStarting) {
					Minecraft mc = Minecraft.getInstance();
					
					Framebuffer buffer = mc.getFramebuffer();
					
//					int width = mc.getMainWindow().getScaledWidth();
//					int height = mc.getMainWindow().getScaledHeight();
					int width = mc.getMainWindow().getWidth();
					int height = mc.getMainWindow().getHeight();
/*					if(OpenGlHelper.isFramebufferEnabled()) {
						width = buffer.framebufferTextureWidth;
						height = buffer.framebufferTextureHeight;
					}*/
					int eachWidth = width-LEFT.value()-RIGHT.value();
					int outputWidth = eachWidth*PANORAMA.value();
					int outputHeight = height-TOP.value()-BOTTOM.value();

					PreciousShotCore.log("panorama: {} / {}", outputWidth, outputHeight);
					if(outputWidth < 1 || outputHeight < 1) {
						PreciousShotCore.log("invalid width & height: width/%s, height/%s", width, height); 
						return;
					}
					
/*					BufferedImage bufferedimage = null;
					if(OpenGlHelper.isFramebufferEnabled()) {
						bufferedimage = new BufferedImage(outputWidth, outputHeight, 1);
						int times = 0;
						for(int[] each: this.pixels) {
							for(int i = TOP.value(); i < height - BOTTOM.value(); i++) {
								for(int j = LEFT.value(); j < width - RIGHT.value(); j++) {
									bufferedimage.setRGB(j-LEFT.value()+times*eachWidth, i-TOP.value(), each[i * width + j]);
								}
							}
							times++;
						}
					}*/

					BufferedImage resultImage = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB);
					try{
						ArrayList<BufferedImage> imageList = new ArrayList<>();
						for(byte[] each: this.images) {
							imageList.add(ImageIO.read(new ByteArrayInputStream(each)));

							Graphics graphic = resultImage.getGraphics();
							for(int i=0; i<imageList.size(); i++) {
								graphic.drawImage(imageList.get(i), i*eachWidth, 0, null);
							}
						}
					}
					catch(Exception exception) {
						PreciousShotCore.log("exception: %s", exception.toString());
					}


					this.panoramaTimes = 0;
					this.currentFileName = null;
					this.shotResource = null;
					this.pixels.clear();
					this.images.clear();

					PanoramaResultThread thread = new PanoramaResultThread(resultImage);
					thread.start();
					
				}
				break;
		}
		
	}
	
	@Override
	public void render() {
		
		super.render();
		
		Minecraft mc = Minecraft.getInstance();
		mc.fontRenderer.drawString(new MatrixStack(),
				LangKey.PANORAMA_MODE.translated(this.panoramaTimes+1), 5, 5, 0xffffff);

		if(this.panoramaTimes == 0) {
			return;
		}

		this.shotResource = ResourceManager.getResource(this.currentFileName);
		if(this.shotResource == null) {
			return;
		}
//		PreciousShotCore.log("panorama file name is %s.", this.currentFileName);
		
		double absTop = (TOP.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)mc.getMainWindow().getScaledHeight();
		double absBottom = (BOTTOM.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)mc.getMainWindow().getScaledHeight();
		double absleft = (LEFT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)mc.getMainWindow().getScaledWidth();
		
		mc.getTextureManager().bindTexture(this.shotResource.location);
//		Gui.drawScaledCustomSizeModalRect(0, (int)absTop, resource.width-LEFT.value(), 0, LEFT.value(), resource.height,
//				(int)absleft, (int)(resolution.getScaledHeight()-absTop-absBottom), resource.width, resource.height);
//		PreciousShotCore.log("render panorama.");
//		GuiUtils.drawContinuousTexturedBox(0, (int)absTop, this.shotResource.width-LEFT.value(), 0,
//				(int)absleft, (int)(mc.getMainWindow().getScaledHeight()-absTop-absBottom), this.shotResource.width, this.shotResource.height,0, 10);
/*		GuiUtils.drawContinuousTexturedBox(0, (int)absTop, this.shotResource.width-LEFT.value(), 0,
				(int)absleft, (int)(mc.getMainWindow().getScaledHeight()-absTop-absBottom),
				(int)absleft, (int)(mc.getMainWindow().getScaledHeight()-absTop-absBottom),0, 10);*/
		RenderHelper.drawScaledTexture(0, (int)absTop, (int)absleft, (int)(mc.getMainWindow().getScaledHeight()-absTop-absBottom),
				this.shotResource.width-LEFT.value(), 0, LEFT.value(), this.shotResource.height,
				this.shotResource.width, this.shotResource.height);

	}
	
	protected void dealScreenshot() {
		
		if(SHOT.isEnable() == true) {
			this.bookScreenShotOnNextTick();
		}
		else {
/*			Minecraft mc = Minecraft.getInstance();
			ITextComponent res = net.minecraft.util.ScreenShotHelper.saveScreenshot(
					mc.gameDir, mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), mc.getFramebuffer(), null);
			mc.ingameGUI.getChatGUI().printChatMessage(res);*/
		}
		
	}
	
	@Override
	public void saveScreenshot(int top, int right, int bottom, int left) {
		
/*		this.currentFileName = super.saveScreenshot(top, right, bottom, left);
		if(this.currentFileName == null) {
			if(CHAT.isEnable()) {
				LangHelper.sendChat(LangKey.SHOOTING_FAILED);
			}
			return null;
		}*/
//		super.saveScreenshot(top, right, bottom, left);

		PreciousShotCore.log("on save panorama screen shot.");

		NativeImage resizedImage = ScreenShotHelper.getScreenShotImage(top, right, bottom, left);

		this.threadStarting = true;
		PanoramaShotThread thread = new PanoramaShotThread(resizedImage, this);
		thread.start();

//		String fileName = ScreenShotHelper.saveScreenshot(top, right, bottom, left);

/*		if(CHAT.isEnable()) {
			LangHelper.sendChatWithViewOpen(LangKey.SHOOTING_DONE, LangKey.SHOOTING_URL, this.currentFileName);
		}
		return this.currentFileName;*/
		
	}
	
	@Override
	public void setFadeStrings() {

		this.fadeStrings.clear();

//		this.fadeStrings.add(new RenderHelper.FadeStringRender(LangKey.SHOOTING_MODE.translated(), 0xffffff, 5, 5,
//				Minecraft.getInstance().fontRenderer, 0, 0, 100, 20));
		if(!SHOT.isEnable()) {
			this.fadeStrings.add(new RenderHelper.FadeStringRender(LangKey.SHOOTING_STOPPED.translated(), 0xff6666, 5, 15,
					Minecraft.getInstance().fontRenderer, 0, 0, 100, 20));
		}
		if(this.fovRender != null) {
			this.fadeStrings.add(this.fovRender);
		}

	}
	
	@Override
	public void closeMode() {
		
		super.closeMode();
		this.panoramaTimes = 0;
		this.currentFileName = null;
		this.shotResource = null;
		this.pixels.clear();
		
	}

	private static class PanoramaShotThread extends Thread {

		private NativeImage image;
		private ModeEventPanorama modePanorama;

		public PanoramaShotThread(NativeImage image, ModeEventPanorama modePanorama) {
			this.image = image;
			this.modePanorama = modePanorama;
		}

		public void run() {

			File file = null;
			try {
				File directory = new File(Minecraft.getInstance().gameDir, "screenshots");
				boolean result = directory.exists();
				if(!result) {
					result = directory.mkdir();
				}
				if(result) {
					file = ScreenShotHelper.getTimestampedPNGFileForDirectory(directory);
					if (file != null) {
						PreciousShotCore.log("file name is {}.", file.getPath());
						this.image.write(file);

						if (PreciousShotConf.PSOption.CHAT.isEnable()) {
							LangHelper.sendChatWithViewOpen(LangKey.SHOOTING_DONE, LangKey.SHOOTING_URL, file.getName());
						}

						this.modePanorama.panoramaTimes++;
						int[] target = this.image.makePixelArray();
						this.modePanorama.pixels.add(Arrays.copyOf(target, target.length));
						this.modePanorama.images.add(this.image.getBytes());
						this.modePanorama.currentFileName = file.getName();
						ResourceManager.reloadResources();
					}
					else {
						LangHelper.sendChat(LangKey.SHOOTING_FAILED);
					}
				}
			}
			catch(Exception exception) {
				PreciousShotCore.log("exception: {}", exception.toString());
			}
			finally {
				this.image.close();
				this.modePanorama.threadStarting = false;
			}
		}
	}

	private static class PanoramaResultThread extends Thread {

		BufferedImage targetImage;

		public PanoramaResultThread(BufferedImage image) {
			this.targetImage = image;
		}

		public void run() {

			File file = null;
			try {
				File directory = new File(Minecraft.getInstance().gameDir, "screenshots");
				boolean result = directory.exists();
				if(!result) {
					result = directory.mkdir();
				}
				if(result) {
					file = ScreenShotHelper.getTimestampedPNGFileForDirectory(directory);
					boolean res = false;
					res = ImageIO.write(this.targetImage, "png", file);

					if(res) {
						LangHelper.sendChatWithViewOpen(LangKey.PANORAMA_DONE, LangKey.SHOOTING_URL, file.getName());
					}
					else {
						LangHelper.sendChat(LangKey.PANORAMA_FAILED);
					}
				}
			}
			catch(Exception exception) {
				PreciousShotCore.log("exception: {}", exception.toString());
			}
			finally {
			}
		}
	}

}
