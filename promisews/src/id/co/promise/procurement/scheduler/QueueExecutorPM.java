package id.co.promise.procurement.scheduler;

import java.util.Date;
import java.util.List;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.promise.procurement.DTO.FinalPurchaseOrderInterface;
import id.co.promise.procurement.DTO.FormFinalContractItem;
import id.co.promise.procurement.DTO.PurchaseOrderDtlInterface;
import id.co.promise.procurement.DTO.PurchaseOrderInterface;
import id.co.promise.procurement.DTO.PurchaseOrderItemInterface;
import id.co.promise.procurement.DTO.ShippingToDtlInterface;
import id.co.promise.procurement.DTO.ShippingToInterface;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.session.CatalogKontrakSession;
import id.co.promise.procurement.catalog.session.CatalogLocationSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.entity.BlacklistVendor;
import id.co.promise.procurement.entity.JenisKontrakPengadaan;
import id.co.promise.procurement.entity.Kontrak;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PerolehanPengadaanSatuan;
import id.co.promise.procurement.entity.PerolehanPengadaanTotal;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.entity.StoreJMS;
import id.co.promise.procurement.entity.TermAndCondition;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.inisialisasi.PurchaseRequestPengadaanSession;
import id.co.promise.procurement.kontrakmanajemen.KontrakSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.master.StoreJMSSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.penetapanpemenang.JenisKontrakPengadaanSession;
import id.co.promise.procurement.perolehan.PerolehanPengadaanSatuanSession;
import id.co.promise.procurement.perolehan.PerolehanPengadaanTotalSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderTermSession;
import id.co.promise.procurement.purchaseorder.ShippingToSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.VendorSession;
import id.co.promise.procurement.vendor.management.BlacklistVendorSession;

@Singleton
@ConcurrencyManagement(value = ConcurrencyManagementType.BEAN)
public class QueueExecutorPM {
	
	final static Logger log = Logger.getLogger(QueueExecutorPM.class);
	@EJB
	private KontrakSession kontrakSession;
	@EJB
	private BlacklistVendorSession blacklistVendorSession;
	@EJB
	private UserSession userSession;
	@EJB
	private StoreJMSSession storeJMSSession;
	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private TermAndConditionSession termAndConditionSession;
	@EJB
	private ShippingToSession shippingToSession;
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private PurchaseOrderTermSession purchaseOrderTermSession;
	@EJB
	private PerolehanPengadaanSatuanSession perolehanPengadaanSatuanSession;
	@EJB
	private PerolehanPengadaanTotalSession perolehanPengadaanTotalSession;
	@EJB
	private PurchaseRequestPengadaanSession purchaseRequestPengadaanSession;
	@EJB
	private JenisKontrakPengadaanSession jenisKontrakPengadaanSession;
	@EJB
	private CatalogSession catalogSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private CatalogKontrakSession catalogKontrakSession;
	@EJB
	private SatuanSession satuanSession;
	@EJB
	private MataUangSession mataUangSession;
	@EJB
	private CatalogLocationSession catalogLocationSession;

	// @Asynchronous
	@Lock(LockType.READ)
	public void execute() throws Exception {
		ResyncronizePurchaseOrderSchedule();
		Thread.sleep(1000);
		ResyncronizeContractItemSchedule();
	}

	// @Asynchronous
	@Lock(LockType.READ)
	public void executeVendor() throws Exception {
		blacklistVendorSchedule();
		Thread.sleep(1000);
	}

	public void blacklistVendorSchedule() throws InterruptedException {
		List<BlacklistVendor> blacklistVendorList = blacklistVendorSession.getBlacklistVendorList();
		for (BlacklistVendor blacklistVendor : blacklistVendorList) {
			if (new Date().after(blacklistVendor.getBlacklistVendorTglAkhir())) {
				Vendor vendor = blacklistVendor.getVendor();
				vendor.setStatusBlacklist(0);
				vendor.setCamelFlag(4);
				User user = userSession.getUser(vendor.getUser());
				user.setBlacklist(0);
				userSession.edit(user);

				blacklistVendor.setVendor(vendor);
				blacklistVendor.setBlacklistVendorStatus(3);
				blacklistVendorSession.edit(blacklistVendor);
			}
		}
	}

	/**
	 * Scheduler for get data from STOREBD with module FINALPO into Purchase
	 * Order form Contract Management
	 **/
	public void ResyncronizePurchaseOrderSchedule() throws InterruptedException {
		List<StoreJMS> storeJMSList = storeJMSSession.getStoreJMSListStatusFinalPurchaseOrderNewFailed();
		if (storeJMSList != null && storeJMSList.size() > 0) {
			for (StoreJMS storeJMS : storeJMSList) {
				if (!storeJMS.getStoreJmsData().isEmpty()) {
					try {

						log.info("################################################################");
						log.info("############# SCHEDULER FINAL PURCHASE ORDER ###################");
						log.info("################################################################");

						/** create contract request and other class needed **/
						Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
						FinalPurchaseOrderInterface finalPurchaseOrderInterface = gson
								.fromJson(storeJMS.getStoreJmsData(), FinalPurchaseOrderInterface.class);
						List<PurchaseOrderDtlInterface> purchaseOrderDtlInterfaceList = finalPurchaseOrderInterface
								.getPurchaseOrderDtlInterfaceList();

						for (PurchaseOrderDtlInterface purchaseOrderDtlInterface : purchaseOrderDtlInterfaceList) {
							PurchaseOrderInterface purchaseOrderInterface = purchaseOrderDtlInterface
									.getPurchaseOrderInterface();

							List<PurchaseOrder> purchaseOrderList = purchaseOrderSession
									.getPurchaseOrderListByTermin(purchaseOrderInterface.getTerminFk());
							if (purchaseOrderList.size() == 0) {

								/** set purchase order **/
								PurchaseOrder purchaseOrder = new PurchaseOrder();
								purchaseOrder.setKontrakFk(purchaseOrderInterface.getKontrakFk());
								purchaseOrder.setTerminFk(purchaseOrderInterface.getTerminFk());

								/** set currency **/
								MataUang mataUang = new MataUang();
								// get mata uang from perolehan
								PerolehanPengadaanTotal perolehanPengadaanTotal = perolehanPengadaanTotalSession
										.findByPengadaan(purchaseOrderInterface.getPengadaanId());
								mataUang = perolehanPengadaanTotal.getMataUang();
								purchaseOrder.setMataUang(mataUang);

								PurchaseRequest purchaseRequest = purchaseRequestSession
										.get(purchaseOrderInterface.getPurchaseRequestId());
								if (purchaseRequest != null) {
									/** update status purchase request **/
									purchaseRequest.setStatus(5);
									purchaseRequestSession.update(purchaseRequest, null);

									// set Afco
									purchaseOrder.setDepartment(purchaseRequest.getOrganisasi().getNama());
									purchaseOrder.setPurchaseRequest(purchaseRequest);
									purchaseOrder.setAddressLabel(purchaseRequest.getOrganisasi().getNama());
								}

								purchaseOrder.setStatus("Request From Contract");
								purchaseOrder.setStatusProses(1);
								purchaseOrder.setTerminFk(purchaseOrderInterface.getTerminFk());

								TermAndCondition termAndCondition = termAndConditionSession
										.getTermAndCondition(purchaseOrderInterface.getTermMasterPk());
								if (termAndCondition != null) {
									purchaseOrder.setTermandcondition(termAndCondition);
								}

								purchaseOrder.setPoNumber("");
								purchaseOrder.setSubTotal(purchaseOrderInterface.getSubTotal() == null ? 0
										: purchaseOrderInterface
												.getSubTotal());/*
																 * purchaseOrder.setTotalCost(purchaseOrderInterface.
																 * getTotalCost() == null ? 0 :
																 * purchaseOrderInterface.getTotalCost());
																 */
								purchaseOrder = purchaseOrderSession.insertPurchaseOrder(purchaseOrder, null);

								List<ShippingToDtlInterface> shippingToDtlInterfaceList = purchaseOrderDtlInterface
										.getShippingToDtlInterfaceList();

								for (ShippingToDtlInterface shippingToDtlInterface : shippingToDtlInterfaceList) {
									ShippingToInterface shippingToInterface = shippingToDtlInterface
											.getShippingToInterface();

									ShippingTo shippingTo = new ShippingTo();
									shippingTo.setOrganisasi(purchaseRequest.getOrganisasi());
									shippingTo.setPurchaseOrder(purchaseOrder);
									shippingTo.setDeliveryTime(shippingToInterface.getDeliveryTime());

									shippingTo = shippingToSession.inserShippingTo(shippingTo, null);

									List<PurchaseOrderItemInterface> purchaseOrderItemInterfaceList = shippingToDtlInterface
											.getPurchaseOrderItemInterfaceList();
									for (PurchaseOrderItemInterface purchaseOrderItemInterface : purchaseOrderItemInterfaceList) {

										PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
										purchaseOrderItem.setQuantityPurchaseRequest(
												purchaseOrderItemInterface.getTermQuantity());

										if (purchaseOrderItemInterface.getPurchaseRequestItemId() != null) {
											PurchaseRequestItem purchaseRequestItem = purchaseRequestItemSession
													.getPurchaseRequestItem(
															purchaseOrderItemInterface.getPurchaseRequestItemId());
											if (purchaseRequestItem != null) {
												purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItem);
												purchaseOrderItem.setItem(purchaseRequestItem.getItem());
												purchaseOrderItem.setItemName(purchaseRequestItem.getItemname());
											}
										}

										purchaseOrderItem.setUnitPrice(purchaseOrderItemInterface.getUnitPrice());
										purchaseOrderItem
												.setTotalUnitPrices(purchaseOrderItemInterface.getTotalUnitPrices());
										purchaseOrderItem.setQuantitySend(purchaseOrderItemInterface.getTermQuantity());

										if (purchaseOrderInterface.getVendorPk() != null) {
											Vendor vendor = vendorSession
													.getVendor(purchaseOrderInterface.getVendorPk());
											if (vendor != null) {
												purchaseOrderItem.setVendor(vendor);
												purchaseOrderItem.setVendorName(vendor.getNama());
											}
										}

										purchaseOrderItem.setPurchaseOrder(purchaseOrder);
										purchaseOrderItem.setShippingTo(shippingTo);
										purchaseOrderItem.setStatus("Request From Contract");
										purchaseOrderItem.setStatusProses(1);

										purchaseOrderItem = purchaseOrderItemSession
												.insertPurchaseOrderItem(purchaseOrderItem, null);
									}
								}

								PurchaseOrderTerm purchaseOrderTerm = new PurchaseOrderTerm();
								purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
								purchaseOrderTerm.setPoTermType(2);
								/** defaultnya value **/
								purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
								if (termAndCondition != null) {
									purchaseOrderTerm.setTermandcondition(termAndCondition);
								}

								purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, null);

								// insert data to contract
								Kontrak kontrak = new Kontrak();
								kontrak.setMataUang(mataUang);
								kontrak.setNilai(purchaseOrderInterface.getTotalCost());
								kontrak.setNomor((new Date()).getTime() + "");

								Pengadaan pengadaan = new Pengadaan();
								pengadaan.setId(purchaseOrderInterface.getPengadaanId());
								kontrak.setPengadaan(pengadaan);

								Vendor vendor = new Vendor();
								vendor.setId(purchaseOrderInterface.getVendorPk());
								kontrak.setVendor(vendor);
								kontrakSession.insertKontrak(kontrak, null);

							}
						}

						/** update status store Jms **/
						storeJMS.setStoreJmsStat(Constant.ONE_VALUE);
						storeJMS.setStoreJmsUpdated(new Date());
						storeJMS.setStoreJmsReason("OK");
						storeJMSSession.updateStoreJMS(storeJMS, null);
						log.info("############# SCHEDULER FINAL PURCHASE ORDER END ###############");
						log.info("################################################################");

					} catch (Exception e) {
						e.printStackTrace();
						log.info("################################################################");
						/** update status store Jms **/
						storeJMS.setStoreJmsStat(Constant.TWO_VALUE);
						storeJMS.setStoreJmsUpdated(new Date());
						storeJMS.setStoreJmsReason("error when saving");
						storeJMSSession.updateStoreJMS(storeJMS, null);
						log.info("############# SCHEDULER FINAL PURCHASE ORDER CATCH #############");
						log.info("################################################################");
					}

				}
			}
		}
	}

	/**
	 * Scheduler for get data from STOREBD with module FINALCONTRACTITEM into
	 * E-CATALOG ITEM form Contract Management
	 **/
	public void ResyncronizeContractItemSchedule() throws InterruptedException {
		List<StoreJMS> storeJMSList = storeJMSSession.getStoreJMSListStatusFinalContractItemNewFailed();
		if (storeJMSList != null && storeJMSList.size() > 0) {
			for (StoreJMS storeJMS : storeJMSList) {
				if (!storeJMS.getStoreJmsData().isEmpty()) {
					try {
						log.info("################################################################");
						log.info("############## SCHEDULER FINAL CONTRACT ITEM ###################");
						log.info("################################################################");

						Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
						FormFinalContractItem finalContractItem = gson.fromJson(storeJMS.getStoreJmsData(),
								FormFinalContractItem.class);
						Date tglKontrakConv = finalContractItem.getTglKontrak() == null ? new Date()
								: finalContractItem.getTglKontrak();
						String noDrafKontrak = finalContractItem.getNoDrafKontrak() == null ? ""
								: finalContractItem.getNoDrafKontrak();

						List<JenisKontrakPengadaan> jnsKtrkPList = jenisKontrakPengadaanSession
								.findByPengadaan(finalContractItem.getPengadaanId());

						if (jnsKtrkPList != null && jnsKtrkPList.size() > 0) {
							if (jnsKtrkPList.get(0).getJenisKontrak().getId() == 2) {

								Integer pengadaanId = finalContractItem.getPengadaanId();

								List<PurchaseRequestPengadaan> purchaseRequestPengadaan = purchaseRequestPengadaanSession
										.getListByPengadaanId(pengadaanId);

								if (purchaseRequestPengadaan.size() > 0) {
									// purchaseRequestPengadaan.get(0).getPurchaseRequest();

									PurchaseRequest purchaseRequest = purchaseRequestPengadaan.get(0)
											.getPurchaseRequest();
									if (purchaseRequest != null) {

										// check PR is Join
										if (purchaseRequest.getIsJoin() == 2) {
											// is was join procurement
											List<PurchaseRequestItem> prItemOriginalList = purchaseRequestItemSession
													.getPurchaseRequestItemByPurchaseRequestJoinId(
															purchaseRequest.getId());
											for (PurchaseRequestItem purchaseRequestItem : prItemOriginalList) {
												PurchaseRequest prNew = purchaseRequestItem.getPurchaserequest();
												prNew.setStatus(5);
												purchaseRequestSession.update(prNew, null);
											}

											// check jika tidak join pr
										} else {
											purchaseRequest.setStatus(5);
											purchaseRequestSession.update(purchaseRequest, null);
										}
									}

								}

								List<PerolehanPengadaanSatuan> t5PerolehanList = perolehanPengadaanSatuanSession
										.findByPengadaan(finalContractItem.getPengadaanId());
								if (t5PerolehanList != null && t5PerolehanList.size() > 0) {
									Catalog catalog = new Catalog();

									for (PerolehanPengadaanSatuan perolehan : t5PerolehanList) {
										List<PurchaseRequestPengadaan> t5prPList = purchaseRequestPengadaanSession
												.getListByPengadaanId(
														perolehan.getItemPengadaan().getPengadaan().getId());
										if (t5prPList != null && t5prPList.size() > 0) {
											int purchaseRequestId = t5prPList.get(0).getPurchaseRequest().getId();
											int vendorId = perolehan.getVendor().getId();
											int itemId = perolehan.getItemPengadaan().getItem().getId();
											List<PurchaseRequestItem> t4PrItmList = purchaseRequestItemSession
													.getPurchaseRequestItemByPurchaseRequestAndItemAndVendor(
															purchaseRequestId, itemId, vendorId);

											if (t4PrItmList != null & t4PrItmList.size() > 0) {
												catalog.setKodeProduk(t4PrItmList.get(0).getKode());
												catalog.setVendor(perolehan.getVendor());
												List<Catalog> catalogList = catalogSession.getCatalogList(catalog);

												if (catalogList != null && catalogList.size() > 0) {
													for (Catalog cat : catalogList) {
														Catalog tempCatalog = simpanCatalogWithItem(cat, null);
														//this.simpanCatalogKontrak(tempCatalog, cat, null);
													}
												} else {
													insertCatalogFromCM(t4PrItmList, catalog, perolehan, tglKontrakConv,
															noDrafKontrak, null); // Tidak
																					// menggunakan
																					// Item
																					// Catalog
												}
											} else {
												List<PurchaseRequestItem> t4PrItemList = purchaseRequestItemSession
														.getPurchaseRequestItemByPurchaseRequestAndItem(
																purchaseRequestId, itemId);
												insertCatalogFromCM(t4PrItemList, catalog, perolehan, tglKontrakConv,
														noDrafKontrak, null); // Tidak
																				// menggunakan
																				// Item
																				// Catalog
											}
										}
									}

								}
							}

							/** update status store Jms **/
							storeJMS.setStoreJmsStat(Constant.ONE_VALUE);
							storeJMS.setStoreJmsUpdated(new Date());
							storeJMS.setStoreJmsReason("OK");
							storeJMSSession.updateStoreJMS(storeJMS, null);
							log.info("############# SCHEDULER FINAL CONTRACT ITEM END ################");
							log.info("################################################################");
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.info("################################################################");
						storeJMS.setStoreJmsStat(Constant.TWO_VALUE);
						storeJMS.setStoreJmsUpdated(new Date());
						storeJMS.setStoreJmsReason("error when saving");
						storeJMSSession.updateStoreJMS(storeJMS, null);
						log.info("############# SCHEDULER FINAL CONTRACT ITEM CATCH ##############");
						log.info("################################################################");
					}
				}
			}
		}
	}

	protected Catalog simpanCatalogWithItem(Catalog catalog, Token token) {
		boolean isInsert = true;
		if (catalog.getVendor() != null) {
			Vendor vendor = vendorSession.getVendor(catalog.getVendor().getId());
			if (vendor != null) {
				catalog.setIsVendor(true);
				catalog.setStatus(true);
				isInsert = false;
			} else {
				// check kode panitia if exist
				catalog.setIsVendor(false);
				Catalog tempCatalog = new Catalog();
				tempCatalog.setKodeProdukPanitia(catalog.getKodeProdukPanitia());
				List<Catalog> checkCatalogList = catalogSession.getCatalogList(tempCatalog);
				if (checkCatalogList != null && checkCatalogList.size() > 0) {
					isInsert = false;
				}
			}
		} else {
			catalog.setIsVendor(false);
			catalog.setStatus(true);
			isInsert = false;
		}

		if (catalog.getItem() != null && catalog.getItem().getId() != null) {
			catalog.setItem(itemSession.getItem(catalog.getItem().getId()));
			catalog.setKodeItem(catalog.getItem().getKode());
		}
		if (catalog.getId() != null && isInsert == false) {
			return catalogSession.updateCatalog(catalog, token);
		} else {
			catalog.setId(null);
			return catalogSession.insertCatalog(catalog, token);
		}
	}

//	protected void simpanCatalogKontrak(Catalog catalogBackend, Catalog catalogFrontend, Token tempToken) {
//		CatalogKontrak catalogKontrak = new CatalogKontrak();
//		catalogKontrak.setCatalog(catalogBackend);
//		List<CatalogKontrak> catalogKontrakList = catalogKontrakSession.getCatalogKontrakList(catalogKontrak);
//
//		int lenOldKontrak = 0;
//		if (catalogKontrakList != null && catalogKontrakList.size() > 0) {
//			lenOldKontrak = catalogKontrakList.size();
//		}
//		int lenNewKontrak = 0;
//		if (catalogFrontend.getCatalogKontrakList() != null && catalogFrontend.getCatalogKontrakList().size() > 0) {
//			lenNewKontrak = catalogFrontend.getCatalogKontrakList().size();
//		}
//		if (lenOldKontrak >= lenNewKontrak) {
//			for (int a = 0; a < lenNewKontrak; a++) {
//				catalogKontrak = catalogKontrakList.get(a);
//				catalogKontrak.setCatalog(catalogBackend);
//				CatalogKontrak tempCatalogKontrak = catalogFrontend.getCatalogKontrakList().get(a);
//				catalogKontrak.setNomorKontrak(tempCatalogKontrak.getNomorKontrak());
//				catalogKontrak.setStock(tempCatalogKontrak.getStock());
//				catalogKontrak.setTglAkhirKontrak(tempCatalogKontrak.getTglAkhirKontrak());
//				catalogKontrakSession.updateCatalogKontrak(catalogKontrak, tempToken);
//			}
//			for (int a = lenNewKontrak; a < lenOldKontrak; a++) {
//				catalogKontrak = catalogKontrakList.get(a);
//				catalogKontrakSession.deleteRowCatalogKontrak(catalogKontrak.getId(), tempToken);
//			}
//		} else {
//			for (int a = 0; a < lenOldKontrak; a++) {
//				catalogKontrak = catalogKontrakList.get(a);
//				catalogKontrak.setCatalog(catalogBackend);
//				CatalogKontrak tempCatalogKontrak = catalogFrontend.getCatalogKontrakList().get(a);
//				catalogKontrak.setNomorKontrak(tempCatalogKontrak.getNomorKontrak());
//				catalogKontrak.setStock(tempCatalogKontrak.getStock());
//				catalogKontrak.setTglAkhirKontrak(tempCatalogKontrak.getTglAkhirKontrak());
//				catalogKontrakSession.updateCatalogKontrak(catalogKontrak, tempToken);
//			}
//			for (int a = lenOldKontrak; a < lenNewKontrak; a++) {
//				catalogKontrak = new CatalogKontrak();
//				catalogKontrak.setCatalog(catalogBackend);
//				CatalogKontrak tempCatalogKontrak = catalogFrontend.getCatalogKontrakList().get(a);
//				catalogKontrak.setNomorKontrak(tempCatalogKontrak.getNomorKontrak());
//				catalogKontrak.setStock(tempCatalogKontrak.getStock());
//				catalogKontrak.setTglAkhirKontrak(tempCatalogKontrak.getTglAkhirKontrak());
//				catalogKontrak.setIsDelete(0);
//				catalogKontrakSession.insertCatalogKontrak(catalogKontrak, tempToken);
//			}
//		}
//	}

	private Catalog insertCatalogFromCM(List<PurchaseRequestItem> t4PrItemList, Catalog catalog,
			PerolehanPengadaanSatuan perolehan, Date tglKontrakConv, String noDrafKontrak, Token tempToken) {
		Catalog hasilSimpan = new Catalog();
		if (t4PrItemList != null && t4PrItemList.size() > 0) {
			for (PurchaseRequestItem prItem : t4PrItemList) {
				String kodePanitia = "PROC-BARANG-" + new Date().getTime(); // Generator
																			// Sementara
																			// karena
																			// belum
																			// ada
																			// Standard
				String kodeProduk = "VENDOR-" + perolehan.getVendor().getId() + "-" + new Date().getTime(); // Generator
																											// Sementara
																											// karena
																											// belum
																											// ada
																											// Standard

				catalog.setNamaIND(prItem.getItemname());
				catalog.setNamaENG(prItem.getItemname());
				catalog.setDeskripsiIND(prItem.getSpecification());
				catalog.setDeskripsiENG(prItem.getSpecification());
				catalog.setKodeProdukPanitia(kodePanitia);
				catalog.setKodeItem(prItem.getKode());
				catalog.setKodeProduk(kodeProduk);
				catalog.setVendor(perolehan.getVendor());
				catalog.setIsVendor(false);
				catalog.setHarga(prItem.getPrice());
				catalog.setMataUang(prItem.getMataUang());
				catalog.setItem(perolehan.getItemPengadaan().getItem());
				catalog.setStatus(true);
				catalog.setApprovalStatus(1);
				catalog.setUser(userSession.find(perolehan.getUserId()));

				List<Satuan> satuanList = satuanSession.getSatuanListByNameEqual(prItem.getUnit());
				if (satuanList != null && satuanList.size() > 0) {
					catalog.setSatuan(satuanList.get(0));
				}

				hasilSimpan = catalogSession.insertCatalog(catalog, tempToken);
				if (hasilSimpan != null) {
					String noKontrak = "Sementara-" + new Date().getTime();
					if (noDrafKontrak != null) {
						noKontrak = noDrafKontrak;
					}
					Date akhirKontrak = tglKontrakConv;
					CatalogKontrak catKontrak = new CatalogKontrak();
					catKontrak.setStock(prItem.getQuantity());
					catKontrak.setNomorKontrak(noKontrak);
					catKontrak.setTglAkhirKontrak(akhirKontrak);
					catalogKontrakSession.insertCatalogKontrak(catKontrak, tempToken); // Simpan
																						// Kontrak
																						// nya
					
					CatalogLocation catalogLocation = new CatalogLocation();
					catalogLocation.setCatalog(catalog);
					catalogLocation.setStockProduct( new Double(prItem.getQuantity()));
					catalogLocation.setIsDelete(0);
					catalogLocationSession.insertCatalogLocation(catalogLocation, tempToken);
				}

			}
		}
		return hasilSimpan;
	}
}
