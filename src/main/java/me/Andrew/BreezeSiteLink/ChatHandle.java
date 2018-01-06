package me.Andrew.BreezeSiteLink;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ChatHandle
{
	Main main;

	public ChatHandle()
	{
		this.main = Main.getInstance();
	}

	public String getMessage(message MsgType)
	{
		String prefix = this.main.getConfig().getString("Messages.prefix");
		return cc(prefix + fromConfig(MsgType));
	}

	public String fromConfig(message MsgType)
	{
		return this.main.getConfig().getString("Messages." + MsgType.toString());
	}

	public String cc(String str)
	{
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
