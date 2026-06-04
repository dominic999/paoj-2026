package com.pao.laboratory13.exercise1;

public class ProtocolEngine {

    enum State { INIT, AUTH, OPEN, CLOSED }

    private State state = State.INIT;
    private String user;
    private int historyCount;

    public String process(String line) {
        String[] tokens = line.trim().split("\\s+");
        if (tokens.length == 0 || tokens[0].isEmpty()) {
            return "ERR E_PARSE UNKNOWN_COMMAND";
        }

        String cmd = tokens[0];

        return switch (cmd) {
            case "AUTH" -> handleAuth(tokens);
            case "OPEN" -> handleOpen(tokens);
            case "SEND" -> handleSend(tokens);
            case "BROADCAST" -> handleBroadcast(tokens);
            case "HISTORY" -> handleHistory(tokens);
            case "CLOSE" -> handleClose(tokens);
            default -> "ERR E_PARSE UNKNOWN_COMMAND";
        };
    }

    private String handleAuth(String[] tokens) {
        if (tokens.length < 2) {
            return "ERR E_PARSE AUTH";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        user = tokens[1];
        historyCount = 0;
        state = State.AUTH;
        return "OK AUTH user=" + user;
    }

    private String handleOpen(String[] tokens) {
        if (tokens.length > 1) {
            return "ERR E_PARSE OPEN";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state == State.OPEN) {
            return "ERR E_STATE ALREADY_OPEN";
        }
        if (state != State.AUTH) {
            return "ERR E_STATE NOT_OPEN";
        }
        state = State.OPEN;
        return "OK OPEN";
    }

    private String handleSend(String[] tokens) {
        if (tokens.length < 2) {
            return "ERR E_PARSE SEND";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        historyCount++;
        return "OK OPEN sent";
    }

    private String handleBroadcast(String[] tokens) {
        if (tokens.length < 2) {
            return "ERR E_PARSE BROADCAST";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        historyCount++;
        return "OK OPEN broadcast";
    }

    private String handleHistory(String[] tokens) {
        if (tokens.length > 1) {
            return "ERR E_PARSE HISTORY";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        return "OK OPEN history=" + historyCount;
    }

    private String handleClose(String[] tokens) {
        if (tokens.length > 1) {
            return "ERR E_PARSE CLOSE";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        state = State.CLOSED;
        return "OK CLOSED";
    }
}
