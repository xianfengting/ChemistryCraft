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
package xyz.xianfengting.chemistrycraft.fluid

import net.minecraft.util.ResourceLocation
import xyz.xianfengting.chemistrycraft.ChemistryCraft

class FluidSteam : FluidCCBase("steam", STILL, FLOWING) {
    companion object {
        val STILL = ResourceLocation(ChemistryCraft.MOD_ID + ":" + "fluid/steam_still")
        val FLOWING = ResourceLocation(ChemistryCraft.MOD_ID + ":" + "fluid/steam_flow")
    }

    init {
        this.setUnlocalizedName("fluid_steam")
        this.setDensity(13600)
        this.setViscosity(750)
        this.setLuminosity(0)
        this.setTemperature(373)
        this.setGaseous(true)
    }
}
