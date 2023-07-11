package i.WinKcode.command;

import i.WinKcode.hack.Hack;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.value.Value;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.value.types.ModeValue;

import java.util.ArrayList;

public class Mode extends Command
{
    public Mode() {
        super("mode");
    }

    public void printMode(int id,Value v){
        if(v instanceof BooleanValue){
            ChatUtils.message(String.format("  %d %s : %b 默认值[%b]",id,v.getName(),v.getValue(),v.getDefaultValue()));
        }
        if(v instanceof DoubleValue){
            ChatUtils.message(String.format("  %d %s : %f 默认值[%f]",id,v.getName(),v.getValue(),v.getDefaultValue()));
        }
        if(v instanceof IntegerValue){
            ChatUtils.message(String.format("  %d %s : %d 默认值[%d]",id,v.getName(),v.getValue(),v.getDefaultValue()));
        }
        if(v instanceof ModeValue){
            ChatUtils.message("  " + id + " [" + ((ModeValue) v).getModeName() + "]");
            int ids = 1;
            for (i.WinKcode.value.Mode m : ((ModeValue) v).getModes()) {
                ChatUtils.message(String.format("    %d %s : %b",ids,m.getName(),m.isToggled()));
                ids++;
            }
        }
    }

    @Override
    public void runCommand(String s, String[] args)
    {
        try
        {
            Hack hack = HackManager.getHack(args[0]);
            if(hack == null){
                ChatUtils.warning("未找到该黑客！");
                return;
            }
            ChatUtils.message(String.format("%s (%s) \u00a79| \u00a7f%s \u00a79| \u00a7f%s \u00a79| \u00a7f%s", hack.getName(), hack.GUIName, hack.getCategory(), hack.getKey(), hack.isToggled()));
            ArrayList<Value> list = hack.getValues();
            if(args.length == 1) {
                int id = 1;
                for(Value v : list){
                    printMode(id,v);
                    id++;
                }
            }
            if(args.length == 2) {
                printMode(Integer.parseInt(args[1]),list.get(Integer.parseInt(args[1])));
            }
            if(args.length >= 3){
                Value v = list.get(Integer.parseInt(args[1]) - 1);
                if(v instanceof BooleanValue){
                    v.setValue(Boolean.valueOf(args[2]));
                }
                if(v instanceof DoubleValue){
                    v.setValue(Double.parseDouble(args[2]));
                }
                if(v instanceof IntegerValue){
                    v.setValue(Integer.parseInt(args[2]));
                }
                if(v instanceof ModeValue){
                    int ids = 1;
                    for (i.WinKcode.value.Mode m : ((ModeValue) v).getModes()) {
                        m.setToggled(ids++ == Integer.parseInt(args[2]));
                    }
                }
                printMode(Integer.parseInt(args[1]),v);
            }
        }catch (Exception e){
            ChatUtils.error("Usage: " + getSyntax());
        }
    }

    @Override
    public String getDescription()
    {
        return "查看/更改 黑客功能的参数.";
    }

    @Override
    public String getSyntax()
    {
        return "mode <HackName> | <id> | <id> [value]";
    }
}
