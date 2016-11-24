package net.shadowfacts.discordchat.one_seven_ten;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * @author shadowfacts
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneSevenTenMod.config.sendJoinLeaveMessages()) {
			OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().join(event.player.getDisplayName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneSevenTenMod.config.sendJoinLeaveMessages()) {
			OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().leave(event.player.getDisplayName()));
		}
	}

}
