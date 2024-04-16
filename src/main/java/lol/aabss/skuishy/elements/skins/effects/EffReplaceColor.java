package lol.aabss.skuishy.elements.skins.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.blueprints.Blueprint;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

@Name("Blueprint - Replace Color")
@Description("Replaces every color in a blueprint with another color.")
@Examples({
        "replace all red in {_blueprint} with blue",
        "replace all rgb(255, 0, 0) with rgb(0, 0, 255)"
})
@Since("2.6")
public class EffReplaceColor extends Effect {

    static {
        Skript.registerEffect(EffReplaceColor.class,
                "replace (all|every) %colors% in %blueprints% with %color%",
                        "replace (all|every) %colors% with %color% in %blueprints%"
        );
    }

    private Expression<Color> oldColor;
    private Expression<Color> newColor;
    private Expression<Blueprint> blueprint;

    @Override
    protected void execute(@NotNull Event e) {
        if (newColor != null){
            Color newColor = this.newColor.getSingle(e);
            if (newColor != null){
                for (Blueprint blueprint : blueprint.getArray(e)){
                    for (Color oldColor : this.oldColor.getArray(e)){
                        blueprint.replaceColor(oldColor, newColor);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "replace colors in blueprints";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        oldColor = (Expression<Color>) exprs[0];
        if (matchedPattern == 0){
            blueprint = (Expression<Blueprint>) exprs[1];
            newColor = (Expression<Color>) exprs[2];
            return true;
        }
        newColor = (Expression<Color>) exprs[1];
        blueprint = (Expression<Blueprint>) exprs[2];
        return true;
    }
}
