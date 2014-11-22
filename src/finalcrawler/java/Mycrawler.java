/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finalcrawler.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Aniket
 */


public class Mycrawler {
    String table_name = "Record1";
    String sentdata = "";

    /**
     * @param args the command line arguments
     */
   public static  DB db = new DB();
    
         //ArrayList<String> arl = new ArrayList<String>(10000);
         Queue<String> qarl = new LinkedList<String>();
        int visited = 0;
        int added = 1;
        String inps;
        Mwriter writer_obj;
       // String seedurl = "http://en.wikipedia.org/wiki/Main_Page"; Error on 5th page !!!
        
        String seedurl = "http://stackoverflow.com/";
        String logfile = "pagedata/crawl_log.txt";
	public void begincrawl() throws SQLException, IOException {
                        System.getProperties().put("http.proxyHost", "172.31.1.4");
                        System.getProperties().put("http.proxyPort", "8080");
                        System.getProperties().put("http.proxyUser", "iit2012011");
                        System.getProperties().put("http.proxyPassword", "*******");
		//db.runSql2("TRUNCATE Record;");
                db.runSql2("TRUNCATE " + table_name + ";");
                //arl.add(seedurl);
                qarl.add(seedurl);
                writer_obj = new Mwriter();
		processPage();
	}
 
	public void processPage() throws SQLException, IOException { 
            
                while (visited < 10000) {
                    
                String URL = qarl.element();
                qarl.remove();
                visited++;
               System.out.println(" Url supplied : " + URL + "****************");
                writer_obj.writetoFile2(logfile, " Url supplied : " + URL + "****************\n");
                
		String sql = "select * from " + table_name + " where URL = '" + URL + "'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
 
		}else{
                        Document doc;
                        try {
			 doc = Jsoup.connect(URL).get();
                          //  System.out.print("Page IS :\n" + doc.text() + "\n");
                         sentdata = "";
                         sentdata += doc.text();
                         
                        //sentdata = sentdata.replaceAll("[^a-zA-Z]+"," ");
                        sentdata = sentdata.replaceAll("[^a-zA-Z0-9]+"," ");
                         
                         
                         writer_obj.writetoFile("pagedata/id"+ (added) +".txt", sentdata);
                      //    writer_obj.writetoFile("pagedata/id"+ (added) +".txt",doc.text());
                          
                        sql = "INSERT INTO  `Crawler`.`" + table_name +"` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();
                        
                        added++;
                            
                          //  System.out.println("Press Any Key To Continue...");
                            //new java.util.Scanner(System.in).nextLine();
                        } catch (IOException e){
                            System.out.println("Not an html page");
                            writer_obj.writetoFile2(logfile, "Not an Html page \n");
                            continue;
                        }
     
			Elements questions = doc.select("a[href]");
                        
                        for(Element link: questions){
                            String tmpurl;
                            tmpurl = link.attr("abs:href");
				if(link.attr("href").contains("http")) {
                                    if (link.attr("href").contains("'"))
                                        continue;
                                    
                                    System.out.println("URL trying to added :" + tmpurl);
                                    String tsql = "";
                                    
                                    try {
                                    tsql = "select * from " + table_name +" where URL = '" + tmpurl + "'";
                                    } catch(Exception e) {
                                        continue;
                                    }
                                    ResultSet trs = db.runSql(tsql);
                                    
                                    if(!trs.next()) {
                                        //arl.add(link.attr("abs:href"));
                                        qarl.add(link.attr("abs:href"));                                
                                        System.out.println("URL added :" + (link.attr("abs:href")));
                                        writer_obj.writetoFile2(logfile,"URL added :" + (link.attr("abs:href")) + "\n");
                                        
                                    } else {
                                        System.out.println("URL NOT added :" + (link.attr("abs:href")));
                                        writer_obj.writetoFile2(logfile,"URL NOT added :" + (link.attr("abs:href")) + "\n");
                                    }
                           
                                }
			}                    
		}
              }
	}
        
        public void processPage2() throws SQLException, IOException{
            Document dc;
            try {
               dc = Jsoup.connect("http://www.ic.unicamp.br/~meidanis/courses/mc336/2009s2/prolog/problemas/").get(); 
               String s = dc.text();
            
                System.out.println(s);
            } catch (IOException e) {
                System.out.println("Naa ho paya !!!");
            }
        }
    
}

