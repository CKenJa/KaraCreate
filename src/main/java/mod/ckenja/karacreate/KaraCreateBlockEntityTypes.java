package mod.ckenja.karacreate;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlockRenderer;

import static com.simibubi.create.Create.REGISTRATE;

public class KaraCreateBlockEntityTypes {
    public static final BlockEntityEntry<SlidingDoorBlockEntity> PAPER_DOOR =
            REGISTRATE.blockEntity("paper_door", SlidingDoorBlockEntity::new)
                    .renderer(() -> PaperDoorBlockRenderer::new)
                    .validBlocks(KaraCreateBlocks.SHOJI_DOOR,KaraCreateBlocks.FUSUMA_DOOR)
                    .register();
    public static void register() {}
}
