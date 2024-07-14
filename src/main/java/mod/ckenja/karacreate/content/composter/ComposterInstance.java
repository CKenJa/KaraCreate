package mod.ckenja.karacreate.content.composter;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import mod.ckenja.karacreate.foundation.register.KaraCreatePartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ComposterInstance extends SingleRotatingInstance<ComposterBlockEntity> {

    public ComposterInstance(MaterialManager materialManager, ComposterBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return materialManager
                .defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(KaraCreatePartialModels.COMPOSTER_WHEEL, referenceState, facing);
    }

}