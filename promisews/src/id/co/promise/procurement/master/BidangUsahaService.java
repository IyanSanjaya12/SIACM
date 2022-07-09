package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.StaticUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/master/bidang-usaha")
@Produces(MediaType.APPLICATION_JSON)
public class BidangUsahaService {
	
	@EJB private BidangUsahaSession bidangUsahaSession;
	@EJB private TokenSession token;
	@EJB private TokenSession tokenSession;
	@EJB private SubBidangUsahaSession suBidangUsahaSession;
	
	
	@Path("/get-list")
	@GET
	public List<BidangUsaha> getBidangUsahaList(){
		return bidangUsahaSession.getBidangUsahaList();
	}
	
	@Path("/get-all")
	@GET
	public String bidangUsahaGetAll(){
		return bidangUsahaSession.bidangUsahaGetAll();
	}
	
	@Path("/find-all-with-tree-for-list-vendor")
	@GET
	public List<BidangUsaha> findAllWithTreeForListVendor () {
		List<BidangUsaha> bidangUsahaList = bidangUsahaSession.getBidangUsahaForTreeList();
		
		for (BidangUsaha bidangUsaha : bidangUsahaList) {
			List<SubBidangUsaha> subBidangUsahaList = suBidangUsahaSession.getSubBidangUsahaByBidangUsahaId(bidangUsaha.getId());
			List<BidangUsaha> childList = new ArrayList<BidangUsaha>();
			for (SubBidangUsaha sb : subBidangUsahaList) {
				BidangUsaha bu = new BidangUsaha();
				bu.setId(sb.getId());
				bu.setNama(sb.getNama());
				childList.add(bu);
			}
			bidangUsaha.setBidangUsahaChildList(childList);
		}
		
		return bidangUsahaList;
	}
	
	@Path("/get-bidang-usaha-by-parent-id/{id}")
	@GET
	public List<BidangUsaha> getBidangUsahaByParentIdList(@PathParam("id")int id){
		return bidangUsahaSession.getBidangUsahaByParentIdList(id);
	}
	
	@Path("/get-bidang-usaha-by-nama/{nama}")
	@GET
	public BidangUsaha getBidangUsahaByNama(@PathParam("nama") String nama) {
		return bidangUsahaSession.getBidangUsahaByNama(nama);
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(BidangUsaha bidangUsaha,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
	  
		Boolean isSaveNama = bidangUsahaSession.checkNamaBidangUsaha(bidangUsaha.getNama(), toDo, bidangUsaha.getId());
	  
		if(!isSaveNama) {
			String errorNama = "Nama Bidang Usaha Sudah Ada";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	  
		if(isSaveNama) {
			bidangUsahaSession.insertBidangUsaha(bidangUsaha, tokenSession.findByToken(token));
		}
	  
		map.put("BidangUsaha", bidangUsaha);
	  
	  
		return map;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(BidangUsaha bidangUsaha,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
	  
		Boolean isSaveNama = bidangUsahaSession.checkNamaBidangUsaha(bidangUsaha.getNama(), toDo, bidangUsaha.getId());
	  
		if(!isSaveNama) {
			String errorNama = "Nama Bidang Usaha Sudah Ada";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	  
		if(isSaveNama) {
		 	bidangUsaha.setIsDelete(0);
		 	bidangUsahaSession.updateBidangUsaha(bidangUsaha, tokenSession.findByToken(token));
		}
	  
		map.put("BidangUsaha", bidangUsaha);
	  
	  
		return map;
	}
	
	@Path("/delete")
	@POST
	public BidangUsaha delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return bidangUsahaSession.deleteBidangUsaha(id, tokenSession.findByToken(token));
	}
	
	@Path("/delete-bidang-usaha/{id}")
	@GET
	public BidangUsaha deleteBidangUsaha(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){		
		return bidangUsahaSession.deleteBidangUsaha(id, tokenSession.findByToken(token));
	}
	
	@Path("/delete-row-bidang-usaha/{id}")
	@GET
	public BidangUsaha deleteRowBidangUsaha(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return bidangUsahaSession.deleteRowBidangUsaha(id, tokenSession.findByToken(token));
	}
	
	
}
