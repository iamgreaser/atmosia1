package chenthread.gm.atmosia1.mixin;

import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import chenthread.gm.atmosia1.Atmosia1Mod;
import chenthread.gm.atmosia1.atmos.AtmosComponent;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow public World world;

	@Shadow public BlockPos getBlockPos() { return null; }
	@Shadow public Vec3d getPos() { return null; }
    @Shadow public void setPos(double x, double y, double z) {}
    @Shadow public Vec3d getVelocity() { return null; }
    @Shadow public void setVelocity(Vec3d velocity) {}
    @Shadow public void setOnGround(boolean onGround) {}
    @Shadow public boolean isOnGround() { return false; }

	@Inject(at = @At("HEAD"), method = "tick()V")
	protected void tickAtmos(CallbackInfo info) {
		// I sometimes like to constrain this to PlayerEntity or LivingEntity during testing --GM
		if (((Object)this) instanceof Entity) {
			Vec3d pos = this.getPos();

			Vec3d derivative = this.getAirPressureDerivativeAtPos(pos);
			Vec3d accel = derivative.multiply(1.0d);

			Vec3d vnew = this.getVelocity().add(accel);
			this.setVelocity(vnew);

			// Deground the entity if we're going faster than jump speed
			if (this.isOnGround() && vnew.y >= +0.002d) {
				this.setOnGround(false);
			}
		}
	}

	protected Vec3d getAirPressureDerivativeAtPos(Vec3d pos)
	{
        double px = pos.getX();
        double py = pos.getY();
        double pz = pos.getZ();

        //double subx = px - (double)x0;
        //double suby = py - (double)y0;
        //double subz = pz - (double)z0;
        int x0 = (int)Math.floor(px);
        int y0 = (int)Math.floor(py);
        int z0 = (int)Math.floor(pz);
        BlockPos centre = new BlockPos(x0, y0, z0);
		double dx = 0.0d;
		double dy = 0.0d;
		double dz = 0.0d;
		double base = this.getAirPressureAtBlockPos(centre);
		
		// Add to faces
		dx -= this.getAirPressureAtBlockPos(centre.add(+1,  0,  0)) - base;
		dy -= this.getAirPressureAtBlockPos(centre.add( 0, +1,  0)) - base;
		dz -= this.getAirPressureAtBlockPos(centre.add( 0,  0, +1)) - base;
		dx += this.getAirPressureAtBlockPos(centre.add(-1,  0,  0)) - base;
		dy += this.getAirPressureAtBlockPos(centre.add( 0, -1,  0)) - base;
		dz += this.getAirPressureAtBlockPos(centre.add( 0,  0, -1)) - base;

		// Add edges
		final double mul_edge = 1.0d/Math.sqrt(2.0d);
		double exnyn = (this.getAirPressureAtBlockPos(centre.add(-1, -1,  0)) - base);
		double expyn = (this.getAirPressureAtBlockPos(centre.add(+1, -1,  0)) - base);
		double exnyp = (this.getAirPressureAtBlockPos(centre.add(-1, -1,  0)) - base);
		double expyp = (this.getAirPressureAtBlockPos(centre.add(+1, -1,  0)) - base);
		double exnzn = (this.getAirPressureAtBlockPos(centre.add(-1,  0, -1)) - base);
		double expzn = (this.getAirPressureAtBlockPos(centre.add(+1,  0, -1)) - base);
		double exnzp = (this.getAirPressureAtBlockPos(centre.add(-1,  0, +1)) - base);
		double expzp = (this.getAirPressureAtBlockPos(centre.add(+1,  0, +1)) - base);
		double eynzn = (this.getAirPressureAtBlockPos(centre.add( 0, -1, -1)) - base);
		double eypzn = (this.getAirPressureAtBlockPos(centre.add( 0, -1, +1)) - base);
		double eynzp = (this.getAirPressureAtBlockPos(centre.add( 0, +1, -1)) - base);
		double eypzp = (this.getAirPressureAtBlockPos(centre.add( 0, +1, +1)) - base);
		dx += mul_edge*(exnyn + exnyp + exnzn + exnzp);
		dx -= mul_edge*(expyn + expyp + expzn + expzp);
		dy += mul_edge*(exnyn + expyn + eynzn + eynzp);
		dy -= mul_edge*(exnyp + expyp + eypzn + eypzp);
		dz += mul_edge*(exnzn + expzn + eynzn + eypzn);
		dz -= mul_edge*(exnzp + expzp + eynzp + eypzp);

		// Add corners
		final double mul_corner = 1.0d/Math.sqrt(3.0d);
		double c000 = (this.getAirPressureAtBlockPos(centre.add(-1, -1, -1)) - base);
		double c001 = (this.getAirPressureAtBlockPos(centre.add(-1, -1, +1)) - base);
		double c010 = (this.getAirPressureAtBlockPos(centre.add(-1, +1, -1)) - base);
		double c011 = (this.getAirPressureAtBlockPos(centre.add(-1, +1, +1)) - base);
		double c100 = (this.getAirPressureAtBlockPos(centre.add(+1, -1, -1)) - base);
		double c101 = (this.getAirPressureAtBlockPos(centre.add(+1, -1, +1)) - base);
		double c110 = (this.getAirPressureAtBlockPos(centre.add(+1, +1, -1)) - base);
		double c111 = (this.getAirPressureAtBlockPos(centre.add(+1, +1, +1)) - base);
		dx += mul_corner*(c000 + c001 + c010 + c011);
		dx -= mul_corner*(c100 + c101 + c110 + c111);
		dy += mul_corner*(c000 + c001 + c100 + c101);
		dy -= mul_corner*(c010 + c011 + c110 + c111);
		dz += mul_corner*(c000 + c010 + c100 + c110);
		dz -= mul_corner*(c001 + c011 + c101 + c111);

        //System.out.printf("p = %+3d %+3d %+3d\n", dx, dy, dz);

		Vec3d result = new Vec3d(dx, dy, dz).multiply(1.0d/8.0d);

		return result;
	}

	protected double getAirPressureAtBlockPos(BlockPos bpos)
	{
		double result = Atmosia1Mod.ATMOS_COMPONENT.maybeGet(this.world.getChunk(bpos)).map(
			(AtmosComponent component) -> component.getAtmosInAtm(bpos)
		).orElse(0.0d);
		return result;
	}
}
