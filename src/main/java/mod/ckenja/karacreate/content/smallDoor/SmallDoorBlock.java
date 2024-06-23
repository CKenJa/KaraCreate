package mod.ckenja.karacreate.content.smallDoor;

import mod.ckenja.karacreate.mixin.DoorBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SmallDoorBlock extends DoorBlock {
    public SmallDoorBlock(Properties pProperties, BlockSetType pType) {
        super(pProperties, pType);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        //上下の状態を合わせるやつ。
        return pState;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        boolean $$3 = level.hasNeighborSignal(pos);
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection())
                .setValue(HINGE, getHinge(pContext, this))
                .setValue(POWERED, $$3)
                .setValue(OPEN, $$3)
                .setValue(HALF, DoubleBlockHalf.LOWER);
    }
    
    public static DoorHingeSide getHinge(BlockPlaceContext pContext, Block pBlock) {
        BlockGetter level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        Direction direction = pContext.getHorizontalDirection();
        Direction left = direction.getCounterClockWise();
        BlockPos leftPos = pos.relative(left);
        BlockState leftState = level.getBlockState(leftPos);
        Direction right = direction.getClockWise();
        BlockPos rightPos = pos.relative(right);
        BlockState rightState = level.getBlockState(rightPos);
        int collisionCount = (leftState.isCollisionShapeFullBlock(level, leftPos) ? -1 : 0) + (rightState.isCollisionShapeFullBlock(level, rightPos) ? 1 : 0);
        boolean isLeftDoor = leftState.is(pBlock);
        boolean isRightDoor = rightState.is(pBlock);
        if ((!isLeftDoor || isRightDoor) && collisionCount <= 0) {
            if ((!isRightDoor || isLeftDoor) && collisionCount == 0) {
                int stepX = direction.getStepX();
                int stepZ = direction.getStepZ();
                Vec3 clickLocation = pContext.getClickLocation();
                double xOffset = clickLocation.x - (double) pos.getX();
                double zOffset = clickLocation.z - (double) pos.getZ();
                return (stepX >= 0 || !(zOffset < 0.5)) && (stepX <= 0 || !(zOffset > 0.5)) &&
                        (stepZ >= 0 || !(xOffset > 0.5)) && (stepZ <= 0 || !(xOffset < 0.5))
                        ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            }
            return DoorHingeSide.LEFT;
        }
        return DoorHingeSide.RIGHT;
    }


    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull LivingEntity pPlacer, @NotNull ItemStack pStack) {
        //設置時に上側を置くやつ
    }

    @Override
    public void neighborChanged(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        boolean signal = pLevel.hasNeighborSignal(pPos);
        if (!this.defaultBlockState().is(pBlock) && signal != pState.getValue(POWERED)) {
            if (signal != pState.getValue(OPEN)) {
                ((DoorBlockAccessor)this).invokePlaySound(null, pLevel, pPos, signal);
                pLevel.gameEvent(null, signal ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
            }

            pLevel.setBlock(pPos, pState.setValue(POWERED, signal).setValue(OPEN, signal), 2);
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        //下がブロックじゃなかったり相方が無かったら壊れるやつ
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER;
   }

    public long getSeed(@NotNull BlockState pState, BlockPos pPos) {
        return Mth.getSeed(pPos.getX(), pPos.getY(),pPos.getZ());
    }
}