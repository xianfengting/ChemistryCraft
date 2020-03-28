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

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import xyz.xianfengting.chemistrycraft.ChemistryCraft

object CCSimpleNetworkHandler {
    private val channel = NetworkRegistry.INSTANCE.newSimpleChannel(ChemistryCraft.MOD_ID)

    fun sendMessageToDim(msg: IMessage, dim: Int) {
        channel.sendToDimension(msg, dim)
    }

    fun sendMessageAroundPos(msg: IMessage, dim: Int, pos: BlockPos) {
        channel.sendToAllAround(msg, NetworkRegistry.TargetPoint(dim,
                pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 2.0))
    }

    fun sendMessageToPlayer(msg: IMessage, player: EntityPlayerMP) {
        channel.sendTo(msg, player)
    }

    fun sendMessageToAll(msg: IMessage) {
        channel.sendToAll(msg)
    }

    fun sendMessageToServer(msg: IMessage) {
        channel.sendToServer(msg)
    }
}
