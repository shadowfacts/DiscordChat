package net.shadowfacts.discordchat.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.DCPrivateProps;
import net.shadowfacts.shadowlib.util.DesktopUtils;
import net.shadowfacts.shadowmc.util.StringHelper;

import java.net.URISyntaxException;

/**
 * @author shadowfacts
 */
public class GuiSetup extends GuiScreen {


	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		buttonList.add(new GuiButton(0, (width / 2) - 100, 100, StringHelper.localize("dc.gui.setup.skip")));
		buttonList.add(new GuiButton(1, (width / 2) - 100, 70, StringHelper.localize("dc.gui.setup.finished")));
		buttonList.add(new GuiButton(2, (width / 2) - 100, 40, StringHelper.localize("dc.gui.setup.instructions")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 0: // Skip
				DCPrivateProps.setup = true;
				DCPrivateProps.save();
				Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
				break;
			case 1: // Finished
				DCPrivateProps.setup = true;
				DCPrivateProps.save();
				DCConfig.load();
				Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
				break;
			case 2: // Instructions
				try {
					DesktopUtils.openWebpage("https://git.io/vrGte");
				} catch (URISyntaxException ignored) {}
				break;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		drawCenteredString(fontRendererObj, StringHelper.localize("dc.gui.setup.line1"), width / 2, 10, 0xffffffff);
		drawCenteredString(fontRendererObj, StringHelper.localize("dc.gui.setup.line2"), width / 2, fontRendererObj.FONT_HEIGHT + 14, 0xffffffff);
	}

}
