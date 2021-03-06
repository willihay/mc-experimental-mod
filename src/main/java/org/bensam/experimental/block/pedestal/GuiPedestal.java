package org.bensam.experimental.block.pedestal;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.ModBlocks;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * @author WilliHay
 *
 */
public class GuiPedestal extends GuiContainer
{
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(ExperimentalMod.MODID,
            "textures/gui/pedestal.png");
    private InventoryPlayer playerInventory;

    public GuiPedestal(Container container, InventoryPlayer playerInventory)
    {
        super(container);

        this.playerInventory = playerInventory;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String name = I18n.format(ModBlocks.PEDESTAL.getTranslationKey() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name), 6, 0x404040);
        fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

}
