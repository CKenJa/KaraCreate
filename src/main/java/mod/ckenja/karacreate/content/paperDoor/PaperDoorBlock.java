package mod.ckenja.karacreate.content.paperDoor;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import mod.ckenja.karacreate.KaraCreateBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;

public class PaperDoorBlock extends SlidingDoorBlock {

    public ListTag itemPatterns;

    public PaperDoorBlock(Properties p_52737_, BlockSetType type) {
        super(p_52737_, type, false);
    }

    @Override
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
        return KaraCreateBlockEntityTypes.PAPER_DOOR.get();
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        pLevel.getBlockEntity(pPos, KaraCreateBlockEntityTypes.PAPER_DOOR.get()).ifPresent((be) -> {
            if(pLevel.isClientSide) {
                PaperDoorBehaviour behaviour = be.getBehaviour(PaperDoorBehaviour.type);
                if(behaviour != null)
                    behaviour.fromItem(pStack);
            }
        });
        /*if(pLevel.isClientSide)
            this.itemPatterns = getItemPatterns(pStack);*/
    }
}
