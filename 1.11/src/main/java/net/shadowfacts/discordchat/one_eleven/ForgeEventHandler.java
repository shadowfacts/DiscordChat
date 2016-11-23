package net.shadowfacts.discordchat.one_eleven;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
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
		OneElevenMod.discordChat.sendMessage(OneElevenMod.discordChat.getFormatter().fromMC(event.getPlayer().getName(), event.getMessage()));
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneElevenMod.config.sendDeathMessages() && event.getEntityLiving() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			OneElevenMod.discordChat.sendMessage(OneElevenMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerReceiveAchievement(AchievementEvent event) {
		if (OneElevenMod.config.sendAchievementMessages() && event.getEntityPlayer() instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) event.getEntityPlayer()).getStatFile().hasAchievementUnlocked(event.getAchievement())) {
				return;
			}
			if (!((EntityPlayerMP) event.getEntityPlayer()).getStatFile().canUnlockAchievement(event.getAchievement())) {
				return;
			}
			ITextComponent achievementComponent = event.getAchievement().getStatName();
			ITextComponent achievementText = new TextComponentString("[").appendSibling(achievementComponent).appendText("]");
			OneElevenMod.discordChat.sendMessage(OneElevenMod.discordChat.getFormatter().achievement(event.getEntityPlayer().getName(), achievementText.getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneElevenMod.config.sendJoinLeaveMessages()) {
			OneElevenMod.discordChat.sendMessage(OneElevenMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneElevenMod.config.sendJoinLeaveMessages()) {
			OneElevenMod.discordChat.sendMessage(OneElevenMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
