package com.tognyp.projekt.controller;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tognyp.projekt.model.Address;
import com.tognyp.projekt.model.StreetCity;

@Controller
@RequestMapping("/ulicaOrazMiasto")
public class StreetCityController {
	
	@GetMapping("/ulicaMiasto")
	public String insertStreetOrCity(Model theModel) {
		
		StreetCity streetCity = new StreetCity();
		
		List<String> districts = new ArrayList<>();
        districts.add("okręg warszawski (0x-xxx)");
        districts.add("okręg olsztyński (1x-xxx)");
        districts.add("okręg lubelski (2x-xxx)");
        districts.add("okręg krakowski (3x-xxx)");
        districts.add("okręg katowicki (4x-xxx)");
        districts.add("okręg wrocławski (5x-xxx)");
        districts.add("okręg poznański (6x-xxx)");
        districts.add("okręg szczeciński (7x-xxx)");
        districts.add("okręg gdański (8x-xxx)");
        districts.add("okręg łódzki (9x-xxx)");
		
		theModel.addAttribute("streetCity", streetCity);
		theModel.addAttribute("districts", districts);
		
		return "insertStreetOrCity";
	}
	
	@PostMapping("/showZip")
	public String showZip(@ModelAttribute("streetCity") StreetCity streetCity, Model theModel) {
		
		List<Address> addressList = new ArrayList<>();
		
		Element doc = null;
		
        try{
        	switch(streetCity.getDistrict()){
        		case "okręg warszawski (0x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/").get();
        			break;
        		case "okręg olsztyński (1x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg1.php").get();
        			break;
        		case "okręg lubelski (2x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg2.php").get();
        			break;
        		case "okręg krakowski (3x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg3.php").get();
        			break;
        		case "okręg katowicki (4x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg4.php").get();
        			break;
        		case "okręg wrocławski (5x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg5.php").get();
        			break;
        		case "okręg poznański (6x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg6.php").get();
        			break;
        		case "okręg szczeciński (7x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg7.php").get();
        			break;
        		case "okręg gdański (8x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg8.php").get();
        			break;
        		case "okręg łódzki (9x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg9.php").get();
        			break;
        	}
        }catch (Exception e){
        	System.out.println("Błąd połączenia");
        }
        Element table = doc.getElementById("page-table");
        Elements rows = table.getElementsByTag("tr");
        
        for(Element e : rows) {
        	Elements td = e.getElementsByTag("td");
        	if(td.get(2).text().equalsIgnoreCase(streetCity.getCity().trim()) &&
        	   td.get(1).text().contains(streetCity.getStreet().trim())) {
        		Address tmpAddress = new Address();
        		tmpAddress.setZipCode(td.get(0).text());
        		tmpAddress.setStreet(td.get(1).text());
        		tmpAddress.setCity(td.get(2).text());
        		tmpAddress.setVoivodeship(td.get(3).text());
        		tmpAddress.setCounty(td.get(4).text());
        		addressList.add(tmpAddress);
        	}
        }

		if(addressList.isEmpty()) {
			return "noResult";
		}
		theModel.addAttribute("addressList", addressList);
		
		return "showAddress";
	}
}
