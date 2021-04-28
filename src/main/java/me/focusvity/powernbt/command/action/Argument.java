package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.container.*;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.StringParser;
import me.focusvity.powernbt.util.Type;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Argument
{

    public static final Map<String, Integer> colors = new HashMap<>()
    {{
        put("black", 0x1E1B1B);
        put("red", 0xb3312C);
        put("green", 0x3B511A);
        put("brown", 0x51310A);
        put("blue", 0x253192);
        put("purple", 0x7B2FBE);
        put("cyan", 0x287697);
        put("lightgray", 0xABABAB);
        put("gray", 0x434343);
        put("pink", 0xD88198);
        put("lime", 0x41CC34);
        put("lightgreen", 0x41CC34);
        put("yellow", 0xDECF2A);
        put("lightblue", 0x6689D3);
        put("magenta", 0xC354CD);
        put("orange", 0xEB8844);
        put("white", 0xF0F0F0);
    }};
    private static final Query emptyQuery = new Query();
    private final Caller caller;
    private Container container;
    private Query query;
    private String objectFuture = null;
    private String queryFuture = null;

    public Argument(Caller caller, String object, String parameter)
    {
        this.caller = caller;
        this.container = getContainer(caller, object, parameter);
        if (container == null)
        {
            this.objectFuture = object;
            this.queryFuture = parameter;
        }
        else if (container instanceof ContainerBase)
        {
            ContainerBase c = (ContainerBase) container;
            byte t = c.getObject().getTypeId();
            if (t == 9 || t == 10)
            {
                query = Query.fromString(parameter);
            }
            else
            {
                query = emptyQuery;
            }
        }
        else
        {
            query = Query.fromString(parameter);
        }
    }

    public static Container getContainer(Caller caller, String object, String parameter)
    {
        if (object == null)
        {
            throw new RuntimeException("No NBT Object");
        }
        if (object.equalsIgnoreCase("me"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            return new ContainerEntity((Player) caller.getSender());
        }
        if (object.equalsIgnoreCase("item") || object.equalsIgnoreCase("i"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            return new ContainerItemStack(((Player) caller.getSender()).getInventory().getItemInMainHand());
        }
        if (object.equalsIgnoreCase("inventory") || object.equalsIgnoreCase("inv"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            return new ContainerComplex(new ContainerEntity((Player) caller.getSender()),
                    new Query("Inventory"));
        }
        if (object.equalsIgnoreCase("id"))
        {
            int id = Integer.parseInt(object.substring(2).replaceAll("\\(.*\\)", ""));
            for (World world : Bukkit.getWorlds())
            {
                for (Entity entity : world.getEntities())
                {
                    if (entity.getEntityId() == id)
                    {
                        return new ContainerEntity(entity);
                    }
                }
            }
            throw new RuntimeException("No entity with ID: " + id);
        }
        if (object.equalsIgnoreCase("block") || object.equalsIgnoreCase("b"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            return new ContainerBlock(((Player) caller.getSender()).getTargetBlock(null, 128));
        }
        if (object.equalsIgnoreCase("chunk"))
        {
            CommandSender sender = caller.getSender();
            Chunk chunk = null;
            if (sender instanceof Entity)
            {
                chunk = ((Entity) sender).getLocation().getChunk();
            }
            if (sender instanceof BlockCommandSender)
            {
                chunk = ((BlockCommandSender) sender).getBlock().getChunk();
            }
            if (chunk == null)
            {
                throw new RuntimeException("Player required");
            }
            return new ContainerChunk(chunk);
        }
        if (object.equalsIgnoreCase("buffer") || object.equalsIgnoreCase("clipboard")
                || object.equalsIgnoreCase("c"))
        {
            return caller;
        }
        if (object.startsWith("*") && object.length() > 1
                || object.startsWith("player:") && object.length() > 7)
        {
            String name = object.substring(object.startsWith("*") ? 1 : 7);
            if (name.startsWith("\"") && name.endsWith("\""))
            {
                name = StringParser.parse(name.substring(1, name.length() - 1)).trim();
            }
            Player player = Bukkit.getPlayer(name);
            if (player == null)
            {
                throw new RuntimeException("Player " + name + " not found");
            }
            return new ContainerEntity(player);
        }
        if (object.startsWith("%") && object.length() > 1)
        {
            String variable = object.substring(1);
            if (variable.startsWith("\"") && variable.endsWith("\""))
            {
                variable = StringParser.parse(variable.substring(1, variable.length() - 1)).trim();
            }
            return new ContainerVariable(caller, variable);
        }
        if (colors.containsKey(object))
        {
            return new ContainerBase(NBTTagInt.a(colors.get(object)));
        }
        if (object.equalsIgnoreCase("compound") || object.equalsIgnoreCase("com"))
        {
            return new ContainerBase(new NBTTagCompound());
        }
        if (object.equalsIgnoreCase("on") || object.equalsIgnoreCase("true"))
        {
            return new ContainerBase(NBTTagByte.a((byte) 1));
        }
        if (object.equalsIgnoreCase("off") || object.equalsIgnoreCase("false"))
        {
            return new ContainerBase(NBTTagByte.a((byte) 0));
        }
        if (object.equalsIgnoreCase("int[]"))
        {
            return new ContainerBase(new NBTTagIntArray(new int[0]));
        }
        if (object.equalsIgnoreCase("byte[]"))
        {
            return new ContainerBase(new NBTTagByteArray(new byte[0]));
        }
        if (object.matches("(-?[0-9]+):(-?[0-9]+):(-?[0-9]+)(:.*)?"))
        {
            String[] t = object.split(":");
            int x = Integer.parseInt(t[0]);
            int y = Integer.parseInt(t[1]);
            int z = Integer.parseInt(t[2]);
            World w = null;
            String ww = "";
            if (t.length >= 4)
            {
                ww = t[3];
            }
            if (ww.isEmpty())
            {
                if (caller.getSender() instanceof BlockCommandSender)
                {
                    w = ((BlockCommandSender) caller.getSender()).getBlock().getWorld();
                }
                else if (caller.getSender() instanceof Player)
                {
                    w = ((Player) caller.getSender()).getWorld();
                }
            }
            else
            {
                w = Bukkit.getWorld(ww);
            }
            if (w == null)
            {
                throw new RuntimeException("No world with name " + ww);
            }
            return new ContainerBlock(w.getBlockAt(x, y, z));
        }
        if (object.matches("chunk:(-?[0-9]+):(-?[0-9]+)(:.*)?"))
        {
            String[] t = object.split(":");
            int x = Integer.parseInt(t[0]);
            int z = Integer.parseInt(t[1]);
            World w = null;
            String ww = "";
            if (t.length >= 3)
            {
                ww = t[2];
            }
            if (ww.isEmpty())
            {
                if (caller.getSender() instanceof BlockCommandSender)
                {
                    w = ((BlockCommandSender) caller.getSender()).getBlock().getWorld();
                }
                else if (caller.getSender() instanceof Player)
                {
                    w = ((Player) caller.getSender()).getWorld();
                }
            }
            else
            {
                w = Bukkit.getWorld(ww);
            }
            if (w == null)
            {
                throw new RuntimeException("No world with name " + ww);
            }
            return new ContainerChunk(w.getChunkAt(x, z));
        }
        if (object.startsWith("\"") && object.endsWith("\"") && object.length() > 1)
        {
            String s = StringParser.parse(object.substring(1, object.length() - 1));
            Type type = Type.STRING;
            if (parameter != null)
            {
                type = Type.fromString(parameter);
            }
            return new ContainerBase(type.parse(s));
        }
        if (object.matches("#-?[0-9a-fA-F]+"))
        {
            Long l = Long.parseLong(object.substring(1), 16);
            Type type = Type.INT;
            if (parameter != null)
            {
                type = Type.fromString(parameter);
            }
            return new ContainerBase(type.parse(l.toString()));
        }
        if (object.matches("b[0-1]+"))
        {
            if (parameter == null)
            {
                return null;
            }
            Long l = Long.parseLong(object.substring(1), 2);
            Type type = Type.fromString(parameter);
            return new ContainerBase(type.parse(l.toString()));
        }
        if (object.matches("-?[0-9]*"))
        {
            if (parameter == null)
            {
                return null;
            }
            Type type = Type.fromString(parameter);
            if (type.equals(Type.BYTE_ARRAY))
            {
                type = Type.BYTE;
            }
            else if (type.equals(Type.INT_ARRAY))
            {
                type = Type.INT;
            }
            else if (type.equals(Type.LONG_ARRAY))
            {
                type = Type.LONG;
            }
            return new ContainerBase(type.parse(object));
        }
        if (object.matches("NaN|-?Infinity"))
        {
            if (parameter == null)
            {
                return null;
            }
            Type type = Type.fromString(parameter);
            if (type.equals(Type.BYTE_ARRAY))
            {
                type = Type.BYTE;
            }
            else if (type.equals(Type.INT_ARRAY))
            {
                type = Type.INT;
            }
            else if (type.equals(Type.LONG_ARRAY))
            {
                type = Type.LONG;
            }
            return new ContainerBase(type.parse(object));
        }
        if (object.matches("-?[0-9]+\\.[0-9]*"))
        {
            if (parameter == null)
            {
                return null;
            }
            Type type = Type.fromString(parameter);
            if (type.equals(Type.BYTE_ARRAY))
            {
                type = Type.BYTE;
            }
            else if (type.equals(Type.INT_ARRAY))
            {
                type = Type.INT;
            }
            else if (type.equals(Type.LONG_ARRAY))
            {
                type = Type.LONG;
            }
            return new ContainerBase(type.parse(object));
        }
        if (object.equals("*") || object.equals("self") || object.equals("this"))
        {
            return null;
        }
        if (object.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]"))
        {
            if (parameter == null)
            {
                return null;
            }
            else
            {
                Type type = Type.fromString(parameter);
                if (type == Type.BYTE)
                {
                    type = Type.BYTE_ARRAY;
                }
                else if (type == Type.INT)
                {
                    type = Type.INT_ARRAY;
                }
                else if (type.equals(Type.LONG))
                {
                    type = Type.LONG_ARRAY;
                }
                return new ContainerBase(type.parse(object));
            }
        }
        if (object.equals("hand") || object.equals("h"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            Player p = (Player) caller.getSender();
            ContainerEntity player = new ContainerEntity(p);
            int pslot = p.getInventory().getHeldItemSlot();
            int ind = 0;
            int result = -1;
            NBTTagList inventory = (NBTTagList) ((NBTTagCompound) player.getCustomTag()).get("Inventory");
            for (NBTBase bt : inventory)
            {
                NBTTagCompound ct = (NBTTagCompound) bt;
                if (ct.getByte("Slot") == pslot)
                {
                    result = ind;
                    break;
                }
                ind++;
            }
            if (result == -1)
            {
                throw new RuntimeException("No value");
            }
            Query q = new Query("Inventory", result);
            return new ContainerComplex(player, q);
        }
        if (object.startsWith("hand:") && object.length() > 5 || object.startsWith("h:") && object.length() > 2)
        {
            String name = object.substring(object.indexOf(':') + 1);
            if (name.startsWith("\"") && name.endsWith("\"") && name.length() > 1)
            {
                name = StringParser.parse(name.substring(1, name.length() - 1)).trim();
            }
            Player player = Bukkit.getPlayer(name);
            if (player == null)
            {
                throw new RuntimeException("Player " + name + " not found");
            }
            ContainerEntity container = new ContainerEntity(player);
            int pslot = player.getInventory().getHeldItemSlot();
            int ind = 0;
            int result = -1;
            NBTTagList inventory = (NBTTagList) ((NBTTagCompound) container.getCustomTag()).get("Inventory");
            for (NBTBase bt : inventory)
            {
                NBTTagCompound ct = (NBTTagCompound) bt;
                if (ct.getByte("Slot") == pslot)
                {
                    result = ind;
                    break;
                }
                ind++;
            }
            if (result == -1)
            {
                throw new RuntimeException("No value");
            }
            Query q = new Query("Inventory", result);
            return new ContainerComplex(container, q);
        }
        throw new RuntimeException("No NBT object: " + object);
    }

    public void prepare(final Action action, final Container<?> pContainer, final Query pQuery) throws Exception
    {
        if (objectFuture.equals("*"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            caller.send("Select a block or entity by right-click");
            caller.hold(this, action);
        }
        else if (objectFuture.equals("self") || objectFuture.equals("this"))
        {
            if (pContainer == null)
            {
                throw new RuntimeException("Object self is not available in this context");
            }
            this.container = pContainer;
            this.query = Query.fromString(queryFuture);
            action.execute();
        }
        else if (objectFuture.equals("hand") || objectFuture.equals("h"))
        {
            if (!(caller.getSender() instanceof Player))
            {
                throw new RuntimeException("Player required");
            }
            Player p = (Player) caller.getSender();
            ContainerEntity player = new ContainerEntity(p);
            int pslot = p.getInventory().getHeldItemSlot();
            int ind = 0;
            int result = -1;
            NBTTagList inventory = (NBTTagList) ((NBTTagCompound) player.getCustomTag()).get("Inventory");
            for (NBTBase bt : inventory)
            {
                NBTTagCompound ct = (NBTTagCompound) bt;
                if (ct.getByte("Slot") == pslot)
                {
                    result = ind;
                    break;
                }
                ind++;
            }
            if (result == -1)
            {
                throw new RuntimeException("No value");
            }
            Query q = new Query("Inventory", result);
            this.container = new ContainerComplex(player, q);
            this.query = Query.fromString(queryFuture);
            action.execute();
        }
        else if (objectFuture.matches("b[0-1]*"))
        {
            if (pContainer == null)
            {
                throw new RuntimeException("Type of value " + objectFuture + " not set");
            }
            Long val = Long.parseLong(objectFuture, 2);
            Type type = Type.fromBase(pQuery.get(pContainer.getCustomTag()));
            this.container = new ContainerBase(type.parse(val.toString()));
            this.query = emptyQuery;
            action.execute();
        }
        else
        {
            throw new RuntimeException("future object type ignored");
        }
    }

    public Container getContainer()
    {
        return container;
    }

    public Query getQuery()
    {
        return query;
    }

    public boolean needPrepare()
    {
        return container == null;
    }

    public void select(Container container)
    {
        this.container = container;
        this.query = Query.fromString(queryFuture);
    }
}
