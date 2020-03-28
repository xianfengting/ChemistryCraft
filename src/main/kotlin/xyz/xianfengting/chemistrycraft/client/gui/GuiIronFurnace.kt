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
package xyz.xianfengting.chemistrycraft.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.inventory.ContainerIronFurnace
import kotlin.math.ceil

@SideOnly(Side.CLIENT)
class GuiIronFurnace(private val inventory: ContainerIronFurnace)
    : GuiContainer(inventory) {

    companion object {
        const val TEXTURE_PATH = ChemistryCraft.MOD_ID + ":" + "textures/gui/container/gui_iron_furnace.png"

        private val TEXTURE = ResourceLocation(TEXTURE_PATH)
    }

    init {
        xSize = 176
        ySize = 156
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f)

        mc.textureManager.bindTexture(TEXTURE)
        val offsetX = (width - xSize) / 2
        val offsetY = (height - ySize) / 2

        drawTexturedModalRect(offsetX, offsetY, 0, 0, xSize, ySize)

        val fuelTime = inventory.fuelTime
        val textureHeight = ceil(17.0 * fuelTime / inventory.totalFuelTime).toInt()
        drawTexturedModalRect(offsetX + 91, offsetY + 47 + (18 - textureHeight), 24,
                156 + (18 - textureHeight), 3, textureHeight)
//        drawTexturedModalRect(offsetX + 91, offsetY + 47 + (17 - textureHeight), 0,
//                156 + (17 - textureHeight), 22, textureHeight)

        val burnTime = inventory.burnTime
        val textureWidth = 1 + ceil(22.0 * burnTime / inventory.totalBurnTime).toInt()
        drawTexturedModalRect(offsetX + 79, offsetY + 30, 0, 156, textureWidth, 17)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val offsetX = (width - xSize) / 2
        val offsetY = (height - ySize) / 2

        val title = I18n.format("tile.${ChemistryCraft.MOD_ID}.iron_furnace.name")
        fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 6, 0x404040)

        if (mouseX in offsetX + 91 .. offsetX + 93 && mouseY in offsetY + 47 .. offsetY + 64) {
            val fuelTimeStr = I18n.format("gui.chemistrycraft.tooltip.fuel_time",
                    inventory.fuelTime, inventory.totalFuelTime)
            drawHoveringText(fuelTimeStr, mouseX - offsetX, mouseY - offsetY)
        }
    }
}
