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
