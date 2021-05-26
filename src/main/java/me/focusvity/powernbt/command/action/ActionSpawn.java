package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.nms.NMSEntity;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class ActionSpawn extends Action
{

    private final Caller caller;
    private final Argument arg;
    private String worldParam;

    public ActionSpawn(Caller caller, String object, String param)
    {
        this.caller = caller;
        this.arg = new Argument(caller, object, param);
    }

    public ActionSpawn(Caller caller, String object, String param, String worldParam)
    {
        this(caller, object, param);
        this.worldParam = worldParam;
    }

    @Override
    public void execute() throws Exception
    {
        if (arg.needPrepare())
        {
            arg.prepare(this, null, null);
            return;
        }
        Container container = arg.getContainer();
        Query query = arg.getQuery();
        World world;
        if (worldParam == null)
        {
            CommandSender sender = caller.getSender();
            if (sender instanceof Entity)
            {
                world = ((Entity) sender).getWorld();
            }
            else if (sender instanceof BlockCommandSender)
            {
                world = ((BlockCommandSender) sender).getBlock().getWorld();
            }
            else
            {
                throw new RuntimeException("Player required");
            }
        }
        else
        {
            world = Bukkit.getWorld(worldParam);
            if (world == null)
            {
                throw new RuntimeException("No world with name " + worldParam);
            }
        }
        NBTBase base = query.get(container.getCustomTag());
        Entity entity = NMSEntity.spawnEntity((NBTTagCompound) base, world);
        if (entity == null)
        {
            caller.send("No entity spawned");
            return;
        }
        caller.send("Entity " + entity + " spawned");
    }
}
