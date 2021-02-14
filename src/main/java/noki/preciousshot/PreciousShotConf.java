package noki.preciousshot;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;


/**********
 * @class PreciousShotConf
 * @inner_class PSOption, FrameSet
 *
 * @description このModの各種データを保存するクラスです。
 * @descriptoin_en Class to store all of the data of this mod.
 */
public class PreciousShotConf {

	//******************************//
	// define member variables.
	//******************************//
	private static Builder builder;
	private static ForgeConfigSpec configSpec;

	public static IntValue keyNum;
	public static FrameSet frameSet1;
	public static FrameSet frameSet2;
	public static FrameSet frameSet3;
	public static FrameSet frameSet4;

//	public static ConfigValue<String>[] twitterKeys = new ConfigValue[4];


	public static void loadConfig() {

		builder = new Builder();

		PSOption.loadConfig(builder);

		builder.push("Keys");
		keyNum = builder.comment("").defineInRange("keyNum", GLFW.GLFW_KEY_J, 1, 300);
		builder.pop();

		builder.push("Frame Set");
		frameSet1 = new FrameSet(1, builder, 640, 480, "");
		frameSet2 = new FrameSet(2, builder, 640, 360, "");
		frameSet3 = new FrameSet(3, builder, 640, 384, "");
		frameSet4 = new FrameSet(4, builder, 854, 480, "");
		builder.pop();

		builder.push("Twitter");
//		twitterKeys[0] = builder.comment("").define("consumerKey", "");
//		twitterKeys[1] = builder.comment("").define("consumerSecret", "");
//		twitterKeys[2] = builder.comment("").define("accessToken", "");
//		twitterKeys[3] = builder.comment("").define("accessTokenSecret", "");
		builder.pop();

		configSpec = builder.build();

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, configSpec);

		frameSet1.setConfigValue();
		frameSet2.setConfigValue();
		frameSet3.setConfigValue();
		frameSet4.setConfigValue();

	}


	//--------------------
	// Inner Class.
	//--------------------
	public enum PSOption {
		//*****define enums.************//
		TOP("top", true, false, 0, 0, Integer.MAX_VALUE, "top margin.[default: 0]"){
			@Override public void set(int value) {
				this.value.set(MathHelper.clamp(value, this.minValue, Minecraft.getInstance().getMainWindow().getHeight()));
				this.value.save();
			}
		},
		RIGHT("right", true, false, 0, 0, Integer.MAX_VALUE, "right margin.[default: 0]"){
			@Override public void set(int value) {
				this.value.set(MathHelper.clamp(value, this.minValue, Minecraft.getInstance().getMainWindow().getWidth()));
				this.value.save();
			}
		},
		BOTTOM("bottom", true, false, 0, 0, Integer.MAX_VALUE, "bottom margin.[default: 0]"){
			@Override public void set(int value) {
				this.value.set(MathHelper.clamp(value, this.minValue, Minecraft.getInstance().getMainWindow().getHeight()));
				this.value.save();
			}
		},
		LEFT("left", true, false, 0, 0, Integer.MAX_VALUE, "left margin.[default: 0]"){
			@Override public void set(int value) {
				this.value.set(MathHelper.clamp(value, this.minValue, Minecraft.getInstance().getMainWindow().getWidth()));
				this.value.save();
			}
		},
		GAMMA("gamma", false, false, 0, 0, 200, "transparency of margin." ){
			@Override public float getFloat() {
				return (float)this.value() / 100.0F;
			}
		},
		FOV("fov", false, false, 70, 10, 150, "fov, applied when taking shot."),
		CONT("cont", false, false, 5, 2, 20, "times of continuous shots."),
		PANORAMA("panorama", false, false, 6, 1, 20, "times of panorama shots."),
		GRID("grid", true, true, 0, 0, 5, "grid for shot support."),
		HIDE("hide", true, true, 1, 0, 1, "flag for hiding gui elements."),
		SHOT("shot", true, true, 1, 0, 1,""),
		MARGIN("margin", true, true, 1, 0, 1, ""),
		CLICK("click", false, true, 0, 0, 1, ""),
		NIGHT("night", false, true, 0, 0, 1, ""),
		CHAT("chat", true, true, 1, 0, 1, ""),
		OTHER("other", true, true, 1, 0, 1, "");


		//*****define member variables.*//
		protected String name;
		protected boolean enable;
		protected boolean cycle;
		protected int defaultValue;
		protected int maxValue;
		protected int minValue;
		protected String comment;
		protected IntValue value;
		protected BooleanValue flagValue;


		//*****define member methods.***//
		private PSOption(String name, boolean enable, boolean cycle, int defaultValue, int minValue, int maxValue, String comment) {
			this.name = name;
			this.enable = enable;
			this.cycle = cycle;
			this.defaultValue = defaultValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.comment = comment;
		}

		public int value() {
			return this.value.get();
		}

		public double getDouble() {
			return (double)this.value();
		}

		public float getFloat() {
			return (float)this.value();
		}

		public void set(int value) {
			this.value.set(MathHelper.clamp(value, this.minValue, this.maxValue));
			this.value.save();
		}

		public void add() {
			this.add(1);
		}

		public void add(int step) {
			int newValue = this.value.get() + step;

			if(newValue <= this.maxValue) {
				this.value.set(newValue);
				this.value.save();
			}
			else {
				if(this.cycle) {
					this.value.set(newValue % (this.maxValue+1));
				}
				else {
					this.value.set(this.maxValue);
				}
				this.value.save();
			}
		}

		public void dif() {
			this.dif(1);
		}

		public void dif(int step) {
			int newValue = this.value.get() - step;

			if(newValue >= this.minValue) {
				this.value.set(newValue);
				this.value.save();
			}
			else {
				if(this.cycle) {
					this.value.set(newValue % (this.maxValue+1));
				}
				else {
					this.value.set(this.minValue);
				}
				this.value.save();
			}
		}

		public void enable() {
			this.flagValue.set(true);
			this.flagValue.save();
		}

		public void disable() {
			this.flagValue.set(false);
			this.flagValue.save();
		}

		public boolean isEnable() {
			return this.flagValue.get();
		}

		public void switchEnable() {
			if(this.isEnable()) {
				this.disable();
			}
			else {
				this.enable();
			}
		}

		public String getHintKey() {
			return "preciousshot.hint."+this.name;
		}

		public String getHintStateKey() {
			if(this.isEnable()) {
				return "preciousshot.hint.on";
			}
			else {
				return "preciousshot.hint.off";
			}
		}

		//-----static methods.----------//
		public static void loadConfig(Builder builder) {

			builder.push("Main Configuration");

			for(PSOption each: PSOption.values()) {
				each.value = builder.comment(each.comment).defineInRange(each.name, each.defaultValue, each.minValue, each.maxValue);
				each.flagValue = builder.comment(each.comment).define(each.name + "Flag", each.enable);
			}

			builder.pop();
		}
	}

	public static class FrameSet {
		//*****define member variables.*//
		public int id;
		public Builder builder;

		public int defaultWidth;
		public int defaultHeight;
		public int width;
		public IntValue intValueWidth;
		public int height;
		public IntValue intValueHeight;
		public String comment;

//		public boolean forceDisplayWidth = false;	//currently not used.
//		public boolean forceDisplayHeight = false;	//currently not used.


		//*****define member methods.***//
		public FrameSet(int id, Builder builder, int defaultWidth, int defaultHeight, String comment) {
			this.id = id;
			this.builder = builder;
			this.defaultWidth= defaultWidth;
			this.defaultHeight = defaultHeight;
			this.comment = comment;

			this.intValueWidth = this.builder.comment(this.comment).defineInRange("frameWidth"+this.id, this.defaultWidth, 10, Integer.MAX_VALUE);
			this.intValueHeight = this.builder.comment(this.comment).defineInRange("frameHeight"+this.id, this.defaultHeight, 10, Integer.MAX_VALUE);
		}

		public int getWidth() {
			return this.intValueWidth.get();
		}

		public int getHeight() {
			return this.intValueHeight.get();
		}

		public void setConfigValue() {
			this.width = this.intValueWidth.get();
			if(this.width < 10 || Minecraft.getInstance().getMainWindow().getWidth() < this.width) {
				this.width = Minecraft.getInstance().getMainWindow().getWidth();
//				forceDisplayWidth = true;
			}

			this.height = this.intValueHeight.get();
			if(this.height < 10 || Minecraft.getInstance().getMainWindow().getHeight() < this.height) {
				this.height = Minecraft.getInstance().getMainWindow().getHeight();
//				forceDisplayHeight = true;
			}

		}
	}

}