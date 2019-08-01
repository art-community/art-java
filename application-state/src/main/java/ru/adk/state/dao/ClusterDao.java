package ru.adk.state.dao;

import ru.adk.entity.Value;
import ru.adk.state.api.model.Cluster;
import static java.util.Objects.isNull;
import static ru.adk.entity.Value.asEntity;
import static ru.adk.rocks.db.dao.RocksDbValueDao.getAsProtobuf;
import static ru.adk.rocks.db.dao.RocksDbValueDao.putAsProtobuf;
import static ru.adk.state.api.mapping.ClusterMapper.fromCluster;
import static ru.adk.state.api.mapping.ClusterMapper.toCluster;
import static ru.adk.state.constants.StateModuleConstants.DbKeys.CLUSTER;

public interface ClusterDao {
    static void saveCluster(Cluster cluster) {
        putAsProtobuf(CLUSTER, fromCluster.map(cluster));
    }

    static Cluster loadCluster() {
        Value value = getAsProtobuf(CLUSTER);
        if (isNull(value)) return Cluster.builder().build();
        return toCluster.map(asEntity(value));
    }
}
