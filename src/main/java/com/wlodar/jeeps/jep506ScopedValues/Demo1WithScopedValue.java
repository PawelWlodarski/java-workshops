package com.wlodar.jeeps.jep506ScopedValues;

/**
 * Example of using ScopedValue to propagate context across method calls.
 * Second class below show classic approach.
 */
public class Demo1WithScopedValue {
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    static void main() {
        /**
         * static invocation chain builds context for run
         */
        ScopedValue.where(REQUEST_ID, "req-999")
                .where(USER_ID, "111")
                .run(() -> controller("payload-123"));
    }

    static void controller(String payload) {
        System.out.println("controller()");
        service(payload);
    }

    static void service(String payload) {
        System.out.println("service()");
        repository(payload);
    }

    static void repository(String payload) {
        System.out.println("repository()");
        audit(payload);
    }

    static void audit(String payload) {
        System.out.println("audit()");
        System.out.println("payload    = " + payload);
        System.out.println("requestId  = " + REQUEST_ID.get());
        System.out.println("userId     = " + USER_ID.get());
    }
}



class Demo1WithoutScopedValue {

    /**
     * Classic approach without ScopedValue.
     * All three parameters are passed explicitly.
     */
    static void handle() {
        controller("somePayload2", "req-111", "222");
    }

    static void controller(String payload, String requestId, String userId) {
        System.out.println("controller()");
        service(payload, requestId, userId);
    }

    static void service(String payload, String requestId, String userId) {
        System.out.println("service()");
        repository(payload, requestId, userId);
    }

    static void repository(String payload, String requestId, String userId) {
        System.out.println("repository()");
        audit(payload, requestId, userId);
    }

    static void audit(String payload, String requestId, String userId) {
        System.out.println("audit()");
        System.out.println("payload    = " + payload);
        System.out.println("requestId  = " + requestId);
        System.out.println("userId     = " + userId);
    }
}