package service;

import java.net.http.HttpHeaders;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

import bll.Location;
import dal.BrancheDAL;
import dal.LocationDAL;

@Path("locationList")
public class LocationList {
	@Context
    private UriInfo context;
	
	public LocationList() {
    }
    
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        try {
            response.entity(new Gson().toJson(LocationDAL.getAll()));
        } catch (Exception e) {
            response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }
       
        
        System.out.println("======================webservice GET called");
        return response.build();
    }
    
    

}