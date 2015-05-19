package com.lekohd.dakvainlobby.teleporter;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import com.lekohd.dakvainlobby.Main;
import com.lekohd.dakvainlobby.util.BungeeAPI;
import com.lekohd.dakvainlobby.util.ItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickHandler
        implements Listener
{
    private Main pl;

    public InventoryClickHandler(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {
            Player p = (Player)e.getWhoClicked();
            Material mat = e.getCurrentItem().getType();

            if (e.getInventory().getTitle().equals("§3Teleporter"))
                if (mat == ItemType.SURVIVAL.getType()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemType.SURVIVAL.getName())) {
                        BungeeAPI api = new BungeeAPI(this.pl);
                        api.connect(p, "survival");
                        p.updateInventory();
                        p.closeInventory();
                    }
                } else if (mat == ItemType.HARDCORE.getType()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemType.HARDCORE.getName())) {
                        BungeeAPI api = new BungeeAPI(this.pl);
                        api.connect(p, "hardcore");
                        p.updateInventory();
                        p.closeInventory();
                    }
                } else if (mat == ItemType.RPG.getType()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemType.RPG.getName()))
                    {
                        p.updateInventory();
                    }
                }
                else if (mat == ItemType.HUB.getType()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemType.HUB.getName())) {
                        p.teleport(new Location(p.getWorld(), 0.0D, 100.0D, 0.0D, 0.0F, 0.0F));
                        p.updateInventory();
                        p.closeInventory();
                    }
                } else if ((mat == ItemType.MINIGAME.getType()) &&
                        (e.getCurrentItem().getItemMeta().getDisplayName().equals(ItemType.MINIGAME.getName())))
                {
                    p.updateInventory();
                }
        }
        catch (Exception localException)
        {
        }
    }
}