/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

import com.sg.vendingmachinespringmvc.dao.Coins;
import com.sg.vendingmachinespringmvc.dao.VendingDaoPersistenceException;
import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * @author tiffinijohnson
 */
public interface VendingService {
        
    String purchaseMessage() throws VendingDaoPersistenceException;
    
    String getChangeMessage();
    
    String getMessage();
    
    Item getItem();
    
    List<Item> getVendingMachineItems() throws VendingDaoPersistenceException;
    
    Item selectItem(long id);
    
    BigDecimal addToTotal(String amount);
    
    BigDecimal getTotal();
    
    Map<Coins, Integer> getChange() throws VendingDaoPersistenceException;
}
