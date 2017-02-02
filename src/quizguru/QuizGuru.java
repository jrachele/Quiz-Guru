/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizguru;

/**
 *
 * @author julrachele
 */

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



import static quizguru.QuizGuruGUI.*;



public class QuizGuru {

    /**
     * @param args the command line arguments
     */
    


    static String category = new String();
    int limit = 10;
    int randNum = 0;
    boolean question = false;
    boolean resultsExist=true;
    String[] results = new String[11];
    float volumeFloat = 2.0f;
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    
    
    public static boolean isStringNumeric( String str )
    {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for ( char c : str.substring( 1 ).toCharArray() )
        {
           if ( !Character.isDigit( c ) )
           {
               if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
              {
                  isDecimalSeparatorFound = true;
                 continue;
              }
              return false;
           }
        }
        return true;
    }
    
    
    public String[] GenerateResults(String category, String tournament, boolean questionRead, String searchBoxValue, String difficulty){
        try {
            
            //Process information from GUI
            String categoryQuery = new String();
            String searchQuery = new String();
            String difficultyQuery = new String();
            if (category == "All"){
                categoryQuery = "";
            } else {
                categoryQuery = "AND Category = '" + category + "'";
            }
            String tournamentQuery = new String();
            if (tournament == "All"){
                tournamentQuery="";
            } else {
                tournamentQuery="AND Tournament LIKE '%" + tournament + "%'";
            }
            if (difficulty == "All"){
                difficultyQuery = "AND Difficulty = 'college' OR Difficulty = 'ms' OR Difficulty = 'hs' OR Difficulty = 'high school' OR Difficulty = 'open' OR Difficulty = 'college' ";
            } else {
                difficultyQuery = "AND Difficulty = '" + difficulty + "'";
            }
            if (searchBoxValue != null || searchBoxValue != ""){
                searchQuery = "AND Answer LIKE '%" + searchBoxValue + "%' OR Question LIKE '%" + searchBoxValue + "%'";
            } else{
                searchQuery = "";
            }

            if(questionRead == false){
                // Get connection to DB
                Connection myConnection = DriverManager.getConnection("jdbc:sqlite:src/db/db.sqlite");

                
                
                // Get number of possible questions
                
                if (resultsExist==true){
                    // Execute SQL Query
                    //ResultSet myRs = myConnection.createStatement().executeQuery("select * from tossupsdbnew");
                    ResultSet myRs = myConnection.createStatement().executeQuery("select * from tossupsdbnew WHERE ID LIKE '%%%%' " + categoryQuery + " " + tournamentQuery + " " + searchQuery + " " + difficultyQuery + " ORDER BY RANDOM() LIMIT 1");
                    

                    results[0]=myRs.getString("tournament");
                    results[1]=myRs.getString("year");
                    results[2]=myRs.getString("ID");
                    results[3]=myRs.getString("question");
                    results[4]=myRs.getString("answer");
                    results[5]=myRs.getString("question #");
                } else if (resultsExist == false){
                    return null;
                }
                
                
            } 

            return results;  
            
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
        
    }
    
    public String[] GenerateBonusResults(String category, String tournament, String searchBoxValue, String difficulty){
        try {
            
            //Process information from GUI
            String categoryQuery = new String();
            String searchQuery = new String();
            String difficultyQuery = new String();
            if (category == "All"){
                categoryQuery = "";
            } else {
                categoryQuery = "AND Category = '" + category + "'";
            }
            String tournamentQuery = new String();
            if (tournament == "All"){
                tournamentQuery="";
            } else {
                tournamentQuery="AND Tournament LIKE '%" + tournament + "%'";
            }
            if (difficulty == "All"){
                difficultyQuery = "AND Difficulty = 'college' OR Difficulty = 'ms' OR Difficulty = 'hs' OR Difficulty = 'high school' OR Difficulty = 'open' OR Difficulty = 'college' ";
            } else {
                difficultyQuery = "AND Difficulty = '" + difficulty + "'";
            }
            if (searchBoxValue != null || searchBoxValue != ""){
                searchQuery = "AND Intro LIKE '%" + searchBoxValue + "%' OR Question1 LIKE '%" + searchBoxValue + "%'  OR Question2 LIKE '%" + searchBoxValue + "%'  OR Question3 LIKE '%" + searchBoxValue + "%'  OR Answer1 LIKE '%" + searchBoxValue + "%'  OR Answer2 LIKE '%" + searchBoxValue + "%'  OR Answer3 LIKE '%" + searchBoxValue + "%'";
            } else{
                searchQuery = "";
            }

            
                // Get connection to DB
                Connection myConnection = DriverManager.getConnection("jdbc:sqlite:src/db/db.sqlite");

                // Create statement
                
                
                // Get number of possible questions
                
                if (resultsExist==true){
                    // Execute SQL Query
                    ResultSet myRs = myConnection.createStatement().executeQuery("select * from bonusesdb WHERE ID LIKE '%%%%' " + categoryQuery + " " + tournamentQuery + " " + searchQuery + " " + difficultyQuery + " ORDER BY RANDOM() LIMIT 1");
                    

                    results[0]=myRs.getString("tournament");
                    results[1]=myRs.getString("year");
                    results[2]=myRs.getString("ID");
                    results[3]=myRs.getString("intro");
                    results[4]=myRs.getString("question1");
                    results[5]=myRs.getString("answer1");
                    results[6]=myRs.getString("question2");
                    results[7]=myRs.getString("answer2");
                    results[8]=myRs.getString("question3");
                    results[9]=myRs.getString("answer3");
                    results[10]=myRs.getString("question #");
                } else if (resultsExist == false){
                    return null;
                }
                
                
           

            return results;  
            
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
        
    }
    
    
    
    
    
    
    
    
}
