package net.shadowfacts.discordchat.one_ten_two;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.exception.CommandException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class OneTenTwoAdapter implements IMinecraftAdapter {

	@Override
	public void sendMessage(String message) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			server.getPlayerList().sendChatMsg(ForgeHooks.newChatWithLinks(message));
		}
	}

	@Override
	public void sendMessageToPlayer(String message, String player) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		EntityPlayer mcPlayer = server.getPlayerList().getPlayerByUsername(player);
		mcPlayer.sendMessage(ForgeHooks.newChatWithLinks(message));
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
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().stream()
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

	@Override
	public String teleportPlayerToSpawn(String username) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		WorldServer world = server.worldServerForDimension(0);
		BlockPos spawn = world.getSpawnPoint();
		PlayerList playerList = server.getPlayerList();
		EntityPlayerMP player = playerList.getPlayerByUsername(username);
		if (player != null) {
			if (player.dimension != 0) {
				int original = player.dimension;
				playerList.transferPlayerToDimension(player, 0, new DummyTeleporter(world));

				if (original == 1 && player.isEntityAlive()) {
					world.spawnEntity(player);
					world.updateEntityWithOptionalForce(player, false);
				}
			}
			player.setPositionAndUpdate(spawn.getX() + 0.5d, spawn.getY(), spawn.getZ() + 0.5d);
		} else {
			GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(username);

			if (profile != null && profile.isComplete()) {
				FakePlayer fakePlayer = FakePlayerFactory.get(world, profile);
				IPlayerFileData saveHandler = world.getSaveHandler().getPlayerNBTManager();
				NBTTagCompound tag = saveHandler.readPlayerData(fakePlayer);

				if (tag == null) {
					return "Unknown player: " + username;
				}

				fakePlayer.dimension = 0;
				fakePlayer.posX = spawn.getX() + 0.5d;
				fakePlayer.posY = spawn.getY();
				fakePlayer.posZ = spawn.getZ() + 0.5d;

				saveHandler.writePlayerData(fakePlayer);
			} else {
				return "Unknown player: " + username;
			}
		}
		return null;
	}

	@Override
	public long getWorldTime(int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension).getWorldTime();
	}

}
