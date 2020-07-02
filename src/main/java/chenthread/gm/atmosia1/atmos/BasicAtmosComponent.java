package chenthread.gm.atmosia1.atmos;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.Chunk;

public class BasicAtmosComponent implements AtmosComponent {
    protected Chunk chunk;

    public BasicAtmosComponent(Chunk chunk) {
        this.chunk = chunk;
    }

    public int getAtmosInt(BlockPos bpos) {
        BlockState bs = this.chunk.getBlockState(bpos);
        int xn = bs.getWeakRedstonePower(this.chunk, bpos, Direction.WEST);
        int xp = bs.getWeakRedstonePower(this.chunk, bpos, Direction.EAST);
        int yn = bs.getWeakRedstonePower(this.chunk, bpos, Direction.DOWN);
        int yp = bs.getWeakRedstonePower(this.chunk, bpos, Direction.UP);
        int zn = bs.getWeakRedstonePower(this.chunk, bpos, Direction.SOUTH);
        int zp = bs.getWeakRedstonePower(this.chunk, bpos, Direction.NORTH);
        int xmax = Math.max(xn, xp);
        int ymax = Math.max(yn, yp);
        int zmax = Math.max(zn, zp);

        // Use 1/15 scale
        int result = Math.max(xmax, Math.max(ymax, zmax));
        int result_denom = 15; // divide by 15

        // Convert to Pascals
        result *= 101325*2;

        // Add in 1 atmosphere
        result += 101325;

        // Apply denominator
        return (result*2+result_denom)/(result_denom*2);
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