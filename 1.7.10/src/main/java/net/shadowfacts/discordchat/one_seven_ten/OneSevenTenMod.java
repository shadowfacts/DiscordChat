package net.shadowfacts.discordchat.one_seven_ten;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.core.DiscordChat;

import java.io.File;
import java.io.IOException;

/**
 * @author shadowfacts
 */
@Mod(modid = OneSevenTenMod.MOD_ID, name = OneSevenTenMod.NAME, version = OneSevenTenMod.VERSION, acceptableRemoteVersions = "*")
public class OneSevenTenMod {

	public static final String MOD_ID = "discordchat";
	public static final String NAME = "Discord Chat";
	public static final String VERSION = "@VERSION@";

	public static IMinecraftAdapter minecraftAdapter = new OneSevenTenAdapter();
	public static IConfig config;
	public static IDiscordChat discordChat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		System.setProperty("org.apache.commons.logging.LogFactory", "net.shadowfacts.discordchat.repack.org.apache.commons.logging.impl.LogFactoryImpl");
		System.setProperty("org.apache.commons.logging.Log", "net.shadowfacts.discordchat.repack.org.apache.commons.logging.impl.SimpleLog");
		discordChat = new DiscordChat(new File(event.getModConfigurationDirectory(), "shadowfacts/DiscordChat/"), minecraftAdapter);
		config = discordChat.getConfig();
		discordChat.connect();

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLEventHandler());
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDC(discordChat));
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
