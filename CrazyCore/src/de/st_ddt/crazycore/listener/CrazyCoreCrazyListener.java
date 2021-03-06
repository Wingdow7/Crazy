package de.st_ddt.crazycore.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazycore.tasks.PlayerWipeTask;
import de.st_ddt.crazyplugin.events.CrazyPlayerPreRemoveEvent;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCoreCrazyListener implements Listener
{

	protected final CrazyCore plugin;

	public CrazyCoreCrazyListener(final CrazyCore plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void CrazyPlayerPreRemoveEvent(final CrazyPlayerPreRemoveEvent event)
	{
		if (plugin.getProtectedPlayers().contains(event.getPlayer().toLowerCase()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void CrazyPlayerRemoveOfflinePlayerDataEvent(final CrazyPlayerRemoveEvent event)
	{
		final OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer());
		if (player != null)
		{
			if (plugin.isWipingPlayerBansEnabled())
				player.setBanned(false);
			player.setOp(false);
			player.setWhitelisted(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	@Localized("CRAZYCORE.COMMAND.PLAYER.DELETE.KICK")
	public void CrazyPlayerRemovePlayerDataEvent(final CrazyPlayerRemoveEvent event)
	{
		final Player player = Bukkit.getPlayerExact(event.getPlayer());
		if (player != null)
			if (player.isOnline())
			{
				player.setBanned(false);
				player.setOp(false);
				player.setWhitelisted(false);
				player.leaveVehicle();
				player.getInventory().clear();
				player.setGameMode(Bukkit.getDefaultGameMode());
				player.setExp(0);
				player.setFoodLevel(20);
				player.setHealth(20);
				player.setFireTicks(0);
				player.resetPlayerTime();
				final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
				player.setCompassTarget(spawn);
				player.teleport(spawn);
				player.setBedSpawnLocation(spawn);
				player.saveData();
				player.kickPlayer(plugin.getLocale().getLocaleMessage(player, "COMMAND.PLAYER.DELETE.KICK"));
			}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CrazyPlayerRemoveLanguageEvent(final CrazyPlayerRemoveEvent event)
	{
		if (CrazyLocale.removeUserLanguage(event.getPlayer()))
			event.markDeletion((Named) plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void CrazyPlayerRemoveCommandEvent(final CrazyPlayerRemoveEvent event)
	{
		final String name = event.getPlayer();
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				for (final String command : plugin.getPlayerWipeCommands())
					Bukkit.dispatchCommand(console, ChatHelper.putArgs(command, name));
			}
		});
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void CrazyPlayerRemoveFileEvent(final CrazyPlayerRemoveEvent event)
	{
		if (plugin.isWipingPlayerFilesEnabled())
			new PlayerWipeTask(event.getPlayer()).execute();
		plugin.save();
	}
}
