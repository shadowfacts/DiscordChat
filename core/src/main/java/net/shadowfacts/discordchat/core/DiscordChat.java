package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;
import net.shadowfacts.discordchat.api.*;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.core.command.CommandManager;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandCommands;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandHelp;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandReload;
import net.shadowfacts.discordchat.core.command.impl.meta.CommandRoleID;
import net.shadowfacts.discordchat.core.command.impl.minecraft.*;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandPermission;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandSetPermission;
import net.shadowfacts.discordchat.core.permission.PermissionManager;
import net.shadowfacts.discordchat.core.util.QueuedMessage;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class DiscordChat implements IDiscordChat {

	private IMinecraftAdapter minecraftAdapter;

	private Logger logger;
	private IConfig config;
	private PermissionManager permissionManager;
	private CommandManager commandManager;
	private MessageFormatter formatter;

	private boolean running;

	private JDA jda;

	private final LinkedBlockingQueue<QueuedMessage> sendQueue = new LinkedBlockingQueue<>();
	private List<TextChannel> channels;

	public DiscordChat(File dcDir, IMinecraftAdapter minecraftAdapter) throws IOException {
		this.minecraftAdapter = minecraftAdapter;

		if (!dcDir.exists()) {
			dcDir.mkdirs();
		}

		logger = new Logger();

		config = new Config(new File(dcDir, "DiscordChat.conf"));
		permissionManager = new PermissionManager(this, new File(dcDir, "permissions.json"));
		commandManager = new CommandManager(this);
		formatter = new MessageFormatter(this);

		commandManager.register(new CommandHelp(this));
		commandManager.register(new CommandCommands(this));
		commandManager.register(new CommandRoleID(this));
		commandManager.register(new CommandReload(this));
		commandManager.register(new CommandOnline(this));
		commandManager.register(new CommandTPS(this));
		commandManager.register(new CommandExecute(this));
		commandManager.register(new CommandUnstick(this));
		commandManager.register(new CommandTime(this));
		commandManager.register(new CommandTell(this));
		commandManager.register(new CommandPermission(this));
		commandManager.register(new CommandSetPermission(this));
	}

	@Override
	public void connect() {
		String token = config.getToken();
		if (token == null || token.isEmpty()) {
			logger.bigWarning("No token provided, DiscordChat automatically disabled.");
			return;
		}

		new Thread(() -> {
			try {
				jda = new JDABuilder(AccountType.BOT)
						.setToken(config.getToken())
						.addEventListener(new Listener(this))
						.buildBlocking();
			} catch (LoginException | IllegalArgumentException e) {
				throw new RuntimeException("Invalid Discord token. Please verify your token in the DiscordChat config file.", e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			running = true;

			Guild guild = jda.getGuildById(config.getServerID());
			if (guild == null) {
				throw new RuntimeException("Invalid server ID");
			}
			if (config.getChannelIDs().isEmpty()) {
				throw new RuntimeException("No channel IDs");
			} else {
				for (long channelID : config.getChannelIDs()) {
					if (guild.getTextChannelById(channelID) == null) {
						throw new RuntimeException("Invalid channel ID: " + channelID);
					}
				}
			}
		}, "DiscordChat-initializer").start();
	}

	@Override
	public void start() {
		if (running) {

			Thread thread = new Thread(this::sendQueueThread, "DiscordChat-send-queue");
			thread.setUncaughtExceptionHandler((thrd, t) -> {
				logger.error(t, "Uncaught exception in DiscordChat-send-queue thread");
			});
			thread.start();
		}
	}
	
	private void sendQueueThread() {
		try {
			while(jda == null) Thread.sleep(50); // Don't hog the CPU
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		QueuedMessage candidate = null;
		while (running) {
			if (jda.getStatus() == JDA.Status.SHUTTING_DOWN || jda.getStatus() == JDA.Status.SHUTDOWN) {
				sendQueue.clear();
				running = false;
				break;
			}

			try {
				if (candidate == null)
					candidate = sendQueue.take();

				RestAction<Message> result = candidate.send();
				result.complete(true);
				candidate = null; // Upon a successful result, reset the candidate
			} catch (InterruptedException e) {
				e.printStackTrace(); // Since we're not explicitly invoking Thread interrupt, this should never occur.
			} catch (RateLimitedException e) {
				logger.debug("Message was rate limited, will try again in " + e.getRetryAfter());
				try {
					Thread.sleep(e.getRetryAfter());
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} catch (ErrorResponseException e) {
				logger.error(e, "Error sending message to Discord: " + e.getErrorResponse().getCode() + " (" + e.getErrorResponse().getMeaning() + ")");
			}
		}
	}

	@Override
	public void stop() {
		running = false;
		jda.shutdown();
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
	public void sendMessage(String message, List<? extends MessageChannel> channels) {
		if (channels.isEmpty()) {
			throw new NullPointerException("channels cannot be empty");
		}
		if (!running) return;
		if (message == null || message.isEmpty()) return;


		List<User> users = jda.getGuildById(config.getServerID()).getMembers().stream()
				.map(Member::getUser)
				.collect(Collectors.toList());
		String temp = message.toLowerCase();
		for (User user : users) {
			if (temp.contains("@" + user.getName().toLowerCase())) {
				message = message.replaceAll("(?i)@" + user.getName(), user.getAsMention());
			}
		}

		for (MessageChannel channel : channels) {
			sendQueue.add(new QueuedMessage(message, channel));
		}
	}

	@Override
	public void sendMessage(String message) {
		if (!running) return;

		if (channels == null) {
			try {
				while (jda == null) Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Guild guild = jda.getGuildById(config.getServerID());
			channels = new ArrayList<>();
			for (long channelID : config.getChannelIDs()) {
				channels.add(guild.getTextChannelById(channelID));
			}
		}
		sendMessage(message, channels);
	}

	@Override
	public String filterMCMessage(String message) {
		switch (config.getMCMessageFilterMode()) {
			case PREFIX:
				if (!message.startsWith(config.getMCMessageFilter())) {
					return null;
				} else {
					if (config.stripFilterPart()) {
						return message.substring(config.getMCMessageFilter().length(), message.length());
					} else {
						return message;
					}
				}
			case SUFFIX:
				if (!message.endsWith(config.getMCMessageFilter())) {
					return null;
				} else {
					if (config.stripFilterPart()) {
						return message.substring(0, message.length() - config.getMCMessageFilter().length());
					} else {
						return message;
					}
				}
			default:
			case NONE:
				return message;
		}
	}

}
