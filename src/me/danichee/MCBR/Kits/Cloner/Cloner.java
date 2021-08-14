package me.danichee.MCBR.Kits.Cloner;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
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

import com.mojang.authlib.GameProfile;

import me.danichee.MCBR.Glow;
import me.danichee.MCBR.Main;
import me.danichee.MCBR.Map;
import me.danichee.MCBR.PlayerElements;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.WorldServer;

import org.bukkit.Bukkit;

public class Cloner implements Listener {

	private Main main;
	private static ItemStack orb;

	public Cloner(Main main) 
	{
		this.main = main;
		orb = new ItemStack(Material.MAGMA_CREAM, 1);
		ItemMeta m = orb.getItemMeta();
		m.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "CLONE ORB");
		NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
		Glow glow = new Glow(key);
		m.addEnchant(glow, 1, true);
		orb.setItemMeta(m);
	}

	public static void setKitItems(Player player) 
	{
		Map.player_kit_map.put(player.getUniqueId(), new PlayerElements("cloner", 0));
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(orb);
		inv.addItem(new ItemStack(Material.COMPASS));
		player.sendMessage(ChatColor.GREEN + "You are now kit " + ChatColor.LIGHT_PURPLE + "CLONER");
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		String player_kit = main.getPlayerKit(player.getUniqueId());

		if (player_kit.equals("cloner")) {
			if (event.getHand() == EquipmentSlot.HAND
					&& player.getInventory().getItemInMainHand().equals(orb)
					&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))  {
				spawnFakePlayer(player, player.getName());
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		Item drop = e.getItemDrop();
		String player_kit = main.getPlayerKit(p.getUniqueId());

		if (player_kit.equals("cloner")) {
			if (drop.getItemStack().equals(orb)) 
			{
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

				if (stack.equals(orb)) {
					litr.remove();
				}
			}
			Map.player_kit_map.put(event.getEntity().getPlayer().getUniqueId(), new PlayerElements("none", 0));
		}

	}
	
	public void spawnFakePlayer(Player player, String displayname){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
       
        Player target = Bukkit.getServer().getPlayer(displayname);
        EntityPlayer npc;
        if (target != null) {
            npc = new EntityPlayer(server, world, new GameProfile(target.getUniqueId(), target.getName()), new PlayerInteractManager(world));
        } else {
            OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
            npc = new EntityPlayer(server, world, new GameProfile(op.getUniqueId(), displayname), new PlayerInteractManager(world));
        }
        Location loc = player.getLocation();
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        for(Player all : Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer)all).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        }
    }
}

