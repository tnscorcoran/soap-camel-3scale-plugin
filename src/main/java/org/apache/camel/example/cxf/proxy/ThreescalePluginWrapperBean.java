package org.apache.camel.example.cxf.proxy;

import org.apache.camel.Exchange;

//import threescale.v3.api.ServiceApi;
//import threescale.v3.api.impl.ServiceApiDriver;
import net.threescale.service.PluginService;
import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ServerError;


public class ThreescalePluginWrapperBean {

	//ServiceApi serviceApi = ServiceApiDriver.createApi();
	private PluginService pluginService;
	

	private String error;
	
	public void process(Exchange exchange)
	{

		
		String user_key = (String)exchange.getIn().getHeader("user_key"); 
		
		AuthorizeResponse response = null;
		try {
			//response = serviceApi.authrep(serviceToken, serviceID, params);
			response = pluginService.authRep(user_key, "hits");
			
			//null response means we didn't wait for 3scale to respond - cache had a success from previous call.
			if (response==null) {
				System.out.println("AuthRep on API Key Success - cached authorization used");
			}
			else if (response!= null && response.success() == true) {
				System.out.println("AuthRep on API Key Success - synchronous call to the manager");
			}
			else if (response!= null && !response.success() == true) {
				System.out.println("AuthRep on API Key Failure - call NOT AUTHORIZED AND REJECTED");
				throw new RuntimeException("Call not authorized by 3scale");
			}
			
		} catch (ServerError serverError) {
			serverError.printStackTrace();
		}
	}


	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

    
    public PluginService getPluginService() {
        return pluginService;
    }

    public void setPluginService(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    
}
