package me.danichee.MCBR;

import java.util.HashMap;
import java.util.UUID;


public class Map {

	public static HashMap<UUID, PlayerElements> player_kit_map;
	
	public Map()
	{
		player_kit_map = new HashMap<UUID, PlayerElements>();
	}
	
	
	public HashMap<UUID, PlayerElements> getPlayerKitMap()
	{
		return player_kit_map;
		
	}
	
	public void put(UUID n, PlayerElements e)
	{
		player_kit_map.put(n, e);
		//System.out.println(n + " KEY, VALUE: " + player_kit_map.get(n));
	}
}
