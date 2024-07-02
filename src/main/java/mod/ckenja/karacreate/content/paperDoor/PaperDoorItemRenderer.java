package mod.ckenja.karacreate.content.paperDoor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import mod.ckenja.karacreate.KaraCreateBlockEntityTypes;
import mod.ckenja.karacreate.KaraCreateBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaperDoorItemRenderer extends BlockEntityWithoutLevelRenderer {
    private SlidingDoorBlockEntity paper_door;

    public PaperDoorItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        ((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        paper_door = new SlidingDoorBlockEntity(KaraCreateBlockEntityTypes.PAPER_DOOR.get(), BlockPos.ZERO, KaraCreateBlocks.SHOJI_DOOR.get().defaultBlockState());
        paper_door.initialize();
    }

    @Override
    public void renderByItem(@NotNull ItemStack pStack, @NotNull ItemDisplayContext pDisplayContext, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        PaperDoorBehaviour behaviour = this.paper_door.getBehaviour(PaperDoorBehaviour.type);
        behaviour.fromItem(pStack);
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.paper_door, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}
