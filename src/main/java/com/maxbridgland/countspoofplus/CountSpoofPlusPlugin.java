package com.maxbridgland.countspoofplus;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.gson.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CountSpoofPlusPlugin extends JavaPlugin {

    CSPConfigManager configManager;
    CSPSpoofer spoofer;
    CSPLogger logger;

    @Override
    public void onEnable(){
        boolean hasProtocolLib;
        logger = new CSPLogger(this);
        if (getConfig().getBoolean("settings.enabled")){
            try {
                Class cls = Class.forName("com.comphenix.protocol.ProtocolLibrary");
                hasProtocolLib = true;
            } catch (ClassNotFoundException e) {
                logger.error("Couldn't find ProtocolLib. Do you have it installed on the server? Disabling CountSpoof+");
                hasProtocolLib = false;
            }
            if (hasProtocolLib){
                configManager = new CSPConfigManager(this);
                spoofer = new CSPSpoofer(configManager);
                this.getCommand("csp").setExecutor(new CSPCommand(configManager));
                getServer().getPluginManager().registerEvents(new CSPListener(this), this);
                logger.info("Starting CountSpoof+ Spoofer in " + configManager.getCurrentMode());
                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                        Arrays.asList(PacketType.Status.Server.SERVER_INFO), ListenerOptions.ASYNC) {
                    private Class<?> SERVER_PING = MinecraftReflection.getServerPingClass();
                    private FieldAccessor PLAYERS = Accessors.getFieldAccessor(SERVER_PING,
                            MinecraftReflection.getServerPingPlayerSampleClass(), true);

                    @Override
                    public void onPacketSending(PacketEvent e){
                        if (configManager.enabled) {
                            if (e.getPacketType().equals(PacketType.Status.Server.SERVER_INFO)){
                                if (e.getPacket() != null){
                                    if (e.getPacket().getServerPings() != null){
                                        if (configManager.getPacketThreshold() != -1){
                                            spoofer.addToCurrentThreshold();
                                            if (spoofer.getCurrentThreshold() >= configManager.getPacketThreshold()){
                                                spoofer.updateCount();
                                                spoofer.resetCurrentThreshold();
                                            }
                                        } else {
                                            spoofer.updateCount();
                                        }
                                        WrappedServerPing ping = e.getPacket().getServerPings().read(0);
                                        JsonObject newPing = new JsonObject();
                                        JsonParser parser = new JsonParser();
                                        JsonObject pingObj = parser.parse(ping.toJson()).getAsJsonObject();
                                        if (pingObj.has("favicon")){
                                            newPing.addProperty("favicon", pingObj.get("favicon").getAsString());
                                        }
                                        JsonObject descriptionObject = pingObj.get("description").getAsJsonObject();
                                        JsonObject protocolObject = pingObj.get("version").getAsJsonObject();
                                        newPing.add("version", protocolObject);
                                        newPing.add("description", descriptionObject);
                                        JsonObject playerObj = new JsonObject();
                                        if (configManager.getPlayerListEnabled()){
                                            JsonArray sampleArray = new JsonArray();
                                            for (String msg : configManager.getPlayerList()){
                                                String sample = "{\"id\":\"" + UUID.randomUUID() + "\",\"name\":\""+msg.replace("&", "§") +"\"}";
                                                sampleArray.add(parser.parse(sample));
                                            }
                                            playerObj.add("sample", sampleArray);
                                        }
                                        int playersMax = pingObj.get("players").getAsJsonObject().get("max").getAsInt();
                                        playerObj.addProperty("max", playersMax);
                                        playerObj.addProperty("online", spoofer.getCurrentCount());
                                        newPing.add("players", playerObj);
                                        e.getPacket().getServerPings().write(0, WrappedServerPing.fromJson(newPing.toString()));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
