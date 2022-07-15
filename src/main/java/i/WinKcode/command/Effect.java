package i.WinKcode.command;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Effect extends Command
{
	public Effect()
	{
		super("effect");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("add")) {
				int id = Integer.parseInt(args[1]);
				int duration = Integer.parseInt(args[2]);
				int amplifier = Integer.parseInt(args[3]);
				if(Potion.getPotionById(id) == null) {
					ChatUtils.error("药水为空");
					return;
				}
				Utils.addEffect(id, duration, amplifier);
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				int id = Integer.parseInt(args[1]);
				if(Potion.getPotionById(id) == null) {
					ChatUtils.error("药水为空");
					return;
				}
				Utils.removeEffect(id);
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				Utils.clearEffects();
			}
			else
			if(args[0].equalsIgnoreCase("list")) {
				for (PotionEffect pe : Utils.getActiveEffects()){
					ChatUtils.message(String.format("ID:%d NAME:%s duration:%d amplifier:%d",
							Potion.getIdFromPotion(pe.getPotion()),
							pe.getEffectName(),
							pe.getDuration(),
							pe.getAmplifier()
					));
				}
			}
			if(args[0].equalsIgnoreCase("all")) {
				for (Potion pe : Potion.REGISTRY) {
					ChatUtils.message(String.format("ID:%d NAME:%s", Potion.getIdFromPotion(pe), pe.getName()));
				}
			}
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "附魔效果管理.";
	}

	@Override
	public String getSyntax()
	{
		return "effect <add/remove/clear/list/all> <id> <duration(时效)> <amplifier(倍数)>";
	}
}