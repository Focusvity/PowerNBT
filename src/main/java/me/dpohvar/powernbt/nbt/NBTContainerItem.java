package me.dpohvar.powernbt.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.dpohvar.powernbt.utils.ItemStackUtils.itemStackUtils;

public class NBTContainerItem extends NBTContainer<ItemStack>
{

    ItemStack item;

    public NBTContainerItem(ItemStack item)
    {
        this.item = item;
    }

    public ItemStack getObject()
    {
        return item;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<String> getTypes()
    {
        Material id = item.getType();
        List<String> s = new ArrayList<String>();
        s.add("item");
        switch (id)
        {
            case WRITABLE_BOOK:
            case WRITTEN_BOOK:
                s.add("item_book");
                break;
            case PLAYER_HEAD:
            case CREEPER_HEAD:
            case DRAGON_HEAD:
            case ZOMBIE_HEAD:
            case SKELETON_SKULL:
            case WITHER_SKELETON_SKULL:
                s.add("item_skull");
                break;
            case ENCHANTED_BOOK:
                s.add("item_enchbook");
                break;
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
                s.add("item_potion");
                break;
            case FIREWORK_ROCKET:
                s.add("item_rocket");
                break;
            case FIREWORK_STAR:
                s.add("item_firework");
                break;
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                s.add("item_leather");

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

            case BOW:
            case FLINT_AND_STEEL:
            case FISHING_ROD:
            case SHEARS:
            case SHIELD:
            case TRIDENT:
                s.add("item_repair");
                break;
        }
        return s;
    }

    @Override
    public NBTTagCompound readTag()
    {
        Object tag = itemStackUtils.getTag(item);
        if (tag == null) return null;
        return (NBTTagCompound) new NBTTagCompound(false, tag).clone();
    }

    @Override
    public void writeTag(NBTBase base)
    {
        Object handle = null;
        if (base != null) handle = base.clone().handle;
        itemStackUtils.setTag(item, handle);
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
    public String toString()
    {
        return item.toString();
    }
}
