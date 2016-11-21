package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author shadowfacts
 */
public class DiscordInterface {

	private DiscordConfig config;

	private JDA jda;

	private LinkedBlockingQueue<Object> sendQueue = new LinkedBlockingQueue<>();

	public DiscordInterface(DiscordConfig config) {
		this.config = config;
		new Thread(() -> {
			try {
				jda = new JDABuilder(AccountType.BOT)
						.setToken(config.getToken())
						.addListener(new DiscordListener(config))
						.buildBlocking();
			} catch (Exception e) {
				throw new RuntimeException("Unable to connect to Discord", e);
			}
		}, "DiscordChat-JDA-creation").start();
	}

	public void sendMessage(String message) {
		sendQueue.add(message);
	}

	public void sendMessage(Message message) {
		sendQueue.add(message);
	}

	public synchronized void sendFirst() {
		TextChannel channel = jda.getGuildById(config.getServerID()).getTextChannelById(config.getChannelID());
		if (channel != null) {
			Object message = sendQueue.peek();
			RestAction<Message> result;
			if (message instanceof Message) {
				result = channel.sendMessage((Message) message);
			} else if (message instanceof String) {
				result = channel.sendMessage((String) message);
			} else {
				throw new RuntimeException("Invalid message type " + message.getClass().getName());
			}
			result.queue(msg -> {
				sendQueue.remove();
			});
		}
	}

}
