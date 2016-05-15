package net.shadowfacts.discordchat.commands;

import net.minecraft.server.MinecraftServer;
import net.shadowfacts.discordchat.discord.DiscordThread;

/**
 * Created by James Hollowell on 4/14/2016.
 */
public class CommandWho extends Command {

    @Override
    public void doCommand(String channel, String[] args) {
        DiscordThread.instance.sendMessageToChannel(channel, "===Users Online: " + MinecraftServer.getServer().getCurrentPlayerCount() + "===\n" + MinecraftServer.getServer().getConfigurationManager().func_152609_b(false));
    }
}
