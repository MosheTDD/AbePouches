package me.moshe.abepouches.commands;

import me.moshe.abepouches.AbePouches;

import me.moshe.abepouches.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.moshe.abepouches.util.Utils.*;

public class AbePouchesCommand implements CommandExecutor {
    private AbePouches plugin;
    public AbePouchesCommand(AbePouches plugin) {
        this.plugin = plugin;
        plugin.getCommand("ap").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args){
        if(!playerCheck(sender)) return true;
        Player p = (Player) sender;
        if (!permCheck(sender, "abepouches.admin")) return true;
        if(args.length < 3){
            sendMsg(sender, "illegalArguments");
            return true;
        }
        if(args[0].equalsIgnoreCase("give")){
            Player t = Bukkit.getPlayerExact(args[1]);
            if(!offlineCheck(t, sender, args[1])) return true;
            if(args[2].equalsIgnoreCase("exp")){
                if(args.length == 4){
                    try {
                        int amount = Integer.parseInt(args[3]);
                        for(int i = 0; i < amount; i++){
                            t.getInventory().addItem(expItem());
                        }
                        sendRawMsg(t,"&6You have received &c" + amount + " &6EXP Pouches!");
                    }catch (NumberFormatException ex){
                        sendMsg(p,"illegalArguments");
                    }
                }else{
                    sendRawMsg(t,"&6You have received &c1 &6EXP Pouch!");
                    t.getInventory().addItem(expItem());
                }
            }else if(args[2].equalsIgnoreCase("money")){
                if(args.length == 4){
                    try {
                        int amount = Integer.parseInt(args[3]);
                        for(int i = 0; i < amount; i++){
                            t.getInventory().addItem(moneyItem());
                        }
                        sendRawMsg(t,"&6You have received &c" + amount + " &6Money Pouches!");
                    }catch (NumberFormatException ex){
                        sendMsg(p,"illegalArguments");
                    }
                }else{
                    sendRawMsg(t,"&6You have received &c1 &6Money Pouch!");
                    t.getInventory().addItem(moneyItem());
                }
            }else {
                sendMsg(p,"illegalArguments");
            }
        }else {
            sendMsg(p, "illegalArguments");
        }
        return true;
    }

    public ItemStack expItem(){
        ItemStack itemStack = new ItemStack(Material.valueOf(Utils.cs("expItem.material").toUpperCase()));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utils.ucs("expItem.displayName"));
        meta.setLore(Utils.csl("expItem.lore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack moneyItem(){
        ItemStack itemStack = new ItemStack(Material.valueOf(Utils.cs("moneyItem.material").toUpperCase()));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utils.ucs("moneyItem.displayName"));
        meta.setLore(Utils.csl("moneyItem.lore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
