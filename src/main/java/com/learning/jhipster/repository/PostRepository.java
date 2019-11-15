package com.learning.jhipster.repository;
import com.learning.jhipster.domain.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends MongoRepository<Post, String> {

}
