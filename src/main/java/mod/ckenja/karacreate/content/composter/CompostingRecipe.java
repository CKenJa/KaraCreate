package mod.ckenja.karacreate.content.composter;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import mod.ckenja.karacreate.foundation.register.KaraCreateRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class CompostingRecipe extends AbstractCrushingRecipe {

    public CompostingRecipe(ProcessingRecipeParams params) {
        super(KaraCreateRecipeTypes.COMPOSTING, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
}