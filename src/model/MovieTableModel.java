package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Table model for the movies
 * @author Ati
 */
public class MovieTableModel extends AbstractTableModel{
    private final ArrayList<Movie> movies;


    /**
     * Constructor for the model
     */
    public MovieTableModel(){
        movies = new ArrayList<>();
        initLoadTable();
    }

    /**
     * Initializes the movies table,getting the informations from the database and saving them into the movies arraylist
     */
    public void initLoadTable(){
        movies.clear();
        try {
            Connection conn = ConnectionFactory.getConnection();
            try (Statement stmt = conn.createStatement()) {
                String str="select title,director,prodYear,lent,lentCount,original,storageType,movieLength,mainCast,img from movies ";
                ResultSet rs= stmt.executeQuery(str);
                while(rs.next()){
                    String title=rs.getString("title");
                    String director=rs.getString("director");
                    int prodYear=rs.getInt("prodYear");
                    boolean lent=rs.getBoolean("lent");     
                    int lentCount=rs.getInt("lentCount");
                    boolean original=rs.getBoolean("original");
                    String storageType=rs.getString("storageType");
                    int movieLength=rs.getInt("movieLength");
                    String mainCast=rs.getString("mainCast");
                    Blob blob = rs.getBlob("img");
                    InputStream inputStream = blob.getBinaryStream();
                    OutputStream outputStream = new FileOutputStream("resources/"+title+director+".jpg");
                    int bytesRead = -1;
                    byte[] buffer = new byte[1];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outputStream.close();
                    System.out.println("File saved");  
                    File image = new File("resources/"+title+director+".jpg");
                                        
                    movies.add(new Movie(title,director,prodYear,lent,lentCount,original,storageType,movieLength,mainCast,image));
                }
                rs.close();
                stmt.close();    
            }
            conn.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }    
        
        fireTableDataChanged();
    }

    /**
     * Reloads the table
     */
    public void reloadTable(){
        fireTableDataChanged();
    }
    
    /**
     * Iterates over the movies arraylist and removes the one from the parameter (from the database too)
     * @param movie
     */
    public void deleteMovie(Movie movie){
        Iterator<Movie> iterator=movies.iterator();
        while(iterator.hasNext()){
            Movie m=iterator.next();
            if (m.equals(movie)){
                deleteMovieSQL(m);
                iterator.remove();
                reloadTable();
                break;
            }
        }
    }
    
    /**
     * Removes the parameter movie from the database
     * @param movie
     */
    public void deleteMovieSQL(Movie movie){
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE (title=? AND director=?);");
                    ps.setString(1, movie.getTitle());
                    ps.setString(2, movie.getDirector());
                    ps.executeUpdate();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     *
     * @param title
     * @param director
     * @return
     */
    public Movie findMovie(String title,String director) {
        for(Movie m: movies){
            if(m.getTitle().equals(title) && m.getDirector().equals(director)){
                return m;
            }
        }
        return null;
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public ArrayList<Movie> getMovies() {
        return movies;
    }
        
    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public String getColumnName(int i) {
        String colNames[] = new String[]{ "Title", "Director", "Production year", "Lent right now","Lend counter","Original","Storage type","Movie length","Main cast"};
        return colNames[i];
    }

    @Override
    public Class getColumnClass(int column){
        for (int row = 0; row < getRowCount(); row++){
            Object o = getValueAt(row, column);
            if (o != null){
                return o.getClass();
            }
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        switch (column){
            case 0: return movies.get(row).getTitle();
            case 1: return movies.get(row).getDirector();
            case 2: return movies.get(row).getYear();
            case 3: return movies.get(row).isLent(); 
            case 4: return movies.get(row).getLentCount(); 
            case 5: return movies.get(row).isOriginal(); 
            case 6: return movies.get(row).getStorageType(); 
            case 7: return movies.get(row).getLength(); 
            case 8: return movies.get(row).getMainCast(); 
            default: return null;
        }        
    }

    @Override
    public void setValueAt(Object o, int row, int column) {}
    
    
   
    
}
