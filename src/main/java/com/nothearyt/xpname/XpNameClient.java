package com.nothearyt.xpname;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XpNameClient implements ClientModInitializer {
    private static String name = "Subscribe";
    private static boolean enabled = true;
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("xpname.txt");

    public static String getName() {
        return name;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void save() {
        try {
            Files.writeString(FILE, (enabled ? "on" : "off") + "\n" + name);
        } catch (Exception ignored) {}
    }

    @Override
    public void onInitializeClient() {
        try {
            if (Files.exists(FILE)) {
                List<String> lines = Files.readAllLines(FILE);
                if (lines.size() >= 1) enabled = !lines.get(0).trim().equalsIgnoreCase("off");
                if (lines.size() >= 2) name = lines.get(1);
            }
        } catch (Exception ignored) {}

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("xpname")
                .executes(ctx -> {
                    enabled = !enabled;
                    save();
                    ctx.getSource().sendFeedback(Text.literal("XP name: " + (enabled ? "ON" : "OFF")));
                    return 1;
                })
                .then(ClientCommandManager.literal("on").executes(ctx -> {
                    enabled = true;
                    save();
                    ctx.getSource().sendFeedback(Text.literal("XP name: ON"));
                    return 1;
                }))
                .then(ClientCommandManager.literal("off").executes(ctx -> {
                    enabled = false;
                    save();
                    ctx.getSource().sendFeedback(Text.literal("XP name: OFF"));
                    return 1;
                }))
                .then(ClientCommandManager.literal("set")
                    .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            name = StringArgumentType.getString(ctx, "text");
                            enabled = true;
                            save();
                            ctx.getSource().sendFeedback(Text.literal("XP bar name set: " + name));
                            return 1;
                        })))));
    }
                        }
