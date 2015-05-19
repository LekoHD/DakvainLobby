package com.lekohd.dakvainlobby.util.event;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCountReceiver extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private String server;
    private int amount;

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerCountReceiver(String server, int amount) {
        this.amount = amount;
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

    public int getAmount() {
        return this.amount;
    }
}