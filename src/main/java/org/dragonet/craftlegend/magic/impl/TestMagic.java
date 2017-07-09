package org.dragonet.craftlegend.magic.impl;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.dragonet.craftlegend.magic.BaseMagic;

/**
 * Created on 2017/7/9.
 */
public class TestMagic extends BaseMagic {

    @Override
    public void init(Entity owner, int slotUsed, Object... args) {
        super.init(owner, slotUsed, args);
    }

    /**
     * how many ticks to wait until next call up?
     *
     * @return
     */
    public long getUpdateFrequency() {
        return 10;
    }

    /**
     * @return should it continue running? false to mark as finished
     */
    public boolean update() {
        if(getTicksLast() > 200L) {
            return false;
        }
        getOwner().getWorld().spawnParticle(Particle.FLAME, getOwner().getLocation(), 10);
        ((InventoryHolder)getOwner()).getInventory().setItem(getSlotUsed(),
                new ItemStack(
                        Material.FIREWORK,
                        (int)((1 - (getTicksLast() / 200.0F)) * 100)
                )
        );
        ((Player)getOwner()).sendTitle("", "Percent " + ((int)((1 - (getTicksLast() / 200.0F)) * 100)) + "%", 0, 50, 0);
        return true;
    }
}
