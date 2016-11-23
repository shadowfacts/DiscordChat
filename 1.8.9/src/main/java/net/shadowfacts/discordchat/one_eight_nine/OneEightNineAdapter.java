package net.shadowfacts.discordchat.one_eight_nine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class OneEightNineAdapter implements IMinecraftAdapter {

	@Override
	public void sendMessage(String message) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
	}

	@Override
	public int[] getAllDimensions() {
		Integer[] boxed = DimensionManager.getIDs();
		int[] unboxed = new int[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}
		return unboxed;
	}

	@Override
	public double getTickTime(int dimension) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		long[] tickTimes = server.worldTickTimes.get(dimension);
		long sum = 0;
		for (int i = 0; i < tickTimes.length; i++) {
			sum += tickTimes[i];
		}
		return sum / tickTimes.length * 1.0E-6D;
	}

	@Override
	public Set<String> getOnlinePlayers() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerList().stream()
				.map(EntityPlayer::getName)
				.collect(Collectors.toSet());
	}

	@Override
	public void executeCommand(String command) {
		FMLCommonHandler.instance().getMinecraftServerInstance().callFromMainThread(() -> {
			FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(DummySender.INSTANCE, command);
			return null;
		});
	}

}
