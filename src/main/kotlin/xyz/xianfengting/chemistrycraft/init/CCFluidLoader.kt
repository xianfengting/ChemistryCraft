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

import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry

object CCFluidLoader : ICCLoader {
    private val fluidList = ArrayList<Fluid>()

    override fun load() {
        registerFluid(CCFluids.STEAM)
        fluidList.add(CCFluids.STEAM)

        CCFluids.setFluidList(fluidList)
    }

    private fun registerFluid(fluid: Fluid) {
        if (!FluidRegistry.isFluidRegistered(fluid)) {
            FluidRegistry.registerFluid(fluid)
        }
    }
}
