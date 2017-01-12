package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
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
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandExecute;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandOnline;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandTPS;
import net.shadowfacts.discordchat.core.command.impl.minecraft.CommandUnstick;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandPermission;
import net.shadowfacts.discordchat.core.command.impl.permissions.CommandSetPermission;
import net.shadowfacts.discordchat.core.permission.PermissionManager;
import net.shadowfacts.discordchat.core.util.QueuedMessage;

import java.io.File;
import java.io.IOException;
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

	private boolean running = true;

	private JDA jda;

	private final LinkedBlockingQueue<QueuedMessage> sendQueue = new LinkedBlockingQueue<>();
	private TextChannel channel;

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
		commandManager.register(new CommandPermission(this));
		commandManager.register(new CommandSetPermission(this));
	}

	@Override
	public void connect() {
		new Thread(() -> {
			try {
				jda = new JDABuilder(AccountType.BOT)
						.setToken(config.getToken())
						.addListener(new Listener(this))
						.buildBlocking();
			} catch (Exception e) {
				throw new RuntimeException(e);
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
			try {
				if (candidate == null)
					candidate = sendQueue.take();
				
				RestAction<Message> result = candidate.send();
				result.block();
				candidate = null; // Upon a succesful result, reset the candidate
			} catch (InterruptedException e) {
				e.printStackTrace(); // Since we're not explicitly invoking Thread interrupt, this should never occur.
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
	public void sendMessage(String message, MessageChannel channel) {
		if (channel == null) {
			throw new NullPointerException("channel cannot be null");
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

		sendQueue.add(new QueuedMessage(message, channel));
	}

	@Override
	public void sendMessage(String message) {
		if (channel == null) {
			while (jda == null) {}
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
