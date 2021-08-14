package me.danichee.MCBR;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import me.danichee.MCBR.Kits.Cloner.Cloner;
import me.danichee.MCBR.Kits.Endermage.Endermage;
import me.danichee.MCBR.Kits.Flash.Flash;
import me.danichee.MCBR.Kits.Shapeshifter.Shapeshifter;
import me.danichee.MCBR.Kits.Stomper.Stomper;
import me.danichee.MCBR.Kits.Thor.Thor;
import net.md_5.bungee.api.ChatColor;


public class Main extends JavaPlugin {

	private Commands commands = new Commands();

	public void onEnable() 
	{
		new Map();
		registerGlow();
		getCommand(commands.cmd1).setExecutor(commands);
		getCommand(commands.cmd2).setExecutor(commands);
		getServer().getPluginManager().registerEvents(new EventsClass(), this);
		//getServer().getPluginManager().registerEvents(new Endermage(this), this);
		getServer().getPluginManager().registerEvents(new Thor(this), this);
		getServer().getPluginManager().registerEvents(new Stomper(this), this);
		getServer().getPluginManager().registerEvents(new Flash(this), this);
		getServer().getPluginManager().registerEvents(new Cloner(this), this);
		getServer().getPluginManager().registerEvents(new Shapeshifter(this), this);
		getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "\n\nMCBR ENABLED\n");
	}

	public void onDisable() 
	{
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nMCBR DISABLED\n");
	}
	

	public void registerGlow() 
	{
		try {
			java.lang.reflect.Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			NamespacedKey key = new NamespacedKey(this, getDescription().getName());
			Glow glow = new Glow(key);
			Enchantment.registerEnchantment(glow);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPlayerKit(UUID player)
	{
		for (Entry<UUID, PlayerElements> p : Map.player_kit_map.entrySet())
		{
			if (player.equals(p.getKey()))
				return p.getValue().getKit();
		}
		return "none";
	}
	
	public long getPlayerCooldownTime(UUID player)
	{
		for (Entry<UUID, PlayerElements> p : Map.player_kit_map.entrySet())
		{
			if (player.equals(p.getKey()))
				return p.getValue().getCooldown();
		}
		return 0;
	}

}
