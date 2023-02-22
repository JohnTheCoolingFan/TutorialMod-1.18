package ru.jtcf.tutorial.block.metalpress;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ru.jtcf.tutorial.TutorialMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MetalPressScreen extends AbstractContainerScreen<MetalPressContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(TutorialMod.MODID,
            "textures/gui/metal_press.png");

    public MetalPressScreen(MetalPressContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, x, y, partialTicks);
        this.renderTooltip(poseStack, x, y);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Progress arrow
        blit(matrixStack, this.leftPos + 79, this.topPos + 35, 176, 14, menu.getProgressArrowScale() + 1, 16);
        // Energy indicator
        blit(matrixStack, this.leftPos + 154, this.topPos + 18 + 50 - menu.getEnergyIndicatorScale(), 176, 31 + 50 - menu.getEnergyIndicatorScale(), 12, menu.getEnergyIndicatorScale());
    }
}
