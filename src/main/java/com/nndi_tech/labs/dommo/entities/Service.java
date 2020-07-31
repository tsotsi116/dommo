package com.nndi_tech.labs.dommo.entities;

import com.activepersistence.model.Base;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name="mobile_money_services")
public class Service extends Base<Integer> {
    @Id
    private Integer id;

    @Column(name="service_name")
    private String name;

    @Column(name="is_activated")
    private boolean activated;

    @Column(name="modified")
    private LocalDate modified;

    public Service() {
    }

    public Service(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<String> supportedServices() {
        return Arrays.asList(
            "Test Service", "TNM Mpamba",
            "Airtel Money", "NBM Mo626",
            "NBS 322", "FDH 522");
    }

    public static Service findSupportedByName(String serviceNameID) {
        return switch(serviceNameID) {
            case "Test Service" ->  new Service(0, "Test Service");
            case "TNM Mpamba" -> new Service(1, "TNM Mpamba");
            case "Airtel Money" -> new Service(2, "Airtel Money");
            case "NBM Mo626" -> new Service(3, "NBM Mo626");
            case "NBS 322" -> new Service(4, "NBS 322");
            case "FDH 522" -> new Service(5, "FDH 522");
            default -> throw new IllegalArgumentException("Invalid service");
        };
    }
}
