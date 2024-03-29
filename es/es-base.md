#文档
Elasticsearch 是面向文档，文档是所有搜索数据的最小单元。
文档就是类似数据库里面的一条长长的存储记录。文档（Document）是索引信息的基本单位。
文档被序列化成为 JSON 格式，物理保存在一个索引(索引是什么)中。JSON 是一种常见的互联网数据交换格式：
文档字段名：JSON 格式由 name/value pairs 组成，对应的 name 就是文档字段名
文档字段类型：每个字段都有对应的字段类型：String、integer、long 等，并支持数据&嵌套

每个文档都会有一个 Unique ID，其字段名称为 _id ：
自行设置指定 ID 或通过 Elasticsearch 自动生成
其值不会被索引
注意：该 _id 字段的值可以在某些查询 term, terms, match, query_string, simple_query_string 等中访问，但不能在 aggregations，scripts 或 sorting 中使用。如果需要对 _id 字段进行排序或汇总，建议新建一个文档字段复制 _id 字段的内容

#文档元数据
元数据是用于标注文档的相关信息，那么索引文档的元数据如下：
_index 文档所属索引名称
_type 文档所属类型名
_id 文档唯一 ID
_score 文档相关性打分
_source 文档 JSON 数据
_version 文档版本信息
其中 _type 文档所属类型名，需要关注版本不同之间区别：
7.0 之前，一个索引可以设置多个 types
7.0 开始，被 Deprecated 了。一个索引只能创建一个 type，值为 _doc

#索引
作为名词，索引代表是在 Elasticsearch 集群中，创建很多不同索引。
作为动词，索引代表保存一个文档到 Elasticsearch。就是在 Elasticsearch 创建一个倒排索引的意思。
索引，就是相似类型文档的集合。类似 Spring Bean 容器装载着很多 Bean ，ES 索引就是文档的容器，是一类文档的集合。
索引，是逻辑空间概念，每个索引有对那个的 Mapping 定义，对应的就是文档的字段名和字段类型。相比后面会讲到分片，是物理空间概念，索引中存储数据会分散到分片上。
mappings：定义文档字段的类型
settings：定义不同数据分布
aliases：定义索引的别名，可以通过别名访问该索引
实战经验总结：aliases 别名大有作为，比如 my_index 迁移到 my_index_new , 数据迁移后，只需要保持一致的别名配置。那么通过别名访问索引的业务方都不需要修改，直接迁移即可。

1.1 高可用

什么是高可用？CAP 定理是分布式系统的基础，也是分布式系统的 3 个指标：

Consistency（一致性）
Availability（可用性）
Partition tolerance（分区容错性）
那高可用（High Availability）是什么？高可用，简称 HA，是系统一种特征或者指标，通常是指，提供一定性能上的服务运行时间，高于平均正常时间段。反之，消除系统服务不可用的时间。

衡量系统是否满足高可用，就是当一台或者多台服务器宕机的时候，系统整体和服务依然正常可用。举个例子，一些知名的网站保证 4 个 9 以上的可用性，也就是可用性超过 99.99%。那 0.01% 就是所谓故障时间的百分比。

Elasticsearch 在高可用性上，体现如下两点：
服务可用性：允许部分节点停止服务，整体服务没有影响
数据可用性：允许部分节点丢失，最终不会丢失数据
1.2 可扩展

随着公司业务发展，Elasticsearch 也面临两个挑战：

搜索数据量从百万到亿量级
搜索请求 QPS 也猛增
那么需要将原来节点和增量数据重新从 10 个节点分布到 100 个节点。Elasticsearch 可以横向扩展至数百（甚至数千）的服务器节点，同时可以处理PB级数据。Elasticsearch 为了可扩展性而生，由小规模集群增长为大规模集群的过程几乎完全自动化，这是水平扩展的体现。