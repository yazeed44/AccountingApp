/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package accountingapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author yazeed44
 */
public class DataBase {
    
    
   
   static final String DB_PATH = "jdbc:sqlite:database.db";//the database path
static Connection c = null;//connection instance
 static Statement stmt = null;//statment instance
   public DataBase(){
       
   }
   
   //create database , or open the existed one
   public static void createDataBase(){
       
               
               try {
                   Class.forName("org.sqlite.JDBC");//register sqlite
                   c = DriverManager.getConnection(DB_PATH);//get connection with database
                  
                   System.out.println("Opened database succesfully");
                   
               }
               
               catch (Exception e){
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   
               }
       
               
   }
   
   //create incoming table
   public static void createTableIncoming(){
                  createDataBase();//open the database
                  try {
                      stmt = c.createStatement();//define the statment
                      
                      String sql = "CREATE TABLE IF NOT EXISTS incoming " +
                              "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "date TEXT NOT NULL," +
                              "amount INT NOT NULL," +
                              "category TEXT NOT NULL," +
                              "isPayed TEXT NOT NULL," +
                             "payMethod TEXT NOT NULL,"
                              +
                              "note TEXT );";//sql string , used for creating table in database if it hasn't been created yet
                      stmt.executeUpdate(sql);//execute the sql string
                    
                  }
                  
                  catch (Exception e){
                     
                      e.printStackTrace();
     
                  }
                  System.out.println("Table incoming created succesfully");
               }
   //create outgoing table
    public static void createTableOutgoing(){
        createDataBase();//open the database
        
        try {
            stmt = c.createStatement();//define statment
            String sql = "CREATE TABLE IF NOT EXISTS outgoing " +
             "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT NOT NULL," + "amount INT NOT NULL,"
                    + "reason TEXT NOT NULL," + "note TEXT );"
                    ;//sql string , used for create table in database if it hasn't been created yet
            stmt.executeUpdate(sql);//execute the sql string
            
            
        }
        
        catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Table outgoing created succesfully");
    }
   
    //insert new row into incoming table
   public static void insertIntoTableIncoming(String date , int amount , String cateogry , String isPayed ,String payMethod, String note){
       createTableIncoming();//create the table if it hasn't been created
      
       try {
          c.setAutoCommit(false);//set auto save to false
           String sql = "INSERT INTO incoming(date,amount,category,isPayed,payMethod,note) " + 
                   "VALUES(" + "'" + date +"' ," + amount +" ," + "'" + cateogry +"' ,"
                   + "'" + isPayed +"' ," + "'" + payMethod +"'," +
                   
                   "'" + note +"');";//insert data into incoming table with the spectfied parameters
           
           stmt.executeUpdate(sql);//execute sql string
           
           
       } catch (SQLException e) {
          
           e.printStackTrace();
  
       }
       finally {//just to make sure
           try {
               stmt.close();//close statment
               c.commit();//save
               c.close();//close
            
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
       }
       System.out.println("Records created successfully in incoming");
   }
   
   //insert new row in outgoing table
   public static void insertIntoTableOutGoing(String date , int amount , String reason ,String note){
       createTableOutgoing();//create the table if it hasn't been created yet
       
       
       try {
           c.setAutoCommit(false);//set auto save to false
           String sql = "INSERT INTO outgoing(date,amount,reason,note) "
                   + "VALUES(" + "'" + date + "'" 
                   + ", " + amount + ", " + "'" + reason + "'" + ", "
                   + "'" + note +"');";//insert data into outgoing table with spectfied parameters
         
           stmt.executeUpdate(sql);//execute sql string
          
       }
       
       catch(SQLException e){
           e.printStackTrace();
       }
        finally {
           try {//just to make sure
               stmt.close();//close statment
               c.commit();//save
               c.close();//close
             
           } catch (SQLException ex) {
              ex.printStackTrace();
           }
       }
       System.out.println("Records created successfully in outGoing");
   }
   
   //load the rows into incoming table (JTable)
   public static void setAllRowsIncoming(DefaultTableModel model){
       createTableIncoming();//create table if it hasn't been created yet,define statment and connection
       
      clearTable(model);//clear the incoming table (JTable)
      
       ResultSet resultSet = null;//define a result set
     
       Object rows[] = new Object[7];//define an array of objects
       
       try {
        
           String sql = "SELECT * FROM incoming;";//get every thing from incoming table (Database)
            resultSet = stmt.executeQuery(sql);//execute the query
        
          while (resultSet.next()){
              int id = resultSet.getInt("id");//get the id from column name
              
              rows[0] = id;
              
              String date = resultSet.getString("date");//get the date from the column name
             
              rows[1] = date;
              
              int amount = resultSet.getInt("amount");//get the amount from the column name
              
              rows[2] = amount;
              
              String category = resultSet.getString("category");//get the category from the column name
              
              rows[3] = category;
              
              String isPayed = resultSet.getString("isPayed");//get is payed from the column name
              
              rows[4] = isPayed;
              
              String payMethod = resultSet.getString("payMethod");//get pay method from the column name
              rows[5] = payMethod;
              
              String note = resultSet.getString("note");//get note from the column name
            
              rows[6] = note;
              
              
              model.addRow(rows);//now add the array to the table as a new row
           
          }
          
       }
       
       catch (SQLException e){
           e.printStackTrace();
       }
       
       
       finally {//just to make sure
           try {
               resultSet.close();//close
          stmt.close();//close
          c.close();//close
           }
           
           catch(SQLException e){
               e.printStackTrace();
           }
       }
       
       System.out.println("It did set all the content of the table incoming succesfully");
   }
   
   //load the rows into outgoing table (JTable)
   public static void setAllRowsOutgoing(DefaultTableModel model){
       
       createTableOutgoing();//create the table if it hasn't been created yet,and define statment and connection
       clearTable(model);//clear the outgoing table (JTable)
      
       ResultSet result = null;//define result set
       Object rows[] = new Object[5];//define an array of objects
       
       try {
           String sql  = "SELECT * FROM outgoing;";//get every thing from outgoing table
           result = stmt.executeQuery(sql);//execute the query
           
           while(result.next()){
               int id = result.getInt("id");//get the id from the column name
               
               rows[0] = id;
               
               String date = result.getString("date");//get the date from the column name
               
               rows[1] = date;
               
               int amount = result.getInt("amount");//get the amount from the column name
              
               rows[2] = amount;
               
               String reason = result.getString("reason");//get the reason from the column name
            
               rows[3] = reason;
               
               String note = result.getString("note");//get the note from the column name
          
               rows[4] = note;
               
               
               
               model.addRow(rows);//now add the array into the table as new row (JTable)
           }
       }
       
       catch (SQLException e){
           e.printStackTrace();
       }
       
       finally {//just to make sure
           try {
               result.close();//close
               stmt.close();//close
               c.close();//close
           }
           catch (SQLException e ){
               e.printStackTrace();
           }
       }
       
       System.out.println("it did set all the content of table outgoing succesfully");
   }
   
   
   //reset the table
   public static void clearTable(DefaultTableModel model){
       
       for (int i = 0 ; i < model.getRowCount(); i++){
           model.removeRow(i);//remove row
       }
       model.setRowCount(0);//reset the row count
       
   }
   
   //edit spectfied row in incoming table (Database)
   public static void editTableIncoming(int id,int amount , String category , String isPayed,String payMethod, String note){
       createTableIncoming();//define statment and connection
       
       try {
           c.setAutoCommit(false);//set auto save to false
           String sql = "UPDATE incoming set amount= " + amount 
                   +",category=" + "'"+ category + "'" +
                   ",isPayed= " + "'" + isPayed + "'"
                   +", payMethod= '" + payMethod + "'"
                   
                   + ",note= " + "'"+ note + "'"
                           + "WHERE id=" + id + ";"
                   ;//edit the row for incoming table (JTable)
           stmt.executeUpdate(sql);//execute the sql string
           c.commit();//save
       }
           
       catch (Exception e){
           e.printStackTrace();
       }
       
       finally {//just to make sure
           
           try {
               stmt.close();//close
               c.close();//close
           }
           
           catch (SQLException e){
               e.printStackTrace();
           }
       }
       
       System.out.println("Editing table incoming succesfully with id = " + id);
   }
   
   //edit spectified row in outgoing table (Database)
   public static void editTableOutgoing(int id,int amount,String reason , String note){
    createTableOutgoing();// define statment and connection
    
    try {
        c.setAutoCommit(false);//set auto save to false
        
        String sql = "UPDATE outgoing set amount=" + amount 
                +", reason=" + "'" + reason +"'" 
                + ",note =" + "'" + note + "'"
                + "WHERE id=" + id +";";//edit the row for incoming table (JTable)
                
        stmt.executeUpdate(sql);//exceute the sql string
        c.commit();//save
        
    }
        catch (Exception e){
            e.printStackTrace();
        }    
       
    finally {//just to make sure
        try {
            stmt.close();//close
            c.close();//close
        }
        
        catch (SQLException e){
            e.printStackTrace();
        }
    }
   }
   
   //delete spectfied row from incoming table
   public static void deleteRowTableIncoming(int id){
       createTableIncoming();//define statment and connection
       
       try {
           c.setAutoCommit(false);//set auto save to false
           String sql = "DELETE FROM incoming WHERE id =" + id + ";";//delete the row 
           stmt.executeUpdate(sql);//execute sql string
           c.commit();//save
           
       }
       
       catch (SQLException e){
           e.printStackTrace();
       }
       
       finally {//just to make sure
           try {
               stmt.close();//close
               c.close();//close
           }
           
           catch (SQLException e){
               e.printStackTrace();
           }
       }
   }
   
   //delete spectfied row from outgoing table
   public static void deleteRowTableOutgoing(int id){
       createTableOutgoing();//define statment and connection
       
       try {
           c.setAutoCommit(false);//set auto save to false
          String sql = "DELETE FROM outgoing WHERE id =" + id + ";"; //delete the row
          stmt.executeUpdate(sql);//execute sql string
          c.commit();//save
       }
       
       catch(SQLException e){
           e.printStackTrace();
       }
       
       finally {//just to make sure
        try {
            stmt.close();//close
            c.close();//close
        }   
        
        catch(SQLException e){
            e.printStackTrace();
        }
       }
   }
   
   //get the count of days for incoming table
   public static int getTotalDaysIncoming(){
       createTableIncoming();//define statment and connection
       ResultSet result = null;
       
       int daysCount = 1;//1 by default
       ArrayList<String> dates = new ArrayList<String>();//a list of dates
       try {
           String sql = "SELECT date FROM incoming;";//get the dates from incoming table (data base)
           
            result = stmt.executeQuery(sql);//execute the query
          
           while(result.next()){   
               dates.add(result.getString("date"));//add the date to the list of dates
            
           }
           
       }
       catch(SQLException e){
           
       }
       
       finally {
           try {
               result.close();//close
               stmt.close();//close
               c.close();//close
           }
           
           catch(SQLException e){
               e.printStackTrace();
           }
       }
       //now the list of dates is complete
       
       
       
       for(int i = 1; i < dates.size();i++){
           
           if (!dates.get(i).equals(dates.get(i-1))){//now compare the current date with previous date
               //if it's true it will increse days count
               
               daysCount++;
           }
       }
       
       
       
       return daysCount;
   }
   
   //get the total for all rows that it's pay method is cash
   public static int getTotalCashIncoming(){
       createTableIncoming();//define connection, and statment
       ResultSet result = null;
       int total = 0;
       try {
           String sql = "SELECT amount FROM incoming WHERE payMethod =" + "'" + "كاش" + "'" + ";";//get the amount where the pay method is cash
           result = stmt.executeQuery(sql);//execute the query
           
                   while(result.next()){
                       int amount = result.getInt("amount");//get the amount
                       total += amount;//add it to the total
                       
                   }
       }
       
       catch(SQLException e){
           e.printStackTrace();
       }
       
       finally {
           try {
               result.close();//close
               stmt.close();//close
               c.close();//close
           }
           catch(SQLException e){
               e.printStackTrace();
           }
                   
       }
       
       return total;
               
   }
   
   //get the total for all rows that it's pay method is network
   public static int getTotalNetworkIncoming(){
       createTableIncoming();//define connection , and statment
       ResultSet result = null;
       int total = 0;
       try {
           String sql = "SELECT amount FROM incoming WHERE payMethod = " + "'" + "شبكة" +"'" +";";//get the amount where the pay method is network
           result = stmt.executeQuery(sql);//execute the query
           
           while(result.next()){
               int amount = result.getInt("amount");//get the amount
               total += amount;//add the amount to the total
           }
       }
       
       catch(SQLException e){
           e.printStackTrace();
       }
       
       
       finally {//just to make sure
           try {
               result.close();//close
               stmt.close();//close
               c.close();//close
           }
           
           catch(SQLException e){
               e.printStackTrace();
           }
       }
       
       return total;
   }
   
  
}
    

