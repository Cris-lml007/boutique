/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author metallica
 */
public class TableJPA implements TableModel{

    List<Class<?>>list;
    String columns[];
    boolean canEdit[];

    public TableJPA(List data,String [] columns,boolean edit[]){
        list=data;
        this.columns=columns;
        this.canEdit=edit;
    }
    
    public Object getObject(int i){
        return list.get(i);
    }
    

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int i) {
        return columns[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return canEdit[i1];
    }        

    @Override
    public Object getValueAt(int i, int i1) {
        Object q= list.get(i);
        return q.toString().split(" - ")[i1];
        
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {

    }
}
