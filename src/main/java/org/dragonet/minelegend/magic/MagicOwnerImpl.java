package org.dragonet.minelegend.magic;

import org.bukkit.entity.Entity;

import java.util.*;

/**
 * Created on 2017/7/11.
 */
class MagicOwnerImpl implements MagicOwner {

    private final Entity owner;

    public final static Map<String, Magic> ongoings = new HashMap<String, Magic>();

    public MagicOwnerImpl(Entity owner) {
        this.owner = owner;
    }

    // api
    public Entity getEntity() {
        return owner;
    }

    // api
    public boolean isMagicPerforming(String name) {
        return ongoings.containsKey(name);
    }


    // internal
    public void addOngoingMagic(Magic magic) {
        String name = MagicManager.getMagicName(magic.getClass());
        if(ongoings.containsKey(name)) {
            throw new IllegalStateException("already have magic [" + magic.getClass().getSimpleName() + "] ongoing, this shouldn't happen");
        }
        ongoings.put(name, magic);
    }

    public void cleanUpEndedMagics(){
        Set<String> finished = new HashSet<String>();
        for(Magic m : ongoings.values()) {
            if(m.isEnded()) {
                finished.add(MagicManager.getMagicName(m.getClass()));
                System.out.println("Magic [" + m.getClass().getSimpleName() + "] ended! ");
            }
        }
        for(String m : finished) {
            ongoings.remove(m);
        }
    }
}
