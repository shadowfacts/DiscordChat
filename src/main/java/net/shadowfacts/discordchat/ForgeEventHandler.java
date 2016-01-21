package net.shadowfacts.discordchat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.shadowfacts.discordchat.discord.DiscordThread;
import net.shadowfacts.discordchat.utils.MiscUtils;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void serverChat(ServerChatEvent event) {
		if (DCConfig.enabled) {
			DiscordThread.instance.sendMessageToAllChannels(MiscUtils.toDiscordMessage(event.username, event.message));
		}
	}

}
