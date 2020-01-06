## Cassandra 

Connector uses version 4 or `com.datastax.oss`. 


Use: 
```
import CassandraHelper._
import scala.concurrent.ExecutionContext.Implicits.global

val future = execute(cql"select ....", args)
```

