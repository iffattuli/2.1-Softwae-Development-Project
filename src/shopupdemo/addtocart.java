/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopupdemo;

import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Jeba Sultana
 */
public class addtocart extends javax.swing.JFrame {

    /**
     * Creates new form addtocart
     */
    private Connection conn;
    private DefaultTableModel dTableModel;
    private Double totalPrice = 0.0;
    
    
    public addtocart() {
        initComponents();
        this.setLocationRelativeTo(null);//center form in the screen
        
        LoadProductTable();
    }
    
    class CellRenderer implements TableCellRenderer {
 
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
 
            TableColumn tb = jTable1.getColumn("Image");
            tb.setMaxWidth(60);
            tb.setMinWidth(60);
 
            jTable1.setRowHeight(60);
 
            return (Component) value;
        }         
    }
    
    public void ConnectDB(){
        DBConnector db = new DBConnector();
        conn = db.openConnection();
    }
    
    public void DeleteFromDB(){
        try{
            ConnectDB();
            String query = "DELETE FROM Cart";
             
            Statement st = (Statement) conn.createStatement();
             
            st.executeUpdate(query);
            
            conn.close();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }
    }
           
    public void ClearDataTable(){
        int rowCount = dTableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dTableModel.removeRow(i);
        }
        jLabelTotalPrice.setText(0+"");
        DeleteFromDB();
    }
    
    public void LoadProductTable(){
        try{
            ConnectDB();
                        
            String ProductName, ProductCode, ProductDescription, ImageName; 
            int ProductPrice, ProductId, Qtn;
                        
            String query = "SELECT * FROM Cart";
             
            Statement st = (Statement) conn.createStatement();
             
            ResultSet rs = st.executeQuery(query);
      
            // iterate through the java resultset
            while (rs.next())
            {
              String ImagePath = "C:\\Users\\Jeba Sultana\\Documents\\NetBeansProjects\\ShopupDemo\\image\\";
                
              ProductId   = rs.getInt("ProductId");
              ProductName = rs.getString("ProductName");
              ProductCode = rs.getString("ProductCode");
              ProductPrice = rs.getInt("ProductPrice");
              ProductDescription = rs.getString("ProductDescription");
              Qtn   = rs.getInt("Qtn");
              
              ImageName = rs.getString("ImagePath");              
              ImagePath = ImagePath+ImageName;
              
              jTable1.getColumn("Image").setCellRenderer(new CellRenderer());  
              JLabel imageLabel = new JLabel();
              ImageIcon imageicon = new ImageIcon(ImagePath);              
              Image img = imageicon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
              imageLabel.setIcon(new ImageIcon(img));
              
              dTableModel = (DefaultTableModel) jTable1.getModel();
              
              dTableModel.addRow(new Object[]{ProductId+"",ProductName,ProductCode,ProductDescription, ProductPrice+"", Qtn,false,imageLabel});
              
              totalPrice = totalPrice+(ProductPrice*Qtn);
            }
            st.close();
            
            jLabelTotalPrice.setText(totalPrice+"");
        }
        catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }  
    }
    
    public void CheckOut(){
                
        for (int i = 0; i < dTableModel.getRowCount(); i++) {
            int j = i;
            Boolean isChecked = Boolean.valueOf(dTableModel.getValueAt(i, 6).toString());
            
            //System.out.println("checked");
            String ProductName, ProductCode, ProductDescription;
            int  ProductId, Qtn, isNew = 1;
            Double ProductPrice;
            Date PaymentDate, DeliveryDate;

            if (isChecked) {
               //get the values of the columns you need.
               //System.out.println("checked all");
               //numdata.add(dTableModel.getValueAt(i, 5).toString());
               ProductId   = Integer.parseInt(dTableModel.getValueAt(i, 0).toString());              
               ProductPrice = Double.parseDouble(dTableModel.getValueAt(i, 4).toString());
               PaymentDate = new Date();
               
               String sqlPaymentDate = new java.sql.Date(PaymentDate.getTime()).toString();
               
               DeliveryDate = new Date();
               
               String sqlDeliveryDate = new java.sql.Date(DeliveryDate.getTime()).toString();
               
               System.out.printf("\n"+PaymentDate+"\n"+DeliveryDate+"\n");
               
                              
               if(dTableModel.getValueAt(i, 5).toString()!= null)
                   Qtn = Integer.parseInt(dTableModel.getValueAt(i, 5).toString());
               else
                   Qtn = 0;
               
               System.out.printf("\n"+ProductId+"\n"+ProductPrice+"\n"+Qtn+"\n");
               
               try{
                   ConnectDB();
                   Statement st = (Statement) conn.createStatement();
                   
                   String query = "INSERT INTO CheckOut(ProductId, UserId, PaymentId, PaymentDate, DeliveryDate, ProductPrice, Qtn, isNew) " 
                                      + "VALUES ("+ProductId+", "+null+", "+null+", '"+sqlPaymentDate+"', '"+sqlDeliveryDate+"', "+ProductPrice+", "+Qtn+", "+isNew+")";
                   st.executeUpdate(query);
                   
                   System.out.println("Data inserted Successfully");
                
                   conn.close();
                   
               }catch(Exception ex){
                   ex.printStackTrace();
               }
               
           } else {
               System.out.printf("Row %s is not checked \n\n", i);
           }
        }
        //DeleteFromDB();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButtonContinueShoping = new javax.swing.JButton();
        jButtonCheckOut = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotalPrice = new javax.swing.JLabel();
        jButtonClearCart = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Jeba Sultana\\3D Objects\\icons8_Shopping_Cart_48px_2.png")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Shopping Cart");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(452, 600));

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 102, 102));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Id", "Product Name", "Product Code", "Product Description", "Price", "Qtn", "Select", "Image"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(4).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel4.setBackground(new java.awt.Color(102, 0, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shopupdemo/imagescart.png"))); // NOI18N

        jButtonContinueShoping.setBackground(new java.awt.Color(0, 102, 102));
        jButtonContinueShoping.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButtonContinueShoping.setForeground(new java.awt.Color(255, 255, 255));
        jButtonContinueShoping.setText("Continue Shoping");
        jButtonContinueShoping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonContinueShopingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonContinueShoping)
                        .addGap(14, 14, 14)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(149, 149, 149)
                .addComponent(jButtonContinueShoping)
                .addContainerGap())
        );

        jButtonCheckOut.setBackground(new java.awt.Color(0, 102, 102));
        jButtonCheckOut.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButtonCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCheckOut.setText("Check Out");
        jButtonCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckOutActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total: ");

        jLabelTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabelTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotalPrice.setText("0");

        jButtonClearCart.setBackground(new java.awt.Color(0, 102, 102));
        jButtonClearCart.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButtonClearCart.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClearCart.setText("Clear Cart");
        jButtonClearCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearCartActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(0, 102, 102));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shopupdemo/icons8_Go_Back_24px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(796, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                .addGap(24, 24, 24))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel5)
                .addGap(244, 244, 244)
                .addComponent(jButtonClearCart)
                .addGap(35, 35, 35)
                .addComponent(jButtonCheckOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(131, 131, 131)
                .addComponent(jLabelTotalPrice)
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonClearCart)
                            .addComponent(jButtonCheckOut)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTotalPrice))
                        .addContainerGap(103, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(127, 127, 127))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonClearCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearCartActionPerformed
        // TODO add your handling code here:
        ClearDataTable();
    }//GEN-LAST:event_jButtonClearCartActionPerformed

    private void jButtonCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckOutActionPerformed
        // TODO add your handling code here:
        CheckOut();
        checkout co = new checkout();
        co.show();
        this.hide();
    }//GEN-LAST:event_jButtonCheckOutActionPerformed

    private void jButtonContinueShopingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonContinueShopingActionPerformed
        // TODO add your handling code here:
        BuyerHomepage byHm = new BuyerHomepage();
        byHm.show();
        this.hide();
    }//GEN-LAST:event_jButtonContinueShopingActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addtocart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addtocart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addtocart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addtocart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addtocart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCheckOut;
    private javax.swing.JButton jButtonClearCart;
    private javax.swing.JButton jButtonContinueShoping;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelTotalPrice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
