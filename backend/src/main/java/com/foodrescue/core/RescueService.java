package com.example.foodrescue.core;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RescueService {
    public List<String> filterNonPerishables(List<String> items) {
        // Dummy-Logik: alles ohne "frisch" gilt als haltbar
        return items.stream()
                .filter(i -> !i.toLowerCase().contains("frisch"))
                .collect(Collectors.toList());
    }

    public String health() {
        return "OK";
    }
}
