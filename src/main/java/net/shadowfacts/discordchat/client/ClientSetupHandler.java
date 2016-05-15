package net.shadowfacts.discordchat.client;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.discordchat.DCPrivateProps;

/**
 * @author shadowfacts
 */
public class ClientSetupHandler {

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (!DCPrivateProps.setup && event.getGui() instanceof GuiMainMenu) {
			event.setGui(GUISetup.create());
		}
	}

}
