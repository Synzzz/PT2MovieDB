/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * An instance of a movie
 * @author Ati
 */
public class Movie {
    private String title;
    private String director;
    private int year;
    private boolean lent;
    private int lentCount;
    private boolean original;
    private String storageType;
    private int length;
    private String mainCast;
    private File image;
    private String filePath;

    /**
     *
     * @param title
     * @param director
     * @param year
     * @param lent
     * @param lentCount
     * @param original
     * @param storageType
     * @param length
     * @param mainCast
     * @param file
     */
    public Movie(String title, String director, int year, boolean lent, int lentCount, boolean original, String storageType, int length, String mainCast,File file) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.lent = lent;
        this.lentCount = lentCount;
        this.original = original;
        this.storageType = storageType;
        this.length = length;
        this.mainCast =mainCast;
        this.image=file;
        this.filePath=image.getAbsolutePath();
    }
    
    @Override
    public boolean equals(Object o){
        Movie m= (Movie)o;
        return(this.getTitle().equals(m.getTitle()) && this.getDirector().equals(m.getDirector()) );
    }
    
    /**
     * Returns true if the movie already exists in the database
     * @return
     * @throws FileNotFoundException
     */
    public boolean persist() throws FileNotFoundException {
        try {
            Connection conn = ConnectionFactory.getConnection();
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO movies VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, title);
            ps.setString(2, director);
            ps.setInt(3,year);
            ps.setBoolean(4,lent);
            ps.setInt(5,lentCount);
            ps.setBoolean(6,original);
            ps.setString(7, storageType);
            ps.setInt(8,length);
            ps.setString(9,mainCast);
            ps.setBinaryStream(10, fis, (int) file.length());
            return((ps.executeUpdate()==0) ? true:false);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return true;
    }
    
    /**
     * Returns true if the newly edited movie data would conflict with one in the database
     * @return
     * @throws FileNotFoundException
     */
    public boolean editNotAvailable(String origTitle,String origDirector ) throws FileNotFoundException {
        try {
            Connection conn = ConnectionFactory.getConnection();
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            
            PreparedStatement ps = conn.prepareStatement("UPDATE movies SET title=?,director=?,prodYear=?,lent=?,lentCount=?,original=?,storageType=?,movieLength=?,mainCast=?,img=? WHERE title='"+origTitle+"' AND director='"+origDirector+"';");
            ps.setString(1, title);
            ps.setString(2, director);
            ps.setInt(3,year);
            ps.setBoolean(4,lent);
            ps.setInt(5,lentCount);
            ps.setBoolean(6,original);
            ps.setString(7, storageType);
            ps.setInt(8,length);
            ps.setString(9,mainCast);
            ps.setBinaryStream(10, fis, (int) file.length());
            return((ps.executeUpdate()==0) ? true:false);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            
        }
        return true;
    }
    
    
    public void editMovie(String title, String director, int year, boolean lent, int lentCount, boolean original, String storageType, int length, String mainCast,File file) throws FileNotFoundException {
        Movie updatedMovie=new Movie(title,director,year,lent,lentCount,original,storageType,length,mainCast,file);
        if(!updatedMovie.editNotAvailable(this.title,this.director)){
            this.setTitle(title);
            this.setDirector(director);
            this.setYear(year);
            this.setLent(lent);
            this.setLentCount(lentCount);
            this.setOriginal(original);
            this.setStorageType(storageType);
            this.setLength(length);
            this.setMainCast(mainCast);
            this.setImage(file);  
        }
        else{
            throw new IllegalArgumentException();
        } 
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public File getImage() {
        return image;
    }

    /**
     * Setter for the object data 
     * @param image
     */
    public void setImage(File image) {
        this.image = image;
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
    public int getYear() {
        return year;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public boolean isLent() {
        return lent;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public int getLentCount() {
        return lentCount;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public boolean isOriginal() {
        return original;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getStorageType() {
        return storageType;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * Getter for the object data 
     * @return
     */
    public String getMainCast() {
        return mainCast;
    }
    
    /**
     * Setter for the object data 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for the object data 
     * @param director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Setter for the object data 
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Setter for the object data 
     * @param lent
     */
    public void setLent(boolean lent) {
        this.lent = lent;
    }

    /**
     * Setter for the object data 
     * @param lentCount
     */
    public void setLentCount(int lentCount) {
        this.lentCount = lentCount;
    }

    /**
     * Setter for the object data 
     * @param original
     */
    public void setOriginal(boolean original) {
        this.original = original;
    }

    /**
     * Setter for the object data 
     * @param storageType
     */
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    /**
     * Setter for the object data 
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Setter for the object data 
     * @param mainCast
     */
    public void setMainCast(String mainCast) {
        this.mainCast = mainCast;
    }

}
