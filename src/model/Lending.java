/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An instance of a lending
 * @author Ati
 */
public class Lending {
    private Movie movie;
    private String title;
    private String director;
    private String name;
    private String startDateString;
    private String endDateString;
    private Date start;
    private Date end;

    /**
     * Constructor for an in-program made lending
     * @param movie
     * @param name
     * @param startDate
     */
    public Lending(Movie movie,String name, Date startDate) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO lendings (name,startDate,title,director) VALUES(?,?,?,?)");
            ps.setString(1,name);
            ps.setDate(2, convertJavaDateToSqlDate(startDate));
            ps.setString(3,movie.getTitle());
            ps.setString(4,movie.getDirector());
            ps.executeUpdate();
            ps = conn.prepareStatement("UPDATE movies SET lent=?,lentCount=? WHERE title=? AND director=?;");
            ps.setBoolean(1,true);
            ps.setInt(2,movie.getLentCount()+1);
            ps.setString(3,movie.getTitle());
            ps.setString(4,movie.getDirector());
            ps.executeUpdate();
            this.movie=movie;
            this.name = name;
            this.title=movie.getTitle();
            this.director=movie.getDirector();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY.MM.dd.");
            cal.setTime(startDate);
            this.startDateString = dateFormat.format(cal.getTime());
            start=startDate;
            this.endDateString=null;
            this.end=null;
            movie.setLent(true);
            movie.setLentCount(movie.getLentCount()+1);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return;
        }
    }

    /**
     * Constructor for a lending read from the database 
     * @param movie
     * @param name
     * @param startDate
     * @param endDate
     */
    public Lending(Movie movie,String name, Date startDate,Date endDate) {
            this.movie=movie;
            this.name = name;
            this.title=movie.getTitle();
            this.director=movie.getDirector();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY.MM.dd.");

            cal.setTime(startDate);
            this.startDateString = dateFormat.format(cal.getTime());
            start=startDate;
            if(endDate!=null){
                cal.setTime(endDate);
                this.endDateString = dateFormat.format(cal.getTime());
                this.end=endDate;
            }
    }

    /**
     * Sets the end date of the lending,sets the movie's lent state to false and uploads these changes to the database
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE lendings SET endDate=? WHERE title=? AND director=? AND startDate=?;");
            ps.setDate(1, convertJavaDateToSqlDate(endDate));
            ps.setString(2,title);
            ps.setString(3,director);
            ps.setDate(4,convertJavaDateToSqlDate(start));
            ps.executeUpdate();
            ps = conn.prepareStatement("UPDATE movies SET lent=? WHERE title=? AND director=?;");
            ps.setBoolean(1,false);
            ps.setString(2,title);
            ps.setString(3,director);
            ps.executeUpdate();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY.MM.dd.");
            cal.setTime(endDate);
            this.endDateString = dateFormat.format(cal.getTime());
            end=endDate;
            movie.setLent(false);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return;
        }
    }
    
    /**
     * Converts a java Date object to a sql Date type
     * @param date
     * @return
     */
    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    
    
    @Override
    public boolean equals(Object o){
        Lending l= (Lending)o;
        return(this.getTitle().equals(l.getTitle()) && this.getDirector().equals(l.getDirector()) &&this.getStartDate().equals(l.getStartDate()) &&this.getName().equals(l.getName()));
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public Date getStart() {
        return start;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getDirector() {
        return director;
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public Movie getMovie() {
        return movie;
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getStartDate() {
        return startDateString;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getEndDate() {
        return endDateString;
    }
}
