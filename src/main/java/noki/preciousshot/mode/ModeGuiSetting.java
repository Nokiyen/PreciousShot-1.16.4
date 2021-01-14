package noki.preciousshot.mode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import noki.preciousshot.PreciousShotConf;
import noki.preciousshot.PreciousShotConf.FrameSet;
import noki.preciousshot.PreciousShotConf.PSOption;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.RenderHelper;
import static noki.preciousshot.PreciousShotConf.PSOption.*;


/**********
 * @class ModeGuiSetting
 * @inner_class SettingButton, WithStrButton, GridSetButton, FrameSetButton
 *
 * @description 設定モードのGUIです。
 * @descriptoin_en 
 */
public class ModeGuiSetting extends Screen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String domain = "preciousshot";
	private static final ResourceLocation texture = new ResourceLocation(domain, "textures/gui/settings.png");
	private static final int charX = 160;
	private static final int charY = 8;
	
	private static final int menuWidth = 304;
	private static final int menuTop = 5;
	
	private int counter;
	private boolean topMoving = false;
	private boolean rightMoving = false;
	private boolean bottomMoving = false;
	private boolean leftMoving = false;

	private int buttonCounter;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ModeGuiSetting() {
		super(new StringTextComponent("PreciousShotGUI"));
	}
	
	@Override
	public void init() {
		
		this.buttons.clear();
		int x = (this.width-menuWidth)/2;
		int y = menuTop;
		
		this.addButton(new SettingButton(18 + x, 8 + y, 16, 8, 128, 8, GAMMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif();
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	18+x,	0+y,	16,	8,	128,0,	GAMMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif(10);
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new WithStrButton(	34+x,	0+y,	38,	16,	16,	32,	GAMMA,	16, 2, "", "", button -> {
			((SettingButton)button).option.switchEnable();
			RenderHelper.applySettingEffect();
		}));
		this.addButton(new SettingButton(	72+x,	8+y,	16,	8,	144,8,	GAMMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add();
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	72+x,	0+y,	16,	8,	144,0,	GAMMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add(10);
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});

		this.addButton(new SettingButton(90 + x, 8 + y, 16, 8, 128, 8, FOV, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif();
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	90+x,	0+y,	16,	8,	128,0,	FOV, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif(10);
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new WithStrButton(	106+x,	0+y,	38,	16,	54,	32,	FOV,	16, 2, "", "", button -> {
			((SettingButton)button).option.switchEnable();
			RenderHelper.applySettingEffect();
		}));
		this.addButton(new SettingButton(	144+x,	8+y,	16,	8,	144,8,	FOV, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add();
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	144+x,	0+y,	16,	8,	144,0,	FOV, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add(10);
			RenderHelper.applySettingEffect();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		
		this.addButton(new SettingButton(	162+x,	8+y,	16,	8,	128,8,	CONT, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	162+x,	0+y,	16,	8,	128,0,	CONT, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif(10);
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new WithStrButton(	178+x,	0+y,	38,	16,	92,	32,	CONT,	16, 2, "/", "", button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	216+x,	8+y,	16,	8,	144,8,	CONT, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	216+x,	0+y,	16,	8,	144,0,	CONT, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add(10);
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		
		this.addButton(new SettingButton(	234+x,	8+y,	16,	8,	128,8,	PANORAMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	234+x,	0+y,	16,	8,	128,0,	PANORAMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.dif(10);
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new WithStrButton(	250+x,	0+y,	38,	16,	130,32,	PANORAMA, 16, 2, "x", "", button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	288+x,	8+y,	16,	8,	144,8,	PANORAMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add();
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		this.addButton(new SettingButton(	288+x,	0+y,	16,	8,	144,0,	PANORAMA, button -> {
			if(((SettingButton)button).option.isEnable()) ((SettingButton)button).option.add(10);
		}) {
			@Override
			public boolean showHint() { return false;}
		});
		
		this.addButton(new GridSetButton(20,	0+x,	18+y,	16,	16,	0,	0,	GRID, button -> {
			((SettingButton)button).option.add();
			if(((SettingButton)button).option.value() == 0) {
				((SettingButton)button).option.disable();
			}
			else {
				((SettingButton)button).option.enable();
			}
		}));
		
		this.addButton(new SettingButton(	18+x,	18+y,	16,	16,	16,	0,	HIDE, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	36+x,	18+y,	16,	16,	32,	0,	SHOT, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	54+x,	18+y,	16,	16,	48,	0,	MARGIN, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	72+x,	18+y,	16,	16,	48,	64,	CLICK, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	90+x,	18+y,	16,	16,	16,	64,	NIGHT, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		this.addButton(new SettingButton(	108+x,	18+y,	16,	16,	64,	64,	CHAT, button -> {
			((SettingButton)button).option.switchEnable();
		}));
		
		this.addButton(new SettingButton(	126+x,	18+y,	16,	16,	64,	0,	OTHER, button -> {
			int margin = LEFT.value() + RIGHT.value();
			int newLeft = margin - margin/2;
			int newRight = margin - newLeft;
			LEFT.set(newLeft);
			RIGHT.set(newRight);
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.horizontal"));
				return message;
			}
		});
		this.addButton(new SettingButton(	144+x,	18+y,	16,	16,	80,	0,	OTHER, button -> {
			int margin2 = TOP.value() + BOTTOM.value();
			int newTop = margin2 - margin2/2;
			int newBottom = margin2 - newTop;
			TOP.set(newTop);
			BOTTOM.set(newBottom);
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.vertical"));
				return message;
			}
		});
		this.addButton(new SettingButton(	162+x,	18+y,	16,	16,	96,	0,	OTHER, button -> {
			int newWidth = Math.min(this.minecraft.getMainWindow().getHeight() - TOP.value() - BOTTOM.value(), this.minecraft.getMainWindow().getWidth());
			int newHeight = Math.min(this.minecraft.getMainWindow().getWidth() - LEFT.value() - RIGHT.value(), this.minecraft.getMainWindow().getHeight());
			int newLeft2 = (this.minecraft.getMainWindow().getWidth()-newWidth) / 2;
			int newRight2 =(this.minecraft.getMainWindow().getWidth()-newWidth) - newLeft2;
			int newTop2 = (this.minecraft.getMainWindow().getHeight()-newHeight) / 2;
			int newBottom2 = (this.minecraft.getMainWindow().getHeight()-newHeight) - newTop2;
			LEFT.set(newLeft2);
			RIGHT.set(newRight2);
			TOP.set(newTop2);
			BOTTOM.set(newBottom2);
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.center"));
				return message;
			}
		});
		this.addButton(new SettingButton(	180+x,	18+y,	16,	16,	112,0,	OTHER, button -> {
			LEFT.set(0);
			RIGHT.set(0);
			TOP.set(0);
			BOTTOM.set(0);
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.zero"));
				return message;
			}
		});
		
		this.addButton(new FrameSetButton(31,198+x,	18+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotConf.frameSet1, button -> {
			FrameSetButton setButton = (FrameSetButton)button;
			int newLeft3 = (this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) / 2;
			int newRight3 =(this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) - newLeft3;
			int newTop3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) / 2;
			int newBottom3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) - newTop3;
			LEFT.set(newLeft3);
			RIGHT.set(newRight3);
			TOP.set(newTop3);
			BOTTOM.set(newBottom3);
		}));
		this.addButton(new FrameSetButton(32,198+x,	27+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotConf.frameSet2, button -> {
			FrameSetButton setButton = (FrameSetButton)button;
			int newLeft3 = (this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) / 2;
			int newRight3 =(this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) - newLeft3;
			int newTop3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) / 2;
			int newBottom3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) - newTop3;
			LEFT.set(newLeft3);
			RIGHT.set(newRight3);
			TOP.set(newTop3);
			BOTTOM.set(newBottom3);
		}));
		this.addButton(new FrameSetButton(33,252+x,	18+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotConf.frameSet3, button -> {
			FrameSetButton setButton = (FrameSetButton)button;
			int newLeft3 = (this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) / 2;
			int newRight3 =(this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) - newLeft3;
			int newTop3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) / 2;
			int newBottom3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) - newTop3;
			LEFT.set(newLeft3);
			RIGHT.set(newRight3);
			TOP.set(newTop3);
			BOTTOM.set(newBottom3);
		}));
		this.addButton(new FrameSetButton(34,252+x,	27+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotConf.frameSet4, button -> {
			FrameSetButton setButton = (FrameSetButton)button;
			int newLeft3 = (this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) / 2;
			int newRight3 =(this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) - newLeft3;
			int newTop3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) / 2;
			int newBottom3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) - newTop3;
			LEFT.set(newLeft3);
			RIGHT.set(newRight3);
			TOP.set(newTop3);
			BOTTOM.set(newBottom3);
		}));
		
		this.addButton(new SettingButton(	0+x,	0+y,	16,	16,	80,	64,	OTHER, button -> {
			this.minecraft.displayGuiScreen(new ModeGuiViewing());
		}) {
			@Override
			public List<ITextComponent> getHint() {
				ArrayList<ITextComponent> message = new ArrayList<>();
				message.add(new TranslationTextComponent("preciousshot.hint.viewing"));
				return message;
			}
		});
		
		RenderHelper.keepOriginalEffect();
		RenderHelper.applySettingEffect();
		
		RenderHelper.disableCrosshairs();
		
	}
	
/*	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		SettingButton target = (SettingButton)button;
		switch(button.id) {
			case 0: case 5: case 10: case 15:
				if(target.option.isEnable()) target.option.dif();
				break;
			case 1: case 6: case 11: case 16:
				if(target.option.isEnable()) target.option.dif(10);
				break;
			case 3: case 8: case 13: case 18: case 20:
				if(target.option.isEnable()) target.option.add();
				break;
			case 4: case 9: case 14: case 19:
				if(target.option.isEnable()) target.option.add(10);
				break;
			case 2: case 7: case 12: case 17: case 21: case 22: case 23: case 24: case 25: case 26:
				target.option.switchEnable();
				break;
			case 27:
				int margin = LEFT.value() + RIGHT.value();
				int newLeft = margin - margin/2;
				int newRight = margin - newLeft;
				LEFT.set(newLeft);
				RIGHT.set(newRight);
				break;
			case 28:
				int margin2 = TOP.value() + BOTTOM.value();
				int newTop = margin2 - margin2/2;
				int newBottom = margin2 - newTop;
				TOP.set(newTop);
				BOTTOM.set(newBottom);
				break;
			case 29:
				int newWidth = Math.min(this.minecraft.getMainWindow().getHeight() - TOP.value() - BOTTOM.value(), this.minecraft.getMainWindow().getWidth());
				int newHeight = Math.min(this.minecraft.getMainWindow().getWidth() - LEFT.value() - RIGHT.value(), this.minecraft.getMainWindow().getHeight());
				int newLeft2 = (this.minecraft.getMainWindow().getWidth()-newWidth) / 2;
				int newRight2 =(this.minecraft.getMainWindow().getWidth()-newWidth) - newLeft2;
				int newTop2 = (this.minecraft.getMainWindow().getHeight()-newHeight) / 2;
				int newBottom2 = (this.minecraft.getMainWindow().getHeight()-newHeight) - newTop2;
				LEFT.set(newLeft2);
				RIGHT.set(newRight2);
				TOP.set(newTop2);
				BOTTOM.set(newBottom2);
				break;
			case 30:
				LEFT.set(0);
				RIGHT.set(0);
				TOP.set(0);
				BOTTOM.set(0);
				break;
			case 31: case 32: case 33: case 34:
				FrameSetButton setButton = (FrameSetButton)target;
				int newLeft3 = (this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) / 2;
				int newRight3 =(this.minecraft.getMainWindow().getWidth()-setButton.frameSet.width) - newLeft3;
				int newTop3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) / 2;
				int newBottom3 = (this.minecraft.getMainWindow().getHeight()-setButton.frameSet.height) - newTop3;
				LEFT.set(newLeft3);
				RIGHT.set(newRight3);
				TOP.set(newTop3);
				BOTTOM.set(newBottom3);
				break;
			case 35:
				this.minecraft.displayGuiScreen(new ModeGuiViewing());
				break;
			default:
		}
		
		if(0 <= button.id && button.id <= 9) {
			RenderHelper.applySettingEffect();
		}
		
	}*/
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		
		RenderHelper.renderMargin(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height);
		
		this.counter = (this.counter+1)%20;
		RenderHelper.renderBorder(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height, this.counter<=10 ? 0 : 1);
		
		if(GRID.value() != 0) {
			RenderHelper.renderGrid(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height, GRID.value());
		}
		
		int centerX = this.width/2;
		int centerY = this.height/2;
		String strT = "T: "+String.valueOf(TOP.value());
		String strL = "L: "+String.valueOf(RIGHT.value());
		String strB = "B: "+String.valueOf(BOTTOM.value());
		String strR = "R: "+String.valueOf(LEFT.value());
		String strW = "W: "+String.valueOf(this.minecraft.getMainWindow().getWidth()-LEFT.value()-RIGHT.value());
		String strH = "H: "+String.valueOf(this.minecraft.getMainWindow().getHeight()-TOP.value()-BOTTOM.value());
		int adjustLeft = Math.max(this.font.getStringWidth(strL), this.font.getStringWidth(strR));
		adjustLeft = Math.max(adjustLeft, this.font.getStringWidth(strW));
		this.font.drawString(matrixStack, strL, centerX-adjustLeft-2, centerY-10, 0xFFFFFF);
		this.font.drawString(matrixStack, strR, centerX-adjustLeft-2, centerY, 0xFFFFFF);
		this.font.drawString(matrixStack, strW, centerX-adjustLeft-2, centerY+10, 0xFFFFFF);
		this.font.drawString(matrixStack, strT, centerX+1, centerY-10, 0xFFFFFF);
		this.font.drawString(matrixStack, strB, centerX+1, centerY, 0xFFFFFF);
		this.font.drawString(matrixStack, strH, centerX+1, centerY+10, 0xFFFFFF);
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		for(Widget each: this.buttons) {
			if(each instanceof SettingButton) {
				if(((SettingButton)each).getHovered() && ((SettingButton)each).showHint()) {

					RenderSystem.pushMatrix();
					RenderSystem.translated(0D,0D,100D);
//					GuiUtils.drawHoveringText(matrixStack, ((SettingButton)each).getHint(), mouseX, mouseY+20, minecraft.getMainWindow().getWidth(), minecraft.getMainWindow().getHeight(),
					GuiUtils.drawHoveringText(matrixStack, ((SettingButton)each).getHint(), mouseX, mouseY+20, this.width, this.height,
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

		int absX = (int)Math.round((double)mouseX/(double)this.width * (double)this.minecraft.getMainWindow().getWidth());
		int absY = (int)Math.round((double)mouseY/(double)this.height * (double)this.minecraft.getMainWindow().getHeight());
		
		if((LEFT.value()-10 <= absX && absX <= LEFT.value()+10)
				&& (TOP.value()-10 <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value()+10)) {
			this.leftMoving = true;
		}
		if((LEFT.value()-10 <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value()+10)
				&& (TOP.value()-10 <= absY && absY <= TOP.value()+10)) {
			this.topMoving = true;
		}
		if((this.minecraft.getMainWindow().getWidth()-RIGHT.value()-10 <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value()+10)
				&& (TOP.value()-10 <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value()+10)) {
			this.rightMoving = true;
		}
		if((LEFT.value()-10 <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value()+10)
				&& (this.minecraft.getMainWindow().getHeight()-BOTTOM.value()-10 <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value()+10)) {
			this.bottomMoving = true;
		}

/*		if((LEFT.value() <= absX && absX <= LEFT.value()+10)
				&& (TOP.value() <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value())) {
			this.leftMoving = true;
		}
		if((LEFT.value() <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value())
				&& (TOP.value() <= absY && absY <= TOP.value()+10)) {
			this.topMoving = true;
		}
		if((this.minecraft.getMainWindow().getWidth()-RIGHT.value()-10 <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value())
				&& (TOP.value() <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value())) {
			this.rightMoving = true;
		}
		if((LEFT.value() <= absX && absX <= this.minecraft.getMainWindow().getWidth()-RIGHT.value())
				&& (this.minecraft.getMainWindow().getHeight()-BOTTOM.value()-10 <= absY && absY <= this.minecraft.getMainWindow().getHeight()-BOTTOM.value())) {
			this.bottomMoving = true;
		}*/

		return 	super.mouseClicked(mouseX, mouseY, mouseButton);

	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		
		this.leftMoving = false;
		this.topMoving = false;
		this.rightMoving = false;
		this.bottomMoving = false;

		return 	super.mouseReleased(mouseX, mouseY, state);

	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {

		int absX = (int)Math.round(mouseX/(double)this.width * (double)this.minecraft.getMainWindow().getWidth());
		int absY = (int)Math.round(mouseY/(double)this.height * (double)this.minecraft.getMainWindow().getHeight());
		
		if(this.leftMoving == true) {
			LEFT.set(Math.min(absX, this.minecraft.getMainWindow().getWidth()-RIGHT.value()-10));
		}
		if(this.topMoving == true) {
			TOP.set(Math.min(absY, this.minecraft.getMainWindow().getHeight()-BOTTOM.value()-10));
		}
		if(this.rightMoving == true) {
			RIGHT.set(Math.min(this.minecraft.getMainWindow().getWidth()-absX, this.minecraft.getMainWindow().getWidth()-LEFT.value()-10));
		}
		if(this.bottomMoving == true) {
			BOTTOM.set(Math.min(this.minecraft.getMainWindow().getHeight()-absY, this.minecraft.getMainWindow().getHeight()-TOP.value()-10));
		}

		return super.mouseDragged(mouseX, mouseY, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);

	}
	
/*	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		
		if(keyCode == 1 || keyCode == PreciousShotConf.keyNum.get()) {
			this.minecraft.displayGuiScreen((Screen)null);
			if(this.minecraft.currentScreen == null) {
//				this.minecraft
			}
		}
		return super.charTyped(typedChar, keyCode);

	}*/

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		PreciousShotCore.log("on key pressed.");
		if ((p_keyPressed_1_ == 256 && this.shouldCloseOnEsc()) || p_keyPressed_1_ == PreciousShotConf.keyNum.get()) {
			this.onClose();
			this.minecraft.displayGuiScreen((Screen)null);
			return true;
		}
		else {
			return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
	}
	
	@Override
	public boolean isPauseScreen() {
		
		return false;
		
	}
	
	@Override
	public void onClose() {
		
		RenderHelper.recoverOriginalEffect();
		RenderHelper.enableCrosshairs();
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class SettingButton extends ExtendedButton {
		//*****define member variables.*//
		protected int textureX;
		protected int textureY;
		public PSOption option;
		private int counter;
		
		
		//*****define member methods.***//
		public SettingButton(int x, int y, int width, int height, int textureX, int textureY, PSOption option, IPressable pressable) {
			super(x, y, width, height, null, pressable);
			this.textureX = textureX;
			this.textureY = textureY;
			this.option = option;
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
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			
			minecraft.getTextureManager().bindTexture(ModeGuiSetting.texture);
			int y = this.textureY;
			if(this.option.isEnable() == false) {
				y += 16;
			}
			if(this.isHovered) {
				y += 113;
			}
			GuiUtils.drawTexturedModalRect(this.x, this.y, this.textureX, y, this.width, this.height, 0);

			RenderSystem.disableBlend();

//			if(!this.isHovered && this.wasHo)
		}

		public boolean getHovered() {
			return this.isHovered;
		}

		public boolean showHint() { return this.counter > 20;}

		public List<ITextComponent> getHint() {
			ArrayList<ITextComponent> message = new ArrayList<>();
//			message.add(new TranslationTextComponent(this.option.getHintStateKey()).getString() + " : " +
//					new TranslationTextComponent(this.option.getHintKey()).getFormattedText());
			TranslationTextComponent status = new TranslationTextComponent(this.option.getHintStateKey());
			if(this.option.isEnable()) {
				status.mergeStyle(TextFormatting.AQUA);
			}
			message.add(status.append(new StringTextComponent(" : ").setStyle(Style.EMPTY.setFormatting(TextFormatting.WHITE)))
								.append(new TranslationTextComponent(this.option.getHintKey()).setStyle(Style.EMPTY.setFormatting(TextFormatting.WHITE))));
			return message;
		}
	}
	
	private class WithStrButton extends SettingButton {
		//*****define member variables.*//
		private int adjustLeft;
		private int adjustRight;
		private String prefix;
		private String suffix;
		
		
		//*****define member methods.***//
		public WithStrButton(int x, int y, int width, int height, int textureX, int textureY,
				PSOption option, int adjustLeft, int adjustRight, String prefix, String suffix, IPressable pressable) {
			super(x, y, width, height, textureX, textureY, option, pressable);
			this.adjustLeft = adjustLeft;
			this.prefix = prefix;
			this.suffix = suffix;
		}
		
		@Override
		public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//			this.drawButtonBasic(mouseX, mouseY);
			this.drawStringFromTexture(this.prefix+this.option.value()+this.suffix);
			super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
		}
		
		protected void drawStringFromTexture(String string) {
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			
			int strWidth = string.length()*5;
			int left = (this.width - this.adjustLeft - this.adjustRight- strWidth)/2 + this.adjustLeft;
			int top = (this.height - 5)/2;
			for(int i=0; i<=string.length()-1; i++) {
				char token = string.charAt(i);
				int disable = 0;
				if(this.option.isEnable() == false) {
					disable = 5;
				}
				if(token == "x".charAt(0)) {
					GuiUtils.drawTexturedModalRect(this.x+left+i*5, this.y+top,
							ModeGuiSetting.charX+5*10, ModeGuiSetting.charY+disable, 5, 5, 0);
				}
				else if(token == "/".charAt(0)) {
					GuiUtils.drawTexturedModalRect(this.x+left+i*5, this.y+top,
							ModeGuiSetting.charX+5*11, ModeGuiSetting.charY+disable, 5, 5, 0);
				}
				else {
					GuiUtils.drawTexturedModalRect(this.x+left+i*5, this.y+top,
							ModeGuiSetting.charX+5*Integer.parseInt(""+token), ModeGuiSetting.charY+disable, 5, 5, 0);
				}
			}

			RenderSystem.disableBlend();
		}
	}
	
	private class GridSetButton extends SettingButton {
		//*****define member methods.***//
		public GridSetButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, PSOption option, IPressable pressable) {
			super(x, y, width, height, textureX, textureY, option, pressable);
		}
		
		@Override
		protected void drawButtonBasic(int mouseX, int mouseY) {
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);

			minecraft.getTextureManager().bindTexture(ModeGuiSetting.texture);
			int texY = this.textureY;
			if(this.isHovered) {
				texY += 113;
			}
			GuiUtils.drawTexturedModalRect(this.x, this.y,
					this.textureX, this.option.value()*16 + texY, this.width, this.height, 0);

			RenderSystem.disableBlend();
		}
	}
	
	private class FrameSetButton extends WithStrButton {
		//*****define member variables.*//
		public FrameSet frameSet;
		
		
		//*****define member methods.***//
		public FrameSetButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, PSOption option,
				int adjustLeft, int adjustRight, String prefix, String suffix, FrameSet frameSet, IPressable pressable) {
			super(x, y, width, height, textureX, textureY, option, adjustLeft, adjustRight, prefix, suffix, pressable);
			this.frameSet = frameSet;
		}
		
		@Override
		public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//			this.drawButtonBasic(mouseX, mouseY);
			this.drawStringFromTexture(this.frameSet.width+"x"+this.frameSet.height);
			super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
		}

		@Override
		public List<ITextComponent> getHint() {
			ArrayList<ITextComponent> message = new ArrayList<>();
			message.add(new TranslationTextComponent("preciousshot.hint.frame"));
			return message;
		}
	}
	
}
