package noki.preciousshot.mode;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraft.util.ResourceLocation;
import noki.preciousshot.PreciousShotConf;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.LangHelper.LangKey;
import noki.preciousshot.helper.RenderHelper;
//import noki.preciousshot.helper.TwitterHelper;
import noki.preciousshot.resource.ResourceManager;
import noki.preciousshot.resource.ResourceManager.ShotResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**********
 * @class ModeGuiViewing
 * @inner_class SettingButton, WithStrButton, GridSetButton, FrameSetButton
 *
 * @description 閲覧モードのGUIです。
 * @descriptoin_en 
 */
public class ModeGuiViewing extends Screen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String domain = "preciousshot";
	private static final ResourceLocation texture = new ResourceLocation(domain, "textures/gui/settings.png");
	
	private static final int charX = 160;
	private static final int charY = 8;
	
	private static final int menuWidth = 104;
	private static final int menuTop = 5;
	
	private static final int boxWidth = 100;
	private static final int boxHeight = 75;
	private static final int innerBoxWidth = 80;
	private static final int innerBoxHeight = 60;
	
	private ViewingButton openButton;
	private ViewingButton prevButton;
	private ViewingButton nextButton;
	private ViewingButton settingButton;
	
//	private ViewingButton tweetButton;
	private ViewingButton returnButton;
	private ViewingButton eachPrevButton;
	private ViewingButton eachNextButton;
	
//	private TextFieldWidget twitterText;
//	private ViewingButton twitterSendButton;
//	private ViewingButton twitterCancelButton;
	
	private int pageNum;
	private Phase currentPhase;
	private int currentIndex;
//	private boolean twitter = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ModeGuiViewing() {
		super(new StringTextComponent("PreciousShotViewing"));
	}

	@Override
	public void init() {

		this.buttons.clear();
		
		int x = (this.width-menuWidth)/2;
		int y = menuTop;
		
		this.openButton		= new ViewingButton(0,	0+x,	0+y, 16, 16, 32, 64, button -> {
			File directory = ResourceManager.screenshotsDirectory;
			if(directory.exists()) {
				try {
					Runtime rt = Runtime.getRuntime();
					rt.exec(String.format("cmd.exe /C start \"Open file\" \"%s\"", directory.getAbsolutePath()));
				} catch (IOException ex) {
				}
			}
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.directory"));
				return message;
			}
		};
		this.prevButton		= new ViewingButton(1,	18+x,	0+y, 16, 16, 32, 96, button -> {
			if(this.getMaxPage() == 0) {
				this.pageNum = 1;
			}
			else {
				this.pageNum = (this.pageNum-1);
				if(this.pageNum == -1) {
					this.pageNum = this.getMaxPage()-1;
				}
			}
		}) {
			@Override
			public boolean showHint() { return false;}
		};
		this.nextButton		= new ViewingButton(2,	70+x,	0+y, 16, 16, 48, 96, button -> {
			if(this.getMaxPage() == 0) {
				this.pageNum = 1;
			}
			else {
				this.pageNum = (this.pageNum+1) % this.getMaxPage();
			}
		}) {
			@Override
			public boolean showHint() { return false;}
		};
		this.settingButton	= new ViewingButton(3,	88+x,	0+y, 16, 16, 64, 96, button -> {
			this.minecraft.displayGuiScreen(new ModeGuiSetting());
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.setting"));
				return message;
			}
		};
		
/*		this.tweetButton	= new ViewingButton(4,	160+x,	0+y, 16, 16, 80, 96, button -> {
			PreciousShotCore.log("enter button 4.");
			if(TwitterHelper.isEnable()) {
				PreciousShotCore.log("enter twitter.");
				this.twitter = true;
				this.twitterText.setVisible(true);
				this.twitterText.setEnabled(true);
				this.twitterSendButton.enable();
				this.twitterCancelButton.enable();
			}
			else {
				LangHelper.sendChat(LangKey.TWITTER_DISABLED);
			}
		});*/

		this.returnButton	= new ViewingButton(5,	178+x,	0+y, 16, 16, 96, 96, button -> {
			currentPhase = Phase.LIST;
			currentIndex = 0;
//			this.tweetButton.disable();
			this.returnButton.disable();
			this.eachPrevButton.disable();
			this.eachNextButton.disable();
//			this.twitter = false;
//			this.twitterText.setVisible(false);
//			this.twitterText.setEnabled(false);
//			this.twitterSendButton.disable();
//			this.twitterCancelButton.disable();
		});
		this.eachPrevButton	= new ViewingButton(6,	142+x,	0+y, 16, 16, 112, 96, button -> {
			if(ResourceManager.getSize() == 0) {
				this.currentIndex = 0;
			}
			else {
				this.currentIndex = (this.currentIndex-1);
				if(this.currentIndex == -1) {
					this.currentIndex = ResourceManager.getSize()-1;
				}
			}
		});
		this.eachNextButton	= new ViewingButton(7,	160+x,	0+y, 16, 16, 128, 96, button -> {
			if(ResourceManager.getSize() == 0) {
				this.currentIndex = 0;
			}
			else {
				this.currentIndex = (this.currentIndex+1) % ResourceManager.getSize();
			}
		});
		
/*		this.twitterText = new TextFieldWidget(this.font, this.width/2-100, this.height/2, 200, 20, null);
		this.twitterSendButton = new ViewingButton(9, this.width/2 - 18, this.height/2 + 24, 16, 16, 80, 96, button -> {
			if(TwitterHelper.isEnable()) {
				ShotResource resource = ResourceManager.getResource(currentIndex);
				if(resource != null) {
					TwitterHelper.tweetMedia(this.twitterText.getText(), resource.file);
					this.twitter = false;
					this.twitterText.setVisible(false);
					this.twitterText.setEnabled(false);
					this.twitterSendButton.disable();
					this.twitterCancelButton.disable();
				}
			}
			else {
				LangHelper.sendChat(LangKey.TWITTER_DISABLED);
			}
		});
		this.twitterCancelButton = new ViewingButton(10, this.width/2, this.height/2 + 24, 16, 16, 96, 96, button -> {
			this.twitter = false;
			this.twitterText.setVisible(false);
			this.twitterText.setEnabled(false);
			this.twitterSendButton.disable();
			this.twitterCancelButton.disable();
		});*/
		
//		this.tweetButton.disable();
		this.returnButton.disable();
		this.eachPrevButton.disable();
		this.eachNextButton.disable();
		
//		this.twitterSendButton.disable();
//		this.twitterCancelButton.disable();
		
//		Keyboard.enableRepeatEvents(true);
//		this.twitterText.setVisible(false);
//		this.twitterText.setEnabled(false);
//		this.twitterText.setFocused(false);
//		this.twitterText.setMaxStringLength(80);
//		this.twitterText.setTextColor(0xffffff);

		this.addButton(this.openButton);
		this.addButton(this.prevButton);
		this.addButton(this.nextButton);
		this.addButton(this.settingButton);
//		this.addButton(this.tweetButton);
		this.addButton(this.returnButton);
		this.addButton(this.eachPrevButton);
		this.addButton(this.eachNextButton);
//		this.addButton(this.twitterSendButton);
//		this.addButton(this.twitterCancelButton);
		
		this.pageNum = 0;
		this.currentPhase = Phase.LIST;
		this.currentIndex = 0;
		
		RenderHelper.disableCrosshairs();
		RenderHelper.disableHotbar();
		
		ResourceManager.reloadResources();
		
	}
	
/*	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		switch (button.id) {
			case 0:
				File directory = ResourceManager.screenshotsDirectory;
				if(directory.exists()) {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec(String.format("cmd.exe /C start \"Open file\" \"%s\"", directory.getAbsolutePath()));
					} catch (IOException ex) {
					}
				}
				break;
			case 1:
				if(this.getMaxPage() == 0) {
					this.pageNum = 1;
				}
				else {
					this.pageNum = (this.pageNum-1);
					if(this.pageNum == -1) {
						this.pageNum = this.getMaxPage()-1;
					}
				}
				break;
			case 2:
				if(this.getMaxPage() == 0) {
					this.pageNum = 1;
				}
				else {
					this.pageNum = (this.pageNum+1) % this.getMaxPage();
				}
				break;
			case 3:
				this.mc.displayGuiScreen(new ModeGuiSetting());
				break;
			case 4:
				PreciousShotCore.log("enter button 4.");
				if(TwitterHelper.isEnable()) {
					PreciousShotCore.log("enter twitter.");
					this.twitter = true;
					this.twitterText.setVisible(true);
					this.twitterText.setEnabled(true);
					this.twitterSendButton.enable();
					this.twitterCancelButton.enable();
				}
				else {
					LangHelper.sendChat(LangKey.TWITTER_DISABLED);
				}
				break;
			case 5:
				currentPhase = Phase.LIST;
				currentIndex = 0;
				this.tweetButton.disable();
				this.returnButton.disable();
				this.eachPrevButton.disable();
				this.eachNextButton.disable();
				this.twitter = false;
				this.twitterText.setVisible(false);
				this.twitterText.setEnabled(false);
				this.twitterSendButton.disable();
				this.twitterCancelButton.disable();
				break;
			case 6:
				if(ResourceManager.getSize() == 0) {
					this.currentIndex = 0;
				}
				else {
					this.currentIndex = (this.currentIndex-1);
					if(this.currentIndex == -1) {
						this.currentIndex = ResourceManager.getSize()-1;
					}
				}
				break;
			case 7:
				if(ResourceManager.getSize() == 0) {
					this.currentIndex = 0;
				}
				else {
					this.currentIndex = (this.currentIndex+1) % ResourceManager.getSize();
				}
				break;
			case 9:
				if(TwitterHelper.isEnable()) {
					ShotResource resource = ResourceManager.getResource(currentIndex);
					if(resource != null) {
						TwitterHelper.tweetMedia(this.twitterText.getText(), resource.file);
						this.twitter = false;
						this.twitterText.setVisible(false);
						this.twitterText.setEnabled(false);
						this.twitterSendButton.disable();
						this.twitterCancelButton.disable();
					}
				}
				else {
					LangHelper.sendChat(LangKey.TWITTER_DISABLED);
				}
				break;
			case 10:
				this.twitter = false;
				this.twitterText.setVisible(false);
				this.twitterText.setEnabled(false);
				this.twitterSendButton.disable();
				this.twitterCancelButton.disable();
			default:
				break;
		}
		
	}*/

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

//		GuiUtils.drawRect(0, 0, this.width, this.height, 0x50000000);
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0x50000000, 0x50000000);
		RenderSystem.color4f(255F, 255F, 255F, 255F);
		
		this.minecraft.getTextureManager().bindTexture(texture);
		GuiUtils.drawTexturedModalRect((this.width-menuWidth)/2+36, 5, 0, 96, 32, 16,10);
		
		String str = String.valueOf(this.pageNum+1) + "/" + String.valueOf(this.getMaxPage());
		int adjustLeft = (28 - str.length()*5) / 2;
		this.drawStringFromTexture(str, (this.width-menuWidth)/2+36+adjustLeft, 11);
		
		switch(currentPhase) {
			case LIST:
				int hNum = this.getColNum();
				int vNum = this.getRowNum();
				int left = (this.width - (hNum * boxWidth)) / 2;
				int top = (this.height - (vNum * boxHeight)) / 2;
				
				int numPerPage = hNum * vNum;
				for(int i=0; i<=numPerPage-1; i++) {
					ShotResource resource = ResourceManager.getResource(i+this.pageNum*numPerPage);
					if(resource == null) {
//						PreciousShotCore.log("resource is null: {}.", i+this.pageNum*numPerPage);
						break;
					}
//					PreciousShotCore.log("try to render each image.");
					this.minecraft.getTextureManager().bindTexture(resource.location);
					int scaledWidth = innerBoxWidth;
					int scaledHeight = resource.height * innerBoxWidth/ resource.width;
					if(scaledHeight > innerBoxHeight) {
						scaledWidth = resource.width * innerBoxHeight / resource.height;
						scaledHeight = innerBoxHeight;
					}
					int innerLeft = (boxWidth-scaledWidth) / 2;
					int innerTop = (boxHeight-scaledHeight) / 2;
					int col = i % hNum;
					int row = i / hNum;
//					GuiUtils.drawScaledCustomSizeModalRect(left+col*boxWidth+innerLeft, top+row*boxHeight+innerTop,
//							0, 0, resource.width, resource.height, scaledWidth, scaledHeight, resource.width, resource.height);
//					GuiUtils.drawContinuousTexturedBox(left+col*boxWidth+innerLeft, top+row*boxHeight+innerTop,
//							0, 0, scaledWidth, scaledHeight, resource.width, resource.height, 0, 0, 0 ,0, 10);
//					RenderHelper.drawScaledTextureRect(left+col*boxWidth+innerLeft, top+row*boxHeight+innerTop, scaledWidth, scaledHeight,
//							0, 0, resource.width, resource.height, resource.width, resource.height);
					RenderHelper.drawScaledTexture(left+col*boxWidth+innerLeft, top+row*boxHeight+innerTop, scaledWidth, scaledHeight);
					String name = resource.file.getName();
					if(this.minecraft.fontRenderer.getStringWidth(name) > innerBoxWidth) {
						while(this.minecraft.fontRenderer.getStringWidth(name+"...") > innerBoxWidth) {
							name = name.substring(0, name.length()-2);
						}
						name = name + "...";
					}
					int strLeft = (int)(((double)innerBoxWidth-(double)this.minecraft.fontRenderer.getStringWidth(name)) / 2D);
					this.minecraft.fontRenderer.drawString(matrixStack, name, left+col*boxWidth+(boxWidth-innerBoxWidth)/2+strLeft,
							top+row*boxHeight+(boxHeight-innerBoxHeight)/2+innerBoxHeight+1, 0xFFFFFF);
				}
				break;
			case EACH:
				ShotResource resource = ResourceManager.getResource(currentIndex);
				if(resource == null) {
					break;
				}
				double scale = (double)this.width / (double)this.minecraft.getMainWindow().getWidth();
				double scaledWidth = scale * (double)resource.width;
				double scaledHeight = scale * (double)resource.height;
				if(scaledWidth > (double)this.width*0.8D) {
					scaledHeight = scaledHeight * (double)this.width*0.8D / scaledWidth;
					scaledWidth = (double)this.width*0.8D;
				}
				if(scaledHeight > (double)this.height*0.8D) {
					scaledWidth = scaledWidth * (double)this.height*0.8D / scaledHeight;
					scaledHeight = (double)this.height*0.8D;
				}
				int left2 = (this.width-(int)scaledWidth) / 2;
				int top2 = (this.height-(int)scaledHeight) / 2;
				this.minecraft.getTextureManager().bindTexture(resource.location);
//				drawScaledCustomSizeModalRect(left2, top2, 0, 0,
//						resource.width, resource.height, (int)scaledWidth, (int)scaledHeight, resource.width, resource.height);
//				GuiUtils.drawContinuousTexturedBox(left2, top2,
//						0, 0, (int)scaledWidth, (int)scaledHeight, resource.width, resource.height, 0, 0, 0 ,0, 10);
//				RenderHelper.drawScaledTextureRect(left2, top2, (int)scaledWidth, (int)scaledHeight,
//						0, 0, resource.width, resource.height, resource.width, resource.height);
				RenderHelper.drawScaledTexture(left2, top2, (int)scaledWidth, (int)scaledHeight);

				String name = resource.file.getName();
				if(this.minecraft.fontRenderer.getStringWidth(name) > this.width) {
					while(this.minecraft.fontRenderer.getStringWidth(name+"...") > this.width) {
						name = name.substring(0, name.length()-2);
					}
					name = name + "...";
				}
				int strLeft = (int)(((double)this.width-(double)this.minecraft.fontRenderer.getStringWidth(name)) / 2D);
				this.minecraft.fontRenderer.drawString(matrixStack, name, strLeft, top2+(int)scaledHeight+1, 0xFFFFFF);
				
				break;
		}

		super.render(matrixStack, mouseX, mouseY, partialTicks);
//		this.twitterText.render(matrixStack, mouseX, mouseY, partialTicks);
		for(Widget each: this.buttons) {
			if(each instanceof ModeGuiViewing.ViewingButton) {
				if(((ModeGuiViewing.ViewingButton)each).getHovered() && ((ModeGuiViewing.ViewingButton)each).showHint()) {
					RenderSystem.pushMatrix();
					RenderSystem.translated(0D,0D,100D);
//					GuiUtils.drawHoveringText(matrixStack, ((SettingButton)each).getHint(), mouseX, mouseY+20, minecraft.getMainWindow().getWidth(), minecraft.getMainWindow().getHeight(),
					GuiUtils.drawHoveringText(matrixStack, ((ModeGuiViewing.ViewingButton)each).getHint(), mouseX, mouseY+20, this.width, this.height,
//							minecraft.getMainWindow().getWidth()/2,
							-1,
							0xFF333333, 0xFF000000,0xFF000000, minecraft.fontRenderer);
					RenderSystem.popMatrix();
				}
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
/*		if(this.twitter == true) {
			this.twitterText.mouseClicked(mouseX, mouseY, mouseButton);
		}*/
		
		if(currentPhase == Phase.LIST) {
			int hNum = this.getColNum();
			int vNum = this.getRowNum();
			int left = (this.width - (hNum * boxWidth)) / 2;
			int top = (this.height - (vNum * boxHeight)) / 2;
			if(mouseX-left < 0 || mouseY-top < 0) {
				PreciousShotCore.log("minus value.");
				return false;
			}
			
			double posX = (mouseX-(double)left) / (double)boxWidth;
			double posY = (mouseY-(double)top) / (double)boxHeight;
			if(posX > hNum || posY > vNum) {
				PreciousShotCore.log("out of range.");
				return false;
			}
			
			this.currentIndex = Math.min((int)posX, hNum) + Math.min((int)posY, vNum) * this.getColNum() + this.pageNum*hNum*vNum;
			if(ResourceManager.exists(this.currentIndex) == false) {
				PreciousShotCore.log("not exists.");
				return false;
			}
			this.currentPhase = Phase.EACH;
//			this.tweetButton.enable();
			this.returnButton.enable();
			this.eachPrevButton.enable();
			this.eachNextButton.enable();
		}

		return true;
		
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {

		if ((p_keyPressed_1_ == 256 && this.shouldCloseOnEsc()) || p_keyPressed_1_ == PreciousShotConf.keyNum.get()) {
			this.onClose();
			this.minecraft.displayGuiScreen((Screen)null);
			return true;
		}
		else {
			return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
/*		if(this.twitter == true && this.twitterText..textboxKeyTyped(typedChar, keyCode)) {
			return;
		}*/

	}

	@Override
	public boolean isPauseScreen() {

		return false;

	}

	@Override
	public void onClose() {
		
		RenderHelper.enableCrosshairs();
		RenderHelper.enableHotbar();
//		RenderHelper.recoverOriginal();
		
	}
	
	private void drawStringFromTexture(String string, int x, int y) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		
		for(int i=0; i<=string.length()-1; i++) {
			char token = string.charAt(i);
			if(token == "/".charAt(0)) {
				GuiUtils.drawTexturedModalRect(x+i*5, y, ModeGuiViewing.charX+5*11, ModeGuiViewing.charY, 5, 5, 10);
			}
			else {
				GuiUtils.drawTexturedModalRect(x+i*5, y,
						ModeGuiViewing.charX+5*Integer.parseInt(""+token), ModeGuiViewing.charY, 5, 5, 10);
			}
		}
		
		RenderSystem.disableBlend();
	}
	
	private int getMaxPage() {
		
		int numPerPage = this.getColNum() * this.getRowNum();
		int ceil = 1;
		int r = ResourceManager.getSize()%numPerPage;
		if(r == 0) {
			ceil = 0;
		}
		return (ResourceManager.getSize()-r)/numPerPage + ceil;
		
	}
	
	private int getColNum() {
		
		return this.width/boxWidth;
		
	}
	
	private int getRowNum() {
		
		return (this.height-boxHeight)/boxHeight;
		
	}
	
	public void setEachView(int index) {
		
		this.currentPhase = Phase.EACH;
		this.currentIndex = index;
//		this.tweetButton.enable();
		this.returnButton.enable();
		this.eachPrevButton.enable();
		this.eachNextButton.enable();
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class ViewingButton extends ExtendedButton {
		//*****define member variables.*//
		protected int textureX;
		protected int textureY;
		private int counter;

		
		//*****define member methods.***//
		public ViewingButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, IPressable pressable) {
			super(x, y, width, height, null, pressable);
			this.textureX = textureX;
			this.textureY = textureY;
		}

		@Override
		public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			this.drawButtonBasic(mouseX, mouseY);

			if(this.isHovered) {
				this.counter++;
			}
			else {
				this.counter = 0;
			}
		}

		protected void drawButtonBasic(int mouseX, int mouseY) {
			if(this.visible) {
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(770, 771, 1, 0);
				
				minecraft.getTextureManager().bindTexture(ModeGuiViewing.texture);
				int y = this.textureY;
				if(this.isHovered) {
					y += 113;
				}
				GuiUtils.drawTexturedModalRect(this.x, this.y, this.textureX, y, this.width, this.height, 10);

				RenderSystem.disableBlend();
			}
		}
		
		public void enable() {
			this.active = true;
			this.visible = true;
		}
		
		public void disable() {
			this.active = false;
			this.visible = false;
		}

		public boolean getHovered() {
			return this.isHovered;
		}

		public boolean showHint() { return this.counter > 20;}

		public List<ITextComponent> getHint() {
			ArrayList<ITextComponent> message = new ArrayList<>();
			return message;
		}
	}
	
	private enum Phase {
		
		LIST,
		EACH;
		
	}
	
}
