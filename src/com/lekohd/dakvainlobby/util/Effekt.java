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
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum Effekt
{
    HUGE_EXPLOSION(
            "hugeexplosion",
            "EXPLOSION_HUGE"),

    LARGE_EXPLODE(
            "largeexplode",
            "EXPLOSION_LARGE"),

    BUBBLE(
            "bubble",
            "WATER_BUBBLE"),

    SUSPEND(
            "suspend",
            "SUSPENDED"),

    DEPTH_SUSPEND(
            "depthSuspend",
            "SUSPENDED_DEPTH"),

    MAGIC_CRIT(
            "magicCrit",
            "CRIT_MAGIC"),

    MOB_SPELL(
            "mobSpell",
            "SPELL_MOB"),

    MOB_SPELL_AMBIENT(
            "mobSpellAmbient",
            "SPELL_MOB_AMBIENT"),

    INSTANT_SPELL(
            "instantSpell",
            "SPELL_INSTANT"),

    WITCH_MAGIC(
            "witchMagic",
            "SPELL_WITCH"),

    EXPLODE(
            "explode",
            "EXPLOSION_NORMAL"),

    SPLASH(
            "splash",
            "WATER_SPLASH"),

    LARGE_SMOKE(
            "largesmoke",
            "SMOKE_LARGE"),

    RED_DUST(
            "reddust",
            "REDSTONE"),

    SNOWBALL_POOF(
            "snowballpoof",
            "SNOWBALL"),

    ANGRY_VILLAGER(
            "angryVillager",
            "VILLAGER_ANGRY"),

    HAPPY_VILLAGER(
            "happerVillager",
            "VILLAGER_HAPPY"),

    EXPLOSION_NORMAL(
            EXPLODE.getName()),

    EXPLOSION_LARGE(
            LARGE_EXPLODE.getName()),

    EXPLOSION_HUGE(
            HUGE_EXPLOSION.getName()),

    FIREWORKS_SPARK(
            "fireworksSpark"),

    WATER_BUBBLE(
            BUBBLE.getName()),

    WATER_SPLASH(
            SPLASH.getName()),

    WATER_WAKE,

    SUSPENDED(
            SUSPEND.getName()),

    SUSPENDED_DEPTH(
            DEPTH_SUSPEND.getName()),
    CRIT(
            "crit"),

    CRIT_MAGIC(
            MAGIC_CRIT.getName()),

    SMOKE_NORMAL,

    SMOKE_LARGE(
            LARGE_SMOKE.getName()),
    SPELL(
            "spell"),

    SPELL_INSTANT(
            INSTANT_SPELL.getName()),

    SPELL_MOB(
            MOB_SPELL.getName()),

    SPELL_MOB_AMBIENT(
            MOB_SPELL_AMBIENT.getName()),

    SPELL_WITCH(
            WITCH_MAGIC.getName()),

    DRIP_WATER(
            "dripWater"),

    DRIP_LAVA(
            "dripLava"),

    VILLAGER_ANGRY(
            ANGRY_VILLAGER.getName()),

    VILLAGER_HAPPY(
            HAPPY_VILLAGER.getName()),

    TOWN_AURA(
            "townaura"),

    NOTE(
            "note"),

    PORTAL(
            "portal"),

    ENCHANTMENT_TABLE(
            "enchantmenttable"),

    FLAME(
            "flame"),

    LAVA(
            "lave"),

    FOOTSTEP(
            "footstep"),

    CLOUD(
            "cloud"),

    REDSTONE(
            "reddust"),

    SNOWBALL(
            "snowballpoof"),

    SNOW_SHOVEL(
            "snowshovel"),

    SLIME(
            "slime"),

    HEART(
            "heart"),

    BARRIER,

    ITEM_CRACK,

    BLOCK_CRACK,

    BLOCK_DUST,

    WATER_DROP,

    ITEM_TAKE,

    MOB_APPEARANCE;

    private String particleName;
    private String enumValue;
    private static Class<?> nmsPacketPlayOutParticle = ReflectionUtilities.getNMSClass("PacketPlayOutWorldParticles");
    private static Class<?> nmsEnumParticle;
    private static int particleRange = 25;

    private Effekt(String particleName, String enumValue)
    {
        this.particleName = particleName;
        this.enumValue = enumValue;
    }

    private Effekt(String particleName) {
        this(particleName, null);
    }

    private Effekt() {
        this(null, null);
    }

    public String getName() {
        return this.particleName;
    }

    public static void setRange(int range)
    {
        if (range < 0) throw new IllegalArgumentException("Range must be positive!");
        if (range > 2147483647) throw new IllegalArgumentException("Range is too big!");
        particleRange = range;
    }

    public static int getRange() {
        return particleRange;
    }

    public void sendToPlayer(Player player, Location location, float areaX, float areaY, float areaZ, float speed, int count) throws Exception {
        if (!isPlayerInRange(player, location)) return;
        if (ReflectionUtilities.getVersion().contains("v1_8"))
            try {
                if (nmsEnumParticle == null) nmsEnumParticle = ReflectionUtilities.getNMSClass("EnumParticle");
                Object packet = nmsPacketPlayOutParticle.getConstructor(new Class[] { nmsEnumParticle, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE })
                .newInstance(new Object[] {
                        getEnum(nmsEnumParticle.getName() + "." + (this.enumValue != null ? this.enumValue : name().toUpperCase())), Boolean.valueOf(true), Float.valueOf((float)location.getX()), Float.valueOf((float)location.getY()), Float.valueOf((float)location.getZ()), Float.valueOf(areaX), Float.valueOf(areaY), Float.valueOf(areaZ), Float.valueOf(speed), Integer.valueOf(count), new int[0] });
                Object handle = ReflectionUtilities.getHandle(player);
                Object connection = ReflectionUtilities.getField(handle.getClass(), "playerConnection").get(handle);
                ReflectionUtilities.getMethod(connection.getClass(), "sendPacket", new Class[0]).invoke(connection, new Object[] { packet });
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to send Particle " + name() + ". (Version 1.8): " + e.getMessage());
            }
        else
            try {
                if (this.particleName == null) throw new Exception();
                Object packet = nmsPacketPlayOutParticle.getConstructor(new Class[] { String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE }).newInstance(new Object[] { this.particleName, Float.valueOf((float)location.getX()), Float.valueOf((float)location.getY()), Float.valueOf((float)location.getZ()), Float.valueOf(areaX), Float.valueOf(areaY), Float.valueOf(areaZ), Float.valueOf(speed), Integer.valueOf(count) });
                Object handle = ReflectionUtilities.getHandle(player);
                Object connection = ReflectionUtilities.getField(handle.getClass(), "playerConnection").get(handle);
                ReflectionUtilities.getMethod(connection.getClass(), "sendPacket", new Class[0]).invoke(connection, new Object[] { packet });
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to send Particle " + name() + ". (Invalid Server Version: 1.7) " + e.getMessage());
            }
    }

    public void sendToPlayers(Collection<? extends Player> collection, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception
    {
        for (Player p : collection)
            sendToPlayer(p, location, offsetX, offsetY, offsetZ, speed, count);
    }

    public void sendToPlayers(Player[] players, Location location, float areaX, float areaY, float areaZ, float speed, int count) throws Exception {
        for (Player p : players)
            sendToPlayer(p, location, areaX, areaY, areaZ, speed, count);
    }

    private static Enum<?> getEnum(String enumFullName)
    {
        String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
        if (x.length == 2) {
            String enumClassName = x[0];
            String enumName = x[1];
            try {
                Class cl = Class.forName(enumClassName);
                return Enum.valueOf(cl, enumName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean isPlayerInRange(Player p, Location center) {
        double distance = 0.0D;
        if ((distance = center.distance(p.getLocation())) > 1.7976931348623157E+308D) return false;
        return distance < particleRange;
    }

    public static class ReflectionUtilities
    {
        public static void setValue(Object instance, String fieldName, Object value) throws Exception {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        }

        public static Object getValue(Object instance, String fieldName) throws Exception {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        }

        public static String getVersion() {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            String version = name.substring(name.lastIndexOf('.') + 1) + ".";
            return version;
        }

        public static Class<?> getNMSClass(String className) {
            String fullName = "net.minecraft.server." + getVersion() + className;
            Class clazz = null;
            try {
                clazz = Class.forName(fullName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return clazz;
        }

        public static Class<?> getOBCClass(String className) {
            String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
            Class clazz = null;
            try {
                clazz = Class.forName(fullName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return clazz;
        }

        public static Object getHandle(Object obj) {
            try {
                return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Field getField(Class<?> clazz, String name) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Method getMethod(Class<?> clazz, String name, Class<?>[] args) {
            for (Method m : clazz.getMethods()) {
                if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
                    m.setAccessible(true);
                    return m;
                }
            }
            return null;
        }

        public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
            boolean equal = true;
            if (l1.length != l2.length) {
                return false;
            }
            for (int i = 0; i < l1.length; i++) {
                if (l1[i] != l2[i]) {
                    equal = false;
                    break;
                }
            }
            return equal;
        }
    }
}