package me.danichee.MCBR.Kits.Shapeshifter;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.danichee.MCBR.Glow;
import me.danichee.MCBR.Main;
import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;

import org.bukkit.Bukkit;

public class Shapeshifter implements Listener {

	private Main main;
	private static ItemStack book;
	private static ItemStack paper;

	public Shapeshifter(Main main) {
		this.main = main;
		book = new ItemStack(Material.BOOK, 1);
		paper = new ItemStack(Material.PAPER, 1);
		ItemMeta mb = book.getItemMeta();
		ItemMeta mp = paper.getItemMeta();
		mb.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PLAYER DISGUISES");
		mp.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "DISGUISE PAPER");
		NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
		Glow glow = new Glow(key);
		mb.addEnchant(glow, 1, true);
		mp.addEnchant(glow, 1, true);
		book.setItemMeta(mb);
		paper.setItemMeta(mp);
	}

	public static void setKitItems(Player player) {
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("shapeshifter", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(book);
		inv.addItem(paper);
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "SHAPESHIFTER");
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());

		if (player_kit.equals("shapeshifter")) {
			if (event.getHand() == EquipmentSlot.HAND && player.getInventory().getItemInMainHand().equals(book)
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				//openGUI(player);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
			String player_disguise_name = event.getCurrentItem().getItemMeta().getDisplayName();
			Player player = (Player) event.getWhoClicked();
			// Player disguised_player = getPlayerFromString(player_disguise_name,
			// player.getWorld());
			player.closeInventory();
			player.sendMessage(ChatColor.GREEN + "You are now disguised as " + player_disguise_name);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		Item drop = e.getItemDrop();
		String player_kit = main.getPlayerKit(p.getUniqueId());

		if (player_kit.equals("shapeshifter")) {
			if (drop.getItemStack().equals(book) || drop.getItemStack().equals(paper)) {
				p.sendMessage(ChatColor.RED + "Cannot drop kit item");
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String player_kit = main.getPlayerKit(event.getEntity().getUniqueId());
		if (player_kit.equals("orb")) {
			List<ItemStack> drops = event.getDrops();

			event.setKeepLevel(true);

			ListIterator<ItemStack> litr = drops.listIterator();

			while (litr.hasNext()) {

				ItemStack stack = litr.next();

				if (stack.equals(book) || stack.equals(paper)) {
					litr.remove();
				}
			}
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		}

	}

	public void openGUI(Player player) {
		Inventory inv = Bukkit.createInventory(null, player.getWorld().getPlayers().size(),
				ChatColor.GREEN + "Disguise Selector");
		for (Player p : player.getWorld().getPlayers()) {
			inv.addItem(getPlayerSkull(p));
		}
		player.openInventory(inv);

	}

	private ItemStack getPlayerSkull(Player p) {
		ItemStack playerskull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
		meta.setDisplayName(p.getName());
		playerskull.setItemMeta(meta);
		return playerskull;
	}

	@SuppressWarnings("unused")
	private Player getPlayerFromString(String player_name, World world) {
		for (Player p : world.getPlayers()) {
			if (p.getName().equals(player_name))
				return p;
		}
		return world.getPlayers().get(0);
	}

	

}
