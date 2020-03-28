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
package xyz.xianfengting.chemistrycraft.tileentity

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import xyz.xianfengting.chemistrycraft.block.BlockIronFurnace

class TileEntityIronFurnace : TileEntity(), ITickable {

    companion object {
        // Normal furnace's speed : Iron furnace's speed = 1 : 2
        fun getFuelTime(stack: ItemStack) = TileEntityFurnace.getItemBurnTime(stack)// * 2 * (1 / 2)
        fun getFuelTime(item: Item) = getFuelTime(ItemStack(item, 1))
    }

    private val upInventory = ItemStackHandler()
    /** 0: Smelting result, 1: Fuel result (e.g. lava bucket -> empty bucket) */
    private val downInventory = ItemStackHandler(2)
    private val fuelInventory = ItemStackHandler()
    var burnTime = 0
        private set
    val totalBurnTime = 100
    var fuelTime = 0
        private set
    var totalFuelTime = 0
        private set

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability) {
            return true
        }
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            val inventory: Any = when (facing) {
                EnumFacing.UP -> upInventory
                EnumFacing.DOWN -> downInventory
                else -> fuelInventory
            }
            @Suppress("UNCHECKED_CAST")
            return inventory as T
        }
        return super.getCapability(capability, facing)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        this.upInventory.deserializeNBT(compound.getCompoundTag("UpInventory"))
        this.downInventory.deserializeNBT(compound.getCompoundTag("DownInventory"))
        this.fuelInventory.deserializeNBT(compound.getCompoundTag("FuelInventory"))
        this.burnTime = compound.getInteger("BurnTime")
//        this.totalBurnTime = compound.getInteger("TotalBurnTime")
        this.fuelTime = compound.getInteger("FuelTime")
        this.totalFuelTime = compound.getInteger("TotalFuelTime")
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("UpInventory", this.upInventory.serializeNBT())
        compound.setTag("DownInventory", this.downInventory.serializeNBT())
        compound.setTag("FuelInventory", this.fuelInventory.serializeNBT())
        compound.setInteger("BurnTime", this.burnTime)
//        compound.setInteger("TotalBurnTime", this.totalBurnTime)
        compound.setInteger("FuelTime", this.fuelTime)
        compound.setInteger("TotalFuelTime", this.totalFuelTime)
        return compound
    }

    override fun update() {
        if (!world.isRemote) {
            val state = world.getBlockState(pos)
            val canBurn = fun (): Boolean {
                val itemStack = upInventory.extractItem(0, 1, true)
                if (itemStack == ItemStack.EMPTY)
                    return false
                for (i in FurnaceRecipes.instance().smeltingList.keys) {
                    if (itemStack.item == i.item) {
//                        val result = FurnaceRecipes.instance().getSmeltingResult(itemStack)
//                        result.count = 1
                        val resultItemStack = downInventory.getStackInSlot(0)
                        val iResult = FurnaceRecipes.instance().getSmeltingResult(i)
                        return if (resultItemStack.isEmpty)
                            true
                        else
                            iResult.item == resultItemStack.item
                                    && resultItemStack.count < resultItemStack.maxStackSize
                    }
                }
                return false
            }()
            run {
                if (fuelTime == 0) {
                    val fuel = fuelInventory.extractItem(0, 1, true)
                    if (fuel != ItemStack.EMPTY && TileEntityFurnace.isItemFuel(fuel) && canBurn) {
                        val temp = getFuelTime(fuel)
                        totalFuelTime = temp
                        fuelTime = temp
                        if (fuel.item == Items.LAVA_BUCKET) {
                            val emptyBucket = ItemStack(Items.BUCKET, 1)
//                            if (downInventory.insertItem(1, emptyBucket, true) != ItemStack.EMPTY) {
                                fuelInventory.extractItem(0, 1, false)
                                downInventory.insertItem(1, emptyBucket, false)
//                            }
                        } else {
                            fuelInventory.extractItem(0, 1, false)
                        }
                        world.setBlockState(pos, state.withProperty(BlockIronFurnace.BURNING, true))
                        markDirty()
                    } else {
                        world.setBlockState(pos, state.withProperty(BlockIronFurnace.BURNING, false))
                    }
                } else {
                    fuelTime--
                    markDirty()
                }
            }
            run {
                if (canBurn) {
                    if (fuelTime > 0) {
                        burnTime++
                        if (burnTime >= totalBurnTime) {
                            burnTime = 0
                            val material = upInventory.extractItem(0, 1, false)
                            val curResult = downInventory.getStackInSlot(0)
                            if (curResult.count > 1) {
                                val result = curResult.copy()
                                result.count++
                                downInventory.setStackInSlot(0, result)
                            } else {
                                val result = FurnaceRecipes.instance().getSmeltingResult(material)
                                result.count = 1
                                downInventory.insertItem(0, result, false)
                            }
                            markDirty()
                        }
                    }
                }
            }
        }
    }

    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean {
        // Forge typo: newSate X  newState V
        return oldState.block != newState.block
    }

    // Copied from TileEntityFurnace
    fun isUsableByPlayer(player: EntityPlayer): Boolean {
        return if (this.world.getTileEntity(this.pos) != this) {
            false
        } else {
            player.getDistanceSq(this.pos.x.toDouble() + 0.5, this.pos.y.toDouble() + 0.5, this.pos.z.toDouble() + 0.5) <= 64.0
        }
    }
}
