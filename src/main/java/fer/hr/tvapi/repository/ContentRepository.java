package fer.hr.tvapi.repository;

import fer.hr.tvapi.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Modifying
    @Transactional
    @Query(value = "Delete from content c WHERE c.id_category = :categoryId", nativeQuery = true)
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

}
