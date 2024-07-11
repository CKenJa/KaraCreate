package mod.ckenja.karacreate.content.smallDoor;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import mod.ckenja.karacreate.infrastructure.mixin.DoorBlockAccessor;
import mod.ckenja.karacreate.infrastructure.mixin.SlidingDoorBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SlidingSmallDoorBlock extends SlidingDoorBlock{

    public SlidingSmallDoorBlock(Properties p_52737_, BlockSetType type, boolean folds) {
        super(p_52737_, type, folds);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos,
                                boolean pIsMoving) {
         boolean isPowered = pLevel.hasNeighborSignal(pPos);
        if (defaultBlockState().is(pBlock))
            return;
        if (isPowered == pState.getValue(POWERED))
            return;

        SlidingDoorBlockEntity be = getBlockEntity(pLevel, pPos);
        if (be != null && ((SlidingDoorBlockEntityAccessor)be).getDeferUpdate())
            return;

        BlockState changedState = pState.setValue(POWERED, isPowered)
                .setValue(OPEN, isPowered);
        if (isPowered)
            changedState = changedState.setValue(VISIBLE, false);

        if (isPowered != pState.getValue(OPEN)) {
            ((DoorBlockAccessor)this).invokePlaySound(null, pLevel, pPos, isPowered);
            pLevel.gameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
        }

        pLevel.setBlock(pPos, changedState, 2);
    }

    //以下SmallDoorBlockより
    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        //上下の状態を合わせるやつ。
        return pState;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection())
                .setValue(HINGE, SmallDoorBlock.getHinge(pContext, this))
                .setValue(POWERED, false)
                .setValue(OPEN, false)
                .setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull LivingEntity pPlacer, @NotNull ItemStack pStack) {
        //設置時に上側を置くやつ
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        //下がブロックじゃなかったり相方が無かったら壊れるやつ
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    public long getSeed(@NotNull BlockState pState, BlockPos pPos) {
        return Mth.getSeed(pPos.getX(), pPos.getY(),pPos.getZ());
    }
}
