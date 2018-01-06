package me.Andrew.BreezeSiteLink;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
		extends JavaPlugin
{
	private static Main ourinstance = null;
	String API_KEY;
	String API_LINK;

	public static Main getInstance()
	{
		return ourinstance;
	}

	public void onEnable()
	{
		ourinstance = this;
		saveDefaultConfig();
		if ((!getConfig().contains("API.KEY")) || (!getConfig().contains("API.LINK")))
		{
			getLogger().severe("Plugin Not Configured!");
			getServer().getPluginManager().disablePlugin(this);
		}
		this.API_KEY = getConfig().getString("API.KEY");
		this.API_LINK = getConfig().getString("API.LINK");
		if ((this.API_KEY.trim() == "") || (this.API_LINK.trim() == ""))
		{
			getLogger().severe("Plugin Not Configured!");
			getServer().getPluginManager().disablePlugin(this);
		}
		if (!new SiteAPI().canConnect())
		{
			getLogger().severe("API Can Not Connect!");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (label.equalsIgnoreCase("register"))
		{
			if (!(sender instanceof Player)) {
				return true;
			}
			ChatHandle ch = new ChatHandle();
			Player p = (Player)sender;
			SiteAPI api = new SiteAPI();
			if (api.userExists(p.getName()))
			{
				p.sendMessage(ch.getMessage(message.userAlreadyMade));
				return true;
			}
			if (args.length == 2)
			{
				String email = args[0];
				String pass = args[1];
				if (pass.length() < 7)
				{
					p.sendMessage(ch.getMessage(message.passwordTooSmall));
					return true;
				}
				if (!isEmailAddress(email))
				{
					p.sendMessage(ch.getMessage(message.emailNotValid));
					return true;
				}
				if (api.registerUser(p, email, pass)) {
					p.sendMessage(ch.getMessage(message.userMade).replace("{PASS}", pass));
					for(String cmd : getConfig().getStringList("commands")){
						cmd = cmd.replace("{username}",p.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(),cmd);
					}
				} else {
					p.sendMessage(ch.getMessage(message.error));
				}
			}
			else
			{
				p.sendMessage(ch.getMessage(message.usage));
			}
		}
		return true;
	}

	public static boolean isEmailAddress(String email)
	{
		return (email.contains(".")) && (email.contains("@"));
	}
}
