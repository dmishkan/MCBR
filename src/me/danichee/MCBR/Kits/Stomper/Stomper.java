package me.danichee.MCBR.Kits.Stomper;



import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.danichee.MCBR.Main;
import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;
import net.md_5.bungee.api.ChatColor;

public class Stomper implements Listener
{
	
	private static Main main;
	
	public Stomper(Main main) 
	{
		Stomper.main = main;
	}
	
	public static void setKitItems(Player player)
	{
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("stomper", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "STOMPER");
	}
	
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event)
	{		
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (event.getEntity() instanceof Player)
		{	
			if (player_kit.equals("stomper"))
			{
				if (event.getCause() == DamageCause.FALL)
				{
					Player p = (Player) event.getEntity();
					for (Entity ent : p.getNearbyEntities(3, 3, 3))
					{
						
						if (ent instanceof Player)
						{
							if ( ((Player) ent).isSneaking() )
							{
								if (event.getDamage()/4 > 4)
									((Player) ent).damage(4);
								else
									((Player) ent).damage(event.getDamage()/4);
							}
							else
								((Player) ent).damage(event.getDamage());
						}
					}
					event.setCancelled(true);
					
					if (event.getDamage() / 8 > 4)
						p.damage(4);
					else
						p.damage(event.getDamage()/8);
				}
			}
		}
        
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (player_kit.equals("stomper"))
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		
	}
	
}
