package mod.ckenja.karacreate.mixin;

import com.simibubi.create.AllBlockEntityTypes;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mod.ckenja.karacreate.KaraCreateBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AllBlockEntityTypes.class)
public class AllBlockEntityTypesMixin {
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/builders/BlockEntityBuilder;validBlocks([Lcom/tterrag/registrate/util/nullness/NonNullSupplier;)Lcom/tterrag/registrate/builders/BlockEntityBuilder;"
            ),
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=track_station"),
                    to = @At(value = "CONSTANT", args = "stringValue=copycat")
            ),
            remap = false
    )
    private static <T extends Block, E extends BlockEntity> BlockEntityBuilder<E, ?> addSlidingDoorBlock(
            BlockEntityBuilder<E, ?> instance,
            NonNullSupplier<T>[] blocks
    ) {
        instance.validBlocks(
                KaraCreateBlocks.SHOJI_DOOR,
                KaraCreateBlocks.SNOW_VIEWING_SHOJI_DOOR,
                KaraCreateBlocks.FUSUMA_DOOR,
                KaraCreateBlocks.SMALL_SHOJI_DOOR,
                KaraCreateBlocks.SMALL_FUSUMA_DOOR
        );
        return instance.validBlocks(blocks);
    }
}
