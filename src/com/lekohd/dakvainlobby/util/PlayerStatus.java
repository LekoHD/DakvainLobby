package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import org.bukkit.ChatColor;

public enum PlayerStatus
{
    PARKOUR(ChatColor.DARK_AQUA + "Parkour"), NORMAL(ChatColor.GREEN + "Normal"), TEAM(ChatColor.RED + "Team"), YOUTUBER(ChatColor.GOLD + "Youtuber"), VIP(ChatColor.BLUE + "VIP");

    private String name;

    private PlayerStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}