package net.shadowfacts.discordchat.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.shadowfacts.discordchat.discord.DiscordThread;

/**
 * Created by James Hollowell on 4/14/2016.
 */
public class CommandWho extends Command {

    @Override
    public void doCommand(String channel, String[] args) {
        DiscordThread.instance.sendMessageToChannel(channel, MinecraftServer.getServer().getConfigurationManager().func_152609_b(false));
    }
}
