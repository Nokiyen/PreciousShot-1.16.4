package noki.preciousshot.helper;

import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import noki.preciousshot.PreciousShotConf;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.asm.ASMChatClickEvent;
import noki.preciousshot.PreciousShotConf.PSOption;
import noki.preciousshot.helper.LangHelper.LangKey;


/**********
 * @class ScreenShotHelper
 *
 * @description スクリーンショットを保存するためのクラスです。
 * @descriptoin_en
 */
public class ScreenShotHelper {

	//******************************//
	// define member variables.
	//******************************//
//	private static IntBuffer pixelBuffer;
	private static int[] pixelValues;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");


	//******************************//
	// define member methods.
	//******************************//

	//----------
	//Static Method.
	//----------
	public static String saveScreenshot(int top, int right, int bottom, int left) {

		//java.lang.IllegalStateException: Rendersystem called from wrong thread
		NativeImage resizedImage = getScreenShotImage(top, right, bottom, left);
		ScreenShotThread newThread = new ScreenShotThread(resizedImage);
		newThread.start();

//		return saveScreenshotRaw(top, right, bottom, left);
		return "";

	}

	public static NativeImage getScreenShotImage(int top, int right, int bottom, int left) {

		Minecraft mc = Minecraft.getInstance();
		Framebuffer buffer = mc.getFramebuffer();

		int width = buffer.framebufferTextureWidth;
		int height = buffer.framebufferTextureHeight;

		int outputWidth = width - left - right;
		int outputHeight = height - top - bottom;

		if(outputWidth < 1 || outputHeight < 1) {
			PreciousShotCore.log("invalid width & height: width/%s, height/%s", width, height);
			return null;
		}

		NativeImage nativeImage = new NativeImage(width, height, false);
		RenderSystem.bindTexture(buffer.func_242996_f());
		nativeImage.downloadFromTexture(0, true);

		NativeImage resizedImage = new NativeImage(outputWidth, outputHeight, false);
		nativeImage.resizeSubRectTo(left, top, outputWidth, outputHeight, resizedImage);

		nativeImage.flip();
		resizedImage.flip();
		nativeImage.close();

		pixelValues = resizedImage.makePixelArray();

		return resizedImage;

	}

	public static File getTimestampedPNGFileForDirectory(File gameDirectory) {

		String s = dateFormat.format(new Date()).toString();
		int i = 1;

		while(true) {
			File file = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

			if(!file.exists()) {
				return file;
			}

			++i;

			if(i > 100) {
				break;
			}
		}

		return null;

	}

	public static int[] getPixels() {

		return pixelValues;

	}

	private static class ScreenShotThread extends Thread {

		private NativeImage image;

		public ScreenShotThread(NativeImage image) {
			this.image = image;
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
					file = getTimestampedPNGFileForDirectory(directory);
					if (file != null) {
						PreciousShotCore.log("file name is {}.", file.getPath());
						this.image.write(file);

						if (PSOption.CHAT.isEnable()) {
							LangHelper.sendChatWithViewOpen(LangKey.SHOOTING_DONE, LangKey.SHOOTING_URL, file.getName());
						}
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
			}
		}
	}

}
