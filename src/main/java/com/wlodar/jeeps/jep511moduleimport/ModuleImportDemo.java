package com.wlodar.jeeps.jep511moduleimport;

import module java.base;
import module java.net.http;

import com.wlodar.WorkshopPrinter;

public class ModuleImportDemo {
    static void main() throws Exception {
        WorkshopPrinter.title("JEP 511 - module imports");

        //can use LocalDate because of module java.base
        var today = LocalDate.now();

        //can use List because of module java.base
        var list = List.of("alpha", "beta", "gamma");
        var joined = String.join(", ", list);
        var maybe = Optional.of("value");

        //can use Files because of module java.base
        var tempFile = Files.createTempFile("jep511-", ".txt");
        Files.writeString(tempFile, "Today is " + today);

        //can use HttpClient because of module java.net.http
        var client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        var request = HttpRequest.newBuilder()
                .uri(new URI("https://example.com"))
                .GET()
                .build();

        WorkshopPrinter.print("Today", today);
        WorkshopPrinter.print("Joined", joined);
        WorkshopPrinter.print("Optional", maybe.orElse("empty"));
        WorkshopPrinter.print("Temp file", tempFile);
        WorkshopPrinter.print("Http client type", client.getClass().getSimpleName());
        WorkshopPrinter.print("Http request type", request.getClass().getSimpleName());
    }
}
