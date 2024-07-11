package mod.ckenja.karacreate.content.paperDoor;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import mod.ckenja.karacreate.foundation.register.KaraCreateBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class PaperDoorBlock extends SlidingDoorBlock {

    public PaperDoorBlock(Properties p_52737_, BlockSetType type) {
        super(p_52737_, type, false);
    }

    @Override
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
        return KaraCreateBlockEntityTypes.PAPER_DOOR.get();
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, Objects.requireNonNull(pPlacer), pStack);
        pLevel.getBlockEntity(pPos, KaraCreateBlockEntityTypes.PAPER_DOOR.get()).ifPresent((be) -> {
            be.initialize();
            be.getBehaviour(PaperDoorBehaviour.type).fromItem(pStack);
        });
    }
}
