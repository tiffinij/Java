/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

/**
 *
 * @author tiffinijohnson
 */
public class VendingDaoPersistenceException extends Exception{
    
    public VendingDaoPersistenceException (String message){
        super(message);
    }
    
    public VendingDaoPersistenceException (String message, Throwable cause){
        super(message, cause);
    }
}