package com.epf.rentmanager.servlet;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientService clientService;

	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	private final VehicleService vehicleService = VehicleService.getInstance();
	private final ReservationService reservationService = ReservationService.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
//		ClientService clientService = context.getBean(ClientService.class);

        List<Client> clientList;
		List<Vehicle> vehicleList;
		List<Reservation> reservationList;
        try {
            clientList = clientService.findAll();
			vehicleList = vehicleService.findAll();
			reservationList = reservationService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("nbClients", clientList.size());
		request.setAttribute("nbVehicles", vehicleList.size());
		request.setAttribute("nbReservations", reservationList.size());

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

}
