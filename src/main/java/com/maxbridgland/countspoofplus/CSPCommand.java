package com.maxbridgland.countspoofplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CSPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            Inventory cspMenu = Bukkit.getServer().createInventory(player, 9, "" + ChatColor.DARK_GREEN + "CountSpoof+ Menu");
            cspMenu.setItem(3, createGuiItem(Material.BEACON, "" + ChatColor.AQUA + "Mode Settings", "" + ChatColor.DARK_AQUA + "Choose Your Spoofing Mode", "" + ChatColor.GREEN + "Options: Static, Random, Realistic, Hidden"));
            cspMenu.setItem(5, createGuiItem(Material.ENCHANTED_BOOK, "" + ChatColor.AQUA + "Config Settings", "" + ChatColor.DARK_AQUA + "Change Configuration Settings", ""));
            cspMenu.setItem(4, createGuiItem(Material.FEATHER, "" + ChatColor.AQUA + "Player List Settings", "" + ChatColor.DARK_AQUA + "Change Player List Messages", ""));
            player.openInventory(cspMenu);
        }
        return true;
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
