package me.danichee.MCBR.Kits.Thor;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.danichee.MCBR.Glow;
import me.danichee.MCBR.Main;
import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;
import net.md_5.bungee.api.ChatColor;

public class Thor implements Listener {

	private Main main;
	private  long cooldown;
	private static ItemStack hammer;

	public Thor(Main main) 
	{
		this.main = main;
		this.cooldown = 5;
		hammer = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta m = hammer.getItemMeta();
		m.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "MJÖLNIR");
		NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
		Glow glow = new Glow(key);
		m.addEnchant(glow, 1, true);
		hammer.setItemMeta(m);
	}

	public static void setKitItems(Player player) 
	{
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("thor", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(hammer);
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "THOR");
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());
		long player_cd = main.getPlayerCooldownTime(player.getUniqueId());

		if (player_kit.equals("thor")) {
			if (event.getHand() == EquipmentSlot.HAND
					&& player.getInventory().getItemInMainHand().equals(hammer)
					&& event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				long secondsleft = (player_cd/1000 + cooldown) - (System.currentTimeMillis()/1000);
				if (secondsleft > 0)
					player.sendMessage(ChatColor.RED + "Can't use for " + secondsleft + " more second(s)");
				else
				{
					World world = player.getWorld();
					Block targetblock = player.getTargetBlock(null, 256);
					Location location = targetblock.getLocation();

					location.getBlock().setType(Material.FIRE);
					world.strikeLightning(location);
					world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f);
					world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
					player.sendMessage(ChatColor.GREEN + "LIGHTNING");
					Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("thor", System.currentTimeMillis()));
				}
					
				
			}
		}

	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (event.getEntity() instanceof Player) {
			if (player_kit.equals("thor")) {
				event.getEntity().setFireTicks(0);
				event.setCancelled(event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FIRE
						|| event.getCause() == DamageCause.LIGHTNING);
			}
		}

	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		Item drop = e.getItemDrop();
		String player_kit = main.getPlayerKit(p.getUniqueId());

		if (player_kit.equals("thor")) {
			if (drop.getItemStack().equals(hammer)) {
				p.sendMessage(ChatColor.RED + "Cannot drop kit item");
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {

		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (player_kit.equals("thor")) {
			List<ItemStack> drops = event.getDrops();

			event.setKeepLevel(true);

			ListIterator<ItemStack> litr = drops.listIterator();

			while (litr.hasNext()) {

				ItemStack stack = litr.next();

				if (stack.equals(hammer)) {
					litr.remove();
				}
			}
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		}

	}

}
