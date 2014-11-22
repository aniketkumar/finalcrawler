package finalcrawler.java;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Aniket
 */
public class FinalcrawlerJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException {
        // TODO code application logic here
        Mycrawler obj = new Mycrawler();
        obj.begincrawl();
    }
    
}
