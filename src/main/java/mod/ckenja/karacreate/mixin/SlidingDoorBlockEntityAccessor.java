package mod.ckenja.karacreate.mixin;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SlidingDoorBlockEntity.class,remap = false)
public interface SlidingDoorBlockEntityAccessor {
    @Accessor("deferUpdate")
    boolean getDeferUpdate();
}