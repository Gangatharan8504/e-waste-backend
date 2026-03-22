package com.Internship.project.repository;

import com.Internship.project.entity.EWasteRequest;
import com.Internship.project.entity.RequestStatus;
import com.Internship.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EWasteRequestRepository extends JpaRepository<EWasteRequest, Long> {

    // Existing
    List<EWasteRequest> findByUser(User user);

    // 🔥 NEW FOR ADMIN FILTERING

    List<EWasteRequest> findByStatus(RequestStatus status);

    List<EWasteRequest> findByAssignedStaff(String assignedStaff);

    List<EWasteRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<EWasteRequest> findAllByOrderByCreatedAtDesc();
}