package mod.ckenja.karacreate.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mod.ckenja.karacreate.KaraCreate;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlockItem;
import mod.ckenja.karacreate.foundation.register.KaraCreateTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@JeiPlugin
@SuppressWarnings("unused")
public class KaraCreateJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return KaraCreate.asResource("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, KaraCreateJEI.createPaperDoorDecorationRecipe());
    }

    public static List<CraftingRecipe> createPaperDoorDecorationRecipe() {
        Iterable<Holder<Item>> banners = BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.BANNERS);

        Set<DyeColor> colors = EnumSet.noneOf(DyeColor.class);

        return StreamSupport.stream(banners.spliterator(), false)
                .filter(Holder::isBound)
                .map(Holder::value)
                .filter(BannerItem.class::isInstance)
                .map(BannerItem.class::cast)
                .filter(item -> colors.add(item.getColor()))
                .flatMap(banner -> BuiltInRegistries.ITEM.getTag(KaraCreateTags.ItemTags.PAPER_DOOR.tag)
                        .stream()
                        .flatMap(HolderSet.ListBacked::stream)
                        .map(Holder::value)
                        .filter(PaperDoorBlockItem.class::isInstance)
                        .map(door -> createRecipe(banner, (PaperDoorBlockItem) door))
                )
                .toList();
        }

        private static CraftingRecipe createRecipe(BannerItem banner, PaperDoorBlockItem door) {
            NonNullList<Ingredient> inputs = NonNullList.of(
                    Ingredient.EMPTY,
                    Ingredient.of(banner),
                    Ingredient.of(door)
            );

            ItemStack output = createOutput(banner, door);

            ResourceLocation id = KaraCreate.asResource("karacreate.paper_door.decoration." + output.getDescriptionId());
            return new ShapelessRecipe(id, "karacreate.paper_door.decoration", CraftingBookCategory.MISC, output, inputs);
        }

        private static ItemStack createOutput(BannerItem banner, PaperDoorBlockItem door) {
            DyeColor color = banner.getColor();
            ItemStack output = new ItemStack(door);
            CompoundTag tag = new CompoundTag();
            tag.putInt("Base", color.getId());
            BlockItem.setBlockEntityData(output, BlockEntityType.BANNER, tag);
            return output;
        }}
