package me.Stellrow.WormHole.events;

import me.Stellrow.WormHole.WormHole;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class WormHoleEvents implements Listener {
    private final WormHole pl;

    public WormHoleEvents(WormHole pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        ItemStack used = event.getItem();
        if(used.hasItemMeta()&&used.getItemMeta().getPersistentDataContainer().has(pl.potionkey, PersistentDataType.STRING)){
        pl.getInventoryManager().openInventory(event.getPlayer());
        event.getItem().setAmount(event.getItem().getAmount()-1);
        return;
        }
    }






    //Handle player quiting while requesting/being requested
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(pl.getTeleportManager().checkExisting(event.getPlayer())){
            pl.getTeleportManager().removeExisting(event.getPlayer());
        }
    }
}
