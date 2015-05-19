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
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{
    private String name;
    private Main pl;

    public Config(String name, Main pl)
    {
        this.pl = pl;
        this.name = ("Data" + File.separator + name + ".yml");

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

    public void save() {
        try {
            save(new File(this.pl.getDataFolder(), this.name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
