package org.dragonet.craftlegend.hotkey;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.dragonet.craftlegend.MineLegend;

import java.util.*;

/**
 * Created on 2017/7/9.
 */
public class HotKeyManager implements Listener {

    private final MineLegend plugin;
    private final Map<UUID, HotKeyMap> enabledPlayers = new HashMap<UUID, HotKeyMap>();
    private final Map<UUID, ItemStack[]> saveUps = new HashMap<UUID, ItemStack[]>();

    public HotKeyManager(MineLegend plugin) {
        this.plugin = plugin;
    }

    public MineLegend getPlugin() {
        return plugin;
    }

    public void sendHotbarItems(Player p, HotKeyMap keyMap) {
        for(int i = 0; i < 9; i++) {
            p.getInventory().setItem(i, keyMap.hotbar[i] != null ? keyMap.hotbar[i].clone() : null);
        }
    }

    public void enableFor(Player player, HotKeyMap keyMap) {
        if(enabledPlayers.containsKey(player.getUniqueId())) return;
        saveUps.put(player.getUniqueId(), player.getInventory().getContents());
        sendHotbarItems(player, keyMap);
        enabledPlayers.put(player.getUniqueId(), keyMap);
    }

    public void disableFor(Player player) {
        if(!enabledPlayers.containsKey(player.getUniqueId())) return;
        player.getInventory().setContents(saveUps.get(player.getUniqueId()));
        saveUps.remove(player.getUniqueId());
        enabledPlayers.remove(player.getUniqueId());
    }

    // ==== events ====

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelectedSlotChange(PlayerItemHeldEvent e) {
        if(!enabledPlayers.containsKey(e.getPlayer().getUniqueId())) return;
        HotKeyMap keyMap = enabledPlayers.get(e.getPlayer().getUniqueId());

        if(e.getPlayer() == null) return; // sometimes bug?
        if(e.getNewSlot() == keyMap.standbySlot) {
            e.getPlayer().sendMessage("standby slot went");
            return;
        }
        if(e.getPreviousSlot() != 8) { // glitched? resend
            sendHotbarItems(e.getPlayer(), keyMap);
            e.setCancelled(true);
            return;
        }
        if(keyMap.handlers.containsKey(e.getNewSlot())) {
            keyMap.handlers.get(e.getNewSlot()).handle(e.getPlayer());
        }

        e.setCancelled(true);
        // e.getPlayer().getInventory().setHeldItemSlot(keyMap.standbySlot);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e){
        if(!enabledPlayers.containsKey(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickupItem(PlayerPickupItemEvent e){
        if(!enabledPlayers.containsKey(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickupArrow(PlayerPickupArrowEvent e){
        if(!enabledPlayers.containsKey(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){
        if(!Player.class.isAssignableFrom(e.getWhoClicked().getClass())) return;
        Player p = (Player)e.getWhoClicked();
        if(!enabledPlayers.containsKey(p.getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent e){
        if(!Player.class.isAssignableFrom(e.getWhoClicked().getClass())) return;
        Player p = (Player)e.getWhoClicked();
        if(!enabledPlayers.containsKey(p.getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemComsume(PlayerItemConsumeEvent e){
        if(!enabledPlayers.containsKey(e.getPlayer())) return;
        e.setCancelled(true);
    }
}
