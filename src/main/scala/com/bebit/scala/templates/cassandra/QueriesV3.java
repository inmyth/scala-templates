package com.bebit.scala.templates.cassandra;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;

@Accessor
public interface QueriesV3 {

    @Query("SELECT * FROM dummy WHERE service_id = ? AND data_id = ?;")
    ListenableFuture<Dummy> fetchDummy(String serviceId, String dataId);


}
