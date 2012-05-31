package net.citizensnpcs.trait;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.abstraction.EventHandler;
import net.citizensnpcs.api.attachment.Attachment;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.DataKey;

public class Saddle extends Attachment implements Toggleable, Listener {
    private final NPC npc;
    private boolean saddle;
    private boolean pig;

    public Saddle(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        saddle = key.getBoolean("");
    }

    @Override
    public void onSpawn() {
        if (npc.getEntity() instanceof Pig) {
            ((Pig) npc.getEntity()).setSaddle(saddle);
            pig = true;
        } else
            pig = false;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (pig && npc.equals(CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked())))
            event.setCancelled(true);
    }

    @Override
    public void save(DataKey key) {
        key.setBoolean("", saddle);
    }

    @Override
    public boolean toggle() {
        saddle = !saddle;
        if (pig)
            ((Pig) npc.getEntity()).setSaddle(saddle);
        return saddle;
    }

    @Override
    public String toString() {
        return "Saddle{" + saddle + "}";
    }
}