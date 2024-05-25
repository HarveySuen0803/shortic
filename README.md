<div align="center">
  <p>
    <img src="https://harvey-image.oss-cn-hangzhou.aliyuncs.com/twitter.png" alt="logo" width="200" height="auto"/>
  </p>
  <h3>Harvey's Awesome Chat Server</h3>
</div>

# Quick Start

Preparing the MySQL environment.

1. Start Mysql container.

```shell
docker container run \
    --name shortic-mysql \
    --privileged \
    -p 3306:3306 \
    -v shortic-mysql-conf:/etc/mysql/conf.d \
    -v shortic-mysql-data:/var/lib/mysql \
    -v shortic-mysql-logs:/var/log/mysql \
    -e MYSQL_ROOT_PASSWORD=111 \
    -d mysql:8.1.0
```

Preparing the Redis environment.

1. Download the Redis configuration file.

```shell
curl -LJO http://download.redis.io/redis-stable/redis.conf
mv ./redis.conf /opt/redis/conf
```

2Modify the Redis configuration file

```
# run as a daemon in background
daemonize no

# disable protected mode
protected-mode no

# disable bind to allow remote connection
# bind 127.0.0.1 -::1

# SET password
requirepass 111

# persistent storage
appendonly yes
```

3. Start Redis container.

```shell
docker volume create shortic-redis-conf
docker volume create shortic-redis-data
docker volume create shortic-redis-logs

sudo mkdir -p /opt/redis

ln -s /var/lib/docker/volumes/shortic-redis-conf/_data /opt/redis/conf
ln -s /var/lib/docker/volumes/shortic-redis-data/_data /opt/redis/data
ln -s /var/lib/docker/volumes/shortic-redis-logs/_data /opt/redis/logs

docker container run \
    --name shortic-redis \
    --privileged \
    -p 6379:6379 \
    -p 16379:16379 \
    -v shortic-redis-conf:/etc/redis \
    -v shortic-redis-data:/data \
    -v shortic-redis-logs:/var/log/redis.log \
    -d redis:7.2 redis-server /etc/redis/redis.conf
```