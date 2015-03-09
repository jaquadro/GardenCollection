package com.jaquadro.minecraft.gardenstuff.integration.twilightforest;

import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderFireflyWrapper extends Render
{
    private static final ResourceLocation textureLoc = new ResourceLocation("twilightforest:textures/model/firefly-tiny.png");

    private Render render;

    public RenderFireflyWrapper () {
        try {
            render = (Render) TwilightForestIntegration.constRenderFirefly.newInstance();
        }
        catch (Throwable t) { }
    }

    @Override
    public void setRenderManager (RenderManager renderMan) {
        super.setRenderManager(renderMan);
        if (render != null)
            render.setRenderManager(renderMan);
    }

    @Override
    public void doRender (Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        if (entity instanceof EntityFireflyWrapper) {
            boolean lightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            if (lightingEnabled)
                GL11.glDisable(GL11.GL_LIGHTING);

            if (render != null)
                render.doRender(((EntityFireflyWrapper) entity).entity, x, y, z, yaw, partialTickTime);

            if (lightingEnabled)
                GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture (Entity entity) {
        return textureLoc;
    }
}
