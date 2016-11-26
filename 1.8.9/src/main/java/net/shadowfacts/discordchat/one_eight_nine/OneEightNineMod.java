package net.shadowfacts.discordchat.one_eight_nine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.core.DiscordChat;

import java.io.File;
import java.io.IOException;

/**
 * @author shadowfacts
 */
@Mod(modid = OneEightNineMod.MOD_ID, name = OneEightNineMod.NAME, version = OneEightNineMod.VERSION, acceptableRemoteVersions = "*")
public class OneEightNineMod {

	public static final String MOD_ID = "discordchat";
	public static final String NAME = "Discord Chat";
	public static final String VERSION = "@VERSION@";

	public static IMinecraftAdapter minecraftAdapter = new OneEightNineAdapter();
	public static IConfig config;
	public static IDiscordChat discordChat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		discordChat = new DiscordChat(minecraftAdapter);
		config = discordChat.getConfig();
		config.init(new File(event.getModConfigurationDirectory(), "shadowfacts/DiscordChat.conf"));
		discordChat.connect();

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		discordChat.start();
		discordChat.sendMessage("Server is online");
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		discordChat.sendMessage("Server is offline");
		discordChat.stop();
	}

}
