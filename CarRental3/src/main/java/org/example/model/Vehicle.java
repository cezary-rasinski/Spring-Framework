package org.example.model;
import lombok.*;
import java.util.Map;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vehicle {
        private String id;
        private String category;
        private String brand;
        private String model;
        private int year;
        private String plate;

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

        @Override
        public String toString() {
                return String.format("Vehicle{id='%s', category='%s', brand='%s', model='%s', year=%d, plate='%s', attributes=%s}",
                        id, category, brand, model, year, plate, attributes);
        }
}
