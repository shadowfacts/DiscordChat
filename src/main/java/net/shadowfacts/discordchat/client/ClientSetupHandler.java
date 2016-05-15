package net.shadowfacts.discordchat.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.shadowfacts.discordchat.DCPrivateProps;

/**
 * @author shadowfacts
 */
public class ClientSetupHandler {

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (!DCPrivateProps.setup && event.gui instanceof GuiMainMenu) {
			event.gui = new GuiSetup();
		}
	}

}
