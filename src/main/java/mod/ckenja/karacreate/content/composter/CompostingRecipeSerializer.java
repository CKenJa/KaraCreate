package mod.ckenja.karacreate.content.composter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class CompostingRecipeSerializer implements RecipeSerializer<CompostingRecipe> {
    @Override
    public CompostingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        NonNullList<ProcessingOutput> results = NonNullList.create();
        for (JsonElement je : GsonHelper.getAsJsonArray(json, "results")) {
            results.add(ProcessingOutput.deserialize(je));
        }
        CompostingRecipe recipe = new CompostingRecipe(recipeId, this, results);

        recipe.maxSpeed = GsonHelper.getAsFloat(json, "maxSpeed");
        recipe.minSpeed = GsonHelper.getAsFloat(json, "minSpeed");
        recipe.processingDuration = GsonHelper.getAsInt(json, "processingTime");
        return recipe;
    }

    @Nullable
    @Override
    public CompostingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        NonNullList<ProcessingOutput> results = NonNullList.create();
        for (int i = 0; i < size; i++)
            results.add(ProcessingOutput.read(buffer));
        CompostingRecipe recipe = new CompostingRecipe(recipeId, this, results);
        recipe.maxSpeed = buffer.readFloat();
        recipe.minSpeed = buffer.readFloat();
        recipe.processingDuration = buffer.readVarInt();
        return recipe;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CompostingRecipe recipe) {
        NonNullList<ProcessingOutput> outputs = recipe.results;
        buffer.writeVarInt(outputs.size());
        outputs.forEach(o -> o.write(buffer));
        buffer.writeFloat(recipe.maxSpeed);
        buffer.writeFloat(recipe.minSpeed);
        buffer.writeInt(recipe.processingDuration);
    }
}
