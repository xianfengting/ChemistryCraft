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
import net.minecraft.inventory.SlotFurnaceFuel
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import xyz.xianfengting.chemistrycraft.tileentity.TileEntityIronFurnace
import sun.audio.AudioPlayer.player
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.SlotItemHandler



class ContainerIronFurnace(player: EntityPlayer, tileEntity: TileEntity) : Container() {

    private val upItems = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
    private val downItems = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)
    private val fuelItems = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
    private val tileEntity = tileEntity as TileEntityIronFurnace
    var burnTime = 0
        private set
    var totalBurnTime = 0
        private set
    var fuelTime = 0
        private set
    var totalFuelTime = 0
        private set

    init {
        // 0 ~ 3
        addSlotToContainer(SlotItemHandler(upItems, 0, 56, 30))
        addSlotToContainer(object : SlotItemHandler(downItems, 0, 110, 30) {
            override fun isItemValid(stack: ItemStack) = false
        })
        addSlotToContainer(object : SlotItemHandler(fuelItems, 0, 56, 48) {
            override fun isItemValid(stack: ItemStack) = super.isItemValid(stack) // TODO
        })
        addSlotToContainer(object : SlotItemHandler(downItems, 1, 74, 48) {
            override fun isItemValid(stack: ItemStack) = false
        })

        // 4 ~ 30
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18))
            }
        }

        // 31 ~ 39
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
            in 0..3 -> isMerged = mergeItemStack(newStack, 4, 39, true)
            in 4..30 -> isMerged = mergeItemStack(newStack, 0, 3, false)
                    || mergeItemStack(newStack, 31, 39, false)
            in 31..39 -> isMerged = mergeItemStack(newStack, 0, 3, false)
                    || mergeItemStack(newStack, 4, 30, false)
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

        burnTime = tileEntity.burnTime
        totalBurnTime = tileEntity.totalBurnTime
        fuelTime = tileEntity.fuelTime
        totalFuelTime = tileEntity.totalFuelTime

        for (listener in listeners) {
            listener.sendWindowProperty(this, 0, burnTime)
            listener.sendWindowProperty(this, 1, totalBurnTime)
            listener.sendWindowProperty(this, 2, fuelTime)
            listener.sendWindowProperty(this, 3, totalFuelTime)
        }
    }

    @SideOnly(Side.CLIENT)
    override fun updateProgressBar(id: Int, data: Int) {
        super.updateProgressBar(id, data)

        when (id) {
            0 -> burnTime = data
            1 -> totalBurnTime = data
            2 -> fuelTime = data
            3 -> totalFuelTime = data
        }
    }
}
