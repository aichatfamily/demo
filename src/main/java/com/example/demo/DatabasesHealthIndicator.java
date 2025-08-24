package com.example.demo;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.actuate.data.redis.RedisHealthIndicator;
import org.springframework.stereotype.Component;

@Component("databases")
public class DatabasesHealthIndicator implements HealthIndicator {

    private final DataSourceHealthIndicator dataSourceHealthIndicator;
    private final RedisHealthIndicator redisHealthIndicator;

    public DatabasesHealthIndicator(DataSourceHealthIndicator dataSourceHealthIndicator, 
                                  RedisHealthIndicator redisHealthIndicator) {
        this.dataSourceHealthIndicator = dataSourceHealthIndicator;
        this.redisHealthIndicator = redisHealthIndicator;
    }

    @Override
    public Health health() {
        Health redisHealth = redisHealthIndicator.health();
        Health mariaDbSourceHealth = dataSourceHealthIndicator.health();

        if (redisHealth.getStatus() == Status.UP || mariaDbSourceHealth.getStatus() == Status.UP) {
            return Health.up()
                    .withDetail("redis", redisHealth)
                    .withDetail("mariadb", mariaDbSourceHealth)
                    .build();
        }

        return Health.down()
                .withDetail("redis", redisHealth)
                .withDetail("database", mariaDbSourceHealth)
                .build();
    }

}