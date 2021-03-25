package me.focusvity.powernbt.container;

import me.focusvity.powernbt.nms.NMSItemStack;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ContainerItemStack extends Container<ItemStack>
{

    private ItemStack stack;

    public ContainerItemStack(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public ItemStack getObject()
    {
        return stack;
    }

    @Override
    protected NBTBase readTag()
    {
        NBTTagCompound compound = NMSItemStack.getTag(stack);
        if (compound == null)
        {
            return null;
        }
        return compound.clone();
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        NBTTagCompound compound = null;
        if (base != null)
        {
            compound = (NBTTagCompound) base.clone();
        }
        NMSItemStack.setTag(stack, compound);
    }

    @Override
    public void eraseTag()
    {
        writeTag(null);
    }

    @Override
    protected Class<ItemStack> getContainerClass()
    {
        return ItemStack.class;
    }

    @Override
    public List<String> getTypes()
    {
        Material material = stack.getType();
        List<String> s = new ArrayList<>();
        s.add("item");
        switch (material)
        {
            case BOOK:
            case WRITTEN_BOOK:
            case WRITABLE_BOOK:
            case KNOWLEDGE_BOOK:
            {
                s.add("item_book");
                break;
            }
            case ENCHANTED_BOOK:
            {
                s.add("item_enchbook");
                break;
            }
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            {
                s.add("item_skull");
                break;
            }
            case POTION:
            case LINGERING_POTION:
            case SPLASH_POTION:
            {
                s.add("item_potion");
            }
            case FIREWORK_ROCKET:
            {
                s.add("item_rocket");
            }
            case FIREWORK_STAR:
            {
                s.add("item_firework");
            }
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
            {
                s.add("item_leather");
            }
            case IRON_BOOTS:
            case IRON_CHESTPLATE:
            case IRON_HELMET:
            case IRON_LEGGINGS:

            case CHAINMAIL_BOOTS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_LEGGINGS:

            case GOLDEN_BOOTS:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_HELMET:
            case GOLDEN_LEGGINGS:

            case DIAMOND_BOOTS:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_HELMET:
            case DIAMOND_LEGGINGS:

            case NETHERITE_BOOTS:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_HELMET:
            case NETHERITE_LEGGINGS:

            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
            case WOODEN_SWORD:

            case STONE_AXE:
            case STONE_HOE:
            case STONE_PICKAXE:
            case STONE_SHOVEL:
            case STONE_SWORD:

            case IRON_AXE:
            case IRON_HOE:
            case IRON_PICKAXE:
            case IRON_SHOVEL:
            case IRON_SWORD:

            case GOLDEN_AXE:
            case GOLDEN_HOE:
            case GOLDEN_PICKAXE:
            case GOLDEN_SHOVEL:
            case GOLDEN_SWORD:

            case DIAMOND_AXE:
            case DIAMOND_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SHOVEL:
            case DIAMOND_SWORD:

            case NETHERITE_AXE:
            case NETHERITE_HOE:
            case NETHERITE_PICKAXE:
            case NETHERITE_SHOVEL:
            case NETHERITE_SWORD:

            case BOW:
            case FLINT_AND_STEEL:
            case FISHING_ROD:
            case SHEARS:
            case SHIELD:
            case TRIDENT:
            {
                s.add("item_repair");
                break;
            }
        }
        return s;
    }

    @Override
    public String toString()
    {
        return stack.toString();
    }
}
