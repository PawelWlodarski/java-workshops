package com.wlodar.jeeps.jep394;

public final class PersonEqualsExample {
    private final String name;
    private final int age;
    private final String city;

    PersonEqualsExample(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    /**
     * Old version before pattern matching:
     * {@code
     * @Override
     * public boolean equals(Object o) {
     *     if (this == o) {
     *         return true;
     *     }
     *     if (!(o instanceof Person)) {
     *         return false;
     *     }
     *     Person other = (Person) o;
     *     return age == other.age
     *             && java.util.Objects.equals(name, other.name)
     *             && java.util.Objects.equals(city, other.city);
     * }
     * }
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        //!!! compiler knows that after this if other has to be PersonEqualsExample
        if (!(o instanceof PersonEqualsExample other)) {
            return false;
        }
        return age == other.age
                && java.util.Objects.equals(name, other.name)
                && java.util.Objects.equals(city, other.city);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age, city);
    }

    @Override
    public String toString() {
        return "Person[name=%s, age=%d, city=%s]".formatted(name, age, city);
    }
}
