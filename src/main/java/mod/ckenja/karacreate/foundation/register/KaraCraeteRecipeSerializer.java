package mod.ckenja.karacreate.foundation.register;

import mod.ckenja.karacreate.KaraCreate;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorDecorationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class KaraCraeteRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, KaraCreate.MODID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<PaperDoorDecorationRecipe>> PAPER_DOOR_DECORATION = RECIPE_SERIALIZERS.register("crafting_paper_door_decoration", () -> new SimpleCraftingRecipeSerializer<>(PaperDoorDecorationRecipe::new));
}
