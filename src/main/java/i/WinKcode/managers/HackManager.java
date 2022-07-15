package i.WinKcode.managers;

import i.WinKcode.gui.click.ClickGuiScreen;
import i.WinKcode.gui.click.theme.dark.DarkTheme;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.hacks.TestHack;
import i.WinKcode.hack.hacks.another.*;
import i.WinKcode.hack.hacks.auto.*;
import i.WinKcode.hack.hacks.combat.*;
import i.WinKcode.hack.hacks.player.*;
import i.WinKcode.hack.hacks.visual.*;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.Value;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HackManager {
	
	private static Hack toggleHack = null;
	private static ArrayList<Hack> hacks;
	private GuiManager guiManager;
	private ClickGuiScreen guiScreen;

	public HackManager() {
		hacks = new ArrayList<Hack>();
		addHack(new AntiFall());
		addHack(new AntiHunger());
		addHack(new Targets());
		addHack(new Enemys());
		addHack(new Teams());
		addHack(new NoGuiEvents());
		addHack(new Glowing());
		addHack(new Trajectories());
		addHack(new XRay());
		addHack(new EntityESP());
		addHack(new ItemESP());
		addHack(new ChestESP());
		addHack(new Tracers());
		addHack(new WallHack());
		addHack(new Flight());
		addHack(new NightVision());
		addHack(new Profiler());
		addHack(new AntiBot());
		addHack(new AimBot());
		addHack(new BowAimBot());
		addHack(new Trigger());
		addHack(new Criticals());
		addHack(new KillAura());
		addHack(new Velocity());
		addHack(new AutoSprint());
		addHack(new AutoArmor());
		addHack(new AutoFish());
		addHack(new InteractClick());
		addHack(new ChestStealer());
		addHack(new Glide());
		addHack(new Nuker());
		addHack(new Ghost());
		addHack(new Blink());
		addHack(new Scaffold());
		addHack(new FastLadder());
		addHack(new Speed());
		addHack(new ExpandJump());
		addHack(new AutoStep());
		addHack(new AntiSneak());
		addHack(new FreeCam());
		addHack(new BlockOverlay());
		addHack(new PluginsGetter());
		addHack(new AttackPacketCW());
		addHack(new AttackPacketCIA());
		addHack(new Teleport());
		addHack(new FireballReturn());
		addHack(new SkinStealer());
		addHack(new RemoteChest());
		addHack(new GuiWalk());
		addHack(new PlayerRadar());
		addHack(new SkinChanger());
		addHack(new Parkour());
		addHack(new AntiRain());
		addHack(new AntiWeb());
		addHack(new Spider());
		addHack(new AutoEat());
		addHack(new AutoWalk());
		addHack(new AutoSwim());
		addHack(new AutoTotem());
		addHack(new AutoShield());
		addHack(new Rage());
		addHack(new HitBox());
		addHack(new AntiAfk());
		addHack(new TestHack());
		addHack(new FastBreak());
		addHack(new Disconnect());
		addHack(new GhostMode());
		addHack(new PortalGodMode());
		addHack(new PickupFilter());
		addHack(new PacketFilter());
		addHack(new FakeCreative());
		addHack(new ArmorHUD());
		addHack(new HUD());
		addHack(new FastGUI());
		addHack(new Console());
		addHack(new ClickGui());
		addHack(new FastCraftingGUI());
	}
	
	public void setGuiManager(GuiManager guiManager) {
		this.guiManager = guiManager;
	}
	
	public ClickGuiScreen getGui() {
        if (this.guiManager == null) {
            this.guiManager = new GuiManager();
            this.guiScreen = new ClickGuiScreen();
            ClickGuiScreen.clickGui = this.guiManager;
            this.guiManager.Init(180, 100);
    		this.guiManager.setTheme(new DarkTheme());
        }
        return this.guiManager;
    }
	
	public static Hack getHack(String name) {
		Hack hack = null;
		for(Hack h : getHacks()) {
        	if(h.getName().equalsIgnoreCase(name)) {
        		hack = h;
        	}
        }
		return hack;
	}
	
	public static List<Hack> getSortedHacks() {
	        final List<Hack> list = new ArrayList<Hack>();
	        for (final Hack hack : getHacks()) {
	            if (hack.isToggled()) {
	                if (!hack.isShow()) {
	                    continue;
	                }
	                list.add(hack);
	            }
	        }
	        list.sort(new Comparator<Hack>() {
	            @Override
	            public int compare(final Hack h1, final Hack h2) {
	                String s1 = h1.getName();
	                String s2 = h2.getName();
	                for(Value value : h1.getValues()) {
	    				if(value instanceof ModeValue) {
	    					ModeValue modeValue = (ModeValue)value;
	    					if(!modeValue.getModeName().equals("Priority")) {
	    						for(Mode mode : modeValue.getModes()) {
	    							if(mode.isToggled()) {
	    								s1 = s1 + " " + mode.getName();
	    							}
	    						}
	    					}
	    				}
	    			}
	                for(Value value : h2.getValues()) {
	    				if(value instanceof ModeValue) {
	    					ModeValue modeValue = (ModeValue)value;
	    					if(!modeValue.getModeName().equals("Priority")) {
	    						for(Mode mode : modeValue.getModes()) {
	    							if(mode.isToggled()) {
	    								s2 = s2 + " " + mode.getName();
	    							}
	    						}
	    					}
	    				}
	    			}
	                final int cmp = Wrapper.INSTANCE.fontRenderer().getStringWidth(s2) - Wrapper.INSTANCE.fontRenderer().getStringWidth(s1);
	                return (cmp != 0) ? cmp : s2.compareTo(s1);
	            }
	        });
	        return list;
	    }
	
	public static void addHack(Hack hack) {
		hacks.add(hack);
	}

	public static ArrayList<Hack> getHacks() {
		return hacks;
	}
	
	public static Hack getToggleHack() {
		return toggleHack;
	}
	
	public static void onKeyPressed(int key) {
		if (Wrapper.INSTANCE.mc().currentScreen != null) {
            return;
        }
		if (HackManager.getHack("XRay").isToggled()){
			XRay x = (XRay) HackManager.getHack("XRay");
			if(x.FakeMine.getMode("反假矿扫描").isToggled()){
				if(key == Keyboard.KEY_Z){
					if(!x.isXR) {
						x.onEnable();
					}else {
						ChatUtils.warning("还没扫完呐.");
					}
				}
			}/*
			if(x.FakeMine.getMode("智能模式").isToggled()){
				if(key == Keyboard.KEY_X) {
					x.lastPx = (int)Wrapper.INSTANCE.player().posX;
					x.lastPz = (int)Wrapper.INSTANCE.player().posY;
					x.XrayAiStart = !x.XrayAiStart;
				}else if(key == Keyboard.KEY_Z){
					x.ClearBlock();
				}
			}*/
		}
		for(Hack hack : getHacks()) {
			if(hack.isToggled()) {
				hack.onKeyPressed(key);
			}
		}

		for(Hack hack : getHacks()) {
    		if(hack.getKey() == key) {
    			hack.toggle();
    			toggleHack = hack;
    		}
    	}
	}
	
	public static void onGuiContainer(GuiContainerEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onGuiContainer(event);
    		}
    	}
	}
	
	public static void onGuiOpen(GuiOpenEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onGuiOpen(event);
    		}
    	}
	}
	
	public static void onMouse(MouseEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onMouse(event);
    		}
    	}
	}
	
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onLeftClickBlock(event);
    		}
    	}
	}
	
	public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onCameraSetup(event);
    		}
    	}
	}
	
	public static void onAttackEntity(AttackEntityEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onAttackEntity(event);
    		}
    	}
	}
	
	public static void onProjectileImpact(ProjectileImpactEvent event) {
		for(Hack hack : getHacks()) {
	    	if(hack.isToggled()) {
	    		hack.onProjectileImpact(event);
	    	}
	    }
	}
	
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onEntityJoinWorldEvent(event);
    		}
    	}
	}
	
    public static void onItemPickup(EntityItemPickupEvent event) {
		for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onItemPickup(event);
    		}
    	}
	}
	
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onPlayerTick(event);
    		}
    	}
    }
	
    public static void onClientTick(TickEvent.ClientTickEvent event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onClientTick(event);
    		}
    	}
    }
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onLivingUpdate(event);
    		}
    	}
    }
    
    public static void onRenderPlayer(RenderPlayerEvent event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onRenderPlayer(event);
    		}
    	}
    }
    
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onRenderWorldLast(event);
    		}
    	}
    }
    
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
    	for(Hack hack : getHacks()) {
    		if(hack.isToggled()) {
    			hack.onRenderGameOverlay(event);
    		}
    	}
    }
}
