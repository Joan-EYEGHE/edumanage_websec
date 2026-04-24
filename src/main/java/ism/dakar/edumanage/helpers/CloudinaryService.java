package ism.dakar.edumanage.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String type) {
        try {
            Map params = ObjectUtils.asMap(
                    "public_id", UUID.randomUUID().toString(),
                    "overwrite", true,
                    "resource_type", type
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload sur Cloudinary", e);
        }
    }

    public byte[] getFile(String path) throws IOException {
        URL url = new URL(path);
        try (InputStream is = url.openStream()) {
            return is.readAllBytes();
        }
    }
}
