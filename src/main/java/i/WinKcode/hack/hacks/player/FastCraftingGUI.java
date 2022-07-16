package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.hacks.another.HackMode;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class FastCraftingGUI extends Hack {
    public FastCraftingGUI() {
        super("FastCraftingGUI", HackCategory.PLAYER);
        this.GUIName = "随身工作台";
        this.setKey(Keyboard.KEY_ADD);
        this.setShow(false);
    }

    @Override
    public String getDescription() {
        return "无需工作台方块即可打开制作工作台.";
    }

    public GuiCrafting guiCrafting;
    @Override
    public void onEnable() {
        if(HackMode.enabled) return;
        //guiContainer = new ContainerWorkbench(Wrapper.INSTANCE.inventory(), Wrapper.INSTANCE.world(), BlockPos.ORIGIN);
        //Wrapper.INSTANCE.mc().displayGuiScreen(new GuiConsole());
        Wrapper.INSTANCE.player().openContainer.windowId += 1;

       // EntityPlayerMP entityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().getPlayerByUUID(Wrapper.INSTANCE.player().getUniqueID());
        //entityPlayerMP.getNextWindowId();
        //EntityPlayerSP entityPlayerMP = Wrapper.INSTANCE.player();
        //entityPlayerMP.closeContainer();
        //Wrapper.INSTANCE.player().openContainer.windowId = entityPlayerMP.currentWindowId;

        guiCrafting = new GuiCrafting(Wrapper.INSTANCE.inventory(), Wrapper.INSTANCE.world());
        //entityPlayerMP.openContainer = guiCrafting.inventorySlots;
        //entityPlayerMP.openContainer.windowId = entityPlayerMP.currentWindowId;
        //Wrapper.INSTANCE.player().openContainer.windowId = entityPlayerMP.currentWindowId;
        //entityPlayerMP.openContainer.addListener(entityPlayerMP);
        Wrapper.INSTANCE.mc().displayGuiScreen(guiCrafting);
        //MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(entityPlayerMP, entityPlayerMP.openContainer));

        //Wrapper.INSTANCE.player().openGui(Main.instance, Main.GUI_PORTABLE_CRAFT_BENCH_ID, Wrapper.INSTANCE.world(), 0, 0, 0);

        this.toggle();
        super.onEnable();
    }

    @Override
    public void onGuiOpen(GuiOpenEvent event){
        if (event.isCanceled()){
            ChatUtils.warning("GuiOpenEvent. true");
        }else{
            ChatUtils.warning("GuiOpenEvent. false");
        }
    }

    @Override
    public void onGuiContainer(GuiContainerEvent event){
        //ChatUtils.warning("onGuiContainer.");
        //guiCrafting.inventorySlots.onCraftMatrixChanged(Wrapper.INSTANCE.inventory());
    }

    @Override
    public void onDisable(){
        //guiContainer.onContainerClosed(Wrapper.INSTANCE.player());

    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event){
        //onContainerClosed

    }

}
