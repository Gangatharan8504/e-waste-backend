package com.Internship.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "request_images")
public class RequestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonBackReference
    private EWasteRequest request;

    public Long getId() { return id; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public EWasteRequest getRequest() { return request; }
    public void setRequest(EWasteRequest request) { this.request = request; }
}
