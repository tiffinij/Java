package com.sg.vendingmachinespringmvc;

import com.sg.vendingmachinespringmvc.dao.VendingDaoPersistenceException;
import com.sg.vendingmachinespringmvc.model.Item;
import com.sg.vendingmachinespringmvc.service.VendingService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VendingController {

    VendingService service;

    @Inject
    public VendingController(VendingService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<Item> items = new ArrayList();
        BigDecimal moneyInserted;
        Item currentItem = service.getItem();
        model.addAttribute("selectedItem", currentItem.getName());
        String output = service.getChangeMessage();
        model.addAttribute("change", output);
        String message = service.getMessage();
        model.addAttribute("message", message);
        try {
            items = service.getVendingMachineItems();
            moneyInserted = service.getTotal();
            model.addAttribute("items", items);
            model.addAttribute("moneyInserted", moneyInserted);
        } catch (VendingDaoPersistenceException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "index";
    }

    @RequestMapping(value = "/addMoney", method = RequestMethod.GET)
    public String addMoney(Model model, @RequestParam String addMoney) {
        service.addToTotal(addMoney);
        return index(model);
    }

    @RequestMapping(value = "/itemSelect", method = RequestMethod.GET)
    public String selectItem(HttpServletRequest request) {
        String stringId = request.getParameter("itemButton");
        long id = Long.parseLong(stringId);
        service.selectItem(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/getChange", method = RequestMethod.GET)
    public String getChange() throws VendingDaoPersistenceException {
        service.getChange();
        return "redirect:/";
    }

    @RequestMapping(value = "/makePurchase", method = RequestMethod.GET)
    public String makePurchase() throws VendingDaoPersistenceException {
        service.purchaseMessage();
        return "redirect:/";
    }

}