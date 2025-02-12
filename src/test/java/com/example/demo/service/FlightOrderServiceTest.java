package com.example.demo.service;

import com.example.demo.data.Flight;
import com.example.demo.data.Offer;
import com.example.demo.data.Order;
import com.example.demo.data.Passenger;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.OfferRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PassengerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightOrderServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private FlightOrderService flightOrderService;

    private Flight flight;
    private Passenger passenger;
    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    void setup() {
        flight = new Flight();
        flight.setId(UUID.fromString("flightuuid"));
        flight.setFlightNumber("ABC-10");

        passenger = new Passenger();
        passenger.setId(UUID.fromString("passengerid"));
        passenger.setName("Barsik");
        passenger.setSeat("13A");

        offer1 = new Offer();
        offer1.setId(UUID.fromString("offer1UUID"));
        offer1.setName("Champagne");
        offer1.setPrice(BigDecimal.valueOf(100L));

        offer2 = new Offer();
        offer2.setId(UUID.fromString("offer2UUID"));
        offer2.setName("Bread");
        offer2.setPrice(BigDecimal.valueOf(100L));
    }

    @Test
    void createOrder_Success() {
        when(flightRepository.findById(flight.getId())).thenReturn(Optional.of(flight));
        when(passengerRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        when(offerRepository.findById(offer1.getId())).thenReturn(Optional.of(offer1));
        when(offerRepository.findById(offer2.getId())).thenReturn(Optional.of(offer2));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(UUID.fromString("999"));
            return savedOrder;
        });

        Order order = flightOrderService.createOrder(flight.getId(), passenger.getId(),
                List.of(offer1.getId(), offer2.getId()));

        Assertions.assertNotNull(order);
    }
}
