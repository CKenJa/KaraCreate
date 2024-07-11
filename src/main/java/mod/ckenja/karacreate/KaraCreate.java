package mod.ckenja.karacreate;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.providers.ProviderType;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBehaviour;
import mod.ckenja.karacreate.foundation.register.*;
import mod.ckenja.karacreate.infrastructure.data.KaraCreateBannerPatternTagsProvider;
import mod.ckenja.karacreate.infrastructure.lang.BannerPatternLangGenerators;
import mod.ckenja.karacreate.infrastructure.lang.LanguageManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static mod.ckenja.karacreate.infrastructure.lang.LanguageManager.provideDefaultLang;

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
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addGenericListener(SlidingDoorBlockEntity.class, (BlockEntityBehaviourEvent<SlidingDoorBlockEntity> event) -> {
            SmartBlockEntity be = event.getBlockEntity();
            if (be.getType() == KaraCreateBlockEntityTypes.PAPER_DOOR.get())
                event.attach(new PaperDoorBehaviour(be));
        });
        KaraCreateTags.init();
        KaraCreateItems.register();
        KaraCreateBlockEntityTypes.register();
        KaraCreateBlocks.register();
        KaraCreateBannerPatterns.register(modEventBus);
        KaraCreateCreativeModeTabs.register(modEventBus);
        KaraCraeteRecipeSerializer.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(Japanese::initializeProvider);
        modEventBus.addListener(EventPriority.LOWEST, KaraCreateBannerPatternTagsProvider::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> KaraCreateClient.onConstructor(modEventBus));

        REGISTRATE.addDataGenerator(ProviderType.LANG, (prov) -> provideDefaultLang(prov::add, KaraCreate.MODID, "en_us"));
        REGISTRATE.addDataGenerator(ProviderType.LANG, BannerPatternLangGenerators::en_us);
        Japanese.addGenerator(BannerPatternLangGenerators::ja_jp);
    }

    public static ResourceLocation asResource(String path){
        return new ResourceLocation(MODID,path);
    }
}
