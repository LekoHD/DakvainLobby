package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemType
{
    TELEPORTER("§3Teleporter", Material.WATCH, null),
    SURVIVAL(ChatColor.DARK_RED + "Survival", Material.BED, "§fHier gelangst du zu dem Survival Server"),
    CREATIVE(ChatColor.DARK_BLUE + "Creative", Material.HAY_BLOCK, "§fComing Soon..."),
    RPG(ChatColor.GOLD + "RPG", Material.IRON_SWORD, "§fComing Soon..."),
    HUB(ChatColor.AQUA + "HUB", Material.ENDER_PEARL, "§fHier gelangst du zu dem HUB"),
    MINIGAME(ChatColor.DARK_GREEN + "GreenZONE", Material.PRISMARINE, "§fComing Soon..."),
    HIDE_PLAYER(ChatColor.GREEN + "Verstecke alle Spieler", Material.BLAZE_ROD, "§fWenn du jetzt klickst, werden alle Spieler versteckt"),
    SHOW_PLAYER(ChatColor.RED + "Zeige alle Spieler", Material.STICK, "§fWenn du jetzt klickst, werden alle Spieler aufgedeckt"),
    FUN_CANON(ChatColor.MAGIC + "Fun Kanone", Material.FIREWORK, "§fBOOOM"),
    HARDCORE(ChatColor.DARK_RED + "Hardcore", Material.SKULL_ITEM, "§fHier gelangst du zu dem Hardcore Server"),
    LAUNCHER(ChatColor.YELLOW + "Launcher", Material.FEATHER, "§fSchiess dich selbst in die Luft");

    private String name;
    private Material type;
    private String lore;

    private ItemType(String name, Material type, String lore) {
        this.name = name;
        this.type = type;
        this.lore = lore; }

    public ItemStack getItem()
    {
        ItemStack i = new ItemStack(this.type);
        ItemMeta m = i.getItemMeta();
        if (this.lore != null) {
            ArrayList l = new ArrayList();
            l.add(this.lore);
            m.setLore(l);
        }
        m.setDisplayName(this.name);
        i.setItemMeta(m);
        if (this.type == RPG.getType()) {
            i = DamageLoreRemover.removeAttackDmg(i);
        }
        return i;
    }

    public Material getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}