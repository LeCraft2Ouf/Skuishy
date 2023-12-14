package lol.aabss.skuishy.vivecraft;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.cjcrafter.vivecraft.VSE;
import com.cjcrafter.vivecraft.VivePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.converter.Converters;

public class Types {
    static{
        if (Bukkit.getServer().getPluginManager().getPlugin("Vivecraft-Spigot-Extenstions") != null) {
            Classes.registerClass(new ClassInfo<>(VivePlayer.class, "viveplayer")
                    .user("vive ?players?")
                    .name("ViveCraft - Vive Player")
                    .description("Represents a ViveCraft player.")
                    .since("1.9")
                    .parser(new Parser<VivePlayer>() {

                        @Override
                        public boolean canParse(@NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toVariableNameString(VivePlayer player) {
                            return player.player.getName();
                        }

                        @Override
                        public @NotNull String toString(VivePlayer player, int flags) {
                            return toVariableNameString(player);
                        }
                    })
            );
            Converters.registerConverter(VivePlayer.class, Player.class, e -> e.player);
            Converters.registerConverter(Player.class, VivePlayer.class, e -> VSE.vivePlayers.get(e.getUniqueId()));
        }
    }
}