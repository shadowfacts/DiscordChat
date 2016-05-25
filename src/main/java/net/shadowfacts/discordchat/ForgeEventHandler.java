package net.shadowfacts.discordchat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.shadowfacts.discordchat.discord.DiscordThread;
import net.shadowfacts.discordchat.utils.MiscUtils;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void serverChat(ServerChatEvent event) {
		if (DCConfig.enabled && !MiscUtils.isMessageFromDiscord(event.getComponent().getUnformattedText())) {
			DiscordThread.instance.sendMessageToAllChannels(MiscUtils.toDiscordMessage(event.getUsername(), event.getMessage()));
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		if (DCConfig.enabled && DCConfig.sendPlayerDeathMessages) {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				DiscordThread.instance.sendMessageToAllChannels(MiscUtils.createDiscordDeathMessage((EntityPlayer)event.getEntityLiving()));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRecieveAchievement(AchievementEvent event) {
		if (DCConfig.enabled && DCConfig.sendPlayerAchievementMessages) {
			if (event.getEntityPlayer() instanceof EntityPlayerMP) {
				if (((EntityPlayerMP) event.getEntityPlayer()).getStatFile().hasAchievementUnlocked(event.getAchievement())) {
					return;
				}
				if (!((EntityPlayerMP) event.getEntityPlayer()).getStatFile().canUnlockAchievement(event.getAchievement())) {
					return;
				}
				DiscordThread.instance.sendMessageToAllChannels(MiscUtils.createAchievementMessage(event.getEntityPlayer(), event.getAchievement()));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (DCConfig.enabled && DCConfig.sendPlayerJoinLeaveMessages) {
			DiscordThread.instance.sendMessageToAllChannels(MiscUtils.createLoggedInMessage(event.player));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (DCConfig.enabled && DCConfig.sendPlayerJoinLeaveMessages) {
			DiscordThread.instance.sendMessageToAllChannels(MiscUtils.createLoggedOutMessage(event.player));
		}
	}

}
