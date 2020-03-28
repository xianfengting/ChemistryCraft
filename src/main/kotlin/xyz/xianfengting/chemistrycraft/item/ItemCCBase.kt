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
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import xyz.xianfengting.chemistrycraft.ChemistryCraft

open class ItemCCBase(override val itemName: String, hasSubTypes: Boolean) : Item(), IItemCC {
    init {
        setHasSubtypes(hasSubTypes)
        if (hasSubTypes) {

        }
    }

    open val subTypeMetas = intArrayOf(0)
    open val subTypeNameMap = mapOf(0 to "${ChemistryCraft.MOD_ID}.$itemName")

    override fun setupModel(event: ModelRegistryEvent) {
        for (meta in subTypeMetas) {
            ModelLoader.setCustomModelResourceLocation(this, meta, getSubTypeModelResourceLocation(meta))
        }
    }

    open fun getSubTypeModelResourceLocation(meta: Int) = ModelResourceLocation(registryName, "inventory")

    override fun getUnlocalizedName(): String {
        Thread.dumpStack()
        return "item.${subTypeNameMap[0]}"
    }
}
