/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableModel;
import model.TableJPA;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultKeyedValuesDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import view.administracion.ReporteView;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import model.DetalleDis;
import model.DetalleSub;
import model.Distribucion;
import model.Subministro;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import persistent.Control;
import utility.WindowDesign;

/**
 *
 * @author metallica
 */
public class ReporteController {
    
    Control control=new Control();
    ReporteView view;
    ButtonGroup g1,g2;
    TableJPA<DetalleSub>modelSubministro;
    TableJPA<DetalleDis>modelDistribucion;
    TableJPA<Distribucion>modelDistribucion1;
    int select=0;
    String column[]={"Producto","Cantidad","Precio","Subtotal"};
    String atrib[]={"productoName","cantidad","precio","subtotal"};
    Boolean edit[]={false,false,false,false};
    Map<String, String>dataInfo;
    TimeSeriesCollection dataSerie;

    public ReporteController(ReporteView v) {
        this.view=v;
        //test();
        g1=new ButtonGroup();
        g2=new ButtonGroup();
        g1.add(view.rbtnAnio);
        g1.add(view.rbtnMes);
        
        g2.add(view.rbtnCosto);
        g2.add(view.rbtnGanancia);
        g2.add(view.rbtnDistribucion);
        initAction();
        JTableHeader t=view.tbDetalle.getTableHeader();
        t.setBackground(new Color(25, 25, 25));
        t.setForeground(Color.white);
    }
    
    public void LoadData(){
        if(view.rbtnCosto.isSelected()){
            modelSubministro=new TableJPA(column,atrib,edit);
            modelSubministro.loadMethod(DetalleSub.class);
            select=1;
        }else if(view.rbtnGanancia.isSelected()){
            modelDistribucion=new TableJPA(column,atrib,edit);
            modelDistribucion.loadMethod(DetalleDis.class);
            select=2;
        }else if(view.rbtnDistribucion.isSelected()){
            String c[]={"Nombre","N° Subministro","Fecha","Total"};
            String a[]={"destinoName","id","fecha","total"};
            modelDistribucion1=new TableJPA(c,a,edit);
            modelDistribucion1.loadMethod(Distribucion.class);
            select=3;
        }
        
        double t=0;
        dataInfo=new HashMap();
        if(view.rbtnAnio.isSelected()){
            if(select==1){
                String sql="SELECT u.* FROM SUBMINISTRO u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtAnio.getYear();
                parameter.put("d", year);
                List<Subministro>ls=control.subministro.QuerySQL(sql, parameter);
                List<DetalleSub> l =new ArrayList();
                for(Subministro i : ls){
                    t+=i.getTotal();
                    l.addAll(l.size(),i.getDetalleSubList());
                }
                TimeSeries s=new TimeSeries("Gastos en Subministro");
                for (Control.TotalByDateDTO i : control.getTotalByDateSubministro(year,null)){
                    s.add(new Day(i.getFecha()),i.getTotal());
                }
                modelSubministro.setData(l);
                view.tbDetalle.setModel(modelSubministro);
                for(DetalleSub i : modelSubministro.getAll()){
                    dataInfo.put(i.getProductoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getProductoName())!= null ? dataInfo.get(i.getProductoName()) : "0") + i.getSubtotal())
                    );
                }
                dataSerie=new TimeSeriesCollection();
                dataSerie.addSeries(s);
                
            }else if(select==2){
                String sql="SELECT u.* FROM DISTRIBUCION u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtAnio.getYear();
                parameter.put("d", year);
                List<Distribucion>ld=control.distribucion.QuerySQL(sql, parameter);
                List<DetalleDis> l =new ArrayList();
                for (Distribucion i : ld){
                    t+=i.getTotal();
                    l.addAll(l.size(), i.getDetalleDisList());
                }
                modelDistribucion.setData(l);
                view.tbDetalle.setModel(modelDistribucion);                
                for(DetalleDis i : modelDistribucion.getAll()){
                    dataInfo.put(i.getProductoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getProductoName())!= null ? dataInfo.get(i.getProductoName()) : "0") + i.getSubtotal())
                    );
                }
                TimeSeries s=new TimeSeries("Ganancias");
                for (Control.TotalByDateDTO i : control.getTotalByDateDistribucion(year,null)){
                    s.add(new Day(i.getFecha()),i.getTotal());
                }
                dataSerie=new TimeSeriesCollection();
                dataSerie.addSeries(s);
                
            }else if(select==3){
                String sql="SELECT u.* FROM DISTRIBUCION u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtAnio.getYear();
                parameter.put("d", year);
                List<Distribucion>ld=control.distribucion.QuerySQL(sql, parameter);
                for(Distribucion i : ld) t+=i.getTotal();
                modelDistribucion1.setData(ld);
                view.tbDetalle.setModel(modelDistribucion1);
                for(Distribucion i : modelDistribucion1.getAll()){
                    dataInfo.put(i.getDestinoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getDestino())!= null ? dataInfo.get(i.getTotal()) : "0") + i.getTotal())
                    );
                }
            }
        }else if(view.rbtnMes.isSelected()){
            if(select==1){
                String sql="SELECT u.* FROM SUBMINISTRO u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d AND EXTRACT(MONTH FROM u.FECHA) = ?m";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtMesAnio.getYear();
                Integer month=view.dtMes.getMonth()+1;
                parameter.put("d", year);
                parameter.put("m", month);
                List<Subministro>ls=control.subministro.QuerySQL(sql, parameter);
                List<DetalleSub> l =new ArrayList();
                for (Subministro i : ls){
                    l.addAll(l.size(), i.getDetalleSubList());
                    t+=i.getTotal();
                }
                modelSubministro.setData(l);
                view.tbDetalle.setModel(modelSubministro);
                for(DetalleSub i : modelSubministro.getAll()){
                    dataInfo.put(i.getProductoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getProductoName())!= null ? dataInfo.get(i.getProductoName()) : "0") + i.getSubtotal())
                    );
                }
                TimeSeries s=new TimeSeries("Gastos en Subministro");
                for (Control.TotalByDateDTO i : control.getTotalByDateSubministro(year,month)){
                    s.add(new Day(i.getFecha()),i.getTotal());
                }
                dataSerie=new TimeSeriesCollection();
                dataSerie.addSeries(s);
                
            }else if(select==2){
                String sql="SELECT u.* FROM DISTRIBUCION u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d AND EXTRACT(MONTH FROM u.FECHA) = ?m";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtMesAnio.getYear();
                Integer month=view.dtMes.getMonth()+1;
                parameter.put("d", year);
                parameter.put("m", month);
                List<Distribucion>ld=control.distribucion.QuerySQL(sql, parameter);
                List<DetalleDis> l =new ArrayList();
                for (Distribucion i : ld){
                    l.addAll(l.size(), i.getDetalleDisList());
                    t+=i.getTotal();
                }
                modelDistribucion.setData(l);
                view.tbDetalle.setModel(modelDistribucion);                
                for(DetalleDis i : modelDistribucion.getAll()){
                    dataInfo.put(i.getProductoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getProductoName())!= null ? dataInfo.get(i.getProductoName()) : "0") + i.getSubtotal())
                    );
                }
                TimeSeries s=new TimeSeries("Ganancias");
                for (Control.TotalByDateDTO i : control.getTotalByDateDistribucion(year,month)){
                    s.add(new Day(i.getFecha()),i.getTotal());
                }
                dataSerie=new TimeSeriesCollection();
                dataSerie.addSeries(s);
                
            }else if(select==3){
                String sql="SELECT u.* FROM DISTRIBUCION u WHERE EXTRACT(YEAR FROM u.FECHA) = ?d AND EXTRACT(MONTH FROM u.FECHA) = ?m";
                Map<String,Object> parameter=new HashMap();
                Integer year=view.dtMesAnio.getYear();
                Integer month=view.dtMes.getMonth()+1;
                parameter.put("d", year);
                parameter.put("m", month);
                List<Distribucion>ld=control.distribucion.QuerySQL(sql, parameter);
                for(Distribucion i : ld) t+=i.getTotal();
                modelDistribucion1.setData(ld);
                view.tbDetalle.setModel(modelDistribucion1);                
                for(Distribucion i : modelDistribucion1.getAll()){
                    dataInfo.put(i.getDestinoName(),String.valueOf(
                            Double.parseDouble(dataInfo.get(i.getDestino())!= null ? dataInfo.get(i.getTotal()) : "0") + i.getTotal())
                    );
                }
            }
        }
        view.txtTotal.setText(t+"");
        loadGraphics("torta",select!=3 ? true : false);
    }
    
    public void initAction(){
        view.btnGenerar.addActionListener((ae) -> {
            LoadData();
        });
    }
    
    
    public void loadGraphics(String title,boolean active){
        DefaultPieDataset dataPie = new DefaultPieDataset();
        Iterator it= dataInfo.keySet().iterator();
        while(it.hasNext()){
            String key=(String)it.next();
            String data=dataInfo.get(key);
            dataPie.setValue(key, Double.parseDouble(data));
        }
        JFreeChart pie=ChartFactory.createPieChart(title, dataPie,true,true,false);
        ChartPanel panel=new ChartPanel(pie);
        panel.setMouseWheelEnabled(true);
        panel.setSize(view.image1.getPreferredSize().width,view.image1.getPreferredSize().height);
        new WindowDesign().callPanel(panel, view.image1);
        
        JFreeChart line=ChartFactory.createTimeSeriesChart("Linea del Tiempo", "Fecha", "Bs.", dataSerie);
        ChartPanel p=new ChartPanel(line);
        p.setSize(255,258);
        p.setMouseWheelEnabled(true);
        new WindowDesign().callPanel(active ? p  : new JPanel(),view.image2);
        
    }
    
    public void test(){
        DefaultPieDataset data =new DefaultKeyedValuesDataset();
        data.setValue("C", 40);
        data.setValue("A", 50);
        data.setValue("B", 10);
        
        JFreeChart chart = ChartFactory.createPieChart("grafico de prueba", data,true,true,false);
        ChartPanel panel=new ChartPanel(chart);
        panel.setSize(view.image1.getPreferredSize().width,view.image1.getPreferredSize().height);
        panel.setMouseWheelEnabled(true);
        view.image1.add(panel);
        
        TimeSeries s1=new TimeSeries("años");
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);
        
        
        TimeSeriesCollection dataset=new TimeSeriesCollection();
        dataset.addSeries(s1);
        XYDataset d=null;
        JFreeChart qq=ChartFactory.createTimeSeriesChart("linea del tiempo", "años", "yy",dataset);
        ChartPanel q=new ChartPanel(qq);
        q.setSize(255,258);
        q.setMouseWheelEnabled(true);
        view.image2.add(q);
        ChartFrame frame=new ChartFrame("ventana",qq);
        frame.pack();
        frame.repaint();
        frame.setVisible(true);
    }
}
