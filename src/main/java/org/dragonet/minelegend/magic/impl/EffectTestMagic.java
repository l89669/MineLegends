package org.dragonet.minelegend.magic.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.dragonet.minelegend.magic.BaseMagic;

/**
 * Created on 2017/7/11.
 */
public class EffectTestMagic extends BaseMagic implements Listener{

    public final static Vector[] OFFSETS = new Vector[]{
            new Vector(1.2D, -0.99D, 0D),
            new Vector(-1.2D, -0.99D, 0D),
            new Vector(0D, -0.99D, 1.2D),
            new Vector(0D, -0.99D, -1.2D)};

    public final static Vector ZERO_VECTOR = new Vector(0, 0, 0);

    private FallingBlock[] blocks = new FallingBlock[4];

    private Plugin plugin;

    public long getUpdateFrequency() {
        return 2L;
    }

    @Override
    public void init(final Entity owner, int slotUsed, Object... args) {
        super.init(owner, slotUsed, args);

        for(int i = 0; i < 4; i++) {
            blocks[i] = owner.getWorld().spawnFallingBlock(owner.getLocation().add(OFFSETS[i]), new MaterialData(Material.ENDER_STONE));
            blocks[i].setGravity(false);
            /* blocks[i].setCustomName("[" + i + "]");
            blocks[i].setCustomNameVisible(true); */
        }
    }

    /**
     * @return should it continue running? false to mark as finished
     */
    public boolean update() {
        for(int i = 0; i < 4; i++) {
            blocks[i].remove();
        }
        for(int i = 0; i < 4; i++) {
            blocks[i] = getOwner().getWorld().spawnFallingBlock(getOwner().getLocation().add(OFFSETS[i]), new MaterialData(Material.ENDER_STONE));
            blocks[i].setGravity(false);
            /* blocks[i].setCustomName("[" + i + "]");
            blocks[i].setCustomNameVisible(true); */
        }
        if(getTicksLast() > 200L) {
            for(int i = 0; i < 4; i++) {
                blocks[i].remove();
            }
            HandlerList.unregisterAll(this);
            return false;
        }
        return true;
    }

    @Override
    public void registerToServer(Plugin plugin) {
        super.registerToServer(plugin);

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFall(EntityChangeBlockEvent e) {
        for(int i = 0; i < blocks.length; i ++) {
            if(e.getEntity().getEntityId() == blocks[i].getEntityId()){
                e.setCancelled(true);
                return;
            }
        }
    }
}
