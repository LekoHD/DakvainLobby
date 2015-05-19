package com.lekohd.dakvainlobby;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import com.lekohd.dakvainlobby.util.PlayerStatus;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class ClickHandler
        implements Listener
{
    private Main pl;

    public ClickHandler(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFall(PlayerMoveEvent e)
    {
        try {
            Player p = e.getPlayer();
            if (this.pl.status.get(p) != PlayerStatus.PARKOUR) {
                if (p.getLocation().getBlockY() <= 36) {
                    p.setVelocity(new Vector(0, 4, 0));
                    p.setFallDistance(0.0F);
                }
            }
            else if (p.getLocation().getBlockY() <= 36) {
                p.teleport(new Location(p.getWorld(), 6.0D, 98.0D, -38.0D, -155.0F, 0.0F));
                this.pl.sendMsg(p, "Du befindest dich nicht mehr im Parkour Modus");
                this.pl.setScoreboard(p, null);
                p.setFallDistance(0.0F);
                this.pl.status.put(p, this.pl.getPlayerStatus(p));
            }
        }
        catch (Exception localException)
        {
        }
    }
}