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
import xyz.xianfengting.chemistrycraft.inventory.ContainerSteamGenerator
import kotlin.math.ceil
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.IFluidTank

@SideOnly(Side.CLIENT)
class GuiSteamGenerator(private val inventory: ContainerSteamGenerator)
    : GuiContainer(inventory) {

    companion object {
        const val TEXTURE_PATH = ChemistryCraft.MOD_ID + ":" + "textures/gui/container/gui_steam_generator.png"

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

        run {
            val fuelTime = inventory.fuelTime
            val textureHeight = ceil(17.0 * fuelTime / inventory.totalFuelTime).toInt()
            drawTexturedModalRect(offsetX + 43, offsetY + 52 + (18 - textureHeight), 24,
                    156 + (18 - textureHeight), 3, textureHeight)
        }
        run {
            val genProgress = inventory.genProgress
            val textureWidth = 1 + ceil(22.0 * genProgress / inventory.totalGenProgress).toInt()
            drawTexturedModalRect(offsetX + 79, offsetY + 33, 0, 156, textureWidth, 17)
        }
//        run {
//            val inputTankAmount = inventory.inputTankAmount
//            val textureHeight = ceil(47.0 * inputTankAmount / inventory.totalInputTankAmount).toInt()
//            drawTexturedModalRect(offsetX + 54, offsetY + 19 + (47 - textureHeight), 0,
//                    173 + (47 - textureHeight), 12, textureHeight)
//        }
//        run {
//            val outputTankAmount = inventory.outputTankAmount
//            val textureHeight = ceil(47.0 * outputTankAmount / inventory.totalOutputTankAmount).toInt()
//            drawTexturedModalRect(offsetX + 109, offsetY + 19 + (47 - textureHeight), 0,
//                    173 + (47 - textureHeight), 12, textureHeight)
//        }
        renderFluidTank(inventory.upFluid as FluidTank, offsetX + 54, offsetY + 19,
                12, 47)
        renderFluidTank(inventory.downFluid as FluidTank, offsetX + 109, offsetY + 19,
                12, 47)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val offsetX = (width - xSize) / 2
        val offsetY = (height - ySize) / 2

        val title = I18n.format("tile.${ChemistryCraft.MOD_ID}.steam_generator.name")
        fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 6, 0x404040)

        if (mouseX in offsetX + 43 .. offsetX + 45 && mouseY in offsetY + 52 .. offsetY + 69) {
            val fuelTimeStr = I18n.format("gui.chemistrycraft.tooltip.fuel_time",
                    inventory.fuelTime, inventory.totalFuelTime)
            drawHoveringText(fuelTimeStr, mouseX - offsetX, mouseY - offsetY)
        }
    }

    /**
     * Draw a fluid tank with given parameters. If the given tank is null, it will do nothing.
     * @param tank The [IFluidTank] instance, will keep unmodified
     * @param x The starting x-coordinate of tank
     * @param y The starting y-coordinate of tank
     * @param tankWidth The fluid tank full width in the GUI texture
     * @param tankHeight The fluid tank full height in the GUI texture
     * @author 3TUSK
     */
    private fun renderFluidTank(tank: IFluidTank?, x: Int, y: Int, tankWidth: Int, tankHeight: Int) {
        if (tank == null || tank.fluid == null)
            return

        val fluidSprite = this.mc.textureMapBlocks.getAtlasSprite(tank.fluid!!.fluid.still.toString())
        this.mc.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

        val scaledHeight = tankHeight * tank.fluidAmount / tank.capacity
        this.drawTexturedModalRect(x, y + tankHeight - scaledHeight, fluidSprite, tankWidth, scaledHeight)
    }
}
