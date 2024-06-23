package mod.ckenja.karacreate.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = DoorBlock.class,remap = false)
public interface DoorBlockAccessor {
    @Invoker("playSound")
    void invokePlaySound(Entity entity, Level level, BlockPos blockPos, boolean bl);
}