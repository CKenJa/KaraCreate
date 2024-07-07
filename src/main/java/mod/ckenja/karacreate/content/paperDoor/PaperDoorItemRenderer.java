package mod.ckenja.karacreate.content.paperDoor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import mod.ckenja.karacreate.KaraCreateBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class PaperDoorItemRenderer extends BlockEntityWithoutLevelRenderer {
    private SlidingDoorBlockEntity paper_door;
    private final Block block;
    private PaperDoorBehaviour behaviour;

    public PaperDoorItemRenderer(Block block) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.block = block;
    }

    @Override
    public void renderByItem(@NotNull ItemStack pStack, @NotNull ItemDisplayContext pDisplayContext, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if(behaviour == null) {
            paper_door = new SlidingDoorBlockEntity(KaraCreateBlockEntityTypes.PAPER_DOOR.get(), BlockPos.ZERO, block.defaultBlockState());
            paper_door.initialize();
            behaviour = this.paper_door.getBehaviour(PaperDoorBehaviour.type);
        }
        behaviour.fromItem(pStack);
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.paper_door, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}
