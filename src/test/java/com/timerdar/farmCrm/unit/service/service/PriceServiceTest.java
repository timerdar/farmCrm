package com.timerdar.farmCrm.unit.service.service;

import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.PriceRepository;
import com.timerdar.farmCrm.service.PriceService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository repository;

    @InjectMocks
    private PriceService priceService;

    @Test
    void createPrice_shouldThrowIllegalArgument_whenDTOisInvalid() {
        Price price = new Price(0L, null, 0, true);

        assertThrows(IllegalArgumentException.class, () -> {
            priceService.createPrice(price);
        });
    }

    @Test
    void getPriceList_shouldReturnEmptyList() {
        when(repository.findAll(any(Sort.class))).thenReturn(new ArrayList<>());

        List<Price> prices = priceService.getPriceList();

        assertTrue(prices.isEmpty());
    }

    @Test
    void getPriceList_shouldReturnList(){
        Price first = new Price();
        Price second = new Price();
        ArrayList<Price> prices = new ArrayList<>();
        prices.add(first);
        prices.add(second);
        when(repository.findAll(any(Sort.class))).thenReturn(prices);

        List<Price> actualPrices = priceService.getPriceList();

        assertTrue(actualPrices.contains(first));
        assertTrue(actualPrices.contains(second));
        assertEquals(2, actualPrices.size());
    }

    @Test
    void getPriceById_shouldThrowEntityNotFound_whenIdNotExists() {
        long priceId = 2L;
        when(repository.findById(priceId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getPriceById(priceId);
        });
    }

    @Test
    void getPriceById_shouldReturnPrice_whenIdExists(){
        Price price = new Price(3L, "Курица", 300, true);
        when(repository.findById(3L)).thenReturn(Optional.of(price));

        Price actual = priceService.getPriceById(3L);

        assertEquals(price, actual);
    }
}