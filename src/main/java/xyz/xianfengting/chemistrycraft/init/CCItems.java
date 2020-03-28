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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.xianfengting.chemistrycraft.ChemistryCraft;

import java.util.ArrayList;

public class CCItems {
    @GameRegistry.ObjectHolder(ChemistryCraft.MOD_ID + ":" + "copper_ingot")
    public static Item COPPER_INGOT;

    private static ArrayList<Item> itemList = null;

    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    static void setItemList(ArrayList<Item> itemList) {
        CCItems.itemList = itemList;
    }
}
