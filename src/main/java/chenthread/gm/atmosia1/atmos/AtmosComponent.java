package chenthread.gm.atmosia1.atmos;

import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.ChunkComponent;
import nerdhub.cardinal.components.api.util.sync.ChunkSyncedComponent;
import net.minecraft.util.math.BlockPos;

public interface AtmosComponent extends ChunkSyncedComponent<ChunkComponent<Component>> {
    public int getAtmosInt(BlockPos bpos);

    // Get atmospheric pressure in atmospheres (atm).
    default public double getAtmosInAtm(BlockPos bpos) {
        return ((double)this.getAtmosInt(bpos))/101325.0d;
    }
}