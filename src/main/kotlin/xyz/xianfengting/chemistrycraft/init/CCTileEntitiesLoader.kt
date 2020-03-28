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

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.tileentity.TileEntityIronFurnace
import xyz.xianfengting.chemistrycraft.tileentity.TileEntitySteamGenerator
import kotlin.reflect.KClass

object CCTileEntitiesLoader : ICCLoader {
    override fun load() {
        registerTileEntity(TileEntityIronFurnace::class, "IronFurnace")
        registerTileEntity(TileEntitySteamGenerator::class, "SteamGenerator")
    }

    private fun registerTileEntity(tileEntityClass: KClass<out TileEntity>, id: String) {
        GameRegistry.registerTileEntity(tileEntityClass.java, ResourceLocation(ChemistryCraft.MOD_ID, id))
    }
}
