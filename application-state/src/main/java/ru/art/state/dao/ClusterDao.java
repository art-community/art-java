/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.state.dao;

import ru.art.entity.Value;
import ru.art.state.api.model.Cluster;
import static ru.art.rocks.db.dao.RocksDbValueDao.getAsProtobuf;
import static ru.art.rocks.db.dao.RocksDbValueDao.putAsProtobuf;
import static ru.art.state.api.mapping.ClusterMapper.fromCluster;
import static ru.art.state.api.mapping.ClusterMapper.toCluster;
import static ru.art.state.constants.StateModuleConstants.DbKeys.CLUSTER;

public interface ClusterDao {
    static void saveCluster(Cluster cluster) {
        putAsProtobuf(CLUSTER, fromCluster.map(cluster));
    }

    static Cluster loadCluster() {
        return getAsProtobuf(CLUSTER)
                .map(Value::asEntity)
                .map(toCluster::map)
                .orElse(Cluster.builder().build());
    }
}
