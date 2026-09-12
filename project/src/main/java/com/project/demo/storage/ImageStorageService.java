package com.project.demo.storage;

import com.project.demo.config.AppSecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Iterator;
import java.util.regex.Pattern;

@Service
public class ImageStorageService {

    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
    };
    private static final Pattern STORED_NAME = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(png|jpg)$");
    private static final int MAX_DIMENSION = 8192;
    private static final long MAX_PIXELS = 25_000_000L;

    private final Path uploadRoot;
    private final long maxBytes;

    public ImageStorageService(AppSecurityProperties properties) {
        this.uploadRoot = Paths.get(properties.getUpload().getDir()).toAbsolutePath().normalize();
        this.maxBytes = properties.getUpload().getMaxBytes();
    }

    public StoredImage store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("没有选择图片");
        }
        if (file.getSize() <= 0 || file.getSize() > maxBytes) {
            throw new InvalidImageException("图片大小必须在允许范围内");
        }

        try {
            byte[] bytes = file.getBytes();
            ImageFormat format = detectFormat(bytes);
            BufferedImage decoded = decode(bytes);
            if (decoded == null || decoded.getWidth() <= 0 || decoded.getHeight() <= 0) {
                throw new InvalidImageException("文件不是可解码的图片");
            }

            Files.createDirectories(uploadRoot);
            String fileName = UUID.randomUUID().toString() + format.extension;
            Path target = safePath(fileName);
            boolean written = ImageIO.write(decoded, format.writerName, target.toFile());
            if (!written) {
                Files.deleteIfExists(target);
                throw new InvalidImageException("当前环境不支持该图片格式");
            }
            return new StoredImage(fileName, format.contentType, target);
        } catch (InvalidImageException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new InvalidImageException("图片读取或保存失败", exception);
        }
    }

    public StoredImage load(String fileName) {
        if (fileName == null || !STORED_NAME.matcher(fileName).matches()) {
            throw new InvalidImageException("图片名称不合法");
        }
        Path target = safePath(fileName);
        if (!Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)
                || Files.isSymbolicLink(target)) {
            throw new InvalidImageException("图片不存在");
        }
        String contentType = fileName.endsWith(".png") ? "image/png" : "image/jpeg";
        return new StoredImage(fileName, contentType, target);
    }

    private ImageFormat detectFormat(byte[] bytes) {
        if (startsWith(bytes, PNG_SIGNATURE)) {
            return new ImageFormat(".png", "png", "image/png");
        }
        if (bytes.length >= 3 && (bytes[0] & 0xff) == 0xff
                && (bytes[1] & 0xff) == 0xd8 && (bytes[2] & 0xff) == 0xff) {
            return new ImageFormat(".jpg", "jpg", "image/jpeg");
        }
        throw new InvalidImageException("仅支持 PNG 或 JPEG 图片");
    }

    private BufferedImage decode(byte[] bytes) throws IOException {
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            if (input == null) {
                throw new InvalidImageException("文件不是可解码的图片");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new InvalidImageException("文件不是可解码的图片");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || width > MAX_DIMENSION || height > MAX_DIMENSION
                        || (long) width * height > MAX_PIXELS) {
                    throw new InvalidImageException("图片尺寸超出允许范围");
                }
                return reader.read(0);
            } finally {
                reader.dispose();
            }
        }
    }

    private boolean startsWith(byte[] bytes, byte[] signature) {
        if (bytes.length < signature.length) {
            return false;
        }
        for (int index = 0; index < signature.length; index++) {
            if (bytes[index] != signature[index]) {
                return false;
            }
        }
        return true;
    }

    private Path safePath(String fileName) {
        Path target = uploadRoot.resolve(fileName).normalize();
        if (!target.startsWith(uploadRoot)) {
            throw new InvalidImageException("图片路径不合法");
        }
        return target;
    }

    private static final class ImageFormat {
        private final String extension;
        private final String writerName;
        private final String contentType;

        private ImageFormat(String extension, String writerName, String contentType) {
            this.extension = extension;
            this.writerName = writerName;
            this.contentType = contentType;
        }
    }
}
