package net.citizensnpcs.npc.entity;

import net.citizensnpcs.api.event.NPCPushEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.MobEntityController;
import net.citizensnpcs.npc.ai.NPCHolder;
import net.citizensnpcs.util.NMS;
import net.citizensnpcs.util.Util;
import net.minecraft.server.v1_7_R1.EntitySpider;
import net.minecraft.server.v1_7_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftSpider;
import org.bukkit.entity.Spider;
import org.bukkit.util.Vector;

public class SpiderController extends MobEntityController {
    public SpiderController() {
        super(EntitySpiderNPC.class);
    }

    @Override
    public Spider getBukkitEntity() {
        return (Spider) super.getBukkitEntity();
    }

    public static class EntitySpiderNPC extends EntitySpider implements NPCHolder {
        private final CitizensNPC npc;

        public EntitySpiderNPC(World world) {
            this(world, null);
        }

        public EntitySpiderNPC(World world, NPC npc) {
            super(world);
            this.npc = (CitizensNPC) npc;
            if (npc != null) {
                NMS.clearGoals(goalSelector, targetSelector);
            }
        }

        @Override
        protected void a(double d0, boolean flag) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag);
            }
        }

        @Override
        protected void b(float f) {
            if (npc == null || !npc.isFlyable()) {
                super.b(f);
            }
        }

        @Override
        public boolean bL() {
            if (npc == null)
                return super.bL();
            boolean protectedDefault = npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
            if (!protectedDefault || !npc.data().get(NPC.LEASH_PROTECTED_METADATA, protectedDefault))
                return super.bL();
            if (super.bL()) {
                unleash(true, false); // clearLeash with client update
            }
            return false; // shouldLeash
        }

        @Override
        public void bn() {
            super.bn();
            if (npc != null)
                npc.update();
        }

        @Override
        public void bq() {
            if (npc == null)
                super.bq();
            else {
                NMS.updateAI(this);
                npc.update();
            }
        }

        @Override
        public void collide(net.minecraft.server.v1_7_R1.Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null)
                Util.callCollisionEvent(npc, entity.getBukkitEntity());
        }

        @Override
        public void e(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.e(f, f1);
            } else {
                NMS.flyingMoveLogic(this, f, f1);
            }
        }

        @Override
        public void g(double x, double y, double z) {
            if (npc == null) {
                super.g(x, y, z);
                return;
            }
            if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                    super.g(x, y, z);
                return;
            }
            Vector vector = new Vector(x, y, z);
            NPCPushEvent event = Util.callPushEvent(npc, vector);
            if (!event.isCancelled()) {
                vector = event.getCollisionVector();
                super.g(vector.getX(), vector.getY(), vector.getZ());
            }
            // when another entity collides, this method is called to push the
            // NPC so we prevent it from doing anything if the event is
            // cancelled.
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (bukkitEntity == null && npc != null)
                bukkitEntity = new SpiderNPC(this);
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public boolean h_() {
            if (npc == null || !npc.isFlyable()) {
                return super.h_();
            } else {
                return false;
            }
        }

        @Override
        protected boolean isTypeNotPersistent() {
            return npc == null ? super.isTypeNotPersistent() : false;
        }
    }

    public static class SpiderNPC extends CraftSpider implements NPCHolder {
        private final CitizensNPC npc;

        public SpiderNPC(EntitySpiderNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}