package org.dragonet.craftlegend.hotkey;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/7/9.
 */
public final class HotKeyMap {

    public int standbySlot = 8;

    public ItemStack[] hotbar = new ItemStack[9]; // MUST fill this in manually

    public Map<Integer, HotKeyHandler> handlers = new HashMap<Integer, HotKeyHandler>(); // register handlers
}
