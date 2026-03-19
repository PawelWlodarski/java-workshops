package com.wlodar.jeeps.jep483aotclassloading;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

public class MiniApp {

    static void main() throws Exception {
        System.out.println("📦 Starting MiniApp...");

        String startupSummary = warmUpJdkClasses();

        Service service = new Service();
        String result = service.run();

        System.out.println("📢 Output: " + result);
        System.out.println("🧠 Startup summary: " + startupSummary);
    }

    private static String warmUpJdkClasses() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Duration duration = Duration.ofMinutes(42);

        URI uri = URI.create("https://example.com/demo?x=1&name=Pawel");
        String decoded = URLDecoder.decode("hello%20world", StandardCharsets.UTF_8);

        Pattern pattern = Pattern.compile("[A-Z]{2,}");
        boolean matched = pattern.matcher("AOT DEMO").find();

        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(42);
        buffer.putLong(123456789L);
        buffer.flip();

        String formattedNumber = NumberFormat.getInstance(Locale.US).format(1234567.89);
        BigDecimal price = new BigDecimal("123.45");

        String encoded = Base64.getEncoder()
                .encodeToString("demo".getBytes(StandardCharsets.UTF_8));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] sha = digest.digest("important-data".getBytes(StandardCharsets.UTF_8));
        String shaHex = HexFormat.of().formatHex(sha);

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("secret-key-12345".getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hmac = mac.doFinal("payload".getBytes(StandardCharsets.UTF_8));
        String hmacHex = HexFormat.of().formatHex(hmac);

        CRC32 crc32 = new CRC32();
        crc32.update("zip-demo".getBytes(StandardCharsets.UTF_8));
        long checksum = crc32.getValue();

        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.parse(new InputSource(new StringReader("<root><item>demo</item></root>")));
        String xmlRoot = document.getDocumentElement().getTagName();

        List<String> values = List.of(
                today.toString(),
                formattedDate,
                duration.toString(),
                uri.getHost(),
                decoded,
                String.valueOf(matched),
                String.valueOf(buffer.remaining()),
                formattedNumber,
                price.toPlainString(),
                encoded,
                shaHex.substring(0, 12),
                hmacHex.substring(0, 12),
                String.valueOf(checksum),
                xmlRoot,
                UUID.randomUUID().toString().substring(0, 8)
        );

        return String.join(" | ", values);
    }

    static class Service {
        private final Repository repository = new Repository();
        private final Controller controller = new Controller();

        String run() {
            System.out.println("🚀 Running service...");
            String data = repository.fetchData();
            return controller.handle(data);
        }
    }

    static class Repository {
        String fetchData() {
            System.out.println("💾 Fetching data...");
            return "important data";
        }
    }

    static class Controller {
        private final Formatter formatter = new Formatter();

        String handle(String input) {
            System.out.println("🎯 Controller handling data...");
            return formatter.format(input);
        }
    }

    static class Formatter {
        String format(String input) {
            System.out.println("🖋 Formatting data...");
            return "[[" + input.toUpperCase() + "]]";
        }
    }
}