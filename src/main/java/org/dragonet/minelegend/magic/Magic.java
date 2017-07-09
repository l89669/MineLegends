package org.dragonet.minelegend.magic;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Created on 2017/7/9.
 */
interface Magic {

    /**
     * which slot is this magic hot key located at? -1 for entities with no inventory
     * @return
     */
    int getSlotUsed();

    /**
     * initialization
     * @param owner
     * @param args
     */
    void init(Entity owner, int slotUsed, Object... args);

    Entity getOwner();

    /**
     * when did this magic fired?
     * @return
     */
    long getFiredTick();

    /**
     * how many ticks lasted until now?
     * @return
     */
    long getTicksLast();

    boolean isEnded();

    /**
     * how many ticks to wait until next call up?
     * @return
     */
    long getUpdateFrequency();

    void registerToScheduler(Plugin plugin);
}
