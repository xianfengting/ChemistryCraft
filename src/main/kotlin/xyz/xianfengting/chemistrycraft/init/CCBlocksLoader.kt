/**
 * ChemistryCraft
 * Copyright (C) 2020  src_resources <xianfengting221@163.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.xianfengting.chemistrycraft.init

import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.block.*
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.xianfengting.chemistrycraft.item.IItemCC
import net.minecraftforge.fluids.BlockFluidBase as ForgeBlockFluidBase

@Mod.EventBusSubscriber(modid = ChemistryCraft.MOD_ID)
object CCBlocksLoader : ICCLoader {

    private val blockList = ArrayList<Block>()

    @JvmStatic
    @SubscribeEvent
    fun onRegisterBlocks(event: RegistryEvent.Register<Block>) {
        val registry = event.registry
        for (block in blockList) {
            registry.register(block)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onRegisterItemBlocks(event: RegistryEvent.Register<Item>) {
        val registry = event.registry
        for (block in blockList) {
            if (block is IBlockWithItem) {
                registry.register(block.itemBlock)
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onModelRegistry(event: ModelRegistryEvent) {
        for (block in blockList) {
            if (block is IBlockWithItem) {
                val itemBlock = block.itemBlock
                if (itemBlock is IItemCC) {
                    itemBlock.setupModel(event)
                } else {
                    ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                            ModelResourceLocation(itemBlock.registryName, "inventory"))
                }
            }
        }
    }

    override fun load() {
        // Fluids
        blockList.add(BlockFluidSteam().setRegistryName("fluid_steam"))


        // Ores
        blockList.add(BlockOre().setRegistryName("ore"))

        // Machines
        blockList.add(BlockIronFurnace().setRegistryName("iron_furnace"))
        blockList.add(BlockSteamGenerator().setRegistryName("steam_generator"))

        CCBlocks.setBlockList(blockList)

        registerRenders()
    }

    @SideOnly(Side.CLIENT)
    private fun registerRenders() {
        for (block in blockList) {
            if (block is IFluidRenderRegisterer) {
                block.registerRender(this::registerFluidRender)
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private fun registerFluidRender(blockFluid: ForgeBlockFluidBase, blockStateName: String) {
        val location = ChemistryCraft.MOD_ID + ":" + blockStateName
        val itemFluid = Item.getItemFromBlock(blockFluid)
        ModelLoader.setCustomMeshDefinition(itemFluid, ItemMeshDefinition { ModelResourceLocation(location, "fluid") })
        ModelLoader.setCustomStateMapper(blockFluid, object : StateMapperBase() {
            override fun getModelResourceLocation(state: IBlockState): ModelResourceLocation {
                return ModelResourceLocation(location, "fluid")
            }
        })
    }
}
