package mod.ckenja.karacreate.foundation.register;

import com.simibubi.create.foundation.utility.Lang;
import mod.ckenja.karacreate.KaraCreate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class KaraCreateBannerPatterns {
    public static final DeferredRegister<BannerPattern> PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, KaraCreate.MODID);

    public static void register(IEventBus modEventBus){
        PATTERNS.register(modEventBus);
        Patterns.init();
    }

    public enum Patterns {
        CHECKERED("karache"),
        HASH,
        ;
        public final RegistryObject<BannerPattern> pattern;

        Patterns(String pHashname) {
            pattern = PATTERNS.register(Lang.asId(name()), () -> new BannerPattern(pHashname));
        }

        Patterns() {
            pattern = PATTERNS.register(Lang.asId(name()), () -> new BannerPattern("kara" + name()));
        }

        public static void init() {
        }
    }
}