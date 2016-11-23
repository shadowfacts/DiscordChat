package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;
import net.shadowfacts.discordchat.api.*;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.core.command.CommandManager;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandCommands;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandHelp;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandExecute;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandOnline;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandTPS;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandPermission;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandSetPermission;
import net.shadowfacts.discordchat.core.permission.PermissionManager;
import net.shadowfacts.discordchat.core.util.QueuedMessage;

import java.util.List;
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

	private final LinkedBlockingQueue<QueuedMessage> sendQueue = new LinkedBlockingQueue<>();
	private TextChannel channel;

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
		commandManager.register(new CommandExecute(this));
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
			} catch (Exception e) {
				logger.warn(e, "Unable to connect to discord");
			}
		}, "DiscordChat-initializer").start();
	}

	@Override
	public void start() {
		new Thread(() -> {
			while (true) {
				if (jda != null && sendQueue.peek() != null) {
					try {
						RestAction<Message> result = sendQueue.element().send();
						result.block();
						sendQueue.remove();
					} catch (RateLimitedException e) {
						logger.debug("Message was rate limited, will try again in " + e.getRetryAfter());
						try {
							Thread.sleep(e.getRetryAfter());
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}, "DiscordChat-send-queue").start();
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
		if (channel == null) {
			throw new NullPointerException("channel cannot be null");
		}
		if (message == null || message.isEmpty()) return;
		try {
			sendQueue.put(new QueuedMessage(message, channel));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(String message) {
		if (channel == null) {
			List<TextChannel> channels = jda.getTextChannelsByName(config.getChannel(), false);
			if (channels.size() < 1) {
				throw new RuntimeException("No such channel: " + config.getChannel());
			} else {
				channel = channels.get(0);
			}
		}
		sendMessage(message, channel);
	}

}
