package me.moshe.abepouches;

import me.moshe.abepouches.commands.AbePouchesCommand;
import me.moshe.abepouches.listener.AbePouchesEvents;
import me.moshe.abepouches.util.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class AbePouches extends JavaPlugin {

    @Override
    public void onEnable(){
        saveConfig();
        getConfig().options().copyDefaults(true);
        getServer().getPluginManager().registerEvents(new AbePouchesEvents(this), this);
        new AbePouchesCommand(this);
        new Utils(this);
    }
}
