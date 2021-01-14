package noki.preciousshot.resource;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackFileNotFoundException;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import noki.preciousshot.PreciousShotCore;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;


/**********
 * @class ScreenShotsResourcePack
 *
 * @description screenshotsフォルダを、リソースパックとして扱うためのクラスです。
 * これにより、screenshotsフォルダ内の画像に対してResourceLocationを使うことができます。
 * @descriptoin_en 
 */
public class ScreenShotsResourcePack extends FolderPack {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final Set<String> resourceDomains = ImmutableSet.of("ps_screenshots");
	private File screenshotsDirectory;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ScreenShotsResourcePack(File directory) {

		super(directory);
//		this.screenshotsDirectory = directory;

	}

	@Override
	public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {

		return this.getInputStream(location.getPath());

	}

	@Override
	public InputStream getInputStream(String resourcePath) throws IOException {

//		PreciousShotCore.log("enter getInputStream");
		File file1 = this.getFile(resourcePath);
		if (file1 == null) {
			throw new ResourcePackFileNotFoundException(this.file, resourcePath);
		} else {
			return new FileInputStream(file1);
		}

	}

	@Nullable
	private File getFile(String p_195776_1_) {
		try {
			File file1 = new File(this.file, p_195776_1_);
			PreciousShotCore.log("{}", file1.getAbsolutePath());
			if(file1.isFile()) {
//				PreciousShotCore.log("is file.");
			}
			if(validatePath(file1, p_195776_1_)) {
//				PreciousShotCore.log("validate path.");
			}
			if (file1.isFile() && validatePath(file1, p_195776_1_)) {
//				PreciousShotCore.log("return the file.");
				return file1;
			}
		} catch (IOException var3) {
			PreciousShotCore.log("getFile() returned null.");
			;
		}

		return null;
	}

	public Set<String> getResourceNamespaces(ResourcePackType type) {
		return resourceDomains;
	}

	public boolean resourceExists(ResourcePackType type, ResourceLocation location) {

//		PreciousShotCore.log("enter resourceExists / {}", location.getPath());
		return this.getFile(location.getPath()) != null;

	}

}
