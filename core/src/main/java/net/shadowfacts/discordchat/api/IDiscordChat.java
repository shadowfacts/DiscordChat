package net.shadowfacts.discordchat.api;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
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

	default boolean sendPrivateMessage(String sender, String message, String user) {
		List<Member> members = getJDA().getGuildById(getConfig().getServerID()).getMembersByEffectiveName(user, true);
		if (!members.isEmpty()) {
			members.get(0).getUser().openPrivateChannel().queue(channel -> {
				sendMessage(getFormatter().fromMCPrivate(sender, message), channel);
			});
			return true;
		} else {
			return false;
		}
	}

	default void sendMessage(String message, MessageChannel channel) {
		sendMessage(message, Collections.singletonList(channel));
	}

	void sendMessage(String message, List<? extends MessageChannel> channel);

	void sendMessage(String message);

	String filterMCMessage(String message);

}
