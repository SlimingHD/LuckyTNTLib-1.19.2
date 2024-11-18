package luckytntlib.client;

import java.lang.reflect.Field;
import java.util.List;

import luckytntlib.util.explosions.ExplosionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class ClientAccess {

	@SuppressWarnings("resource")
	public static void updateChunkSection(SectionPos pos, List<Short> changed, boolean empty, boolean updateLight) {
		ClientLevel level = Minecraft.getInstance().level;
		
		if(level == null) {
			return;
		}
		
		LevelChunk chunk = level.getChunk(pos.getX(), pos.getZ());
		PalettedContainer<BlockState> states = chunk.getSection(pos.getY()).getStates();
		LevelLightEngine engine = level.getLightEngine();
		chunk.setLoaded(true);
		
		if(updateLight) {
			for(Short s : changed) {
				Vec3i secpos = ExplosionHelper.decodeSectionPos(s);
				BlockPos blockpos = new BlockPos((pos.getX() << 4) + secpos.getX(), ((pos.getY() + level.getMinSection()) << 4) + secpos.getY(), (pos.getZ() << 4) + secpos.getZ());
				engine.checkBlock(blockpos);
			}
			
			engine.updateSectionStatus(SectionPos.of(pos.getX(), pos.getY() + level.getMinSection(), pos.getZ()), false);
			engine.setLightEnabled(chunk.getPos(), true);
		} else {
			if(empty) {
				for(short s = 0; s < 4096; s++) {
					Vec3i secpos = ExplosionHelper.decodeSectionPos(s);
					states.set(secpos.getX(), secpos.getY(), secpos.getZ(), Blocks.AIR.defaultBlockState());
					
					BlockPos blockpos = new BlockPos((pos.getX() << 4) + secpos.getX(), ((pos.getY() + level.getMinSection()) << 4) + secpos.getY(), (pos.getZ() << 4) + secpos.getZ());
					chunk.removeBlockEntity(blockpos);
				}
			} else {
				for(Short s : changed) {
					Vec3i secpos = ExplosionHelper.decodeSectionPos(s);
					states.set(secpos.getX(), secpos.getY(), secpos.getZ(), Blocks.AIR.defaultBlockState());
					
					BlockPos blockpos = new BlockPos((pos.getX() << 4) + secpos.getX(), ((pos.getY() + level.getMinSection()) << 4) + secpos.getY(), (pos.getZ() << 4) + secpos.getZ());
					chunk.removeBlockEntity(blockpos);
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	public static void updateChunkSkyLightSources(ChunkPos pos, int[] data) {
		ClientLevel level = Minecraft.getInstance().level;
		
		if(level == null) {
			return;
		}
		
		BitStorage heightmap = null;
		
		try {
			for(Field f : ChunkSkyLightSources.class.getDeclaredFields()) {
				f.setAccessible(true);
				if(f.get(level.getChunk(pos.x, pos.z).getSkyLightSources()) instanceof BitStorage storage) {
					heightmap = storage;
					break;
				}
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(heightmap != null) {
			for(short i = 0; i < 256; i++) {
				heightmap.set(i, data[i]);
			}
		}
	}
}
