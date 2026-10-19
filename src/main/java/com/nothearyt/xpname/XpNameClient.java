package com.nothearyt.xpname;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;

public class XpNameClient implements ClientModInitializer {
    private static String name = "Subscribe";
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("xpname.txt");

    public static String getName() {
        return name;
    }

    @Override
    public void onInitializeClient() {
        try {
            if (Files.exists(FILE)) name = Files.readString(FILE).trim();
        } catch (Exception ignored) {}

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("xpname")
                .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        name = StringArgumentType.getString(ctx, "text");
                        try { Files.writeString(FILE, name); } catch (Exception ignored) {}
                        ctx.getSource().sendFeedback(Text.literal("XP bar name set: " + name));
                        return 1;
                    }))
                .executes(ctx -> {
                    name = "";
                    try { Files.writeString(FILE, ""); } catch (Exception ignored) {}
                    ctx.getSource().sendFeedback(Text.literal("XP bar name cleared"));
                    return 1;
                })));
    }
}
