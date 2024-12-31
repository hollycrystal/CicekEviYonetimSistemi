package application;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;
    
    
    
    @FXML
    private AnchorPane availableFlower_form;

    @FXML
    private ImageView availableFlower_imageView;
    @FXML
    private Button availableFlowers_addBtn;

    @FXML
    private Button availableFlowers_btn;

    @FXML
    private Button availableFlowers_clearBtn;

    @FXML
    private TableColumn<?, ?> availableFlowers_col_flowerID;

    @FXML
    private TableColumn<?, ?> availableFlowers_col_flowerName;

    @FXML
    private TableColumn<?, ?> availableFlowers_col_price;

    @FXML
    private TableColumn<?, ?> availableFlowers_col_status;

    @FXML
    private Button availableFlowers_deleteBtn;

    @FXML
    private TextField availableFlowers_flowerID;

    @FXML
    private TextField availableFlowers_flowerName;

    @FXML
    private Button availableFlowers_importBtn;

    @FXML
    private TextField availableFlowers_price;



    @FXML
    private ComboBox<String> availableFlowers_status;

    @FXML
    private TableView<flowersData> availableFlowers_tableView;

    @FXML
    private Button availableFlowers_updateBtn;
    

    @FXML
    private Button close;
    
    
    

    @FXML
    private Label home_availableFlowers;

    @FXML
    private Button home_btn;

    @FXML
    private BarChart<String, Number> home_chart;


    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalCostumers;

    @FXML
    private Label home_totalIncome;
    
    
    

    @FXML
    private Button logout_btn;

    @FXML
    private Button minimize;
    
    
    

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<customerData, Integer> purchase_col_flowerID;  

    @FXML
    private TableColumn<customerData, String> purchase_col_flowerName;  
    
    @FXML
    private TableColumn<customerData, Integer> purchase_col_quantity;  

    @FXML
    private TableColumn<customerData, Double> purchase_col_price;  

    @FXML
    private TextField purchase_flowerID;

    @FXML
    private TextField purchase_flowerName;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private Button purchase_payBtn;
    
    @FXML
    private Button purchase_addCart;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private Label purchase_total;
    
    

    @FXML
    private Label username;
    
    

    private double x = 0;
    private double y = 0;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;
    
    
    public void homeAF() {
    	
    	String sql = "SELECT COUNT(id) FROM flowers WHERE status= 'Stokta'";
    	
    	connect = database.connect();
    	
    	try {
    		
    		int countAF = 0;
    		statement = connect.createStatement();
    		result = statement.executeQuery(sql);
    		
    		if(result.next()) {
    			
    			countAF = result.getInt("COUNT(id)");
    		}
    		
    		home_availableFlowers.setText(String.valueOf(countAF));
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void homeTI() {
    	
    	String sql = "SELECT SUM(total) FROM customer_info";
    	connect = database.connect();
    	
    	try {
    		
    		int countTI = 0;
    		statement = connect.createStatement();
    		result = statement.executeQuery(sql);
    		
    		
    		if(result.next()) {
    			countTI = result.getInt("SUM(total)");
    		}
    
    		home_totalIncome.setText(String.valueOf(countTI)+"₺");
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    
    
    
    public void homeTC(){
    	
    	String sql = "SELECT COUNT(id) FROM customer_info";
    	connect = database.connect();
    	
    	try {
    		
    		int countTC = 0;
    		statement = connect.createStatement();
    		result = statement.executeQuery(sql);
    		
    		
    		if(result.next()) {
    			countTC = result.getInt("COUNT(id)");
    			
    		}
    		
    		home_totalCostumers.setText(String.valueOf(countTC));
    		
    		
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void homeChart() {
        // Bar Chart bileşenindeki tüm verileri temizle
        home_chart.getData().clear();

        // SQL sorgusu: Günlük toplamları getir
        String sql = "SELECT date, SUM(total) AS total_sum " +
                     "FROM customer_info " +
                     "GROUP BY date " +
                     "ORDER BY TIMESTAMP(date) ASC " +
                     "LIMIT 7";

        connect = database.connect(); // Veritabanına bağlan

        try {
            // Bar Chart için seri oluştur
            XYChart.Series<String, Number> chart = new XYChart.Series<>();
            chart.setName("Günlük Satışlar");

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            // Veritabanı sonuçlarını oku
            while (result.next()) {
                String date = result.getString("date"); // Tarih
                int totalSum = result.getInt("total_sum"); // Günlük toplam

                // Bar Chart serisine veri ekle
                chart.getData().add(new XYChart.Data<>(date, totalSum));
            }

            // Seriyi Bar Chart bileşenine ekle
            home_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace(); // Hataları konsola yazdır
        } finally {
            try {
                // Veritabanı bağlantısını kapat
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }






    public void availableFlowersAdd() {
        String sql = "INSERT INTO flowers(flower_id, name, status, price, image, date) VALUES(?,?,?,?,?,?)";
        connect = database.connect();

        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty() ||
                availableFlowers_flowerName.getText().isEmpty() ||
                availableFlowers_status.getSelectionModel().getSelectedItem() == null ||
                availableFlowers_price.getText().isEmpty() ||
                getData.path == null || getData.path.isEmpty()) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Lütfen tüm boşlukları doldurun.");
                alert.showAndWait();

            } else {
            	
                String checkData = "SELECT flower_id FROM flowers WHERE flower_id = ?";
                PreparedStatement checkStatement = connect.prepareStatement(checkData);
                checkStatement.setString(1, availableFlowers_flowerID.getText());
                result = checkStatement.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hata Mesajı");
                    alert.setHeaderText(null);
                    alert.setContentText("Çiçek no: " + availableFlowers_flowerID.getText() + " zaten mevcut.");
                    alert.showAndWait();

                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, availableFlowers_flowerID.getText());
                    prepare.setString(2, availableFlowers_flowerName.getText());
                    prepare.setString(3, (String) availableFlowers_status.getSelectionModel().getSelectedItem());
                    prepare.setString(4, availableFlowers_price.getText());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(5, uri);

                    Date date = new Date(System.currentTimeMillis());
                    prepare.setDate(6, date);
                    
                    
                    
                    
                    prepare.executeUpdate();
                    
                    
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bilgilendirme Mesajı");
                    alert.setHeaderText(null);
                    alert.setContentText("Çiçek başarıyla eklendi.");
                    alert.showAndWait();
                    
                
                    availableFlowersShowListData();
                    availableFlowersClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void availableFlowersUpdate() {
    	String uri = getData.path;
    	if(!(uri ==null)|| uri == "") {
        	uri = uri.replace("\\","\\\\ ");
    	}
    	
        String sql = "UPDATE flowers SET name = ?, status = ?, price = ?, image = ? WHERE flower_id = ?";
        connect = database.connect();

        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty() ||
                availableFlowers_flowerName.getText().isEmpty() ||
                availableFlowers_status.getSelectionModel().getSelectedItem() == null ||
                availableFlowers_price.getText().isEmpty() ||
                uri == null || uri.isEmpty()) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Lütfen tüm boşlukları doldurun.");
                alert.showAndWait();

            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Onay Mesajı");
                alert.setHeaderText(null);
                alert.setContentText(availableFlowers_flowerID.getText() + "  nolu çiçeğin bilgilerini güncellemek istediğinize emin misiniz?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, availableFlowers_flowerName.getText());
                    prepare.setString(2, (String) availableFlowers_status.getSelectionModel().getSelectedItem());
                    prepare.setString(3, availableFlowers_price.getText());
                    prepare.setString(4, getData.path); // image path
                    prepare.setString(5, availableFlowers_flowerID.getText());

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bilgi Mesajı");
                    alert.setHeaderText(null);
                    alert.setContentText("Başarıyla güncellendi!");
                    alert.showAndWait();

                    availableFlowersShowListData();
                    availableFlowersClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void availableFlowersDelete() {
    	
        String sql = "DELETE FROM flowers WHERE flower_id = ?";

        connect = database.connect();
        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Lütfen geçerli bir Çiçek ID'si girin.");
                alert.showAndWait();
            } else {
                String checkSQL = "SELECT flower_id FROM flowers WHERE flower_id = ?";
                PreparedStatement checkStatement = connect.prepareStatement(checkSQL);
                checkStatement.setInt(1, Integer.parseInt(availableFlowers_flowerID.getText()));
                result = checkStatement.executeQuery();

                if (!result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hata Mesajı");
                    alert.setHeaderText(null);
                    alert.setContentText("Çiçek ID'si bulunamadı.");
                    alert.showAndWait();
                    return; // Exit if flower ID does not exist
                }

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Onay Mesajı");
                alert.setHeaderText(null);
                alert.setContentText(availableFlowers_flowerID.getText() + " çiçek bilgilerini silmek istediğinize emin misiniz?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, Integer.parseInt(availableFlowers_flowerID.getText()));

                    int rowsAffected = prepare.executeUpdate();
                    if (rowsAffected > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Bilgi Mesajı");
                        alert.setHeaderText(null);
                        alert.setContentText("Başarıyla silindi!");
                        alert.showAndWait();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Hata Mesajı");
                        alert.setHeaderText(null);
                        alert.setContentText("Silme işlemi başarısız oldu.");
                        alert.showAndWait();
                    }

                    availableFlowersShowListData();
                    availableFlowersClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }
    
    

    public void availableFlowersClear() {

    	availableFlowers_flowerID.setText("");
    	availableFlowers_flowerName.setText("");
    	availableFlowers_status.getSelectionModel().clearSelection();
    	availableFlowers_price.setText("");
    	getData.path = "";

    	availableFlower_imageView.setImage(null);

    }
    

    
    
    String listStatus[] = {"Stokta","Tükenmiş"};

    public void availableFlowersStatus() {
    	List<String> listS = new ArrayList<>();

    	for(String data : listStatus) {
    		listS.add(data);
    	}

    	ObservableList listData = FXCollections.observableArrayList(listS);

    	availableFlowers_status.setItems(listData);

    }


    public void availableFlowersInsertImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Fotoğraf aç.");

        open.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg", "*.bmp", "*.gif"));

        File defaultDirectory = new File(System.getProperty("user.home") + "/Downloads"); 
        if (defaultDirectory.exists()) {
            open.setInitialDirectory(defaultDirectory);
        }

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 141, 152, false, true);
            availableFlower_imageView.setImage(image);
        }
    }








    public ObservableList<flowersData> availableFlowersListData() {

    	ObservableList<flowersData> listData = FXCollections.observableArrayList();
    	String sql = "SELECT * FROM flowers";

    	connect = database.connect();


    	try {
    		prepare = connect.prepareStatement(sql);
    		result = prepare.executeQuery();

    		flowersData flower;

    		while(result.next()) {
    			flower = new flowersData(result.getInt("flower_id"),
    					result.getString("name"),
    					result.getString("status"),
    					result.getDouble("price"),
    					result.getString("image"),
    					result.getDate("date"));
    			listData.add(flower);
    		}


    	}catch(Exception e) {e.printStackTrace();}
    	return listData;
    }


    private ObservableList<flowersData> availableFlowersList;


    public void availableFlowersShowListData() {
        availableFlowersList = availableFlowersListData();
        
        availableFlowers_col_flowerID.setCellValueFactory(new PropertyValueFactory<>("flowerId"));
        availableFlowers_col_flowerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableFlowers_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableFlowers_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        availableFlowers_tableView.setItems(availableFlowersList); 
    }


    public void availableFlowersSelect() {
        
        flowersData flower = availableFlowers_tableView.getSelectionModel().getSelectedItem();
        int num = availableFlowers_tableView.getSelectionModel().getSelectedIndex();

        
        if (flower != null && num >= 0) {
            availableFlowers_flowerID.setText(String.valueOf(flower.getFlowerId()));  
            availableFlowers_flowerName.setText(flower.getName());  
            availableFlowers_price.setText(String.valueOf(flower.getPrice())); 
            
            availableFlowers_status.getSelectionModel().select(flower.getStatus());  

           
            getData.path = flower.getImage();  
            String uri = "file:" + flower.getImage();  
            image = new Image(uri, 116, 139, false, true);  
            availableFlower_imageView.setImage(image);  
        }
    }



    
    public void purchaseAddtoCart() {
        purchaseCustomerId();

        String sql = "INSERT INTO customer(customer_id, flower_id, name, quantity, price, date) VALUES (?, ?, ?, ?, ?, ?)";
        connect = database.connect();

        try {
            Alert alert;

            if (purchase_flowerID.getText() == null || purchase_flowerID.getText().isEmpty() ||
                purchase_flowerName.getText() == null || purchase_flowerName.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Lütfen önce çiçeği seçin.");
                alert.showAndWait();
                return; 
            }

            double priceData = 0;
            double totalPrice = 0;

            
            String checkPriceAndStatus = "SELECT price, status FROM flowers WHERE name = ?";
            prepare = connect.prepareStatement(checkPriceAndStatus);
            prepare.setString(1, purchase_flowerName.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                String status = result.getString("status");

                
                if (status.equals("Tükenmiş")) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hata Mesajı");
                    alert.setHeaderText(null);
                    alert.setContentText("Seçtiğiniz çiçek tükenmiş.");
                    alert.showAndWait();
                    return; 
                }

                
                priceData = result.getDouble("price");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Çiçek veritabanında bulunamadı.");
                alert.showAndWait();
                return; 
            }

            int qty = purchase_quantity.getValue();
            totalPrice = priceData * qty;

            
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, customerId);
            prepare.setInt(2, Integer.parseInt(purchase_flowerID.getText()));
            prepare.setString(3, purchase_flowerName.getText());
            prepare.setInt(4, qty);
            prepare.setDouble(5, totalPrice);

            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setDate(6, sqlDate);

            prepare.executeUpdate();
            
            
            purchaseShowListData();
            purchaseDisplayTotal();
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
    public void purchaseClearTable() {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        connect = database.connect();

        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Onay Mesajı");
            alert.setHeaderText(null);
            alert.setContentText("Tablodaki tüm verileri temizlemek istediğinize emin misiniz?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                
                prepare = connect.prepareStatement(sql);
                prepare.setInt(1, customerId); 
                prepare.executeUpdate();

                
                purchaseShowListData();
                purchase_total.setText("0₺");

                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Bilgilendirme Mesajı");
                alert.setHeaderText(null);
                alert.setContentText("Tablo başarıyla temizlendi.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    


    
     public void purchasePay() {
    	 String sql = "INSERT into customer_info(customer_id,total,date) VALUES(?,?,?)";
    	 connect = database.connect();
    	 
    	 try {
    		 
    		 Alert alert;
    		 
    		 if(totalP == 0) {
    			 alert = new Alert(AlertType.ERROR);
    			 alert.setTitle("Hata Mesajı");
    			 alert.setHeaderText(null);
    			 alert.setContentText("Bir şeyler yanlış.");
    			 alert.showAndWait();
    		 }else {
    			 alert = new Alert(AlertType.CONFIRMATION);
    			 alert.setTitle("Onay Mesajı");
    			 alert.setHeaderText(null);
    			 alert.setContentText("Ödeme alınsın mı?");
    			 Optional<ButtonType> option = alert.showAndWait();
    			 
    			 if(option.get().equals(ButtonType.OK)) {
    				 
    				 
    				 prepare = connect.prepareStatement(sql);
            		 prepare.setString(1, String.valueOf(customerId));
            		 prepare.setString(2, String.valueOf(totalP));
            		 
            		 java.util.Date date = new java.util.Date();
                     java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                     prepare.setString(3, String.valueOf(sqlDate));
                     
                     prepare.executeUpdate();
                     
                     
                     alert = new Alert(AlertType.INFORMATION);
        			 alert.setTitle("Bilgilendirme Mesajı");
        			 alert.setHeaderText(null);
        			 alert.setContentText("Ödeme tamamlandı.");
        			 alert.showAndWait();
                     
                     totalP = 0; 
                     
                     
                     
                     
                     
                     
                    home_form.setVisible(true);
             		availableFlower_form.setVisible(false);
             		purchase_form.setVisible(false);
             		
             		
             		
             		homeAF();
                    homeTI();
                    homeTC();
                    homeChart();
                     
    			 }
 
    		 }

    	 }catch(Exception e) {
    		 e.printStackTrace();
    	 }
     }
    
    
     
     
     
     private double totalP = 0;
     
     public void purchaseDisplayTotal() {
	purchaseCustomerId();
	String sql = "SELECT SUM(price) FROM customer WHERE customer_id = ' " +
	customerId + "'";
	
	connect = database.connect();
	
	try {
		
		prepare = connect.prepareStatement(sql);
		result = prepare.executeQuery();
		
		if(result.next()) {
			totalP = result.getDouble("SUM(price)");
			
		}
		
		purchase_total.setText(String.valueOf(totalP)+"₺");
		
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
}
    
   
    
    
    
  
    
    
     public void purchaseFlowerId() {
        String sql = "SELECT flower_id FROM flowers WHERE status = ?";
        connect = database.connect(); 

        try {
            ObservableList<String> listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "Stokta"); // 
            result = prepare.executeQuery();

            
            while (result.next()) {
                listData.add(String.valueOf(result.getInt("flower_id")));
            }

            if (!listData.isEmpty()) {
                purchase_flowerID.setText(listData.get(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }





    public void purchaseFlowerName() {
        String sql = "SELECT name FROM flowers WHERE flower_id = ?"; 
        connect = database.connect(); 

        try {
            ObservableList<String> listData = FXCollections.observableArrayList();

            String text = purchase_flowerID.getText(); 
            if (text != null && !text.isEmpty()) { 
                try {
                    Integer selectedFlowerId = Integer.parseInt(text); 

                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, selectedFlowerId); 
                    result = prepare.executeQuery();

                    while (result.next()) {
                        listData.add(result.getString("name"));
                    }

                    if (!listData.isEmpty()) {
                        purchase_flowerName.setText(listData.get(0)); 
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz giriş: " + text);
                    purchase_flowerName.setText(""); 
                }
            } else {
                System.out.println("Boş ID girişi");
                purchase_flowerName.setText(""); 
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }




 
private SpinnerValueFactory<Integer> spinner;
public void purchaseSpinner() {
	spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10,1);
	purchase_quantity.setValueFactory(spinner);
	
}

    
    
    
    
public ObservableList<customerData> purchaseListData() {
    ObservableList<customerData> listData = FXCollections.observableArrayList();

    purchaseCustomerId();

    String sql = "SELECT * FROM customer WHERE customer_id = ?";
    connect = database.connect();

    try {
        prepare = connect.prepareStatement(sql);
        prepare.setInt(1, customerId);  
        result = prepare.executeQuery();

        customerData customer;
        
        while (result.next()) {
            customer = new customerData(
                    result.getInt("customer_id"),
                    result.getInt("flower_id"),
                    result.getString("name"),
                    result.getInt("quantity"),
                    result.getDouble("price"),
                    result.getDate("date")
            );
            listData.add(customer);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return listData;
}

private ObservableList<customerData> purchaseListD;

public void purchaseShowListData() {
    purchaseListD = purchaseListData();

    purchase_col_flowerID.setCellValueFactory(new PropertyValueFactory<>("flowerID"));
    purchase_col_flowerName.setCellValueFactory(new PropertyValueFactory<>("name"));
    purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

    purchase_tableView.setItems(purchaseListD);
}





    private int customerId;
    public void purchaseCustomerId() {
    	
    	String sql = "SELECT MAX(customer_id) FROM customer";
    	connect = database.connect();
    	
    	try {
    		
    		prepare = connect.prepareStatement(sql);
    		result = prepare.executeQuery();
    		
    		if(result.next()) {
    			customerId = result.getInt(1); 

    			
    		}
    		int countData = 0;
    		String checkInfo = "SELECT MAX(customer_id) FROM customer_info";
    		
    		
    		prepare = connect.prepareStatement(checkInfo);
    		result = prepare.executeQuery();
    		
    		if(result.next()) {
    			countData = result.getInt("MAX(customer_id)");
    			
    			if(customerId == 0) {
    				customerId +=1;
    			}else if(customerId== countData) {
    				customerId = countData +1;
    			}	
    		}

    	}catch(Exception e ) {
    		e.printStackTrace();
    	}
    	
    }
    
    
    private int qty;
    public void purchaseQuantity() {
    	qty = purchase_quantity.getValue();
    	
    	
    } 

    
    public void displayUsername() {
        String user = getData.path;
        if (user == null || user.isEmpty()) {
            username.setText("Kullanıcı");
        } else {
            username.setText(user.substring(0, 1).toUpperCase() + user.substring(1) + "!");
        }
    }



    public void switchForm(ActionEvent event) {



    	if(event.getSource()== home_btn) {
    		home_form.setVisible(true);
    		availableFlower_form.setVisible(false);
    		purchase_form.setVisible(false);
    		
    		home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #df59a7, #e475b4)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");
    		
    		homeAF();
        	homeTI();
        	homeTC();
        	homeChart();
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	        	


    	}else if(event.getSource() == availableFlowers_btn) {
    		home_form.setVisible(false);
    		availableFlower_form.setVisible(true);
    		purchase_form.setVisible(false);
    		
    		availableFlowers_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #df59a7, #e475b4)");
            home_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");


        	availableFlowersShowListData();
        	availableFlowersStatus();


    	}else if(event.getSource() == purchase_btn) {
    		home_form.setVisible(false);
    		availableFlower_form.setVisible(false);
    		purchase_form.setVisible(true);
    		
    		purchase_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #df59a7, #e475b4)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
    		
    		purchaseShowListData();
    		purchaseFlowerId();
    		purchaseFlowerName();
    		purchaseSpinner();
    		purchaseDisplayTotal();


    	}
    }




    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Onay Mesajı");
            alert.setHeaderText(null);
            alert.setContentText("Çıkış yapmak istediğine emin misin?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                Stage currentStage = (Stage) logout_btn.getScene().getWindow();
                currentStage.close();

                Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(new Scene(root));
                stage.show();

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





     public void close() {
        System.exit(0);
    }




    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	
    	
    	
    	home_form.setVisible(true);
 		availableFlower_form.setVisible(false);
 		purchase_form.setVisible(false);


    	displayUsername();
    	
    	homeAF();
    	homeTI();
    	homeTC();
    	homeChart();
    	
    	
    	availableFlowersShowListData();
    	availableFlowersStatus();
    	
    	
    	purchaseShowListData();
    	purchaseFlowerId();
		purchaseFlowerName();
		purchaseSpinner();
        purchaseDisplayTotal();

		
        availableFlowers_tableView.setOnMouseClicked(event -> availableFlowersSelect());


    }
}