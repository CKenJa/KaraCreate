package mod.ckenja.karacreate.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mod.ckenja.karacreate.foundation.register.KaraCreateBlocks;
import mod.ckenja.karacreate.foundation.register.KaraCreatePartialModels;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedComposter extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);
        matrixStack.translate(-2, 18, 0);
        int scale = 22;

        blockElement(KaraCreatePartialModels.COMPOSTER_WHEEL)
                .rotateBlock(22.5,getCurrentAngle() * 2,0)
                .scale(scale)
                .render(graphics);

        blockElement(KaraCreateBlocks.COMPOSTER.getDefaultState())
                .rotateBlock(22.5, 22.5, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }
}
