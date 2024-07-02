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
import mod.ckenja.karacreate.content.paperDoor.PaperDoorItemRenderer;
import mod.ckenja.karacreate.util.LanguageManager;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
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
    public static final BlockEntityWithoutLevelRenderer RENDERER = new PaperDoorItemRenderer();

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
        KaraCreateBlockEntityTypes.register();
        KaraCreateBlocks.register();
        KaraCreateCreativeModeTabs.register(modEventBus);
        modEventBus.addListener(Japanese::initializeProvider);
        REGISTRATE.addDataGenerator(ProviderType.LANG, (prov)->provideDefaultLang(prov::add, MODID, "en_us"));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> KaraCreateClient.onConstructor(modEventBus, forgeEventBus));
    }

    public static ResourceLocation asResource(String path){
        return new ResourceLocation(MODID,path);
    }
}
