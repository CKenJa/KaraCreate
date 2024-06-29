package mod.ckenja.karacreate.foundation.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RenderTypes extends RenderStateShard {
    public static final RenderType CUTOUT_MIPPED_NO_CULL = RenderType.create("cutout_mipped", DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS, 131072, true, false, RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setCullState(NO_CULL)
                    .createCompositeState(true));

    public RenderTypes(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }
}
