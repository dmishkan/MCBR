package me.danichee.MCBR;

public class PlayerElements
{

	private String kit;
	private long cooldown;
	
	public PlayerElements(String kit, long cooldown)
	{
		this.kit = kit;
		this.cooldown = cooldown;
	}
	
	public String getKit()
	{
		return kit;
	}
	
	public long getCooldown()
	{
		return cooldown;
	}
	
}
