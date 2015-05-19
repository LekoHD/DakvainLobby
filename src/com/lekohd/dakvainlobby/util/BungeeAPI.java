package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.lekohd.dakvainlobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BungeeAPI
{
    private Main pl;

    public BungeeAPI(Main instance)
    {
        this.pl = instance;
    }

    public void connect(Player p, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try
        {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.sendPluginMessage(this.pl, "BungeeCord", b.toByteArray());
    }

    public void sendMessage(Player p, String server, String msg) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try
        {
            out.writeUTF("Forward");
            out.writeUTF(server);
            out.writeUTF("DakvainLobby");
            out.writeShort(msg.length());
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.sendPluginMessage(this.pl, "BungeeCord", b.toByteArray());
    }

    public void callPlayerCount(String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("PlayerCount");
            out.writeUTF("pvp");
        } catch (IOException x) {
            x.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(this.pl, "BungeeCord", b.toByteArray());
    }
}