package me.focusvity.powernbt.util;

import net.minecraft.server.v1_16_R3.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Viewer
{

    public static String getShortValue(NBTBase base, boolean hex, boolean bin)
    {
        String value = "";
        switch (base.getTypeId())
        {
            case 1:
            {
                byte v = ((NBTTagByte) base).asByte();
                if (hex)
                {
                    value = "#" + Integer.toHexString(v & 0xFF);
                }
                else if (bin)
                {
                    value = "b" + Integer.toBinaryString(v & 0xFF);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 2:
            {
                short v = ((NBTTagShort) base).asShort();
                if (hex)
                {
                    value = "#" + Integer.toHexString(v & 0xFFFF);
                }
                else if (bin)
                {
                    value = "b" + Integer.toBinaryString(v & 0xFFFF);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 3:
            {
                int v = ((NBTTagInt) base).asInt();
                if (hex)
                {
                    value = "#" + Long.toHexString(v & 0xFFFFFFFFL);
                }
                else if (bin)
                {
                    value = "b" + Long.toBinaryString(v & 0xFFFFFFFFL);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 4:
            {
                long v = ((NBTTagLong) base).asLong();
                if (hex)
                {
                    value = "#" + Long.toHexString(v);
                }
                else if (bin)
                {
                    value = "b" + Long.toBinaryString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 5:
            {
                float v = ((NBTTagFloat) base).asFloat();
                if (hex)
                {
                    value = "#" + Float.toHexString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 6:
            {
                double v = ((NBTTagDouble) base).asDouble();
                if (hex)
                {
                    value = "#" + Double.toHexString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 7:
            {
                NBTTagByteArray v = ((NBTTagByteArray) base);
                if (v.isEmpty())
                {
                    value = "Empty Array";
                }
                else
                {
                    StringBuilder builder = new StringBuilder(v.size()).append(" :[");
                    if (hex)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(b ->
                                h.add(Integer.toHexString(b.asByte() & 0xFF)));
                        builder.append("#").append(StringUtils.join(h, ", "));
                    }
                    else if (bin)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(b ->
                                h.add(Integer.toBinaryString(b.asByte() & 0xFF)));
                        builder.append("b").append(StringUtils.join(h, ", "));
                    }
                    else
                    {
                        ArrayList<Byte> h = new ArrayList<>(v.size());
                        v.iterator().forEachRemaining(b ->
                                h.add(b.asByte()));
                        builder.append(StringUtils.join(h, ", "));
                    }
                    value = builder.append("]").toString();
                }
                break;
            }
            case 8:
            {
                value = base.asString();
                if (hex)
                {
                    ArrayList<String> h = new ArrayList<>();
                    for (byte b : value.getBytes(StandardCharsets.UTF_8))
                    {
                        h.add(Integer.toHexString(b & 0xFF));
                    }
                    value = StringUtils.join(h, ", ");
                }
                break;
            }
            case 9:
            {
                NBTTagList list = (NBTTagList) base;
                Type listType = Type.fromByte(list.getTypeId());
                if (list.isEmpty())
                {
                    value = "Empty List";
                }
                else
                {
                    value = list.size() + " elements " + listType.getColor() + listType.getName() + ChatColor.RESET;
                }
                break;
            }
            case 10:
            {
                NBTTagCompound compound = (NBTTagCompound) base;
                if (compound.isEmpty())
                {
                    value = "Empty Compound";
                }
                else
                {
                    ArrayList<String> h = new ArrayList<>();
                    for (String key : compound.getKeys())
                    {
                        Type t = Type.fromBase(compound.get(key));
                        h.add(t.getColor() + key);
                    }
                    value = StringUtils.join(h, ChatColor.RESET + ", ");
                }
                break;
            }
            case 11:
            {
                NBTTagIntArray v = (NBTTagIntArray) base;
                if (v.isEmpty())
                {
                    value = "Empty Array";
                }
                else
                {
                    StringBuilder builder = new StringBuilder(v.size()).append(": [");
                    if (hex)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(i ->
                                h.add(Long.toHexString(i.asInt() & 0xFFFFFFFFL)));
                        builder.append("#").append(StringUtils.join(h, ", "));
                    }
                    else if (bin)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(i ->
                                h.add(Long.toBinaryString(i.asInt() & 0xFFFFFFFFL)));
                        builder.append("b").append(StringUtils.join(h, ", "));
                    }
                    else
                    {
                        ArrayList<Integer> h = new ArrayList<>(v.size());
                        v.iterator().forEachRemaining(i ->
                                h.add(i.asInt()));
                        builder.append(StringUtils.join(h, ", "));
                    }
                    value = builder.append("]").toString();
                }
                break;
            }
            case 12:
            {
                NBTTagLongArray v = (NBTTagLongArray) base;
                if (v.isEmpty())
                {
                    value = "Empty Array";
                }
                else
                {
                    StringBuilder builder = new StringBuilder(v.size() + ": [");
                    if (hex)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(l ->
                                h.add(Long.toHexString(l.asLong())));
                        builder.append("#").append(StringUtils.join(h, ", "));
                    }
                    else if (bin)
                    {
                        ArrayList<String> h = new ArrayList<>(v.size());
                        v.forEach(l ->
                                h.add(Long.toBinaryString(l.asLong())));
                        builder.append("b").append(StringUtils.join(h, ", "));
                    }
                    else
                    {
                        ArrayList<Long> h = new ArrayList<>(v.size());
                        v.iterator().forEachRemaining(l ->
                                h.add(l.asLong()));
                        builder.append(StringUtils.join(h, ", "));
                    }
                    value = builder.append("]").toString();
                }
                break;
            }
        }

        int overText = Math.max(ChatColor.stripColor(value).length() - 60, value.length() - 120);
        String resetString = ChatColor.RESET.toString();
        if (value.endsWith(resetString))
        {
            value = value.substring(0, value.length() - resetString.length());
        }
        if (overText > 0)
        {
            value = value.substring(0, 59).concat("\u2026");
        }
        return value;
    }

    public static String getFullValue(NBTBase base, int start, int end, boolean hex, boolean bin)
    {
        if (base == null)
        {
            return "No Value";
        }
        if (start > end)
        {
            int t = start;
            start = end;
            end = t;
        }
        Type type = Type.fromBase(base);
        String value = "";
        switch (base.getTypeId())
        {
            case 1:
            {
                byte v = ((NBTTagByte) base).asByte();
                if (hex)
                {
                    value = "#" + Integer.toHexString(v & 0xFF);
                }
                else if (bin)
                {
                    value = "b" + Integer.toBinaryString(v & 0xFF);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 2:
            {
                short v = ((NBTTagShort) base).asShort();
                if (hex)
                {
                    value = "#" + Integer.toHexString(v & 0xFFFF);
                }
                else if (bin)
                {
                    value = "b" + Integer.toBinaryString(v & 0xFFFF);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 3:
            {
                int v = ((NBTTagInt) base).asInt();
                if (hex)
                {
                    value = "#" + Long.toHexString(v & 0xFFFFFFFFL);
                }
                else if (bin)
                {
                    value = "b" + Long.toBinaryString(v & 0xFFFFFFFFL);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 4:
            {
                long v = ((NBTTagLong) base).asLong();
                if (hex)
                {
                    value = "#" + Long.toHexString(v);
                }
                else if (bin)
                {
                    value = "b" + Long.toBinaryString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 5:
            {
                float v = ((NBTTagFloat) base).asFloat();
                if (hex)
                {
                    value = "#" + Float.toHexString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 6:
            {
                double v = ((NBTTagDouble) base).asDouble();
                if (hex)
                {
                    value = "#" + Double.toHexString(v);
                }
                else
                {
                    value = String.valueOf(v);
                }
                break;
            }
            case 7:
            {
                if (start == 0 && end == 0)
                {
                    end = 10;
                }
                NBTTagByteArray v = (NBTTagByteArray) base;
                if (v.isEmpty())
                {
                    value = "Empty Array";
                    break;
                }
                else if (start > v.size())
                {
                    value = "Out of Range";
                    break;
                }
                else
                {
                    StringBuilder builder = new StringBuilder();
                    for (int i = start; i < end; i++)
                    {
                        if (i >= v.size())
                        {
                            break;
                        }
                        builder.append("\n").append(type.getColor()).append("[").append(i).append("] ").append(ChatColor.RESET);
                        if (hex)
                        {
                            builder.append("#").append(Integer.toHexString(v.get(i).asByte() & 0xFF));
                        }
                        else if (bin)
                        {
                            builder.append("b").append(Integer.toBinaryString(v.get(i).asByte() & 0xFF));
                        }
                        else
                        {
                            builder.append(v.get(i).asByte() & 0xFF);
                        }
                    }
                    value = v.size() + " elements" + builder;
                }
                break;
            }
            case 8:
            {
                boolean postFix = false;
                if (start == 0 && end == 0)
                {
                    end = 600;
                    postFix = true;
                }
                value = base.asString();
                if (start > value.length())
                {
                    value = "Out of Range";
                    break;
                }
                else
                {
                    if (end > value.length())
                    {
                        end = value.length();
                        postFix = false;
                    }
                    value = value.substring(start, end);
                }
                if (hex)
                {
                    ArrayList<String> h = new ArrayList<>();
                    for (byte b : value.getBytes(StandardCharsets.UTF_8))
                    {
                        h.add(Integer.toHexString(b & 0xFF));
                    }
                    value = StringUtils.join(h, " ");
                }
                if (postFix)
                {
                    value.concat("\u2026");
                }
                break;
            }
            case 9:
            {
                if (start == 0 && end == 0)
                {
                    end = 10;
                }
                NBTTagList list = (NBTTagList) base;
                Type t = Type.fromByte(list.getTypeId());
                if (list.isEmpty())
                {
                    value = "Empty List";
                    break;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = start; i < end; i++)
                {
                    if (i >= list.size())
                    {
                        break;
                    }
                    NBTBase b = list.get(i);
                    builder.append("\n").append(t.getColor()).append(ChatColor.BOLD).append("[").append(i).append("] ")
                            .append(ChatColor.RESET).append(getShortValue(b, hex, bin));
                }
                value = list.size() + " elements" + builder;
                break;
            }
            case 10:
            {
                if (start == 0 && end == 0)
                {
                    end = 60;
                }
                List<String> keys = new ArrayList<>(((NBTTagCompound) base).getKeys());
                if (keys.isEmpty())
                {
                    value = "Empty Compound";
                    break;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = start; i < end; i++)
                {
                    if (i >= keys.size())
                    {
                        break;
                    }
                    String key = keys.get(i);
                    NBTBase b = ((NBTTagCompound) base).getCompound(key);
                    Type t = Type.fromBase(b);
                    ChatColor c = t.getColor();
                    if (b instanceof NBTTagList)
                    {
                        c = Type.fromByte(b.getTypeId()).getColor();
                    }
                    String bold = "";
                    switch (t)
                    {
                        case LIST:
                        case COMPOUND:
                        {
                            bold = ChatColor.BOLD.toString();
                        }
                    }
                    builder.append("\n").append(c).append(t.getPrefix()).append(" ").append(bold).append(key).append(":")
                            .append(ChatColor.RESET).append(" ").append(getShortValue(b, hex, bin));
                }
                value = keys.size() + " elements" + builder;
                break;
            }
            case 11:
            {
                if (start == 0 && end == 0)
                {
                    end = 60;
                }
                NBTTagIntArray v = (NBTTagIntArray) base;
                if (v.isEmpty())
                {
                    value = "Empty Array";
                }
                else if (start > v.size())
                {
                    value = "Out of Range";
                }
                else
                {
                    StringBuilder builder = new StringBuilder();
                    for (int i = start; i < end; i++)
                    {
                        if (i >= v.size())
                        {
                            break;
                        }
                        builder.append("\n").append(type.getColor()).append("[").append(i).append("] ").append(ChatColor.RESET);
                        if (hex)
                        {
                            builder.append("#").append(Long.toHexString(v.get(i).asInt() & 0xFFFFFFFFL));
                        }
                        else if (bin)
                        {
                            builder.append("b").append(Long.toBinaryString(v.get(i).asInt() & 0xFFFFFFFFL));
                        }
                        else
                        {
                            builder.append(v.get(i).asInt());
                        }
                    }
                    value = v.size() + " elements" + builder;
                }
                break;
            }
            case 12:
            {
                if (start == 0 && end == 0)
                {
                    end = 60;
                }
                NBTTagLongArray v = (NBTTagLongArray) base;
                if (v.isEmpty())
                {
                    value = "Empty Array";
                }
                else if (start > v.size())
                {
                    value = "Out of Range";
                }
                else
                {
                    StringBuilder builder = new StringBuilder();
                    for (int i = start; i < end; i++)
                    {
                        if (i >= v.size())
                        {
                            break;
                        }
                        builder.append("\n").append(type.getColor()).append("[").append(i).append("] ").append(ChatColor.RESET);
                        if (hex)
                        {
                            builder.append("#").append(Long.toHexString(v.get(i).asLong()));
                        }
                        else if (bin)
                        {
                            builder.append("b").append(Long.toBinaryString(v.get(i).asLong()));
                        }
                        else
                        {
                            builder.append(v.get(i).asLong());
                        }
                    }
                    value = v.size() + " elements" + builder;
                }
                break;
            }
        }
        return type.getPrefix() + ChatColor.RESET + ": " + value;
    }
}
