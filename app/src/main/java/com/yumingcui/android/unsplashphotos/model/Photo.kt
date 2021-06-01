package com.yumingcui.android.unsplashphotos.model
import com.google.gson.annotations.SerializedName

import java.io.Serializable


data class Photo (

	@SerializedName("id") val id : String

): Serializable{
	@SerializedName("created_at") val createdAt : String?=null
	@SerializedName("updated_at") val updatedAt : String?=null
	@SerializedName("width") val width : Int?=null
	@SerializedName("height") val height : Int?=null
	@SerializedName("color") val color : String?=null
	@SerializedName("description") val description : String?=null
	@SerializedName("alt_description") val altDescription : String?=null
	@SerializedName("urls") val urls : Urls?=null
	@SerializedName("links") val links : Links?=null
	@SerializedName("categories") val categories : List<String>?=null
	@SerializedName("sponsored") val sponsored : Boolean?=null
	@SerializedName("sponsored_by") val sponsoredBy : String?=null
	@SerializedName("sponsored_impressions_id") val sponsoredImpressionsId : String?=null
	@SerializedName("likes") val likes : Int?=null
	@SerializedName("liked_by_user") val likedByUser : Boolean?=null
	@SerializedName("current_user_collections") val currentUserCollections : List<String>?=null
	@SerializedName("user") val user : User? = null
}
