package net.shadowfacts.discordchat.commands;

import net.dv8tion.jda.entities.User;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.shadowfacts.discordchat.discord.DiscordThread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Naosyth
 */
public class ChatCommands {
	public boolean cconline(User sender, String args) {
		List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().getPlayerList();
		int numPlayers = players.size();

		if (numPlayers == 0) {
			DiscordThread.instance.sendMessageToAllChannels("No players are currently online.");
			return true;
		}

		List<String> names = new ArrayList<String>();
		for (EntityPlayerMP player : players) {
			names.add(player.getName());
		}
		String nameStrings = String.join(", ", names);
		DiscordThread.instance.sendMessageToAllChannels("There " + (numPlayers == 1 ? "is" : "are") + " currently **" + numPlayers + "** player" + (numPlayers == 1 ? "" : "s") + " online:");
		DiscordThread.instance.sendMessageToAllChannels(nameStrings);
		return true;
	}
}
