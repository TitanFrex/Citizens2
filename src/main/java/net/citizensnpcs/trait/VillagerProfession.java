package net.citizensnpcs.trait;

import net.citizensnpcs.api.attachment.Attachment;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.DataKey;

public class VillagerProfession extends Attachment {
    private final NPC npc;
    private Profession profession = Profession.FARMER;

    public VillagerProfession(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        try {
            profession = Profession.valueOf(key.getString(""));
        } catch (IllegalArgumentException ex) {
            throw new NPCLoadException("Invalid profession.");
        }
    }

    @Override
    public void onSpawn() {
        if (npc.getEntity() instanceof Villager)
            ((Villager) npc.getEntity()).setProfession(profession);
    }

    @Override
    public void save(DataKey key) {
        key.setString("", profession.name());
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
        if (npc.getEntity() instanceof Villager)
            ((Villager) npc.getEntity()).setProfession(profession);
    }

    @Override
    public String toString() {
        return "Profession{" + profession + "}";
    }
}