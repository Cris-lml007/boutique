/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.List;
import model.Item;
import persistent.Control;
import view.BuscarItemView;

/**
 *
 * @author metallica
 */
public class BuscarItemController {
    
    BuscarItemView view;
    Control control=new Control();
    
    public BuscarItemController(BuscarItemView v){
        view=v;
    }
    
}
