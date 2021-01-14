package noki.preciousshot.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import noki.preciousshot.PreciousShotCore;

import javax.imageio.ImageIO;


/**********
 * @class ResourceManager
 * @inner_class ShotResource, DateDescComparator
 *
 * @description screenshotsフォルダ内の画像についてコントロールするクラスです。
 * @descriptoin_en 
 */
public class ResourceManager {
	
	//******************************//
	// define member variables.
	//******************************//
	public static File screenshotsDirectory;
	public static ScreenShotsResourcePack screenshotsResourcePack;
	public static ArrayList<ShotResource> resources;
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void init() {
		
		screenshotsDirectory = new File(Minecraft.getInstance().gameDir, "screenshots");
		screenshotsResourcePack = new ScreenShotsResourcePack(screenshotsDirectory);
		resources = new ArrayList();
		reloadResources();
		
	}
	
	public static void reloadResources() {

		PreciousShotCore.log("enter reloadResources.");
		((SimpleReloadableResourceManager)Minecraft.getInstance().getResourceManager()).addResourcePack(screenshotsResourcePack);
		
		Thread thread = new Thread() {
			@Override
			synchronized public void run() {
//				PreciousShotCore.log("enter run.");
				resources.clear();
				File[] files = screenshotsDirectory.listFiles();
//				PreciousShotCore.log("{}", files.length);
				Arrays.sort(files, new DateDescComparator());
				for(File each: files) {
					if(!isImage(each)) {
//						PreciousShotCore.log("not a file.");
						continue;
					}
					ResourceLocation location = new ResourceLocation("ps_screenshots", each.getName());
					try {
//						PreciousShotCore.log("{}", location.getPath());
						InputStream stream = screenshotsResourcePack.getInputStream(location.getPath());
						BufferedImage image = ImageIO.read(stream);
						resources.add(new ShotResource(each, location, image.getWidth(), image.getHeight()));
					}
					catch (Exception e) {
						PreciousShotCore.log("{}", e.getMessage());
					}
				}
			}
		};
		
		thread.start();
		
	}
	
	public static int getSize() {
		
		return resources.size();
		
	}
	
	public static ShotResource getResource(int index) {
		
		if(0<=index && index<=resources.size()-1) {
			return resources.get(index);
		}
		return null;
		
	}
	
	public static ShotResource getResource(String fileName) {
		
		for(ShotResource each: resources) {
//			PreciousShotCore.log("{} / {}", each.file.getName(), fileName);
			if(each.file.getName().equals(fileName)) {
				return each;
			}
		}
		return null;
		
	}
	
	public static int getResourceIndex(String fileName) {
		
		return resources.indexOf(getResource(fileName));
		
	}
	
	public static boolean exists(int index) {
		
		if(0<=index && index<=resources.size()-1) {
			return resources.get(index) == null ? false : true;
		}
		return false;
		
	}
	
	public static boolean isImage(File file) {
		
		if(!file.isFile()) {
			return false;
		}
		
		int position = file.getName().lastIndexOf(".");
		if(position == -1) {
			return false;
		}
		
		String ext = file.getName().substring(position + 1).toLowerCase();
		if(!ext.equals("png") && !ext.equals("jpg") && !ext.equals("bmp")) {
			return false;
		}
		
		return true;
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public static class ShotResource {
		//*****define member variables.*//
		public File file;
		public ResourceLocation location;
		public int width;
		public int height;
		
		
		//*****define member methods.***//
		public ShotResource(File file, ResourceLocation location, int width, int height) {
			this.file = file;
			this.location = location;
			this.width = width;
			this.height = height;
		}
	}
	
	public static class DateDescComparator implements Comparator<File> {
		//*****define member methods.***//
		@Override
		public final int compare(final File file1, final File file2) {
			if((file1 != null) && (file2 != null)) {
				return (int)Math.signum(file2.lastModified() - file1.lastModified());
			}
			return 0;
		}
	}

}
