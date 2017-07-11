package org.dragonet.minelegend.magic;

import org.bukkit.entity.Entity;

/**
 * Created on 2017/7/11.
 */
public interface MagicOwner {

    Entity getEntity();

    boolean isMagicPerforming(String name);
}
