package me.focusvity.powernbt.util;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;

public enum Type
{

    END("\u24EA", ChatColor.WHITE),
    BYTE("\u24B7", ChatColor.RED),
    SHORT("\u24C8", ChatColor.YELLOW),
    INT("\u24BE", ChatColor.BLUE),
    LONG("\u24C1", ChatColor.AQUA),
    FLOAT("\u24BB", ChatColor.DARK_PURPLE),
    DOUBLE("\u24B9", ChatColor.LIGHT_PURPLE),
    BYTE_ARRAY(ChatColor.BOLD + "\u24D1", ChatColor.RED),
    STRING("\u24C9", ChatColor.GREEN),
    LIST("\u2630", ChatColor.DARK_GRAY),
    COMPOUND("\u26B2", ChatColor.GRAY),
    INT_ARRAY(ChatColor.BOLD + "\u24D8", ChatColor.DARK_BLUE),
    LONG_ARRAY(ChatColor.BOLD + "\u24DB", ChatColor.DARK_AQUA);

    private final String name;
    private final byte typeId;
    private final String prefix;
    private final ChatColor color;

    Type(String prefix, ChatColor color)
    {
        this.name = name().toLowerCase().replace("_array", "[]");
        this.typeId = (byte) ordinal();
        this.prefix = prefix;
        this.color = color;
    }

    public static Type fromByte(byte b)
    {
        for (Type type : values())
        {
            if (type.typeId == b)
            {
                return type;
            }
        }
        return END;
    }

    public static Type fromBase(NBTBase base)
    {
        if (base == null)
        {
            return END;
        }
        return fromByte(base.getTypeId());
    }

    public static Type fromString(String name)
    {
        if (name == null || name.isEmpty())
        {
            return END;
        }
        name = name.toLowerCase();
        for (Type type : values())
        {
            if (name.equalsIgnoreCase(type.name) || type.name.toLowerCase().startsWith(name))
            {
                return type;
            }
        }
        return END;
    }

    public NBTBase parse(String s)
    {
        switch (this)
        {
            case STRING:
            {
                return NBTTagString.a(s);
            }
            case BYTE:
            {
                Byte v = null;
                try
                {
                    v = Byte.parseByte(s);
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (byte) Long.parseLong(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    try
                    {
                        v = (byte) Double.parseDouble(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagByte.a(v);
            }
            case SHORT:
            {
                Short v = null;
                try
                {
                    v = Short.parseShort(s);
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (short) Long.parseLong(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    try
                    {
                        v = (short) Double.parseDouble(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagShort.a(v);
            }
            case INT:
            {
                Integer v = null;
                try
                {
                    v = Integer.parseInt(s);
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (int) Long.parseLong(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    try
                    {
                        v = (int) Double.parseDouble(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagInt.a(v);
            }
            case LONG:
            {
                Long v = null;
                try
                {
                    v = Long.parseLong(s);
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (long) Double.parseDouble(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagLong.a(v);
            }
            case DOUBLE:
            {
                Double v = null;
                try
                {
                    if (s.equalsIgnoreCase("NaN"))
                    {
                        v = Double.NaN;
                    }
                    else
                    {
                        v = Double.parseDouble(s);
                    }
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (double) Long.parseLong(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagDouble.a(v);
            }
            case FLOAT:
            {
                Float v = null;
                try
                {
                    if (s.equalsIgnoreCase("NaN"))
                    {
                        v = Float.NaN;
                    }
                    else
                    {
                        v = Float.parseFloat(s);
                    }
                }
                catch (Throwable ignored)
                {
                }
                if (v == null)
                {
                    try
                    {
                        v = (float) Double.parseDouble(s);
                    }
                    catch (Throwable ignored)
                    {
                    }
                }
                if (v == null)
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                return NBTTagFloat.a(v);
            }
            case BYTE_ARRAY:
            {
                if (!s.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]"))
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                String sp = s.substring(1, s.length() - 1);
                if (sp.isEmpty())
                {
                    return new NBTTagByteArray(new byte[0]);
                }
                String[] ss = sp.split(",");
                byte[] v = new byte[ss.length];
                for (int i = 0; i < v.length; i++)
                {
                    Byte t = null;
                    String x = ss[i];
                    if (x.startsWith("#"))
                    {
                        try
                        {
                            t = (byte) Long.parseLong(x.substring(1), 16);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    try
                    {
                        t = Byte.parseByte(x);
                    }
                    catch (Throwable ignored)
                    {
                    }
                    if (t == null)
                    {
                        try
                        {
                            t = (byte) Long.parseLong(x);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    if (t == null)
                    {
                        throw new RuntimeException("Cannot parse " + x + " to " + BYTE.name);
                    }
                    v[i] = t;
                }
                return new NBTTagByteArray(v);
            }
            case INT_ARRAY:
            {
                if (!s.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]"))
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                String sp = s.substring(1, s.length() - 1);
                if (sp.isEmpty())
                {
                    return new NBTTagIntArray(new int[0]);
                }
                String[] ss = sp.split(",");
                int[] v = new int[ss.length];
                for (int i = 0; i < v.length; i++)
                {
                    Integer t = null;
                    String x = ss[i];
                    if (x.startsWith("#"))
                    {
                        try
                        {
                            t = (int) Long.parseLong(x.substring(1), 16);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    try
                    {
                        t = Integer.parseInt(x);
                    }
                    catch (Throwable ignored)
                    {
                    }
                    if (t == null)
                    {
                        try
                        {
                            t = (int) Long.parseLong(x);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    if (t == null)
                    {
                        throw new RuntimeException("Cannot parse " + x + " to " + INT.name);
                    }
                    v[i] = t;
                }
                return new NBTTagIntArray(v);
            }
            case LONG_ARRAY:
            {
                if (!s.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]"))
                {
                    throw new RuntimeException("Cannot parse " + s + " to " + name);
                }
                String sp = s.substring(1, s.length() - 1);
                if (sp.isEmpty())
                {
                    return new NBTTagLongArray(new long[0]);
                }
                String[] ss = sp.split(",");
                long[] v = new long[ss.length];
                for (int i = 0; i < v.length; i++)
                {
                    Long t = null;
                    String x = ss[i];
                    if (x.startsWith("#"))
                    {
                        try
                        {
                            t = Long.parseLong(x.substring(1), 16);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    try
                    {
                        t = Long.parseLong(x);
                    }
                    catch (Throwable ignored)
                    {
                    }
                    if (t == null)
                    {
                        try
                        {
                            t = Long.parseLong(x);
                        }
                        catch (Throwable ignored)
                        {
                        }
                    }
                    if (t == null)
                    {
                        throw new RuntimeException("Cannot parse " + x + " to " + LONG.name);
                    }
                    v[i] = t;
                }
                return new NBTTagLongArray(v);
            }
            default:
            {
                throw new RuntimeException("Cannot parse " + s + " to " + name);
            }
        }
    }
}
