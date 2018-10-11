package com.apap.tutorial5.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.service.FlightService;
import com.apap.tutorial5.service.PilotService;

/**
 *  FlightController
 *  @author Priscilla Tiffany
 */
@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
	private List<FlightModel> tempFlight;
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		FlightModel flight = new FlightModel();
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		flight.setPilot(pilot);
		tempFlight = pilot.getPilotFlight();
		pilot.setPilotFlight(new ArrayList<FlightModel>());
		pilot.addPilotFlight(flight);
				
		model.addAttribute("pilot", pilot);
		return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params= {"save"})
	private String addFlightSubmit(@ModelAttribute PilotModel pilot) {
		for(FlightModel flight: tempFlight) {
			pilot.addPilotFlight(flight);
		}
		for(FlightModel flight : pilot.getPilotFlight()) {
			flight.setPilot(pilot);
			flightService.addFlight(flight);
		}
		return "add";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"addRow"}, method = RequestMethod.POST)
	public String addRow(@ModelAttribute PilotModel pilot, BindingResult bindingResult, Model model) {
		pilot.getPilotFlight().add(new FlightModel());
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"removeRow"})
	public String removeRow(@ModelAttribute PilotModel pilot, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	    pilot.getPilotFlight().remove(rowId.intValue());
	    
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
	@RequestMapping(value = "/flight/delete/", method = RequestMethod.POST)
	private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
		for(FlightModel flight : pilot.getPilotFlight()) {
			flightService.deleteFlight(flight);
		}
		return "delete";
	}
	
//	@RequestMapping(value = "/flight/delete/{id}", method = RequestMethod.GET)
//	private String delete(@PathVariable(value = "id") Long id, Model model) {
//		FlightModel flight = flightService.findFlightById(id);
//		flightService.deleteFlight(flight);
//		return "delete";
//	}
	
	@RequestMapping(value = "/flight/update/{id}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "id") Long id, Model model) {
		FlightModel flight = flightService.findFlightById(id);
		model.addAttribute("flight", flight);
		return "updateFlight";
	}
	
	@RequestMapping(value = "/flight/update/{id}", method = RequestMethod.POST)
	private String updateFlightSubmit(@PathVariable(value = "id") Long id,
									  @RequestParam("flightNumber") String flightNumber,
									  @RequestParam("origin") String origin,
									  @RequestParam("destination") String destination,
									  @RequestParam("time") Date time, Model model) {
		FlightModel flight = flightService.findFlightById(id);
		flightService.updateFlight(flight, flightNumber, origin, destination, time);
		return "update";
	}

	@RequestMapping(value = "/flight/view", method = RequestMethod.GET)
	private String viewFlightByName(@RequestParam("flightNumber") String flightNumber, Model model) {
		List<FlightModel> flight = flightService.findFlightByName(flightNumber);
		model.addAttribute("flight", flight);
		return "view-flight";
	}
	
}
