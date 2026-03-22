package com.Internship.project.controller;

import com.Internship.project.dto.ApiResponse;
import com.Internship.project.entity.EWasteRequest;
import com.Internship.project.service.EWasteRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class EWasteRequestController {

    private final EWasteRequestService requestService;

    public EWasteRequestController(EWasteRequestService requestService) {
        this.requestService = requestService;
    }

    // ========================================
    // CREATE REQUEST
    // ========================================
    @PostMapping
    public ResponseEntity<ApiResponse> createRequest(
            @RequestParam String deviceType,
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam String condition,
            @RequestParam int quantity,
            @RequestParam String pickupAddress,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) List<MultipartFile> images
    ) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ApiResponse response = requestService.createRequest(
                email,
                deviceType,
                brand,
                model,
                condition,
                quantity,
                pickupAddress,
                latitude,
                longitude,
                remarks,
                images
        );

        return ResponseEntity.ok(response);
    }

    // ========================================
    // GET MY REQUESTS
    // ========================================
    @GetMapping("/my")
    public ResponseEntity<List<EWasteRequest>> getMyRequests() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        List<EWasteRequest> requests = requestService.getUserRequests(email);

        return ResponseEntity.ok(requests);
    }

    // ========================================
    // GET REQUEST BY ID
    // ========================================
    @GetMapping("/{id}")
    public ResponseEntity<EWasteRequest> getRequestById(@PathVariable Long id) {

        EWasteRequest request = requestService.getRequestById(id);

        return ResponseEntity.ok(request);
    }

    // ========================================
    // USER CONFIRMS SLOT
    // ========================================
    @PutMapping("/{id}/confirm-slot")
    public ResponseEntity<ApiResponse> confirmSlot(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ApiResponse response = requestService.confirmSlot(id, email);

        return ResponseEntity.ok(response);
    }

    // ========================================
    // USER REQUEST RESCHEDULE
    // ========================================
    @PutMapping("/{id}/request-reschedule")
    public ResponseEntity<ApiResponse> requestReschedule(
            @PathVariable Long id,
            @RequestParam String requestedDate,
            @RequestParam String requestedSlot
    ) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ApiResponse response = requestService.requestReschedule(
                id,
                email,
                requestedDate,
                requestedSlot
        );

        return ResponseEntity.ok(response);
    }

    // ========================================
    // CANCEL REQUEST
    // ========================================
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelRequest(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ApiResponse response = requestService.cancelRequest(id, email);

        return ResponseEntity.ok(response);
    }

    // ========================================
    // DELETE REQUEST
    // ========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        requestService.deleteRequest(id, email);

        return ResponseEntity.noContent().build();
    }
}