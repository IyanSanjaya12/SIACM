package id.co.promise.procurement.catalog;

import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.StaticUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Stateless
@TransactionManagement( TransactionManagementType.BEAN )
@Path(value = "/catalog/CategoryPortalServices")
@Produces(MediaType.APPLICATION_JSON)
public class MasterCategoryPortalServices {
	
	@EJB private CategorySession categorySession;
	@EJB private CatalogCategorySession catalogCategorySession;
	
	@EJB private TokenSession tokenSession;
	
	@Resource private EJBContext context;
	
	@Path("/findAllWithTree")
	@POST
	public List<Category> findAllWithTree () {
		List<Category> categoryList = categorySession.getCategoryForTreeList(null);
		List<Category> categoryTreeList = new ArrayList<Category>();
		
		if (categoryList != null && categoryList.size() > 0) {
			for (Category category : categoryList) {
				if (category.getParentCategory() == null) {
					Category tempCategory = new Category();
					tempCategory.setId(category.getId());
					tempCategory.setAdminLabel(category.getAdminLabel());
					tempCategory.setDescription(category.getDescription());
					tempCategory.setFlagEnabled(category.getFlagEnabled());
					tempCategory.setIsDelete(category.getIsDelete());
					tempCategory.setTranslateEng(category.getTranslateEng());
					tempCategory.setTranslateInd(category.getTranslateInd());
					tempCategory.setValue(category.getValue());
					
					categoryTreeList.add(tempCategory);
				} else {
					StaticUtility.populateTreeList(category, categoryTreeList, false);
				}
			}
		}
		return categoryTreeList;
	}
	
	@Path("/findCategoryWithTree")
	@POST
	public List<Category> findCategoryWithTree (Category categoryFront) {
		List<Category> categoryList = categorySession.getCategoryForTreeList(null);
		List<Category> categoryTreeList = new ArrayList<Category>();
		
		if (categoryList != null && categoryList.size() > 0) {
			for (Category category : categoryList) {
				if(categoryFront.getTranslateInd().equals(category.getTranslateInd())) {
					if (category.getParentCategory() != null) {
						Category currentCategory = new Category();
						currentCategory.setId(category.getId());
						currentCategory.setAdminLabel(category.getAdminLabel());
						currentCategory.setDescription(category.getDescription());
						currentCategory.setFlagEnabled(category.getFlagEnabled());
						currentCategory.setIsDelete(category.getIsDelete());
						currentCategory.setTranslateEng(category.getTranslateEng());
						currentCategory.setTranslateInd(category.getTranslateInd());
						currentCategory.setValue(category.getValue());
						currentCategory.setParentCategory(category.getParentCategory());
						
						categoryTreeList.add(currentCategory);
						
						for (Category category2 : categoryList) {
							if(category2.getParentCategory() != null) {
								StaticUtility.populateTreeList(category2, categoryTreeList, false);
							}
						}
						
						for(int i=0; i < categoryTreeList.size(); i++) {
							if(categoryTreeList.get(i).getParentCategory() != null) {
								Category parentCategories = new Category();
								parentCategories.setId(categoryTreeList.get(i).getParentCategory().getId());
								parentCategories.setAdminLabel(categoryTreeList.get(i).getParentCategory().getAdminLabel());
								parentCategories.setDescription(categoryTreeList.get(i).getParentCategory().getDescription());
								parentCategories.setFlagEnabled(categoryTreeList.get(i).getParentCategory().getFlagEnabled());
								parentCategories.setIsDelete(categoryTreeList.get(i).getParentCategory().getIsDelete());
								parentCategories.setTranslateEng(categoryTreeList.get(i).getParentCategory().getTranslateEng());
								parentCategories.setTranslateInd(categoryTreeList.get(i).getParentCategory().getTranslateInd());
								parentCategories.setValue(categoryTreeList.get(i).getParentCategory().getValue());
								parentCategories.setParentCategory(categoryTreeList.get(i).getParentCategory().getParentCategory());
								categoryTreeList.add(parentCategories);
							}
						}
						break;
					} else {
						
						categoryTreeList.add(categoryFront);
						for (Category category2 : categoryList) {
							if(category2.getParentCategory() != null) {
								StaticUtility.populateTreeList(category2, categoryTreeList, false);
							}
						}
						
					}
				}
			}
		}
		Collections.reverse(categoryTreeList);
		
		return categoryTreeList;
	}
	
	
	
	@Path("/findAll")
	@GET
	public List<Category> findAll () {
		return categorySession.getCategoryList();
	}
	
	@Path("/findCategoryByParent")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Category findParent (Category category) {
		return categorySession.find(category.getParentCategory().getId());
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Category save (Category category, @HeaderParam("Authorization") String token) {
		Token tempToken = tokenSession.findByToken(token);
		
		if (category.getId() != null) {
			return categorySession.updateCategory(category, tempToken);
		} else {
			return categorySession.insertCategory(category, tempToken);
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Category delete (Category category, @HeaderParam("Authorization") String token) {
		CatalogCategory catCat = new CatalogCategory();
		catCat.setCategory(category);
		List<CatalogCategory> catCatList = catalogCategorySession.getCatalogCategoryList(catCat);
		
		Category tempCat = new Category();
		tempCat.setParentCategory(category);
		List<Category> catList = categorySession.getCategoryList(tempCat);
		
		if(catCatList != null && catCatList.size() > 0) {
			return new Category();
		} else {
			if(catList != null && catList.size() > 0) {
				return new Category();
			} else {
				return categorySession.deleteCategory(category.getId(), tokenSession.findByToken(token));
			}
		}
	}
	
	@POST
	@Path("/readExcellCategory")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts 				= uploadForm.get("file");
		Boolean hasil 							= false;

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName 	= TemplateXls.getFileNameTemplateXls(header);
			Workbook workbook 	= null;
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
	        	if(fileName.endsWith("xlsx") || fileName.endsWith("xls")){
	        		workbook = new XSSFWorkbook(inputStream);
	        		hasil = analyseExcelForCategory(workbook, token);
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(hasil) {
			return Response.ok().build();
		} else {
			return Response.status(406).entity("File Upload Error and Not Acceptable").build();

		}
	}
	
	private Boolean analyseExcelForCategory(Workbook workbook, String token) {
    	try{
    		
    		Sheet datatypeSheet = workbook.getSheetAt(0);
	        Row row ;
	        for(int i=1;i<=datatypeSheet.getLastRowNum();i++){
	        	row = datatypeSheet.getRow(i);
	        	
	        	String description = row.getCell(Constant.TEMPLATE_CATEGORY_DESCRIPTION).toString();
	        	String translateInd = row.getCell(Constant.TEMPLATE_CATEGORY_TRANSLATE_IND).toString();
	        	String translateEng = row.getCell(Constant.TEMPLATE_CATEGORY_TRANSLATE_ENG).toString();
	        	String parentCategory = null;
	        	if(row.getCell(Constant.TEMPLATE_CATEGORY_PARENT_CATEGORY) != null)
	        		parentCategory = row.getCell(Constant.TEMPLATE_CATEGORY_PARENT_CATEGORY).toString();
	        	Boolean flagEnabled = true;
	        	
	        	Category categoryNew = new Category();
	        	categoryNew.setDescription(description);
	        	categoryNew.setAdminLabel(translateInd);
	        	categoryNew.setTranslateInd(translateInd);
	        	categoryNew.setTranslateEng(translateEng);
	        	categoryNew.setFlagEnabled(flagEnabled);
	        	
	        	if(parentCategory != null) {
	        		Category parentCat = new Category();
		        	parentCat.setAdminLabel(parentCategory);
		        	List<Category> categoryParentList = categorySession.getCategoryList(parentCat);
		        	if(categoryParentList != null && categoryParentList.size() > 0) {
		        		categoryNew.setParentCategory(categoryParentList.get(0));
		        	} else {
		        		// Jika Parent Category Belum Ada, langsung Disimpan Sebagai Parent Category
		        		parentCat.setTranslateInd(parentCategory);
		        		parentCat.setFlagEnabled(true);
		        		Category pCategory = categorySession.insertCategory(parentCat, tokenSession.findByToken(token));
		        		if(pCategory != null) {
		        			categoryNew.setParentCategory(pCategory);
		        		}
		        	}
	        	}
	        	
	        	categorySession.insertCategory(categoryNew, tokenSession.findByToken(token));
	        }
    		
	        return true;
    	}catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }
}

