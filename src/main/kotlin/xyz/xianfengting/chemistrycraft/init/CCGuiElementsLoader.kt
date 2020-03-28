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

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.client.gui.GuiIronFurnace
import xyz.xianfengting.chemistrycraft.client.gui.GuiSteamGenerator
import xyz.xianfengting.chemistrycraft.inventory.ContainerIronFurnace
import xyz.xianfengting.chemistrycraft.inventory.ContainerSteamGenerator

object CCGuiElementsLoader : ICCLoader, IGuiHandler {
    override fun load() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ChemistryCraft, this)
    }

    override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        when (id) {
            CCGuiElements.IRON_FURNACE -> return ContainerIronFurnace(player, world.getTileEntity(BlockPos(x, y, z))!!)
            CCGuiElements.STEAM_GENERATOR -> return ContainerSteamGenerator(player, world.getTileEntity(BlockPos(x, y, z))!!)
        }
        return null
    }

    override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        when (id) {
            CCGuiElements.IRON_FURNACE -> {
                val container = ContainerIronFurnace(player, world.getTileEntity(BlockPos(x, y, z))!!)
                return GuiIronFurnace(container)
            }
            CCGuiElements.STEAM_GENERATOR -> {
                val container = ContainerSteamGenerator(player, world.getTileEntity(BlockPos(x, y, z))!!)
                return GuiSteamGenerator(container)
            }
        }
        return null
    }
}
