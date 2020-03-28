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
package xyz.xianfengting.chemistrycraft.init;

import net.minecraftforge.fluids.Fluid;
import xyz.xianfengting.chemistrycraft.fluid.FluidSteam;

import java.util.ArrayList;

public class CCFluids {
    public static Fluid STEAM = new FluidSteam();

    private static ArrayList<Fluid> fluidList = null;

    public static ArrayList<Fluid> getFluidList() {
        return fluidList;
    }

    static void setFluidList(ArrayList<Fluid> fluidList) {
        CCFluids.fluidList = fluidList;
    }
}
