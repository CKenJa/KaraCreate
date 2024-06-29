package mod.ckenja.karacreate;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorRenderer;

import static com.simibubi.create.Create.REGISTRATE;

public class KaraCreateBlockEntityTypes {
    public static final BlockEntityEntry<SlidingDoorBlockEntity> PAPER_DOOR =
            REGISTRATE.blockEntity("paper_door", SlidingDoorBlockEntity::new)
                    .renderer(() -> PaperDoorRenderer::new)
                    .validBlocks(KaraCreateBlocks.SHOJI_DOOR)
                    .register();
    public static void register() {}
}
