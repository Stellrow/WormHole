package me.Stellrow.WormHole.teleport;

import me.Stellrow.WormHole.Utils;
import me.Stellrow.WormHole.WormHole;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportManager {
    private final WormHole pl;
    //Hashmap<teleportTo,whoRequested>
    private ConcurrentHashMap<UUID, UUID> requests = new ConcurrentHashMap<>();


    public TeleportManager(WormHole pl) {
        this.pl = pl;
    }

    public void addRequest(Player teleportTo, Player whoRequested){
        if(requests.containsKey(teleportTo.getUniqueId())){
            requests.replace(teleportTo.getUniqueId(),whoRequested.getUniqueId());
            teleportTo.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.request-received").replaceAll("%sender",whoRequested.getName())));
            teleportTo.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.timeout")));
            timeOut(whoRequested);
             return;
        }
        requests.put(teleportTo.getUniqueId(),whoRequested.getUniqueId());
        timeOut(whoRequested);
        teleportTo.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.request-received").replaceAll("%sender",whoRequested.getName())));
        teleportTo.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.timeout")));

    }
    public void removeRequest(Player teleportTo){
        if(requests.containsKey(teleportTo.getUniqueId())){
            requests.remove(teleportTo.getUniqueId());
        }
    }
    public void acceptRequest(Player requested){
        Player whoRequested = Bukkit.getPlayer(requests.get(requested.getUniqueId()));
        if(whoRequested==null){
            requested.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.player-not-online")));
            return;
        }
        whoRequested.teleport(requested);
        requested.getWorld().playSound(requested.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1f,1f);
        whoRequested.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.request-accepted")));
        removeRequest(requested);
    }
    private void timeOut(Player whoAsked){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(requests.containsValue(whoAsked.getUniqueId())){
                    removeExisting(whoAsked);
                }

            }
        }.runTaskLaterAsynchronously(pl,15*20);
    }
    public boolean checkExisting(Player toCheck){
        if(requests.containsKey(toCheck.getUniqueId())){
            return true;
        }
        if(requests.containsValue(toCheck.getUniqueId())){
            return true;
        }
        return false;
    }
    public void removeExisting(Player toRemove){
        if(requests.containsKey(toRemove.getUniqueId())){
            requests.remove(toRemove.getUniqueId());
            return;
        }
        if(requests.containsValue(toRemove.getUniqueId())){
            for(UUID key : requests.keySet()){
                if(requests.get(key).equals(toRemove.getUniqueId())){
                    requests.remove(key);
                }
            }
        }
    }

}
