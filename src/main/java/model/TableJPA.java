/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author metallica
 * @param <T> tipo de Objeto que manejara la clase Genererica
 */
public class TableJPA<T> implements TableModel{
    
    private EventListenerList listEvent;
    List<T>list=new ArrayList<>();
    List<String>columns;
    List<Boolean> canEdit;
    List<String> atribName;
    
    List<Method> getters=new ArrayList<>();

    public TableJPA(List data,String [] columns,String []atribName,Boolean edit[]){
        this.list=data;
        this.atribName=Arrays.asList(atribName);
        this.columns=Arrays.asList(columns);
        this.canEdit=Arrays.asList(edit);
        listEvent=new EventListenerList();
        T obj = list.get(0);
        try{
            for(String name : atribName){
                Method get=obj.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1));
                getters.add(get);
            }
        }catch(NoSuchMethodException | SecurityException e){
            System.out.println("error al cargar lista de metodos para reflexion: "+e);
        }
    }
    
    public TableJPA(String [] columns,String []atribName, Boolean edit[]){
        this.atribName=Arrays.asList(atribName);
        this.columns=Arrays.asList(columns);
        this.canEdit=Arrays.asList(edit);
        listEvent=new EventListenerList();
    }
    
    public void loadMethod(Class<T> o){
        try{
            T obj =o.newInstance();
            for(String name : atribName){
                Method get=obj.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1));
                getters.add(get);
            }
        }catch(IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException e){
            System.out.println("error al cargar lista de metodos para reflexion: "+e);
        }
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
        //Object q= list.get(i);
        //return q.toString().split(" - ")[i1];
        
        T obj = list.get(i);
        try{
            Method getterMethod = getters.get(i1);
            return getterMethod.invoke(obj);
        }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            System.out.println("error al hacer la reflexion: "+e);
            return null;
        }
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
        notifyTableChanged(e);
    }
    /////////////////////////////////////
}
