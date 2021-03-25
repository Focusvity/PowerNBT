package me.focusvity.powernbt.nms;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NMSItemStack
{

    public static NBTTagCompound getTag(ItemStack stack)
    {
        return CraftItemStack.asNMSCopy(stack).getTag();
    }

    public static void setTag(ItemStack stack, NBTTagCompound compound)
    {
        CraftItemStack.asNMSCopy(stack).setTag(compound);
    }

    public static NBTTagCompound readItemStack(ItemStack stack, NBTTagCompound compound)
    {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        return nmsStack.save(compound);
    }

    public static void writeNMSItemStack(NBTTagCompound compound)
    {
        net.minecraft.server.v1_16_R3.ItemStack.a(compound);
    }

    public static void setTagOrigin(ItemStack stack, NBTTagCompound compound)
    {
        if (compound == null)
        {
            stack.setItemMeta(null);
            return;
        }
        net.minecraft.server.v1_16_R3.ItemStack copy = CraftItemStack.asNMSCopy(stack);
        copy.setTag(compound);
        ItemMeta meta = CraftItemStack.asCraftMirror(copy).getItemMeta();
        stack.setItemMeta(meta);
    }

    public static NBTTagCompound getTagOrigin(ItemStack stack)
    {
        CraftItemStack copy = CraftItemStack.asCraftCopy(stack);
        ItemMeta meta = stack.getItemMeta();
        copy.setItemMeta(meta);
        return CraftItemStack.asNMSCopy(copy).getTag();
    }
}
