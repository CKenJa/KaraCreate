package mod.ckenja.karacreate.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static mod.ckenja.karacreate.KaraCreate.LOGGER;

public class LanguageManager {
    private final List<NonNullConsumer<? super LanguageProvider>> translationGenerators = new ArrayList<>();
    private final String locale;
    private final String modid;

    public LanguageManager(String modid, String locale) {
        this.locale = locale;
        this.modid = modid;
        addGenerator(prov -> provideDefaultLang(prov::add, modid, locale));
    }

    public void addGenerator(NonNullConsumer<LanguageProvider> generator){
        this.translationGenerators.add(generator);
    }

    public static void provideDefaultLang(BiConsumer<String, String> consumer, String modid, String locale) {
        String path = "assets/" + modid + "/lang/default/" + locale + ".json";
        LOGGER.debug("Loading default language file from path: {}", path);
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            String errorMsg = String.format("Could not find default lang file: %s", path);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue().getAsString()));
    }

    public void initializeProvider(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        LanguageProvider prov = new LanguageProvider(gen.getPackOutput(), modid, locale) {
            @Override
            protected void addTranslations() {
                translationGenerators.forEach(generator -> generator.accept(this));
            }
        };
        gen.addProvider(event.includeClient(), prov);
    }

    public <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> translation(String name) {
        return builder -> {
            addGenerator(prov-> prov.add(builder.getOwner().get(builder.getName(), builder.getRegistryKey()).get().getDescriptionId(),name));
            return builder;
        };
    }
}
