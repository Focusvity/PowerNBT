package me.focusvity.powernbt.nms;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPacket
{

    public static void sendPacket(Player player, Packet<?> packet)
    {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        PlayerConnection connection = p.playerConnection;
        connection.sendPacket(packet);
    }
}
