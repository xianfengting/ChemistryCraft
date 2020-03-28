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

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import xyz.xianfengting.chemistrycraft.block.BlockSteamGenerator
import xyz.xianfengting.chemistrycraft.init.CCFluids

class TileEntitySteamGenerator : TileEntity(), ITickable {
    private val upInventory = FluidTank(FluidRegistry.WATER, 0, 8000)
    private val downInventory = FluidTank(CCFluids.STEAM, 0, 8000)
    private val inputInventory = ItemStackHandler(3)
    private val outputInventory = ItemStackHandler(3)
    var genProgress = 0
        private set
    val totalGenProgress = 100
    var fuelTime = 0
        private set
    var totalFuelTime = 0
        private set
    val inputTankAmount: Int
        get() = upInventory.fluidAmount
    val totalInputTankAmount = 8000
    val outputTankAmount: Int
        get() = downInventory.fluidAmount
    val totalOutputTankAmount = 8000

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability
                || CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability) {
            return true
        }
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> {
                val inventory: Any = when (facing) {
                    EnumFacing.DOWN -> outputInventory
                    else -> inputInventory
                }
                @Suppress("UNCHECKED_CAST")
                return inventory as T
            }
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY -> {
                val inventory: Any = when (facing) {
                    EnumFacing.UP -> upInventory
                    else -> downInventory
                }
                @Suppress("UNCHECKED_CAST")
                return inventory as T
            }
        }
        return super.getCapability(capability, facing)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        val upInventoryCompound = compound.getCompoundTag("UpInventory")
        val downInventoryCompound = compound.getCompoundTag("DownInventory")
        this.upInventory.readFromNBT(upInventoryCompound)
        this.downInventory.readFromNBT(downInventoryCompound)
        this.inputInventory.deserializeNBT(compound.getCompoundTag("InputInventory"))
        this.outputInventory.deserializeNBT(compound.getCompoundTag("OutputInventory"))
        this.genProgress = compound.getInteger("GenProgress")
        this.fuelTime = compound.getInteger("FuelTime")
        this.totalFuelTime = compound.getInteger("TotalFuelTime")
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        var compoundObj = compound
        compoundObj = super.writeToNBT(compoundObj)
        var upInventoryCompound = NBTTagCompound()
        var downInventoryCompound = NBTTagCompound()
        upInventoryCompound = this.upInventory.writeToNBT(upInventoryCompound)
        downInventoryCompound = this.downInventory.writeToNBT(downInventoryCompound)
        compoundObj.setTag("UpInventory", upInventoryCompound)
        compoundObj.setTag("DownInventory", downInventoryCompound)
        compoundObj.setTag("InputInventory", this.inputInventory.serializeNBT())
        compoundObj.setTag("OutputInventory", this.outputInventory.serializeNBT())
        compoundObj.setInteger("GenProgress", this.genProgress)
        compoundObj.setInteger("FuelTime", this.fuelTime)
        compoundObj.setInteger("TotalFuelTime", this.totalFuelTime)
        return compoundObj
    }

    override fun update() {
        if (!world.isRemote) {
            val state = world.getBlockState(pos)
//            val canBurn = fun (): Boolean {
//                val itemStack = upInventory.extractItem(0, 1, true)
//                if (itemStack == ItemStack.EMPTY)
//                    return false
//                for (i in FurnaceRecipes.instance().smeltingList.keys) {
//                    if (itemStack.item == i.item) {
////                        val result = FurnaceRecipes.instance().getSmeltingResult(itemStack)
////                        result.count = 1
//                        val resultItemStack = downInventory.getStackInSlot(0)
//                        if (resultItemStack.isEmpty)
//                            return true
//                        else
//                            return resultItemStack.count < resultItemStack.maxStackSize
//                    }
//                }
//                return false
//            }()
            val canBurn = true
            run {
                if (fuelTime == 0) {
                    val fuel = inputInventory.extractItem(0, 1, true)
                    if (fuel != ItemStack.EMPTY && TileEntityFurnace.isItemFuel(fuel) && canBurn) {
                        val temp = TileEntityIronFurnace.getFuelTime(fuel)
                        totalFuelTime = temp
                        fuelTime = temp
                        if (fuel.item == Items.LAVA_BUCKET) {
                            val emptyBucket = ItemStack(Items.BUCKET, 1)
//                            if (downInventory.insertItem(1, emptyBucket, true) != ItemStack.EMPTY) {
                            inputInventory.extractItem(0, 1, false)
                            outputInventory.insertItem(0, emptyBucket, false)
//                            }
                        } else {
                            inputInventory.extractItem(0, 1, false)
                        }
                        world.setBlockState(pos, state.withProperty(BlockSteamGenerator.WORKING, true))
                        markDirty()
                    } else {
                        world.setBlockState(pos, state.withProperty(BlockSteamGenerator.WORKING, false))
                    }
                } else {
                    fuelTime--
                    markDirty()
                }
            }
            run {
                if (canBurn) {
                    if (fuelTime > 0) {
                        genProgress++
                        if (genProgress >= totalGenProgress) {
                            genProgress = 0
                        }
                        if (upInventory.fluidAmount >= 10 && downInventory.fluidAmount <= downInventory.capacity - 10) {
                            upInventory.drain(FluidStack(FluidRegistry.WATER, 10), true)
                            downInventory.fill(FluidStack(CCFluids.STEAM, 10), true)
                        }
                        markDirty()
                    }
                }
            }
            run {
                val waterInput = inputInventory.extractItem(1, 1, true)
                if (waterInput != ItemStack.EMPTY) {
                    if (waterInput.item == Items.WATER_BUCKET && upInventory.fluidAmount <= upInventory.capacity - 1000) {
                        inputInventory.extractItem(1, 1, false)
                        upInventory.fill(FluidStack(FluidRegistry.WATER, 1000), true)
                        // TODO: Return the empty bucket
                    }
                    markDirty()
                }
            }

            syncToTrackingClients()
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

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        val nbt = NBTTagCompound()
        val upInventoryNBT = NBTTagCompound()
        val downInventoryNBT = NBTTagCompound()
        upInventory.writeToNBT(upInventoryNBT)
        downInventory.writeToNBT(downInventoryNBT)
        nbt.setTag("UpInventory", upInventoryNBT)
        nbt.setTag("DownInventory", downInventoryNBT)
        return SPacketUpdateTileEntity(pos, 1, nbt)
    }

    @SideOnly(Side.CLIENT)
    override fun onDataPacket(manager: NetworkManager, packet: SPacketUpdateTileEntity) {
        val nbt = packet.nbtCompound
        val upInventoryNBT = nbt.getCompoundTag("UpInventory")
        val downInventoryNBT = nbt.getCompoundTag("DownInventory")
        upInventory.readFromNBT(upInventoryNBT)
        downInventory.readFromNBT(downInventoryNBT)
    }
}
