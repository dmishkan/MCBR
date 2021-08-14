package me.danichee.MCBR;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.danichee.MCBR.Kits.Kits;
import me.danichee.MCBR.Kits.Cloner.Cloner;
import me.danichee.MCBR.Kits.Endermage.Endermage;
import me.danichee.MCBR.Kits.Flash.Flash;
import me.danichee.MCBR.Kits.None.None;
import me.danichee.MCBR.Kits.Shapeshifter.Shapeshifter;
import me.danichee.MCBR.Kits.Stomper.Stomper;
import me.danichee.MCBR.Kits.Thor.Thor;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor
{
	
	public String cmd1 = "kit";
	public String cmd2 = "kits";
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String [] args)
	{
		
		
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase(cmd1))
			{
				if (args[0].equalsIgnoreCase("THOR"))
					Thor.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("NONE"))
					None.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("ENDERMAGE"))
					Endermage.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("STOMPER"))
					Stomper.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("FLASH"))
					Flash.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("CLONER"))
					Cloner.setKitItems(player);
				
				else if (args[0].equalsIgnoreCase("SHAPESHIFTER"))
					Shapeshifter.setKitItems(player);
				
				else
					player.sendMessage(ChatColor.LIGHT_PURPLE + args[0] + ChatColor.RED + " is not a valid kit.");
				
				return true;
			}
			
			else if (cmd.getName().equalsIgnoreCase(cmd2))
			{
				player.sendMessage(ChatColor.GRAY + "Current Kits: ");
				for (String kit : Kits.kits)
				{
					player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + kit + " ");
				}
				
			}
		}
		else
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
		return true;
		
	}
	
	
}
