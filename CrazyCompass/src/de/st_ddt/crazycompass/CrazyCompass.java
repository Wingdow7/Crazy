package de.st_ddt.crazycompass;

import org.bukkit.plugin.PluginManager;
import de.st_ddt.crazyplugin.CrazyPlugin;

public class CrazyCompass extends CrazyPlugin
{

	@Override
	public void onEnable()
	{
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		CrazyCompassPlayerListener playerListener = new CrazyCompassPlayerListener();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	protected boolean isSupportingLanguages()
	{
		return false;
	}
}
