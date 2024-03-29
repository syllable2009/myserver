#ES-node
node.master:true/false
node.data:true/false
这是ES节点默认配置，既作为候选节点又作为数据节点，这样的节点一旦被选举为Master，压力是比较大的，通常来说Master节点应该只承担较为轻量级的任务，比如创建删除索引，分片均衡等。
2) node.master = true node.data = false
只作为候选节点，不作为数据节点，可参选Master节点，当选后成为真正的Master节点。
3) node.master = false node.data = false
既不当候选节点，也不作为数据节点，那就是仅协调节点，负责负载均衡
4) node.master=false node.data=true
不作为候选节点，但是作为数据节点，这样的节点主要负责数据存储和查询服务。
#角色:
Master: 主节点（主节点，每个集群都有且只有一个）
master节点的主要职责是和集群操作相关的内容，如创建或删除索引，跟踪哪些节点是群集的一部分，并决定哪些分片分配给相关的节点。
稳定的主节点对集群的健康是非常重要的，默认情况下任何一个集群中的节点都有可能被选为主节点，
索引数据和搜索查询等操作会占用大量的cpu，内存，io资源，为了确保一个集群的稳定，分离master和数据节点是一个比较好的选择：
也就是尽量避免Master节点设置 node.data ＝ true
voting: 投票节点（不竞选主节点）
通过设置配置中的node.voting_only=true，（仅投票节点，即使配置了node.master = true，也不会参选, 但是仍然可以作为数据节点）。
Master-eligible node (候选节点)
当node.master为true时，其表示这个node是一个master的候选节点，可以参与选举，在ES的文档中常被称作master-eligible node，类似于MasterCandidate。ES正常运行时只能有一个master(即leader)，多于1个时会发生脑裂。
Data node (数据节点)
当node.data为true时，这个节点作为一个数据节点，数据节点主要是存储索引数据的节点，主要对文档进行增删改查操作，聚合操作等。数据节点对cpu，内存，io要求较高， 在优化的时候需要监控数据节点的状态，当资源不够的时候，需要在集群中添加新的节点。
coordinating: 协调节点
当node.master和node.data配置都设置为false的时候，该节点只能处理路由请求，处理搜索，分发索引操作等，从本质上来说该客户节点表现为智能负载平衡器。
独立的协调节点在一个比较大的集群中是非常有用的，他协调主节点和数据节点，客户端节点加入集群可以得到集群的状态，根据集群的状态可以直接路由请求。
每一个节点都隐式的是一个协调节点，如果同时设置了node.master = false和node.data=false，那么此节点将成为仅协调节点。
不常用：
Ingest node（预处理节点）
Machine learning node (机器学习节点)

建议
在一个生产集群中我们可以对这些节点的职责进行划分，建议集群中设置3台以上的节点作为master节点，这些节点只负责成为主节点，维护整个集群的状态。
再根据数据量设置一批data节点，这些节点只负责存储数据，后期提供建立索引和查询索引的服务，
这样的话如果用户请求比较频繁，这些节点的压力也会比较大，所以在集群中建议再设置一批协调节点(node.master: false node.data: false)，这些节点只负责处理用户请求，实现请求转发，负载均衡等功能。

高可用性（生产环境均为一台机器一个节点）

(1) ES在分配单个索引的分片时会将每个分片尽可能分配到更多的节点上。但是，实际情况取决于集群拥有的分片和索引的数量以及它们的大小，不一定总是能均匀地分布。
(2) ES不允许Primary和它的Replica放在同一个节点中，并且同一个节点不接受完全相同的两个Replica
(3) 同一个节点允许多个索引的分片同时存在。

#分片
把分片想象成数据的容器。文档存储在分片中，然后分片分配到集群中的节点上。当集群扩容或缩小，Elasticsearch 将会自动在节点间迁移分片，以使集群保持平衡。
分片可以是主分片(primary shard)或者是复制分片(replica shard)。
Elasticsearch 如何知道一个文档应该存放到哪个分片中呢？当我们创建文档时，它如何决定这个文档应当被存储在分片 1 还是分片 2 中呢？
curl -H "Content-Type: application/json" -XPUT localhost:9200/blogs -d '
{
    "settings": {
        "number_of_shards": 3,
        "number_of_replicas": 1
    }
}'
shard = hash(routing) % number_of_primary_shards
routing 是一个可变值，唯一不可重复，默认是文档的 _id ，也可以设置成一个自定义的值。 routing 通过 hash 函数生成一个数字，然后这个数字再除以 number_of_primary_shards （主分片的数量）后得到余数 。这个分布在 0 到 number_of_primary_shards-1 之间的余数，就是我们所寻求的文档所在分片的位置。
这就解释了为什么我们要在创建索引的时候就确定好主分片的数量 并且永远不会改变这个数量：因为如果数量变化了，那么所有之前路由的值都会无效，文档也再也找不到了。

#索引
在创建某个索引之前，需要指定分配这个索引多少个分片？多少个副本？副本就这这个分片的备胎，当分片挂掉了，它的副本就会随时准备上位，因此副本也是个分片只不过不负责主要功能。
不仅仅如此，ES 如何能够提高数据吞吐量呢？增加副本个数就是个不错的选择，比如说读写分离，读数据的时候从副本上读，写数据的时候只用主分片去写。需要注意的是，主分片的个数实在建立索引之前要确定，建立完索引之后，是不能够进行修改的，除非重新建索引。因此在建索引之前，一定要合理的配置分片个数，副本个数的话后期是可以改动的。
为提高查询吞吐量或实现高可用性，可以使用分片副本。
副本是一个分片的精确复制，每个分片可以有零个或多个副本。ES中可以有许多相同的分片，其中之一被选择更改索引操作，这种特殊的分片称为主分片。
当主分片丢失时，如：该分片所在的数据不可用时，集群将副本提升为新的主分片。
Elasticsearch 禁止同一个分片的主分片和副本分片在同一个节点上，所以如果是一个节点的集群是不能有副本的。

它在节点失败的情况下提供高可用性。由于这个原因，需要注意的是，副本分片永远不会分配到与主分片相同的节点上。

#水平扩展原理
单个节点的容量是有限的，如果后期两个节点的容量不能够支持三个分片，那么另外启动一个节点就可以了，ES 会自动的重新规划分片，如下图：可以看到 A3 节点已经被自动的分配到 Node3 节点里面了，另外副本 B1 从 Node2 移动到 Node3 节点，B3 分片从 Node1 节点被分配到 Node2 节点。这里想一下，如果再启动一个节点呢？是的，再启动一个节点将不会对主分片起到任何作用，因为主分片不可以修改，只有三个，但是副本可以修改，能够起到扩容的作用。


#
在一个写请求被发送到某个节点后，该节点即为协调节点，协调节点会根据路由公式计算出需要写到哪个分片上，再将请求转发到该分片的主分片节点上。假设 shard = hash(routing) % 4 = 0 ，则过程大致如下：
1.客户端向 ES1节点（协调节点）发送写请求，通过路由计算公式得到值为0，则当前数据应被写到主分片 S0 上。
2.ES1 节点将请求转发到 S0 主分片所在的节点 ES3，ES3 接受请求并写入到磁盘。
3.并发将数据复制到两个副本分片 R0 上，其中通过乐观并发控制数据的冲突。一旦所有的副本分片都报告成功，则节点 ES3 将向协调节点报告成功，协调节点向客户端报告成功。

# 索引的不可变性

索引文件分段存储并且不可修改，那么新增、更新和删除如何处理呢？

新增，新增很好处理，由于数据是新的，所以只需要对当前文档新增一个段就可以了。
删除，由于不可修改，所以对于删除操作，不会把文档从旧的段中移除，而是通过新增一个 .del文件（每一个提交点都有一个 .del 文件），包含了段上已经被删除的文档。当一个文档被删除，它实际上只是在.del文件中被标记为删除，依然可以匹配查询，但是最终返回之前会被从结果中删除。
更新，不能修改旧的段来进行反映文档的更新，其实更新相当于是删除和新增这两个动作组成。会将旧的文档在 .del文件中标记删除，然后文档的新版本被索引到一个新的段中。可能两个版本的文档都会被一个查询匹配到，但被删除的那个旧版本文档在结果集返回前就会被移除。



