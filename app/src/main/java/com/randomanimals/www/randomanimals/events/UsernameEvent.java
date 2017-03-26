package com.randomanimals.www.randomanimals.events;

/**
 * Created by cbuonocore on 3/18/17.
 */

public class UsernameEvent {

    public String username;

    public UsernameEvent() {
        username = null;
    }

    public UsernameEvent(String username) {
        this.username = username;
    }
}
