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
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid
import xyz.xianfengting.chemistrycraft.ChemistryCraft

open class BlockFluidBase(fluid: Fluid, material: Material, val blockName: String)
    : BlockFluidClassic(fluid, material), IBlockWithItem {

    private val theItemBlock = ItemBlock(this)

    init {
        unlocalizedName = "${ChemistryCraft.MOD_ID}.$blockName"

        theItemBlock.setRegistryName(blockName)
    }

    override val itemBlock: ItemBlock
        get() = theItemBlock
}
