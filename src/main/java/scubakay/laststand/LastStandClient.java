package scubakay.laststand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import scubakay.laststand.client.LivesHudOverlay;
import scubakay.laststand.event.ModEvents;
import scubakay.laststand.networking.ModMessages;

@Environment(EnvType.CLIENT)
public class LastStandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();
        ModEvents.registerClientEvents();

        HudRenderCallback.EVENT.register(new LivesHudOverlay());
    }
}