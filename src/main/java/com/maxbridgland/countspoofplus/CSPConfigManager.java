package com.maxbridgland.countspoofplus;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSPConfigManager {

    CountSpoofPlusPlugin plugin;
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
    boolean enabled;

    public CSPConfigManager(CountSpoofPlusPlugin plugin){
        this.plugin = plugin;
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
        int configVersion = config.getInt("config_verison");
        if (configVersion != 1){
            logger.warning("Found Old or Missing Config File! Updating to New One. THIS WILL RESET YOUR CONFIGURATION!");
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
        enabled = config.getBoolean("settings.enabled");
        playerListEnabled = config.getBoolean("settings.customPlayerList.enabled");
        playerList = config.getStringList("settings.customPlayerList.messages");
    }

    public void flipEnabled(){
        enabled = !enabled;
        config.set("settings.enabled", enabled);
        plugin.saveConfig();
    }

    public void addToMinNumber(String type){
        switch (type){
            case "random":
                minimumRandomNumber += 1;
                if (minimumRandomNumber > maximumRandomNumber){
                    minimumRandomNumber -= 1;
                }
                config.set("settings.randomMode.minimumCount", minimumRandomNumber);
                plugin.saveConfig();
                break;
            case "realistic":
                minimumRealisticThreshold += 1;
                if (minimumRealisticThreshold > maximumRealisticThreshold){
                    minimumRealisticThreshold -= 1;
                }
                config.set("settings.realisticMode.minimumChange", minimumRealisticThreshold);
                plugin.saveConfig();
                break;
            default:
                break;
        }
    }

    public void subFromMinNumber(String type){
        switch (type){
            case "random":
                minimumRandomNumber -= 1;
                config.set("settings.randomMode.minimumCount", minimumRandomNumber);
                plugin.saveConfig();
                break;
            case "realistic":
                minimumRealisticThreshold -= 1;
                config.set("settings.realisticMode.minimumChange", minimumRealisticThreshold);
                plugin.saveConfig();
                break;
            default:
                break;
        }
    }

    public void addToMaxNumber(String type){
        switch (type){
            case "random":
                maximumRandomNumber += 1;
                config.set("settings.randomMode.maximumCount", minimumRandomNumber);
                plugin.saveConfig();
                break;
            case "realistic":
                maximumRealisticThreshold += 1;
                config.set("settings.realisticMode.maximumChange", minimumRealisticThreshold);
                plugin.saveConfig();
                break;
            default:
                break;
        }
    }

    public void subFromMaxNumber(String type){
        switch (type){
            case "random":
                maximumRandomNumber -= 1;
                if (minimumRandomNumber > maximumRandomNumber){
                    maximumRandomNumber += 1;
                }
                config.set("settings.randomMode.maximumCount", minimumRandomNumber);
                plugin.saveConfig();
                break;
            case "realistic":
                maximumRealisticThreshold -= 1;
                if (minimumRealisticThreshold > maximumRealisticThreshold){
                    maximumRealisticThreshold += 1;
                }
                config.set("settings.realisticMode.maximumChange", minimumRealisticThreshold);
                plugin.saveConfig();
                break;
            default:
                break;
        }
    }

    public void subFromStatic(){
        staticCountNumber -= 1;
        config.set("settings.staticMode.playerCount", staticCountNumber);
        plugin.saveConfig();
    }

    public void addToStatic(){
        staticCountNumber += 1;
        config.set("settings.staticMode.playerCount", staticCountNumber);
        plugin.saveConfig();
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
    public void setPlayerListDisabled(){
        playerListEnabled = false;
        config.set("settings.customPlayerList.enabled", false);
        plugin.saveConfig();
    }
    public void setPlayerListEnabled(){
        playerListEnabled = true;
        config.set("settings.customPlayerList.enabled", true);
        plugin.saveConfig();
    }


    public void setCurrentMode(String mode){
        currentMode = mode;
        config.set("settings.mode", mode);
        plugin.saveConfig();
        logger.info("Set Mode To: " + mode);
    }

}
