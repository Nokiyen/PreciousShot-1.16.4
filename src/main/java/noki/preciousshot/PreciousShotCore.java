/**********
 * NOTICE
 * 
 *
 * This work uses the Java library, "Twitter4J (http://twitter4j.org)", licensed under Apache License 2.0.
 * 
 * For Apache License 2.0, see here.
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Especially the class, noki.preciousshot.helper.TwitterHelper, directly uses classes supplied by the library.
 * 
 **********/


package noki.preciousshot;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import noki.preciousshot.mode.ModeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import noki.preciousshot.event.EventCommon;
//import noki.preciousshot.mode.ModeManager;
import noki.preciousshot.resource.ResourceManager;


/**********
 * @Mod PreciousShot
 *
 * @author Nokiyen
 *
 * @description スクリーンショットを強化するmodです。
 * @description_en Mod for adding variable functions for screenshot.
 */
@Mod("preciousshot")
public class PreciousShotCore {

	//******************************//
	// define member variables.
	//******************************//
	public static boolean DEBUG = true;
	public static Logger logger = LogManager.getLogger();


	//******************************//
	// define member methods.
	//******************************//
	public PreciousShotCore() {

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
		PreciousShotConf.loadConfig();

		//for version notification.
		MinecraftForge.EVENT_BUS.register(new EventCommon());

	}

	//----------
	//Core Event Methods.
	//----------
	private void postInit(final FMLLoadCompleteEvent event) {

		ResourceManager.init();

	}

	public void onClientSetup(FMLClientSetupEvent event) {

		ModeManager.init();

	}

	//----------
	//Static Method.
	//----------
	public static void log(String message, Object... data) {

		if(DEBUG == true) {
			logger.info("[PreciousShot:LOG] "+message, data);
		}

	}

}