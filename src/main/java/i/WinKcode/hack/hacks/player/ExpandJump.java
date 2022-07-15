package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class ExpandJump extends Hack {
    public IntegerValue Distance;
    public BooleanValue stepAir, step;

    public ExpandJump() {
        super("ExpandJump", HackCategory.PLAYER);
        this.GUIName = "跳跃增幅";

        //hDistance = new IntegerValue("高跳", 10, 0, 100);
        Distance = new IntegerValue("范围", 200, 100, 1000);

        stepAir = new BooleanValue("踏空", false);
        step = new BooleanValue("跟随视角", false);
        this.addValue(Distance, stepAir, step);
    }

    @Override
    public String getDescription() {
        return "使玩家跳跃能力增幅.";
    }

    @Override
    public void onKeyPressed(int key){
        //double hce = hDistance.getValue() * 0.1;
        //double wce = wDistance.getValue() * 0.01;
        if (key == Keyboard.KEY_SPACE){
            if(!stepAir.getValue() && !Wrapper.INSTANCE.player().onGround) {
                return;
            }

            if(!step.getValue()) {
                float f = Utils.getDirection();
                double wce = Distance.getValue() * 0.01;
                Wrapper.INSTANCE.player().motionX -= (double)(MathHelper.sin(f)) * wce;
                Wrapper.INSTANCE.player().motionZ += (double)(MathHelper.cos(f)) * wce;
                if(!stepAir.getValue() && !Wrapper.INSTANCE.player().onGround) {
                    return;
                }
                Wrapper.INSTANCE.player().motionY = 1F;
                return;
            }

            float yawAim = Wrapper.INSTANCE.player().rotationYaw;
            float pitchAim = Wrapper.INSTANCE.player().rotationPitch;

            float arrowMotionFactor = Distance.getValue();
            float yaw = (float)Math.toRadians(yawAim);
            float pitch = (float)Math.toRadians(pitchAim);
            float arrowMotionX =
                    -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
            float arrowMotionZ =
                    MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX
                    + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
            arrowMotionX /= arrowMotion;
            arrowMotionY /= arrowMotion;
            arrowMotionZ /= arrowMotion;

            Wrapper.INSTANCE.player().motionX += arrowMotionX * (Distance.getValue() * 0.01);
            Wrapper.INSTANCE.player().motionZ += arrowMotionZ * (Distance.getValue() * 0.01);
            if (arrowMotionY * (Distance.getValue() * 0.01) > -2){
                Wrapper.INSTANCE.player().motionY = arrowMotionY * (Distance.getValue() * 0.01);
            }else{
                Wrapper.INSTANCE.player().motionY = -2;
            }
        }
    }
}
