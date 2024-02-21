package com.aksulloc.basket.controller;

import com.aksulloc.basket.infrastructure.EventBus;
import com.aksulloc.basket.integrationevents.events.UserCheckoutAcceptedIntegrationEvent;
import com.aksulloc.basket.model.BasketCheckout;
import com.aksulloc.basket.model.BasketRepository;
import com.aksulloc.basket.model.CustomerBasket;
import com.aksulloc.basket.services.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController()
@RequestMapping("basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketRepository basketRepository;
    private final IdentityService identityService;
    private final EventBus eventBus;

    @RequestMapping("{customerId}")
    public ResponseEntity<CustomerBasket> getBasketById(@PathVariable String customerId) {
        var basket = basketRepository.getBasket(customerId);
        return ResponseEntity.of(basket);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CustomerBasket> updateBasket(@RequestBody @Valid CustomerBasket basket) {
        return ResponseEntity.ok(basketRepository.updateBasket(basket));
    }

    @RequestMapping(path = "checkout", method = RequestMethod.POST)
    public void checkout(@RequestBody BasketCheckout basketCheckout, @RequestHeader("x-requestid") String requestId) {
        var userId = identityService.getUserIdentity();
        UUID requestIdUuid;

        try {
            requestIdUuid = UUID.fromString(requestId);
        } catch (IllegalArgumentException e) {
            requestIdUuid = basketCheckout.getRequestId();
        }
        basketCheckout.setRequestId(requestIdUuid);

        var basket = basketRepository.getBasket(userId).orElse(null);

        if (basket == null) {
            throw new IllegalArgumentException(); // TODO HD BadRequest
        }
        // this.HttpContext.User.FindFirst(x => x.Type == ClaimTypes.Name).Value;
        // TODO HD i need spring oAuth2 to access the logged in user info
        var userName = "TODO";

        var event = new UserCheckoutAcceptedIntegrationEvent(
                userId,
                userName,
                basketCheckout.getCity(),
                basketCheckout.getStreet(),
                basketCheckout.getState(),
                basketCheckout.getCountry(),
                basketCheckout.getZipCode(),
                basketCheckout.getCardNumber(),
                basketCheckout.getCardHolderName(),
                basketCheckout.getCardExpiration(),
                basketCheckout.getCardSecurityNumber(),
                basketCheckout.getCardTypeId(),
                basketCheckout.getBuyer(),
                basketCheckout.getRequestId(),
                basket
        );

        // Once basket is checkout, sends an integration event to
        // ordering.api to convert basket to order and proceeds with
        // order creation process
        eventBus.publish(event);
    }

    @RequestMapping("{id}")
    public void deleteBasketById(@PathVariable String id) {
        basketRepository.deleteBasket(id);
    }
}
