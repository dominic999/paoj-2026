package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {
    private OrderState currentState;
    private final Deque<OrderState> history;

    public Order(OrderState currentState) {
        this.currentState = currentState;
        this.history = new ArrayDeque<>();
    }

    public OrderState nextState() {
        if (currentState.isFinalState()) {
            throw new OrderIsAlreadyFinalException();
        }
        history.push(currentState);
        currentState = currentState.next();
        return currentState;
    }

    public void cancel() {
        if (currentState.isFinalState()) {
            throw new CannotCancelFinalOrderException();
        }
        history.push(currentState);
        currentState = OrderState.CANCELED;
    }

    public OrderState undoState() {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }
        currentState = history.pop();
        return currentState;
    }
}
