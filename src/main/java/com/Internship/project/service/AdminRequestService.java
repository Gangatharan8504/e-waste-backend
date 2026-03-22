package com.Internship.project.service;

import com.Internship.project.dto.ApiResponse;
import com.Internship.project.dto.AdminRequestDetailsDTO;
import com.Internship.project.entity.EWasteRequest;

import java.util.List;

public interface AdminRequestService {

    // ========================================
    // VIEW ALL REQUESTS (FOR DASHBOARD)
    // ========================================
    List<EWasteRequest> getAllRequests();

    // ========================================
    // VIEW SINGLE REQUEST DETAILS (DTO)
    // ========================================
    AdminRequestDetailsDTO getRequestDetails(Long id);

    // ========================================
    // APPROVE / REJECT
    // ========================================
    ApiResponse approveRequest(Long id);

    ApiResponse rejectRequest(Long id, String reason);

    // ========================================
    // SLOT MANAGEMENT
    // ========================================
    ApiResponse proposePickupSlot(Long id, String pickupDate, String pickupSlot);

    ApiResponse approveReschedule(Long id);

    ApiResponse rejectReschedule(Long id);

    // ========================================
    // STAFF ASSIGNMENT
    // ========================================
    ApiResponse assignStaff(Long id, String staffName);

    // ========================================
    // PICKUP PROCESS
    // ========================================
    ApiResponse markInProgress(Long id);

    ApiResponse completeRequest(Long id);
}