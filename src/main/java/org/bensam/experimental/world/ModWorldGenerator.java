/**
 * ModWorldGenerator - hooks into Minecraft's world generation, generates copper ore
 */
package org.bensam.experimental.world;

import java.util.Random;

import org.bensam.experimental.block.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * @author WilliHay
 *
 */
public class ModWorldGenerator implements IWorldGenerator
{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0) // the overworld
        {
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                                   IChunkProvider chunkProvider)
    {
        // Note: chunks are 16 x 16
        generateOre(ModBlocks.COPPER_ORE.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64,
                4 + random.nextInt(4), 18);
    }

    private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size,
                             int chances)
    {
        int deltaY = maxY - minY;

        for (int i = 0; i < chances; i++)
        {
            BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
            WorldGenMinable generator = new WorldGenMinable(ore, size);
            generator.generate(world, random, pos);
        }
    }
}
