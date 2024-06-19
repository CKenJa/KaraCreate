package mod.ckenja.karacreate;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static mod.ckenja.karacreate.KaraCreate.REGISTRATE;
import static mod.ckenja.karacreate.KaraCreate.JPLang;
import static mod.ckenja.karacreate.LangProvider.lang;

public class KaraCreateBlocks {
    public static final BlockSetType KARACREATE_WOOD = new BlockSetType("paper", true, SoundType.BAMBOO_WOOD,
            SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
            SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON);

    public static final BlockEntry<SlidingDoorBlock> SHOJI =
            REGISTRATE.block("shoji_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
                    .transform(lang("障子",JPLang))
                    .transform(BuilderTransformers.slidingDoor("shoji_door"))
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)
                            .sound(SoundType.WOOD)
                            .noOcclusion())
                    .transform(axeOnly())
                    .lang("Shoji")
                    .register();

    public static final BlockEntry<SlidingDoorBlock> FUSUMA =
            REGISTRATE.block("fusuma_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
                    .transform(BuilderTransformers.slidingDoor("fusuma_door"))
                    .transform(lang("襖",JPLang))
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)
                            .sound(SoundType.WOOD)
                            .noOcclusion())
                    .transform(axeOnly())
                    .lang("Fusuma")
                    .register();

    public static void register(){
        REGISTRATE.setCreativeTab(KaraCreateCreativeModeTabs.MAIN_TAB);
    }
}
