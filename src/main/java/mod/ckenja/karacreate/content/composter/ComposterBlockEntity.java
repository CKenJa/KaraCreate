package mod.ckenja.karacreate.content.composter;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.minecraft.world.level.block.ComposterBlock.COMPOSTABLES;
import static net.minecraft.world.level.block.ComposterBlock.LEVEL;

public class ComposterBlockEntity extends KineticBlockEntity {

    //考えるの面倒だから残す
    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;

    public LazyOptional<IItemHandler> itemCapability;
    public int timer;
    private CompostingRecipe lastRecipe;

    public ComposterBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(9);
        itemCapability = LazyOptional.of(ComposterBlockEntity.ComposterInventoryHandler::new);
    }

    @Override
    public void tick() {
        super.tick();

        //TODO 見直し
        if (getSpeed() == 0)
            return;
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i)
                    .getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                return;
            }
            if (timer <= 0)
                process();
            return;
        }

        if (lastRecipe == null || !lastRecipe.appliesTo(speed)) {
            Optional<CompostingRecipe> recipe = CompostingRecipe.find(level, speed);
            if (recipe.isEmpty()) {
                timer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get();
                timer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }

        timer = lastRecipe.getProcessingDuration();
        sendData();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    private void process() {
        if (getBlockState().getValue(LEVEL) != 8) {
            ItemStack stack = inputInv.getStackInSlot(0);
            BlockState state = getBlockState();
            int i = state.getValue(LEVEL);
            float f = COMPOSTABLES.getFloat(stack.getItem());
            if ((i != 0 || !(f > 0.0F)) && !(level.getRandom().nextDouble() < (double)f))
                return;
            BlockState blockstate = state.setValue(LEVEL, i+1);
            BlockPos pos = getBlockPos();
            level.setBlock(pos, blockstate, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, blockstate));
            level.playSound(null, pos, SoundEvents.COMPOSTER_FILL_SUCCESS,
                    SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            stack.shrink(1);
            inputInv.setStackInSlot(0, stack);
        }else {

            if (lastRecipe == null || !lastRecipe.appliesTo(speed)) {
                Optional<CompostingRecipe> recipe = CompostingRecipe.find(level, speed);
                if (!recipe.isPresent())
                    return;
                lastRecipe = recipe.get();
            }

            BlockState state = getBlockState().setValue(LEVEL, 0);
            level.setBlock(getBlockPos(), state, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(null, state));
            lastRecipe.rollResults()
                    .forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));

            sendData();
            setChanged();
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", timer);
        compound.put("InputInventory", inputInv.serializeNBT());
        compound.put("OutputInventory", outputInv.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("Timer");
        inputInv.deserializeNBT(compound.getCompound("InputInventory"));
        outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
        super.read(compound, clientPacket);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f)*8, 1, 512);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap))
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }

    public class ComposterInventoryHandler extends CombinedInvWrapper {
        public ComposterInventoryHandler() {
            super(inputInv, outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;//出力スロットに入れようとしてたら跳ね返す
            return COMPOSTABLES.containsKey(stack.getItem()) && super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return stack;
            if (!isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (inputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }
    }
}
