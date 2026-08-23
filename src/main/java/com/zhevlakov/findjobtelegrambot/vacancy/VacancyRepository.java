package com.zhevlakov.findjobtelegrambot.vacancy;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
//    @Query("select v from Vacancy v where (v.title, v.company) in :keys")
//    List<Vacancy> searchAllByFilter(
//            @Param("keys") List<Pair<String, String>> keys,
//            Pageable pager);
}
