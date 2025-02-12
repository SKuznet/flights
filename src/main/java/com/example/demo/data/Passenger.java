package com.example.demo.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "passenger")
@Data
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;
    private String seat;

    @OneToMany(mappedBy = "passenger")
    private List<Order> flightOrders = new ArrayList<>();
}
