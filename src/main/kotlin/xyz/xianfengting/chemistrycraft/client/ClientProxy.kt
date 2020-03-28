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
package xyz.xianfengting.chemistrycraft.client

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import xyz.xianfengting.chemistrycraft.common.CommonProxy
import xyz.xianfengting.chemistrycraft.init.CCGuiElementsLoader

class ClientProxy : CommonProxy() {
    override fun onPreInit(event: FMLPreInitializationEvent) {
        super.onPreInit(event)
    }

    override fun onInit(event: FMLInitializationEvent) {
        super.onInit(event)
        CCGuiElementsLoader.load()
    }

    override fun onPostInit(event: FMLPostInitializationEvent) {
        super.onPostInit(event)
    }
}
