package lol.aabss.skuishy.elements.decentholograms.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Name("Decent Holograms - Hologram ID")
@Description("Gets a hologram id of a hologram.")
@Examples({
        "send hologram id of {_holo}"
})
@Since("2.5")
@RequiredPlugins("DecentHolograms")
public class ExprHologramID extends PropertyExpression<Hologram, String> {

    static {
        register(ExprHologramID.class, String.class, "[holo[gram]] id", "holograms");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Hologram[] source) {
        List<String> names = new ArrayList<>();
        for (Hologram holo : source){
            names.add(holo.getId());
        }
        return names.toArray(String[]::new);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "id of hologram";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends Hologram>) exprs[0]);
        return true;
    }
}
