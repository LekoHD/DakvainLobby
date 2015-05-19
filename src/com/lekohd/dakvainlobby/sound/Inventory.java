package com.lekohd.dakvainlobby.sound;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Inventory
{
    private static org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 54, "§3Wähle Song");

    public static void open(Player p)
    {
        p.openInventory(inv);
    }
}
