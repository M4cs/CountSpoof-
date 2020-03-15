package com.maxbridgland.countspoofplus;

import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CSPSpoofer {

    CSPConfigManager configManager;
    int currentCount;
    int currentThreshold;

    public CSPSpoofer(CSPConfigManager configManager){
        this.configManager = configManager;
        currentCount = Bukkit.getServer().getOnlinePlayers().size();
        updateCount();
    }

    public int getCount(){
        return currentCount;
    }

    public void updateCount(){
        System.out.println("Current Mode: " + configManager.getCurrentMode());
        if (configManager.getCurrentMode().equalsIgnoreCase("realistic")){
            System.out.println("In Realistic");
            Random ran = new Random();
            int coinFlip = ran.nextInt(100);
            int countChange = ThreadLocalRandom.current().nextInt(configManager.getMinimumRealisticThreshold(), configManager.getMaximumRealisticThreshold() + 1);
            if (coinFlip >= 50){
                currentCount = currentCount + countChange;
            } else {
                currentCount = currentCount - countChange;
            }
            if (currentCount < configManager.minimumRealisticNumber){
                currentCount = configManager.minimumRealisticNumber;
            }
        } else if (configManager.getCurrentMode().equalsIgnoreCase("random")){
            System.out.println("In Random");
            System.out.println(ThreadLocalRandom.current().nextInt(configManager.getMinimumRandomNumber(), configManager.getMaximumRandomNumber() + 1));
            currentCount = ThreadLocalRandom.current().nextInt(configManager.getMinimumRandomNumber(), configManager.getMaximumRandomNumber() + 1);
        } else if (configManager.getCurrentMode().equalsIgnoreCase("static")){
            currentCount = configManager.getStaticCountNumber();
        }
        System.out.println("The Current Count:" + currentCount);
    }

    public int getCurrentThreshold() {
        return currentThreshold;
    }

    public void resetCurrentThreshold(){
        currentThreshold = 0;
    }

    public int getCurrentCount(){
        return currentCount;
    }

    public void addToCurrentThreshold(){
        currentThreshold += 1;
    }

}
