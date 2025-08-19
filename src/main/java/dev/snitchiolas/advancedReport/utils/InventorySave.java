package dev.snitchiolas.advancedReport.utils;

import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codehaus.plexus.util.FastMap;

public class InventorySave {
    private static final Map<String, ItemStack[]> inventory = new FastMap<>();
    private static final Map<String, ItemStack[]> armor = new FastMap<>();

    public static void saveInventory(Player p) {
        inventory.put(String.valueOf(p.getUniqueId()), p.getInventory().getContents());
        armor.put(String.valueOf(p.getUniqueId()), p.getInventory().getArmorContents());
    }

    public static void saveAndClearInventory(Player p) {
        saveInventory(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.updateInventory();
    }

    public static ItemStack[] getInventory(Player p) {
        return inventory.get(String.valueOf(p.getUniqueId()));
    }

    public static ItemStack[] getArmor(Player p) {
        return armor.get(String.valueOf(p.getUniqueId()));
    }

    public static void removeInventory(Player p) {
        inventory.remove(String.valueOf(p.getUniqueId()));
        armor.remove(String.valueOf(p.getUniqueId()));
    }

    public static void loadInventory(Player p) {
        ItemStack[] inv = inventory.get(String.valueOf(p.getUniqueId()));
        ItemStack[] arm = armor.get(String.valueOf(p.getUniqueId()));

        if (inv != null) {
            p.getInventory().setContents(inv);
        }
        if (arm != null) {
            p.getInventory().setArmorContents(arm);
        }
        p.updateInventory();
    }
}
