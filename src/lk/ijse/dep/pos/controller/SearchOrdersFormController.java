package lk.ijse.dep.pos.controller;


import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.business.custom.TransDetailBO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.dto.OrderDTO2;
import lk.ijse.dep.pos.util.OrderTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchOrdersFormController {


    public TableView<OrderTM> tblReports;
    public JFXTextField txtItemCode;
    public JFXDatePicker jdcFrom;
    public JFXDatePicker jdcTo;
    public JFXTextField txtDescription;
    public AnchorPane root;
    public Button btnSearch;
    public RadioButton rbtItem;
    public RadioButton rbtDate;
    public Label lblTotal;
    public Button btnPrint;
    public Label lblCashSale;
    public Label lblCreditSale;
    public Label lblDis;
    public Label lblCardSale;

    private LocalDate dateto;
    private LocalDate datefrom;
    private String itemCode;

    private OrderBO orderBO = BOFactory.getInstance().getBO(BOTypes.ORDER);
    private ItemBO itemBO = BOFactory.getInstance().getBO(BOTypes.ITEM);
    private TransDetailBO transDetailBO = BOFactory.getInstance().getBO(BOTypes.TRANSDETAIL);

    public void initialize() throws Exception {

        tblReports.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblReports.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblReports.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblReports.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblReports.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        ObservableList<OrderTM> olOrders = tblReports.getItems();

        jdcTo.setValue(LocalDate.now());
        jdcFrom.setValue(LocalDate.now());

        loadInitialTable();
        calculateTotal();

        //Set rbt selected
        rbtDate.setSelected(true);
    }

    private void loadInitialTable() {
        ObservableList<OrderTM> tempOrders = FXCollections.observableArrayList();
        dateto = jdcTo.getValue();
        datefrom = jdcFrom.getValue();


        try {
            List<OrderDTO2> orderInfo = orderBO.getOrderInfo3(Date.valueOf(datefrom), Date.valueOf(dateto));
            for (OrderDTO2 data : orderInfo) {

                Double dtotal = data.getQty() * data.getUnitPrice();
                Double total = Double.valueOf(String.format("%.2f", dtotal));
                  Double qty = Double.valueOf(String.format("%.3f", data.getQty()));

                tempOrders.add(new OrderTM(data.getItemCode(), data.getDescription(), qty, data.getUnitPrice(),total));
            }
//Start
            dateto = LocalDate.now();
            String sDateTo = String.valueOf(dateto);
            //Get total discount
            Double disGiven = transDetailBO.getTotalAndDis("", "Discount Given", sDateTo, "", "Discount Given");
            if (disGiven == null) {
                disGiven = 0.00;
                lblDis.setText("Discount : 0.00");
            } else {
                lblDis.setText("Discount : " + String.format("%.2f", disGiven));
            }

            Double creditSale = transDetailBO.getTotalAndDis("", "Oil Sale", sDateTo, "Expenses", "Credit Oil Sale");
            if (creditSale == null) {
                creditSale = 0.00;
                lblCreditSale.setText("Credit Sale :0.00");
            } else {
                lblCreditSale.setText("Credit Sale : " + String.format("%.2f", creditSale));
            }

            Double totSale = transDetailBO.getTotalAndDis("", "Oil Sale", sDateTo, "Income", "Oil Sale");
            Double tSale;
            if (totSale == null) {
                tSale = 0.00;
            } else {
                tSale = totSale;
            }

            Double cardSale = transDetailBO.getTotalAndDis("", "Oil Sale", sDateTo, "Expenses", "Card Oil Sale");
            if (cardSale == null) {
                cardSale = 0.00;
                lblCardSale.setText("Card Sale : 0.00");
            } else {
                lblCardSale.setText("Card Sale : " + String.format("%.2f", cardSale));
            }


            double cashOnHand = tSale - (cardSale + creditSale + disGiven);
            lblCashSale.setText("Cash :" + String.format("%.2f", cashOnHand));


        } catch (Exception e) {
            e.printStackTrace();
        }
        tblReports.setItems(tempOrders);
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

    public void btnSearch_OnAction(ActionEvent actionEvent) {
        ObservableList<OrderTM> tempOrders = FXCollections.observableArrayList();
        itemCode = txtItemCode.getText();
        datefrom = jdcFrom.getValue();
        dateto = jdcTo.getValue();
       if (rbtDate.isSelected()) {
            try {
                List<OrderDTO2> orderInfo = orderBO.getOrderInfo3(Date.valueOf(datefrom), Date.valueOf(dateto));
                for (OrderDTO2 data : orderInfo) {

                    Double dtotal = data.getQty() * data.getUnitPrice();
                    Double total = Double.valueOf(String.format("%.2f", (dtotal*0.1)+dtotal));
                    Double qty = Double.valueOf(String.format("%.3f", data.getQty()));

                    tempOrders.add(new OrderTM( data.getItemCode(), data.getDescription(), qty, data.getUnitPrice(),dtotal, total));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            tblReports.setItems(tempOrders);
            calculateTotal();

        } else {
            try {

                List<OrderDTO2> orderInfo = orderBO.getOrderInfo3(Date.valueOf(datefrom), Date.valueOf(dateto));
                for (OrderDTO2 data : orderInfo) {
                    Double dtotal = data.getQty() * data.getUnitPrice();
                    Double total = Double.valueOf(String.format("%.2f", dtotal));
                    Double qty = Double.valueOf(String.format("%.3f", data.getQty()));

                    tempOrders.add(new OrderTM(data.getItemCode(), data.getDescription(), qty, data.getUnitPrice(),dtotal));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            tblReports.setItems(tempOrders);
            calculateTotal();
        }

    }

    public void txtItemCode_onAction(ActionEvent actionEvent) {
        String itemCode = txtItemCode.getText();
        try {
            ItemDTO item = itemBO.findItem(itemCode);
            String description = item.getDescription();

            txtDescription.setText(description);
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Scan again Incorrect Item", ButtonType.OK).show();
        }
    }

    private void calculateTotal() {
        ObservableList<OrderTM> details = tblReports.getItems();

        double total = 0;
        for (OrderTM detail : details) {
            total += detail.getTotal();
        }

        // Let's format the total
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);

        lblTotal.setText("Total : " + nf.format(total));
    }

    public void btnPrint_OnAction(ActionEvent actionEvent) throws JRException {


        JasperReport jasperReport = null;

            jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/lk/ijse/dep/pos/report/DailySale2.jrxml"));


        Map<String, Object> params = new HashMap<>();
        params.put("dateFrom", datefrom + "");
        JasperReport subReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/dep/pos/report/DailySale3.jasper"));
        params.put("subReport", subReport);

        Session session = HibernateUtil.getSessionFactory().openSession();
        JasperReport finalJasperReport = jasperReport;
        session.doWork(connection -> {

            try {
                JasperPrint jasperPrint1 = JasperFillManager.fillReport(finalJasperReport, params, connection);
                JasperViewer Jviewer = new JasperViewer(jasperPrint1, false);
                Jviewer.setVisible(true);
                JasperPrintManager.printReport(jasperPrint1, false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        });
        session.disconnect();










/*-----------------------------------This section is include itext report which I created for the time
that Jasper not working---------------------------------------------------*/
 /*try {


          Paragraph para1 = new Paragraph("Dandagamuwa Filling Station", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
          para1.setAlignment(Paragraph.ALIGN_CENTER);
          //  document.add(para1 );
          Paragraph para2= new Paragraph("& Service Center", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
          para2.setAlignment(Paragraph.ALIGN_CENTER);
          Paragraph para3= new Paragraph("__________________________________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK));
          para3.setAlignment(Paragraph.ALIGN_CENTER);

          Paragraph para4 = new Paragraph("Date : " +(jdcFrom.getValue())+ " - " +jdcTo.getValue(), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.BLACK));
          para4.setAlignment(Paragraph.ALIGN_RIGHT);

          Paragraph para6 = new Paragraph("Lubricant Sales Report", FontFactory.getFont(FontFactory.TIMES_BOLD, 13, BaseColor.BLACK));
          para6.setAlignment(Paragraph.ALIGN_CENTER);


          //Create Table


          PdfPTable table = new PdfPTable(5);
          table.setWidthPercentage(95);
          table.setSpacingBefore(20f);
          table.setSpacingAfter(10f);



          // table.setTotalWidth(new float[]{ 80, 160,60,60 });
          table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
          //     table.addCell(new Paragraph("Date", FontFactory.getFont(FontFactory.TIMES_BOLD,9,BaseColor.BLACK)));
          table.addCell(new Paragraph("Code", FontFactory.getFont(FontFactory.TIMES_BOLD,10, BaseColor.BLACK)));
          table.addCell(new Paragraph("Description", FontFactory.getFont(FontFactory.TIMES_BOLD,10,BaseColor.BLACK)));
          table.addCell(new Paragraph("Qty", FontFactory.getFont(FontFactory.TIMES_BOLD,10,BaseColor.BLACK)));
          table.addCell(new Paragraph("Price", FontFactory.getFont(FontFactory.TIMES_BOLD,10,BaseColor.BLACK)));
          table.addCell(new Paragraph("Amount", FontFactory.getFont(FontFactory.TIMES_BOLD,10,BaseColor.BLACK)));

          //Total Column
            PdfPTable table1= new PdfPTable(1);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(12f);
            table1.setSpacingAfter(1f);
          //table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

          Double TotalAmount=0.00;

          try {
          datefrom=jdcFrom.getValue();
          dateto=jdcTo.getValue();
          List<OrderDTO2> orderInfo = orderBO.getOrderInfo3(Date.valueOf(datefrom), Date.valueOf(dateto));
        for (OrderDTO2 data : orderInfo) {

        Double dtotal = data.getQty() * data.getUnitPrice();
        Double total = Double.valueOf(String.format("%.2f", (dtotal*0.1)+dtotal));

        String datea= String.valueOf(data.getOrderDate());
        String Icode=data.getItemCode();
        String descrip=data.getDescription();
        double qty= (data.getQty());
        double uPrice=(data.getUnitPrice());
        TotalAmount+=total;
        // table.addCell(new Paragraph(datea, FontFactory.getFont(FontFactory.TIMES,9,BaseColor.BLACK)));
        table.addCell(new Paragraph(Icode, FontFactory.getFont(FontFactory.TIMES,11,BaseColor.BLACK)));
        table.addCell(new Paragraph(descrip, FontFactory.getFont(FontFactory.TIMES,11,BaseColor.BLACK)));
        table.addCell(new Paragraph(String.format("%.3f",qty), FontFactory.getFont(FontFactory.TIMES,11,BaseColor.BLACK)));
        table.addCell(new Paragraph(String.format("%.2f",uPrice), FontFactory.getFont(FontFactory.TIMES,11,BaseColor.BLACK)));
        table.addCell(new Paragraph(String.format("%.2f",total), FontFactory.getFont(FontFactory.TIMES,11,BaseColor.BLACK)));

        }

        } catch (Exception e) {
        e.printStackTrace();
        }
        Paragraph para5 = new Paragraph("                                                                   Total : " +String.format("%.2f", TotalAmount), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.BLACK));
        para5.setAlignment(Paragraph.ALIGN_CENTER);

        String reportname="Sales Report"+jdcFrom.getValue().toString()+"pdf";
        System.out.println(reportname);
        Document document = new Document(PageSize.A4);
        document.setMargins(25, 20, 10, 5);
        PdfWriter.getInstance(document,new FileOutputStream("C://Users//abc//Desktop//report/reportname.pdf"));
        //PdfWriter.getInstance(document,new FileOutputStream(reportname+".pdf"));
        document.open();

        document.add(para1 );
        document.add(para2 );
        document.add(para4 );
        document.add(para3);
        document.add(para6);
        document.add(para3);
        document.add(table);
        document.add(para5);



        document.close();
        }catch (Exception e){
        System.out.println(e);
        }
*/
  }
  }
