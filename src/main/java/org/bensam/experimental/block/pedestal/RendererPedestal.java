package org.bensam.experimental.block.pedestal;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * @author WilliHay
 *
 */
public class RendererPedestal extends TileEntitySpecialRenderer<TileEntityPedestal>
{

    @Override
    public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage,
                       float alpha)
    {
        ItemStack itemStack = te.inventory.getStackInSlot(0);
        if (!itemStack.isEmpty())
        {
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.pushMatrix();

            // Item should bounce up and down a bit
            double yOffset = Math.sin((te.getWorld().getTotalWorldTime() - te.lastChangeTime + partialTicks) / 8) / 4.0;
            GlStateManager.translate(x + 0.5, y + 1.25 + yOffset, z + 0.5);

            // Item should smoothly rotate around the Y-axis
            GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(itemStack,
                    te.getWorld(), null);
            model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, model);

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

}
