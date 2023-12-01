/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.List;
import model.Empleado;
import model.TableJPA;
import persistent.Control;
import view.administracion.GestionEmpleadoView;

/**
 *
 * @author metallica
 */
public class GestionEmpleadoController {
    GestionEmpleadoView view;
    TableJPA<Empleado>modelTable;
    String column[]={"Ci","Apellidos","Nombres","Estado","Tipo"};
    String atrib[]={"ci","apellido","nombre","activoName","rol"};
    Boolean edit[]={false,false,false,false,false};
    Control control=new Control();

    public GestionEmpleadoController(GestionEmpleadoView v) {
        this.view=v;
        modelTable=new TableJPA(column,atrib,edit);
        modelTable.loadMethod(Empleado.class);
        view.tbEmpleado.setModel(modelTable);
        loadData();
    }
    
    public void initAction(){
        
    }
    
    public void loadData(){
        List<Empleado>l=control.empleado.findEmpleadoEntities();
        modelTable.setData(l);
    }
    
}
