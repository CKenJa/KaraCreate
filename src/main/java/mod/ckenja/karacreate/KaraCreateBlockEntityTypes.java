package mod.ckenja.karacreate;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.Create.REGISTRATE;

public class KaraCreateBlockEntityTypes {

    public static final BlockEntityEntry<SlidingDoorBlockEntity> WOODEN_SLIDING_DOOR =
            REGISTRATE.blockEntity("wooden_sliding_door", SlidingDoorBlockEntity::new)
                    .renderer(() -> SlidingDoorRenderer::new)
                    .validBlocks(KaraCreateBlocks.PAPER_DOOR)
                    .register();

    public static void register() {}
}
