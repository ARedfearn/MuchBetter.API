package com.wallet.repository;

import com.google.inject.Singleton;

import com.wallet.model.Transaction;
import com.wallet.model.User;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;

@Singleton
public class WalletRepository {

  final JedisPoolConfig poolConfig = buildPoolConfig();
  JedisPool jedisPool = new JedisPool(poolConfig, "localhost");

  private JedisPoolConfig buildPoolConfig() {
    final JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(128);
    poolConfig.setMaxIdle(128);
    poolConfig.setMinIdle(16);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
    poolConfig.setNumTestsPerEvictionRun(3);
    poolConfig.setBlockWhenExhausted(true);
    return poolConfig;
  }

  public User setUser(User user) {

    try (Jedis jedis = jedisPool.getResource()) {
      String key = StringUtils.join("user#", user.getToken());
      jedis.hset(key, "balance", Integer.toString(user.getBalance()));
      jedis.hset(key, "currency", user.getCurrency());
    }

    return user;
  }

  public User getUser(String token) {

    Map<String, String> map;

    try (Jedis jedis = jedisPool.getResource()) {
      map = jedis.hgetAll(StringUtils.join("user#", token));
    }

    if (!map.isEmpty()) {
      return new User(token)
        .setCurrency(map.get("currency"))
        .setBalance(Integer.valueOf(map.get("balance")));
    } else return null;
  }

  public Transaction setTransaction(String token, Transaction transaction) {

    try (Jedis jedis = jedisPool.getResource()) {
      String key = StringUtils.join("transaction#", token);
      jedis.hset(key, "date", transaction.getDate().toString());
      jedis.hset(key, "description", transaction.getDescription());
      jedis.hset(key, "amount", transaction.getAmount().toString());
      jedis.hset(key, "currency", transaction.getCurrency());
    }

    return transaction;
  }
}
