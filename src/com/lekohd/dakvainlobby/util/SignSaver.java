package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.io.File;

import com.lekohd.dakvainlobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class SignSaver extends YamlConfiguration
{
    private String name;
    private Main pl;

    public SignSaver()
    {
        this.pl = Main.getInstance();
        this.name = "Signs.yml";

        createFile();
    }

    private void createFile() {
        try {
            File file = new File(this.pl.getDataFolder(), this.name);
            if (!file.exists()) {
                if (Bukkit.getPlayer(this.name) != null)
                    if (this.pl.getResource(this.name) != null)
                        this.pl.saveResource(this.name, false);
                    else
                        save(file);
            }
            else
            {
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveLocation(String server, Location s) {
        int c = Main.counter;
        set("Signs." + c + ".server", server);
        set("Signs." + c + ".world", s.getWorld().getName());
        set("Signs." + c + ".x", Integer.valueOf(s.getBlockX()));
        set("Signs." + c + ".y", Integer.valueOf(s.getBlockY()));
        set("Signs." + c + ".z", Integer.valueOf(s.getBlockZ()));
        set("counter", Integer.valueOf(c++));
        Main.counter += 1;
        save();
    }

    public void save() {
        try {
            save(new File(this.pl.getDataFolder(), this.name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}