package net.shadowfacts.discordchat.one_eight;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author shadowfacts
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneEightMod.config.sendJoinLeaveMessages()) {
			OneEightMod.discordChat.sendMessage(OneEightMod.discordChat.getFormatter().join(event.player.getDisplayNameString()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneEightMod.config.sendJoinLeaveMessages()) {
			OneEightMod.discordChat.sendMessage(OneEightMod.discordChat.getFormatter().leave(event.player.getDisplayNameString()));
		}
	}

}
