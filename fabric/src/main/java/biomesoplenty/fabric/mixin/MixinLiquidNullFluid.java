/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.fabric.mixin;

import biomesoplenty.api.block.BOPFluids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(net.minecraft.client.renderer.fog.FogRenderer.class)
public abstract class MixinLiquidNullFluid
{
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private void setupFog(Camera camera, int i, DeltaTracker deltaTracker, float f, ClientLevel level, CallbackInfoReturnable<FogData> cir)
    {
        BlockPos blockPos = camera.blockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        Fluid fluid = fluidState.getType();

        if(camera.position().y > blockPos.getY() + fluidState.getHeight(level, blockPos))
        {
            return;
        }

        if (!BOPFluids.LIQUID_NULL.isSame(fluid))
            return;

        float h = (float)(i * 16);

        FogData fogData = new FogData();
        fogData.color.set(0.6274509803921569F, 0.12549019607843137F, 0.9411764705882353F, 0.5F);

        float j = Mth.clamp(h / 10.0F, 4.0F, 64.0F);
        fogData.renderDistanceStart = h - j;
        fogData.renderDistanceEnd = h;
        fogData.environmentalStart = 0.1F;
        fogData.environmentalEnd = 2.5F;
        fogData.skyEnd = fogData.environmentalEnd;
        fogData.cloudEnd = fogData.environmentalEnd;

        cir.setReturnValue(fogData);
    }
}