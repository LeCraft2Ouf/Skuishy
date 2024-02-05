package lol.aabss.skuishy.hooks.decentholograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import org.eclipse.jdt.annotation.Nullable;

@Name("Decent Holograms - Move Hologram")
@Description("Moves a hologram.")
@Examples({
        "move hologram to player"
})
@Since("1.7")
@RequiredPlugins("DecentHolograms")

public class EffMoveHologram extends Effect {

    static{
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("DecentHolograms")){
            Skript.registerEffect(EffMoveHologram.class,
                    "(move|teleport) [(decent [hologram[s]]|dh)] [hologram] %hologram% to %location%"
            );
        }
    }

    private Expression<Hologram> hologram;
    private Expression<Location> location;

    @Override
    protected void execute(@NotNull Event e) {
        Hologram holo = hologram.getSingle(e);
        Location loc = location.getSingle(e);
        if (holo != null && loc != null){
            DHAPI.moveHologram(holo, loc);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "move hologram";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        hologram = (Expression<Hologram>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }
}
