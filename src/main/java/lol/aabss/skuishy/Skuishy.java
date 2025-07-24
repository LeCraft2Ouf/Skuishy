package lol.aabss.skuishy;

import lol.aabss.skuishy.other.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static lol.aabss.skuishy.other.SubCommands.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@SuppressWarnings("deprecation")
public class Skuishy extends JavaPlugin {

    public static Skuishy instance;
    public static ch.njol.skript.SkriptAddon addon;
    public static long start;
    public static PermissionAttachment last_permission_attachment;
    public static Permission last_permission;
    public static lol.aabss.skuishy.other.Blueprint last_blueprint;
    public static String latest_version;
    public static ch.njol.skript.util.Version latest_version_object;
    public static ch.njol.skript.util.Version plugin_version;
    public static String data_path;
    public static HashMap<String, Boolean> element_map = new HashMap<>();
    public static final String prefix = ChatColor.of("#00ff00") + "[Skuishy] " + ChatColor.RESET;
    public static final boolean skript_reflect_supported = ch.njol.skript.Skript.classExists("com.btk5h.skriptmirror.ObjectWrapper");

    @Override
    public void onEnable() {
        instance = this;
        plugin_version = new ch.njol.skript.util.Version(getPluginMeta().getVersion());
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
        start = System.currentTimeMillis() / 50;
        Logger.success("Skuishy has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        if (getConfig().getBoolean("auto-update", false)) {
            if (new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).delete()) {
                lol.aabss.skuishy.other.UpdateChecker.update();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(miniMessage().deserialize(
                    "<red>/skuishy <" +
                            "<click:run_command:'/skuishy dependencies'><hover:show_text:'<green>/skuishy dependencies'>dependencies</hover></click>" +
                            " | " +
                            "<click:run_command:'/skuishy info'><hover:show_text:'<green>/skuishy info'>info</hover></click>" +
                            " | " +
                            "<click:run_command:'/skuishy reload'><hover:show_text:'<green>/skuishy reload'>reload</hover></click>" +
                            " | " +
                            "<click:run_command:'/skuishy update'><hover:show_text:'<green>/skuishy update'>update</hover></click>" +
                            " | " +
                            "<click:run_command:'/skuishy version'><hover:show_text:'<green>/skuishy version'>version</hover></click>" +
                            ">")
            );
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "dependencies" -> cmdDependencies(sender);
            case "info" -> cmdInfo(sender, args.length > 1 ? args[1] : null);
            case "reload" -> cmdReload(sender);
            case "update" -> cmdUpdate(sender);
            case "version" -> cmdVersion(sender);
            default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }
        return true;
    }

    @Nullable
    public static NamespacedKey namespacedKeyFromObject(Object object) {
        if (object instanceof String string) {
            String[] split = string.split(":");
            if (split.length > 1) {
                return new NamespacedKey(split[0], split[1]);
            } else {
                return new NamespacedKey(Skuishy.instance, string);
            }
        } else if (object instanceof NamespacedKey) {
            return (NamespacedKey) object;
        }
        return null;
    }

    public static int index(int index) {
        boolean skriptIndex = instance.getConfig().getBoolean("prefer-skript-index", false);
        return (skriptIndex ? index + 1 : index);
    }

    public static class Logger {
        public static void success(@Nullable Object message) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + (message != null ? message.toString() : "null"));
        }

        public static void log(@Nullable Object message) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.WHITE + (message != null ? message.toString() : "null"));
        }

        public static void warn(@Nullable Object message) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + (message != null ? message.toString() : "null"));
        }

        public static void error(@Nullable Object message) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + (message != null ? message.toString() : "null"));
        }

        public static void exception(Throwable throwable) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.DARK_RED + "An unexpected error occurred! See the stacktrace below:");
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + throwable + "\n" + ChatColor.DARK_RED);
            ComponentBuilder stackTrace = new ComponentBuilder();
            Bukkit.getConsoleSender().sendMessage(prefix + "Stacktrace Message: " + ChatColor.RED + throwable.getMessage());
            Bukkit.getConsoleSender().sendMessage(prefix + "Cause: " + ChatColor.RED + throwable.getCause());
            for (StackTraceElement element : throwable.getStackTrace()) {
                Bukkit.getConsoleSender().sendMessage(prefix + "| " + ChatColor.RED + element);
                stackTrace.append(" " + element + "\n").color(ChatColor.RED);
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isOp()) {
                    ComponentBuilder component = new ComponentBuilder();
                    component
                            .append("[Skuishy] ").color(ChatColor.of("#00ff00"))
                            .append("Something went wrong! See the problem in console.").color(ChatColor.RED)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, stackTrace.create()));
                    p.sendMessage(component.build());
                }
            }
        }

        public static void exception(Class<? extends Throwable> exceptionClass, String message) {
            for (Constructor<?> constructor : exceptionClass.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().equals(String.class)) {
                    try {
                        exception((Throwable) constructor.newInstance(message));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        exception(e);
                    }
                }
            }
        }

        public static void exception(String message) {
            exception(new RuntimeException(message));
        }
    }
}
