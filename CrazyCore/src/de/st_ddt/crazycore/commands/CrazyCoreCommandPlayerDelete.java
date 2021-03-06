package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCoreCommandPlayerDelete extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandPlayerDelete(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.PLAYER.DELETE.SUCCESS $Name$ $Amount$", "CRAZYCORE.COMMAND.PLAYER.DELETE.LISTHEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCORE.COMMAND.PLAYER.DELETE.LISTFORMAT $Index$ $Entry$ $ChatHeader$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		String name = ChatHelper.listingString(" ", args);
		final Player player = Bukkit.getPlayer(name);
		if (player != null)
			name = player.getName();
		final boolean self = sender.getName().equalsIgnoreCase(name);
		if (!PermissionModule.hasPermission(sender, "crazycore.player.delete." + (self ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		final CrazyPlayerRemoveEvent event = new CrazyPlayerRemoveEvent(plugin, name);
		if (self)
			event.callEvent();
		else
			event.checkAndCallEvent();
		plugin.sendLocaleMessage("COMMAND.PLAYER.DELETE.SUCCESS", sender, name, event.getDeletionsCount());
		if (event.getDeletionsCount() != 0)
			plugin.sendLocaleList(sender, "COMMAND.PLAYER.DELETE.LISTHEADER", "COMMAND.PLAYER.DELETE.LISTFORMAT", null, -1, 1, new ArrayList<String>(event.getDeletions()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>();
		final Pattern pattern = Pattern.compile(args[0], Pattern.CASE_INSENSITIVE);
		for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
			if (pattern.matcher(player.getName()).find())
				res.add(player.getName());
		return res;
	}

	@Override
	public boolean hasAccessPermission(CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.player.delete.self") || PermissionModule.hasPermission(sender, "crazycore.player.delete.other");
	}
}
