package me.danichee.MCBR.Kits.Endermage;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Utils 
{

	public static ArrayList<Player> getPlayersAroundLocation(int r, Player player, World w, double x, double y, double z)
	{
		ArrayList<Player> p = new ArrayList<>();
		Location loc = new Location(w, x, y, z);
		for (Entity ent : w.getEntities())
		{
			if (ent instanceof Player && !player.equals( (Player) ent) && ent.getLocation().distanceSquared(loc) <= r)
				p.add((Player)ent);
		}
		return p;
	}
	
}
