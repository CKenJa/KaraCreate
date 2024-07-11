package mod.ckenja.karacreate.content.paperDoor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import java.util.EnumSet;
import java.util.List;

import mod.ckenja.karacreate.foundation.render.KaraCreateModelLayers;
import mod.ckenja.karacreate.infrastructure.mixin.SlidingDoorBlockEntityAccessor;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.RenderType.cutoutMipped;

public class PaperDoorBlockRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    private final ModelPart modelPart;

    public PaperDoorBlockRenderer(BlockEntityRendererProvider.Context ctx) {
        modelPart = ctx.bakeLayer(KaraCreateModelLayers.PAPER_DOOR);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("1", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, 0.0F, 0.0F, 20.0F, 40.0F, 0.0F, EnumSet.of(Direction.SOUTH)), PartPose.ZERO);
        partDefinition.addOrReplaceChild("2", CubeListBuilder.create().texOffs(1, 1).addBox(0.0F, 0.0F, 2.875F, 20.0F, 40.0F, 0.0F, EnumSet.of(Direction.NORTH)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(SlidingDoorBlockEntity be, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        Direction facing = blockState.getValue(DoorBlock.FACING);
        Direction movementDirection = facing.getClockWise();

        if (blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT)
            movementDirection = movementDirection.getOpposite();

        float value = ((SlidingDoorBlockEntityAccessor)be).getAnimation().getValue(partialTicks);
        float value2 = Mth.clamp(value * 10, 0, 1);

        Vec3 offset = Vec3.atLowerCornerOf(movementDirection.getNormal())
                .scale(value * value * 13 / 16f)
                .add(Vec3.atLowerCornerOf(facing.getNormal())
                        .scale(value2 * 1 / 32f));

        if (((SlidingDoorBlockEntityAccessor)be).invokeShouldRenderSpecial(blockState) || !be.hasLevel())
            for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                CachedBufferer.block(blockState.setValue(DoorBlock.OPEN, false).setValue(DoorBlock.HALF, half))
                        .translate(0, half == DoubleBlockHalf.UPPER ? 1 - 1 / 512f : 0, 0)
                        .translate(offset)
                        .light(light)
                        .renderInto(poseStack, buffer.getBuffer(cutoutMipped()));
            }
        PaperDoorBehaviour behaviour = be.getBehaviour(PaperDoorBehaviour.type);
        if(behaviour == null)
            return;
        List<Pair<Holder<BannerPattern>, DyeColor>> patterns = behaviour.getPatterns();
        if(patterns == null)
            return;

        //Stackなので、最初に入れた操作が最後に実行される
        poseStack.translate(offset.x, offset.y, offset.z);

        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5F, 2.0F, -0.5F+.00625);
        poseStack.scale(0.8F, -0.8F, 1);
        poseStack.pushPose();

        BannerRenderer.renderPatterns(poseStack, buffer, light, overlay, modelPart, ModelBakery.BANNER_BASE, true, patterns);
        poseStack.popPose();
    }
}