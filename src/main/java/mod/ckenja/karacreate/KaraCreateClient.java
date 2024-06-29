package mod.ckenja.karacreate;

import mod.ckenja.karacreate.content.paperDoor.PaperDoorRenderer;
import mod.ckenja.karacreate.foundation.render.KaraCreateModelLayers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class KaraCreateClient {
    public static void onConstructor(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(KaraCreateClient::registerLayerDefinitions);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(KaraCreateModelLayers.PAPER_DOOR, PaperDoorRenderer::createBodyLayer);
    }
}
