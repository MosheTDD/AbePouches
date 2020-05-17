package me.moshe.abepouches.listener;

import me.moshe.abepouches.AbePouches;
import me.moshe.abepouches.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class AbePouchesEvents implements Listener {
    private AbePouches plugin;

    public AbePouchesEvents(AbePouches plugin) {
        this.plugin = plugin;
    }
    public Economy econ = null;


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack expItem = expItem();
        ItemStack moneyItem = moneyItem();
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() == null) return;
            if (!p.getItemInHand().hasItemMeta()) return;
            ItemStack inHand = p.getItemInHand();
            if (inHand.getType() == moneyItem.getType() && inHand.getItemMeta().getLore() == moneyItem.getItemMeta().getLore() && inHand.getItemMeta().getDisplayName().equals(moneyItem.getItemMeta().getDisplayName())) {
                if(!setupEconomy()){
                    Bukkit.getConsoleSender().sendMessage(Utils.colorize("&cVault is not activated on this server! Money will not work!"));
                    p.sendMessage(Utils.colorize("&cMoney pouches are disabled"));
                    return;
                }
                int min = Utils.ci("moneyMinAmount");
                int max = Utils.ci("moneyMaxAmount");
                int range = max - min + 1;
                int newMoney = (int) (Math.random() * range) + min;
                int newItemAmount = p.getItemInHand().getAmount() - 1;
                econ.depositPlayer(p, newMoney);
                p.sendMessage(Utils.ucs("messages.money").replaceAll("%newMoney%", String.valueOf(newMoney)));
                if(newItemAmount == 0){
                    p.getInventory().remove(inHand);
                }
                p.getItemInHand().setAmount(newItemAmount);
                e.setCancelled(true);
                return;
            }
            if (inHand.getType() == expItem.getType() && inHand.getItemMeta().getLore() == expItem.getItemMeta().getLore() && inHand.getItemMeta().getDisplayName().equals(expItem.getItemMeta().getDisplayName())) {
                int min = Utils.ci("expMinAmount");
                int max = Utils.ci("expMaxAmount");
                int range = max - min + 1;
                int newExp = (int) (Math.random() * range) + min;
                int newItemAmount = p.getItemInHand().getAmount() - 1;
                p.giveExp(newExp);
                p.sendMessage(Utils.ucs("messages.exp").replaceAll("%newExp%", String.valueOf(newExp)));
                if(newItemAmount == 0){
                    p.getInventory().remove(inHand);
                }
                p.getItemInHand().setAmount(newItemAmount);
                e.setCancelled(true);
            }
        }
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

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
