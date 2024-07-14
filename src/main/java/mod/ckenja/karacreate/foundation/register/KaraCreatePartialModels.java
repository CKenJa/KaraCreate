package mod.ckenja.karacreate.foundation.register;

import com.jozufozu.flywheel.core.PartialModel;
import mod.ckenja.karacreate.KaraCreate;

public class KaraCreatePartialModels {
    public static final PartialModel
            COMPOSTER_WHEEL = block("mechanical_composter/wheel");

    private static PartialModel block(String path) {
        return new PartialModel(KaraCreate.asResource("block/" + path));
    }

    public static void init() {
    }
}
