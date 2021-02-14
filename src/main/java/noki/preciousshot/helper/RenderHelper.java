package noki.preciousshot.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import noki.preciousshot.PreciousShotConf.PSOption;
import static noki.preciousshot.PreciousShotConf.PSOption.*;


/**********
 * @class RenderHelper
 * @inner_class FadeStringRender
 *
 * @description 各種描画処理のためのヘルパークラスです。
 * @descriptoin_en 
 */
public class RenderHelper {
	
	//******************************//
	// define member variables.
	//******************************//
	public static double originalGamma;
	public static double originalFov;
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void renderMargin(int top, int right, int bottom, int left, int dispWidth, int dispHeight) {
		
		Minecraft mc = Minecraft.getInstance();

		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        if(PSOption.NIGHT.isEnable()) {
        	RenderSystem.color4f(255F, 255F, 255F, 80F/255F);
        }
        else {
			RenderSystem.color4f(0F, 0F, 0F, 80F/255F);
        }
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		double absTop = (TOP.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absRight = (RIGHT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		double absBottom = (BOTTOM.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absLeft = (LEFT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		placeRect(renderer, 0, 0, dispWidth, absTop);
		placeRect(renderer, 0, (double)dispHeight-absBottom, dispWidth, dispHeight);
		placeRect(renderer, 0, absTop, absLeft, (double)dispHeight-absBottom);
		placeRect(renderer, (double)dispWidth-absRight, absTop, dispWidth, (double)dispHeight-absBottom);
		tessellator.draw();

		RenderSystem.enableTexture();
		RenderSystem.enableBlend();

	}
	
	public static void placeRect(BufferBuilder renderer, double left, double top, double right, double bottom) {

		renderer.pos((double)left, (double)bottom, 0D).endVertex();
		renderer.pos((double)right, (double)bottom, 0D).endVertex();
		renderer.pos((double)right, (double)top, 0D).endVertex();
		renderer.pos((double)left, (double)top, 0D).endVertex();
		
	}


	public static void drawScaledTextureRect(int x, int y, int width, int height, int u, int v, int uEnd, int vEnd, int texWidth, int texHeight) {

		float uStart = (float)u / (float)texWidth;
		float vStart = (float)v / (float)texHeight;
		float uEndF = (float)(u+uEnd) / (float)texWidth;
		float vEndF = (float)(v+vEnd) / (float)texHeight;

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(x, y+height, 0D).tex(uStart, vEndF).endVertex();
		renderer.pos(x+width, y+height, 0D).tex(uEndF, vEndF).endVertex();
		renderer.pos(x+width, y, 0D).tex(uEndF, vStart).endVertex();
		renderer.pos(x, y, 0D).tex(uStart, vStart).endVertex();

		tessellator.draw();

		RenderSystem.enableBlend();

	}

	public static void drawScaledTexture(int x, int y, int width, int height) {

		final float uScale = 1f / 0x100;
		final float vScale = 1f / 0x100;

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);

		BufferBuilder renderer = Tessellator.getInstance().getBuffer();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x, y+height, 0D).tex(0,1).endVertex();
		renderer.pos(x+width, y+height, 0D).tex(1,1).endVertex();
		renderer.pos(x+width, y, 0D).tex(1,0).endVertex();
		renderer.pos(x, y, 0D).tex(0,0).endVertex();

		renderer.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(renderer);

//		RenderSystem.enableBlend();

	}

	public static void drawScaledTexture(int x, int y, int width, int height,
										 int u, int v, int inTexWidth, int inTexHeight, int texWidth, int texHeight) {

//		final float uScale = 1f / 0x100;
//		final float vScale = 1f / 0x100;

		float uStart = (float)u / (float)texWidth;
		float vStart = (float)v / (float)texHeight;
		float uEnd = (float)(u+inTexWidth) / (float)texWidth;
		float vEnd = (float)(v+inTexHeight) / (float)texHeight;

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);

		BufferBuilder renderer = Tessellator.getInstance().getBuffer();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x, y+height, 0D).tex(uStart,vEnd).endVertex();
		renderer.pos(x+width, y+height, 0D).tex(uEnd,vEnd).endVertex();
		renderer.pos(x+width, y, 0D).tex(uEnd,vStart).endVertex();
		renderer.pos(x, y, 0D).tex(uStart,vStart).endVertex();

		renderer.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(renderer);

//		RenderSystem.enableBlend();

	}

	public static void renderBorder(int top, int right, int bottom, int left, int dispWidth, int dispHeight, int type) {
		
		Minecraft mc = Minecraft.getInstance();
		
		double absTop = (TOP.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absRight = (RIGHT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		double absBottom = (BOTTOM.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absLeft = (LEFT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;

		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);
		if(type == 0) {
			GL11.glLineStipple(1 , (short)0xF0F0);
		}
		else {
			GL11.glLineStipple(1 , (short)0x0F0F);
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(1);
		
		placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, absTop);
		placeLine(renderer, (double)dispWidth-absRight, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
		placeLine(renderer, (double)dispWidth-absRight, (double)dispHeight-absBottom, absLeft, (double)dispHeight-absBottom);
		placeLine(renderer, absLeft, (double)dispHeight-absBottom, absLeft, absTop);
		
		tessellator.draw();

		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		GL11.glDisable(GL11.GL_LINE_STIPPLE);

	}
	
	public static void renderGrid(int top, int right, int bottom, int left, int dispWidth, int dispHeight, int type) {
		
		Minecraft mc = Minecraft.getInstance();

//		double absTop = (TOP.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
//		double absRight = (RIGHT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
//		double absBottom = (BOTTOM.getDouble()/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
//		double absLeft = (LEFT.getDouble()/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		double absTop = (top/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absRight = (right/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		double absBottom = (bottom/(double)mc.getMainWindow().getHeight()) * (double)dispHeight;
		double absLeft = (left/(double)mc.getMainWindow().getWidth()) * (double)dispWidth;
		double absWidth = dispWidth - absRight - absLeft;
		double absHeight = dispHeight - absTop - absBottom;

		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(1);
		
		switch(type) {
			case 1://2 by 2
				placeLine(renderer, absLeft+absWidth/2, absTop, absLeft+absWidth/2, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/2, (double)dispWidth-absRight, absTop+absHeight/2);
				break;
			case 2://3 by 3
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/3D*1D, (double)dispWidth-absRight, absTop+absHeight/3D*1D);
				placeLine(renderer, absLeft, absTop+absHeight/3D*2D, (double)dispWidth-absRight, absTop+absHeight/3D*2D);
				break;
			case 3://diagonal
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			case 4://3 by 3 & diagonal
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/3D*1D, (double)dispWidth-absRight, absTop+absHeight/3D*1D);
				placeLine(renderer, absLeft, absTop+absHeight/3D*2D, (double)dispWidth-absRight, absTop+absHeight/3D*2D);
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			case 5://railman
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			default:
		}
		
		tessellator.draw();

		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		
	}
	
	public static void placeLine(BufferBuilder renderer, double x1, double y1, double x2, double y2) {
		
		renderer.pos((double)x1, (double)y1, 0D).endVertex();;
		renderer.pos((double)x2, (double)y2, 0D).endVertex();;
		
	}
	
	public static void keepOriginalEffect() {
		
		originalGamma = Minecraft.getInstance().gameSettings.gamma;
		originalFov = Minecraft.getInstance().gameSettings.fov;

	}
	
	public static void applySettingEffect() {
		
		if(GAMMA.isEnable()) {
			Minecraft.getInstance().gameSettings.gamma = GAMMA.getFloat();
		}
		else {
			Minecraft.getInstance().gameSettings.gamma = originalGamma;
		}
		
		if(FOV.isEnable()) {
			Minecraft.getInstance().gameSettings.fov = FOV.getFloat();
		}
		else {
			Minecraft.getInstance().gameSettings.fov = originalFov;
		}
		
	}
	
	public static void recoverOriginalEffect() {
		
		Minecraft.getInstance().gameSettings.gamma = originalGamma;
		Minecraft.getInstance().gameSettings.fov = originalFov;
		
	}
	
	public static void enableCrosshairs() {
		
		ForgeIngameGui.renderCrosshairs = true;
		
	}
	
	public static void disableCrosshairs() {

		ForgeIngameGui.renderCrosshairs = false;
		
	}
	
	public static void enableHotbar() {

		ForgeIngameGui.renderHotbar = true;
		
	}
	
	public static void disableHotbar() {

		ForgeIngameGui.renderHotbar = false;
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public static class FadeStringRender {
		//*****define member variables.*//
		public String text;
		public int textColor;
		public int xPosition;
		public int yPosition;
		public FontRenderer fontRenderer;
		
		public int readyTime;
		public int fadeInTime;
		public int displayTime;
		public int fadeOutTime;
		
		public FadePhase currentPhase;
		public int currentTime;
		
		
		//*****define member methods.***//
		public FadeStringRender(String text, int textColor, int xPosition, int yPosition, FontRenderer fontRenderer,
				int readyTime, int fadeInTime, int displayTime, int fadeOutTime) {
			this.text = text;
			this.textColor = textColor;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			this.fontRenderer = fontRenderer;
			this.readyTime = readyTime;
			this.fadeInTime = fadeInTime;
			this.displayTime = displayTime;
			this.fadeOutTime = fadeOutTime;
			this.currentPhase = FadePhase.Ready;
			this.currentTime = 0;
		}
		
		public void tick(){
			int caliculated = this.currentTime-this.readyTime;
			if(caliculated== 0) {
				this.currentPhase = FadePhase.FadeIn;
			}
			caliculated -= this.fadeInTime;
			if(caliculated== 0) {
				this.currentPhase = FadePhase.Display;
			}
			caliculated -= this.displayTime;
			if(caliculated== 0) {
				this.currentPhase = FadePhase.FadeOut;
			}
			caliculated -= this.fadeOutTime;
			if(caliculated== 0) {
				this.currentPhase = FadePhase.Finished;
			}
			
			float density = 0.0F;
			switch(this.currentPhase) {
				case FadeIn:
					density = (float)(this.currentTime-this.readyTime) / (float)this.fadeInTime;
					break;
				case Display:
					density = 1.0F;
					break;
				case FadeOut:
					density = 1.0F - 
						(float)(this.currentTime-this.readyTime-this.fadeInTime-this.displayTime) / (float)this.fadeOutTime;
					break;
				default:
					break;
			}
			if(density != 0) {
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(770, 771, 1, 0);

				int color = ((int)(0xFF*density) * 0x01000000)+ this.textColor;
				this.fontRenderer.drawString(new MatrixStack(), this.text, this.xPosition, this.yPosition, color);

				RenderSystem.disableBlend();
			}
			
			this.currentTime++;
		}
		
		public boolean isFinished() {
			return this.currentPhase == FadePhase.Finished ? true : false;
		}
		
		public void resetPhase() {
			this.currentPhase = FadePhase.Ready;
			this.currentTime = 0;
		}
		
		public enum FadePhase {
			Ready,
			FadeIn,
			Display,
			FadeOut,
			Finished;
		}
	}
	
}
