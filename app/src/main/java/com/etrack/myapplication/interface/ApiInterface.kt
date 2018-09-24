package com.etrack.myapplication.`interface`

import com.etrack.myapplication.model.inputService.*
import com.etrack.myapplication.model.outputService.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Hussain on 19-02-2018.
 */

interface ApiInterface {

    //Create company
    @Headers("Content-Type:application/json")
    @POST("company/Save_Company")
    fun createCompany(@Body createCompany: CreateCompany): Call<SuccessFailure>

    //register citizen && guest
    @Headers("Content-Type:application/json")
    @POST("Request/Register_muser")
    fun registerItemApi(@Body assignModel: AddCatogorey): Call<SuccessFailure>

    //Create Group catogorey
    @Headers("Content-Type:application/json")
    @POST("category/Save_Category")
    fun createCatagorey(@Body addCatogorey: AddCatogorey): Call<SuccessFailure>

    //Create Group catogorey
    @Headers("Content-Type:application/json")
    @POST("company/Save_Branch")
    fun createBranch(@Body addBranch: AddBranch): Call<SuccessFailure>

    //get all catogorey
    @Headers("Content-Type:application/json")
    @POST("category/Get_Category")
    fun getAllCatogoreyList(@Body catogorey: GetCatogorey): Call<GetCatogoreySuccessFailure>

    //get all Account list by company
    @Headers("Content-Type:application/json")
    @POST("account/Get_Account")
    fun getAllAccountList(@Body deleteAccountin: GetAccountList): Call<GetAllAccount>

    //Login
    @Headers("Content-Type:application/json")
    @POST("user/Login")
    fun login(@Body login: Login): Call<LoginSuccessFailure>

    //get All Products
    @Headers("Content-Type:application/json")
    @POST("item/Get_Item")
    fun getAllProductsList(@Body item: GetItem): Call<GetItemListSuccessFailure>

    //get next bill No
    @Headers("Content-Type:application/json")
    @POST("item/GeneratePOCode")
    fun getNextBillNo(@Body item: GetBillNo): Call<GetBillNoGroup>
    //get next bill No
    @Headers("Content-Type:application/json")
    @POST("item/GenerateSOCode")
    fun getNextSalesBillNo(@Body item: GetBillNo): Call<GetBillNoGroup>

    //Get full branches list
    @Headers("Content-Type:application/json")
    @POST("company/Get_Branch")
    fun getAllBranch(@Body getBranches: GetBranchs): Call<GetBranchsSuccessFailure>

    //Create New user
    @Headers("Content-Type:application/json")
    @POST("user/Save_User")
    fun createNewUser(@Body createNewUser: CreateNewUser): Call<SuccessFailure>

    @Headers("Content-Type:application/json")
    @POST("item/Save_Item")
    fun saveItemApi(@Body saveItem: SaveItem): Call<SuccessFailure>


    @Headers("Content-Type:application/json")
    @POST("account/Save_Account")
    fun saveAccountApi(@Body saveAccount: SaveAccount): Call<SuccessFailure>

    //Create company
    @Headers("Content-Type:application/json")
    @POST("user/Is_UserNameAlreadyExist")
    fun checkUserAvailable(@Body checkUserAvailable: CheckUserAvailable): Call<SuccessFailure>

    //Delete branch byId
    @Headers("Content-Type:application/json")
    @POST("company/Delete_Branch")
    fun deleteBranch(@Body deleteBranch: DeleteBranch): Call<SuccessFailure>


    //Delete catgorey  by Id
    @Headers("Content-Type:application/json")
    @POST("category/Delete_Category")
    fun deleteCatog(@Body deleteCatog: DeleteCatog): Call<SuccessFailure>

    //Delete Account  by Id
    @Headers("Content-Type:application/json")
    @POST("account/Delete_Account")
    fun deleteAccount(@Body deleteCatog: DeleteAccount): Call<SuccessFailure>

    //Delete item byId
    @Headers("Content-Type:application/json")
    @POST("item/Delete_Item")
    fun deleteItem(@Body deleteItem: DeleteItem): Call<SuccessFailure>

    //Delete item byId
    @Headers("Content-Type:application/json")
    @POST("Account/Get_AccountGroup")
    fun getAllAcctTypeList(): Call<GetGroupAccount>

    //save purchase bill
    @Headers("Content-Type:application/json")
    @POST("item/SavePurchaseOrder")
    fun savePurchaseOrder(@Body item: PurchaseOrder): Call<SuccessFailure>

    //save sales bill
    @Headers("Content-Type:application/json")
    @POST("item/SaveSalesOrder")
    fun saveSalesOrder(@Body item: SalesOrder): Call<SuccessFailure>

    //stock adjustment  bill
    @Headers("Content-Type:application/json")
    @POST("item/SaveStockAdjust")
    fun saveStockAdjustment(@Body item: StockAjustment): Call<SuccessFailure>


    //get stock bill No
    @Headers("Content-Type:application/json")
    @POST("item/GenerateSTACode")
    fun getStockBillNo(@Body item: GetBillNo): Call<SuccessFailure>

    //get bill report by purchase order
    @Headers("Content-Type:application/json")
    @POST("report/Get_Bill")
    fun getReportbyBilpo(@Body reportByBill: ReportByBill): Call<GetRepotBIllPO>

    //get bill report by sales order
    @Headers("Content-Type:application/json")
    @POST("report/Get_Bill")
    fun getReportbyBilSo(@Body reportByBill: ReportByBill): Call<GetRepotBillSO>

    //get bill report by stock ajustment
    @Headers("Content-Type:application/json")
    @POST("report/Get_Bill")
    fun getReportbyBilSta(@Body reportByBill: ReportByBill): Call<GetRepotBillSta>

    //get item report by purchase order
    @Headers("Content-Type:application/json")
    @POST("report/Get_BillByItem")
    fun getReportbyItemPo(@Body reportByBill: ReportByItem): Call<GetRepotItemPO>

    //get item report by sales order
    @Headers("Content-Type:application/json")
    @POST("report/Get_BillByItem")
    fun getReportbyItemSo(@Body reportByBill: ReportByItem): Call<GetRepotItemSO>

    //get bill report by stock ajustment
    @Headers("Content-Type:application/json")
    @POST("report/Get_BillByItem")
    fun getReportbyItemSta(@Body reportByBill: ReportByItem): Call<GetRepotItemSta>

    //get bill report by Item list
    @Headers("Content-Type:application/json")
    @POST("report/Get_ItemByCategory")
    fun getReportbyItemCat(@Body reportByItemCat: ReportByItemCat): Call<GetReportItemCat>


    //get bill report by Item list
    @Headers("Content-Type:application/json")
    @POST("report/Get_ItemHistory ")
    fun getReportbyItemHistory(@Body reportByItemHist:GetReportItemHist ): Call<ReportItemHist>

}
