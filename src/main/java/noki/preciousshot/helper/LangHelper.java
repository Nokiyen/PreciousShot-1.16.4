package noki.preciousshot.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.ITextComponent;
import noki.preciousshot.PreciousShotCore;


/**********
 * @class LangHelper
 * @inner_class LangKey
 *
 * @description lang key及びチャットに関するヘルパークラスです。
 * @descriptoin_en 
 */
public class LangHelper {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String clickEventPrefix = "2aaf7189bdef12:";
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void sendChat(LangKey key, Object... inserts) {

		sendChat(new TranslationTextComponent(key.key(), inserts));
		
	}
	
	public static void sendChatWithUrl(LangKey key, LangKey urlKey, String url, Object... urlInserts) {
		
		ITextComponent component = new TranslationTextComponent(urlKey.key(), urlInserts);
		component.getStyle().setUnderlined(true);
		component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		
		sendChat(new TranslationTextComponent(key.key(), component));
		
	}
	
	public static void sendChatWithViewOpen(LangKey key, LangKey urlKey, String fileName, Object... urlInserts) {
		
		TranslationTextComponent component = new TranslationTextComponent(urlKey.key(), urlInserts);
		component.setStyle(Style.EMPTY.setUnderlined(true).setClickEvent(new ClickEvent(null, clickEventPrefix+fileName)));

		sendChat(new TranslationTextComponent(key.key()).append(component));
		
	}
	
	public static void sendChat(ITextComponent chatComponent) {
		
		Minecraft.getInstance().player.sendMessage(chatComponent, Util.DUMMY_UUID);
		
	}
	
	public static String getViewOpenString(ClickEvent event) {

		PreciousShotCore.log("{}", event.getValue());
		if(event.getValue().length() < clickEventPrefix.length()) {
			return null;
		}
		
		boolean res = event.getValue().substring(0, clickEventPrefix.length()).equals(clickEventPrefix);
		return res ? event.getValue().substring(clickEventPrefix.length()) : null;
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public enum LangKey {
		//*****define enums.************//
		SHOOTING_MODE	("preciousshot.chat.shooting.mode"),
		SHOOTING_DONE	("preciousshot.chat.shooting.done"),
		SHOOTING_FAILED	("preciousshot.chat.shooting.failed"),
		SHOOTING_URL	("preciousshot.chat.shooting.url"),
		PANORAMA_MODE	("preciousshot.chat.panorama.mode"),
		PANORAMA_DONE	("preciousshot.chat.panorama.done"),
		PANORAMA_FAILED	("preciousshot.chat.panorama.failed"),
		SHOOTING_STOPPED("preciousshot.chat.shooting.stop"),
		TWITTER_DISABLED("preciousshot.chat.twitter.disabled"),
		TWITTER_FAILED	("preciousshot.chat.twitter.failed"),
		TWITTER_SUCCESS	("preciousshot.chat.twitter.sccess"),
		TWITTER_URL		("preciousshot.chat.twitter.url");
		
		
		//*****define member variables.*//
		private String key;
		
		
		//*****define member methods.***//
		private LangKey(String key) {
			this.key = key;
		}
		
		public String key() {
			return this.key;
		}
		
		public String translated(Object... args) {
			return I18n.format(this.key, args);
		}
	}
	
}
