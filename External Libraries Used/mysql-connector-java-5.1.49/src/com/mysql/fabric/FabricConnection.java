/*
  Copyright (c) 2013, 2016, Oracle and/or its affiliates. Projects_And_Users rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.fabric;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.mysql.fabric.proto.xmlrpc.XmlRpcClient;

public class FabricConnection {
    private XmlRpcClient client;

    // internal caches
    private Map<String, ShardMapping> shardMappingsByTableName = new HashMap<String, ShardMapping>();
    private Map<String, ServerGroup> serverGroupsByName = new HashMap<String, ServerGroup>();
    private long shardMappingsExpiration;
    private int shardMappingsTtl;
    private long serverGroupsExpiration;
    private int serverGroupsTtl;

    public FabricConnection(String url, String username, String password) throws FabricCommunicationException {
        this.client = new XmlRpcClient(url, username, password);
        refreshState();
    }

    /**
     * @param urls
     * @param username
     * @param password
     * @throws FabricCommunicationException
     */
    public FabricConnection(Set<String> urls, String username, String password) throws FabricCommunicationException {
        throw new UnsupportedOperationException("Multiple connections not supported.");
    }

    public String getInstanceUuid() {
        return null;
    }

    public int getVersion() {
        return 0;
    }

    /**
     * @return version of state data
     */
    public int refreshState() throws FabricCommunicationException {
        FabricStateResponse<Set<ServerGroup>> serverGroups = this.client.getServerGroups();
        FabricStateResponse<Set<ShardMapping>> shardMappings = this.client.getShardMappings();

        this.serverGroupsExpiration = serverGroups.getExpireTimeMillis();
        this.serverGroupsTtl = serverGroups.getTtl();
        for (ServerGroup g : serverGroups.getData()) {
            this.serverGroupsByName.put(g.getName(), g);
        }

        this.shardMappingsExpiration = shardMappings.getExpireTimeMillis();
        this.shardMappingsTtl = shardMappings.getTtl();
        for (ShardMapping m : shardMappings.getData()) {
            // a shard mapping may be associated with more than one table
            for (ShardTable t : m.getShardTables()) {
                this.shardMappingsByTableName.put(t.getDatabase() + "." + t.getTable(), m);
            }
        }

        return 0;
    }

    public int refreshStatePassive() {
        try {
            return refreshState();
        } catch (FabricCommunicationException e) {
            // Fabric node is down but we can operate on previous setup. Just reset the TTL timers.
            this.serverGroupsExpiration = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.serverGroupsTtl);
            this.shardMappingsExpiration = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.shardMappingsTtl);
        }

        return 0;
    }

    public ServerGroup getServerGroup(String serverGroupName) {
        if (isStateExpired()) {
            refreshStatePassive();
        }
        return this.serverGroupsByName.get(serverGroupName);
    }

    public ShardMapping getShardMapping(String database, String table) {
        if (isStateExpired()) {
            refreshStatePassive();
        }
        return this.shardMappingsByTableName.get(database + "." + table);
    }

    public boolean isStateExpired() {
        return System.currentTimeMillis() > this.shardMappingsExpiration || System.currentTimeMillis() > this.serverGroupsExpiration;
    }

    public Set<String> getFabricHosts() {
        return null;
    }

    public XmlRpcClient getClient() {
        return this.client;
    }
}
