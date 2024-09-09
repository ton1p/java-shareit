package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i " +
            "where i.available = true " +
            "and (i.name ilike ?1 or i.description ilike ?1)")
    List<Item> search(String query);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> findAllByRequestId(Long requestId);
}
