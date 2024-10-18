package com.walhalla.smsregclient.network;

import com.walhalla.smsregclient.network.badbackend.ResponseWrapper;
import com.walhalla.smsregclient.network.response.Balance;
import com.walhalla.smsregclient.network.beans.GetStateModel;
import com.walhalla.smsregclient.network.response.GlobalResponse;
import com.walhalla.smsregclient.network.beans.ListOrders;
import com.walhalla.smsregclient.network.response.NumModel;
import com.walhalla.smsregclient.network.beans.OperationBean;
import com.walhalla.smsregclient.network.beans.OrderAccOkModel;
import com.walhalla.smsregclient.network.beans.OrderAccReviseModel;
import com.walhalla.smsregclient.network.response.OrderAddModel;
import com.walhalla.smsregclient.network.beans.RateModel;
import com.walhalla.smsregclient.network.response.ReadyModel;
import com.walhalla.smsregclient.network.response.VsimGetModel;
import com.walhalla.smsregclient.network.beans.VsimGetSMSModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Created by combo on 07.04.2017.
 */

public interface APIService {


    @GET("getNumRepeatOffline")
    Call<GlobalResponse> getNumRepeatOffline(
            @Query("apikey") String signature,
            @Query("tzid") int tzid,
            @Query("type") int type //1 or 0
    );

    /**
     * Работа с номерами для активаций
     *
     * @param options
     * @return
     */
    @GET("/getNum.php")
    Call<NumModel> getNum(
            @Query("apikey") String signature,
            @QueryMap Map<String, String> options
            //@Query("country") String country,//all ru
            //@Query("service") String service

    );

    /**
     * Cоздает операцию на использование виртуального номера.
     * Сообщает что вы отправили смс на номер и готовы получить код поступивший в этом смс .
     *
     * @param tzid
     * @return
     */
    @GET("/setReady.php")
    Call<ReadyModel> setReady(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );

    @GET("/getState.php")
    Call<GetStateModel> getState(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );//Позволяет получить информацию о состоянии операции.

    //    @Headers({
//            CACHE_HEADER,
//            USER_AGENT
//    })
    @GET("/getOperations.php")
    Call<ResponseWrapper<OperationBean[]>> getOperations(
            @Query("apikey") String signature,
            @Query("opstate") String opstate,//active | completed
            @Query("count") int count//100 ... 1000
    );//Возвращает список ваших операций и их состояние.
    //Отправляет уведомление об успешном получении кода.

    @GET("/setOperationOk.php")
    Call<GlobalResponse> setOperationOk(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );

    @GET("/setOperationRevise.php")
    Call<GlobalResponse> setOperationRevise(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );//Создает запрос на уточнение правильности кода.
    //Завершает операцию с уведомлением о неверном коде.

    @GET("/setOperationOver.php")
    Call<GlobalResponse> setOperationOver(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );

    @GET("/getNumRepeat.php")
    Call<GlobalResponse> getNumRepeat(
            @Query("apikey") String signature,
            @Query("tzid") int tzid
    );//Cоздает операцию на повторное использование ранее использованного номера.
    //Cоздает заказ на оффлайн-повтор по ранее использованному номеру.

    @GET("/setOperationUsed.php")
    Call<GlobalResponse> setOperationUsed(
            @Query("apikey") String signature,
            @Query("tzid") String tzid
    );
    //Сообщает, что выданный номер уже использован или заблокирован
    // в сервисе для которого запрашивалась активация.

    //==========================================================================================
    //Работа с VirtualSiM
    @GET("/vsimGet.php")
    //# Создает новый заказ на аренду виртуального номера VirtualSiM.
    Call<VsimGetModel> vsimGet(
            @Query("apikey") String signature,
            @QueryMap Map<String, String> data
//            @Query("country") String country,
//            @Query("period") String period
    );

    @GET("/vsimGetSMS.php")
    Call<VsimGetSMSModel> vsimGetSMS(
            @Query("apikey") String signature,
            @Query("number") String number
    );//Возвращает список полученных SMS на арендуемый номер.


    //===========================================================================================
    //<----------- tab2
    //Работа с заказами
    @POST("/orderAdd.php")
    Call<OrderAddModel> orderAdd(
            @Query("apikey") String signature,
            @QueryMap Map<String, String> options
            /*
            @Query("count") int count,
            @Query("country") String country,
            @Query("service") String service,
            @Query("options") String on,

            @Query("name") String name,
            @Query("age") int age,
            @Query("gender") String gender,
            @Query("city") String[] city
    */
    );//Создает новый заказ на регистрацию готовых аккаунтов.

    @GET("/orderGetByID.php")
    Call<GlobalResponse> orderGetByID(
            @Query("apikey") String signature,
            @Query("order_id") int order_id//Идентификатор заказа.
    );//Возвращает список аккаунтов из заказа.


    //  - not
    @GET("/listOrders.php")
    Call<ListOrders> listOrders(
            @Query("apikey") String signature,
            @Query("count") int count //default 10
    );//Возвращает список заказов и информацию по ним.

    //  - not
    @GET("/setOrderAccOk.php")
    Call<OrderAccOkModel> setOrderAccOk(
            @Query("apikey") String signature,
            @Query("histid") int histid
    );

    //Отправляет подтверждение о правильности принятого аккаунта.
    //  - not
    @GET("/setOrderAccRevise.php")
    Call<OrderAccReviseModel> setOrderAccRevise(
            @Query("apikey") String signature,
            @Query("histid") int histid
    );
    //Создает запрос на уточнение данных по аккаунту.


    //=====================================================
    //Аккаунт
    // Возвращает информацию о состоянии баланса.
    @GET("/getBalance.php")
    Call<Balance> getBalance(
            @Query("apikey") String signature
    );


    @GET("/setRate.php")
    Call<RateModel> setRate(
            @Query("apikey") String signature,
            @Query("rate") float rate
    );
    //Устанавливает новое значение персональной ставки.
}