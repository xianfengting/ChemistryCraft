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
package xyz.xianfengting.chemistrycraft.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.item.IItemCC

class BlockOre : BlockCCBase(Material.ROCK, "ore") {
    companion object {
        val TYPE = PropertyEnum.create("type", OreType::class.java)
    }

    enum class OreType(val oreName: String, val metadata: Int) : IStringSerializable {
        COPPER("copper", 0);

        companion object {
            fun getOreTypeFromMetadata(metadata: Int) = when (metadata) {
                0 -> COPPER
                else -> throw IllegalArgumentException("Metadata $metadata does not match any of the ore types.")
            }
        }

        override fun getName() = oreName
    }

    class ItemBlockOre(blockOreObj: BlockOre) : ItemBlock(blockOreObj), IItemCC {
        init {
            setHasSubtypes(true)
        }

        override val itemName = "ore"
        val metaMap = mapOf(0 to OreType.COPPER)

        override fun setupModel(event: ModelRegistryEvent) {
            for ((key, value) in metaMap.entries) {
                ModelLoader.setCustomModelResourceLocation(this, key,
                        ModelResourceLocation("${ChemistryCraft.MOD_ID}:${value}_ore", "inventory"))
            }
        }

        override fun getItemStackDisplayName(stack: ItemStack): String {
            val type = OreType.getOreTypeFromMetadata(stack.metadata)
            return I18n.format("tile.${ChemistryCraft.MOD_ID}.ore.${type.oreName}.name")
        }
    }

    override fun initItemBlock() = ItemBlockOre(this)

    override fun createBlockState() = BlockStateContainer(this, TYPE)

    override fun getStateFromMeta(meta: Int) = defaultState.withProperty(TYPE, OreType.getOreTypeFromMetadata(meta))

    override fun getMetaFromState(state: IBlockState) = state.getValue(TYPE).metadata
}
