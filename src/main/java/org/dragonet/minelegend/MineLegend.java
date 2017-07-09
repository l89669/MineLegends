package org.dragonet.minelegend;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.minelegend.legend.LegendManager;
import org.dragonet.minelegend.magic.MagicManager;
import org.dragonet.hotkeymanager.HotKeyHandler;
import org.dragonet.hotkeymanager.HotKeyManager;
import org.dragonet.hotkeymanager.HotKeyMap;

/**
 * Created on 2017/7/9.
 */
public class MineLegend extends JavaPlugin implements Listener {

    private HotKeyManager hotKeyManager ;

    private MagicManager magicManager = new MagicManager(this);

    private LegendManager legendManager = new LegendManager(this);

    @Override
    public void onEnable() {
        hotKeyManager = (HotKeyManager) getServer().getPluginManager().getPlugin("HotKeyManager");

        magicManager.init();
        legendManager.init();

        getServer().getPluginManager().registerEvents(this, this);
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        HotKeyMap keyMap = new HotKeyMap();
        keyMap.hotbar[8] = new ItemStack(Material.FIREWORK.BARRIER, 1);
        ItemMeta meta = keyMap.hotbar[8].getItemMeta();
        meta.setDisplayName("1=Shoot Fire");
        keyMap.hotbar[8].setItemMeta(meta);
        keyMap.handlers.put(0, new HotKeyHandler() {
            public void handle(Player p) {
                magicManager.fireMagic(p.getPlayer(), 0, "test");
            }
        });
        hotKeyManager.enableFor(e.getPlayer(), keyMap);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerQuitEvent e) {
        hotKeyManager.disableFor(e.getPlayer());
    }

}
