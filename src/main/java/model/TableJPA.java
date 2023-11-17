/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author metallica
 */
public class TableJPA<T> implements TableModel{
    
    private EventListenerList listEvent;
    List<T>list=new ArrayList<>();
    List<String>columns;
    List<Boolean> canEdit;

    public TableJPA(List data,String [] columns,Boolean edit[]){
        this.list=data;
        this.columns=Arrays.asList(columns);
        this.canEdit=Arrays.asList(edit);
        listEvent=new EventListenerList();
    }
    
    public TableJPA(String [] columns, Boolean edit[]){
        this.columns=Arrays.asList(columns);
        this.canEdit=Arrays.asList(edit);
        listEvent=new EventListenerList();
    }
    
    public void addElement(T obj){
        this.list.add(obj);
        notifyTableRowsInserted(list.size()-1, list.size()-1);
    }
    
    public void removeElement(Object obj){
        int i=this.list.indexOf(obj);
        this.list.remove(obj);
        notifyTableRowsDeleted(i, i);
    }
    
    public void removeIndex(int i){
        this.list.remove(i);
        notifyTableRowsDeleted(i, i);
    }
    
    public void update(T obj){
        T[] elements=(T[])list.toArray();
        for(int i=0;i<elements.length;i++){
            if(elements[i]==obj){
                notifyTableRowsUpdated(i, i);
            }
        }
    }
    
    public void clear(){
        if(!list.isEmpty()){
            int i=list.size()-1;
            list.clear();
            notifyTableRowsDeleted(0, i);
        }
    }
    
    public void setColumnIdentifiers(List columns){
        this.columns.clear();
        this.columns.addAll(columns);
        notifyTableHeaderChanged();
    }
    
    public T getObject(int i){
        return list.get(i);
    }
    

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int i) {
        return columns.get(i);
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return i1<canEdit.size() ? canEdit.get(i1) : true;
    }        

    @Override
    public Object getValueAt(int i, int i1) {
        Object q= list.get(i);
        return q.toString().split(" - ")[i1];
        
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
        listEvent.add(TableModelListener.class,tl);
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
        listEvent.remove(TableModelListener.class, tl);
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        //Object obj[]=list.get(i);
    }

    public List<T> getAll(){
        return list;
    }
    
    //////Events Listeners/////////////
    protected void notifyTableChanged(TableModelEvent e){
        TableModelListener [] listener=listEvent.getListeners(TableModelListener.class);
        for(int i=listener.length-1;i>=0;i--){
            listener[i].tableChanged(e);
        }
    }
    
    protected void notifyTableHeaderChanged(){
        TableModelEvent e=new TableModelEvent(this,TableModelEvent.HEADER_ROW);
        notifyTableChanged(e);
    }

    protected void notifyTableRowsInserted(int firstRow, int lastRow) {
        TableModelEvent e = new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        notifyTableChanged(e);
    }
    
    protected void notifyTableRowsUpdated(int frow,int lrow){
        TableModelEvent e=new TableModelEvent(this,frow,lrow,TableModelEvent.ALL_COLUMNS,TableModelEvent.UPDATE);
        notifyTableChanged(e);
    }
    
    protected void notifyTableRowsDeleted(int firstRow, int lastRow) {
        TableModelEvent e = new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        notifyTableChanged(e);
    }
    
    protected void notifyTableCellUpdated(int row, int column) {
        TableModelEvent e = new TableModelEvent(this, row, row, column);
    }
    /////////////////////////////////////
}
