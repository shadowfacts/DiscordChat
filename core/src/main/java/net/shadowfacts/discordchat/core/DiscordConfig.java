package net.shadowfacts.discordchat.core;

/**
 * @author shadowfacts
 */
public interface DiscordConfig {

	String getToken();

	String getServerID();

	String getChannelID();

	MinecraftAdapter getAdapter();

}
