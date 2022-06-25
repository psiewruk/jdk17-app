package com.example.demo;

import com.example.demo.util.InTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected InTransaction inTransaction;

    @BeforeEach
    void tearDown() {
        inTransaction.run(this::cleanUpH2Db);
    }

    private void cleanUpH2Db(EntityManager em) {
        List<String> tables = ((List<Object[]>) em.createNativeQuery("SHOW tables").getResultList()).stream()
                .map(el -> el[0].toString())
                .collect(Collectors.toList());

        for (String table : tables) {
            em.createNativeQuery(String.format("ALTER TABLE \"%s\" SET REFERENTIAL_INTEGRITY FALSE", table)).executeUpdate();
        }

        for (String table : tables) {
            em.createNativeQuery(String.format("DELETE FROM \"%s\"", table)).executeUpdate();
        }

        for (String table : tables) {
            em.createNativeQuery(String.format("ALTER TABLE \"%s\" SET REFERENTIAL_INTEGRITY TRUE", table)).executeUpdate();
        }
    }
}
