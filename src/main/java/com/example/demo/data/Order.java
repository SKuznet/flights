package com.example.demo.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToMany
    @JoinTable(name = "flight_order_offers", joinColumns = @JoinColumn(name = "flight_order_id"),
    inverseJoinColumns = @JoinColumn(name = "offer_id"))
    private List<Offer> offers = new ArrayList<>();

    private LocalDateTime orderDate;
    private BigDecimal finalPrice;
}
