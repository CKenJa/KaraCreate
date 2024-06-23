package mod.ckenja.karacreate;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.providers.ProviderType;
import mod.ckenja.karacreate.util.LanguageManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static mod.ckenja.karacreate.util.LanguageManager.provideDefaultLang;

@Mod(KaraCreate.MODID)
public class KaraCreate {
    public static final String MODID = "karacreate";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static LanguageManager Japanese = new LanguageManager(MODID,"ja_jp");

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE));
    }

    public KaraCreate() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        KaraCreateTags.init();
        KaraCreateBlocks.register();
        KaraCreateCreativeModeTabs.register(modEventBus);
        modEventBus.addListener(Japanese::initializeProvider);
        REGISTRATE.addDataGenerator(ProviderType.LANG, (prov)->provideDefaultLang(prov::add, MODID, "en_us"));
    }
}
