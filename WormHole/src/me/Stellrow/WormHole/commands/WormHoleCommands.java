package me.Stellrow.WormHole.commands;

import me.Stellrow.WormHole.Utils;
import me.Stellrow.WormHole.WormHole;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class WormHoleCommands implements CommandExecutor {
    private final WormHole pl;

    public WormHoleCommands(WormHole pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String sa, String[] args) {
        if(args.length==1&&args[0].equalsIgnoreCase("accept")){
            if(pl.getTeleportManager().checkExisting((Player) sender)){
                pl.getTeleportManager().acceptRequest((Player) sender);
                return true;
            }


        }
        if(args.length==3&&args[0].equalsIgnoreCase("give")){
            if(!sender.hasPermission("wormhole.give")){
                sender.sendMessage(Utils.asColor(pl.getConfig().getString("Messages.no-permission")));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target==null){
                sender.sendMessage(Utils.asColor("&cPlayer is not online or doesn't exist!"));
                return true;
            }
            try{
                Integer amount = Integer.parseInt(args[2]);
                giveItem(target,amount);
                sender.sendMessage(Utils.asColor("&aGave the player the potion!"));
                return true;
            }catch (IllegalArgumentException exception){
                sender.sendMessage(Utils.asColor("&cPlease give a number as amount!"));
                return true;
            }


        }




        if(sender.hasPermission("wormhole.give")) {
            sender.sendMessage(Utils.asColor("&7Usage: /wormhole give <player> <amount>"));
            return true;
        }
        return true;
    }

    private void giveItem(Player toGive,Integer amount){
        ItemStack toSend = pl.potion;
        toSend.setAmount(amount);
        HashMap<Integer, ItemStack> remaining = toGive.getInventory().addItem(toSend);
        for(Integer key : remaining.keySet()){
            toGive.getWorld().dropItemNaturally(toGive.getLocation(),toSend);
        }
    }
}
