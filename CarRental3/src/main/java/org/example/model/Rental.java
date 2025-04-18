package org.example.model;

import lombok.*;

import java.time.LocalTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Rental {
    private String id;
    private String vehicleId;
    private String userId;
    private String rentTime;
    private String returnTime;

    @Builder.Default
    private Map<String, Object> attributes = Map.of();

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}
