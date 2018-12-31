/**
 * 
 */
package org.bensam.experimental.entity;

import javax.annotation.Nonnull;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * @author Will
 *
 */
public class RenderHealthZombie extends RenderLiving<EntityHealthZombie>
{
    private ResourceLocation mobTexture = new ResourceLocation("experimental:textures/entity/health_zombie.png");
    
    public static final Factory RENDER_FACTORY = new Factory();
    
    /**
     * @param rendermanagerIn
     * @param modelbaseIn
     * @param shadowsizeIn
     */
    public RenderHealthZombie(RenderManager rendermanager)
    {
        super(rendermanager, new ModelZombie(), 0.5f); // reuses vanilla zombie model
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityHealthZombie entity)
    {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityHealthZombie>
    {
        @Override
        public Render<? super EntityHealthZombie> createRenderFor(RenderManager manager)
        {
            return new RenderHealthZombie(manager);
        }
    }
}
