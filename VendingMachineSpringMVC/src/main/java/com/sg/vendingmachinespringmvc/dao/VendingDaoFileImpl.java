/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

import com.sg.vendingmachinespringmvc.model.Item;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



/**
 *
 * @author tiffinijohnson
 */
public class VendingDaoFileImpl implements VendingDao {

    private List<Item> items = new ArrayList();
    private BigDecimal moneyInserted = new BigDecimal("0.00");
    private Map<String, String> itemAndCost = new HashMap();
    private Map<Coins, Integer> changeMap = new HashMap();
    private Item currentItem = new Item();
    public static final String DELIMITER = "::";

//method for loading stock from the inventory file in the resources folder
    private void loadStock() throws VendingDaoPersistenceException {
        Scanner myScanner;
        items.clear();
        File f = new File(getClass().getClassLoader().getResource("inventory.txt").getFile());
        try {
            myScanner = new Scanner(new BufferedReader(
                    new FileReader(f)));
        } catch (FileNotFoundException e) {
            throw new VendingDaoPersistenceException("Could not load stock "
                    + "data into memory.", e);
        }

        String currentLine;
        String[] currentToken;
        while (myScanner.hasNextLine()) {
            currentLine = myScanner.nextLine();
            currentToken = currentLine.split(DELIMITER);
            Item currentItem = new Item();
            String stringId = currentToken[0];
            long id = Long.parseLong(stringId);
            currentItem.setId(id);
            currentItem.setName(currentToken[1]);
            currentItem.setPrice(currentToken[2]);
            String stringQ = currentToken[3];
            int quantity = Integer.parseInt(stringQ);
            currentItem.setQuantity(quantity);
            items.add(currentItem);
        }
        myScanner.close();
    }

//method for writing to the stock file for quantity changes    
    private void writeStock() throws VendingDaoPersistenceException {
    
    PrintWriter out;
    File f = new File(getClass().getClassLoader().getResource("inventory.txt").getFile());
    try {
        out = new PrintWriter(new FileWriter(f));
    } catch (IOException e) {
        throw new VendingDaoPersistenceException("Could not save stock data.", e);
    }

    
    for (Item i : items) {
        out.println(i.getId() + DELIMITER + i.getName() + DELIMITER + i.getPrice()
                + DELIMITER + i.getQuantity());

        out.flush();
    }
    out.close();
}


//method for calling the load stock function from before    
    @Override
    public List<Item> getVendingMachineItems() throws VendingDaoPersistenceException {
        loadStock();
        return items;
    }

//method for adding together money/coin selections to the total enntered box
    @Override
    public BigDecimal addToTotal(BigDecimal amount) {
        changeMap.clear();
        moneyInserted = moneyInserted.add(amount).setScale(2, RoundingMode.HALF_UP);
        return moneyInserted;
    }
    
//method for substracting the cost of the item from the 
    @Override
    public Map<Coins, Integer> getChange(Item item, BigDecimal total) throws VendingDaoPersistenceException {
        changeMap.clear();
        BigDecimal zeroSum = new BigDecimal("0.00");

        if (total.compareTo(item.getPrice()) >= 0) {
            total = total.subtract(item.getPrice().setScale(2, RoundingMode.HALF_UP));
        }
        if (changeMap.isEmpty()) {
            changeMap.put(Coins.QUARTER, 0);
            changeMap.put(Coins.DIME, 0);
            changeMap.put(Coins.NICKEL, 0);
            changeMap.put(Coins.PENNY, 0);
        }
        while (total.compareTo(zeroSum) > 0) {

            if (total.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.25")) >= 0) {
                int appendValue = changeMap.get(Coins.QUARTER);
                appendValue++;
                changeMap.put(Coins.QUARTER, appendValue);
                total = total.subtract(new BigDecimal("0.25").setScale(2, RoundingMode.HALF_UP));
            } else if (total.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.10")) >= 0) {
                int appendValue = changeMap.get(Coins.DIME);
                appendValue++;
                changeMap.put(Coins.DIME, appendValue);
                total = total.subtract(new BigDecimal("0.10").setScale(2, RoundingMode.HALF_UP));
            } else if (total.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.05")) >= 0) {
                int appendValue = changeMap.get(Coins.NICKEL);
                appendValue++;
                changeMap.put(Coins.NICKEL, appendValue);
                total = total.subtract(new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP));
            } else if (total.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP)) >= 0) {
                int appendValue = changeMap.get(Coins.PENNY);
                appendValue++;
                changeMap.put(Coins.PENNY, appendValue);
                total = total.subtract(new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP));
            } else if (total.setScale(2, RoundingMode.HALF_UP).compareTo(zeroSum) < 0) {
                throw new VendingDaoPersistenceException("ERROR: "
                        + "Change was miscalculated");
            }
        }
        moneyInserted = new BigDecimal("0.00");

        return changeMap;
    }

    @Override
    public Map<Coins, Integer> getChangeMap() {
        return changeMap;
    }

    @Override
    public Item getItem() {
        return currentItem;
    }

    @Override
    public Item selectItem(long id) {
        for(Item item : items) {
            if (item.getId() == id){
                currentItem = item;
            }
        }
        changeMap.clear();
        return currentItem;
    }

    @Override
    public BigDecimal getTotal() {
        return moneyInserted;
    }

    @Override
    public Item removeOneStock() throws VendingDaoPersistenceException {
        int stock = currentItem.getQuantity();
        currentItem.setQuantity(stock - 1);
        int index = (int) currentItem.getId();
        items.set(index - 1, currentItem);
        writeStock();
        currentItem = new Item();
        return currentItem;
    }
}