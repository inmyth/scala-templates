# Cassandra 

Keyspace should not be changed during runtime. One running app should have one keyspace.

## Version 3.x
Version 3 uses Mapper. 



## Version 4.x
Versions 4 does not use Mapper. Object mapping is handled manually.

Version 4 uses completely different annotations. It also uses annotation processor that generates factory classes. 
Upgrading to version 4 will need complete rewrite of entities that use annotations. 


Sample query in v.4: 
```
import CassandraHelper._
import scala.concurrent.ExecutionContext.Implicits.global

val future = execute(cql"SELECT * FROM my_table WHERE my_key = ?", 44)
```


## Set up
Default contact point is `127.0.0.1:9042`.









