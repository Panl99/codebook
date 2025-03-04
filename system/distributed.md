
# 目录

- [CAP理论](#CAP理论)
- [BASE理论](#BASE理论)

------

# CAP理论
一个分布式系统不可能同时满足：Consistency（一致性）、Availability（可用性）、Partition Tolerance（分区容错性）。

对于分布式系统来说，分区容错性必须被保证，因为发生网络分区的时候，系统要继续提供服务。

所以，一致性、可用性只能2选1。

**CAP定义：**
- 一致性（Consistence）: 所有节点访问同一份最新的数据副本。
- 可用性（Availability）: 非故障的节点在合理的时间内返回合理的响应（不是错误或者超时的响应）。
- 分区容错性（Partition tolerance）: 分布式系统出现网络分区的时候，仍然能够对外提供服务。

什么是网络分区？
- 分布式系统中，多个节点之前的网络本来是连通的，但是因为某些故障（比如部分节点网络出了问题）某些节点之间不连通了，整个网络就分成了几块区域，这就叫网络分区。

**CAP应用：**
- 注册中心：
    - ZooKeeper 保证的是 CP。 任何时刻对 ZooKeeper 的读请求都能得到一致性的结果，但是， ZooKeeper 不保证每次请求的可用性比如在 Leader 选举过程中或者半数以上的机器不可用的时候服务就是不可用的。
    - etcd 保证的是 CP。
    - Eureka 保证的则是 AP。 Eureka 在设计的时候就是优先保证 A （可用性）。在 Eureka 中不存在什么 Leader 节点，每个节点都是一样的、平等的。因此 Eureka 不会像 ZooKeeper 那样出现选举过程中或者半数以上的机器不可用的时候服务就是不可用的情况。 Eureka 保证即使大部分节点挂掉也不会影响正常提供服务，只要有一个节点是可用的就行了。只不过这个节点上的数据可能并不是最新的。
    - Nacos 不仅支持 CP 也支持 AP。

[目录](#目录)

------

# BASE理论
BASE：Basically Available（基本可用）、Soft-state（软状态）、Eventually Consistent（最终一致性）三个短语的缩写。
BASE 理论是对 CAP 中一致性 C 和可用性 A 权衡的结果，其来源于对大规模互联网系统分布式实践的总结，是基于 CAP 定理逐步演化而来的，它大大降低了我们对系统的要求。

**BASE定义：**
- 基本可用
    - 指分布式系统在出现不可预知故障的时候，允许损失部分可用性。但这绝不等价于系统不可用。
    - 什么叫允许损失部分可用性呢？
        - 响应时间上的损失: 正常情况下，处理用户请求需要 0.5s 返回结果，但是由于系统出现故障，处理用户请求的时间变为 3s。
        - 系统功能上的损失：正常情况下，用户可以使用系统的全部功能，但是由于系统访问量突然剧增，系统的部分非核心功能无法使用。
- 软状态
    - 软状态指允许系统中的数据存在中间状态（CAP 理论中的数据不一致），并认为该中间状态的存在不会影响系统的整体可用性，即允许系统在不同节点的数据副本之间进行数据同步的过程存在延时。
- 最终一致性
    - 最终一致性强调的是系统中所有的数据副本，在经过一段时间的同步后，最终能够达到一个一致的状态。因此，最终一致性的本质是需要系统保证最终数据能够达到一致，而不需要实时保证系统数据的强一致性。

分布式一致性的 3 种级别：
- 强一致性 ：系统写入了什么，读出来的就是什么。
- 弱一致性 ：不一定可以读取到最新写入的值，也不保证多少时间之后读取到的数据是最新的，只是会尽量保证某个时刻达到数据一致的状态。
- 最终一致性 ：弱一致性的升级版，系统会保证在一定时间内达到数据一致的状态，

[目录](#目录)

------