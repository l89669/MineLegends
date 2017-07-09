package org.dragonet.minelegend.legend;

import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.dragonet.minelegend.MineLegend;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 2017/7/9.
 */
public class LegendManager implements Listener {

    private final MineLegend plugin;

    private final Map<UUID, Legend> managedEntities = new HashMap<UUID, Legend>();


    public LegendManager(MineLegend plugin) {
        this.plugin = plugin;
    }

    /**
     * DO NOT CALL THIS EXTERNALLY!!
     */
    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * check whether an entity is managed by this plugin (as a legend)
     * @param e
     * @return
     */
    public boolean isLegend(Entity e) {
        return managedEntities.containsKey(e.getUniqueId());
    }

    /**
     * transform an entity into a legend
     * @param e
     * @param legendName
     */
    public void setLegend(Entity e, String legendName) {
        if(isLegend(e)) throw new IllegalStateException("already set legend for " + e.getClass().getSimpleName());
        // TODO: ...
    }

    /**
     * reset an entity to its original state
     * @param e
     */
    public void unsetLegend(Entity e) {
        if(!isLegend(e)) return; // no error throw out here
        // TODO: ...
    }
}
