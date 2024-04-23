package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Double latitude;

    private Double longitude;

    private String address;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Enumerated(EnumType.STRING)
    private LockedStatus isLocked;

    @Column(name = "is_approve")
    private Boolean isApprove;

    @Column(name = "is_remove")
    private Boolean isRemove;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cars")
    private List<Comment> comment;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "car")
    private List<Contract> contracts;

    @OneToMany(mappedBy = "car")
    private List<Asset> assets;

    @OneToMany(mappedBy = "car")
    private List<Request> requests;

    @OneToMany(mappedBy = "car")
    private List<Maintenance> maintenances;

    @OneToMany(mappedBy = "car")
    private List<Rate> rates;

    @OneToMany(mappedBy = "car")
    private List<CarMedia> carMedia;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<BlogStore> stores;


    public Car(String title, String description, BigDecimal price, Double latitude, Double longitude, String address, String createdBy, String updatedBy, Location location, Category category, User user, CarStatus carStatus) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.location = location;
        this.category = category;
        this.color = color;
        this.user = user;
        this.status = carStatus;
        this.isLocked = LockedStatus.ENABLE;
        this.isApprove = Boolean.FALSE;
        this.isRemove = Boolean.FALSE;
    }
}
