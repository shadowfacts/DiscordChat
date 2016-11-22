package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;
import net.shadowfacts.discordchat.api.*;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.core.command.CommandManager;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandCommands;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandHelp;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandOnline;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandTPS;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandPermission;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandSetPermission;
import net.shadowfacts.discordchat.core.permission.PermissionManager;
import net.shadowfacts.discordchat.core.util.QueuedMessage;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author shadowfacts
 */
public class DiscordChat implements IDiscordChat {

	private IMinecraftAdapter minecraftAdapter;

	private ILogger logger;
	private IConfig config;
	private PermissionManager permissionManager;
	private CommandManager commandManager;
	private MessageFormatter formatter;

	private JDA jda;

	private LinkedBlockingQueue<QueuedMessage> sendQueue = new LinkedBlockingQueue<>();

	public DiscordChat(IMinecraftAdapter minecraftAdapter, ILogger logger, IConfig config) {
		this.minecraftAdapter = minecraftAdapter;
		this.logger = logger;
		this.config = config;
		permissionManager = new PermissionManager(this);
		commandManager = new CommandManager(this);
		formatter = new MessageFormatter(this);

		commandManager.register(new CommandHelp(this));
		commandManager.register(new CommandCommands(this));
		commandManager.register(new CommandOnline(this));
		commandManager.register(new CommandTPS(this));
		commandManager.register(new CommandPermission(this));
		commandManager.register(new CommandSetPermission(this));

		initializeJDA();
	}

	private void initializeJDA() {
		new Thread(() -> {
			try {
				jda = new JDABuilder(AccountType.BOT)
						.setToken(config.getToken())
						.addListener(new Listener(this))
						.buildBlocking();

				Thread.currentThread().setName("DiscordChat-sending-thread");

				while (true) {
					if (!sendQueue.isEmpty()) {
						sendFirst();
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to connect to Discord", e);
			}
		}, "DiscordChat-initializer").start();
	}

	@Override
	public ILogger getLogger() {
		return logger;
	}

	@Override
	public IConfig getConfig() {
		return config;
	}

	@Override
	public ICommandManager getCommandManager() {
		return commandManager;
	}

	@Override
	public IPermissionManager getPermissionManager() {
		return permissionManager;
	}

	@Override
	public IMessageFormatter getFormatter() {
		return formatter;
	}

	@Override
	public JDA getJDA() {
		return jda;
	}

	@Override
	public IMinecraftAdapter getMinecraftAdapter() {
		return minecraftAdapter;
	}

	@Override
	public void sendMessage(String message, MessageChannel channel) {
		sendQueue.add(new QueuedMessage(message, channel));
	}

	private void sendFirst() {
		RestAction<Message> result = sendQueue.peek().send();
		try {
			result.block();
			sendQueue.remove();
		} catch (RateLimitedException e) {
			logger.debug("Message was rate limited, will try again in " + e.getRetryAfter());
			try {
				Thread.sleep(e.getRetryAfter());
			} catch (InterruptedException ex) {
				logger.warn(ex, "Sending thread sleep interrupted");
			}
		}
	}

}
