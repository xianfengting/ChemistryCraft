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
package xyz.xianfengting.chemistrycraft.networking

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import java.nio.charset.Charset

class MessageSyncFluidTank : IMessage {
    var fluidTank = FluidTank(0)

    override fun fromBytes(buf: ByteBuf) {
        val fluidNameBytesLen = buf.readInt()
        val fluidNameBytes = buf.readBytes(fluidNameBytesLen).array()
        val fluidName = String(fluidNameBytes, Charset.forName("utf-8"))
        if (fluidName == "NULL")
            return
        val fluid = FluidRegistry.getFluid(fluidName)
        val fluidAmount = buf.readInt()
        fluidTank.fluid = FluidStack(fluid, fluidAmount)
    }

    override fun toBytes(buf: ByteBuf) {
        val fluidName = fluidTank.fluid.let { if (it == null) "NULL" else it.fluid.name }
        val fluidNameBytes = fluidName.toByteArray() // Uses UTF-8 by default
        buf.writeInt(fluidNameBytes.size)
        buf.writeBytes(fluidNameBytes)
        if (fluidName == "NULL")
            return
        buf.writeInt(fluidTank.fluid!!.amount)
    }
}
