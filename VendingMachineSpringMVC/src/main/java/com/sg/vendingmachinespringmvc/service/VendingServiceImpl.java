/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

import com.sg.vendingmachinespringmvc.dao.Coins;
import com.sg.vendingmachinespringmvc.dao.VendingDao;
import com.sg.vendingmachinespringmvc.dao.VendingDaoPersistenceException;
import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author tiffinijohnson
 */
public class VendingServiceImpl implements VendingService {

    VendingDao dao;
    private String message;
    private String changeMessage;

    @Inject
    public VendingServiceImpl(VendingDao dao) {
        this.dao = dao;
    }

    @Override
    public String purchaseMessage() throws VendingDaoPersistenceException{
        message = "";
        changeMessage = "";
        int number = validateItemAndCost(getItem(), getTotal());
        switch (number) {
            case 1:
                message =  "SOLD OUT!!!";
                break;
            case 2:
                BigDecimal moneyRequired = getItem().getPrice().subtract(getTotal()).setScale(2, RoundingMode.HALF_UP);
                String output = moneyRequired.toString();
                message =  "Please deposit: $" + output;
                break;
            case 3:
                dao.getChange(getItem(), getTotal());
                getChangeMessage();
                dao.removeOneStock();
                message = "Thank You";
                break;
            case 4:
                message =  "Please select an item";
                break;
            default:
                message =  "ERROR: could not process";
        }
        return message;
    }
    
    @Override
    public String getMessage(){
        return message;
    }
    
    @Override
    public Item getItem(){
        return dao.getItem();
    }
    
    @Override
    public Item selectItem(long id){
        message = "";
        return dao.selectItem(id);
    }
    
    @Override 
    public List<Item> getVendingMachineItems() throws VendingDaoPersistenceException{
        return dao.getVendingMachineItems();
    }
    
    @Override
    public BigDecimal addToTotal(String addMoney){
        if (addMoney.equals("Add Dollar")) {
            String stringMoney = ("1.00");
            BigDecimal money = new BigDecimal(stringMoney);
            return dao.addToTotal(money);
        }
        
        if (addMoney.equals("Add Quarter")) {
            String stringMoney = ("0.25");
            BigDecimal money = new BigDecimal(stringMoney);
            return dao.addToTotal(money);
        }
        
        if (addMoney.equals("Add Dime")) {
            String stringMoney = ("0.10");
            BigDecimal money = new BigDecimal(stringMoney);
            return dao.addToTotal(money);
        }
        
        if (addMoney.equals("Add Nickel")) {
            String stringMoney = ("0.05");
            BigDecimal money = new BigDecimal(stringMoney);
            return dao.addToTotal(money);
        }
        return dao.addToTotal(new BigDecimal("0.00"));
    }
    
    @Override
    public BigDecimal getTotal(){
        return dao.getTotal();
    }
    
    @Override
    public Map<Coins, Integer> getChange() throws VendingDaoPersistenceException{
        Item item = new Item();
        item.setPrice("0.00");
        message = "";
        return dao.getChange(item, getTotal());
    }

    @Override
    public String getChangeMessage() {
        changeMessage = "";
        Map<Coins, Integer> changeMap = new HashMap();
        changeMap = dao.getChangeMap();
        if(changeMap.isEmpty()){
            return changeMessage;
        }
        int count = 0;
        for (Integer x : changeMap.values()){
            count = count + x;
        }
        if (count == 0){
            changeMessage = "No Change";
            return changeMessage;
        }
        String output = " ";
        if (changeMap.get(Coins.QUARTER) > 0) {
            output += changeMap.get(Coins.QUARTER) + " quarters ";
        }
        if (changeMap.get(Coins.NICKEL) > 0) {
            output += changeMap.get(Coins.NICKEL) + " nickels ";
        }
        if (changeMap.get(Coins.DIME) > 0) {
            output += changeMap.get(Coins.DIME) + " dimes ";
        }
        if (changeMap.get(Coins.PENNY) > 0) {
            output += changeMap.get(Coins.PENNY) + " pennies";
        }
        changeMessage = output;
        return changeMessage;
    }

    private int validateItemAndCost(Item item, BigDecimal total) {
        int howMany = 0;
        if (item.getName() != null) {
            howMany = item.getQuantity();
        } else {
            return 4;
        }
        if (howMany == 0) {
            return 1;
        }

        BigDecimal cost = item.getPrice();
        if (total == null || total.compareTo(cost) < 0) {
            return 2;
        }
        return 3;
    }

}