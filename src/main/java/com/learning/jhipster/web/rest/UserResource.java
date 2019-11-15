package com.learning.jhipster.web.rest;

import com.learning.jhipster.domain.User;
import com.learning.jhipster.service.UserService;
import com.learning.jhipster.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.learning.jhipster.domain.User}.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "demoUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code POST  /users} : Create a new user.
     *
     * @param user the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the user has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        if (user.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User result = userService.save(user);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /users} : Updates an existing user.
     *
     * @param user the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user,
     * or with status {@code 400 (Bad Request)} if the user is not valid,
     * or with status {@code 500 (Internal Server Error)} if the user couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to update User : {}", user);
        if (user.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User result = userService.update(user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, user.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /users} : get all the users.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get a page of Users");
        Page<User> page = userService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /users/:id} : get the "id" user.
     *
     * @param id the id of the user to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        log.debug("REST request to get User : {}", id);
        Optional<User> user = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(user);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.debug("REST request to delete User : {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
