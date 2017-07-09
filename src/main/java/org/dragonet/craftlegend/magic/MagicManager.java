package org.dragonet.craftlegend.magic;

import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.dragonet.craftlegend.MineLegend;
import org.dragonet.craftlegend.magic.impl.TestMagic;

import java.util.*;

/**
 * Created on 2017/7/9.
 */
public class MagicManager implements Listener, Runnable {

    private final static Map<String, Class<? extends Magic>> nameRegister = new HashMap<String, Class<? extends Magic>>();

    static {
        // load stuffs
        register("test", TestMagic.class);
    }

    private static void register(String name, Class<? extends Magic> clazz) {
        System.out.println("Registered magic [" + name + "] to [" + clazz.getSimpleName() + "]. ");
        nameRegister.put(name, clazz);
    }

    private final MineLegend plugin;

    // a map of entity UUIDs to ongoing magics
    private final Map<UUID, Magic> ongoings = new HashMap<UUID, Magic>();

    public MagicManager(MineLegend plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0L, 5L);
    }

    public boolean isFiringMagic(Entity e) {
        return ongoings.containsKey(e.getUniqueId());
    }

    public boolean fireMagic(Entity e, int slotUsed, String magic, Object... args) {
        if(!nameRegister.containsKey(magic)) {
            throw new IllegalArgumentException("invalid magic nae");
        }
        if(ongoings.containsKey(e.getUniqueId())) {
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
        ongoings.put(e.getUniqueId(), instance);
        instance.registerToScheduler(plugin);
        return true;
    }

    public void run() {
        Set<UUID> finished = new HashSet<UUID>();
        for(Magic m : ongoings.values()) {
            if(m.isEnded()) {
                finished.add(m.getOwner().getUniqueId());
                System.out.println("Magic [" + m.getClass().getSimpleName() + "] ended! ");
            }
        }
        for(UUID u : finished) {
            ongoings.remove(u);
        }
    }
}
