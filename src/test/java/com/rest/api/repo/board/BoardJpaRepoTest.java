package com.rest.api.repo.board;

import com.rest.api.entity.board.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// in-memory DB로 테스트
// @DataJpaTest
@SpringBootTest
@ActiveProfiles("local")
@Transactional
class BoardJpaRepoTest {
    @Autowired
    private BoardJpaRepo boardJpaRepo;

    //@Rollback(value = false)
    @Test
    void findByName() {
        // given
        String name = "test2";
        boardJpaRepo.save(Board.builder().name(name).build());

        // when
        Board result = boardJpaRepo.findByName(name);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }
}