package mod.ckenja.karacreate.content.composter;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import mod.ckenja.karacreate.foundation.register.KaraCreatePartialModels;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ComposterRenderer extends KineticBlockEntityRenderer<ComposterBlockEntity> {
    public ComposterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(ComposterBlockEntity be, BlockState state) {
        return CachedBufferer.partial(KaraCreatePartialModels.COMPOSTER_WHEEL, state);
    }
}
