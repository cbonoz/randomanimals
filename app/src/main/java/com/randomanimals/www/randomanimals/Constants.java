package com.randomanimals.www.randomanimals;

public class Constants {
    private Constants() {
    }

    // private static final String BASE_URL = "http://35.167.54.81:9001/ra"; // old aws instance.
    // TODO: move this to load balancer url.
    private static final String BASE_URL = "http://blackshoalgroup.com:9001/ra";

    public static final String ANIMAL_URL = BASE_URL + "/animal";
    public static final String PROFILE_URL = BASE_URL + "/userid";
    public static final String INCREMENT_URL = BASE_URL + "/increment";
    public static final String USERNAME_URL = BASE_URL + "/username";

    public static final String PLAY_EVENT = "playevent";

    public static final String SOUND_FOLDER = "sounds";
    // public static final String USERINFO_PREF = "userinfo";
    public static final int ENTER_LEFT = 0;
    public static final int ENTER_RIGHT = 1;

    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 16;

    public static final String USERNAME_KEY = "username";
    public static final String SPINNER_KEY = "spinner";
}
