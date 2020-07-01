package chenthread.gm.atmosia1.atmos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;

public class BasicAtmosComponent implements AtmosComponent {
    protected Chunk chunk;

    public BasicAtmosComponent(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        // TODO load things
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        // TODO store things

        return tag;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }
}