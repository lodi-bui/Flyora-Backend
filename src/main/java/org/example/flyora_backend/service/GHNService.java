package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.ProvinceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GHNService {

    @Value("${ghn.token}")
    private String ghnToken;

    private final String PROVINCE_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";

    public List<ProvinceDTO> getProvinces() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            PROVINCE_API,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<>() {}
        );

        Object rawData = response.getBody().get("data");
        List<ProvinceDTO> provinces = new ArrayList<>();

        if (rawData instanceof List<?> rawList) {
            for (Object item : rawList) {
                if (item instanceof Map<?, ?> map) {
                    ProvinceDTO dto = new ProvinceDTO();
                    dto.setProvinceID((Integer) map.get("ProvinceID"));
                    dto.setProvinceName((String) map.get("ProvinceName"));
                    provinces.add(dto);
                }
            }
        }

        return provinces;
    }
}
