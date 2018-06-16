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

import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.transport.IGridNetwork;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.handler.matter_network.MatterNetworkHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 1/27/2016.
 */
public class MatterNetwork extends AbstractGridNetwork<IMatterNetworkConnection> {
    List<IMatterNetworkClient> clients;

    public MatterNetwork(MatterNetworkHandler handler) {
        super(handler, IMatterNetworkConnection.class);
        clients = new ArrayList<>();
    }

    @Override
    protected void onNodeAdded(IMatterNetworkConnection node) {
        if (node instanceof IMatterNetworkClient) {
            IMatterNetworkClient client = (IMatterNetworkClient) node;
            post(new IMatterNetworkEvent.ClientAdded(client));
            clients.add(client);
            client.getMatterNetworkComponent().onNetworkEvent(new IMatterNetworkEvent.AddedToNetwork());
        }
    }

    @Override
    protected void onNodeRemoved(IMatterNetworkConnection node) {
        if (node instanceof IMatterNetworkClient) {
            IMatterNetworkClient client = (IMatterNetworkClient) node;
            post(new IMatterNetworkEvent.ClientRemoved(client));
            clients.remove(client);
            client.getMatterNetworkComponent().onNetworkEvent(new IMatterNetworkEvent.RemovedFromNetwork());
        }
    }

    public void post(IMatterNetworkEvent event) {
        for (IMatterNetworkClient client : clients) {
            client.getMatterNetworkComponent().onNetworkEvent(event);
        }
    }

    public void post(IMatterNetworkClient poster, IMatterNetworkEvent event) {
        for (IMatterNetworkClient client : clients) {
            client.getMatterNetworkComponent().onNetworkEvent(event);
        }
    }

    @Override
    public void recycle() {
        clients.clear();
        super.recycle();
    }

    @Override
    public boolean canMerge(IGridNetwork network) {
        return true;
    }

    public List<IMatterNetworkClient> getClients() {
        return clients;
    }
}
