package mod.ckenja.karacreate;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlock;
import mod.ckenja.karacreate.content.smallDoor.SlidingSmallDoorBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static mod.ckenja.karacreate.KaraCreate.REGISTRATE;
import static mod.ckenja.karacreate.KaraCreate.Japanese;

public class KaraCreateBlocks {
    public static final BlockSetType KARACREATE_WOOD = new BlockSetType("paper", true, SoundType.BAMBOO_WOOD,
            SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
            SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
            SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON);

    public static final BlockEntry<PaperDoorBlock> SHOJI_DOOR =
            REGISTRATE.block("shoji_door", p -> new PaperDoorBlock(p, KARACREATE_WOOD))
                    .transform(woodenSlidingDoor("shoji"))
                    .transform(Japanese.translation("障子"))
                    .properties(p -> p.mapColor(MapColor.WOOD))
                    .register();

    public static final BlockEntry<SlidingDoorBlock> SNOW_VIEWING_SHOJI_DOOR =
            REGISTRATE.block("snow_viewing_shoji_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
                    .transform(woodenSlidingDoor("snow_viewing_shoji"))
                    .transform(Japanese.translation("雪見障子"))
                    .properties(p -> p.mapColor(MapColor.WOOD))
                    .register();

    public static final BlockEntry<SlidingDoorBlock> FUSUMA_DOOR =
            REGISTRATE.block("fusuma_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
                    .transform(woodenSlidingDoor("fusuma"))
                    .transform(Japanese.translation("襖"))
                    .properties(p -> p.mapColor(MapColor.WOOL))
                    .register();

    public static final BlockEntry<SlidingSmallDoorBlock> SMALL_SHOJI_DOOR =
            REGISTRATE.block("small_shoji_door", p -> new SlidingSmallDoorBlock(p, KARACREATE_WOOD,false))
                    .transform(slidingSmallDoor())
                    .initialProperties(() -> Blocks.BAMBOO_DOOR)
                    .properties(p -> p.mapColor(MapColor.WOOL))
                    .transform(Japanese.translation("欄間障子"))
                    .transform(axeOnly())
                    .register();

    public static final BlockEntry<SlidingSmallDoorBlock> SMALL_FUSUMA_DOOR =
            REGISTRATE.block("small_fusuma_door", p -> new SlidingSmallDoorBlock(p, KARACREATE_WOOD,false))
                    .transform(slidingSmallDoor())
                    .initialProperties(() -> Blocks.BAMBOO_DOOR)
                    .properties(p -> p.mapColor(MapColor.WOOL))
                    .transform(Japanese.translation("半襖"))
                    .transform(axeOnly())
                    .register();

    public static void register() {
        REGISTRATE.setCreativeTab(KaraCreateCreativeModeTabs.MAIN_TAB);
    }

    public static <B extends SlidingSmallDoorBlock, P>NonNullUnaryOperator<BlockBuilder<B, P>> slidingSmallDoor() {
        return b -> b.blockstate((c, p) -> {
                    //ModelFile model = p.models().getExistingFile(p.modLoc("block/small_" + type + "_door"));
                    ModelFile model = AssetLookup.standardModel(c, p);
                    p.getVariantBuilder(c.get()).forAllStatesExcept(state -> {
                        int xRot = 0;
                        int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
                        yRot %= 360;
                        return ConfiguredModel.builder().modelFile(model)
                                .rotationX(xRot)
                                .rotationY(yRot)
                                .build();
                    }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
                })
                .tag(KaraCreateTags.BlockTags.SMALL_DOOR.tag)
                .item()
                .tag(KaraCreateTags.ItemTags.SMALL_DOOR.tag)
                .build();
    }

    public static <B extends SlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> woodenSlidingDoor(String type) {
        return b -> b.initialProperties(() -> Blocks.BAMBOO_DOOR)
                .properties(p -> p.requiresCorrectToolForDrops()
                        .strength(3.0F, 6.0F)
                        .noOcclusion())
                .blockstate((c, p) -> {
                    ModelFile bottom = AssetLookup.partialBaseModel(c, p, "bottom");
                    ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                    p.doorBlock(c.get(), bottom, bottom, bottom, bottom, top, top, top, top);
                })
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(axeOnly())
                .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                .onRegister(movementBehaviour(new SlidingDoorMovementBehaviour()))
                .tag(BlockTags.DOORS)
                .tag(BlockTags.WOODEN_DOORS) // for villager AI
                .tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
                .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)))
                .item()
                .tag(ItemTags.DOORS)
                .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                .model((c, p) -> p.blockSprite(c, p.modLoc("item/" + type + "_door")))
                .build();
    }
}