package net.evatunadailyquest.salkcoding.main;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.anticheat.AntiCheating;
import net.evatunadailyquest.salkcoding.command.PluginCommand;
import net.evatunadailyquest.salkcoding.event.InventoryClick;
import net.evatunadailyquest.salkcoding.event.PlayerJoin;
import net.evatunadailyquest.salkcoding.questevent.*;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public Main() {
        if (instance != null)
            throw new IllegalStateException();
        instance = this;
    }

    @Override
    public void onEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockBreak(), this);
        manager.registerEvents(new BlockPlace(), this);
        manager.registerEvents(new KillEntity(), this);
        manager.registerEvents(new PickupItem(), this);
        manager.registerEvents(new PlayerWalk(), this);
        manager.registerEvents(new PlayTime(), this);
        manager.registerEvents(new InventoryClick(), this);
        manager.registerEvents(new PlayTime(), this);
        manager.registerEvents(new PlayerJoin(), this);
        manager.registerEvents(new PlayerShear(), this);
        //System.out.println(Constants.getAvailableSpaceForItem(Bukkit.getPlayer("Salk_Coding"), new ItemStack(Material.KELP)));
        Bukkit.getPluginCommand("DailyQuest").setExecutor(new PluginCommand());
        try {
            ScriptManager.loadScripts();
        } catch (IOException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            ScriptManager.addPlayerDailyQuests(player);
        }
        AntiCheating.load();
        System.out.println(Constants.Console_Format + "Now plugin is enabled");
    }

    @Override
    public void onDisable() {
        try {
            ScriptManager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AntiCheating.unload();
        System.out.println(Constants.Console_Format + "Now plugin is disabled");
    }

}
