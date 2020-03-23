package com.panaceasoft.firoozboard.edit;

public class Detail {
    /*
        private String itemId ;
        private String locationId ;
        private String locationName ;

        private String title ;
        private String category ;
        private String type ;
        private String phone ;
        private String price ;
        private String description ;
        private String city;
        private String address;

        private String lat;
        private String lng;
    */
    private String catId;
    private String subCatId;
    private String itemTypeId;
    private String itemPriceTypeId;
    private String conditionId;
    private String locationId;
    private String remark;
    private String description;
    private String highlightInfo;
    private String price;
    private String dealOptionId;
    private String brand;
    private String businessMode;
    private String isSoldOut;
    private String title;
    private String address;
    private String lat;
    private String lng;
    private String itemId;
    private String userId;

    public Detail() {

    }

    public Detail(String catId, String subCatId, String itemTypeId, String itemPriceTypeId, String conditionId, String locationId, String remark, String description, String highlightInfo, String price, String dealOptionId, String brand, String businessMode, String isSoldOut, String title, String address, String lat, String lng, String itemId, String userId) {
        this.catId = catId;
        this.subCatId = subCatId;
        this.itemTypeId = itemTypeId;
        this.itemPriceTypeId = itemPriceTypeId;
        this.conditionId = conditionId;
        this.locationId = locationId;
        this.remark = remark;
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
        this.itemId = itemId;
        this.userId = userId;
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

    public String getConditionId() {
        return conditionId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getRemark() {
        return remark;
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

    public String getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }
}
