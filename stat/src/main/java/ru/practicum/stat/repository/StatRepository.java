package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.entity.EndpointHit;

import java.time.LocalDateTime;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT  COUNT (DISTINCT hit.ip)  FROM EndpointHit hit" +
            " WHERE hit.uri = :uri AND hit.timestamp > :start AND hit.timestamp < :end ")
    Integer getHitCountUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("SELECT COUNT (hit.id) FROM EndpointHit hit " +
            "WHERE hit.uri = :uris AND hit.timestamp > :start AND hit.timestamp < :end")
    Integer getHitCountAll(LocalDateTime start, LocalDateTime end, String uris);

    @Query("SELECT distinct hit.app from EndpointHit hit where hit.uri =:uri")
    String findAppByUri(String uri);
}
