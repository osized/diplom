package ayudin.diplom.test;

import java.io.File;

/**
 * Created by AYUdin on 16.06.2016.
 */
public class Main {

    public static void main(String[] args) {
        Utils.generateLog(5000000, new File("Hibernate.log"));
    }
}
