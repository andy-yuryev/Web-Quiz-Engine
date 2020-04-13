package engine.repository;

import engine.domain.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, Long> {

    @Query(value = "SELECT c FROM Completion c WHERE c.user.username = :username")
    Page<Completion> findAllByUsername(@Param("username") String username, Pageable pageable);
}
