package com.abhilashmishra.urlfy2

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by abhilash.mishra on 6/16/17.
 */

class SharedPreferenceUtility private constructor() {

    fun updateUrlList(list: MutableList<LinkData>?) {
        val json = Gson().toJson(list)
        sharedPreferences?.edit()?.putString(SAVED_LINK_KEY, json)?.apply()
    }

    val savedLinks: MutableList<LinkData>
        get() {
            val str = """
                [
                   {
                      "linkTitle":"Discovery",
                      "linkUrl":"swiggy://explore?query=pizza"
                   },
                   {
                      "linkTitle":"Favourites",
                      "linkUrl":"swiggy://favorites"
                   },
                   {
                      "linkTitle":"Profile",
                      "linkUrl":"swiggy://profile"
                   },
                   {
                      "linkTitle":"Invite",
                      "linkUrl":"swiggy://invite"
                   },
                   {
                      "linkTitle":"Collection",
                      "linkUrl":"swiggy://collection?collection_id=1&collection_name=Trending"
                   },
                   {
                      "linkTitle":"Cart",
                      "linkUrl":"swiggy://cart"
                   },
                   {
                      "linkTitle":"Payments",
                      "linkUrl":"swiggy://payments"
                   },
                   {
                      "linkTitle":"Filters",
                      "linkUrl":"swiggy://filter?CUISINES=Andhra,American&sortBy=DELIVERY_TIME"
                   },
                   {
                      "linkTitle":"Menu",
                      "linkUrl":"swiggy://menu?restaurant_id=954"
                   },
                   {
                      "linkTitle":"Restaurant List",
                      "linkUrl":"swiggy://restaurantList?lat=12.9279232&lng=77.6271077999999"
                   },
                   {
                      "linkTitle":"Offers",
                      "linkUrl":"swiggy://offers"
                   },
                   {
                      "linkTitle":"Help",
                      "linkUrl":"swiggy://support"
                   },
                   {
                      "linkTitle":"Help With OrderId",
                      "linkUrl":"swiggy://support?ordId=1092528740"
                   },
                   {
                      "linkTitle":"Stores",
                      "linkUrl":"swiggy://stores/go"
                   },
                   {
                      "linkTitle":"Track Order",
                      "linkUrl":"swiggy://track-order/<order-id>"
                   },
                   {
                      "linkTitle":"Trackorder HTTPS",
                      "linkUrl":"https://www.swiggy.com/order-track/<order-id>"
                   },
                   {
                      "linkTitle":"Trackorder HTTP",
                      "linkUrl":"http://www.swiggy.com/track-order/<order-id>"
                   },
                   {
                      "linkTitle":"Help HTTPS",
                      "linkUrl":"https://www.swiggy.com/support"
                   },
                   {
                      "linkTitle":"Help HTTP",
                      "linkUrl":"http://www.swiggy.com/support"
                   },
                   {
                      "linkTitle":"Restaurant List",
                      "linkUrl":"swiggy://restaurantList"
                   },
                   {
                      "linkTitle":"In- app update",
                      "linkUrl":"swiggy://in-app-update"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=HEALTH_HUB"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=OFFER&header_title=Offers&show_location=true"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=Offers&header_title=Offers&show_location=false"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=RESTO_SAFETY&header_title=Similar+Restaurants&show_location=false"
                   },
                   {
                      "linkTitle":"Instamart",
                      "linkUrl":"swiggy://stores/instamart"
                   },
                   {
                      "linkTitle":"KYC",
                      "linkUrl":"swiggy://stores/kyc"
                   },
                   {
                      "linkTitle":"Instamart Search",
                      "linkUrl":"swiggy://stores/instamart/search?query=coke"
                   },
                   {
                      "linkTitle":"Stores Search",
                      "linkUrl":"swiggy://stores/search?q=Milk"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=PREMIUM"
                   },
                   {
                      "linkTitle":"Rating",
                      "linkUrl":"swiggy://rateApp?rate_title=Loving Swiggy?&&rate_subtitle=Show us your love by rating us on the Play Store :)&&rate_skip_title=skip for now&&rate_button_title=rate us on play store"
                   },
                   {
                      "linkTitle":"Webview",
                      "linkUrl":"swiggy://webviewV2?webview_url=https://events.swiggy.com"
                   },
                   {
                      "linkTitle":"Webview",
                      "linkUrl":"swiggy://webview?webview_url=https://events.swiggy.com"
                   },
                   {
                      "linkTitle":"Landing page",
                      "linkUrl":"swiggy://fusion-landing?id=GOURMET-MVP&show_location=true"
                   },
                   {
                      "linkTitle":"Genie Landing page",
                      "linkUrl":"http://swiggy.com/swiggy-genie"
                   },
                   {
                      "linkTitle":"Genie Landing page",
                      "linkUrl":"http://www.swiggy.com/swiggy-genie"
                   }
                ]
            """.trimIndent()
//            val str = sharedPreferences?.getString(SAVED_LINK_KEY, "[{\"linkTitle\":\"youtube\",\"linkUrl\":\"https://youtu" +
//                ".be/-4Czk00xoTc\"}," +
//                "{\"linkTitle\":\"playstore\",\"linkUrl\":\"market://details?id=in.swiggy.android&referrer=utm_source%3DabhilashMishra%26utm_medium%3DabhilashMishraApp\"}]")
            val json = sharedPreferences?.getString(SAVED_LINK_KEY, str)
            val listType = object : TypeToken<ArrayList<LinkData>>() {
            }.type
            return Gson().fromJson<ArrayList<LinkData>>(json, listType)
        }

    companion object {
        private const val PREFERENCE_NAME = "preferences"
        private const val SAVED_LINK_KEY = "savedLinks"
        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferenceUtility: SharedPreferenceUtility? = null

        fun getSharedPreferenceUtility(context: Context): SharedPreferenceUtility {
            if (sharedPreferenceUtility == null) {
                sharedPreferenceUtility = SharedPreferenceUtility()
                sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
            }
            return sharedPreferenceUtility as SharedPreferenceUtility
        }
    }
}
