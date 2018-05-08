/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import org.hamcrest.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import model.Movie;
import model.Lending;
import model.MovieTableModel;
import model.LendingTableModel;
/**
 *
 * @author Ati
 */

public class UnitTest {
    MovieTableModel mtm;
    LendingTableModel ltm;
    
    @Before
    public void init(){
        mtm=new MovieTableModel();
        ltm=new LendingTableModel(mtm);
    }
    
    @Test
    public void moviePersistTest() {
        try{
            Movie first=new Movie("The asd","asd mc asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            assertFalse(first.persist());
            mtm.getMovies().add(first);
            Movie second=new Movie("The asd","asd mc asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            assertTrue(second.persist());
            mtm.deleteMovie(first);
        }
        catch(FileNotFoundException e){}
    }
    
    @Test
    public void movieEqualsTest(){
            Movie first=new Movie("The asd","asd mc asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            Movie second=new Movie("The asd","asd mc asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            Movie third=new Movie("The adasdsd","asd mcw asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));

            assertTrue(first.equals(second));
            assertFalse(first.equals(third));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void editTest() {
        try{
            Movie first=new Movie("The asd","asd mc asdface",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            Movie second=new Movie("hmm","hmmm",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            first.persist();
            mtm.getMovies().add(first);
            second.persist();
            mtm.getMovies().add(second);
            first.editMovie("hmm","hmmm",2018,false,0,false,"DVD",2,"Senki",new File("D:\\OneDrive\\Dokumentumok\\PT2MovieDB\\resources\\test.jpg"));
            mtm.deleteMovie(first);
            mtm.deleteMovie(second);
        }
        catch(FileNotFoundException e){}
    }
}
