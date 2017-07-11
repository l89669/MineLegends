package org.dragonet.minelegend.magic.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.dragonet.minelegend.magic.BaseMagic;

/**
 * Created on 2017/7/12.
 */
public class FireBallMagic extends BaseMagic {
    /**
     * how many ticks to wait until next call up?
     *
     * @return
     */
    public long getUpdateFrequency() {
        return 1L;
    }

    /**
     * @return should it continue running? false to mark as finished
     */
    public boolean update() {
        Entity f = getOwner().getWorld().spawnEntity(getOwner().getLocation().add(new Vector(0f, 1.6f, 0f)).add(getOwner().getLocation().getDirection().multiply(2F)), EntityType.FIREBALL);
        f.setVelocity(getOwner().getLocation().getDirection().multiply(5));
        return false;
    }
}
