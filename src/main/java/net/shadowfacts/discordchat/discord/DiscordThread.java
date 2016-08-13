package net.shadowfacts.discordchat.discord;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.DiscordChat;

import javax.security.auth.login.LoginException;
import java.util.Optional;

/**
 * @author shadowfacts
 */
public class DiscordThread implements Runnable {

	private static Thread thread;

	public static DiscordThread instance;

	public JDA jda;

	public static void runThread() {
		if (thread == null) {
			thread = new Thread(new DiscordThread());
			thread.start();
		} else {
			DiscordChat.log.error("JDA thread is already running!");
		}
	}

	public DiscordThread() {
		instance = this;
	}

	@Override
	public void run() {
		try {
			try {
				jda = new JDABuilder()
						.setBulkDeleteSplittingEnabled(false)
						.setBotToken(DCConfig.botToken)
						.addListener(new MainListener())
						.buildBlocking();
			} catch (LoginException | IllegalArgumentException e) {
				DiscordChat.log.error("Invalid login credentials for Discord, disabling DiscordChat", e);
				DCConfig.enabled = false;
				return;
			} catch (InterruptedException e) {
				DiscordChat.log.error("Couldn't complete login, disabling DiscordChat");
				e.printStackTrace();
				DCConfig.enabled = false;
				return;
			}
			if (jda != null) {
				Guild server = jda.getGuildById(DCConfig.serverId);
				if (server == null) {
					DiscordChat.log.error("Couldn't get the server with the specified ID, please check the config and ensure the ID is correct.");
					DCConfig.enabled = false;
					return;
				}
			}
		} catch (Throwable t) {
			DiscordChat.log.error("Problem starting DiscordChat, disabling", t);
			DCConfig.enabled = false;
		}

	}

	public void sendMessageToAllChannels(String message) {
		for (String name : DCConfig.channels) {
			Optional<TextChannel> channel = getChannel(name);
			if (channel.isPresent()) {
				try {
					channel.get().sendMessage(message);
				} catch (RateLimitException e) {
					DiscordChat.log.warn("The ratelimit for messages in the channel with the ID <" + channel.get.getId() + "> is being hit!");
				} catch (PermissionException e) {
					DiscordChat.log.warn("Bot doesn't have \"Write\" permissions in the channel with the ID <" + channel.get().getId() + ">!");
				} catch (VerificationLevelException e) {
					DiscordChat.log.warn("Bot doesn't meet the verification level in the channel with the ID <" + channel.get().getId() + ">!");
				}
			}
		}
	}

	private Optional<TextChannel> getChannel(String name) {
		if (jda == null) return Optional.empty();
		return jda.getGuildById(DCConfig.serverId).getTextChannels().stream()
				.filter(channel -> channel.getName().equalsIgnoreCase(name))
				.findFirst();
	}

}
