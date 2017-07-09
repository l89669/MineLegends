package org.dragonet.craftlegend.legend;

import org.bukkit.event.Listener;
import org.dragonet.craftlegend.MineLegend;

/**
 * Created on 2017/7/9.
 */
public class LegendManager implements Listener {

    private final MineLegend plugin;



    public LegendManager(MineLegend plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
