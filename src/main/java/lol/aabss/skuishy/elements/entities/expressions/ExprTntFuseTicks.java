package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Primed TNT - Fuse Ticks")
@Description("Gets/sets the fuse ticks of a primed tnt.")
@Examples({
        "set fuse ticks of {_tnt} to 10"
})
@Since("2.8")
public class ExprTntFuseTicks extends SimplePropertyExpression<Entity, Integer> {

    static {
        register(ExprTntFuseTicks.class, Integer.class, "[[[primed[-| ]] tnt] fuse ticks", "entities");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "fuse ticks";
    }

    @Override
    public @Nullable Integer convert(Entity entity) {
        if (entity instanceof TNTPrimed) {
            return ((TNTPrimed) entity).getFuseTicks();
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Integer.class);
        }
        return null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            if (delta[0] instanceof Integer) {
                for (Entity entity : getExpr().getArray(e)) {
                    if (entity instanceof TNTPrimed) {
                        ((TNTPrimed) entity).setFuseTicks((Integer) delta[0]);
                    }
                }
            }
        }
    }
}
