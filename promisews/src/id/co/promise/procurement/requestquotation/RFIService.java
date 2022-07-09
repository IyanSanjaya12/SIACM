package id.co.promise.procurement.requestquotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.RequestQuotation;
import id.co.promise.procurement.entity.RequestQuotationStatus;
import id.co.promise.procurement.entity.RequestQuotationVendor;
import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/procurement/RequestQuotationServices")
@Produces(MediaType.APPLICATION_JSON)
public class RFIService {
	
	@EJB private RequestQuotationSession requestQuotationSession;
	@EJB private RequestQuotationVendorSession requestQuotationVendorSession;
	@EJB TokenSession tokenSession;
	
	@Path("/insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insert(
			RequestQuotation requestQuotation,
			@HeaderParam("Authorization") String token
			) {
		requestQuotation.setRequestQuotationStatus(RequestQuotationStatus.Received);
		RequestQuotation tempRequestQuotation = requestQuotationSession.insert(requestQuotation, tokenSession.findByToken(token));
		return Response.ok(tempRequestQuotation).build();
	}
	
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(
			RequestQuotation requestQuotation,
			@HeaderParam("Authorization") String token
			) {
		RequestQuotation tempRequestQuotation = requestQuotationSession.update(requestQuotation, tokenSession.findByToken(token));
		return Response.ok(tempRequestQuotation).build();
	}
	
	@Path("/updateVendor")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateVendor(
			RequestQuotationVendor requestQuotationVendor,
			@HeaderParam("Authorization") String token
			) {
		RequestQuotationVendor tempRequestQuotationVendor = requestQuotationVendorSession.update(requestQuotationVendor, tokenSession.findByToken(token));
		return Response.ok(tempRequestQuotationVendor).build();
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(
			RequestQuotation requestQuotation,
			@HeaderParam("Authorization") String token
			) {
		RequestQuotation tempRequestQuotation = requestQuotationSession.delete(requestQuotation.getId(), tokenSession.findByToken(token));
		return Response.ok(tempRequestQuotation).build();
	}
	
	@Path("/initSearch")
	@POST
	public Response initSearch(
			@FormParam("keywordSearch") String keywordSearch,
			@FormParam("startRecord") int startRecord,
			@FormParam("pageSize") int pageSize,
			@FormParam("orderKeyword") String orderKeyword
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Object[]> resultDataCount = requestQuotationSession.getCountAllByStatus();
		if (resultDataCount != null && resultDataCount.size() > 0) {
			for (Object[] statusResult : resultDataCount) {
				String name = String.valueOf( statusResult[0]);
				int count = ((Number) statusResult[1]).intValue();
				if (name == RequestQuotationStatus.Received.name()) {
					map.put(RequestQuotationStatus.Received.name(), count);
				} else {
					map.put(RequestQuotationStatus.Process.name(), count);
				}
			}
		}
		
		map.put("jmlData", requestQuotationSession.getTotalList(keywordSearch));
		map.put("requestQuotationList", requestQuotationSession.getPagingList(keywordSearch, startRecord, pageSize, orderKeyword));
		
		return Response.ok(map).build();
	}
	
	@Path("/initSearchVendor")
	@POST
	public Response initSearchVendor (
			@FormParam("vendorId") int vendorId,
			@FormParam("keywordSearch") String keywordSearch,
			@FormParam("startRecord") int startRecord,
			@FormParam("pageSize") int pageSize,
			@FormParam("orderKeyword") String orderKeyword
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jmlData", requestQuotationSession.getTotalListByVendor(vendorId, keywordSearch));
		map.put("requestQuotationList", requestQuotationSession.getListByVendor(vendorId, keywordSearch, startRecord, pageSize, orderKeyword));
		
		return Response.ok(map).build();
	}
	
}
