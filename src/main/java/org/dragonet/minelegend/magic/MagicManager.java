package org.dragonet.minelegend.magic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.dragonet.minelegend.MineLegend;
import org.dragonet.minelegend.magic.impl.EffectTestMagic;
import org.dragonet.minelegend.magic.impl.FireBallMagic;
import org.dragonet.minelegend.magic.impl.TestMagic;

import java.util.*;

/**
 * Created on 2017/7/9.
 */
public class MagicManager implements Listener {

    private final static Map<String, Class<? extends Magic>> nameRegister = new HashMap<String, Class<? extends Magic>>();
    private final static Map<Class<? extends Magic>, String> classRegister = new HashMap<Class<? extends Magic>, String>();

    static {
        // load stuffs
        register("test", TestMagic.class);
        register("effect", EffectTestMagic.class);
        register("fireball", FireBallMagic.class);
    }

    private static void register(String name, Class<? extends Magic> clazz) {
        System.out.println("Registered magic [" + name + "] to [" + clazz.getSimpleName() + "]. ");
        nameRegister.put(name, clazz);
        classRegister.put(clazz, name);
    }

    public static String getMagicName(Class<? extends Magic> clazz){
        return classRegister.get(clazz);
    }



    private final MineLegend plugin;

    // a map of entity UUIDs to ongoing magics
    private final Map<UUID, MagicOwnerImpl> managedEntites = new HashMap<UUID, MagicOwnerImpl>();

    public MagicManager(MineLegend plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                for(MagicOwnerImpl owner : managedEntites.values()) {
                    owner.cleanUpEndedMagics();
                }
            }
        }, 0L, 5L);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean fireMagic(Entity e, int slotUsed, String magic, Object... args) {
        if(!nameRegister.containsKey(magic)) {
            throw new IllegalArgumentException("invalid magic nae");
        }
        if(!managedEntites.containsKey(e.getUniqueId())) {
            throw new IllegalStateException("That entity is not managed by MagicManager");
        }
        MagicOwnerImpl owner = managedEntites.get(e.getUniqueId());
        if(owner.isMagicPerforming(magic)) {
            return false;
        }

        Class<? extends Magic> clazz = nameRegister.get(magic);
        Magic instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        int slot = slotUsed;
        if(!InventoryHolder.class.isAssignableFrom(e.getClass())) {
            slot = -1;
        }
        instance.init(e, slot, args);
        owner.addOngoingMagic(instance);
        instance.registerToServer(plugin);
        return true;
    }

    // api
    public boolean isEntityManaged(Entity entity) {
        return managedEntites.containsKey(entity.getUniqueId());
    }

    // api
    public void manageEntity(Entity e){
        if(isEntityManaged(e)) return;
        managedEntites.put(e.getUniqueId(), new MagicOwnerImpl(e));
        plugin.getLogger().info("[+] Now managing entity #" + e.getEntityId() + " [" + e.getClass().getSimpleName() + "]");
    }

    // api
    public void unmanageEntity(Entity e) {
        if(!isEntityManaged(e)) return;
        // TODO: force end all magics
        // ...
        managedEntites.remove(e.getEntityId());
        plugin.getLogger().info("[-] NO MORE managing entity #" + e.getEntityId() + " [" + e.getClass().getSimpleName() + "]");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e){
        if(isEntityManaged(e.getPlayer()))  unmanageEntity(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent e) {
        if(!Player.class.isAssignableFrom(e.getEntity().getClass())) {
            // not a real player, just an NPC
            unmanageEntity(e.getEntity());
        }
    }
}
