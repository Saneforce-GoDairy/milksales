package com.saneforce.milksales.Interface;

public interface ParentListInterface {
    void onClickParentInter(String value, int totalValue, String itemID, Integer positionValue, String productName, String productCode, Integer productQuantiy, String catImage, String catName,String productUnit,Integer Value);
    void onProductUnit(String productSaleUnit,String productItemId);

    void ProductImage(String ImageUrl);

}
