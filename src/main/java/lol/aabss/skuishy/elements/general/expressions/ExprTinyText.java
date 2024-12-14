package lol.aabss.skuishy.elements.general.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@Name("Other - Tiny Text")
@Description("Returns a text in tiny caps.")
@Examples({
        "send \"hi\" in tiny caps"
})
@Since("2.7")
public class ExprTinyText extends SimpleExpression<String> {

    private static final List<String> normal_char_set = Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).toList();
    private static final List<String> tiny_char_set = Arrays.stream("ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".split("")).toList();
    private static final List<String> super_tiny_char_set = Arrays.stream("ᵃᵇᶜᵈᵉᶠᵍʰᶦʲᵏˡᵐⁿᵒᵖᵠʳˢᵗᵘᵛʷˣʸᶻ".split("")).toList();

    static {
        Skript.registerExpression(ExprTinyText.class, String.class, ExpressionType.COMBINED,
                "[:super] tiny [(text|caps)] %objects%",
                "%objects% (in|as) [:super] tiny (text|caps)"
        );
    }

    private Expression<Object> objects;
    private boolean isSuperTiny;

    @Override
    public boolean init(Expression<?>[] exprs, int i, @NotNull Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        objects = LiteralUtils.defendExpression(exprs[0]);
        this.isSuperTiny = parseResult.hasTag("super");
        return LiteralUtils.canInitSafely(objects);
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event) {
        return objects.stream(event)
                .map(Classes::toString)
                .map(this::tinyText).toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return objects.isSingle();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return this.objects.toString(event, debug) + " as " + (isSuperTiny ? "super " : "") + "tiny text";
    }

    public String tinyText(String string){
        StringBuilder tinyText = new StringBuilder();
        List<String> char_set = isSuperTiny ? super_tiny_char_set : tiny_char_set;
        boolean isColorCode = false; // Preset the isColorCode to false
        for (String str : string.split("")) {
            int index = normal_char_set.indexOf(str);
            if (!isColorCode && index != -1) { // Checks if string is not a color code and index was founds
                str = char_set.get(index);
            }
            isColorCode = str.equals("§"); // Ensures the next loop uses a normal character
            tinyText.append(str);
        }
        return tinyText.toString();
    }

}
