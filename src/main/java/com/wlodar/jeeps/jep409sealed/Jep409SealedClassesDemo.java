package com.wlodar.jeeps.jep409sealed;

import com.wlodar.WorkshopPrinter;

public class Jep409SealedClassesDemo {

    static void main() {
        WorkshopPrinter.title("JEP 409: Sealed Classes");

        basicSealedHierarchy();
        reopenedBranchWithNonSealed();
    }

    static void basicSealedHierarchy() {
        WorkshopPrinter.subtitle("Closed hierarchy");

        Payment payment1 = new CardPayment("4111-xxxx");
        Payment payment2 = new CashPayment();
        Payment payment3 = new BlikPayment("123456");

        WorkshopPrinter.print("payment1", describePayment(payment1));
        WorkshopPrinter.print("payment2", describePayment(payment2));
        WorkshopPrinter.print("payment3", describePayment(payment3));
    }

    static String describePayment(Payment payment) {
        return switch (payment) {
            case CardPayment c -> "Card payment, masked card = " + c.maskedCardNumber();
            case CashPayment _ -> "Cash payment"; //JDK 22 _
            case BlikPayment b -> "BLIK payment, code = " + b.code();
        };
    }

    static void reopenedBranchWithNonSealed() {
        WorkshopPrinter.subtitle("One branch reopened with non-sealed");

        Notification notification1 = new Email("user@example.com");
        Notification notification2 = new Sms("+48 123 456 789");
        Notification notification3 = new TeamsNotification("Build failed on main");

        WorkshopPrinter.print("notification1", describeNotification(notification1));
        WorkshopPrinter.print("notification2", describeNotification(notification2));
        WorkshopPrinter.print("notification3", describeNotification(notification3));
    }

    static String describeNotification(Notification notification) {
        return switch (notification) {
            case Email e -> "Email to " + e.address();
            case Sms s -> "SMS to " + s.number();
            case TeamsNotification t -> "Teams notification: " + t.message();
            //comment out and see what happens
            case ExternalNotification e -> "External notification: " + e.message();
        };
    }

    sealed interface Payment permits CardPayment, CashPayment, BlikPayment {}

    record CardPayment(String maskedCardNumber) implements Payment {}
    record CashPayment() implements Payment {}
    record BlikPayment(String code) implements Payment {}

    sealed interface Notification permits Email, Sms, ExternalNotification {}

    record Email(String address) implements Notification {}
    record Sms(String number) implements Notification {}


    //this non-sealed is controversial for me - ITs sealed but not sealed - can be overused by
    //devs who do quick shortcuts
    non-sealed static class ExternalNotification implements Notification {
        private final String message;

        ExternalNotification(String message) {
            this.message = message;
        }

        String message() {
            return message;
        }
    }

    static final class TeamsNotification extends ExternalNotification {
        TeamsNotification(String message) {
            super(message);
        }
    }
}
