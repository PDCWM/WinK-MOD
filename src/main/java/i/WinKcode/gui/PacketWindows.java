package i.WinKcode.gui;

import i.WinKcode.utils.system.Connection;
import net.minecraft.block.Block;
import net.minecraft.network.play.server.SPacketMultiBlockChange;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class PacketWindows extends JFrame{
    JTextArea jta = new JTextArea();

    public PacketWindows()
    {
        this.setTitle("封包查看器 - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.setSize(500 , 600);
        this.setVisible(true);    //设置窗口是否可见

        JScrollPane jsp = new JScrollPane(jta);
        jsp.setBounds(13,10,350,340);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(jsp);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void appendText(String str){
        this.jta.append(str);
    }

    public void clearText(){
        this.jta.setText("");
    }

    public void onPacket(Object packet, Connection.Side side) {
        if(packet instanceof net.minecraft.network.play.server.SPacketEntity
                || packet instanceof net.minecraft.network.play.server.SPacketEntityHeadLook
                || packet instanceof net.minecraft.network.play.server.SPacketEntityVelocity
                || packet instanceof net.minecraft.network.play.server.SPacketEntityTeleport
        ) return;
        try {
            //FileOutputStream fo = new FileOutputStream(PacketLog, true);
            //ByteBuf buf = Unpooled.buffer();
            //PacketBuffer buffer = new PacketBuffer(buf);
            //Method repay1 = null;
            //for(Method r : packet.getClass().getMethods()){
            //    if(Arrays.equals(r.getParameterTypes(), new Class[]{PacketBuffer.class})){
            //        repay1 = r;
            //        break;
            //    }
            //}
            //assert repay1 != null;
            //repay1.invoke(packet,buffer);
            //String json = buf.toString(CharsetUtil.UTF_8);

            String json = "";

            if( packet instanceof  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock ){
                net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock cpakcet =
                        (net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock)packet;
                json = String.format("pos: %s %s %s , Direction: %s , Hand: %s , FacingX: %s , FacingY: %s , FacingZ: %s",
                        cpakcet.getPos().getX(),
                        cpakcet.getPos().getY(),
                        cpakcet.getPos().getZ(),
                        cpakcet.getDirection().toString(),
                        cpakcet.getHand().toString(),
                        cpakcet.getFacingX(),
                        cpakcet.getFacingY(),
                        cpakcet.getFacingZ());
            }

            if( packet instanceof  net.minecraft.network.play.client.CPacketPlayerDigging ){
                net.minecraft.network.play.client.CPacketPlayerDigging cpakcet =
                        (net.minecraft.network.play.client.CPacketPlayerDigging)packet;
                json = String.format("pos: %s %s %s , Facing: %s , Action: %s",
                        cpakcet.getPosition().getX(),
                        cpakcet.getPosition().getY(),
                        cpakcet.getPosition().getZ(),
                        cpakcet.getFacing().toString(),
                        cpakcet.getAction().toString()
                );
            }

            if( packet instanceof  net.minecraft.network.play.client.CPacketClickWindow ){
                net.minecraft.network.play.client.CPacketClickWindow cpakcet =
                        (net.minecraft.network.play.client.CPacketClickWindow)packet;
                json = String.format("windowId:%s slotId:%s packedClickData:%s ActionNumber:%s ClickType:%s\r\n",
                        cpakcet.getWindowId(),
                        cpakcet.getSlotId(),
                        cpakcet.getUsedButton(),
                        cpakcet.getActionNumber(),
                        cpakcet.getClickType().toString()
                );
            }

            if( packet instanceof  net.minecraft.network.play.server.SPacketOpenWindow ){
                net.minecraft.network.play.server.SPacketOpenWindow cpakcet =
                        (net.minecraft.network.play.server.SPacketOpenWindow)packet;
                json = String.format("windowId:%s GuiId:%s WindowTitle:%s slotCount:%s entityId:%s\r\n",
                        cpakcet.getWindowId(),
                        cpakcet.getGuiId(),
                        cpakcet.getWindowTitle().getFormattedText(),
                        cpakcet.getSlotCount(),
                        cpakcet.getEntityId()
                );
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketMultiBlockChange) {
                net.minecraft.network.play.server.SPacketMultiBlockChange cpakcet =
                        (net.minecraft.network.play.server.SPacketMultiBlockChange)packet;
                json = String.format("changedBlocks: %d.\r\n",
                        Arrays.stream(cpakcet.getChangedBlocks()).count()
                );
                for (SPacketMultiBlockChange.BlockUpdateData data : cpakcet.getChangedBlocks()) {
                    json = json + String.format("Pos: x:%d y:%d z:%d ,id:%d meta:%d name:%s\r\n",
                            data.getPos().getX(),
                            data.getPos().getY(),
                            data.getPos().getZ(),
                            Block.getIdFromBlock(data.getBlockState().getBlock()),
                            data.getBlockState().getBlock().getMetaFromState(data.getBlockState()),
                            data.getBlockState().getBlock().getLocalizedName()
                            );
                }
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketBlockChange) {
                net.minecraft.network.play.server.SPacketBlockChange data =
                        (net.minecraft.network.play.server.SPacketBlockChange)packet;
                json = json + String.format("Pos: x:%d y:%d z:%d ,id:%d meta:%d name:%s\r\n",
                        data.getBlockPosition().getX(),
                        data.getBlockPosition().getY(),
                        data.getBlockPosition().getZ(),
                        Block.getIdFromBlock(data.getBlockState().getBlock()),
                        data.getBlockState().getBlock().getMetaFromState(data.getBlockState()),
                        data.getBlockState().getBlock().getLocalizedName()
                );
            }

            if (packet instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItem) {
                net.minecraft.network.play.client.CPacketPlayerTryUseItem data =
                        (net.minecraft.network.play.client.CPacketPlayerTryUseItem)packet;
                json = json + String.format("Hand: %s\r\n",
                        data.getHand().toString()
                );
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketSpawnMob) {
                net.minecraft.network.play.server.SPacketSpawnMob data =
                        (net.minecraft.network.play.server.SPacketSpawnMob)packet;
                json = json + String.format("entityId:%d uniqueId:%s type:%d\r\n",
                        data.getEntityID(),
                        data.getUniqueId().toString(),
                        data.getEntityType()
                );
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketSpawnObject) {
                net.minecraft.network.play.server.SPacketSpawnObject data =
                        (net.minecraft.network.play.server.SPacketSpawnObject)packet;
                json = json + String.format("entityId:%d uniqueId:%s type:%d data:%s\r\n",
                        data.getEntityID(),
                        data.getUniqueId().toString(),
                        data.getType(),
                        data.getData()
                );
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketSoundEffect) {
                net.minecraft.network.play.server.SPacketSoundEffect data =
                        (net.minecraft.network.play.server.SPacketSoundEffect)packet;
                json = json + String.format("sound:%s category:%s \r\n",
                        data.getSound().getSoundName().toString(),
                        data.getCategory().getName()
                );
            }

            if (packet instanceof net.minecraft.network.play.server.SPacketParticles) {
                net.minecraft.network.play.server.SPacketParticles data =
                        (net.minecraft.network.play.server.SPacketParticles)packet;
                json = json + String.format("particleType:%s \r\n",
                        data.getParticleType().getParticleName()
                );
            }

            //if (!json.equals("")){
                String value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + ": " + side.toString() + "\r\n" + packet.getClass().toString() + "\r\n"
                        + json + "\r\n";
                appendText(value);
            //}
            //fo.write(value.getBytes(StandardCharsets.UTF_8));
            //fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
