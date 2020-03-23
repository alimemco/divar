package com.panaceasoft.firoozboard.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class Item {

    @NonNull
    @SerializedName("id")
    public final String id;

    @SerializedName("cat_id")
    public final String catId;

    @SerializedName("sub_cat_id")
    public final String subCatId;

    @SerializedName("item_type_id")
    public final String itemTypeId;

    @SerializedName("item_price_type_id")
    public final String itemPriceTypeId;

    @SerializedName("item_currency_id")
    public final String itemCurrencyId;

    @SerializedName("condition_of_item_id")
    public final String conditionOfItem;

    @SerializedName("description")
    public final String description;

    @SerializedName("highlight_info")
    public final String highlightInfo;

    @SerializedName("price")
    public final String price;

    @SerializedName("deal_option_id")
    public final String dealOptionId;

    @SerializedName("brand")
    public final String brand;

    @SerializedName("business_mode")
    public final String businessMode;

    @SerializedName("is_sold_out")
    public final String isSoldOut;

    @SerializedName("title")
    public final String title;

    @SerializedName("address")
    public final String address;

    @SerializedName("lat")
    public final String lat;

    @SerializedName("lng")
    public final String lng;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("touch_count")
    public final String touchCount;

    @SerializedName("favourite_count")
    public final String favouriteCount;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @SerializedName("photo_count")
    public final String photoCount;

    @SerializedName("deal_option_remark")
    public final String dealOptionRemark;

    @Embedded(prefix = "default_photo_")
    @SerializedName("default_photo")
    public Image defaultPhoto;

    @Embedded(prefix = "category_")
    @SerializedName("category")
    public final ItemCategory category;

    @Embedded(prefix = "sub_category_")
    @SerializedName("sub_category")
    public final ItemSubCategory subCategory;

    @Embedded(prefix = "item_type_")
    @SerializedName("item_type")
    public final ItemType itemType;

    @Embedded(prefix = "item_price_type_")
    @SerializedName("item_price_type")
    public final ItemPriceType itemPriceType;

    @Embedded(prefix = "item_currency_")
    @SerializedName("item_currency")
    public final ItemCurrency itemCurrency;

    @Embedded(prefix = "item_location_")
    @SerializedName("item_location")
    public final ItemLocation itemLocation;

    @Embedded(prefix = "condition_of_item_")
    @SerializedName("condition_of_item")
    public final ItemCondition itemCondition;

    @Embedded(prefix = "deal_option_")
    @SerializedName("deal_option")
    public final ItemDealOption itemDealOption;

    @Embedded(prefix = "user_")
    @SerializedName("user")
    public final User user;

    @SerializedName("is_owner")
    public final String isOwner;

    @SerializedName("is_favourited")
    public final String isFavourited;

    public Item(@NonNull String id, String catId, String subCatId, String itemTypeId, String itemPriceTypeId, String itemCurrencyId, String conditionOfItem, String description, String highlightInfo, String price, String dealOptionId, String brand, String businessMode, String isSoldOut, String title, String address, String lat, String lng, String status, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String touchCount, String favouriteCount, String addedDateStr, String photoCount, String dealOptionRemark, Image defaultPhoto, ItemCategory category, ItemSubCategory subCategory, ItemType itemType, ItemPriceType itemPriceType, ItemCurrency itemCurrency, ItemLocation itemLocation, ItemCondition itemCondition, ItemDealOption itemDealOption, User user, String isOwner, String isFavourited) {
        this.id = id;
        this.catId = catId;
        this.subCatId = subCatId;
        this.itemTypeId = itemTypeId;
        this.itemPriceTypeId = itemPriceTypeId;
        this.itemCurrencyId = itemCurrencyId;
        this.conditionOfItem = conditionOfItem;
        this.description = description;
        this.highlightInfo = highlightInfo;
        this.price = price;
        this.dealOptionId = dealOptionId;
        this.brand = brand;
        this.businessMode = businessMode;
        this.isSoldOut = isSoldOut;
        this.title = title;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.touchCount = touchCount;
        this.favouriteCount = favouriteCount;
        this.addedDateStr = addedDateStr;
        this.photoCount = photoCount;
        this.dealOptionRemark = dealOptionRemark;
        this.defaultPhoto = defaultPhoto;
        this.category = category;
        this.subCategory = subCategory;
        this.itemType = itemType;
        this.itemPriceType = itemPriceType;
        this.itemCurrency = itemCurrency;
        this.itemLocation = itemLocation;
        this.itemCondition = itemCondition;
        this.itemDealOption = itemDealOption;
        this.user = user;
        this.isOwner = isOwner;
        this.isFavourited = isFavourited;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public String getCatId() {
        return catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public String getItemPriceTypeId() {
        return itemPriceTypeId;
    }

    public String getItemCurrencyId() {
        return itemCurrencyId;
    }

    public String getConditionOfItem() {
        return conditionOfItem;
    }

    public String getDescription() {
        return description;
    }

    public String getHighlightInfo() {
        return highlightInfo;
    }

    public String getPrice() {
        return price;
    }

    public String getDealOptionId() {
        return dealOptionId;
    }

    public String getBrand() {
        return brand;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public String getIsSoldOut() {
        return isSoldOut;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getStatus() {
        return status;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getAddedUserId() {
        return addedUserId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public String getUpdatedFlag() {
        return updatedFlag;
    }

    public String getTouchCount() {
        return touchCount;
    }

    public String getFavouriteCount() {
        return favouriteCount;
    }

    public String getAddedDateStr() {
        return addedDateStr;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public String getDealOptionRemark() {
        return dealOptionRemark;
    }

    public Image getDefaultPhoto() {
        return defaultPhoto;
    }

    public void setDefaultPhoto(Image defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public ItemSubCategory getSubCategory() {
        return subCategory;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public ItemPriceType getItemPriceType() {
        return itemPriceType;
    }

    public ItemCurrency getItemCurrency() {
        return itemCurrency;
    }

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    public ItemCondition getItemCondition() {
        return itemCondition;
    }

    public ItemDealOption getItemDealOption() {
        return itemDealOption;
    }

    public User getUser() {
        return user;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public String getIsFavourited() {
        return isFavourited;
    }
}
