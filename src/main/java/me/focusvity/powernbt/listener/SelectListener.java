package me.focusvity.powernbt.listener;

import me.focusvity.powernbt.PowerNBT;
import me.focusvity.powernbt.command.action.Action;
import me.focusvity.powernbt.command.action.Argument;
import me.focusvity.powernbt.container.ContainerBlock;
import me.focusvity.powernbt.container.ContainerEntity;
import me.focusvity.powernbt.container.ContainerItemStack;
import me.focusvity.powernbt.util.Caller;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectListener implements Listener
{


    @EventHandler(priority = EventPriority.NORMAL)
    public void block(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }
        Player player = event.getPlayer();
        Caller caller = PowerNBT.getCaller(player);
        try
        {
            Argument argument = caller.getArgument();
            Action action = caller.getAction();
            if (argument == null || action == null)
            {
                return;
            }
            if (!player.isSneaking())
            {
                caller.hold(null, null);
            }
            Block block = event.getClickedBlock();
            argument.select(new ContainerBlock(block));
            action.execute();
            event.setCancelled(true);
        }
        catch (Throwable t)
        {
            caller.handleException(t);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void entity(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        Caller caller = PowerNBT.getCaller(player);
        try
        {
            Argument argument = caller.getArgument();
            Action action = caller.getAction();
            if (argument == null || action == null)
            {
                return;
            }
            if (!player.isSneaking())
            {
                caller.hold(null, null);
            }
            Entity entity = event.getRightClicked();
            argument.select(new ContainerEntity(entity));
            action.execute();
            event.setCancelled(true);
        }
        catch (Throwable t)
        {
            caller.handleException(t);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void inventory(InventoryClickEvent event)
    {
        GameMode gm = event.getWhoClicked().getGameMode();
        if (event.isRightClick() && gm.equals(GameMode.CREATIVE))
        {
            return;
        }
        ItemStack cursor = event.getCursor();
        if (cursor != null && !cursor.getType().equals(Material.AIR))
        {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().equals(Material.AIR))
        {
            return;
        }
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player))
        {
            return;
        }
        Player player = (Player) human;
        Caller caller = PowerNBT.getCaller(player);
        try
        {
            Argument argument = caller.getArgument();
            Action action = caller.getAction();
            if (argument == null || action == null)
            {
                return;
            }
            if (!event.isShiftClick())
            {
                caller.hold(null, null);
            }
            argument.select(new ContainerItemStack(item));
            action.execute();
            event.setCancelled(true);
        }
        catch (Throwable t)
        {
            caller.handleException(t);
        }
    }
}
