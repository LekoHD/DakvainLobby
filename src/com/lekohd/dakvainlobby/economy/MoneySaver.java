package com.lekohd.dakvainlobby.economy;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import com.lekohd.dakvainlobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.sql.SQLException;

public class MoneySaver
        implements Listener
{
    private Main pl;

    public MoneySaver(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Eco eco = new Eco(this.pl);
        eco.loadEco(e.getPlayer());
        try {
            this.pl.setScoreboard(e.getPlayer(), null);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Eco eco = new Eco(this.pl);
        eco.saveEco(e.getPlayer());
    }

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Eco eco = new Eco(this.pl);
            eco.saveEco(p);
        }
    }

    @EventHandler
    public void onEnable(PluginEnableEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Eco eco = new Eco(this.pl);
            eco.loadEco(p);
            try {
                this.pl.setScoreboard(p, null);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
