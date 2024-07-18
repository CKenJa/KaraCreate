package mod.ckenja.karacreate.content.composter;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import mod.ckenja.karacreate.foundation.register.KaraCreateRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class CompostingRecipe implements Recipe<RecipeWrapper> {

    public NonNullList<ProcessingOutput> results;
    protected float maxSpeed;
    protected float minSpeed;
    private final ResourceLocation id;
    private final CompostingRecipeSerializer serializer;
    protected int processingDuration;
    private Supplier<ItemStack> forcedResult;

    public CompostingRecipe(ResourceLocation id, CompostingRecipeSerializer serializer, NonNullList<ProcessingOutput> results) {
        this.forcedResult = null;
        this.id = id;
        this.serializer = serializer;
        this.results = results;
    }

    public static Optional<CompostingRecipe> find(Level world, float speed) {
        List<CompostingRecipe> all = world.getRecipeManager()
                .getAllRecipesFor(KaraCreateRecipeTypes.COMPOSTING.getType());
        for (CompostingRecipe compostingRecipe : all) {
            if (!compostingRecipe.appliesTo(speed))
                continue;
            return Optional.of(compostingRecipe);
        }
        return Optional.empty();
    }

    boolean appliesTo(float speed) {
        return minSpeed <= Math.abs(speed) && Math.abs(speed) <= maxSpeed;
    }

    public void enforceNextResult(Supplier<ItemStack> stack) {
        forcedResult = stack;
    }

    public List<ItemStack> rollResults() {
        return rollResults(this.getRollableResults());
    }

    public List<ItemStack> rollResults(List<ProcessingOutput> rollableResults) {
        List<ItemStack> results = new ArrayList<>();
        for (int i = 0; i < rollableResults.size(); i++) {
            ProcessingOutput output = rollableResults.get(i);
            ItemStack stack = i == 0 && forcedResult != null ? forcedResult.get() : output.rollOutput();
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }

    public List<ProcessingOutput> getRollableResults() {
        return results;
    }

    @Override
    @Deprecated
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return getRollableResults().isEmpty() ? ItemStack.EMPTY
                : getRollableResults().get(0)
                .getStack();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return KaraCreateRecipeTypes.COMPOSTING.getType();
    }

    public int getProcessingDuration() {
        return processingDuration;
    }
}