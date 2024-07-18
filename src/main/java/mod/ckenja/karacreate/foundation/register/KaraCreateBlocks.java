package mod.ckenja.karacreate.foundation.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mod.ckenja.karacreate.content.composter.MechanicalComposterBlock;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlock;
import mod.ckenja.karacreate.content.paperDoor.PaperDoorBlockItem;
import mod.ckenja.karacreate.content.smallDoor.SlidingSmallDoorBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
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
                    .transform(woodenSlidingDoor(false))
                    .transform(paperDoorItem())
                    .onRegister(Japanese.block("障子"))
                    .properties(p -> p.mapColor(MapColor.WOOD))
                    .register();

    public static final BlockEntry<SlidingDoorBlock> SNOW_VIEWING_SHOJI_DOOR =
            REGISTRATE.block("snow_viewing_shoji_door", p -> new SlidingDoorBlock(p, KARACREATE_WOOD, false))
                    .transform(woodenSlidingDoor(false))
                    .item(PaperDoorBlockItem::new)
                    .tag(ItemTags.DOORS)
                    .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                    .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("item/template_paper_door")))
                    .build()
                    .onRegister(Japanese.block("雪見障子"))
                    .properties(p -> p.mapColor(MapColor.WOOD))
                    .register();

    public static final BlockEntry<PaperDoorBlock> FUSUMA_DOOR =
            REGISTRATE.block("fusuma_door", p -> new PaperDoorBlock(p, KARACREATE_WOOD))
                    .transform(woodenSlidingDoor(true))
                    .transform(paperDoorItem())
                    .onRegister(Japanese.block("襖"))
                    .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
                    .register();

    public static final BlockEntry<SlidingSmallDoorBlock> SMALL_SHOJI_DOOR =
            REGISTRATE.block("small_shoji_door", p -> new SlidingSmallDoorBlock(p, KARACREATE_WOOD,false))
                    .transform(smallDoor(false))
                    .onRegister(Japanese.block("欄間障子"))
                    .properties(p -> p.mapColor(MapColor.WOOD))
                    .register();

    public static final BlockEntry<SlidingSmallDoorBlock> SMALL_FUSUMA_DOOR =
            REGISTRATE.block("small_fusuma_door", p -> new SlidingSmallDoorBlock(p, KARACREATE_WOOD,false))
                    .transform(smallDoor(true))
                    .onRegister(Japanese.block("半襖"))
                    .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
                    .transform(axeOnly())
                    .register();

    public static final BlockEntry<MechanicalComposterBlock> COMPOSTER =
            REGISTRATE.block("mechanical_composter",MechanicalComposterBlock::new)
    		        .initialProperties(SharedProperties::stone)
		            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
                    .transform(pickaxeOnly())
                    .onRegister(Japanese.block("メカニカルコンポスター"))
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .transform(BlockStressDefaults.setImpact(4.0))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static void register() {
        REGISTRATE.setCreativeTab(KaraCreateCreativeModeTabs.MAIN_TAB);
    }

    public static <B extends SlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> woodenSlidingDoor(boolean knob) {
        return b -> b.initialProperties(() -> Blocks.BAMBOO_DOOR)
                .properties(p -> p.requiresCorrectToolForDrops()
                        .strength(3.0F, 6.0F)
                        .noOcclusion())
                .blockstate(doorBlockProvider(knob))
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(axeOnly())
                .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                .onRegister(movementBehaviour(new SlidingDoorMovementBehaviour()))
                .tag(BlockTags.DOORS)
                .tag(BlockTags.WOODEN_DOORS) // for villager AI
                .tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
                .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)));
    }

    private static <B extends SlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> smallDoor(boolean knob) {
        return b -> b.tag(KaraCreateTags.BlockTags.SMALL_DOOR.tag)
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/"+c.getName()+"/block_right")))
            .tag(KaraCreateTags.ItemTags.SMALL_DOOR.tag)
            .build()
            .initialProperties(() -> Blocks.BAMBOO_DOOR)
            .blockstate((c, p) -> {
            p.getVariantBuilder(c.get()).forAllStatesExcept(state -> {
                if(knob) {
                    ModelFile rightModel = AssetLookup.partialBaseModel(c, p, "right");
                    ModelFile leftModel = AssetLookup.partialBaseModel(c, p, "left");
                    boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
                    int xRot = 0;
                    int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
                    yRot %= 360;
                    return ConfiguredModel.builder().modelFile(right ? rightModel : leftModel)
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .build();
                } else {
                    ModelFile model = AssetLookup.standardModel(c, p);
                    int xRot = 0;
                    int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
                    yRot %= 360;
                    return ConfiguredModel.builder().modelFile(model)
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .build();
                }
            }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
        });
    }

    public static <B extends SlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> paperDoorItem() {
        return b -> b.tag(KaraCreateTags.BlockTags.PAPER_DOOR.tag)
                .item(PaperDoorBlockItem::new)
                .tag(ItemTags.DOORS)
                .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                .tag(KaraCreateTags.ItemTags.PAPER_DOOR.tag)
                .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("item/template_paper_door")))
                .build();
    }

    public static <T extends DoorBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> doorBlockProvider(boolean knob) {
        if(knob)
            return (c, p) -> {
                ModelFile bottom_left = AssetLookup.partialBaseModel(c, p, "bottom_left");
                ModelFile bottom_right = AssetLookup.partialBaseModel(c, p, "bottom_right");
                ModelFile top_left = AssetLookup.partialBaseModel(c, p, "top_left");
                ModelFile top_right = AssetLookup.partialBaseModel(c, p, "top_right");
                p.doorBlock(c.get(), bottom_left, bottom_left, bottom_right, bottom_right, top_left, top_left, top_right, top_right);
            };
        else
            return (c, p) -> {
                ModelFile bottom = AssetLookup.partialBaseModel(c, p, "bottom");
                ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                p.doorBlock(c.get(), bottom, bottom, bottom, bottom, top, top, top, top);
            };
    }
}