/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiffinijohnson
 */
public interface VendingDao {
     
    Item getItem();
    
    Item selectItem(long id);
    
    Item removeOneStock() throws VendingDaoPersistenceException;   
    
    List<Item> getVendingMachineItems() throws VendingDaoPersistenceException;
    
    BigDecimal getTotal();
    
    BigDecimal addToTotal(BigDecimal amount);
    
    Map<Coins, Integer> getChange(Item item, BigDecimal total) throws VendingDaoPersistenceException;
    
    Map<Coins, Integer> getChangeMap();
  
    
    
    
    
}
