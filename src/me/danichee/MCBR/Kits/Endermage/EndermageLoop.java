package me.danichee.MCBR.Kits.Endermage;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.danichee.MCBR.Main;
import net.md_5.bungee.api.ChatColor;

public class EndermageLoop extends BukkitRunnable
{

	private Player p;
	private Block b;
	private Material oldblock;
	private int ticks;
	private ArrayList<Player> waiting;
	private ItemStack portal;
	private Boolean flag;
	private Main main;
	
	public EndermageLoop(Player p, Block b, Material oldblock, ItemStack portal, Main main, ArrayList<Player> waiting)
	{
		this.p = p;
		this.b = b;
		this.oldblock = oldblock;
		this.waiting = waiting;
		this.portal = portal;
		this.main = main;
		this.flag = false;
		ticks = 3;
		waiting.add(p);
	}
	
	@Override
	public void run()
	{
		
		Location loc = b.getLocation();
		for (int i = 0; i <= 256; i++)
		{
			if (Utils.getPlayersAroundLocation(3, p, loc.getWorld(), loc.getX(), i, loc.getZ()).size() >= 1)
			{
				for (Player p : Utils.getPlayersAroundLocation(3, p, loc.getWorld(), loc.getX(), i, loc.getZ()))
				{
					if (!main.getPlayerKit(p.getUniqueId()).equals("endermage")) //set so that you can't teleport other endermages
					{
						p.teleport(new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1, loc.getBlockZ()));
						p.setNoDamageTicks(100);
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You've been teleported by an Endermage. You have 5 seconds of invinbility.");
						flag = true;
					}
				}
				if (flag == true) //if players other than endermage are under portal, we are allowed to tp ourselves now
				{
					p.teleport(new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1, loc.getBlockZ()));
					p.setNoDamageTicks(100);
					end();
					return;
				}
				
			}
		}
		
		
	
		if (ticks <= 0)
		{
			end();
		}
		ticks--;
	}
	
	private void end()
	{
		p.sendMessage(ChatColor.DARK_PURPLE + "DONE");
		p.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
		cancel();
		p.getInventory().addItem(portal);
		b.getLocation().getBlock().setType(oldblock);
		waiting.remove(p);
	}
}
