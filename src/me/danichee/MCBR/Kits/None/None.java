package me.danichee.MCBR.Kits.None;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;
import net.md_5.bungee.api.ChatColor;

public class None
{

	public None() {}
	
	public static void setKitItems(Player player)
	{
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("none", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.GRAY + "NONE");
	}
	
	public static void setKitAbilities()
	{
		
	}
}
