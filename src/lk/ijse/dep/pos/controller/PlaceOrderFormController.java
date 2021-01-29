package lk.ijse.dep.pos.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.pos.business.BOFactory;
import lk.ijse.dep.pos.business.BOTypes;
import lk.ijse.dep.pos.business.custom.*;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.*;
import lk.ijse.dep.pos.entity.Item;
import lk.ijse.dep.pos.entity.OrderDetailPK;
import lk.ijse.dep.pos.util.OrderDetailTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import sun.plugin2.gluegen.runtime.StructAccessor;

import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaceOrderFormController {

    public JFXTextField txtDescription;
    public JFXTextField txtCustomerName;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnSave;
    public TableView<OrderDetailTM> tblOrderDetails;
    public JFXTextField txtUnitPrice;
    public JFXComboBox<String> cmbCustomerId;

    public JFXTextField txtQty;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public AnchorPane root;
    public Label lblId;
    public JFXButton btnAddNewOrder;
    public JFXTextField txtItemCode;
    public Button btnSearch;
    public JFXComboBox cmbSearch;
    public JFXTextField txtAmount;
    public JFXComboBox<String> cmbPMethod;
    public JFXComboBox<String> cmbTransAcc;
    public JFXTextField txtDisc;
    public Label lblDisTotal;
    public JFXTextField txtSearchOrder;
    public JFXDatePicker dpDate;
    public JFXTextField txtSearchId;

    String maxTrans = "";
    Double pDisAmount = 0.00;
    Double pTotal = 0.00;
    Double transAmount = 0.00;
    Double transDisAmount=0.00;
    String transDebNo="";
    String debtorAccNo="";
    String oilTransNo="";

    private boolean readOnly = false;
    private List<ItemDTO> tempItems = new ArrayList<>();
    private CustomerBO customerBO = BOFactory.getInstance().getBO(BOTypes.CUSTOMER);
    private ItemBO itemBO = BOFactory.getInstance().getBO(BOTypes.ITEM);
    private OrderBO orderBO = BOFactory.getInstance().getBO(BOTypes.ORDER);
    private TransDetailBO transDetailBO = BOFactory.getInstance().getBO(BOTypes.TRANSDETAIL);
    private GenAccBO genAccBO = BOFactory.getInstance().getBO(BOTypes.GENACC);

    public void initialize() {

        reset();

        // Let's map columns with table model
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblOrderDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        try {

            List<String> ids = customerBO.getAllCustomerIDs();
            cmbCustomerId.setItems(FXCollections.observableArrayList(ids));
            cmbCustomerId.getSelectionModel().selectFirst();
            tempItems = itemBO.findAllItems();


            List<String> searchItems = itemBO.getAllItemCodes();
            for (String item : searchItems) {
                cmbSearch.setItems(FXCollections.observableArrayList(searchItems));
            }

            //Insert Data into account
            List<GenAccDTO> allAcc = genAccBO.findAllAcc();
            for (GenAccDTO genAccDTO : allAcc) {

                cmbTransAcc.getItems().add(genAccDTO.getAccNo());
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO2").show();
            Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
        }

        // When customer id is selected
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String selectedCustomerID = cmbCustomerId.getSelectionModel().getSelectedItem();
                enablePlaceOrderButton();

                try {
                    if (selectedCustomerID != null) {
                        CustomerDTO customer = customerBO.findCustomer(selectedCustomerID);
                        txtCustomerName.setText(customer.getName());
                    } else {

                        cmbCustomerId.getSelectionModel().selectFirst();
                        txtCustomerName.setText("Default");
                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO3").show();
                    Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
                }
            }
        });


        // When a table row is selected
        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM newValue) {

                OrderDetailTM selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();
                if (selectedOrderDetail == null) {
                    if (!readOnly) {
                        btnSave.setText("Add");
                    }
                    return;
                }
                for (ItemDTO tempItem : tempItems) {
                    if (tempItem.getCode().equals(selectedOrderDetail.getCode())) {
                        try {
                            ItemDTO item = itemBO.findItem(selectedOrderDetail.getCode());
                            tempItem.setQtyOnHand(item.getQtyOnHand());
//Set on textFields
                            txtDescription.setText(item.getDescription());
                            txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                        } catch (Exception e) {
                            new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO4").show();
                            Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
                        }
                    }
                }
                txtItemCode.setText(selectedOrderDetail.getCode());
                txtQty.setText(selectedOrderDetail.getQty() + "");
                // Don't think about this now...!

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        txtQty.requestFocus();
                        txtQty.selectAll();
                    }
                });
                if (!readOnly) {
                    btnSave.setText("Update");
                }
            }
        });


        cmbSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String selectedItem = (String) cmbSearch.getSelectionModel().getSelectedItem();

                try {
                    if (selectedItem != null) {
                        ItemDTO item = itemBO.findItem(selectedItem);
                        txtItemCode.setText(item.getCode());
                        txtDescription.setText(item.getDescription());
                    } else {
                        txtItemCode.setText("");
                        txtDescription.setText("");

                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO3").show();
                    Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
                }
            }
        });

        //SearchOrder

        dpDate.requestFocus();


        //Load cmb Payment Method data
        cmbPMethod.getItems().addAll("Cash", "Credit", "Card");
        cmbPMethod.getSelectionModel().selectFirst();

        cmbTransAcc.setVisible(false);
        cmbPMethod.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String selectedItem = cmbPMethod.getSelectionModel().getSelectedItem();
                if (selectedItem.equals("Credit")) {
                    cmbTransAcc.setVisible(true);
                } else {
                    cmbTransAcc.setVisible(false);
                }
            }
        });
    }

    private void reset() {
        // Initialize controls to their default states

        dpDate.setValue(LocalDate.now());
        btnSave.setDisable(true);
        txtCustomerName.setEditable(false);
        txtCustomerName.clear();
        txtDescription.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setEditable(false);
        txtQty.setEditable(false);
        txtQty.setText("1");
        txtDisc.setText("0.00");
        cmbCustomerId.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        lblTotal.setText("Total : 0.00");
        lblDisTotal.setText("Total : 0.00");
        enablePlaceOrderButton();
        cmbPMethod.getItems().clear();
        cmbPMethod.getItems().addAll("Cash", "Credit", "Card");
        cmbPMethod.getSelectionModel().selectFirst();
        btnPlaceOrder.setText("Place Order");
        clearItem();


        // Generate the new order id
        int maxOrderId = 0;
        try {
            maxOrderId = orderBO.getLastOrderId();
            maxOrderId++;
            if (maxOrderId < 10) {
                lblId.setText("Order ID : OD00" + maxOrderId);
            } else if (maxOrderId < 100) {
                lblId.setText("Order ID : OD0" + maxOrderId);
            } else {
                lblId.setText("Order ID : OD" + maxOrderId);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //Generate TransId
     /*   int maxTransId = 0;
        try {
            Integer maxTransIds = transDetailBO.getLastOrderId();

            if (maxTransIds == null) {

                maxTrans = "T001";
            } else {

                maxTransId++;
                if (maxTransId < 10) {
                    maxTrans = "T00" + maxTransId;
                } else if (maxTransId < 100) {
                    maxTrans = "T0" + maxTransId;
                } else {
                    maxTrans = "T" + maxTransId;
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO5").show();
            Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
        }*/
    }

    private void clearItem() {
        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtUnitPrice.setText("0.00");
        txtAmount.setText("0.00");
        txtQtyOnHand.clear();
        txtQtyOnHand.setText("0");
        txtQty.setText("1");
        btnSave.setDisable(true);

    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        dpDate.requestFocus();
        reset();

    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        double qty = Double.parseDouble(txtQty.getText());
        double qtyOnHand = Double.parseDouble(txtQtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());


        String selectedItemCode = txtItemCode.getText().toUpperCase();
        ObservableList<OrderDetailTM> details = tblOrderDetails.getItems();

        boolean isExists = false;
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            if (detail.getCode().equals(selectedItemCode)) {
                isExists = true;

                if (btnSave.getText().equals("Update")) {
                    detail.setQty(qty);
                } else {
                    detail.setQty(Double.parseDouble(String.format("%.3f", detail.getQty() + qty)));
                }
                detail.setTotal(Double.parseDouble(String.format("%.2f", (detail.getQty() * detail.getUnitPrice())+detail.getQty() * detail.getUnitPrice()*0.1)));
                tblOrderDetails.refresh();

                break;
            }
        }

        if (!isExists) {

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            nf.setGroupingUsed(false);

            Double amount = Double.valueOf(nf.format((qty * unitPrice)+(qty * unitPrice*0.1)));

            JFXButton btnDelete = new JFXButton("Delete");
            OrderDetailTM detailTM = new OrderDetailTM(txtItemCode.getText().toUpperCase(),
                    txtDescription.getText(),
                    qty,
                    unitPrice, amount,
                    btnDelete
            );
            btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (ItemDTO tempItem : tempItems) {
                        if (tempItem.getCode().equals(detailTM.getCode())) {
                            // Let's restore the qty
                            double qtyOnHand = tempItem.getQtyOnHand() + detailTM.getQty();
                            tempItem.setQtyOnHand(qtyOnHand);
                            break;
                        }
                    }
                    tblOrderDetails.getItems().remove(detailTM);
                    calculateTotal();
                    enablePlaceOrderButton();
                    txtItemCode.requestFocus();
                    //cmbItemCode.getSelectionModel().clearSelection();
                    tblOrderDetails.getSelectionModel().clearSelection();
                }
            });
            details.add(detailTM);

        }
        clearItem();
        // Calculate the grand total
        calculateTotal();
        enablePlaceOrderButton();
        txtItemCode.requestFocus();
        tblOrderDetails.getSelectionModel().clearSelection();

    }

    private void updateQty(String selectedItemCode, double qty) {

        selectedItemCode = txtItemCode.getText().toUpperCase();
        ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
        for (OrderDetailTM item : items) {
            if (item.getCode().equals(selectedItemCode)) {
                double tblQty = item.getQty();
                double remainQty = Double.parseDouble(txtQtyOnHand.getText());
                qty = Double.parseDouble(txtQty.getText());
                txtQtyOnHand.setText(String.format("%.2f", remainQty - tblQty));

            }

        }
      /*  for (ItemDTO item : tempItems) {
            if (item.getCode().equals(selectedItemCode)) {
                item.setQtyOnHand(item.getQtyOnHand() - qty);
                //   txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()-qty));
                break;
            }
        }*/
    }

    private void calculateTotal() {
        ObservableList<OrderDetailTM> details = tblOrderDetails.getItems();

        double total = 0;
        for (OrderDetailTM detail : details) {
            total += detail.getTotal();
        }

        // Let's format the total
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);
        double disTotal = total - Double.parseDouble(txtDisc.getText());
        lblTotal.setText(nf.format(total));
        lblDisTotal.setText("Total : " + String.format("%.2f", disTotal));
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        if (btnPlaceOrder.getText().equals("Place Order")) {
            actionPlaceOrder();
        } else {
            actionVoidSale();
        }
    }

    private void actionVoidSale() {
        //  this transaction is a credit sale with discount offer we should update amount of both rows(Discount given and crediter account)


        String orderId = txtSearchId.getText();
        Double lDisAmount = Double.valueOf(txtDisc.getText());
        Double disAmount;


        String accNO="Oil Sale";
        String accDes="Discount Given";
        try {
            String transNo = transDetailBO.findTransDetailByDateCategory(accNO, String.valueOf(dpDate.getValue()), accDes);
            if (tblOrderDetails.getItems().isEmpty()){
                disAmount = pDisAmount;
                orderBO.updateDis(Integer.parseInt(orderId), 0.00);

            }else {
                disAmount = pDisAmount - lDisAmount;
                orderBO.updateDis(Integer.parseInt(orderId), Double.valueOf(txtDisc.getText()));
            }
            //Transno ekata adala discount amount eka hoyanawa
            List<TransDetailDTO> transDetail = transDetailBO.findTransDetail(transNo);
            for (TransDetailDTO transData : transDetail) {

                transDisAmount = transData.getTransAmount();
            }
            Double upDatedDis=transDisAmount-disAmount;
            TransDetailDTO updateTrans = new TransDetailDTO(transNo, Date.valueOf(dpDate.getValue()), "Oil Sale",
                    "Oil Sale", "Discount Given", "Discount Given", "Expenses", upDatedDis);
            transDetailBO.updateTransDetail(updateTrans);
            //Update debtor record
            if (transDebNo.equals(null)||transDebNo.equals("")){

            }else {

                TransDetailDTO debtorRecord = new TransDetailDTO(transDebNo, Date.valueOf(dpDate.getValue()), cmbTransAcc.getSelectionModel().getSelectedItem(),
                        "Oil Sale", "Credit Oil Sale", "Oil Sale by Credit", "Expenses", Double.parseDouble(lblDisTotal.getText().replace("Total :","")),txtSearchId.getText());
                transDetailBO.updateTransDetail(debtorRecord);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionPlaceOrder() {
        String onDate = String.valueOf(dpDate.getValue());


        /////btn Place Order
        int orderId = Integer.parseInt(lblId.getText().replace("Order ID : OD", ""));
        double orderDisc = Double.parseDouble(txtDisc.getText());


        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        for (OrderDetailTM item : tblOrderDetails.getItems()) {
            orderDetails.add(new OrderDetailDTO(item.getCode(), item.getQty(), item.getUnitPrice()+(item.getUnitPrice()*0.1)));
        }

        OrderDTO order = new OrderDTO(orderId, Date.valueOf(onDate), cmbCustomerId.getSelectionModel().getSelectedItem(), orderDetails, orderDisc);

        try {
            orderBO.placeOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Play in transactionDetails

        //Find transaction no for the date in transaction Table

        try {
            boolean existsOilNo = transDetailBO.existsByCateDate("Oil Sale", onDate, "Income", "Oil Sale");
            String totValue = lblDisTotal.getText().replace("Total : ", "");


            if (existsOilNo==true){
                oilTransNo = transDetailBO.findTransDetailByDateCategory("Oil Sale", onDate, "Oil Sale");
                Double totalOilSale = transDetailBO.getTotal(onDate, onDate, "Income", "Oil Sale");
                 Double uTotal= totalOilSale+Double.valueOf(totValue);
                 Double upTotal= Double.parseDouble(String.format("%.2f",uTotal));


                TransDetailDTO newTransaction = new TransDetailDTO(oilTransNo, Date.valueOf(onDate),
                        "Oil Sale","Oil Sale","Oil Sale","Oil Sale",
                        "Income",upTotal);

                transDetailBO.updateTransDetail(newTransaction);

            }else{
                Integer lastOrderId = transDetailBO.getLastOrderId();
                oilTransNo= String.valueOf(++lastOrderId);
                transDetailBO.saveTransDetail(new TransDetailDTO("T"+oilTransNo,Date.valueOf(onDate),"Oil Sale","Oil Sale","Oil Sale","Oil Sale","Income",Double.parseDouble(totValue)));

            }
            //If it is a Discount Sale
            if (Double.parseDouble(txtDisc.getText())>0) {
                Integer lastOrderId = transDetailBO.getLastOrderId();
                Integer oilTransNo1 = (++lastOrderId);
                transDetailBO.saveTransDetail(new TransDetailDTO("T" + oilTransNo1, Date.valueOf(onDate), "Discount Given", "Oil Sale", "Dis Oil Sale", "Oil Sale With Dis", "Expenses", Double.parseDouble(txtDisc.getText())));
            }

            //If it is Not a cash Sale
            if (cmbPMethod.getSelectionModel().getSelectedItem().equals("Credit")){
                Integer lastOrderId = transDetailBO.getLastOrderId();
                Integer oilTransNo1=(++lastOrderId);
                transDetailBO.saveTransDetail(new TransDetailDTO("T"+oilTransNo1,Date.valueOf(onDate),cmbTransAcc.getSelectionModel().getSelectedItem(),"Oil Sale","Credit Oil Sale","Oil Sale By Credit","Expenses",Double.parseDouble(totValue)));

            }else if(cmbPMethod.getSelectionModel().getSelectedItem().equals("Credit Card")){
                Integer lastOrderId = transDetailBO.getLastOrderId();
                Integer oilTransNo1=(++lastOrderId);
                transDetailBO.saveTransDetail(new TransDetailDTO("T"+oilTransNo1,Date.valueOf(onDate),cmbTransAcc.getSelectionModel().getSelectedItem(),"Oil Sale","Card Oil Sale","Oil Sale By Card","Expenses",Double.parseDouble(totValue)));

            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        JasperReport jasperReport = null;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/dep/pos/report/Bill.jasper"));
        } catch (JRException e) {
            e.printStackTrace();
        }

        Map<String, Object> params1 = new HashMap<>();
            params1.put("orderId", orderId + "");

            Session session = HibernateUtil.getSessionFactory().openSession();
        JasperReport finalJasperReport = jasperReport;
        session.doWork(connection -> {

                try {
                    JasperPrint jasperPrint1 = JasperFillManager.fillReport(finalJasperReport, params1, connection);
                  /*  JasperViewer Jviewer = new JasperViewer(jasperPrint1, false);
                    Jviewer.setVisible(true);*/
                     JasperPrintManager.printReport(jasperPrint1, false);
                      } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            });
            session.disconnect();


        //btn Place Order

        reset();
        dpDate.requestFocus();
        dpDate.setValue(LocalDate.now());

    }


    private void billGenerate() throws IOException {

    }


    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        if (readOnly) {
            ((Stage) (txtQty.getScene().getWindow())).close();
            return;
        }
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public void txtQty_OnAction(ActionEvent actionEvent) {

        double qty = Double.parseDouble(txtQty.getText());
        double qtyOnHand = Double.parseDouble(txtQtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        // Let's validate the qty.
        if (qty <= 0 || qty > qtyOnHand) {
            new Alert(Alert.AlertType.ERROR, "Invalid Qty", ButtonType.OK).show();
            txtQty.requestFocus();
            txtQty.selectAll();
            return;
        } else {
            double amount = Double.parseDouble(String.format("%.2f", (qty * unitPrice)));
            System.out.println(amount);
            txtAmount.setText(String.valueOf(amount));
        }

        txtAmount.requestFocus();
        txtAmount.selectAll();
    }

    private void enablePlaceOrderButton() {

        double size = tblOrderDetails.getItems().size();

        if (size == 0) {
            btnPlaceOrder.setDisable(true);
        } else {
            btnPlaceOrder.setDisable(false);
        }
    }


    public void txtItemCode_onAction(ActionEvent actionEvent) {
       itemCodeAction();

    }

    private void itemCodeAction() {
        if (txtItemCode.getText().length() == 0) {
            txtDisc.requestFocus();
            txtDisc.selectAll();

        } else {
            try {

                ItemDTO item = itemBO.findItem(txtItemCode.getText());

                if (item == (null)) {
                    new Alert(Alert.AlertType.ERROR, "Item Code not available", ButtonType.OK).show();
                } else {
                    txtDescription.setText(item.getDescription());
                    txtUnitPrice.setText(String.format("%.2f", item.getUnitPrice()));
                    txtQtyOnHand.setText(String.format("%.3f", item.getQtyOnHand()));

                    txtQty.setEditable(true);
                    txtQty.setDisable(false);
                    btnSave.setDisable(false);
                    txtQty.requestFocus();
                    txtQty.selectAll();

                    updateQty(txtItemCode.getText(), Double.parseDouble(txtQty.getText()));
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Item code not available").show();
                Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
            }
        }
    }


    public void cmbSearch_onAction(ActionEvent actionEvent) {

    }

    public void cmbSearch_KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            itemCodeAction();
        }
    }

    public void txtAmount_onAction(ActionEvent actionEvent) {
        double qty = Double.parseDouble(txtQty.getText());
        double qtyOnHand = Double.parseDouble(txtQtyOnHand.getText());
        double damount = Double.parseDouble(txtAmount.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        // Let's validate the qty.
        double aqty = (damount / unitPrice);


        txtQty.setText(String.format("%.3f", aqty));
        if (qty <= 0 || aqty > qtyOnHand) {
            new Alert(Alert.AlertType.ERROR, "Invalid Qty", ButtonType.OK).show();
            txtQty.setText("1");
            txtAmount.requestFocus();
            txtAmount.selectAll();
            return;
        }

        btnAdd_OnAction(actionEvent);
    }

    public void txtItemCode_onKeyPresesd(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DOWN) {
            txtDisc.requestFocus();
            txtDisc.selectAll();
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if (btnPlaceOrder.isDisabled()) {

            } else {
                actionPlaceOrder();
            }
        }
    }

    public void cmbPMethod_onkeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (cmbPMethod.getSelectionModel().getSelectedItem().equals("Credit")) {
                cmbTransAcc.requestFocus();


            }else {
                actionPlaceOrder();
            }

        }else if (keyEvent.getCode()==KeyCode.E){
                          cmbPMethod.getItems().clear();
                cmbPMethod.getItems().addAll("Cash", "Credit", "Card");
                cmbPMethod.getSelectionModel().selectFirst();
        }
    }

    public void cmbTransAcc_onAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
           if (btnPlaceOrder.getText().equals("Place Order")) {
               actionPlaceOrder();
           }else {
               actionVoidSale();
           }
        }
    }

    public void txtDisc_OnAction(ActionEvent actionEvent) {
        double total = Double.parseDouble(lblTotal.getText());
        double disc = Double.parseDouble(txtDisc.getText());
        double disTotal = total - disc;
        lblDisTotal.setText("Total : " + String.format("%.2f", disTotal));
        cmbPMethod.requestFocus();
    }

    public void txtDisc_keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.SPACE) {
            double total = Double.parseDouble(lblTotal.getText());
            double disc = Double.parseDouble(txtDisc.getText());
            double disTotal = total - disc;
            lblDisTotal.setText("Total : " + String.format("%.2f", disTotal));
            actionPlaceOrder();
        }
    }

      public void dpDate_onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            txtItemCode.requestFocus();
        }
    }


    public void txtSearchId_onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            tblOrderDetails.getItems().clear();
            try {
                List<OrderDTO2> orderInfo = orderBO.getOrderInfo4(txtSearchId.getText());
                ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
                for (OrderDTO2 item : orderInfo) {

                    Double amount = Double.valueOf(String.format("%.2f", item.getUnitPrice() * item.getQty()));
                    JFXButton btnVoidSale = new JFXButton("Void Sale");
                    OrderDetailTM orderDetailTM = new OrderDetailTM(item.getItemCode(), item.getDescription(),
                            item.getQty(), item.getUnitPrice(), amount, btnVoidSale);
                    items.add(orderDetailTM);

                    //Get discount details
                    Double disDetail = orderBO.getDisDetail(Integer.parseInt(txtSearchId.getText()));
                    txtDisc.setText(String.valueOf(disDetail));
                    dpDate.setValue(item.getOrderDate().toLocalDate());

                    calculateTotal();
                    //getDisCount amount and creditor amount
                    pDisAmount = Double.valueOf(txtDisc.getText());
                    pTotal = Double.valueOf(lblDisTotal.getText().replace("Total :", ""));

                    //getTotal transaction Value of current date
                    String oilSale = "Oil Sale";
                    String transNo = transDetailBO.findTransDetailByDateCategory(oilSale, String.valueOf(dpDate.getValue()), oilSale);
                    //Transno ekata adala amount eka hoyanawa
                    List<TransDetailDTO> transDetail = transDetailBO.findTransDetail(transNo);
                    for (TransDetailDTO transData : transDetail) {
                        transAmount = transData.getTransAmount();
                    }

                    btnVoidSale.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            for (ItemDTO tempItem : tempItems) {
                                if (tempItem.getCode().equals(orderDetailTM.getCode())) {
                                    // Let's restore the qty
                                    double qtyOnHand = tempItem.getQtyOnHand() + orderDetailTM.getQty();
                                    tempItem.setQtyOnHand(qtyOnHand);


                                    OrderDetailTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();
                                    Double selAmount = selectedItem.getQty() * selectedItem.getUnitPrice();
                                    try {
                                        Double upDatedAmount = transAmount - selAmount;
                                        TransDetailDTO updateTrans = new TransDetailDTO(transNo, Date.valueOf(dpDate.getValue()), "Oil Sale",
                                                "Oil Sale", "Oil Sale", "Oil Sale", "Income", upDatedAmount);
                                      transDetailBO.updateTransDetail(updateTrans);
                                     } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        itemBO.updateItem(new ItemDTO(selectedItem.getCode(),
                                                selectedItem.getDescription(),
                                                qtyOnHand,
                                                selectedItem.getUnitPrice()));


                                        OrderDetailPK orderPK = new OrderDetailPK(Integer.parseInt(txtSearchId.getText()), selectedItem.getCode());
                                        orderBO.deleteOrderDetail(orderPK);


                                    } catch (Exception e) {
                                        System.out.println("Please Click Row first");
                                    }
                                    break;
                                }
                            }

                            tblOrderDetails.getItems().remove(orderDetailTM);
                            if (tblOrderDetails.getItems().isEmpty()) {
                                txtDisc.setText("0.00");
                            }
                                calculateTotal();
                        }
                    });

                    btnPlaceOrder.setText("Void Sale");
                    btnPlaceOrder.setDisable(false);
                }
                transDebNo = transDetailBO.getTransaNoByOrderId(txtSearchId.getText());

                List<TransDetailDTO> transData = transDetailBO.findTransDetail(transDebNo);
                for (TransDetailDTO transDatum : transData) {

                    debtorAccNo = transDatum.getTransAccNo();
                    if (debtorAccNo.equals(null)){

                    }else{
                        cmbTransAcc.getItems().clear();
                        cmbPMethod.getItems().clear();
                        cmbPMethod.getItems().add("Credit");
                        cmbPMethod.getSelectionModel().selectFirst();
                        cmbTransAcc.setVisible(true);
                        cmbTransAcc.getItems().add(debtorAccNo);
                        cmbTransAcc.getSelectionModel().selectFirst();
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
