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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CSPListener implements Listener {

    CSPConfigManager configManager;
    CSPSpoofer spoofer;

    public CSPListener(CountSpoofPlusPlugin plugin){
        this.configManager = plugin.configManager;
        this.spoofer = plugin.spoofer;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            if (e.getClick().equals(ClickType.NUMBER_KEY)){
                e.setCancelled(true);
            }
            ItemStack clickedItem = e.getCurrentItem();
            if (!(clickedItem == null || clickedItem.getType() == Material.AIR)){
                if (e.getWhoClicked().getOpenInventory().getTitle().contains("CountSpoof+ Menu")){
                    if (e.getSlot() == 3 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.AQUA + "Mode Settings"))){
                        player.closeInventory();
                        Inventory modes = Bukkit.getServer().createInventory(player, 18, "" + ChatColor.LIGHT_PURPLE + "Mode Settings");
                        modes.setItem(1, createGuiItem(Material.END_CRYSTAL, "" + ChatColor.BLUE + "Random Mode", "Random Count between two set numbers in config"));
                        modes.setItem(3, createGuiItem(Material.DIAMOND, "" + ChatColor.GREEN + "Realistic Mode", "Realistic Player Count", "Uses Algorithm to give off realistic count"));
                        modes.setItem(5, createGuiItem(Material.REDSTONE, "" + ChatColor.DARK_PURPLE + "Static Mode", "Static Player Count"));
                        if (configManager.enabled){
                            modes.setItem(7, createGuiItem(Material.OBSIDIAN, "" + ChatColor.AQUA + "Enable/Disable Spoofing", "Current Setting:", "" + ChatColor.GREEN + "Enabled"));
                        } else {
                            modes.setItem(7, createGuiItem(Material.OBSIDIAN, "" + ChatColor.AQUA + "Enable/Disable Spoofing", "Current Setting:", "" + ChatColor.DARK_RED + "Disabled"));
                        }
                        modes.setItem(13, createGuiItem(Material.BARRIER, "" + ChatColor.RED + "Go Back", "Back to Main Menu"));
                        player.openInventory(modes);
                    }
                    if (e.getSlot() == 4 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.AQUA + "Enable/Disable Player List"))){
                        if (configManager.getPlayerListEnabled()){
                            configManager.setPlayerListDisabled();
                            e.getWhoClicked().getOpenInventory().setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.DARK_RED + "Disabled"));
                        } else {
                            configManager.setPlayerListEnabled();
                            e.getWhoClicked().getOpenInventory().setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.GREEN + "Enabled"));
                        }
                        player.updateInventory();
                    }
                    if (e.getSlot() == 5 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.AQUA + "Config Settings"))){
                        player.closeInventory();
                        openConfigInventory(player);
                    }
                    e.setCancelled(true);
                }
                if (e.getWhoClicked().getOpenInventory().getTitle().contains("CS+ Configuration Editor")){
                    e.setCancelled(true);
                    if (e.getSlot() == 9 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Increase Random Min"))){
                        configManager.addToMinNumber("random");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 18 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Decrease Random Min"))){
                        configManager.subFromMinNumber("random");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 11 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Increase Random Max"))){
                        configManager.addToMaxNumber("random");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 20 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Decrease Random Max"))){
                        configManager.subFromMaxNumber("random");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 13 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Increase Static Count"))){
                        configManager.addToStatic();
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 22 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Decrease Static Count"))){
                        configManager.subFromStatic();
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 15 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Increase Realistic Min"))){
                        configManager.addToMinNumber("realistic");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 24 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Decrease Realistic Min"))){
                        configManager.subFromMinNumber("realistic");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 17 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Increase Realistic Max"))){
                        configManager.addToMaxNumber("realistic");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 26 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Decrease Realistic Max"))){
                        configManager.subFromMaxNumber("realistic");
                        openConfigInventory(player);
                    }
                    if (e.getSlot() == 49 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Go Back"))){
                        player.closeInventory();
                        Inventory cspMenu = Bukkit.getServer().createInventory(player, 9, "" + ChatColor.DARK_GREEN + "CountSpoof+ Menu");
                        cspMenu.setItem(3, createGuiItem(Material.BEACON, "" + ChatColor.AQUA + "Mode Settings", "" + ChatColor.DARK_AQUA + "Choose Your Spoofing Mode", "" + ChatColor.GREEN + "Options: Static, Random, Realistic, Hidden"));
                        cspMenu.setItem(5, createGuiItem(Material.ENCHANTED_BOOK, "" + ChatColor.AQUA + "Config Settings", "" + ChatColor.DARK_AQUA + "Change Configuration Settings", ""));
                        if (configManager.getPlayerListEnabled()){
                            cspMenu.setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.GREEN + "Enabled"));
                        } else {
                            cspMenu.setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.DARK_RED + "Disabled"));
                        }
                        player.openInventory(cspMenu);
                    }
                }
                if (e.getWhoClicked().getOpenInventory().getTitle().contains("Mode Settings")){
                    e.setCancelled(true);
                    if (e.getSlot() == 1 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.BLUE + "Random Mode"))){
                        configManager.setCurrentMode("random");
                        spoofer.updateCount();
                        player.sendMessage("" + ChatColor.GREEN + "[CS+] Set Mode To Random");
                    }
                    if (e.getSlot() == 3 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.GREEN + "Realistic Mode"))){
                        configManager.setCurrentMode("realistic");
                        spoofer.updateCount();
                        player.sendMessage("" + ChatColor.GREEN + "[CS+] Set Mode To Realistic");
                    }
                    if (e.getSlot() == 5 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.DARK_PURPLE + "Static Mode"))){
                        configManager.setCurrentMode("static");spoofer.updateCount();
                        player.sendMessage("" + ChatColor.GREEN + "[CS+] Set Mode To Static");
                    }
                    if (e.getSlot() == 7 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.AQUA + "Enable/Disable Spoofing"))){
                        configManager.flipEnabled();
                        if (configManager.enabled){
                            e.getWhoClicked().getOpenInventory().setItem(7, createGuiItem(Material.OBSIDIAN, "" + ChatColor.AQUA + "Enable/Disable Spoofing", "Current Setting:", "" + ChatColor.GREEN + "Enabled"));
                            player.sendMessage("" + ChatColor.GREEN + "[CS+] Enabled Spoofing");
                        } else {
                            e.getWhoClicked().getOpenInventory().setItem(7, createGuiItem(Material.OBSIDIAN, "" + ChatColor.AQUA + "Enable/Disable Spoofing", "Current Setting:", "" + ChatColor.DARK_RED + "Disabled"));
                            player.sendMessage("" + ChatColor.DARK_RED + "[CS+] Disabled Spoofing");
                        }
                        player.updateInventory();
                    }
                    if (e.getSlot() == 13 && (clickedItem.getItemMeta().getDisplayName().equals("" + ChatColor.RED + "Go Back"))){
                        player.closeInventory();
                        Inventory cspMenu = Bukkit.getServer().createInventory(player, 9, "" + ChatColor.DARK_GREEN + "CountSpoof+ Menu");
                        cspMenu.setItem(3, createGuiItem(Material.BEACON, "" + ChatColor.AQUA + "Mode Settings", "" + ChatColor.DARK_AQUA + "Choose Your Spoofing Mode", "" + ChatColor.GREEN + "Options: Static, Random, Realistic, Hidden"));
                        cspMenu.setItem(5, createGuiItem(Material.ENCHANTED_BOOK, "" + ChatColor.AQUA + "Config Settings", "" + ChatColor.DARK_AQUA + "Change Configuration Settings", ""));
                        if (configManager.getPlayerListEnabled()){
                            cspMenu.setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.GREEN + "Enabled"));
                        } else {
                            cspMenu.setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Enable/Disable Player List", "" + ChatColor.DARK_AQUA + "Current Setting:", "" + ChatColor.DARK_RED + "Disabled"));
                        }
                        player.openInventory(cspMenu);
                    }
                }
            }
        }
    }

    private void openConfigInventory(Player player){
        Inventory configSettings = Bukkit.getServer().createInventory(player, 54, "" + ChatColor.BLUE + "CS+ Configuration Editor");
        configSettings.setItem(1, createGuiItem(Material.END_CRYSTAL, "" + ChatColor.BLUE + "Random Settings", "" + ChatColor.AQUA + "Current Max: " + configManager.getMaximumRandomNumber(), "" + ChatColor.AQUA + "Current Min: " + configManager.getMinimumRandomNumber()));
        configSettings.setItem(9, createGuiItem(Material.EMERALD_BLOCK, "" + ChatColor.GREEN + "Increase Random Min"));
        configSettings.setItem(18, createGuiItem(Material.REDSTONE_BLOCK, "" + ChatColor.RED + "Decrease Random Min"));
        configSettings.setItem(11, createGuiItem(Material.EMERALD_BLOCK, "" + ChatColor.GREEN + "Increase Random Max"));
        configSettings.setItem(20, createGuiItem(Material.REDSTONE_BLOCK, "" + ChatColor.RED + "Decrease Random Max"));
        configSettings.setItem(7, createGuiItem(Material.DIAMOND, "" + ChatColor.GREEN + "Realistic Settings", "" + ChatColor.DARK_GREEN + "Current Max: " + configManager.getMaximumRealisticThreshold(), "" + ChatColor.DARK_GREEN + "Current Min: " + configManager.getMinimumRealisticThreshold()));
        configSettings.setItem(15, createGuiItem(Material.EMERALD_BLOCK, "" + ChatColor.GREEN + "Increase Realistic Min"));
        configSettings.setItem(24, createGuiItem(Material.REDSTONE_BLOCK, "" + ChatColor.RED + "Decrease Realistic Min"));
        configSettings.setItem(17, createGuiItem(Material.EMERALD_BLOCK, "" + ChatColor.GREEN + "Increase Realistic Max"));
        configSettings.setItem(26, createGuiItem(Material.REDSTONE_BLOCK, "" + ChatColor.RED + "Decrease Realistic Max"));
        configSettings.setItem(4, createGuiItem(Material.REDSTONE, "" + ChatColor.DARK_PURPLE + "Static Settings", "" + ChatColor.LIGHT_PURPLE + "Current Number: " + configManager.getStaticCountNumber()));
        configSettings.setItem(13, createGuiItem(Material.EMERALD_BLOCK, "" + ChatColor.GREEN + "Increase Static Count"));
        configSettings.setItem(22, createGuiItem(Material.REDSTONE_BLOCK, "" + ChatColor.RED + "Decrease Static Count"));
        configSettings.setItem(49, createGuiItem(Material.BARRIER, "" + ChatColor.RED + "Go Back"));
        player.openInventory(configSettings);
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
