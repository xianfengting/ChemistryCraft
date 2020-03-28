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
package xyz.xianfengting.chemistrycraft

import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import xyz.xianfengting.chemistrycraft.common.CommonProxy

@Mod(modid = ChemistryCraft.MOD_ID, name = ChemistryCraft.MOD_NAME, useMetadata = true,
        acceptedMinecraftVersions = "[1.12,1.13)",
        dependencies = "required-after:forge@[14.23.5.2768,);required-after:forgelin@[1.8.4,);",
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object ChemistryCraft {
    const val MOD_ID = "chemistrycraft"
    const val MOD_NAME = "Chemistry Craft"
//    const val VERSION = "\${version}" // Defined in the mcmod.info file

    private lateinit var logger: Logger

    // This field should only be accessed after the mod's pre-initialization.
    val modLogger: Logger
        get() = logger

    @SidedProxy(clientSide = "xyz.xianfengting.chemistrycraft.client.ClientProxy",
            serverSide = "xyz.xianfengting.chemistrycraft.common.CommonProxy")
    lateinit var proxy: CommonProxy

    @Mod.EventHandler
    fun onConstruct(event: FMLConstructionEvent) {
        // Here's currently nothing that we should do
    }

    @Mod.EventHandler
    fun onPreInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        logger.info("Welcome to use Chemistry Craft Mod!")
        proxy.onPreInit(event)
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.registryName)
        proxy.onInit(event)
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        proxy.onPostInit(event)
    }
}