package com.learning.jhipster.web.rest;

import com.learning.jhipster.RedisTestContainerExtension;
import com.learning.jhipster.DemoApp;
import com.learning.jhipster.domain.User;
import com.learning.jhipster.domain.Post;
import com.learning.jhipster.repository.UserRepository;
import com.learning.jhipster.service.UserService;
import com.learning.jhipster.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static com.learning.jhipster.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@SpringBootTest(classes = DemoApp.class)
@ExtendWith(RedisTestContainerExtension.class)
public class UserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restUserMockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserResource userResource = new UserResource(userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity() {
        User user = new User()
            .name(DEFAULT_NAME);
        // Add required entity
        Post post;
        post = PostResourceIT.createEntity();
        post.setId("fixed-id-for-tests");
        user.setUserPost(post);
        return user;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity() {
        User user = new User()
            .name(UPDATED_NAME);
        // Add required entity
        Post post;
        post = PostResourceIT.createUpdatedEntity();
        post.setId("fixed-id-for-tests");
        user.setUserPost(post);
        return user;
    }

    @BeforeEach
    public void initTest() {
        userRepository.deleteAll();
        user = createEntity();
    }

    @Test
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        restUserMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User with an existing ID
        user.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setName(null);

        // Create the User, which fails.

        restUserMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.save(user);

        // Get all the userList
        restUserMockMvc.perform(get("/api/users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    public void getUser() throws Exception {
        // Initialize the database
        userRepository.save(user);

        // Get the user
        restUserMockMvc.perform(get("/api/users/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    public void getNonExistingUser() throws Exception {
        // Get the user
        restUserMockMvc.perform(get("/api/users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateUser() throws Exception {
        // Initialize the database
        userService.save(user);

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();
        updatedUser
            .name(UPDATED_NAME);

        restUserMockMvc.perform(put("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUser)))
            .andExpect(status().isOk());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Create the User

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc.perform(put("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteUser() throws Exception {
        // Initialize the database
        userService.save(user);

        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc.perform(delete("/api/users/{id}", user.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
