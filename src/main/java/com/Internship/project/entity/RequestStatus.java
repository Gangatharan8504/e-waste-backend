package com.Internship.project.entity;

public enum RequestStatus {

    // User submitted the request
    PENDING,

    // Admin reviewed and approved
    APPROVED,

    // Admin proposed pickup slot
    SLOT_PROPOSED,

    // User confirmed the proposed slot
    SLOT_CONFIRMED,

    // User requested a new pickup time
    RESCHEDULE_REQUESTED,

    // Staff has been assigned and pickup is finalized
    ASSIGNED,

    // Pickup has started
    IN_PROGRESS,

    // Pickup completed successfully
    COMPLETED,

    // Admin rejected the request
    REJECTED,

    // User cancelled before processing
    CANCELLED
}