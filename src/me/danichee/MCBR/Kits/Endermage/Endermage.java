package me.danichee.MCBR.Kits.Endermage;

import java.util.ArrayList;
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
import org.bukkit.event.entity.PlayerDeathEvent;
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

public class Endermage implements Listener {

	private static ArrayList<Player> waiting;
	private static Main main;
	private static ItemStack portal;

	public Endermage(Main main) 
	{
		waiting = new ArrayList<>();
		Endermage.main = main;
		portal = new ItemStack(Material.END_PORTAL_FRAME, 1);
		ItemMeta m = portal.getItemMeta();
		m.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PORTAL");
		NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
		Glow glow = new Glow(key);
		m.addEnchant(glow, 1, true);
		portal.setItemMeta(m);
	}

	public static void setKitItems(Player player) {
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("endermage", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(portal);
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "ENDERMAGE");
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());

		if (player_kit.equals("endermage")) {
			if (event.getHand() == EquipmentSlot.HAND
					&& player.getInventory().getItemInMainHand().equals(portal)
					&& event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (waiting.contains(player))
					return;

				World world = player.getWorld();
				Block targetblock = player.getTargetBlock(null, 256);
				Location location = targetblock.getLocation();
				Material oldblock = targetblock.getType();

				location.getBlock().setType(Material.END_PORTAL_FRAME);
				player.getInventory().getItemInMainHand()
						.setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

				world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
				player.sendMessage(ChatColor.DARK_PURPLE + "PORTAL DOWN");

				new EndermageLoop(player, targetblock, oldblock, portal, main, waiting).runTaskTimer(main, 0, 20);

			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		Item drop = e.getItemDrop();
		String player_kit = main.getPlayerKit(p.getUniqueId());

		if (player_kit.equals("endermage")) {
			if (drop.getItemStack().equals(portal)) 
			{
				p.sendMessage(ChatColor.RED + "Cannot drop kit item");
				e.setCancelled(true);
			}
		}

	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (player_kit.equals("endermage")) {
			List<ItemStack> drops = event.getDrops();

			event.setKeepLevel(true);

			ListIterator<ItemStack> litr = drops.listIterator();

			while (litr.hasNext()) {

				ItemStack stack = litr.next();

				if (stack.equals(portal)) {
					litr.remove();
				}
			}
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		}

		
	}
}
