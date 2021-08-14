# 常用命令

1. 查看日志：`sudo docker logs -f -t --since="2017-05-31" --tail=100 CONTAINER_ID`

   --since : 此参数指定了输出日志开始日期，即只输出指定日期之后的日志。
   -f : 查看实时日志
   -t : 查看日志产生的日期
   -tail=100 : 查看最后的100条日志。
   CONTAINER_ID: 容器名称

- 查看最近30分钟的日志：$ docker logs --since 30m CONTAINER_ID

- 查看某时间之后的日志：$ docker logs -t --since="2018-02-08T13:23:37" CONTAINER_ID

- 查看某时间段日志：$ docker logs -t --since="2018-02-08T13:23:37" --until "2018-02-09T12:23:37" CONTAINER_ID

2. nginx重加载

   docker exec -i [nginx容器名/id] nginx -s reload

