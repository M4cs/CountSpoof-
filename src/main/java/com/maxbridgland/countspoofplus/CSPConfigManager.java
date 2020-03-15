package com.maxbridgland.countspoofplus;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class CSPConfigManager {

    FileConfiguration config;
    int packetThreshold = -1;
    String currentMode;
    int staticCountNumber;
    int minimumRandomNumber;
    int maximumRandomNumber;
    int minimumRealisticNumber;
    int minimumRealisticThreshold;
    int maximumRealisticThreshold;
    int currentThreshold;
    boolean playerListEnabled;
    List<String> image64s;
    List<String> playerList;
    CSPLogger logger;

    public CSPConfigManager(CountSpoofPlusPlugin plugin){
        this.config = plugin.getConfig();
        this.logger = plugin.logger;
        this.image64s = new ArrayList<>();
        init(plugin);
    }

    private void init(CountSpoofPlusPlugin plugin){
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()){
            plugin.saveDefaultConfig();
        }

        packetThreshold = config.getInt("settings.packetThreshold");
        staticCountNumber = config.getInt("settings.staticMode.playerCount");
        minimumRandomNumber = config.getInt("settings.randomMode.minimumCount");
        maximumRandomNumber = config.getInt("settings.randomMode.maximumCount");
        minimumRealisticNumber = config.getInt("settings.realisticMode.minimumPlayers");
        minimumRealisticThreshold = config.getInt("settings.realisticMode.minimumChange");
        maximumRealisticThreshold = config.getInt("settings.realisticMode.maximumChange");
        currentMode = config.getString("settings.mode");
        if (currentMode == null){
            currentMode = "random";
        }
        playerListEnabled = config.getBoolean("settings.customPlayerList.enabled");
        playerList = config.getStringList("settings.customPlayerList.messages");
    }

    public List<String> getPlayerList(){
        return playerList;
    }

    public String getCurrentMode(){
        return currentMode;
    }

    public boolean getPlayerListEnabled(){
        return playerListEnabled;
    }

    public int getStaticCountNumber(){
        return staticCountNumber;
    }

    public int getPacketThreshold(){
        return packetThreshold;
    }

    public int getMinimumRandomNumber(){
        return minimumRandomNumber;
    }
    public int getMaximumRandomNumber(){
        return maximumRandomNumber;
    }
    public int getMinimumRealisticNumber(){
        return minimumRealisticNumber;
    }
    public int getMinimumRealisticThreshold(){
        return minimumRealisticThreshold;
    }
    public int getMaximumRealisticThreshold(){
        return maximumRealisticThreshold;
    }
    public int getCurrentThreshold(){
        return currentThreshold;
    }

}
