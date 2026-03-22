package com.Internship.project.service;

import com.Internship.project.dto.AdminRequestDetailsDTO;
import com.Internship.project.dto.ApiResponse;
import com.Internship.project.entity.EWasteRequest;
import com.Internship.project.entity.RequestImage;
import com.Internship.project.entity.RequestStatus;
import com.Internship.project.repository.EWasteRequestRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminRequestServiceImpl implements AdminRequestService {

    private final EWasteRequestRepository requestRepository;
    private final EmailService emailService;

    public AdminRequestServiceImpl(EWasteRequestRepository requestRepository,
                                   EmailService emailService) {
        this.requestRepository = requestRepository;
        this.emailService = emailService;
    }

    // ========================================
    // VIEW ALL REQUESTS
    // ========================================
    @Override
    public List<EWasteRequest> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc();
    }

    // ========================================
    // VIEW SINGLE REQUEST (DTO)
    // ========================================
    @Override
    public AdminRequestDetailsDTO getRequestDetails(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        AdminRequestDetailsDTO dto = new AdminRequestDetailsDTO();

        dto.setId(request.getId());
        dto.setDeviceType(request.getDeviceType());
        dto.setBrand(request.getBrand());
        dto.setModel(request.getModel());
        dto.setCondition(request.getCondition());
        dto.setQuantity(request.getQuantity());
        dto.setPickupAddress(request.getPickupAddress());
        dto.setRemarks(request.getRemarks());
        dto.setStatus(request.getStatus().name());

        dto.setCreatedAt(request.getCreatedAt());
        dto.setScheduledAt(request.getScheduledAt());
        dto.setCompletedAt(request.getCompletedAt());

        dto.setAssignedStaff(request.getAssignedStaff());
        dto.setRejectionReason(request.getRejectionReason());

        // ================================
        // CUSTOMER DETAILS
        // ================================
        if (request.getUser() != null) {

            String name = request.getUser().getFirstName() + " " +
                    request.getUser().getLastName();

            dto.setUserName(name);
            dto.setUserEmail(request.getUser().getEmail());
            dto.setUserPhone(request.getUser().getMobile());
        }

        // ================================
        // IMAGES
        // ================================
        if (request.getImages() != null) {

            List<String> images = request.getImages()
                    .stream()
                    .map(RequestImage::getImageName)
                    .collect(Collectors.toList());

            dto.setImages(images);
        }

        return dto;
    }

    // ========================================
    // APPROVE REQUEST
    // ========================================
    @Override
    public ApiResponse approveRequest(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            return new ApiResponse(false, "Only pending requests can be approved");
        }

        request.setStatus(RequestStatus.APPROVED);

        requestRepository.save(request);

        emailService.sendApprovalEmail(request.getUser().getEmail());

        return new ApiResponse(true, "Request approved successfully");
    }

    // ========================================
    // REJECT REQUEST
    // ========================================
    @Override
    public ApiResponse rejectRequest(Long id, String reason) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            return new ApiResponse(false, "Only pending requests can be rejected");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);

        requestRepository.save(request);

        emailService.sendRejectionEmail(
                request.getUser().getEmail(),
                reason
        );

        return new ApiResponse(true, "Request rejected successfully");
    }

    // ========================================
    // ADMIN PROPOSES SLOT
    // ========================================
    @Override
    public ApiResponse proposePickupSlot(Long id, String date, String slot) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.APPROVED) {
            return new ApiResponse(false, "Request must be approved first");
        }

        request.setPickupDate(date);
        request.setPickupSlot(slot);
        request.setStatus(RequestStatus.SLOT_PROPOSED);

        requestRepository.save(request);

        emailService.sendPickupProposalEmail(
                request.getUser().getEmail(),
                date,
                slot
        );

        return new ApiResponse(true, "Pickup slot proposed successfully");
    }

    // ========================================
    // APPROVE RESCHEDULE
    // ========================================
    @Override
    public ApiResponse approveReschedule(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setPickupDate(request.getRequestedDate());
        request.setPickupSlot(request.getRequestedSlot());

        request.setRequestedDate(null);
        request.setRequestedSlot(null);

        request.setStatus(RequestStatus.SLOT_PROPOSED);

        requestRepository.save(request);

        return new ApiResponse(true, "Reschedule approved");
    }

    // ========================================
    // REJECT RESCHEDULE
    // ========================================
    @Override
    public ApiResponse rejectReschedule(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setRequestedDate(null);
        request.setRequestedSlot(null);

        request.setStatus(RequestStatus.SLOT_CONFIRMED);

        requestRepository.save(request);

        return new ApiResponse(true, "Reschedule rejected");
    }

    // ========================================
    // ASSIGN STAFF
    // ========================================
    @Override
    public ApiResponse assignStaff(Long id, String staffName) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.SLOT_CONFIRMED) {
            return new ApiResponse(false, "User must confirm slot first");
        }

        request.setAssignedStaff(staffName);
        request.setStatus(RequestStatus.ASSIGNED);

        requestRepository.save(request);

        emailService.sendPickupScheduledEmail(
                request.getUser().getEmail(),
                request.getPickupDate() + " " + request.getPickupSlot()
        );

        return new ApiResponse(true, "Staff assigned successfully");
    }

    // ========================================
    // MARK IN PROGRESS
    // ========================================
    @Override
    public ApiResponse markInProgress(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.ASSIGNED) {
            return new ApiResponse(false, "Pickup must be assigned first");
        }

        request.setStatus(RequestStatus.IN_PROGRESS);

        requestRepository.save(request);

        return new ApiResponse(true, "Pickup marked as in progress");
    }

    // ========================================
    // COMPLETE REQUEST
    // ========================================
    @Override
    public ApiResponse completeRequest(Long id) {

        EWasteRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.IN_PROGRESS) {
            return new ApiResponse(false, "Pickup must be in progress to complete");
        }

        request.setStatus(RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());

        requestRepository.save(request);

        emailService.sendPickupCompletedEmail(
                request.getUser().getEmail()
        );

        return new ApiResponse(true, "Pickup completed successfully");
    }
}