package mod.ckenja.karacreate;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.RegistryObject;

import static mod.ckenja.karacreate.KaraCreate.PATTERNS;

public enum KaraCreateBannerPatterns{
    CHECKERED("karache"),
    HASH,
    ;
    public final RegistryObject<BannerPattern> pattern;

    KaraCreateBannerPatterns(String pHashname){
        pattern = PATTERNS.register(Lang.asId(name()), ()-> new BannerPattern(pHashname));
    }

    KaraCreateBannerPatterns(){
        pattern = PATTERNS.register(Lang.asId(name()), ()-> new BannerPattern("kara"+name()));
    }

    public static void init(){}
}
