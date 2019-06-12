import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Photo (

	@SerializedName("id") val id : String,
	@SerializedName("created_at") val createdAt : String,
	@SerializedName("updated_at") val updatedAt : String,
	@SerializedName("width") val width : Int,
	@SerializedName("height") val height : Int,
	@SerializedName("color") val color : String,
	@SerializedName("description") val description : String,
	@SerializedName("alt_description") val altDescription : String,
	@SerializedName("urls") val urls : Urls,
	@SerializedName("links") val links : Links,
	@SerializedName("categories") val categories : List<String>,
	@SerializedName("sponsored") val sponsored : Boolean,
	@SerializedName("sponsored_by") val sponsored_by : String,
	@SerializedName("sponsored_impressions_id") val sponsoredImpressionsId : String,
	@SerializedName("likes") val likes : Int,
	@SerializedName("liked_by_user") val liked_by_user : Boolean,
	@SerializedName("current_user_collections") val currentUserCollections : List<String>,
	@SerializedName("user") val user : User
): Serializable