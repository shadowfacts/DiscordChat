package net.shadowfacts.discordchat.core;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.shadowlib.util.IOUtils;

import java.io.*;

/**
 * @author shadowfacts
 */
public class Config implements IConfig {

	private File file;
	private com.typesafe.config.Config config;

	public Config(File file) throws IOException {
		this.file = file;
		load();
	}

	@Override
	public void load() throws IOException {
		config = ConfigFactory.parseFile(file).withFallback(ConfigFactory.load("assets/discordchat/default.conf"));
		save();
	}

	@Override
	public void save() throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		ConfigObject toRender = config.root().withOnlyKey("discordchat");
		String s = toRender.render(ConfigRenderOptions.defaults().setOriginComments(false).setJson(false));
		InputStream in = new ByteArrayInputStream(s.getBytes());
		OutputStream out = new FileOutputStream(file);
		IOUtils.copy(in, out);
		in.close();
		out.close();

	}

	@Override
	public String getToken() {
		return config.getString("discordchat.discord.token");
	}

	@Override
	public String getServerID() {
		return config.getString("discordchat.discord.server");
	}

	@Override
	public String getChannel() {
		return config.getString("discordchat.discord.channel");
	}

	@Override
	public String getCommandPrefix() {
		return config.getString("discordchat.commands.prefix");
	}

	@Override
	public boolean sendDeathMessages() {
		return config.getBoolean("discordchat.relay.deaths");
	}

	@Override
	public boolean sendAchievementMessages() {
		return config.getBoolean("discordchat.relay.achievements");
	}

	@Override
	public boolean sendJoinLeaveMessages() {
		return config.getBoolean("discordchat.relay.joinleave");
	}

	@Override
	public String getFromMCFormat() {
		return config.getString("discordchat.format.fromMC");
	}

	@Override
	public String getFromDiscordFormat() {
		return config.getString("discordchat.format.fromDiscord");
	}

	@Override
	public String getDeathFormat() {
		return config.getString("discordchat.format.death");
	}

	@Override
	public String getAchievementFormat() {
		return config.getString("discordchat.format.achievement");
	}

	@Override
	public String getJoinFormat() {
		return config.getString("discordchat.format.join");
	}

	@Override
	public String getLeaveFormat() {
		return config.getString("discordchat.format.leave");
	}

}
