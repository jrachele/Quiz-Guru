package quizguru;

/**
 *  QuizGuru | by Julian Rachele
 *  Cleaned up for version 1.4.1 in March 2018
 */

import java.sql.*;


public class QuizGuru {

    private String[] results = new String[11];
    private Connection tossups = null;
    private Connection bonuses = null;
    private Connection history = null;
    
    
    QuizGuru(){
        try{
            this.tossups = DriverManager.getConnection("jdbc:sqlite::resource:db/tossups.db");
            this.bonuses = DriverManager.getConnection("jdbc:sqlite::resource:db/bonuses.db");
            this.history = DriverManager.getConnection("jdbc:sqlite::resource:db/history.db");
            
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void closeConnections() throws SQLException{
        if (this.tossups != null){
            tossups.close();
        }
        if (this.bonuses != null){
            bonuses.close();
        }
    }
    
    
    
    
    public String[] GenerateResults(String category, String tournament, String searchBoxValue, String difficulty, boolean history) throws SQLException {
            //Process information from GUI
            String categoryQuery, searchQuery, difficultyQuery, tournamentQuery;
            if (category.equals("All")){
                categoryQuery = "";
            } else {
                categoryQuery = "AND Category = '" + category + "'";
            }
            if (tournament.equals("All")){
                tournamentQuery="";
            } else {
                tournamentQuery="AND Tournament LIKE '%" + tournament + "%'";
            }
            if (difficulty.equals("All")){
                difficultyQuery = "AND (Difficulty = 'College' OR Difficulty = 'MS' OR Difficulty = 'HS' OR Difficulty = 'High School' OR Difficulty = 'Open')";
            } else {
                difficultyQuery = "AND Difficulty = '" + difficulty + "'";
            }
            if (searchBoxValue != null || !searchBoxValue.equals("")){
                searchQuery = "AND (Answer LIKE '%" + searchBoxValue + "%' OR Question LIKE '%" + searchBoxValue + "%')";
            } else{
                searchQuery = "";
            }

            ResultSet myRs;
            if(history){
                String id = this.history.createStatement().executeQuery("SELECT * FROM history WHERE Type=0 ORDER BY RANDOM() LIMIT 1").getString("ID");
                myRs = tossups.createStatement().executeQuery("SELECT * FROM tossupsdb WHERE ID="+ id);
            }
            else {
            myRs = tossups.createStatement().executeQuery("SELECT * FROM tossupsdb WHERE ID LIKE '%%%%' " + categoryQuery + " " + tournamentQuery + " " + searchQuery + " " + difficultyQuery + " ORDER BY RANDOM() LIMIT 1");
            }        
            if (myRs.isBeforeFirst()){
                        results[0]=myRs.getString("tournament");
                        results[1]=myRs.getString("year");
                        results[2]=myRs.getString("ID");
                        results[3]=myRs.getString("question");
                        results[4]=myRs.getString("answer");
                        results[5]=myRs.getString("questionnum");
                        storeQuestion(Integer.valueOf(results[2]),true);
                    } else {
                        return null;
                    }
            return results;  

        
    }
    
    public String[] GenerateBonusResults(String category, String tournament, String searchBoxValue, String difficulty, boolean history) throws SQLException {
            String categoryQuery, searchQuery, difficultyQuery, tournamentQuery;
            if (category.equals("All")){
                categoryQuery = "";
            } else {
                categoryQuery = "AND Category = '" + category + "'";
            }
            if (tournament.equals("All")){
                tournamentQuery="";
            } else {
                tournamentQuery="AND Tournament LIKE '%" + tournament + "%'";
            }
            if (difficulty.equals("All")){
                difficultyQuery = "AND (Difficulty = 'College' OR Difficulty = 'MS' OR Difficulty = 'HS' OR Difficulty = 'High School' OR Difficulty = 'Open')";
            } else {
                difficultyQuery = "AND Difficulty = '" + difficulty + "'";
            }
            if (searchBoxValue != null || searchBoxValue != ""){
                searchQuery = "AND (Intro LIKE '%" + searchBoxValue + "%' OR Question1 LIKE '%" + searchBoxValue + "%'  OR Question2 LIKE '%" + searchBoxValue + "%'  OR Question3 LIKE '%" + searchBoxValue + "%'  OR Answer1 LIKE '%" + searchBoxValue + "%'  OR Answer2 LIKE '%" + searchBoxValue + "%'  OR Answer3 LIKE '%" + searchBoxValue + "%')";
            } else{
                searchQuery = "";
            }
            
            ResultSet myRs;
            if(history){
                String id = this.history.createStatement().executeQuery("SELECT * FROM history WHERE Type=1 ORDER BY RANDOM() LIMIT 1").getString("ID");
                myRs = bonuses.createStatement().executeQuery("SELECT * FROM bonusesdb WHERE ID="+ id);
            } else{
            myRs = bonuses.createStatement().executeQuery("select * from bonusesdb WHERE ID LIKE '%%%%' " + categoryQuery + " " + tournamentQuery + " " + searchQuery + " " + difficultyQuery + " ORDER BY RANDOM() LIMIT 1");
            }
            if(myRs.isBeforeFirst()){
                results[0] = myRs.getString("tournament");
                results[1] = myRs.getString("year");
                results[2] = myRs.getString("ID");
                results[3] = myRs.getString("intro");
                results[4] = myRs.getString("question1");
                results[5] = myRs.getString("answer1");
                results[6] = myRs.getString("question2");
                results[7] = myRs.getString("answer2");
                results[8] = myRs.getString("question3");
                results[9] = myRs.getString("answer3");
                results[10] = myRs.getString("questionnum");
                storeQuestion(Integer.valueOf(results[2]),false);
            } else{
                return null;
            }
            return results;
    }
   
    public void storeQuestion(int questionID, boolean tossup) throws SQLException{
        if(tossup){
            history.createStatement().executeUpdate("INSERT INTO history VALUES (" + questionID + ", 0)");
        } else {
            history.createStatement().executeUpdate("INSERT INTO history VALUES (" + questionID + ", 1)");
        }     
    }
    
    
    
    
    
    
    
    
}
