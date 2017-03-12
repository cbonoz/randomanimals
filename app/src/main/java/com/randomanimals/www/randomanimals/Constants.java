package com.randomanimals.www.randomanimals;

/**
 * Created by cbono on 2/17/17.
 */

public class Constants {
    private Constants() {

    }

    // TODO: Change to remote server.
    private static final String BASE_URL = "http://localhost:3001";
    public static final String IO_SOCKET = "http://localhost:3001";

    public static final String ANIMAL_URL = BASE_URL + "/animal";
    public static final String PROFILE_URL = BASE_URL + "/userid";
    public static final String INCREMENT_URL = BASE_URL + "/increment";

    public static final String PLAY_EVENT = "playevent";

    public static final String SOUND_FOLDER = "sounds";
    public static final String USERINFO_PREF = "userinfo";
    public static final int ENTER_LEFT = 0;
    public static final int ENTER_RIGHT = 1;

    public static final int MIN_USERNAME_LENGTH = 3;
}
