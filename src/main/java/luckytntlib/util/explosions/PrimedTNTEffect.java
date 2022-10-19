package luckytntlib.util.explosions;

import luckytntlib.client.util.ClientSoundExecutor;
import luckytntlib.util.IExplosiveEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public abstract class PrimedTNTEffect extends ExplosiveEffect{
	
	public void baseTick(IExplosiveEntity entity) {
		Level level = entity.level();
		if(level.isClientSide) {
			int clientFuse = entity.getTNTFuse() - 1;
			if (clientFuse <= 0) {
				clientExplosion(entity);
				entity.destroy();
			} else {
				spawnParticles(entity);
			}
		}
		else if(entity.getTNTFuse() <= 0) {
			serverExplosion(entity);
			entity.destroy();
		}
		explosionTick(entity);
		entity.setTNTFuse(entity.getTNTFuse() - 1);
	}
	
	public void spawnParticles(IExplosiveEntity entity) {
		entity.level().addParticle(ParticleTypes.SMOKE, entity.getPos().x(), entity.getPos().y + 0.5f, entity.getPos().z, 0, 0, 0);
	}
	
	@SuppressWarnings("resource")
	public void clientExplosion(IExplosiveEntity entity) {
		ClientSoundExecutor.playSoundAt(entity.getPos().x, entity.getPos().y, entity.getPos().z, SoundEvents.GENERIC_EXPLODE, SoundSource.MASTER, 4f, (1f + (entity.level().random.nextFloat() - entity.level().random.nextFloat()) * 0.2f) * 0.7f);
	}
	
	public void serverExplosion(IExplosiveEntity entity) {	
	}
	
	public void explosionTick(IExplosiveEntity entity) {		
	}
	
	public int getDefaultFuse() {
		return 80;
	}
	
	public float getSize() {
		return 1f;
	}
}
