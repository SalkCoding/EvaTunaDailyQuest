package net.evatunadailyquest.salkcoding.anticheat;

import net.evatunadailyquest.salkcoding.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AntiCheating {

    private static BukkitTask task;
    private static final HashMap<UUID, List<Location>> locationMap = new HashMap<>();

    public static boolean isUsed(Player player, Location location) {
        if (locationMap.containsKey(player.getUniqueId())) {
            List<Location> list = locationMap.get(player.getUniqueId());
            for (Location l : list) {
                if (l.equals(location))
                    return true;
            }
        }
        for (Entity entity : location.getWorld().getNearbyEntities(location, 10, 10, 10))
            if (entity instanceof Player) {
                if (entity.getEntityId() == player.getEntityId()) continue;
                return true;
            }
        return false;
    }

    public static void addLocation(Player player, Location location) {
        UUID uuid = player.getUniqueId();
        if (locationMap.containsKey(uuid)) {
            List<Location> list = locationMap.get(uuid);
            for (Location l : list) {
                if (l.equals(location))
                    return;
            }
            list.add(location);
        } else {
            List<Location> list = new ArrayList<>();
            list.add(location);
            locationMap.put(uuid, list);
        }
    }

    public static void removeLocation(Player player, Location location) {
        if (locationMap.containsKey(player.getUniqueId())) {
            List<Location> list = locationMap.get(player.getUniqueId());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(location))
                    list.remove(location);
            }
        }
    }

    public static void load() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), locationMap::clear, 6000, 6000);
    }

    public static void unload() {
        task.cancel();
    }

}
