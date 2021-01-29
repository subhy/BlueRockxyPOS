/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.dep.pos.controller;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.pos.business.BOFactory;
import lk.ijse.dep.pos.business.BOTypes;
import lk.ijse.dep.pos.business.custom.ItemBO;
import lk.ijse.dep.pos.business.exception.AlreadyExistsInOrderException;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.util.ItemTM;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author ranjith-suranga
 */
public class ManageItemFormController implements Initializable {

    public JFXTextField txtCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public TableView<ItemTM> tblItems;
    public JFXTextField txtUnitPrice;
    public JFXButton btnCart;
    public TextField txtItemSearch;
    public Button btnReport;
    public JFXTextField txtnewQty;
    public Label lblTotSValue;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private AnchorPane root;

    private ItemBO itemBO = BOFactory.getInstance().getBO(BOTypes.ITEM);


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItems.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        txtCode.setDisable(true);
        txtDescription.setDisable(true);
        txtQtyOnHand.setDisable(true);
        txtUnitPrice.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(true);


        loadAllItems();

        //Disable textFields
        txtQtyOnHand.setDisable(true);
        txtnewQty.setVisible(false);


        tblItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {
                ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    btnSave.setText("Save");
                    btnDelete.setDisable(true);
                    return;
                }

                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                txtDescription.setDisable(false);
                txtQtyOnHand.setDisable(false);
                txtUnitPrice.setDisable(false);
                txtCode.setEditable(false);
                txtCode.setDisable(false);
                txtnewQty.setVisible(true);
                txtnewQty.setText("0");

                txtCode.setText(selectedItem.getCode());
                txtDescription.setText(selectedItem.getDescription());
                txtQtyOnHand.setText(selectedItem.getQtyOnHand() + "");
                txtUnitPrice.setText(selectedItem.getUnitPrice() + "");


            }
        });

        txtItemSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                tblItems.getItems().clear();
                List<ItemDTO> searchItems = itemBO.findSearchItems(newValue);
                ObservableList<ItemTM> items = tblItems.getItems();
                for (ItemDTO item : searchItems) {
                    Double getQtyOnHand = Double.parseDouble(String.format("%.3f", item.getQtyOnHand()));

                    items.add(new ItemTM(item.getCode(), item.getDescription(),
                            getQtyOnHand, item.getUnitPrice(), Double.parseDouble(String.format("%.2f", getQtyOnHand * item.getUnitPrice()))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });


        txtnewQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double newQty = Double.parseDouble(txtnewQty.getText());
                if (newQty > 0) {
                    btnSave.setText("Purchase");
                } else {
                    btnSave.setText("Update");
                }
            }
        });

    }

    private void loadAllItems() {
        try {
            List<ItemDTO> allItems = itemBO.findAllItems();
            ObservableList<ItemTM> items = tblItems.getItems();

            for (ItemDTO item : allItems) {
                Double getQtyOnHand = Double.parseDouble(String.format("%.3f", item.getQtyOnHand()));

                items.add(new ItemTM(item.getCode(), item.getDescription(),
                        getQtyOnHand, item.getUnitPrice(), Double.parseDouble(String.format("%.2f", getQtyOnHand * item.getUnitPrice()))));
            }

            ObservableList<ItemTM> items1 = tblItems.getItems();
            double total = 0.00;
            for (ItemTM detail : items1) {
                total += detail.getTotalValue();
            }
            lblTotSValue.setText("Total Stock Value : " + String.format("%.2f", total));

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO").show();
            Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    @FXML
    private void btnSave_OnAction(ActionEvent event) {
        Double qtyOnHand = Double.parseDouble(txtQtyOnHand.getText());
        Double uPrice = Double.parseDouble(txtUnitPrice.getText());
        if (btnSave.getText().equals("Save")) {
            ObservableList<ItemTM> items = tblItems.getItems();
            ItemTM newItem = new ItemTM(
                    txtCode.getText(),
                    txtDescription.getText(),
                    qtyOnHand,
                    uPrice,
                    qtyOnHand * uPrice);
            ItemDTO item = new ItemDTO(txtCode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtQtyOnHand.getText()),
                    Double.parseDouble(txtUnitPrice.getText()));
            try {
                itemBO.saveItem(item);
                items.add(newItem);
                btnAddNew_OnAction(event);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO").show();
                Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
            }
        } else if (btnSave.getText().equals("Update")) {
            String selectedItem = txtCode.getText();
            try {
                itemBO.updateItem(new ItemDTO(selectedItem,
                        txtDescription.getText(),
                        Double.parseDouble(txtQtyOnHand.getText()),
                        Double.parseDouble(txtUnitPrice.getText())));
                tblItems.getItems().clear();
                loadAllItems();

                btnAddNew_OnAction(event);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO").show();
                Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
            }
        } else if (btnSave.getText().equals("Purchase")) {

            try {
                itemBO.updateItem(new ItemDTO(txtCode.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtQtyOnHand.getText()) + Double.parseDouble(txtnewQty.getText()),
                        Double.parseDouble(txtUnitPrice.getText())));

                tblItems.getItems().clear();
                loadAllItems();
                btnAddNew_OnAction(event);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this item?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();
            try {
                itemBO.deleteItem(selectedItem.getCode());
                tblItems.getItems().remove(selectedItem);
            } catch (AlreadyExistsInOrderException e) {
                new Alert(Alert.AlertType.INFORMATION, e.getMessage()).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong, please contact DEPPO").show();
                Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null, e);
            }
        }
    }

    @FXML
    private void btnAddNew_OnAction(ActionEvent actionEvent) {
        txtCode.clear();
        txtCode.setEditable(true);
        txtCode.setDisable(false);
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        tblItems.getSelectionModel().clearSelection();
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtCode.requestFocus();
        btnSave.setDisable(false);

        // Generate a new id
       /* int maxCode = 0;
        try {
            String lastItemCode = itemBO.getLastItemCode();
            if (lastItemCode == null) {
                maxCode = 0;
            } else {
                maxCode = Integer.parseInt(lastItemCode.replace("I", ""));
            }

            maxCode = maxCode + 1;
            String code = "";
            if (maxCode < 10) {
                code = "I00" + maxCode;
            } else if (maxCode < 100) {
                code = "I0" + maxCode;
            } else {
                code = "I" + maxCode;
            }
            txtCode.setText(code);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Something went wrong, please contact DEPPO").show();
            Logger.getLogger("lk.ijse.dep.pos.controller").log(Level.SEVERE, null,e);
        }
*/

    }


    public void txtDesc_onAction(ActionEvent actionEvent) {
        txtQtyOnHand.requestFocus();
    }

    public void txtQOH_onAction(ActionEvent actionEvent) {
        txtUnitPrice.requestFocus();
    }

    public void btnCart_OnAction(ActionEvent actionEvent) {

    }

    public void tblOrders_onMouseClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {

            URL resource = this.getClass().getResource("/view/PlaceOrderForm.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent root = fxmlLoader.load();
            Scene placeOrderScene = new Scene(root);


            PlaceOrderFormController ctrl = fxmlLoader.getController();
            ItemTM selectedOrder = tblItems.getSelectionModel().getSelectedItem();
            System.out.println(selectedOrder.getCode());
            ctrl.txtItemCode.setText(selectedOrder.getCode());


        }
    }

    public void txtCode_onAction(ActionEvent actionEvent) {
        try {
            ItemDTO item = itemBO.findItem(txtCode.getText());
            if (item == null) {
                txtDescription.requestFocus();
                txtDescription.clear();
                txtnewQty.setVisible(false);
                txtQtyOnHand.setDisable(false);

            } else {
                txtDescription.setText(item.getDescription());
                txtQtyOnHand.setDisable(true);
                txtQtyOnHand.setText(String.format("%.3f", item.getQtyOnHand()));
                txtnewQty.setVisible(true);
                txtnewQty.requestFocus();
                txtnewQty.selectAll();
                txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                btnSave.setText("Update");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void btnReport_onAction(ActionEvent actionEvent) {

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/dep/pos/report/StockReport.jasper"));

            Map<String, Object> params1 = new HashMap<>();


            Session session = HibernateUtil.getSessionFactory().openSession();
            session.doWork(connection -> {

                try {
                    JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport, params1, connection);
                    JasperViewer Jviewer = new JasperViewer(jasperPrint1, false);
                    Jviewer.setVisible(true);
                    JasperPrintManager.printReport(jasperPrint1, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void txtnewQty_onAction(ActionEvent actionEvent) {
        if (txtnewQty.getText().equals("") || txtnewQty.getText().equals("0")) {
            txtnewQty.setText("0");
            txtQtyOnHand.setDisable(false);
            txtQtyOnHand.requestFocus();
            txtQtyOnHand.selectAll();
        }

    }
}



/*This is what about I create using Itext for report when I don't have Jasper iReport*/
/*



        try {


            Paragraph para1 = new Paragraph("Dandagamuwa Filling Station", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
            para1.setAlignment(Paragraph.ALIGN_CENTER);
            //  document.add(para1 );
            Paragraph para2 = new Paragraph("& Service Center", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
            para2.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph para3 = new Paragraph("__________________________________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK));
            para3.setAlignment(Paragraph.ALIGN_CENTER);

            Paragraph para4 = new Paragraph("Date : " + LocalDate.now(), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.BLACK));
            para4.setAlignment(Paragraph.ALIGN_RIGHT);

            Paragraph para6 = new Paragraph("Stock Report", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
            para6.setAlignment(Paragraph.ALIGN_CENTER);


            //Create Table


            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(95);
            table.setSpacingBefore(20f);
            table.setSpacingAfter(10f);


            // table.setTotalWidth(new float[]{ 80, 160,60,60 });
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            //     table.addCell(new Paragraph("Date", FontFactory.getFont(FontFactory.TIMES_BOLD,9,BaseColor.BLACK)));
            table.addCell(new Paragraph("Code", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));
            table.addCell(new Paragraph("Description", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));
            table.addCell(new Paragraph("Qty", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));
            table.addCell(new Paragraph("Price", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));
            table.addCell(new Paragraph("Amount", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));


            try {

                List<ItemDTO> items = itemBO.findAllItems();
                for (ItemDTO data : items) {

                    String Icode = data.getCode();
                    String descrip = data.getDescription();
                    double qty = data.getQtyOnHand();
                    double uPrice = (data.getUnitPrice());

                    // table.addCell(new Paragraph(datea, FontFactory.getFont(FontFactory.TIMES,9,BaseColor.BLACK)));
                    table.addCell(new Paragraph(Icode, FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK)));
                    table.addCell(new Paragraph(descrip, FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK)));
                    table.addCell(new Paragraph(String.format("%.3f", qty), FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK)));
                    table.addCell(new Paragraph(String.format("%.2f", uPrice), FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK)));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Document document = new Document(PageSize.A4);
            document.setMargins(25, 20, 10, 5);
            PdfWriter.getInstance(document, new FileOutputStream("C://Users//abc//Desktop//report/StockReport.pdf"));
            // PdfWriter.getInstance(document,new FileOutputStream("StockReport.pdf"));
            document.open();

            document.add(para1);
            document.add(para2);
            document.add(para4);
            document.add(para3);
            document.add(para6);
            document.add(para3);
            document.add(table);


            document.close();
            new Alert(Alert.AlertType.INFORMATION, "Stock Report Generated Successfully").show();
        } catch (Exception e) {
            System.out.println(e);
        }
 */