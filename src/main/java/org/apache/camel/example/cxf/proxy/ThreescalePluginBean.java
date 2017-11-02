package org.apache.camel.example.cxf.proxy;

import org.apache.camel.Exchange;

import net.threescale.service.PluginService;
import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ParameterMap;
import threescale.v3.api.ServerError;
import threescale.v3.api.ServiceApi;
import threescale.v3.api.impl.ServiceApiDriver;


public class ThreescalePluginBean {

	ServiceApi serviceApi = ServiceApiDriver.createApi();	
	
	private String serviceToken;
	private String serviceID;
	private String error;
	
	public void process(Exchange exchange)
	{

		String user_key = (String)exchange.getIn().getHeader("user_key");
		ParameterMap params = new ParameterMap(); // the parameters of your call
		params.add("user_key", user_key);		
	
		ParameterMap usage = new ParameterMap(); // Add a metric to the call
		usage.add("hits", "1");
		params.add("usage", usage); // metrics belong inside the usage parameter
		
		
		AuthorizeResponse response = null;
		// the 'preferred way' of calling the backend: authrep
		try {
			response = serviceApi.authrep(serviceToken, serviceID, params);
			System.out.println("AuthRep on App Id Success: " + response.success());
			if (response.success() == true) {
				// your api access got authorized and the traffic added to
				// 3scale backend
				System.out.println("Plan: " + response.getPlan());
			} else {
				// your api access did not authorized, check why
				System.out.println("Error: " + response.getErrorCode());
				System.out.println("Reason: " + response.getReason());
				//TODO throw a security exception to interrupt the flow.
			}
		} catch (ServerError serverError) {
			serverError.printStackTrace();
		}
	}


	public String getServiceToken() {
		return serviceToken;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
    

    
}
