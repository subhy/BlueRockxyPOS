package lk.ijse.dep.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.pos.business.BOFactory;
import lk.ijse.dep.pos.business.BOTypes;
import lk.ijse.dep.pos.business.custom.ItemBO;
import lk.ijse.dep.pos.business.custom.PurchaseBO;
import lk.ijse.dep.pos.business.custom.PurchaseOrderBO;
import lk.ijse.dep.pos.dto.*;
import lk.ijse.dep.pos.util.POrderDetailTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseOrderFormController implements Initializable {

    public JFXTextField txtNOP;
    public JFXTextField txtReference;
    public JFXTextField txtProMar;
    public Label lblId;
    public JFXComboBox cmbVendor;
    public JFXTextField txtStockPrice;

    ItemBO itemBO = BOFactory.getInstance().getBO(BOTypes.ITEM);
    PurchaseBO purchaseBO = BOFactory.getInstance().getBO(BOTypes.PURCHASE);
    PurchaseOrderBO pDetailBO=BOFactory.getInstance().getBO(BOTypes.PURCHASEORDER);
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView btnHome;
    @FXML
    private JFXButton btnAddNewPOrder;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtUnit;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private TableView<POrderDetailTM> tblPOrderDetails;

    @FXML
    private JFXTextField txtSellPrice;

    @FXML
    private Label lblPTotal;
    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private JFXComboBox<String> cmbSearch;
    @FXML
    private JFXTextField txtAmount;
    @FXML
    private Label lblPDisTotal;
    @FXML
    private JFXComboBox<String> cmbPMethod;
    @FXML
    private JFXTextField txtDisc;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXTextField txtSearchId;
    @FXML
    private JFXTextField txtDisPres;
    @FXML
    private JFXTextField txtQty;


    double amount=0.00;
    Double qty=0.00;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblPOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblPOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblPOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblPOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblPOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblPOrderDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblPOrderDetails.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("btnDelete"));


        //Set Data to jdc
        dpDate.setValue(LocalDate.now());
        //Initial Values
      firstLook();
      btnPlaceOrder.setDisable(true);

        //find item by typing
        txtItemCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                enablePlaceOrder();
            }
        });

    }

    private void calculateTotal() {
        ObservableList<POrderDetailTM> items = tblPOrderDetails.getItems();
       double total=0.00;
        for (POrderDetailTM item : items) {
            total+=item.getTotal();
        }
        lblPTotal.setText("Total : "+String.format("%.2f",total));
    }

    private void enablePlaceOrder() {
        if (tblPOrderDetails.getItems().size()>0){
            btnPlaceOrder.setDisable(false);
            calculateTotal();
        }else {
            btnPlaceOrder.setDisable(true);
            lblPTotal.setText("Total : 0.00");
            lblPDisTotal.setText("Total : 0.00");

        }
    }

    private void firstLook() {
        txtItemCode.requestFocus();
        txtItemCode.clear();
        txtDescription.clear();
        txtItemCode.requestFocus();
            txtSellPrice.setText("0.00");
        txtUnit.setText("1");
        txtQty.setText("0.00");
        txtDisPres.setText("0");
        txtDisc.setText("0.00");
        txtAmount.setText("0.00");
        tblPOrderDetails.getSelectionModel().clearSelection();
        btnAdd.setDisable(true);


    }

    @FXML
    void btnAddNew_OnAction(ActionEvent event) {

    }

    @FXML
    void btnAdd_OnAction(ActionEvent event) {
     addToCart();
    }

    private void addToCart() {
        //Add data into DB
        int orderId = Integer.parseInt(lblId.getText().replace("ORDER ID : PO", ""));
        int oNo = 1;

        try {
            pDetailBO.savePDetail(new PurchaseOrderDTO(oNo,orderId,txtItemCode.getText(),Double.parseDouble(txtUnit.getText()),Double.parseDouble(txtNOP.getText()),
                    Double.parseDouble(txtSellPrice.getText()),Double.parseDouble(txtDisPres.getText()),Double.parseDouble(txtProMar.getText())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFXButton btnDelete = new JFXButton("Delete");
        ObservableList<POrderDetailTM> items = tblPOrderDetails.getItems();
        POrderDetailTM pOrderDetailTM = new POrderDetailTM(txtItemCode.getText().toUpperCase(), txtDescription.getText(), Double.parseDouble(txtQty.getText()),
                Double.parseDouble(txtSellPrice.getText()), Double.parseDouble(txtDisPres.getText()), Double.parseDouble(txtStockPrice.getText()), btnDelete);
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
           @Override
            public void handle(ActionEvent event) {
                tblPOrderDetails.getItems().remove(pOrderDetailTM);
                tblPOrderDetails.refresh();
                tblPOrderDetails.getSelectionModel().clearSelection();
                enablePlaceOrder();
            }
        });
        items.add(pOrderDetailTM);
        firstLook();
        oNo++;
    }


    @FXML
    void btnPlaceOrder_OnAction(ActionEvent event) {
       /* try {
            int orNo=1;
            List<PurOrderDetailDTO> pOrderDetail = new ArrayList<>();
            ObservableList<POrderDetailTM> items = tblPOrderDetails.getItems();
            for (POrderDetailTM item : items) {
                pOrderDetail.add(new PurOrderDetailDTO(orNo,001,item.getCode(),item.getQty(),item.getQty(),item.getUnitPrice(),item.getDiscount(),
                        item.getTotal(),"Precent"));
                orNo++;
            }

            purchaseBO.savePurchase(new PurchaseDTO(001, Date.valueOf(dpDate.getValue()),8,"0.00","0.00","0.00",pOrderDetail));
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

         }

    @FXML
    void cmbPMethod_onkeyPressed(KeyEvent event) {

    }

    @FXML
    void cmbSearch_KeyPressed(KeyEvent event) {

    }

    @FXML
    void cmbSearch_onAction(ActionEvent event) {

    }

    @FXML
    void dpDate_onKeyPressed(KeyEvent event) {

    }

    @FXML
    void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    @FXML
    void txtDisPres_onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            double disPres = Double.parseDouble(txtDisPres.getText());

                amount = Double.parseDouble(txtSellPrice.getText());
                double disValue = amount * disPres * 0.01;
                double netAmount = amount - disValue;
                txtAmount.setText(String.valueOf(netAmount));
                txtStockPrice.setText(String.format("%.2f",netAmount));
                txtProMar.requestFocus();

        }


    }

    @FXML
    void txtDisc_OnAction(ActionEvent event) {

    }

    @FXML
    void txtDisc_keyPressed(KeyEvent event) {

    }

    @FXML
    void txtItemCode_onAction(ActionEvent event) {

    }

    @FXML
    void txtItemCode_onKeyPresesd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                ItemDTO item = itemBO.findItem(txtItemCode.getText());
                txtDescription.setText(item.getDescription());
                txtAmount.setText(String.valueOf(item.getUnitPrice()));
                txtSellPrice.setText(String.valueOf(item.getUnitPrice()));
                txtUnit.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @FXML
    void txtQty_onKeyPressed(KeyEvent event) {
       if (event.getCode()==KeyCode.ENTER){
           txtSellPrice.requestFocus();
           txtSellPrice.selectAll();
       }
    }

    @FXML
    void txtSearchId_onKeyPressed(KeyEvent event) {

    }

    @FXML
    void txtSellPrice_OnAction(ActionEvent event) {

    }

    @FXML
    void txtUnit_onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
           txtNOP.requestFocus();
        }
    }

    public void txtNOP_onKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode()==KeyCode.ENTER) {
        Double uip = Double.valueOf(txtUnit.getText());
        Double pack = Double.valueOf(txtNOP.getText());

        qty = uip * pack;
        txtQty.setText(String.format("%.2f", qty));
        txtQty.requestFocus();
    }
    }

    public void txtSellPrice_OnKeyPressed(KeyEvent keyEvent) {
     if (keyEvent.getCode()==KeyCode.ENTER){
         Double tQty= Double.valueOf(txtQty.getText());
         Double tPP= Double.valueOf(txtSellPrice.getText());
         Double uPrice=(tPP/tQty);
         txtAmount.setText(String.format("%.2f",uPrice));
         txtDisPres.requestFocus();
         btnAdd.setDisable(false);

     }
    }

    public void txtProMar_onKeyPressed(KeyEvent keyEvent) {

        if (keyEvent.getCode()==KeyCode.ENTER){
            double pM= Double.parseDouble(txtProMar.getText());
            double disc = Double.parseDouble(txtDisPres.getText());
            double disValue = amount - (amount) * disc * 0.01;
            double unitPrice=(disValue/qty)+((disValue/qty)*pM*0.01);
            txtAmount.setText(String.format("%.2f",unitPrice));
            txtAmount.requestFocus();
        }
    }

    public void txtStockPrice(ActionEvent actionEvent) {

    }

    public void txtStockPrice_onKeyPressed(KeyEvent keyEvent) {

    }

    public void txtAmount_onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode()==KeyCode.ENTER){
            addToCart();
        }
    }

    public void btnAdd_onKeyPressed(KeyEvent keyEvent) {

    }
}
