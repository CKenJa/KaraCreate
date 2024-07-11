package mod.ckenja.karacreate.infrastructure.data;

import mod.ckenja.karacreate.KaraCreate;
import mod.ckenja.karacreate.foundation.register.KaraCreateBannerPatterns;
import mod.ckenja.karacreate.foundation.register.KaraCreateTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class KaraCreateBannerPatternTagsProvider extends BannerPatternTagsProvider {
    public KaraCreateBannerPatternTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, KaraCreate.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        TagAppender<BannerPattern> tag = this.tag(KaraCreateTags.JAPAN_BANNER_PATTERN);
        for(KaraCreateBannerPatterns.Patterns value: KaraCreateBannerPatterns.Patterns.values())
            tag.add(Objects.requireNonNull(value.pattern.getKey()));
    }

    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(), new KaraCreateBannerPatternTagsProvider(output, provider, helper));
    }
}
