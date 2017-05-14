package net.shadowfacts.discordchat.one_eight_nine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		String message = OneEightNineMod.discordChat.filterMCMessage(event.message);
		if (message != null) {
			OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().fromMC(event.player.getName(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneEightNineMod.config.sendDeathMessages() && event.entityLiving instanceof EntityPlayer && !(event.entityLiving instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerReceiveAchievement(AchievementEvent event) {
		if (OneEightNineMod.config.sendAchievementMessages() && event.entityPlayer instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) event.entityPlayer).getStatFile().hasAchievementUnlocked(event.achievement)) {
				return;
			}
			if (!((EntityPlayerMP) event.entityPlayer).getStatFile().canUnlockAchievement(event.achievement)) {
				return;
			}
			IChatComponent achievementComponent = event.achievement.getStatName();
			IChatComponent achievementText = new ChatComponentText("[").appendSibling(achievementComponent).appendText("]");
			OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().achievement(event.entityPlayer.getName(), achievementText.getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneEightNineMod.config.sendJoinLeaveMessages()) {
			OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneEightNineMod.config.sendJoinLeaveMessages()) {
			OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
