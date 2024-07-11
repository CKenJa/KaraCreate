package mod.ckenja.karacreate.infrastructure.lang;

import net.minecraftforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BannerPatternLangGenerators {

    public static final String INPUT_FORMAT = "block.minecraft.banner.karacreate.%2$s.%1$s";

    public static void ja_jp(LanguageProvider prov){
        Map<String, String> colorTranslations = Map.ofEntries(
                Map.entry("black", "黒色"),
                Map.entry("blue", "青色"),
                Map.entry("brown", "茶色"),
                Map.entry("cyan", "青緑色"),
                Map.entry("gray", "灰色"),
                Map.entry("green", "緑色"),
                Map.entry("light_blue", "水色"),
                Map.entry("light_gray", "薄灰色"),
                Map.entry("lime", "黄緑色"),
                Map.entry("magenta", "赤紫色"),
                Map.entry("orange", "橙色"),
                Map.entry("pink", "桃色"),
                Map.entry("purple", "紫色"),
                Map.entry("red", "赤色"),
                Map.entry("white", "白色"),
                Map.entry("yellow", "黄色")
        );
        Map<String, String> patternTranslations = Map.of("checkered", "市松模様", "hash", "井桁模様");
        String outputFormat = "%sの%s";
        generateTranslations(List.of(colorTranslations,patternTranslations), INPUT_FORMAT,outputFormat,prov::add);
    }

    public static void en_us(LanguageProvider prov){
        Map<String, String> colorTranslations = Map.ofEntries(
                Map.entry("black", "Black"),
                Map.entry("blue", "Blue"),
                Map.entry("brown", "Brown"),
                Map.entry("cyan", "Cyan"),
                Map.entry("gray", "Gray"),
                Map.entry("green", "Green"),
                Map.entry("light_blue", "Light blue"),
                Map.entry("light_gray", "Light gray"),
                Map.entry("lime", "Lime"),
                Map.entry("magenta", "Magenta"),
                Map.entry("orange", "Orange"),
                Map.entry("pink", "Pink"),
                Map.entry("purple", "Purple"),
                Map.entry("red", "Red"),
                Map.entry("white", "White"),
                Map.entry("yellow", "Yellow")
        );
        Map<String, String> patternTranslations = Map.of("checkered", "Checkered", "hash", "Hash");
        String outputFormat = "%s %s";
        generateTranslations(List.of(colorTranslations,patternTranslations), INPUT_FORMAT,outputFormat,prov::add);
    }

    public static void generateTranslations(List<Map<String, String>> translations,
                                            String inputFormat,
                                            String outputFormat,
                                            BiConsumer<String, String> consumer) {
        generateTranslationsHelper(translations, new ArrayList<>(), new ArrayList<>(), 0, (k,v)->{
            String key = String.format(inputFormat, k.toArray());
            String value = String.format(outputFormat, v.toArray());
            consumer.accept(key, value);
        });
    }

    public static void generateTranslationsHelper(List<Map<String, String>> input, List<String> keys, List<String> values, int index, BiConsumer<List<String>,List<String>> consumer) {
        if (index == input.size()) {
            consumer.accept(List.copyOf(keys), List.copyOf(values));
            return;
        }

        Map<String, String> currentMap = input.get(index);
        for (Map.Entry<String, String> entry : currentMap.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
            generateTranslationsHelper(input, keys, values, index + 1, consumer);
            keys.remove(keys.size() - 1);
            values.remove(values.size() - 1);
        }
    }

    private BannerPatternLangGenerators(){}
}