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
package xyz.xianfengting.chemistrycraft.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler
import xyz.xianfengting.chemistrycraft.tileentity.TileEntitySteamGenerator

class ContainerSteamGenerator(player: EntityPlayer, tileEntity: TileEntity) : Container() {
    internal val upFluid
        get() = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)
    internal val downFluid
        get() = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)
    private val inputItems = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
    private val outputItems = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)
    private val tileEntity = tileEntity as TileEntitySteamGenerator
    var genProgress = 0
        private set
    var totalGenProgress = 0
        private set
    var fuelTime = 0
        private set
    var totalFuelTime = 0
        private set
    var inputTankAmount = 0
        private set
    var totalInputTankAmount = 0
        private set
    var outputTankAmount = 0
        private set
    var totalOutputTankAmount = 0
        private set

    init {
        // 0 ~ 5
        addSlotToContainer(object : SlotItemHandler(inputItems, 0, 8, 53) {
            override fun isItemValid(stack: ItemStack) = super.isItemValid(stack) // TODO
        })
        addSlotToContainer(object : SlotItemHandler(outputItems, 0, 26, 53) {
            override fun isItemValid(stack: ItemStack) = false
        })
        addSlotToContainer(object : SlotItemHandler(inputItems, 1, 32, 16) {
            override fun isItemValid(stack: ItemStack) = super.isItemValid(stack) // TODO
        })
        addSlotToContainer(object : SlotItemHandler(outputItems, 1, 32, 34) {
            override fun isItemValid(stack: ItemStack) = false
        })
        addSlotToContainer(object : SlotItemHandler(inputItems, 2, 127, 16) {
            override fun isItemValid(stack: ItemStack) = super.isItemValid(stack) // TODO
        })
        addSlotToContainer(object : SlotItemHandler(outputItems, 2, 127, 34) {
            override fun isItemValid(stack: ItemStack) = false
        })

        // 6 ~ 32
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18))
            }
        }

        // 33 ~ 41
        for (i in 0..8) {
            addSlotToContainer(Slot(player.inventory, i, 8 + i * 18, 132))
        }
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val slot = inventorySlots[index]

        if (slot == null || !slot.hasStack) {
            return ItemStack.EMPTY
        }

        val newStack = slot.stack
        val oldStack = newStack.copy()

        var isMerged = false

        when (index) {
            in 0..5 -> isMerged = mergeItemStack(newStack, 6, 41, true)
            in 6..32 -> isMerged = mergeItemStack(newStack, 0, 5, false)
                    || mergeItemStack(newStack, 33, 41, false)
            in 33..41 -> isMerged = mergeItemStack(newStack, 0, 5, false)
                    || mergeItemStack(newStack, 6, 32, false)
        }

        if (!isMerged) {
            return ItemStack.EMPTY
        }

        if (newStack.count == 0) {
            slot.putStack(ItemStack.EMPTY)
        } else {
            slot.onSlotChanged()
        }

        slot.onTake(playerIn, newStack)

        return oldStack
    }

    override fun canInteractWith(playerIn: EntityPlayer) = tileEntity.isUsableByPlayer(playerIn)

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()

        genProgress = tileEntity.genProgress
        totalGenProgress = tileEntity.totalGenProgress
        fuelTime = tileEntity.fuelTime
        totalFuelTime = tileEntity.totalFuelTime
        inputTankAmount = tileEntity.inputTankAmount
        totalInputTankAmount = tileEntity.totalInputTankAmount
        outputTankAmount = tileEntity.outputTankAmount
        totalOutputTankAmount = tileEntity.totalOutputTankAmount

        for (listener in listeners) {
            listener.sendWindowProperty(this, 0, genProgress)
            listener.sendWindowProperty(this, 1, totalGenProgress)
            listener.sendWindowProperty(this, 2, fuelTime)
            listener.sendWindowProperty(this, 3, totalFuelTime)
            listener.sendWindowProperty(this, 4, inputTankAmount)
            listener.sendWindowProperty(this, 5, totalInputTankAmount)
            listener.sendWindowProperty(this, 6, outputTankAmount)
            listener.sendWindowProperty(this, 7, totalOutputTankAmount)
        }
    }

    @SideOnly(Side.CLIENT)
    override fun updateProgressBar(id: Int, data: Int) {
        super.updateProgressBar(id, data)

        when (id) {
            0 -> genProgress = data
            1 -> totalGenProgress = data
            2 -> fuelTime = data
            3 -> totalFuelTime = data
            4 -> inputTankAmount = data
            5 -> totalInputTankAmount = data
            6 -> outputTankAmount = data
            7 -> totalOutputTankAmount = data
        }
    }
}
