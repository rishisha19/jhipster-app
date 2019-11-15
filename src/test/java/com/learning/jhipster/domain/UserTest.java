package com.learning.jhipster.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.learning.jhipster.web.rest.TestUtil;

public class UserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId("id1");
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId("id2");
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }
}
