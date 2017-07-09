package org.dragonet.craftlegend.magic;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created on 2017/7/9.
 */
public abstract class BaseMagic implements Magic, Runnable {

    private Entity owner;
    private int slotUsed = -1;
    private long firedTick;
    private BukkitTask task;
    private boolean ended;

    public void init(Entity owner, int slotUsed, Object... args) {
        this.owner = owner;
        if(slotUsed < 0) {
            this.slotUsed = -1;
        } else {
            this.slotUsed = slotUsed;
        }
        firedTick = owner.getWorld().getFullTime();
    }

    public Entity getOwner() {
        return owner;
    }

    public int getSlotUsed() {
        return slotUsed;
    }

    public long getFiredTick() {
        return firedTick;
    }

    public long getTicksLast() {
        return owner.getWorld().getFullTime() - firedTick;
    }

    public void registerToScheduler(Plugin plugin) {
        task = owner.getServer().getScheduler().runTaskTimer(plugin, this, 0L, getUpdateFrequency());
    }

    public boolean isEnded() {
        return ended;
    }

    public void run() {
        if(!update()) {
            task.cancel();
            ended = true;
        }
    }

    /**
     *
     * @return should it continue running? false to mark as finished
     */
    public abstract boolean update();
}
