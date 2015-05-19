package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection
{
    private static String CRAFT;
    private static String NMS;
    private static Class<?> packetClass = getNMSClass("Packet");

    public static Class<?> getClass(String name)
    {
        try
        {
            return Class.forName(name);
        } catch (Exception ex) {
        }
        return null;
    }

    public static Class<?> getNMSClass(String name)
    {
        if (NMS == null) {
            String[] pieces = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
            if (pieces.length >= 4) {
                NMS = "net.minecraft.server." + pieces[3] + ".";
            }
            else {
                NMS = "net.minecraft.server.";
            }
        }
        return getClass(NMS + name);
    }

    public static Class<?> getCraftClass(String name)
    {
        if (CRAFT == null) {
            String[] pieces = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
            if (pieces.length >= 4) {
                CRAFT = "org.bukkit.craftbukkit." + pieces[3] + ".";
            }
            else {
                CRAFT = "org.bukkit.craftbukkit.";
            }
        }
        return getClass(CRAFT + name);
    }

    public static Object getInstance(Class<?> c, Object[] args)
    {
        if (c == null) return null; try
    {
        for (Constructor constructor : c.getDeclaredConstructors())
            if (constructor.getGenericParameterTypes().length == args.length)
                return constructor.newInstance(args);
    }
    catch (Exception localException)
    {
    }
        return null;
    }

    public static void setValue(Object o, String fieldName, Object value)
    {
        try
        {
            Field field = o.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) field.setAccessible(true);
            field.set(o, value);
        }
        catch (Exception localException)
        {
        }
    }

    public static Object getValue(Object o, String fieldName)
    {
        try
        {
            Field field = o.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) field.setAccessible(true);
            return field.get(o);
        } catch (Exception localException) {
        }
        return null;
    }

    public static Method getMethod(Object o, String methodName)
    {
        try
        {
            Method method = o.getClass().getMethod(methodName, new Class[0]);
            if (!method.isAccessible()) method.setAccessible(true);
            return method;
        } catch (Exception localException) {
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", new Class[] { packetClass }).invoke(connection, new Object[] { packet });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}