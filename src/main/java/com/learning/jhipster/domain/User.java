package com.learning.jhipster.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A User.
 */
@Document(collection = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 30)
    @Field("name")
    private String name;

    @DBRef
    @Field("userPost")
    private List<Post> userPost= new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getUserPost() {
        return userPost;
    }

    public void setUserPost(Post post) {
        this.userPost.add(post);
    }
    public void setUserPost(List<Post> post) {
        this.userPost.addAll(post);
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
