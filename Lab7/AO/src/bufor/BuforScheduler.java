package bufor;

import bufor.request.MethodRequest;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BuforScheduler implements Runnable{

    private final BlockingQueue<MethodRequest> activationQueue;
    private final Queue<MethodRequest> awaitingRequestsQueue;

    public BuforScheduler() {
        this.activationQueue = new LinkedBlockingQueue<>();
        this.awaitingRequestsQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                dispatch();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatch() throws InterruptedException {
//        System.out.format("Awaiting:   %s\n", awaitingRequestsQueue);
//        System.out.format("Activation: %s\n", activationQueue);
//        Process all possible awaiters in order, since they were in bufer first
        MethodRequest firstAwaiter = awaitingRequestsQueue.peek();
        if (firstAwaiter != null && firstAwaiter.guard()) {
            awaitingRequestsQueue.remove();
            firstAwaiter.call();
            return;
        }

//        Process new requests, queueing same type of requests as awaiters
//        (can do it, because only one type will be awaiting, if producers are waiting for space
//        any incoming consumer will be able to take and any incoming producer should wait in queue after others
//        to prevent starvation)
        MethodRequest newRequest = activationQueue.take();
        if (firstAwaiter != null && areSameClass(newRequest, firstAwaiter)){
//            queue up request if more of the same type are waiting
            awaitingRequestsQueue.add(newRequest);
        } else if (newRequest.guard()) {
//            process request, since it is different type than awaiting once
            newRequest.call();
        } else {
//            queue up request (awaitingRequestsQueue is now empty)
            awaitingRequestsQueue.add(newRequest);
        }

    }

    public void enqueue(MethodRequest methodRequest) {
        activationQueue.add(methodRequest);
    }

    private boolean areSameClass(MethodRequest object1, MethodRequest object2) {
        return object1.getClass().equals(object2.getClass());
    }

}
