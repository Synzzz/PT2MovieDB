package view;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.Movie;
import model.Lending;
import model.MovieTableModel;
import model.LendingTableModel;

/**
 * An instance of the main window with the controls for the tables
 * @author Ati
 */
public class MainWindow extends javax.swing.JFrame {
    private TableRowSorter<TableModel> movieSorter;
    private TableRowSorter<TableModel> lendingSorter;
    private RowFilter<TableModel, Object> notReturnedFilter;
    private RowFilter<TableModel, Object> allLendingsFilter;
    private MovieTableModel movieTableModel;
    private LendingTableModel lendingTableModel;
    private File imageFile;
    private ImageIcon ii;
    private Movie selectedMovie=null;
    private Lending selectedLending=null;
    private String nameSearch;
    
    /**
     * Scales the parameter image to the width and height of the other int parameters
     * @param w
     * @param h
     * @param img
     * @return
     * @throws Exception
     */
    public static BufferedImage scaleImage(int w, int h, BufferedImage img) throws Exception {
        BufferedImage bi;
        bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(img, 0, 0, w, h, null);
        g2d.dispose();
        return bi;
    }
    
    /**
     * Constructor for the main window, initializing the tables,their models and their sorters
     */
    public MainWindow() {
        movieTableModel = new MovieTableModel();
        lendingTableModel = new LendingTableModel(movieTableModel);
        setTitle("Movie database");
        initComponents();
        // Movie sorter 
        movieSorter = new TableRowSorter(movieTableModel);
        movies.setRowSorter(movieSorter);
        keyword.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = keyword.getText();
                if (text.trim().length() != 0) {
                } else {
                    movieSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = keyword.getText();
                if (text.trim().length() == 0) {
                    movieSorter.setRowFilter(null);
                } else {
                    movieSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        });
        
        // Lending sorter
        nameSearch="";
        notReturnedFilter= new RowFilter<TableModel, Object>() {
            @Override
            public boolean include(javax.swing.RowFilter.Entry<? extends TableModel, ? extends Object> entry) {
                
                if(nameSearch.trim().length()==0){
                    return ((String)entry.getValue(2)==null);
                }
                else{
                    Pattern pat=Pattern.compile("(?i)"+nameSearch);
                    Matcher matcher=pat.matcher((String)entry.getValue(0));
                    return ((String)entry.getValue(2)==null && matcher.find());
                }
            
            }
        };
        allLendingsFilter= new RowFilter<TableModel, Object>() {
            @Override
            public boolean include(javax.swing.RowFilter.Entry<? extends TableModel, ? extends Object> entry) {
                if(nameSearch.trim().length()==0){
                    return true;
                }
                else{
                    Pattern pat=Pattern.compile("(?i)"+nameSearch);
                    Matcher matcher=pat.matcher((String)entry.getValue(0));
                    return matcher.find();

                }
            }
        };
        name.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                nameSearch = name.getText();
                if (toggleLending.isSelected()) {      
                    lendingSorter.setRowFilter(notReturnedFilter);
                }
                else{
                    lendingSorter.setRowFilter(allLendingsFilter);
                }
                
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                nameSearch = name.getText();
                nameSearch = name.getText();
                if (toggleLending.isSelected()) {      
                    lendingSorter.setRowFilter(notReturnedFilter);
                }
                else{
                    lendingSorter.setRowFilter(allLendingsFilter);
                }
                
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        });
        
        lendingSorter= new TableRowSorter<TableModel>(lendingTableModel);
        lendingSorter.setRowFilter(null);
        lendings.setRowSorter(lendingSorter);
        
        //Selecting in table for movies
        ListSelectionModel rowMovie = movies.getSelectionModel();
        rowMovie.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow=movies.getSelectedRow();
                if(selectedRow>=0){
                    String value1=(String)movies.getValueAt(selectedRow,0);
                    String value2=(String)movies.getValueAt(selectedRow,1);
                    selectedMovie=movieTableModel.findMovie(value1,value2);
                    imageFile = selectedMovie.getImage();
                    try {
                        //get the image from file chooser and scale it to match JLabel size
                        ImageIcon ii=new ImageIcon(scaleImage(image.getWidth(), image.getHeight(), ImageIO.read(new File(imageFile.getAbsolutePath()))));
                        image.setIcon(ii);
                    } catch (Exception ex) {
                        System.err.println(ex.toString());
                    }
                }
            } 
        });
        
        //Selecting in table for lendings
        ListSelectionModel rowLending = lendings.getSelectionModel();
        rowLending.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow=lendings.getSelectedRow();
                if(selectedRow>=0){
                    String value1=(String)lendings.getValueAt(selectedRow,0);
                    String value2=(String)lendings.getValueAt(selectedRow,1);
                    String value3=(String)lendings.getValueAt(selectedRow,3);
                    String value4=(String)lendings.getValueAt(selectedRow,4);
                    selectedLending=lendingTableModel.findLending(value1,value2,value3,value4);
                }
            } 
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        movies = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        lendings = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        keyword = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        addMovie = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Lend = new javax.swing.JButton();
        panic = new javax.swing.JButton();
        image = new javax.swing.JLabel();
        returnMovie = new javax.swing.JButton();
        toggleLending = new javax.swing.JToggleButton("OFF",false);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        name = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        movies.setModel(movieTableModel);
        movies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        movies.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(movies);

        lendings.setModel(lendingTableModel);
        jScrollPane2.setViewportView(lendings);

        jLabel3.setText("Movies");

        jLabel4.setText("Lendings");

        jLabel5.setText("Keyword:");

        jLabel7.setText("Cover preview:");

        addMovie.setText("Add movie");
        addMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMovieActionPerformed(evt);
            }
        });

        Edit.setText("Edit movie");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Delete.setText("Delete movie");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        Lend.setText("Lend movie");
        Lend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LendActionPerformed(evt);
            }
        });

        panic.setForeground(new java.awt.Color(255, 0, 0));
        panic.setText("PANIC");
        panic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panicActionPerformed(evt);
            }
        });

        returnMovie.setText("Return movie");
        returnMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnMovieActionPerformed(evt);
            }
        });

        toggleLending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleLendingActionPerformed(evt);
            }
        });

        jLabel1.setText("Show only not returned:");

        jLabel2.setText("Name of the lender:");

        jScrollPane3.setViewportView(name);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(201, 201, 201))
                                    .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(44, 44, 44))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Lend, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(returnMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(keyword, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(toggleLending, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane3)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panic, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(220, 220, 220))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addMovie)
                    .addComponent(Edit)
                    .addComponent(Delete)
                    .addComponent(Lend)
                    .addComponent(panic)
                    .addComponent(returnMovie))
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(keyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addComponent(toggleLending, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(image, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void addMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMovieActionPerformed
        // TODO add your handling code here:
        AddMovieDialog addMovie=new AddMovieDialog(this,true,movieTableModel);
    }//GEN-LAST:event_addMovieActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        // TODO add your handling code here:
        if(selectedMovie!=null){
            EditMovieDialog editMovie= new EditMovieDialog(this,true,movieTableModel,selectedMovie);
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a movie first!");
        }
    }//GEN-LAST:event_EditActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        // TODO add your handling code here:
        if(selectedMovie!=null && !selectedMovie.isLent()){
            movieTableModel.deleteMovie(selectedMovie);
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a movie first or select one that is not lent!");
        }
    }//GEN-LAST:event_DeleteActionPerformed

    private void LendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LendActionPerformed
        // TODO add your handling code here:
        if(selectedMovie!=null && selectedMovie.isLent()==false){
            AddLendingDialog newLending=new AddLendingDialog(this,true,selectedMovie,lendingTableModel);
            movieTableModel.reloadTable();
            lendingTableModel.reloadTable();
        }
        else if(selectedMovie==null){
            JOptionPane.showMessageDialog(this, "Please select a movie first!");
        }
        else{
            JOptionPane.showMessageDialog(this, "This movie is lent at the moment");
        }
    }//GEN-LAST:event_LendActionPerformed

    private void panicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_panicActionPerformed
        // TODO add your handling code here:
        lendingTableModel.deletePirated();
        movieTableModel.deletePirated();
    }//GEN-LAST:event_panicActionPerformed

    private void returnMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnMovieActionPerformed
        // TODO add your handling code here:
        if(selectedLending!=null && selectedLending.getEndDate()==null){
            ReturnDialog returning=new ReturnDialog(this,true,selectedLending,lendingTableModel);
            movieTableModel.reloadTable();
            lendingTableModel.reloadTable();
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a lending what you want to return");
        }
    }//GEN-LAST:event_returnMovieActionPerformed

    private void toggleLendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleLendingActionPerformed
        // TODO add your handling code here:
        if (toggleLending.isSelected()) {
            toggleLending.setText("ON");
            lendingSorter.setRowFilter(notReturnedFilter);
            pack();
            lendingTableModel.reloadTable();
        } 
        else {
            toggleLending.setText("OFF"); 
            lendingSorter.setRowFilter(allLendingsFilter);
            pack();
            lendingTableModel.reloadTable();
        }
    }//GEN-LAST:event_toggleLendingActionPerformed
    
    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                //System.out.println(info.getName());
            }
        } catch (Exception ex) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Delete;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Lend;
    private javax.swing.JButton addMovie;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField keyword;
    private javax.swing.JTable lendings;
    private javax.swing.JTable movies;
    private javax.swing.JTextPane name;
    private javax.swing.JButton panic;
    private javax.swing.JButton returnMovie;
    private javax.swing.JToggleButton toggleLending;
    // End of variables declaration//GEN-END:variables
}
