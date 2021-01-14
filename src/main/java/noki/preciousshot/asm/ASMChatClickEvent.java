package noki.preciousshot.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.mode.ModeGuiViewing;
import noki.preciousshot.resource.ResourceManager;

public class ASMChatClickEvent {

    public static void handleComponentClick(Style style) {

        PreciousShotCore.log("enter handleComponentClick");

        if(style == null) {
            return;
        }

        if(Screen.hasShiftDown()) {
            return;
        }

        ClickEvent clickevent = style.getClickEvent();

        if(clickevent == null) {
            return;
        }

        String fileName = LangHelper.getViewOpenString(clickevent);
        if(fileName == null) {
            return;
        }

        int index = ResourceManager.getResourceIndex(fileName);
        if(index == -1) {
            index = 0;
        }
        ModeGuiViewing gui = new ModeGuiViewing();
        Minecraft.getInstance().displayGuiScreen(gui);
        gui.setEachView(index);

    }

}
