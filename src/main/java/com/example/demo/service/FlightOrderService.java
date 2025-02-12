package com.example.demo.service;

import com.example.demo.data.Flight;
import com.example.demo.data.Offer;
import com.example.demo.data.Order;
import com.example.demo.data.Passenger;
import com.example.demo.exception.FlightNotFoundException;
import com.example.demo.exception.PassengerNotFoundException;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.OfferRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FlightOrderService {
    private final FlightRepository flightRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public Order createOrder(UUID flightId, UUID passengerId, List<UUID> offerIds) {
        Flight flight = flightRepository.findById(flightId).orElseThrow(
                () -> new FlightNotFoundException(String.format("Flight not found with id: %s", flightId)));
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(
                () -> new PassengerNotFoundException(String.format("Passenger not found with id: %s", passengerId)));

        Order order = new Order();
        order.setFlight(flight);
        order.setPassenger(passenger);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (UUID offerId : offerIds) {
            Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new RuntimeException("No such offer"));
            order.getOffers().add(offer);
            if (offer.getPrice() != null) {
                totalPrice = totalPrice.add(offer.getPrice());
            }
        }

        order.setFinalPrice(totalPrice);
        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order addOfferToOrder(UUID orderId, UUID offerId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("No such order"));
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new RuntimeException("No such offer"));

        order.getOffers().add(offer);

        if(offer.getPrice() != null) {
            BigDecimal updatedPrice = order.getFinalPrice().add(offer.getPrice());
            order.setFinalPrice(updatedPrice);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
