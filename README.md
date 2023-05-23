# distributed-db

## 环境

1. jdk java20, IDE IntelliJ IDEA

2. 使用MAVEN管理项目依赖，添加package修改pom.xml

3. zookeeper集群搭建，参考[(97 条消息) ZooKeeper 伪集群搭建_client ssl:false_空槐树的博客 - CSDN 博客](https://blog.csdn.net/tmr1016/article/details/113973560)，教材9_11.分布式共识系统实践 P67-78

4. MySQL使用：预先在本地开一个mysql，方便region节点连接

5. ...

## 项目文件

1. common: 公共定义的属性，常量，类

2. MySQL: 对JDBC的一些封装

3. RPC: 对Thrift的一些封装

4. Master: master服务器的运行逻辑，包含两个线程zookeeper, RPC

5. Region: region服务器的运行逻辑，包含两个线程zookeeper, RPC

6. Client：

## 测试

1. 测试使用zookeeper伪集群

   
## 运行
以三台主机为例：

1. 三台主机各开启一个zookeeper客户端，搭建zookeeper集群
2. 一台主机运行MasterRunner类下的main函数，启动master节点
3. 三台主机运行各运行RegionRunner类下的main函数，启动region节点
4. 一台主机启动Client客户端，在终端处理输入/输出









