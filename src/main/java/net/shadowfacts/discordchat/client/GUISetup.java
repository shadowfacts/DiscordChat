package net.shadowfacts.discordchat.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.DCPrivateProps;
import net.shadowfacts.shadowlib.util.DesktopUtils;
import net.shadowfacts.shadowmc.gui.component.GUIComponentText;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonText;
import net.shadowfacts.shadowmc.gui.mcwrapper.GuiScreenWrapper;
import net.shadowfacts.shadowmc.gui.mcwrapper.MCBaseGUI;
import net.shadowfacts.shadowmc.util.MouseButton;
import net.shadowfacts.shadowmc.util.StringHelper;

import java.net.URISyntaxException;

/**
 * @author shadowfacts
 */
public class GUISetup extends MCBaseGUI {

	public GUISetup(GuiScreenWrapper wrapper) {
		super(wrapper);

		String line1 = StringHelper.localize("dc.gui.setup.line1");
		String line2 = StringHelper.localize("dc.gui.setup.line2");

		addChild(new GUIComponentText(-(mc.fontRendererObj.getStringWidth(line1) / 2), 10, line1));
		addChild(new GUIComponentText(-(mc.fontRendererObj.getStringWidth(line2) / 2), mc.fontRendererObj.FONT_HEIGHT + 14, line2));

		addChild(new GUIButtonText(-100, 40, 200, 20, this::handleInstructions, StringHelper.localize("dc.gui.setup.instructions")));
		addChild(new GUIButtonText(-100, 70, 200, 20, this::handleFinished, StringHelper.localize("dc.gui.setup.finished")));
		addChild(new GUIButtonText(-100, 100, 200, 20, this::handleSkip, StringHelper.localize("dc.gui.setup.skip")));
	}

	@Override
	public void setInitialized(boolean initialized) {
		super.setInitialized(initialized);
		updatePosition(width / 2, y);
	}

	private boolean handleInstructions(GUIButtonText button, MouseButton mouseButton) {
		try {
			DesktopUtils.openWebpage("https://git.io/vrGte");
		} catch (URISyntaxException ignored) {}
		return true;
	}

	private boolean handleFinished(GUIButtonText button, MouseButton mouseButton) {
		DCPrivateProps.setup = true;
		DCPrivateProps.save();
		DCConfig.load();
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		return true;
	}

	private boolean handleSkip(GUIButtonText button, MouseButton mouseButton) {
		DCPrivateProps.setup = true;
		DCPrivateProps.save();
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		return true;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		// because something else disables GL_TEXTURE_2D without using the GlStateManager
		GlStateManager.disableTexture2D();
		GlStateManager.enableTexture2D();

		super.draw(mouseX, mouseY, partialTicks);
	}

	public static GuiScreen create() {
		GuiScreenWrapper wrapper = new GuiScreenWrapper();
		GUISetup gui = new GUISetup(wrapper);
		gui.setZLevel(0);
		wrapper.gui = gui;
		return wrapper;
	}

}
