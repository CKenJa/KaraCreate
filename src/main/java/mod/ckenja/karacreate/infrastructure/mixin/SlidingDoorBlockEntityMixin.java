package mod.ckenja.karacreate.infrastructure.mixin;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlidingDoorBlockEntity.class)
public abstract class SlidingDoorBlockEntityMixin extends SmartBlockEntity{
    public SlidingDoorBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(method = "showBlockModel()V",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/sounds/SoundEvents;IRON_DOOR_CLOSE:Lnet/minecraft/sounds/SoundEvent;"
            )
    )
   protected SoundEvent getDoorSound() {
        if(getBlockState().getBlock() instanceof DoorBlock door)
            return door.type().doorClose();
        return SoundEvents.IRON_DOOR_CLOSE;
    }
}
