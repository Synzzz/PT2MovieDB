package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Table model for the lendings
 * @author Ati
 */
public class LendingTableModel extends AbstractTableModel{
    private final ArrayList<Lending> lendings;
    private MovieTableModel movieModel;
    
    /**
     * Constructor for the model
     * @param movieModel
     */
    public LendingTableModel(MovieTableModel movieModel){
        lendings = new ArrayList<>();
        this.movieModel=movieModel;
        initLoadTable();
    }

    /**
     * Initializes the lendings table,getting the informations from the database and saving them into the lendings arraylist
     */
    public void initLoadTable(){
        lendings.clear();
        try {
            Connection conn = ConnectionFactory.getConnection();
            try (Statement stmt = conn.createStatement()) {
                String str="select name,startDate,endDate,title,director from lendings ";
                ResultSet rs= stmt.executeQuery(str);
                while(rs.next()){
                    String name=rs.getString("name");
                    Date startDate=rs.getDate("startDate");
                    Date endDate=rs.getDate("endDate");
                    String title=rs.getString("title");
                    String director=rs.getString("director");
                    Movie m=movieModel.findMovie(title,director);
                    lendings.add(new Lending(m,name,startDate,endDate));
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
     * Returns the lending with the data given or null if not found
     * @param name
     * @param date
     * @param title
     * @param director
     * @return
     */
    public Lending findLending(String name,String date,String title,String director){
        for(Lending l: lendings){
            if(l.getTitle().equals(title) && l.getDirector().equals(director) && l.getName().equals(name) &&l.getStartDate().equals(date)){
                return l;
            }
        }
        return null;
    }
    
    /**
     * Reloads the lending table
     */
    public void reloadTable(){
        fireTableDataChanged();
    }
    
    /**
     * Iterates over the lendings arraylist and removes the one from the parameter (from the database too)
     * @param lend
     */
    public void deleteLending(Lending lend){
        try {
            Iterator<Lending> iterator=lendings.iterator();
            while(iterator.hasNext()){
                Lending l=iterator.next();
                if (l.equals(lend)){
                    deleteLendingSQL(l);
                    iterator.remove();
                    reloadTable();
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Removes the parameter lending from the database
     * @param lend
     */
    public void deleteLendingSQL(Lending lend){
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM lendings WHERE (title=? AND director=? AND startDate=? AND name=?);");
            ps.setString(1, lend.getTitle());
            ps.setString(2, lend.getDirector());
            ps.setDate(3, lend.convertJavaDateToSqlDate(lend.getStart()));
            ps.setString(4,lend.getName());
            ps.executeUpdate();

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Getter for the object data 
     * @return
     */
    public ArrayList<Lending> getLendings() {
        return lendings;
    }
    @Override
    public int getRowCount() {
        return lendings.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int i) {
        String colNames[] = new String[]{"Name","Lending date","Return date","Title","Director"};
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
            case 0: return lendings.get(row).getName();
            case 1: return lendings.get(row).getStartDate();
            case 2: return lendings.get(row).getEndDate();
            case 3: return lendings.get(row).getTitle();
            case 4: return lendings.get(row).getDirector();
            default: return null;
        }        
    }

    @Override
    public void setValueAt(Object o, int row, int column) {}

}
