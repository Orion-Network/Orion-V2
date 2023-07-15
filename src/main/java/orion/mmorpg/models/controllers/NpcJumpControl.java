package orion.mmorpg.models.controllers;

import net.minecraft.world.entity.ai.control.JumpControl;
import orion.mmorpg.models.NPC;
import orion.mmorpg.models.NPCUtils;

public class NpcJumpControl extends JumpControl {
    private boolean canJump;
    private final NPC npc;
    public NpcJumpControl(NPC npc) {
        super(NPCUtils.getDummySentient(npc.getLevel()));
        this.npc = npc;
    }

    @Override
    public void jump() {
        this.canJump = true;
    }

    @Override
    public void tick() {
        this.npc.setJumping(this.canJump);
        this.canJump = false;
    }
}
