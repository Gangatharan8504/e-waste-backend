package com.Internship.project.service;

import com.Internship.project.dto.ApiResponse;
import com.Internship.project.entity.EWasteRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EWasteRequestService {

    ApiResponse createRequest(
            String email,
            String deviceType,
            String brand,
            String model,
            String condition,
            int quantity,
            String pickupAddress,
            Double latitude,
            Double longitude,
            String remarks,
            List<MultipartFile> images
    );

    List<EWasteRequest> getUserRequests(String email);

    EWasteRequest getRequestById(Long id);

    ApiResponse cancelRequest(Long id, String email);

    void deleteRequest(Long id, String email);

    // NEW METHODS

    ApiResponse confirmSlot(Long id, String email);

    ApiResponse requestReschedule(Long id, String email, String requestedDate, String requestedSlot);

}