package com.Internship.project.controller;

import com.Internship.project.dto.ApiResponse;
import com.Internship.project.dto.AdminRequestDetailsDTO;
import com.Internship.project.entity.EWasteRequest;
import com.Internship.project.service.AdminRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminRequestService adminService;

    public AdminController(AdminRequestService adminService) {
        this.adminService = adminService;
    }

    // ========================================
    // DASHBOARD TEST ENDPOINT
    // ========================================
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome ADMIN 👑";
    }

    // ========================================
    // VIEW ALL REQUESTS
    // ========================================
    @GetMapping("/requests")
    public ResponseEntity<List<EWasteRequest>> getAllRequests() {
        return ResponseEntity.ok(adminService.getAllRequests());
    }

    // ========================================
    // VIEW SINGLE REQUEST (DTO)
    // ========================================
    @GetMapping("/requests/{id}")
    public ResponseEntity<AdminRequestDetailsDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getRequestDetails(id));
    }

    // ========================================
    // APPROVE REQUEST
    // ========================================
    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<ApiResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveRequest(id));
    }

    // ========================================
    // REJECT REQUEST
    // ========================================
    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<ApiResponse> reject(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        return ResponseEntity.ok(adminService.rejectRequest(id, reason));
    }

    // ========================================
    // ADMIN PROPOSES PICKUP SLOT
    // ========================================
    @PutMapping("/requests/{id}/propose-slot")
    public ResponseEntity<ApiResponse> proposeSlot(
            @PathVariable Long id,
            @RequestParam String pickupDate,
            @RequestParam String pickupSlot
    ) {
        return ResponseEntity.ok(adminService.proposePickupSlot(id, pickupDate, pickupSlot));
    }

    // ========================================
    // APPROVE USER RESCHEDULE REQUEST
    // ========================================
    @PutMapping("/requests/{id}/approve-reschedule")
    public ResponseEntity<ApiResponse> approveReschedule(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveReschedule(id));
    }

    // ========================================
    // REJECT USER RESCHEDULE REQUEST
    // ========================================
    @PutMapping("/requests/{id}/reject-reschedule")
    public ResponseEntity<ApiResponse> rejectReschedule(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.rejectReschedule(id));
    }

    // ========================================
    // ASSIGN STAFF AFTER USER CONFIRMATION
    // ========================================
    @PutMapping("/requests/{id}/assign")
    public ResponseEntity<ApiResponse> assign(
            @PathVariable Long id,
            @RequestParam String staffName
    ) {
        return ResponseEntity.ok(adminService.assignStaff(id, staffName));
    }

    // ========================================
    // MARK IN PROGRESS
    // ========================================
    @PutMapping("/requests/{id}/start")
    public ResponseEntity<ApiResponse> startPickup(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.markInProgress(id));
    }

    // ========================================
    // COMPLETE REQUEST
    // ========================================
    @PutMapping("/requests/{id}/complete")
    public ResponseEntity<ApiResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.completeRequest(id));
    }
}