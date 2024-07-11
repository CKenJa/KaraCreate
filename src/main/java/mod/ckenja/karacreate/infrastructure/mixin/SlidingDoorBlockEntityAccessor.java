package mod.ckenja.karacreate.infrastructure.mixin;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = SlidingDoorBlockEntity.class,remap = false)
public interface SlidingDoorBlockEntityAccessor {

    @Accessor("animation")
    LerpedFloat getAnimation();

    @Accessor("deferUpdate")
    boolean getDeferUpdate();

    @Invoker("shouldRenderSpecial")
    boolean invokeShouldRenderSpecial(BlockState state);
}