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

/*
MIT License - see the LICENSE file for full details

Copyright (c) [年] [著作権者名]

https://github.com/Rabbitminers/Extended-Cogwheels
common/src/main/java/com/rabbitminers/extendedgears/mixin/MixinAllBlockEntityTypes.java
*/
@Mixin(AllBlockEntityTypes.class)
public class AddSlidingDoorBlockMixin {
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
    private static <T extends Block, E extends BlockEntity> BlockEntityBuilder<E, ?> addSlidingDoor(
            BlockEntityBuilder<E, ?> instance,
            NonNullSupplier<T>[] blocks
    ) {

        instance.validBlocks(KaraCreateBlocks.PAPER_DOOR);

        return instance.validBlocks(blocks);
    }
}
