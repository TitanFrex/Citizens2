package net.citizensnpcs.npc.entity;

import java.rmi.server.Skeleton;

import net.citizensnpcs.api.abstraction.World;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensMobNPC;
import net.citizensnpcs.npc.CitizensNPC;

public class CitizensSkeletonNPC extends CitizensMobNPC {

    public CitizensSkeletonNPC(int id, String name) {
        super(id, name, EntitySkeletonNPC.class);
    }

    @Override
    public Skeleton getBukkitEntity() {
        return (Skeleton) getHandle().getBukkitEntity();
    }

    public static class EntitySkeletonNPC extends EntitySkeleton implements NPCHandle {
        private final CitizensNPC npc;

        public EntitySkeletonNPC(World world) {
            this(world, null);
        }

        public EntitySkeletonNPC(World world, NPC npc) {
            super(world);
            this.npc = (CitizensNPC) npc;
            goalSelector = new PathfinderGoalSelector();
            targetSelector = new PathfinderGoalSelector();
        }

        @Override
        public void z_() {
            super.z_();
            if (npc != null)
                npc.update();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}