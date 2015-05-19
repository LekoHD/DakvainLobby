package com.lekohd.dakvainlobby.economy;

import java.sql.SQLException;
import java.util.HashMap;

import com.lekohd.dakvainlobby.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class Eco
{
    private Main pl;

    public Eco(Main pl)
    {
        this.pl = pl;
    }

    public void addGems(Player p, int amount) {
        int temp = ((Integer)this.pl.gems.get(p)).intValue();
        this.pl.gems.put(p, Integer.valueOf(temp + amount));
        try {
            this.pl.setScoreboard(p, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGems(Player p) {
        return ((Integer)this.pl.gems.get(p)).intValue();
    }

    public boolean removeGems(Player p, int amount) {
        int temp = ((Integer)this.pl.gems.get(p)).intValue();
        if (temp < amount) {
            return false;
        }
        this.pl.gems.put(p, Integer.valueOf(temp - amount));
        try {
            this.pl.setScoreboard(p, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void saveEco(Player p)
    {
        this.pl.getConfig().set("Gems." + p.getName(), this.pl.gems.get(p));
        this.pl.getConfig().set("Coins." + p.getName(), this.pl.coins.get(p));
        this.pl.saveConfig();
    }

    public void loadEco(Player p) {
        this.pl.gems.put(p, Integer.valueOf(this.pl.getConfig().getInt("Gems." + p.getName())));
        this.pl.coins.put(p, Integer.valueOf(this.pl.getConfig().getInt("Coins." + p.getName())));
        this.pl.saveConfig();
    }

    public void addCoins(Player p, int amount) {
        int temp = ((Integer)this.pl.coins.get(p)).intValue();
        this.pl.coins.put(p, Integer.valueOf(temp + amount));
        try {
            this.pl.setScoreboard(p, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCoins(Player p) {
        return ((Integer)this.pl.coins.get(p)).intValue();
    }

    public boolean removeCoins(Player p, int amount) {
        int temp = ((Integer)this.pl.coins.get(p)).intValue();
        if (temp < amount) {
            return false;
        }
        this.pl.coins.put(p, Integer.valueOf(temp - amount));
        try {
            this.pl.setScoreboard(p, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
