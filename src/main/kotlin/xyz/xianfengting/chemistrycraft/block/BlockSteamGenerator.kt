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
package xyz.xianfengting.chemistrycraft.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import xyz.xianfengting.chemistrycraft.ChemistryCraft
import xyz.xianfengting.chemistrycraft.init.CCGuiElements
import xyz.xianfengting.chemistrycraft.tileentity.TileEntitySteamGenerator
import java.lang.RuntimeException
import kotlin.math.abs

class BlockSteamGenerator : BlockContainerBase(Material.IRON, "steam_generator") {
    companion object {
        val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
        val WORKING = PropertyBool.create("working")
    }

    override fun createBlockState() = BlockStateContainer(this, FACING, WORKING)

    override fun getStateFromMeta(meta: Int): IBlockState {
        val facing = EnumFacing.getHorizontal(meta and 3)
        val working = meta and 4 != 0
        return this.defaultState.withProperty(FACING, facing).withProperty(WORKING, working)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        val facing = state.getValue(FACING).horizontalIndex
        val working = if (state.getValue(WORKING)) 4 else 0
        return facing or working
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntitySteamGenerator()

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
                                  hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote) {
            playerIn.openGui(ChemistryCraft, CCGuiElements.STEAM_GENERATOR, worldIn, pos.x, pos.y, pos.z)
        }
        return true
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val te = worldIn.getTileEntity(pos) as TileEntitySteamGenerator

        val fuel = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
                ?: throw RuntimeException("Getting fuel capability failed")

        for (i in fuel.slots - 1 downTo 0) {
            Block.spawnAsEntity(worldIn, pos, fuel.getStackInSlot(i))
            (fuel as IItemHandlerModifiable).setStackInSlot(i, ItemStack.EMPTY)
        }

        super.breakBlock(worldIn, pos, state)
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase,
                                 stack: ItemStack) {
        var degree = abs(placer.rotationYawHead % 360.0f)
        if (placer.rotationYawHead < 0f) {
            degree = 360f - degree
        }
        if ((315.0f <= degree && degree < 360.0f) || (0 <= degree && degree < 45.0f)) {
            // Placer towards SOUTH
            // Block towards NORTH
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.NORTH))
        } else if (45.0f <= degree && degree < 135.0f) {
            // Placer towards WEST
            // Block towards EAST
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.EAST))
        } else if (135.0f <= degree && degree < 225.0f) {
            // Placer towards NORTH
            // Block towards SOUTH
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.SOUTH))
        } else {
            // Placer towards EAST
            // Block towards WEST
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.WEST))
        }
    }
}
