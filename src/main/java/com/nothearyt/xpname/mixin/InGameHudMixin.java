package com.nothearyt.xpname.mixin;

import com.nothearyt.xpname.XpNameClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBar.class)
public class InGameHudMixin {
    @Inject(method = "renderAddons", at = @At("HEAD"), cancellable = true)
    private void xpname$draw(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!XpNameClient.isEnabled()) return;
        String name = XpNameClient.getName();
        if (name == null || name.isEmpty()) {
            ci.cancel();
            return;
        }

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int x = (context.getScaledWindowWidth() - tr.getWidth(name)) / 2;
        int y = context.getScaledWindowHeight() - 35;

        context.drawText(tr, name, x + 1, y, 0xFF000000, false);
        context.drawText(tr, name, x - 1, y, 0xFF000000, false);
        context.drawText(tr, name, x, y + 1, 0xFF000000, false);
        context.drawText(tr, name, x, y - 1, 0xFF000000, false);
        context.drawText(tr, name, x, y, 0xFF80FF20, false);
        ci.cancel();
    }
                         }
