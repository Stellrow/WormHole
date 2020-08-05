package me.Stellrow.WormHole.inventory;

import me.Stellrow.WormHole.Utils;
import me.Stellrow.WormHole.WormHole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashSet;
import java.util.Set;

public class InventoryManager implements Listener {
    private final WormHole pl;
    private Set<Inventory> activeInventories = new HashSet<Inventory>();

    public InventoryManager(WormHole pl) {
        this.pl = pl;
    }

    private Inventory returnInventory(Player whoRequested){
        Inventory inv = Bukkit.createInventory(whoRequested,54,Utils.asColor("&aWormHole"));
        activeInventories.add(inv);
        for(Player online : Bukkit.getOnlinePlayers()){
            if(online.equals(whoRequested)){
                //Skip requested player head
            }else {
                inv.addItem(getHead(online));
            }
        }
        return inv;
    }
    public void openInventory(Player toOpen){
        toOpen.openInventory(returnInventory(toOpen));
    }
    private ItemStack getHead(Player toCopy){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta im = head.getItemMeta();
        im.setDisplayName(Utils.asColor("&a"+toCopy.getName()));
        SkullMeta skullMeta = (SkullMeta) im;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(toCopy.getUniqueId()));
        head.setItemMeta(im);
        return head;
    }

    ///Handle inventory clicking
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory()==null&&event.getCurrentItem()==null){
            return;
        }
        if(activeInventories.contains(event.getClickedInventory())){
            if(event.getCurrentItem()==null){
                return;
            }
            Player whoSent = (Player) event.getWhoClicked();
            event.setCancelled(true);
            Player target = Bukkit.getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            if(pl.getConfig().getBoolean("General.insta-teleport")){
                whoSent.teleport(target);
                whoSent.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1f,1f);
                return;
            }

            pl.getTeleportManager().addRequest(target,whoSent);
            whoSent.closeInventory();
            whoSent.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.request-sent")));
            return;
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(activeInventories.contains(event.getInventory())){
            activeInventories.remove(event.getInventory());
        }
    }



    //handle inventory
}
