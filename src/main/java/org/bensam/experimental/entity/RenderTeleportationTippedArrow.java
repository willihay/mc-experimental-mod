package org.bensam.experimental.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTeleportationTippedArrow extends RenderTippedArrow
{
    public static final Factory RENDER_FACTORY = new Factory();

    public RenderTeleportationTippedArrow(RenderManager manager)
    {
        super(manager);
    }

    public static class Factory implements IRenderFactory<EntityTeleportationTippedArrow>
    {
        @Override
        public Render<? super EntityTeleportationTippedArrow> createRenderFor(RenderManager manager)
        {
            return new RenderTeleportationTippedArrow(manager);
        }
    }
}
