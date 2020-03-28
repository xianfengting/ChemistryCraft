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
package xyz.xianfengting.chemistrycraft.item

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import xyz.xianfengting.chemistrycraft.ChemistryCraft

class ItemIngot : ItemCCBase("ingot", true) {
    init {
        unlocalizedName = "${ChemistryCraft.MOD_ID}.$itemName"
    }

    override val subTypeMetas: IntArray
        get() = intArrayOf(0)
    override val subTypeNameMap: Map<Int, String>
        get() = mapOf(0 to "${ChemistryCraft.MOD_ID}.copper_ingot")

    override fun getItemStackDisplayName(stack: ItemStack)
            = I18n.format("item.${subTypeNameMap[stack.metadata]}.name")

    override fun getSubTypeModelResourceLocation(meta: Int): ModelResourceLocation {
        return when (meta) {
            0 -> ModelResourceLocation("${ChemistryCraft.MOD_ID}:copper_ingot", "inventory")
            else -> throw IllegalArgumentException("Illegal metadata given")
        }
    }
}
