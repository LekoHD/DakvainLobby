package com.lekohd.dakvainlobby.loot;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.util.ArrayList;

import com.lekohd.dakvainlobby.Main;
import com.lekohd.dakvainlobby.economy.Eco;
import com.lekohd.dakvainlobby.util.Config;
import com.lekohd.economysystem.EconomySystem;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpecialSign
        implements Listener
{
    private Main pl;

    public SpecialSign(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).contains("[Loot]")) {
            int amount = Integer.valueOf(e.getLine(1)).intValue();
            int temp = 0;
            if (this.pl.getConfig().contains("Loot")) {
                temp = this.pl.getConfig().getInt("Loot");
            }
            String[] lines = new String[4];
            lines[0] = "§3[Loot]";
            lines[1] = (ChatColor.GOLD + "Coins: " + amount);
            lines[2] = amount + "";
            lines[3] = (temp + 1 + "");
            for (int i = 0; i < 4; i++) {
                e.setLine(i, lines[i]);
            }
            this.pl.getConfig().set("Loot", Integer.valueOf(temp + 1));
            this.pl.saveConfig();
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        try
        {
            Player p = e.getPlayer();
            Config file = new Config(p.getName(), this.pl);
            if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                    (e.getClickedBlock().getState() != null) &&
                    ((e.getClickedBlock().getState() instanceof Sign))) {
                Sign s = (Sign)e.getClickedBlock().getState();

                if (s.getLine(0).contains("[Loot]")) {
                    int id = Integer.valueOf(s.getLine(3)).intValue();
                    ArrayList x = null;
                    if (file.contains("Clicked"))
                        x = (ArrayList)file.getList("Clicked");
                    else {
                        x = new ArrayList();
                    }
                    if (x.contains(Integer.valueOf(id))) {
                        e.setCancelled(true);
                        this.pl.sendMsg(p, "Du hast diese Belohnung bereits eingesammelt");
                    } else {
                        x.add(Integer.valueOf(id));
                        file.set("Clicked", x);
                        //Eco eco = new Eco(this.pl);
                        if (s.getLine(0).contains("[Loot]")) {
                            int amount = Integer.valueOf(s.getLine(2)).intValue();
                            //eco.addCoins(p, amount);
                            EconomySystem.addCoins(p, amount);
                            this.pl.sendMsg(p, "Du hast soeben " + amount + " Gems eingesammelt");
                            this.pl.setScoreboard(p, null);
                            file.save();
                        }
                    }
                }
            }

        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }
}
