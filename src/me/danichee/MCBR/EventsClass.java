package me.danichee.MCBR;



import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;

import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;



import net.md_5.bungee.api.ChatColor;


public class EventsClass implements Listener
{
	Plugin plugin = Main.getPlugin(Main.class);

	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Map.player_kit_map.put(event.getPlayer().getUniqueId(), new PlayerElements("none", 0));
		
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		 setFireworkDeath(event);
		 setKillFeed(event);
		
		    
	}
	
	@EventHandler
	public void onCompassClick(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

			if (event.getHand() == EquipmentSlot.HAND
					&& player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))  {
				Player player_near = getNearestPlayer(player);
				if (player_near != null) 
				{
				    player.setCompassTarget(player_near.getLocation());
				    player.sendMessage(ChatColor.YELLOW + "Compass pointing at " + player_near.getName());
				} else {
					player.sendMessage(ChatColor.YELLOW + "No players found");
				}
		}
	}
	
	
	@EventHandler
    public void onPlayerDrinkSoup(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(player.getInventory().getItemInMainHand().getType() == Material.MUSHROOM_STEW) {
            	
                double health = player.getHealth();
                int foodlvl = player.getFoodLevel();
                if (health < 20 || foodlvl < 20) 
                {
                	if(health == 20 && foodlvl >= 13) {
                        player.setFoodLevel(20);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL,1));
                    } else if(health == 20 && foodlvl < 13) {
                        player.setFoodLevel(foodlvl + 7);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL,1));
                    } else {
                        if(health < 20 && health >= 13) {
                                player.setHealth(20);
                                player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL,1));
                            } else if(health < 20 && health < 13) {
                                player.setHealth(health + 7);
                                player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL,1));
                        }
                    }
                }
                
            }
        }
    }
	
	public void setKillFeed(PlayerDeathEvent event)
	{
		Player p = event.getEntity();
        if(p.getKiller() instanceof Player)
        	event.setDeathMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + ChatColor.AQUA + " has killed " + ChatColor.RED + p.getKiller().getName());
        else if (event.getEntity().getLastDamageCause().getCause() == DamageCause.FALL)
        	event.setDeathMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + ChatColor.AQUA + " has died from falling too far");
        else
        	event.setDeathMessage(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + ChatColor.AQUA + " has died to some miscellaneous shit I didn't account for in the code" );
	}
		
	public void setFireworkDeath(PlayerDeathEvent event)
	{
		Firework f = (Firework) event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
	    fm.addEffect(FireworkEffect.builder()
	            .flicker(true)
	            .trail(true)
	            .with(Type.BALL_LARGE)
	            .withColor(Color.RED)
	            .withFade(Color.RED)
	            .build());
	    fm.setPower(3);
	    f.setFireworkMeta(fm);
	}
	
	
	
	public Player getNearestPlayer(Player player) {
	    double dist_near = 0;
	    Player player_near = null;
	    for (Player player2 : player.getWorld().getPlayers()) 
	    {
	        if (player == player2) { continue; }
	     
	        Location location2 = player2.getLocation();
	        double dist = player.getLocation().distance(location2);
	        if (player_near == null || dist < dist_near) 
	        {
	        	player_near = player2;
	            dist_near = dist;
	        }
	    }
	    return player_near;
	}
	
}
