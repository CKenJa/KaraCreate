package mod.ckenja.karacreate.content.paperDoor;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.level.block.entity.BannerBlockEntity.createPatterns;

public class PaperDoorBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<PaperDoorBehaviour> type = new BehaviourType<>();

    @Nullable
    private ListTag itemPatterns;
    @Nullable
    private List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

    public PaperDoorBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Override
    public BehaviourType<?> getType() {
        return type;
    }

    public void fromItem(ItemStack pItem) {
        this.itemPatterns = BannerBlockEntity.getItemPatterns(pItem);
        this.patterns = null;
    }

    public List<Pair<Holder<BannerPattern>, DyeColor>> getPatterns() {
        if(patterns == null)
            patterns = createPatterns(DyeColor.WHITE, this.itemPatterns);
        return patterns;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if(itemPatterns != null)
            nbt.put("Patterns", itemPatterns);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        itemPatterns = nbt.getList("Patterns",Tag.TAG_COMPOUND);
        patterns = null;
    }
}
