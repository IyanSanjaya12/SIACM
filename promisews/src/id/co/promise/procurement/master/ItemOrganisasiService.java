package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.ItemOrganisasi;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;


@Stateless
@Path(value = "/procurement/master/item-organisasi")
@Produces(MediaType.APPLICATION_JSON)
public class ItemOrganisasiService {
	@EJB
	ItemOrganisasiSession itemOrganisasiSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@Path("/get-list-item-organisasi-by-item-kode")
	@POST
	public List<ItemOrganisasi> getListItemOrganisasiByItem(String itemKode) {
		List<ItemOrganisasi> itemOrganisasiList = itemOrganisasiSession.getItemOrganisasiListByItemKode(itemKode);
		return itemOrganisasiList;
	}
	
	@Path("/get-list-item-organisasi-for-add-cart")
	@POST
	public ItemOrganisasi getItemOrganisasiForAddCart(Integer itemId, @HeaderParam("Authorization") String token) {
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		Organisasi organisasi = organisasiSession.getRootParentByOrganisasi(roleUser.getOrganisasi().getId());
		List<ItemOrganisasi> itemOrganisasiList = itemOrganisasiSession.getItemOrganisasiListByItemId(itemId, organisasi.getId());
		if (itemOrganisasiList.size() > 0) {
			return itemOrganisasiList.get(0);
		}
		else {
			return null;
		}
	}
}
