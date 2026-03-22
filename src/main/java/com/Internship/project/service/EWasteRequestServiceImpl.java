package com.Internship.project.service;

import com.Internship.project.dto.ApiResponse;
import com.Internship.project.entity.*;
import com.Internship.project.repository.EWasteRequestRepository;
import com.Internship.project.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EWasteRequestServiceImpl implements EWasteRequestService {

    private final EWasteRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public EWasteRequestServiceImpl(
            EWasteRequestRepository requestRepository,
            UserRepository userRepository
    ) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // ========================================
    // CREATE REQUEST
    // ========================================
    @Override
    public ApiResponse createRequest(
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
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EWasteRequest request = new EWasteRequest();
        request.setDeviceType(deviceType);
        request.setBrand(brand);
        request.setModel(model);
        request.setCondition(condition);
        request.setQuantity(quantity);
        request.setPickupAddress(pickupAddress);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setRemarks(remarks);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);

        requestRepository.save(request);

        if (images != null && !images.isEmpty()) {

            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            List<RequestImage> imageList = new ArrayList<>();

            for (MultipartFile image : images) {

                if (!image.isEmpty()) {

                    try {

                        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                        File destination = new File(uploadDir + File.separator + fileName);
                        image.transferTo(destination);

                        RequestImage requestImage = new RequestImage();
                        requestImage.setImageName(fileName);
                        requestImage.setRequest(request);

                        imageList.add(requestImage);

                    } catch (IOException e) {
                        throw new RuntimeException("Image upload failed");
                    }

                }

            }

            request.setImages(imageList);
        }

        return new ApiResponse(true, "E-Waste pickup request created successfully");
    }

    // ========================================
    // USER CONFIRMS SLOT
    // ========================================
    @Override
    public ApiResponse confirmSlot(Long id, String email) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getUser().getEmail().equals(email)) {
            return new ApiResponse(false, "Unauthorized");
        }

        if (request.getStatus() != RequestStatus.SLOT_PROPOSED) {
            return new ApiResponse(false, "No slot available to confirm");
        }

        request.setStatus(RequestStatus.SLOT_CONFIRMED);

        requestRepository.save(request);

        return new ApiResponse(true, "Pickup slot confirmed");
    }

    // ========================================
    // USER REQUEST RESCHEDULE
    // ========================================
    @Override
    public ApiResponse requestReschedule(Long id, String email, String date, String slot) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getUser().getEmail().equals(email)) {
            return new ApiResponse(false, "Unauthorized");
        }

        request.setRequestedDate(date);
        request.setRequestedSlot(slot);

        request.setStatus(RequestStatus.RESCHEDULE_REQUESTED);

        requestRepository.save(request);

        return new ApiResponse(true, "Reschedule request sent to admin");
    }

    // ========================================
    // GET USER REQUESTS
    // ========================================
    @Override
    public List<EWasteRequest> getUserRequests(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return requestRepository.findByUser(user);
    }

    // ========================================
    // GET REQUEST BY ID
    // ========================================
    @Override
    public EWasteRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    // ========================================
    // CANCEL REQUEST
    // ========================================
    @Override
    public ApiResponse cancelRequest(Long id, String email) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getUser().getEmail().equals(email)) {
            return new ApiResponse(false, "Unauthorized");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            return new ApiResponse(false, "Only pending requests can be cancelled");
        }

        request.setStatus(RequestStatus.CANCELLED);
        requestRepository.save(request);

        return new ApiResponse(true, "Request cancelled successfully");
    }

    // ========================================
    // DELETE REQUEST
    // ========================================
    @Override
    public void deleteRequest(Long id, String email) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        if (request.getStatus() != RequestStatus.CANCELLED) {
            throw new RuntimeException("Only cancelled requests can be deleted");
        }

        requestRepository.delete(request);
    }
}