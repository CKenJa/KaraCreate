package mod.ckenja.karacreate.content.paperDoor;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class PaperDoorBlockItem extends BlockItem {
    private final Block block;

    public PaperDoorBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
        block = pBlock;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new PaperDoorItemRenderer(block);
            }
        });
    }
}
