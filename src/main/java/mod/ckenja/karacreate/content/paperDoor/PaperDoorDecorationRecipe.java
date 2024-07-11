package mod.ckenja.karacreate.content.paperDoor;

import mod.ckenja.karacreate.foundation.register.KaraCreateTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import static mod.ckenja.karacreate.foundation.register.KaraCraeteRecipeSerializer.PAPER_DOOR_DECORATION;

public class PaperDoorDecorationRecipe extends CustomRecipe {

    public PaperDoorDecorationRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    public boolean matches(CraftingContainer pInv, @NotNull Level pLevel) {
        ItemStack paperDoor = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;

        for(int i = 0; i < pInv.getContainerSize(); ++i) {
            ItemStack stack = pInv.getItem(i);
            if (!stack.isEmpty())
                if (stack.getItem() instanceof BannerItem) {
                    if (!banner.isEmpty())
                        return false;
                    banner = stack;
                } else {
                    if (!stack.is(KaraCreateTags.ItemTags.PAPER_DOOR.tag))
                        return false;
                    if (!paperDoor.isEmpty())
                        return false;
                    if (BlockItem.getBlockEntityData(stack) != null)
                        return false;
                    paperDoor = stack;
                }
        }
        return !paperDoor.isEmpty() && !banner.isEmpty();
    }

    public @NotNull ItemStack assemble(CraftingContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack paper_door = ItemStack.EMPTY;

        for(int i = 0; i < pContainer.getContainerSize(); ++i) {
            ItemStack stack = pContainer.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem)
                    banner = stack;
                else if (stack.is(KaraCreateTags.ItemTags.PAPER_DOOR.tag))
                    paper_door = stack.copy();
            }
        }

        if (paper_door.isEmpty())
            return paper_door;
        else {
            CompoundTag bannerTag = BlockItem.getBlockEntityData(banner);
            CompoundTag resultTag = bannerTag == null ? new CompoundTag() : bannerTag.copy();
            resultTag.putInt("Base", ((BannerItem) banner.getItem()).getColor().getId());
            BlockItem.setBlockEntityData(paper_door, BlockEntityType.BANNER, resultTag);
            paper_door.setCount(1);
            return paper_door;
        }
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return PAPER_DOOR_DECORATION.get();
    }
}