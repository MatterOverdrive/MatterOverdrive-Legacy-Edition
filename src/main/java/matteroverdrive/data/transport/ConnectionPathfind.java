/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */
package matteroverdrive.data.transport;

import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.util.MOLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Simeon on 1/20/2016.
 */
public class ConnectionPathfind<T extends IGridNode> {
    private final Set<T> burned;
    private final T target;
    private Class<T> nodeTypes;
    private TileEntity neighborTileTmp;
    private T neighborTmp;
    private IBlockState neighborTmpState;
    private BlockPos neighborPosTmp;

    public ConnectionPathfind(final T target, final Class<T> nodeTypes) {
        this.burned = new HashSet<>();
        this.target = target;
        this.nodeTypes = nodeTypes;
    }

    public boolean init(final T startNode) {
        burned.clear();
        burned.add(startNode);

        for (EnumFacing d : EnumFacing.VALUES) {
            if (startNode.canConnectFromSide(startNode.getNodeWorld().getBlockState(startNode.getNodePos()), d)) {
                BlockPos neighborPos = startNode.getNodePos().offset(d);
                TileEntity neighborTile = startNode.getNodeWorld().getTileEntity(neighborPos);
                if (neighborTile instanceof IGridNode && neighborTile != target) {
                    if (isConnectedToSourceRecursive((T) neighborTile)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isConnectedToSourceRecursive(final T node) {
        this.burned.add(node);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (node.canConnectFromSide(node.getNodeWorld().getBlockState(node.getNodePos()), dir)) {
                neighborPosTmp = node.getNodePos().offset(dir);
                if (node.getNodeWorld().isBlockLoaded(neighborPosTmp)) {
                    neighborTileTmp = node.getNodeWorld().getTileEntity(neighborPosTmp);
                    if (nodeTypes.isInstance(neighborTileTmp)) {
                        neighborTmp = nodeTypes.cast(neighborTileTmp);
                        neighborTmpState = neighborTmp.getNodeWorld().getBlockState(neighborPosTmp);
                        if (neighborTmp == target || !this.burned.contains(neighborTmp)) {
                            if (node.canConnectToNetworkNode(node.getNodeWorld().getBlockState(node.getNodePos()), neighborTmp, dir) && neighborTmp.canConnectToNetworkNode(neighborTmpState, node, dir.getOpposite())) {
                                if (neighborTmp == target) {
                                    return true;
                                }

                                if (isConnectedToSourceRecursive(neighborTmp)) {
                                    return true;
                                }
                            } else {
                                MOLog.info("Matter Network Pathfind: cannot connect to node");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Collection<T> getBurnedNodes() {
        return burned;
    }
}
