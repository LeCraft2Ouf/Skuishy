package lol.aabss.skuishy.elements.general;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.lang.function.SimpleJavaFunction;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.DefaultClasses;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import static ch.njol.skript.lang.function.Functions.registerFunction;

public class Functions {

    static {
        Parameter<?>[] numbersParam = new Parameter[]{new Parameter<>("numbers", DefaultClasses.NUMBER, false, null)};
        registerFunction(new SimpleJavaFunction<>("mean", numbersParam, DefaultClasses.NUMBER, true) {
            @Override
            public Number @NotNull [] executeSimple(Object[][] params) {
                double sum = 0.0;
                for (Number num : (Number[]) params[0]) {
                    sum += num.doubleValue();
                }
                double mean = sum / params[0].length;
                return new Number[]{mean};
            }
        }
                .description("Calculates the mean of a list of numbers.")
                .examples()
                .since("1.7"));

        ClassInfo<EulerAngle> eulerAngleClassInfo = Classes.getExactClassInfo(EulerAngle.class);
        if (eulerAngleClassInfo != null) {
            registerFunction(new SimpleJavaFunction<>("eulerangle", new Parameter[]{
                    new Parameter<>("x", DefaultClasses.NUMBER, true, null),
                    new Parameter<>("y", DefaultClasses.NUMBER, true, null),
                    new Parameter<>("z", DefaultClasses.NUMBER, true, null)
            }, eulerAngleClassInfo, true) {
                @Override
                public EulerAngle[] executeSimple(Object[][] params) {
                    if (params.length < 3) {
                        return null;
                    }
                    return new EulerAngle[]{new EulerAngle((Double) params[0][0], (Double) params[1][0], (Double) params[2][0])};
                }
            });
        }

    }
}
