package com.HomeRentSolution.MS_Pagos.model;

import org.springframework.web.client.RestClient;

import java.util.Map;

public class PasarelaPago {

    // Herramienta de Spring para hacer peticiones HTTP a internet
    private final RestClient restClient = RestClient.create();

    public boolean cobrar(Double monto, Long pagoId) {
        // 1. Armas los datos que pide la pasarela
        Map<String, Object> datosEnvio = Map.of(
                "amount", monto,
                "reference_id", pagoId
        );

        // 2. Mandas un POST a la URL de la pasarela externa
        String respuesta = restClient.post()
                .uri("https://api.pasarela-ejemplo.com/v1/cobrar")
                .header("Authorization", "Bearer TU_API_KEY_SECRETA")
                .body(datosEnvio)
                .retrieve()
                .body(String.class);

        // 3. Revisas si la respuesta dice que fue aprobado
        return respuesta != null && respuesta.contains("APPROVED");
    }
}
