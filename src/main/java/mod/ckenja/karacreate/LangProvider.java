package mod.ckenja.karacreate;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class LangProvider extends LanguageProvider {

    public Map<String, String> langs = new HashMap<>();

    public LangProvider(PackOutput output, String modid, String locate) {
        super(output, modid, locate);
    }

    @Override
    protected void addTranslations() {
        langs.forEach(this::add);
    }
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> lang(String name, LangProvider prov) {
        return b -> {
            prov.langs.put(String.valueOf(new ResourceLocation(b.getOwner()
                    .getModid(), b.getName())), name);
            return b;
        };
    }
}
