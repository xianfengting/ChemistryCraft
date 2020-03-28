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

import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.item.IItemCC
import xyz.xianfengting.chemistrycraft.item.ItemIngot
import xyz.xianfengting.chemistrycraft.item.ItemStoneHammer

@Mod.EventBusSubscriber(modid = ChemistryCraft.MOD_ID)
object CCItemsLoader : ICCLoader {

    private val itemList = ArrayList<Item>()

    @JvmStatic
    @SubscribeEvent
    fun onRegister(event: RegistryEvent.Register<Item>) {
        val registry = event.registry
        for (item in itemList) {
            registry.register(item)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onModelRegistry(event: ModelRegistryEvent) {
        for (item in CCItems.getItemList()) {
            if (item is IItemCC) {
                item.setupModel(event)
            }
        }
    }

   override fun load() {
       // Resources
       itemList.add(ItemIngot().setRegistryName("ingot"))

       // Tools
       itemList.add(ItemStoneHammer().setRegistryName("stone_hammer"))

       CCItems.setItemList(itemList)
   }
}
