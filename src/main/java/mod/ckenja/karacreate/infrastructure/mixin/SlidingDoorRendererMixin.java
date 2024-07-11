package mod.ckenja.karacreate.infrastructure.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import mod.ckenja.karacreate.content.smallDoor.SlidingSmallDoorBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlidingDoorRenderer.class)
public class SlidingDoorRendererMixin {

    @Redirect(method = "renderSafe(Lcom/simibubi/create/content/decoration/slidingDoor/SlidingDoorBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/properties/DoubleBlockHalf;values()[Lnet/minecraft/world/level/block/state/properties/DoubleBlockHalf;"//,
            ),
            remap = false
    )
    private DoubleBlockHalf[] redirectValues(SlidingDoorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                             int light, int overlay) {
        if(be.getBlockState().getBlock() instanceof SlidingSmallDoorBlock)
            return new DoubleBlockHalf[]{DoubleBlockHalf.LOWER};
        return DoubleBlockHalf.values();
    }
}
