package net.shadowfacts.discordchat.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.DCPrivateProps;
import net.shadowfacts.discordchat.DiscordChat;
import net.shadowfacts.shadowmc.ui.element.UILabel;
import net.shadowfacts.shadowmc.ui.element.button.UIButtonLink;
import net.shadowfacts.shadowmc.ui.element.button.UIButtonText;
import net.shadowfacts.shadowmc.ui.element.view.UIStackView;
import net.shadowfacts.shadowmc.ui.util.UIBuilder;
import net.shadowfacts.shadowmc.util.MouseButton;

/**
 * @author shadowfacts
 */
public class GUISetup {

	public static GuiScreen create() {
		UIStackView view = new UIStackView("setup");

		UILabel line1 = new UILabel(I18n.format("dc.gui.setup.line1"), "line1");
		view.add(line1);

		UILabel line2 = new UILabel(I18n.format("dc.gui.setup.line2"), "line2");
		view.add(line2);

		UIButtonText instructions = new UIButtonLink(I18n.format("dc.gui.setup.instructions"), "https://git.io/vrGte", "instructions");
		view.add(instructions);

		UIButtonText finished = new UIButtonText(I18n.format("dc.gui.setup.finished"), GUISetup::handleFinished, "finished");
		view.add(finished);

		UIButtonText skip = new UIButtonText(I18n.format("dc.gui.setup.skip"), GUISetup::handleSkip, "skip");
		view.add(skip);

		return new UIBuilder().add(view).style(DiscordChat.modId + ":setup").createScreen();
	}

	private static boolean handleFinished(UIButtonText button, MouseButton mouseButton) {
		DCPrivateProps.setup = true;
		DCPrivateProps.save();
		DCConfig.load();
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		return true;
	}

	private static boolean handleSkip(UIButtonText button, MouseButton mouseButton) {
		DCPrivateProps.setup = true;
		DCPrivateProps.save();
		DCConfig.load();
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		return true;
	}

}
