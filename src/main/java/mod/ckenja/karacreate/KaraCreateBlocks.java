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

public class KaraCreateBlocks {
    public static final BlockSetType KARACREATE_WOOD = new BlockSetType("paper", true, SoundType.BAMBOO_WOOD,
            SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
            SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON);

    public static final BlockEntry<SlidingDoorBlock> PAPER_DOOR =
            REGISTRATE.block("paper_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
            .transform(BuilderTransformers.slidingDoor("paper"))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)
                    .sound(SoundType.WOOD)
                    .noOcclusion())
            .transform(axeOnly())
            .lang("Paper door")
            .register();

    public static void register(){
        REGISTRATE.setCreativeTab(KaraCreateCreativeModeTabs.MAIN_TAB);
    }
}
