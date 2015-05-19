package com.lekohd.dakvainlobby.parkour;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lekohd.dakvainlobby.Main;
import com.lekohd.dakvainlobby.economy.Eco;
import com.lekohd.dakvainlobby.util.PlayerStatus;
import com.lekohd.economysystem.EconomySystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Parkour
        implements Listener
{
    private ArrayList<Player> temp = new ArrayList();
    private Main pl;

    public Parkour(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if ((p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.EMERALD_BLOCK) &&
                (this.pl.status.get(p) != PlayerStatus.PARKOUR)) {
            this.pl.status.put(p, PlayerStatus.PARKOUR);
            try {
                this.pl.setScoreboard(p, PlayerStatus.PARKOUR);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            this.pl.sendMsg(p, "Du befindest dich nun im Parkour Modus");
        }

        if ((p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.DIAMOND_BLOCK) &&
                (this.pl.status.get(p) == PlayerStatus.PARKOUR) &&
                (!this.temp.contains(p))) {
            this.pl.sendMsg(p, "Du hast erfolgreich ein Level abgeschlossen");
            this.temp.add(p);
            new BukkitRunnable()
            {
                public void run()
                {
                    Parkour.this.temp.remove(p);
                }
            }
                    .runTaskLaterAsynchronously(this.pl, 100L);
        }

        if ((p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SEA_LANTERN) &&
                (this.pl.status.get(p) == PlayerStatus.PARKOUR) &&
                (!this.temp.contains(p))) {
            this.temp.add(p);
            this.pl.sendMsg(p, "Du hast erfolgreich den Parkour abgeschlossen");
            this.pl.sendMsg(p, "Hier ist deine Belohnung: 400 Gems");
            //Eco eco = new Eco(this.pl);
            //eco.addGems(p, 400);
            EconomySystem.addGems(p, 400);
            p.teleport(new Location(p.getWorld(), 6.0D, 98.0D, -38.0D, -155.0F, 0.0F));
            this.pl.sendMsg(p, "Du befindest dich nicht mehr im Parkour Modus");
            p.setFallDistance(0.0F);
            this.pl.status.put(p, this.pl.getPlayerStatus(p));
            try {
                this.pl.setScoreboard(p, null);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            new BukkitRunnable()
            {
                public void run()
                {
                    Parkour.this.temp.remove(p);
                }
            }
                    .runTaskLaterAsynchronously(this.pl, 100L);
        }
    }
}