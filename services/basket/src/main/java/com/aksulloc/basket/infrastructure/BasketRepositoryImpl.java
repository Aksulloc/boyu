package com.aksulloc.basket.infrastructure;

import com.aksulloc.basket.model.BasketRepository;
import com.aksulloc.basket.model.CustomerBasket;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class BasketRepositoryImpl implements BasketRepository {
    private final HashOperations<String, String, CustomerBasket> hashOperations;

    public BasketRepositoryImpl(RedisTemplate<String, CustomerBasket> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Optional<CustomerBasket> getBasket(String customerId) {
        return Optional.ofNullable(hashOperations.get("BASKET", customerId));
    }

    @Override
    public Set<String> getUsers() {
        return hashOperations.keys("BASKET");
    }

    @Override
    public CustomerBasket updateBasket(CustomerBasket basket) {
        hashOperations.put("USER", basket.getBuyerId(), basket);
        return basket;
    }

    @Override
    public boolean deleteBasket(String id) {
        hashOperations.delete("USER", id);
        return true;
    }
}
