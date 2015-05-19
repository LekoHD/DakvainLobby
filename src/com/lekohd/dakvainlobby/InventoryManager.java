package com.lekohd.dakvainlobby;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.util.HashMap;
import java.util.List;

import com.lekohd.dakvainlobby.util.Effekt;
import com.lekohd.dakvainlobby.util.ItemType;
import com.lekohd.dakvainlobby.util.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

public class InventoryManager
        implements Listener
{
    private HashMap<Player, Long> cd = new HashMap();
    private HashMap<Player, Long> fun = new HashMap();
    private Main pl;

    public InventoryManager(Main pl)
    {
        this.pl = pl;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        this.pl.status.put(p, this.pl.getPlayerStatus(p));
        p.getInventory().clear();
        p.getInventory().setItem(0, ItemType.TELEPORTER.getItem());
        p.getInventory().setItem(8, ItemType.HIDE_PLAYER.getItem());
        p.getInventory().setItem(7, ItemType.LAUNCHER.getItem());

        Location loc = new Location(p.getWorld(), -12.0D, 111.0D, 13.0D);
        Chest chest = (Chest)loc.getBlock().getState();
        ItemStack i = chest.getInventory().getItem(3).clone();
        p.getInventory().setItem(4, i); // Chest muss an bestimmter position sein
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
                if (e.getItem().getType() == ItemType.TELEPORTER.getType()) {
                    if (e.getItem().getItemMeta().getDisplayName().equals(ItemType.TELEPORTER.getName())) {
                        Inventory inv = Bukkit.createInventory(p, 27, "§3Teleporter");
                        inv.setItem(2, ItemType.SURVIVAL.getItem());
                        inv.setItem(20, ItemType.HARDCORE.getItem());
                        inv.setItem(24, ItemType.RPG.getItem());
                        inv.setItem(13, ItemType.HUB.getItem());
                        inv.setItem(6, ItemType.MINIGAME.getItem());
                        p.playSound(p.getLocation(), Sound.GLASS, 30.0F, 30.0F);
                        p.openInventory(inv);
                    }
                } else if (e.getItem().getType() == ItemType.HIDE_PLAYER.getType()) {
                    if (e.getItem().getItemMeta().getDisplayName().equals(ItemType.HIDE_PLAYER.getName()))
                        if (this.cd.containsKey(p)) {
                            if (((Long)this.cd.get(p)).longValue() + 3000L <= System.currentTimeMillis()) {
                                hidePlayers(p);
                                p.getInventory().remove(ItemType.HIDE_PLAYER.getType());
                                p.getInventory().setItem(8, ItemType.SHOW_PLAYER.getItem());
                                this.cd.put(p, Long.valueOf(System.currentTimeMillis()));
                            } else {
                                this.pl.sendMsg(p, "Bitte warte etwas, bevor du dieses Item erneut nutzt");
                            }
                        } else {
                            hidePlayers(p);
                            p.getInventory().remove(ItemType.HIDE_PLAYER.getType());
                            p.getInventory().setItem(8, ItemType.SHOW_PLAYER.getItem());
                            this.cd.put(p, Long.valueOf(System.currentTimeMillis()));
                        }
                }
                else if (e.getItem().getType() == ItemType.SHOW_PLAYER.getType()) {
                    if (e.getItem().getItemMeta().getDisplayName().equals(ItemType.SHOW_PLAYER.getName()))
                        if (this.cd.containsKey(p)) {
                            if (((Long)this.cd.get(p)).longValue() + 3000L <= System.currentTimeMillis()) {
                                showPlayers(p);
                                p.getInventory().remove(ItemType.SHOW_PLAYER.getType());
                                p.getInventory().setItem(8, ItemType.HIDE_PLAYER.getItem());
                                this.cd.put(p, Long.valueOf(System.currentTimeMillis()));
                            } else {
                                this.pl.sendMsg(p, "Bitte warte etwas, bevor du dieses Item erneut nutzt");
                            }
                        } else {
                            showPlayers(p);
                            p.getInventory().remove(ItemType.SHOW_PLAYER.getType());
                            p.getInventory().setItem(8, ItemType.HIDE_PLAYER.getItem());
                            this.cd.put(p, Long.valueOf(System.currentTimeMillis()));
                        }
                }
                else if ((e.getItem().getType() == ItemType.LAUNCHER.getType()) &&
                        (e.getItem().getItemMeta().getDisplayName().equals(ItemType.LAUNCHER.getName()))) {
                    if (this.pl.status.get(p) == PlayerStatus.PARKOUR) {
                        this.pl.sendMsg(p, "Das wäre schummeln");
                        return;
                    }
                    if (this.fun.containsKey(p)) {
                        if (((Long)this.fun.get(p)).longValue() + 3000L <= System.currentTimeMillis()) {
                            launch(p);
                            this.fun.put(p, Long.valueOf(System.currentTimeMillis()));
                        } else {
                            this.pl.sendMsg(p, "Bitte warte etwas, bevor du dieses Item erneut nutzt");
                        }
                    } else {
                        launch(p);
                        this.fun.put(p, Long.valueOf(System.currentTimeMillis()));
                    }
                }
        }
        catch (Exception localException)
        {
        }
    }

    private void hidePlayers(Player p)
    {
        for (Player t : p.getWorld().getPlayers())
            if (t.getName() != p.getName()) {
                p.hidePlayer(t);
                try {
                    Effekt.EXPLOSION_HUGE.sendToPlayer(p, t.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    Effekt.FLAME.sendToPlayer(p, t.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 5);
                }
                catch (Exception localException) {
                }
            }
        Chunk[] chunks = p.getWorld().getLoadedChunks();
        Chunk[] arrayOfChunk1;
        // int localException1 = (arrayOfChunk1 = chunks).length; for (localException = 0; localException < localException1; localException++) { Chunk i = arrayOfChunk1[localException];
        //if (i.getEntities().length > 0) {
            // int i1 = i.getX();
            // int i2 = i.getZ();
            // p.getWorld().refreshChunk(i1, i2);
       // }
    //}
    }

    private void showPlayers(Player p)
    {
        for (Player t : p.getWorld().getPlayers())
            if (t != p) {
                p.showPlayer(t);
                try {
                    Effekt.EXPLOSION_HUGE.sendToPlayer(p, t.getLocation(), 0.0F, 0.0F, 0.0F, 3.0F, 5);
                    Effekt.FLAME.sendToPlayer(p, t.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 5);
                }
                catch (Exception localException) {
                }
            }
        Chunk[] chunks = p.getWorld().getLoadedChunks();
        Chunk[] arrayOfChunk1;
        /*Exception localException1 = (arrayOfChunk1 = chunks).length; for (localException = 0; localException < localException1; localException++) { Chunk i = arrayOfChunk1[localException];
        if (i.getEntities().length > 0) {
            int i1 = i.getX();
            int i2 = i.getZ();
            p.getWorld().refreshChunk(i1, i2);
        } }*/
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e)
    {
        if ((((MetadataValue)e.getEntity().getMetadata("CUSTOM").get(0)).asBoolean()) &&
                (e.getEntityType() == EntityType.SNOWBALL)) {
            Player p = Bukkit.getPlayer(((MetadataValue)e.getEntity().getMetadata("Player").get(0)).asString());
            e.getEntity().remove();
            Location loc = e.getEntity().getLocation();
            try {
                Effekt.EXPLOSION_LARGE.sendToPlayer(p, loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void launch(Player p)
    {
        //p.setVelocity(new Vector(0, 2, 0));
        p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(2)).setY(2));
        p.setFallDistance(0.0F);
        try {
            Effekt.SMOKE_NORMAL.sendToPlayers(Bukkit.getOnlinePlayers(), p.getEyeLocation(), 1.0F, 1.0F, 1.0F, 1.0F, 10);
            Effekt.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), p.getEyeLocation(), 0.0F, 0.0F, 0.0F, 1.0F, 20);
        } catch (Exception localException) {
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }
}