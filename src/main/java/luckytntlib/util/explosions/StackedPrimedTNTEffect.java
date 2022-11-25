package luckytntlib.util.explosions;

import java.util.ArrayList;
import java.util.List;

import luckytntlib.util.IExplosiveEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class StackedPrimedTNTEffect extends PrimedTNTEffect{

	private final List<PrimedTNTEffect> effects = new ArrayList<>();
	
	public StackedPrimedTNTEffect(PrimedTNTEffect primaryEffect, List<PrimedTNTEffect> secondaryEffects) {
		effects.add(primaryEffect);
		effects.addAll(secondaryEffects);
	}
	
	@Override
	public void explosionTick(IExplosiveEntity entity) {
		for(PrimedTNTEffect effect : effects) {
			effect.explosionTick(entity);
		}
	}
	
	@Override
	public void serverExplosion(IExplosiveEntity entity) {
		for(PrimedTNTEffect effect : effects) {
			effect.serverExplosion(entity);
		}
	}
	
	@Override
	public void spawnParticles(IExplosiveEntity entity) {
		effects.get(0).spawnParticles(entity);
	}
	
	@Override
	public ItemStack getItem() {
		return effects.get(0).getItem();
	}
	
	@Override
	public Block getBlock() {
		return effects.get(0).getBlock();
	}
	
	@Override
	public boolean airFuse() {
		return effects.get(0).airFuse();
	}
	
	@Override
	public boolean explodesOnImpact() {
		return effects.get(0).explodesOnImpact();
	}
	
	@Override
	public float getSize() {
		return effects.get(0).getSize();
	}
	
	@Override
	public int getDefaultFuse(IExplosiveEntity entity) {
		return effects.get(0).getDefaultFuse(entity);
	}
}
