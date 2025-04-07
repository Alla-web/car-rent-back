package de.aittr.car_rent.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.aittr.car_rent.config.DigitalOceanProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarImageService {


    private final DigitalOceanProperties digitalOceanProperties;
    private AmazonS3 amazonS3;


    @PostConstruct
    private void init() {
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        digitalOceanProperties.getEndpoint(), "fra1"))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                digitalOceanProperties.getAccessKey(),
                                digitalOceanProperties.getSecretKey())))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    public String uploadToSpaces(String fileName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(digitalOceanProperties.getSpaceName(), fileName, file.getInputStream(), metadata));

        return "https://" + digitalOceanProperties.getSpaceName() + "." + digitalOceanProperties.getEndpoint() + "/" + fileName;
    }

}
