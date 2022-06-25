package com.example.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

@Getter
@Component
@RequiredArgsConstructor
public class InTransaction {

    private final EntityManager entityManager;

    @Transactional
    public void run(Consumer<EntityManager> consumer) {
        consumer.accept(entityManager);
    }

}
