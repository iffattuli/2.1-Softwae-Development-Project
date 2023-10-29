/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopupdemo;

import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
public class BuyerHomepage extends javax.swing.JFrame {

    /**
     * Creates new form BuyerHomepage
     */

    
    private Connection conn;
    private DefaultTableModel dTableModel,dTableModel2;
    private String selectedPath;
    
    public BuyerHomepage() {
        initComponents();
        this.setLocationRelativeTo(null);//center form in the screen
        LoadPromotionTable();
    }
    
    class CellRenderer implements TableCellRenderer {
 
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
 
            TableColumn tb = ProductTable.getColumn("Image");
            tb.setMaxWidth(60);
            tb.setMinWidth(60);
 
            ProductTable.setRowHeight(60);
 
            return (Component) value;
        }         
    }
    
    public void ConnectDB(){
        DBConnector db = new DBConnector();
        conn = db.openConnection();
    }
    
    public void ClearDataTable(){
        dTableModel = (DefaultTableModel)  ProductTable.getModel();
        int rowCount = dTableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dTableModel.setValueAt(0,i,6);
            dTableModel.removeRow(i);
        }
    }
    
    public void AddtoCart(){
        
        ArrayList<String> numdata = new ArrayList<String>();
        
        //DefaultTableModel dTableModel2 = new DefaultTableModel();
        dTableModel = (DefaultTableModel)  ProductTable.getModel();
        
        for (int i = 0; i < dTableModel.getRowCount(); i++) {
            int j = i;
            Boolean isChecked = Boolean.valueOf(dTableModel.getValueAt(i, 6).toString());
            
            //System.out.println("checked");
            String ProductName, ProductCode, ProductDescription, ImagePath="";
            int ProductPrice, ProductId, Qtn;
           

            if (isChecked) {
               //get the values of the columns you need.
               //System.out.println("checked all");
               //numdata.add(dTableModel.getValueAt(i, 5).toString());
               ProductId   = Integer.parseInt(dTableModel.getValueAt(i, 0).toString());
               ProductName = dTableModel.getValueAt(i, 1).toString();
               ProductCode = dTableModel.getValueAt(i, 2).toString();
               ProductPrice = Integer.parseInt(dTableModel.getValueAt(i, 3).toString());
               ProductDescription = dTableModel.getValueAt(i, 4).toString();
               //ImagePath = dTableModel.getValueAt(i, 7).toString();
               
               if(dTableModel.getValueAt(i, 5).toString()!= null)
                   Qtn = Integer.parseInt(dTableModel.getValueAt(i, 5).toString());
               else
                   Qtn = 0;
               
               
               System.out.printf("\n"+ProductId+"\n"+ProductName+"\n"+ProductCode+"\n"+ProductPrice+"\n"+ProductDescription+"\n"+Qtn+"\n");
               
               try{
                   ConnectDB();
                   Statement st = (Statement) conn.createStatement();
                   
                   String query1 = "SELECT ImagePath FROM ProductInfo where ProductId = "+ProductId;
                   ResultSet rs = st.executeQuery(query1);
                   while(rs.next()){
                       ImagePath = rs.getString("ImagePath");
                       
                       //ImagePath = "+"+ImagePath+"+";
                       
                       System.out.printf("\n"+ProductId+"\n"+ProductName+"\n"+ProductCode+"\n"+ProductPrice+"\n"+ProductDescription+"\n"+Qtn+"\n"+ImagePath+"\n");
                   
                   }
                   
                   String query = "INSERT INTO cart(ProductId, ProductName, ProductCode, ProductPrice, ProductDescription, Qtn, ImagePath) " 
                                      + "VALUES ("+ProductId+", '"+ProductName+"', '"+ProductCode+"', "+ProductPrice+", '"+ProductDescription+"', "+Qtn+", '"+ImagePath+"')";
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
        System.out.println(numdata); 
        numdata = null;
    }
    
    public void LoadProductTable(int ProductType, String ItemName){
        try{
            ConnectDB();
            ClearDataTable();
            
            String ProductName, ProductCode, ProductDescription, ImageName, query; 
            int ProductPrice, ProductId, Qtn = 0;
            
            if(ItemName == null && ProductType > 0){
                query = "SELECT * FROM ProductInfo where ProductType = "+ProductType;
            }                
            else{
                query = "SELECT * FROM ProductInfo where ProductName = '"+ItemName+"'";
            }
             
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
              ImageName = rs.getString("ImagePath");
              ImagePath = ImagePath+ImageName;
              
              ProductTable.getColumn("Image").setCellRenderer(new CellRenderer());  
              JLabel imageLabel = new JLabel();
              ImageIcon imageicon = new ImageIcon(ImagePath);              
              Image img = imageicon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
              imageLabel.setIcon(new ImageIcon(img));
              
              
              String DbTable [] = {ProductId+"",ProductName,ProductCode,ProductPrice+"",ProductDescription};
              //DefaultTableModel dTableModel = (DefaultTableModel) jTable1.getModel();
              dTableModel.addRow(new Object[]{ProductId+"",ProductName,ProductCode,ProductPrice+"",ProductDescription, Qtn,false,imageLabel});
            }
            st.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }  
    }
    
    public void LoadPromotionTable(){
        try{
            ConnectDB();
            ClearDataTable();
            
            String ProductName, PromoCode, query; 
            int Price;
            
            query = "SELECT * FROM Promotion ";
                         
            Statement st = (Statement) conn.createStatement();
             
            ResultSet rs = st.executeQuery(query);
      
            // iterate through the java resultset
            while (rs.next())
            {
              ProductName = rs.getString("ProductName");
              PromoCode = rs.getString("PromoCode");
              Price = rs.getInt("DiscountPrice");
              
              dTableModel2 = (DefaultTableModel) PromotionTable.getModel();
              dTableModel2.addRow(new Object[]{PromoCode,Price+""});
            }
            st.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage()); 
        }  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jSearchButton = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        CartButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButtonGoToCart = new javax.swing.JButton();
        jSearchTextField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        FoodButton = new javax.swing.JButton();
        DressButton = new javax.swing.JButton();
        BeautyButton = new javax.swing.JButton();
        HomeDecorButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ProductTable = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        PromotionTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));

        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\Jeba Sultana\\3D Objects\\icons8_Shopping_Bag_48px.png")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ShopUp Shopping");

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jSearchButton.setBackground(new java.awt.Color(255, 255, 255));
        jSearchButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jSearchButton.setForeground(new java.awt.Color(102, 0, 51));
        jSearchButton.setText("Search");
        jSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSearchButtonActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 255, 255));
        jButton9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton9.setForeground(new java.awt.Color(102, 0, 51));
        jButton9.setText("View Order");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        CartButton.setBackground(new java.awt.Color(255, 255, 255));
        CartButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        CartButton.setForeground(new java.awt.Color(102, 0, 51));
        CartButton.setText("Cart");
        CartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartButtonActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Jeba Sultana\\3D Objects\\icons8_OpenCart_24px.png")); // NOI18N

        jButtonGoToCart.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButtonGoToCart.setForeground(new java.awt.Color(102, 0, 51));
        jButtonGoToCart.setText("Go To Cart");
        jButtonGoToCart.setToolTipText("");
        jButtonGoToCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToCartActionPerformed(evt);
            }
        });

        jSearchTextField.setBackground(new java.awt.Color(0, 102, 102));
        jSearchTextField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jSearchTextField.setForeground(new java.awt.Color(102, 0, 51));

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Logout");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jSearchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonGoToCart)
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CartButton)
                .addGap(20, 20, 20)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CartButton)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSearchButton)
                            .addComponent(jButton9)
                            .addComponent(jButtonGoToCart))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Categories");

        FoodButton.setBackground(new java.awt.Color(0, 102, 102));
        FoodButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        FoodButton.setForeground(new java.awt.Color(255, 255, 255));
        FoodButton.setText("Food");
        FoodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FoodButtonActionPerformed(evt);
            }
        });

        DressButton.setBackground(new java.awt.Color(0, 102, 102));
        DressButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        DressButton.setForeground(new java.awt.Color(255, 255, 255));
        DressButton.setText("Dress");
        DressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DressButtonActionPerformed(evt);
            }
        });

        BeautyButton.setBackground(new java.awt.Color(0, 102, 102));
        BeautyButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        BeautyButton.setForeground(new java.awt.Color(255, 255, 255));
        BeautyButton.setText("Beauty");
        BeautyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BeautyButtonActionPerformed(evt);
            }
        });

        HomeDecorButton.setBackground(new java.awt.Color(0, 102, 102));
        HomeDecorButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        HomeDecorButton.setForeground(new java.awt.Color(255, 255, 255));
        HomeDecorButton.setText("Home decor");
        HomeDecorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeDecorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BeautyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HomeDecorButton, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(DressButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FoodButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addComponent(FoodButton)
                .addGap(27, 27, 27)
                .addComponent(DressButton)
                .addGap(18, 18, 18)
                .addComponent(HomeDecorButton)
                .addGap(32, 32, 32)
                .addComponent(BeautyButton)
                .addContainerGap(401, Short.MAX_VALUE))
        );

        ProductTable.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ProductTable.setForeground(new java.awt.Color(0, 102, 102));
        ProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Id", "Product Name", "Product Code", "Peoduct Price", "Product Description", "Qtn", "Add to Cart", "Image"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(ProductTable);
        if (ProductTable.getColumnModel().getColumnCount() > 0) {
            ProductTable.getColumnModel().getColumn(0).setResizable(false);
            ProductTable.getColumnModel().getColumn(1).setResizable(false);
            ProductTable.getColumnModel().getColumn(2).setResizable(false);
            ProductTable.getColumnModel().getColumn(3).setResizable(false);
            ProductTable.getColumnModel().getColumn(4).setResizable(false);
            ProductTable.getColumnModel().getColumn(5).setMaxWidth(50);
            ProductTable.getColumnModel().getColumn(6).setMaxWidth(100);
            ProductTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jButton8.setBackground(new java.awt.Color(102, 0, 51));
        jButton8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 102, 102));
        jButton8.setText("Popular on Shopup");

        PromotionTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        PromotionTable.setForeground(new java.awt.Color(0, 102, 102));
        PromotionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Promo Code", "Discount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(PromotionTable);
        if (PromotionTable.getColumnModel().getColumnCount() > 0) {
            PromotionTable.getColumnModel().getColumn(0).setResizable(false);
            PromotionTable.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(0, 102, 102));
        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\Jeba Sultana\\3D Objects\\icons8_Search_36px_1.png")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(57, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)))
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)))
                        .addGap(31, 31, 31))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(128, 128, 128))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FoodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FoodButtonActionPerformed
        // TODO add your handling code here:
        LoadProductTable(1, null);
    }//GEN-LAST:event_FoodButtonActionPerformed

    private void DressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DressButtonActionPerformed
        // TODO add your handling code here:
        LoadProductTable(2, null);
    }//GEN-LAST:event_DressButtonActionPerformed

    private void HomeDecorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeDecorButtonActionPerformed
        // TODO add your handling code here:
        LoadProductTable(3, null);
    }//GEN-LAST:event_HomeDecorButtonActionPerformed

    private void BeautyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BeautyButtonActionPerformed
        // TODO add your handling code here:
        LoadProductTable(4, null);
    }//GEN-LAST:event_BeautyButtonActionPerformed

    private void CartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartButtonActionPerformed
        // TODO add your handling code here:
        AddtoCart();
    }//GEN-LAST:event_CartButtonActionPerformed

    private void jSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSearchButtonActionPerformed
        // TODO add your handling code here:
        String itemName = jSearchTextField.getText();
        LoadProductTable(0, itemName);
    }//GEN-LAST:event_jSearchButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonGoToCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToCartActionPerformed
        // TODO add your handling code here:
        
        addtocart atd = new addtocart();
        atd.show();
        this.hide();
    }//GEN-LAST:event_jButtonGoToCartActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        Cancel c = new Cancel();
        c.show();
        this.hide();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
       
        Login log = new Login();
        log.show();
        this.hide();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(BuyerHomepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuyerHomepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuyerHomepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuyerHomepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BuyerHomepage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BeautyButton;
    private javax.swing.JButton CartButton;
    private javax.swing.JButton DressButton;
    private javax.swing.JButton FoodButton;
    private javax.swing.JButton HomeDecorButton;
    private javax.swing.JTable ProductTable;
    private javax.swing.JTable PromotionTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonGoToCart;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jSearchButton;
    private javax.swing.JTextField jSearchTextField;
    // End of variables declaration//GEN-END:variables
}
