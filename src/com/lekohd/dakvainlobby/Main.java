package com.lekohd.dakvainlobby;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.lekohd.dakvainlobby.economy.Eco;
import com.lekohd.dakvainlobby.economy.MoneySaver;
import com.lekohd.dakvainlobby.loot.SpecialSign;
import com.lekohd.dakvainlobby.parkour.Parkour;
import com.lekohd.dakvainlobby.teleporter.InventoryClickHandler;
import com.lekohd.dakvainlobby.util.PlayerStatus;
import com.lekohd.dakvainlobby.util.SignSaver;
import com.lekohd.economysystem.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin
{
    public HashMap<Player, PlayerStatus> status = new HashMap();
    public HashMap<Player, Integer> gems = new HashMap();
    public HashMap<Player, Integer> coins = new HashMap();

    private FileConfiguration Location = null;
    private File LocationFile = null;
    private static Main instance;
    public static int counter;

    public void onEnable()
    {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ClickHandler(this), this);
        pm.registerEvents(new InventoryManager(this), this);
        pm.registerEvents(new InventoryClickHandler(this), this);
        pm.registerEvents(new Parkour(this), this);
        pm.registerEvents(new MoneySaver(this), this);
        pm.registerEvents(new SpecialSign(this), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        instance = this;

        SignSaver saver = new SignSaver();
        if (saver.contains("counter")) {
            counter = saver.getInt("counter");
        } else {
            counter = 0;
            saver.set("counter", Integer.valueOf(0));
        }
        saver.save();
    }

    public void onDisable()
    {
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if ((cmd.getName().equalsIgnoreCase("portal")) &&
                ((cs instanceof Player))) {
            Player localPlayer = (Player)cs;
        }

        return true;
    }

    public static Main getInstance() {
        return instance;
    }

    public void sendMsg(Player p, String text) {
        p.sendMessage("§7Dakvain §8§l>> §3" + text);
    }

    public void sendNoPermissionMsg(Player p) {
        sendMsg(p, "Du hast nicht die Erlaubniss, diesen Befehl zu benutzen");
    }

    public PlayerStatus getPlayerStatus(Player p) {
        if (p.hasPermission("Lobby.team"))
            return PlayerStatus.TEAM;
        if (p.hasPermission("Lobby.youtuber"))
            return PlayerStatus.YOUTUBER;
        if (p.hasPermission("Lobby.VIP")) {
            return PlayerStatus.VIP;
        }
        return PlayerStatus.NORMAL;
    }

    public void setScoreboard(Player p, PlayerStatus st) throws SQLException {
        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("MainBoard", "dummy");
        objective.setDisplayName(ChatColor.AQUA + "  DakvainMC  ");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Eco eco = new Eco(this);
        //int s = eco.getGems(p);
        int s = 0;
        s = EconomySystem.getGems(p);
        Score gems = objective.getScore(ChatColor.DARK_GREEN + "Gems: §f" + s);
        gems.setScore(3);

        Score empty = objective.getScore(" ");
        empty.setScore(4);

        Score e2 = objective.getScore("  ");
        e2.setScore(2);

        if (st == null) {
            Score status = objective.getScore(ChatColor.DARK_PURPLE + "Mode:" + getPlayerStatus(p).getName());
            status.setScore(1);
        } else {
            Score status = objective.getScore(ChatColor.DARK_PURPLE + "Mode:" + ((PlayerStatus)this.status.get(p)).getName());
            status.setScore(1);
        }

        Score coins = objective.getScore(ChatColor.GOLD + "Coins: §f" + EconomySystem.getCoins(p));
        coins.setScore(5);

        Score e3 = objective.getScore("   ");
        e3.setScore(6);

        p.setScoreboard(board);
    }

    public void reloadLocationFile()
    {
        if (this.LocationFile == null) {
            this.LocationFile = new File(getDataFolder(), "Location.yml");
        }
        this.Location = YamlConfiguration.loadConfiguration(this.LocationFile);
    }

    public FileConfiguration getLocationFile() {
        if (this.Location == null) {
            reloadLocationFile();
        }
        return this.Location;
    }

    public void saveLocationFile() {
        if ((this.Location == null) || (this.LocationFile == null))
            return;
        try
        {
            this.Location.save(this.LocationFile);
        } catch (IOException x) {
            Bukkit.getServer().getLogger().info("Dakvain >> Location konnte nicht gespeichert werden!");
        }
    }
}