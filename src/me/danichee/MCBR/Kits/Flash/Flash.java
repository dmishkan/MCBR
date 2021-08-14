package me.danichee.MCBR.Kits.Flash;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.danichee.MCBR.Glow;
import me.danichee.MCBR.Main;
import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;
import net.md_5.bungee.api.ChatColor;

public class Flash implements Listener 
{

	private static Main main;
	private static ItemStack rod;
	private long cooldown;
	
	public Flash(Main main)
	{
		Flash.main = main;
		this.cooldown = 60;
		rod = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta m = rod.getItemMeta();
		m.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "SPEED FORCE");
		NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
		Glow glow = new Glow(key);
		m.addEnchant(glow, 1, true);
		rod.setItemMeta(m);
	}
	
	
	public static void setKitItems(Player player) 
	{
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("flash", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(rod);
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "FLASH");
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) 
	{
		
		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());
		long player_cd = main.getPlayerCooldownTime(player.getUniqueId());
		
		if (player_kit.equals("flash"))
		{
			if (event.getHand() == EquipmentSlot.HAND
					&& player.getInventory().getItemInMainHand().equals(rod)
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) 
			{
				long secondsleft = (player_cd/1000 + cooldown) - (System.currentTimeMillis()/1000);
				if (secondsleft > 0)
					player.sendMessage(ChatColor.RED + "Can't use for " + secondsleft + " more second(s)");
				else
				{
					player.setWalkSpeed(1);
					player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "YOU ARE NOW FAST AS FUCK BOI!");
					new SpeedTimer(player).runTaskTimer(main, 0, 20);
					Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("flash", System.currentTimeMillis()));
				}
			}
		}
		
	}
	
	
	@EventHandler
	public void onPlayerRun(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());
		if (player_kit.equals("flash") && player.getWalkSpeed() == 1)
		{
			Location loc = player.getLocation().clone().subtract(0, 1, 0);
			player.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
			Block b = loc.getBlock();
			b.getRelative(BlockFace.UP).setType(Material.FIRE);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) 
	{
		Player p = e.getPlayer();
		Item drop = e.getItemDrop();
		if (drop.getItemStack().equals(rod)) 
		{
			p.sendMessage(ChatColor.RED + "Cannot drop kit item");
			e.setCancelled(true);
		} 
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event)
	{		
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (event.getEntity() instanceof Player)
		{
			if (player_kit.equals("flash")) 
			{
				event.getEntity().setFireTicks(0);
				event.setCancelled(event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FIRE);
			}
		}
        
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {

		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (player_kit.equals("flash")) {
			List<ItemStack> drops = event.getDrops();

			event.setKeepLevel(true);

			ListIterator<ItemStack> litr = drops.listIterator();

			while (litr.hasNext()) {

				ItemStack stack = litr.next();

				if (stack.equals(rod)) {
					litr.remove();
				}
			}
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		}

	}
	
}
