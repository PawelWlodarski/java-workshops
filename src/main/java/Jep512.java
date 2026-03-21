//module java.base with Lists etc imported by default

String workshopName = "JEP 512 demo";

void main() {
    IO.println(workshopName);

    String name = IO.readln("Please enter your name: ");
    IO.print("Pleased to meet you, ");
    IO.println(name);

    var names = List.of("Java", "Kotlin", "Scala");
    names.forEach(this::printName);

    IO.println("Technically this can be run java Jep512.java like scripts...");
}

void printName(String name) {
    IO.println("Language: " + name);
}