package mod.ckenja.karacreate.foundation.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BannerPatternItem;

import static mod.ckenja.karacreate.KaraCreate.Japanese;
import static mod.ckenja.karacreate.KaraCreate.REGISTRATE;

public class KaraCreateItems {
    public static final ItemEntry<BannerPatternItem> PATTERN =
            REGISTRATE.item("japanese_banner_pattern", p -> new BannerPatternItem(KaraCreateTags.JAPAN_BANNER_PATTERN, p))
                    .onRegister(Japanese.item("旗の伝統文様"))
                    .register();

    public static void register() {
        REGISTRATE.setCreativeTab(KaraCreateCreativeModeTabs.MAIN_TAB);
    }
}
