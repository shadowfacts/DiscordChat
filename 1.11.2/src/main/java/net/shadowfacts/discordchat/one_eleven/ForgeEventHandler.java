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
		String message = OneElevenTwoMod.discordChat.filterMCMessage(event.getMessage());
		if (message != null) {
			OneElevenTwoMod.discordChat.sendMessage(OneElevenTwoMod.discordChat.getFormatter().fromMC(event.getPlayer().getName(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneElevenTwoMod.config.sendDeathMessages() && event.getEntityLiving() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			OneElevenTwoMod.discordChat.sendMessage(OneElevenTwoMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerReceiveAchievement(AchievementEvent event) {
		if (OneElevenTwoMod.config.sendAchievementMessages() && event.getEntityPlayer() instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) event.getEntityPlayer()).getStatFile().hasAchievementUnlocked(event.getAchievement())) {
				return;
			}
			if (!((EntityPlayerMP) event.getEntityPlayer()).getStatFile().canUnlockAchievement(event.getAchievement())) {
				return;
			}
			ITextComponent achievementComponent = event.getAchievement().getStatName();
			ITextComponent achievementText = new TextComponentString("[").appendSibling(achievementComponent).appendText("]");
			OneElevenTwoMod.discordChat.sendMessage(OneElevenTwoMod.discordChat.getFormatter().achievement(event.getEntityPlayer().getName(), achievementText.getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneElevenTwoMod.config.sendJoinLeaveMessages()) {
			OneElevenTwoMod.discordChat.sendMessage(OneElevenTwoMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneElevenTwoMod.config.sendJoinLeaveMessages()) {
			OneElevenTwoMod.discordChat.sendMessage(OneElevenTwoMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
