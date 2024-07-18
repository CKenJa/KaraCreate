package mod.ckenja.karacreate.foundation.register;

import com.simibubi.create.foundation.utility.Lang;
import mod.ckenja.karacreate.KaraCreate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class KaraCreateBannerPatterns {
    public static final DeferredRegister<BannerPattern> PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, KaraCreate.MODID);

    public static void register(IEventBus modEventBus){
        PATTERNS.register(modEventBus);
        Patterns.init();
    }

    public enum Patterns {
        CHECKERED,
        HASH,
        HASH4,
        CIRCLE,
        MOUNTAIN,
        VERT_LEFT,
        VERT_RIGHT,
        BAMBOO,
        HEMP,
        BASKET,
        //ETERNAL,
        VAPOR,
        PINE,
        PAMPAS,
        HAZE,
        ARROW,
        ;
        public final RegistryObject<BannerPattern> pattern;

        Patterns() {
            pattern = PATTERNS.register(Lang.asId(name()), () -> new BannerPattern("kara" + name().toLowerCase(Locale.ROOT)));
        }

        public static void init() {
        }
    }
}