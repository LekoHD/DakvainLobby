package com.lekohd.dakvainlobby.util;

/**
 * Created by Leon on 19.05.2015.
 * Project DakvainLobby
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DamageLoreRemover
{
    private static final HashSet<Material> TOOLS = new HashSet() { } ;
    private static Class<?> NBT_BASE;
    private static Class<?> NBT_COMPOUND;
    private static Class<?> NBT_LIST;
    private static Class<?> NMS_ITEM;
    private static Class<?> CRAFT_ITEM;
    private static Method SET;
    private static Method SET_TAG;
    private static Method GET_TAG;
    private static Method AS_CRAFT;
    private static Method AS_NMS;

    private static void setup()
    {
        try
        {
            NBT_BASE = Reflection.getNMSClass("NBTBase");
            NBT_COMPOUND = Reflection.getNMSClass("NBTTagCompound");
            NBT_LIST = Reflection.getNMSClass("NBTTagList");
            NMS_ITEM = Reflection.getNMSClass("ItemStack");
            CRAFT_ITEM = Reflection.getCraftClass("inventory.CraftItemStack");

            AS_NMS = CRAFT_ITEM.getMethod("asNMSCopy", new Class[] { ItemStack.class });
            GET_TAG = NMS_ITEM.getMethod("getTag", new Class[0]);
            SET = NBT_COMPOUND.getMethod("set", new Class[] { String.class, NBT_BASE });
            SET_TAG = NMS_ITEM.getMethod("setTag", new Class[] { NBT_COMPOUND });
            AS_CRAFT = CRAFT_ITEM.getMethod("asCraftMirror", new Class[] { NMS_ITEM });
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().severe("Failed to set up reflection for removing damage lores.");
        }
    }

    public static ItemStack removeAttackDmg(ItemStack item)
    {
        if ((item == null) || (!TOOLS.contains(item.getType())))
        {
            return item;
        }
        if (NBT_BASE == null) setup();
        try
        {
            item = item.clone();
            Object nmsStack = AS_NMS.invoke(null, new Object[] { item });
            Object nbtTag = GET_TAG.invoke(nmsStack, new Object[0]);
            Object nbtTagList = Reflection.getInstance(NBT_LIST, new Object[0]);
            SET.invoke(nbtTag, new Object[] { "AttributeModifiers", nbtTagList });
            SET_TAG.invoke(nmsStack, new Object[] { nbtTag });
            return (ItemStack)AS_CRAFT.invoke(null, new Object[] { nmsStack });
        }
        catch (Exception ex) {
        }
        return item;
    }
}
