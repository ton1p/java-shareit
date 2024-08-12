package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();

    private long id;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        if (userMap.containsKey(id)) {
            return Optional.of(userMap.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = new ArrayList<>(userMap.values());
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        userMap.put(id, user);
        return user;
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }
}
