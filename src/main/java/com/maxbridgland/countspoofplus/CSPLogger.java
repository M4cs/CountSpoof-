package com.maxbridgland.countspoofplus;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class CSPLogger {

    ConsoleCommandSender console;

    public CSPLogger(CountSpoofPlusPlugin plugin){
        console = plugin.getServer().getConsoleSender();
    }

    public void info(String msg){
        console.sendMessage(ChatColor.DARK_GREEN + "[CountSpoof+:INFO] " + msg);
    }

    public void warning(String msg){
        console.sendMessage(ChatColor.GOLD + "[CountSpoof+:WARN] " +  msg);
    }

    public void success(String msg){
        console.sendMessage(ChatColor.GREEN + "[CountSpoof+:SUCCESS] " +  msg);
    }

    public void error(String msg){
        console.sendMessage(ChatColor.DARK_RED + "[CountSpoof+:ERROR] " + msg);
    }

}
