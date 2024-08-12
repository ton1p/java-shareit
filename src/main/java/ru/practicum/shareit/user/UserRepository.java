package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(final Long id);

    Optional<User> findByEmail(final String email);

    User create(final User user);

    User update(final Long id, final User user);

    void delete(final Long id);
}
