package com.wallet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.wallet.model.Transaction;
import com.wallet.model.User;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.List;
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

  public Transaction setTransaction(String token, Transaction transaction) throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonTransaction = objectMapper.writeValueAsString(transaction);

    try (Jedis jedis = jedisPool.getResource()) {
      String key = StringUtils.join("transactions#", token);
      jedis.lpush(key, jsonTransaction);
    }

    return transaction;
  }

  public List<String> getTransactions(String token) {

    List<String> transactions;

    try (Jedis jedis = jedisPool.getResource()) {
      String key = StringUtils.join("transactions#", token);
      transactions = jedis.lrange(key, 0, -1);
    }

    return transactions;
  }
}
