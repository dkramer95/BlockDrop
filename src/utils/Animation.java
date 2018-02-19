package utils;

/**
 * Interface for intro and outro animations.
 * Created by David Kramer on 2/11/2016.
 */
public interface Animation {

    // these are static so that static utility classes can also implement
    // this interface, but all other subclasses can still implement these
    // normally, without using the static keyword.If creating an object
    // of time Animation, they WILL need to be accessed in a static way!
    static void animateIn() {}
    static void animateOut() {}
}
