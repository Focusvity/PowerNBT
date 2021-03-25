package me.focusvity.powernbt.container;

import me.focusvity.powernbt.nms.NMSEntity;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ContainerEntity extends Container<Entity>
{

    private Entity entity;

    public ContainerEntity(Entity entity)
    {
        this.entity = entity;
    }

    @Override
    public Entity getObject()
    {
        return entity;
    }

    @Override
    protected NBTBase readTag()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NMSEntity.readEntity(entity, compound);
        return compound;
    }

    @Override
    protected void writeTag(NBTBase base)
    {
        NMSEntity.writeEntity(entity, (NBTTagCompound) base.clone());
    }

    @Override
    protected Class<Entity> getContainerClass()
    {
        return Entity.class;
    }

    @Override
    public List<String> getTypes()
    {
        List<String> s = new ArrayList<>();
        s.add("entity");
        if (entity instanceof LivingEntity)
        {
            s.add("living");
        }
        s.add(entity.getName());
        return null;
    }

    @Override
    public String toString()
    {
        if (entity instanceof Player)
        {
            return ((Player) entity).getDisplayName();
        }
        return entity.getType().toString();
    }
}
