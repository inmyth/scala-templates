## Cassandra 

Connector uses version 4 or `com.datastax.oss`. 

Default contact point is `127.0.0.1:9042`.

Keyspace should not be changed during runtime. One running app should have one keyspace.

Use: 
```
import CassandraHelper._
import scala.concurrent.ExecutionContext.Implicits.global

val future = execute(cql"SELECT * FROM my_table WHERE my_key = ?", 44)
```




