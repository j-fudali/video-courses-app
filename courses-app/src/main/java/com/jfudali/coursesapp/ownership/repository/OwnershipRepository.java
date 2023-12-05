package com.jfudali.coursesapp.ownership.repository;

import com.jfudali.coursesapp.ownership.model.Ownership;
import com.jfudali.coursesapp.ownership.model.OwnershipKey;
import com.jfudali.coursesapp.user.dto.OwnedCoursesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnershipRepository extends JpaRepository<Ownership, OwnershipKey> {
    @Query("SELECT new com.jfudali.coursesapp.user.dto.OwnedCoursesDto(o.course.idcourse ,o.course.name, o.course.category, o.isCompleted) FROM Ownership o WHERE o.user.email = ?1")
    Page<OwnedCoursesDto> findCoursesOwnedByUser(String email, Pageable pageable);
}
