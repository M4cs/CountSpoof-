package com.maxbridgland.countspoofplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CSPListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            if (e.getClick().equals(ClickType.NUMBER_KEY)){
                e.setCancelled(true);
            }
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) e.setCancelled(true);
            if (e.getView().getTitle().equals("" + ChatColor.GREEN + "CountSpoof+ Menu")){
                System.out.println("Item: " + clickedItem.getItemMeta().getDisplayName());
                if (e.getSlot() == 3 && clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.AQUA + "Mode Settings")){
                    Inventory modes = Bukkit.getServer().createInventory(player, 9, "" + ChatColor.LIGHT_PURPLE + "Mode Settings");
                    modes.setItem(1, createGuiItem(Material.END_CRYSTAL, "" + ChatColor.BLUE + "Random Mode", ""));
                    modes.setItem(3, createGuiItem(Material.SUNFLOWER, "" + ChatColor.BLUE + "Realistic Mode", ""));
                    modes.setItem(5, createGuiItem(Material.REDSTONE, "" + ChatColor.BLUE + "Static Mode", ""));
                    modes.setItem(7, createGuiItem(Material.OBSIDIAN, "" + ChatColor.BLUE + "Hidden Mode", ""));
                    player.closeInventory();
                    player.openInventory(modes);
                }
            }
        }
    }


    private ItemStack createGuiItem(Material material, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> metaLore = new ArrayList<String>();

        for(String loreComments : lore) {
            metaLore.add(loreComments);
        }

        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }


}
