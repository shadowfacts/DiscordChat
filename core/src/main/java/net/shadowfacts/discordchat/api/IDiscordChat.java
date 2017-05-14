package net.shadowfacts.discordchat.api;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;

import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public interface IDiscordChat {

	ILogger getLogger();

	IConfig getConfig();

	ICommandManager getCommandManager();

	IPermissionManager getPermissionManager();

	IMessageFormatter getFormatter();

	JDA getJDA();

	IMinecraftAdapter getMinecraftAdapter();

	void connect();

	void start();

	void stop();

	default void sendMessage(String message, MessageChannel channel) {
		sendMessage(message, Collections.singletonList(channel));
	}

	void sendMessage(String message, List<? extends MessageChannel> channel);

	void sendMessage(String message);

	String filterMCMessage(String message);

}
