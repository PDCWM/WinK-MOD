package i.WinKcode.hack.hacks.visual;

import java.util.ArrayDeque;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ChestESP extends Hack {

	private int maxChests = 1000;
	public boolean shouldInform = true;
	private TileEntityChest openChest;
	public ArrayDeque<BlockPos> ChestsPos = new ArrayDeque<BlockPos>();
	private ArrayDeque<TileEntityChest> emptyChests = new ArrayDeque<TileEntityChest>();
	private ArrayDeque<TileEntityChest> nonEmptyChests = new ArrayDeque<TileEntityChest>();
	private String[] chestClasses = new String[] {
			"TileEntityIronChest",
			"TileEntityGoldChest",
			"TileEntityDiamondChest",
			"TileEntityCopperChest",
			"TileEntitySilverChest",
			"TileEntityCrystalChest",
			"TileEntityObsidianChest",
			"TileEntityDirtChest"
			};
	private boolean shouldRenderIronChest = true;
	
	public ChestESP() {
		super("ChestESP", HackCategory.VISUAL);
		this.GUIName = "箱子透视";
	}
	
	@Override
    public String getDescription() {
        return "让你看到你周围的所有宝箱.";
    }
	
	@Override
	public void onEnable() {
		shouldInform = true;
		emptyChests.clear();
		nonEmptyChests.clear();
		super.onEnable();
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		int chests = 0;
		ChestsPos.clear();
		for(int i = 0; i < Wrapper.INSTANCE.world().loadedTileEntityList.size(); i++)
		{
			TileEntity tileEntity = Wrapper.INSTANCE.world().loadedTileEntityList.get(i);
			if(chests >= maxChests) {
				break;
			}
			
			if(tileEntity instanceof TileEntityChest){
				chests++;
				ChestsPos.add(tileEntity.getPos());
				TileEntityChest chest = (TileEntityChest)tileEntity;
				boolean trapped = chest.getChestType() == BlockChest.Type.TRAP;
				
				if(emptyChests.contains(tileEntity)) {
					RenderUtils.drawBlockESP(chest.getPos(), 0.25F, 0.25F, 0.25F);
				} else if(nonEmptyChests.contains(tileEntity)) {
					
					if(trapped) {
						RenderUtils.drawBlockESP(chest.getPos(), 0, 1, 0);
					} else {
						RenderUtils.drawBlockESP(chest.getPos(), 1, 0F, 0);
					}
					
				} else if(trapped) {
					RenderUtils.drawBlockESP(chest.getPos(), 0, 1F, 0);
				} else {
					RenderUtils.drawBlockESP(chest.getPos(), 1, 0f, 0);
				}
				
				if(trapped) {
					RenderUtils.drawBlockESP(chest.getPos(), 0, 1F, 0);
				} else {
					RenderUtils.drawBlockESP(chest.getPos(), 1, 0F, 0);
				}
				
			} else if(tileEntity instanceof TileEntityEnderChest) {
				chests++;
				ChestsPos.add(tileEntity.getPos());
				RenderUtils.drawBlockESP(((TileEntityEnderChest)tileEntity).getPos(), 1, 0, 1);
			} else {
				if(shouldRenderIronChest) {
					try {
						for(String chestClass : chestClasses) {
							Class clazz = Class.forName("cpw.mods.ironchest.common.tileentity.chest." + chestClass);
							if(clazz != null && clazz.isInstance(tileEntity)) 
								RenderUtils.drawBlockESP(tileEntity.getPos(), 0.7F, 0.7F, 0.7F);
						}
					} catch (ClassNotFoundException e) { shouldRenderIronChest = false; }
				}
			}
		}
		
		for(int i = 0; i < Utils.getEntityList().size(); i++){
			Entity entity = Utils.getEntityList().get(i);
			if(chests >= maxChests) {
				break;
			}
			if(entity instanceof EntityMinecartChest){
				chests++;
				ChestsPos.add(entity.getPosition());
				RenderUtils.drawBlockESP(((EntityMinecartChest)entity).getPosition(), 1, 1, 1);
			}
		}
		
		if(chests >= maxChests && shouldInform){
			ChatUtils.warning("为了防止延迟，它只会显示前 " + maxChests + " 个箱子.");
			shouldInform = false;
		} else if(chests < maxChests) {
			shouldInform = true;
		}
		super.onRenderWorldLast(event);
	}
}
