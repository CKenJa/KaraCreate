package mod.ckenja.karacreate;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class KaraCreateTags {
    public enum BlockTags {
        SMALL_DOOR;
        public final TagKey<Block> tag;

        BlockTags() {
            ResourceLocation id = new ResourceLocation(KaraCreate.MODID, Lang.asId(name()));
            tag = net.minecraft.tags.BlockTags.create(id);
        }
        private static void init() {}
    }
    public enum ItemTags {
        SMALL_DOOR;
        public final TagKey<Item> tag;

        ItemTags() {
            ResourceLocation id = new ResourceLocation(KaraCreate.MODID, Lang.asId(name()));
            tag = net.minecraft.tags.ItemTags.create(id);
        }
        private static void init() {}
    }
    public static void init(){
        BlockTags.init();
        ItemTags.init();
    }
}