package fr.intechinfo.crashreporterlibrary;

/**
 * Created by Nicolas on 15/09/2016.
 */
public class Guard {

    public static <T extends Exception> void Requires(boolean condition, T ex) throws T {
        if(!condition) throw ex;
    }

}
