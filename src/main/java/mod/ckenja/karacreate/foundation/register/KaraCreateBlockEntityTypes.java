package mod.ckenja.karacreate.foundation.register;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mod.ckenja.karacreate.content.composter.ComposterBlockEntity;
import mod.ckenja.karacreate.content.composter.ComposterInstance;
import mod.ckenja.karacreate.content.composter.ComposterRenderer;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlockRenderer;

import static com.simibubi.create.Create.REGISTRATE;

public class KaraCreateBlockEntityTypes {
    public static final BlockEntityEntry<SlidingDoorBlockEntity> PAPER_DOOR =
            REGISTRATE.blockEntity("paper_door", SlidingDoorBlockEntity::new)
                    .renderer(() -> PaperDoorBlockRenderer::new)
                    .validBlocks(KaraCreateBlocks.SHOJI_DOOR,KaraCreateBlocks.FUSUMA_DOOR)
                    .register();

    public static final BlockEntityEntry<ComposterBlockEntity> COMPOSTER =
            REGISTRATE.blockEntity("composter", ComposterBlockEntity::new)
                    .instance(() -> ComposterInstance::new, false)
                    .validBlocks(KaraCreateBlocks.COMPOSTER)
                    .renderer(() -> ComposterRenderer::new)
                    .register();

    public static void register() {}
}
