package com.learning.jhipster.service.impl;

import com.learning.jhipster.service.UserService;
import com.learning.jhipster.domain.User;
import com.learning.jhipster.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save a user.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    @Override
    public User save(User user) {
        log.debug("Request to save User : {}", user);
        return userRepository.save(user);
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return userRepository.findAll(pageable);
    }


    /**
     * Get one user by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<User> findOne(String id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id);
    }

    @Override
    public User update(User user) {
        log.debug("Request to update User : {}", user);
        Optional<User> oldEntry =  findOne(user.getId());
        if(oldEntry.isPresent()){
            if(null == user.getName())
                user.setName(oldEntry.get().getName());
            user.setUserPost(oldEntry.get().getUserPost());
        }
        return userRepository.save(user);
    }

    /**
     * Delete the user by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}
