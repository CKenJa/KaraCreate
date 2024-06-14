package mod.ckenja.karacreate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.BiConsumer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KaraCreate.MODID)
public class KaraCreate {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "karacreate";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE));
    }

    public KaraCreate() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        KaraCreateBlocks.register();
        KaraCreateBlockEntityTypes.register();
        KaraCreateCreativeModeTabs.register(modEventBus);
        REGISTRATE.addDataGenerator(ProviderType.LANG, prov->{
            provideDefaultLang("interface", prov::add);
            provideDefaultLang("tooltips", prov::add);
        });

    }

    public static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/karacreate/lang/default/" + fileName + ".json";
        LOGGER.debug("Loading default language file from path: {}", path);
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            String errorMsg = String.format("Could not find default lang file: %s", path);
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

}
