package com.jsvcastillo.cashcard;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(
            CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard request, UriComponentsBuilder ucb) {

        CashCard savedCashCard = cashCardRepository.save(request);

        URI locationUri = ucb.path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();

        return ResponseEntity.created(locationUri).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(
                    Sort.by(Sort.Direction.ASC, "amount")
                )));

        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{requestId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
