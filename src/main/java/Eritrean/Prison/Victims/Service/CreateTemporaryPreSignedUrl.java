package Eritrean.Prison.Victims.Service;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Component
public class CreateTemporaryPreSignedUrl {
    private final String BUCKET_NAME = "eritrean-victims-profile-photo-bucket";

    public static void main(String[] args) {
        CreateTemporaryPreSignedUrl createTemporaryPreSignedUrl = new CreateTemporaryPreSignedUrl();
        System.out.println(createTemporaryPreSignedUrl.generateUploadUrl("jpeg"));
    }

    public String generateUploadUrl(String fileType) {

        try (S3Presigner presigner = S3Presigner.create()) {
            String fileName = "profiles/" + UUID.randomUUID() + "." + fileType;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType("image/" + fileType)
                    .build();
            PresignedPutObjectRequest preSignedRequest = presigner.presignPutObject(
                    PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10)) // Use this instead
                            .putObjectRequest(putObjectRequest)
                            .build()
            );

            return preSignedRequest.url().toString();
        }


    }
}
