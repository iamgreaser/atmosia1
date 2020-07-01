package chenthread.gm.atmosia1;

import chenthread.gm.atmosia1.atmos.AtmosComponent;
import chenthread.gm.atmosia1.atmos.BasicAtmosComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Atmosia1Mod implements ModInitializer {
	public static final ComponentType<AtmosComponent> ATMOS_COMPONENT =
		ComponentRegistry.INSTANCE.registerIfAbsent(
			new Identifier("atmosia1:atmos_component"),
			AtmosComponent.class);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//
		ChunkComponentCallback.EVENT.register(
			(chunk, components) -> components.put(
				ATMOS_COMPONENT, new BasicAtmosComponent(chunk)));
	}
}
