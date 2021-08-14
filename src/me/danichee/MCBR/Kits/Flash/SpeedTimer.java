package me.danichee.MCBR.Kits.Flash;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class SpeedTimer extends BukkitRunnable
{

	private Player p;
	private int ticks;
	
	public SpeedTimer(Player p)
	{
		this.p = p;
		ticks = 4;
	}
	
	@Override
	public void run()
	{
		if (ticks <= 0)
		{
			end();
		}
		
		ticks--;
	}
	
	private void end()
	{
		p.sendMessage(ChatColor.RED + "ur not fast anymore haha loser");
		p.setWalkSpeed((float) 0.2);
		cancel();
	}
	
	
	
}

