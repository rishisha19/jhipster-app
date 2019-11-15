package com.learning.jhipster.service;

import com.learning.jhipster.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {

    /**
     * Save a user.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    User save(User user);

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<User> findAll(Pageable pageable);


    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<User> findOne(String id);

    /**
     * Update "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    User update(User user);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
