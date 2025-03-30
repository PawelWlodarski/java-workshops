package com.wlodar.jeeps.jep483aotclassloading;

public class MiniApp {
    public static void main(String[] args) {
        System.out.println("📦 Starting MiniApp...");
        new Service().run();
    }
}

// === Simulated Application Components ===

class Service {
    private final Repository repository = new Repository();
    private final Controller controller = new Controller();

    void run() {
        System.out.println("🚀 Running service...");
        String data = repository.fetchData();
        controller.handle(data);
    }
}

class Repository {
    String fetchData() {
        System.out.println("💾 Fetching data...");
        return "Important Data";
    }
}

class Controller {
    private final Formatter formatter = new Formatter();

    void handle(String input) {
        System.out.println("🎯 Controller handling data...");
        String formatted = formatter.format(input);
        System.out.println("📢 Output: " + formatted);
    }
}

class Formatter {
    String format(String input) {
        System.out.println("🖋 Formatting data...");
        return "[[" + input.toUpperCase() + "]]";
    }
}

