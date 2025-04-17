package de.aittr.car_rent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "digitalocean")
@Getter
@Setter
public class DigitalOceanProperties {

    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String spaceName;
}
