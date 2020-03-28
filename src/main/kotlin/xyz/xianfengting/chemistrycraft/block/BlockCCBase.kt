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

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import xyz.xianfengting.chemistrycraft.ChemistryCraft

open class BlockCCBase(material: Material, val blockName: String) : Block(material), IBlockWithItem {

    private val theItemBlock = initItemBlock()

    init {
        unlocalizedName = "${ChemistryCraft.MOD_ID}.$blockName"

        theItemBlock.setRegistryName(blockName)
    }

    open fun initItemBlock() = ItemBlock(this)

    override val itemBlock: ItemBlock
        get() = theItemBlock
}
