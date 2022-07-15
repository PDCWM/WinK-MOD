package i.WinKcode.command;

import i.WinKcode.managers.HackManager;
import i.WinKcode.managers.XRayManager;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import i.WinKcode.xray.XRayData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class XRay extends Command
{
	public XRay()
	{
		super("xray");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("add")) {
				if(args[1].equalsIgnoreCase("mouse") && Wrapper.INSTANCE.mc().objectMouseOver != null) {
					RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
				
					if(object.typeOfHit == RayTraceResult.Type.BLOCK) {
						
						BlockPos block = object.getBlockPos();
						IBlockState state = Wrapper.INSTANCE.world().getBlockState(block);
						int id = Block.getIdFromBlock(state.getBlock());
						int meta =  state.getBlock().getMetaFromState(state);
						
						XRayData data = new XRayData(id, meta, Utils.random(0, 254), Utils.random(0, 254), Utils.random(0, 254));
						XRayManager.addData(data);
					}
				} else {
					if(args[1].contains(":")) {
						String[] split = args[1].split(":");
						XRayManager.add(new XRayData(
								Integer.parseInt(split[0]),
								Integer.parseInt(split[1]),
								Integer.parseInt(args[2]),
								Integer.parseInt(args[3]),
								Integer.parseInt(args[4])));
						} else {
							XRayManager.add(new XRayData(
									Integer.parseInt(args[1]),
									0,
									Integer.parseInt(args[2]),
									Integer.parseInt(args[3]),
									Integer.parseInt(args[4])));
					}
				}
				
				
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				XRayManager.removeData(Integer.parseInt(args[1]));
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				XRayManager.clear();
			}
			else if(args[0].equalsIgnoreCase("mode")){
				i.WinKcode.hack.hacks.visual.XRay x = (i.WinKcode.hack.hacks.visual.XRay) HackManager.getHack("XRay");
				switch (args[1]){
					case "1":
						ChatUtils.warning("1");
						x.FakeMine.getMode("矿洞模式").setToggled(false);
						x.FakeMine.getMode("反假矿扫描").setToggled(false);
						x.FakeMine.getMode("简易").setToggled(true);
						break;
					case "2":
						ChatUtils.warning("2");
						x.FakeMine.getMode("反假矿扫描").setToggled(false);
						x.FakeMine.getMode("简易").setToggled(false);
						x.FakeMine.getMode("矿洞模式").setToggled(true);
						break;
					case "3":
						ChatUtils.warning("3");
						x.FakeMine.getMode("矿洞模式").setToggled(false);
						x.FakeMine.getMode("反假矿扫描").setToggled(true);
						x.FakeMine.getMode("简易").setToggled(false);
						break;
				}
			}else if(args[0].equalsIgnoreCase("list")){
				for(XRayData data : XRayManager.xrayList) {
					ChatUtils.message("当前xray列表: ");
					ChatUtils.message(String.format("\u00a77ID: \u00a73%s \u00a77NAME: \u00a73%s \u00a77RGB: \u00a7c%s\u00a77, \u00a7a%s\u00a77, \u00a79%s \u00a77.",
							data.getId(), Block.getBlockById(data.getId()).getLocalizedName(), data.getRed(), data.getGreen(), data.getBlue()));
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
		return "X射线方块透视管理器.";
	}

	@Override
	public String getSyntax()
	{
		return "xray add <id:meta> <red> <green> <blue> | add mouse | remove <id> | list | clear";
	}
}