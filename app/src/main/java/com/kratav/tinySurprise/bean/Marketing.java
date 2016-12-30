package com.kratav.tinySurprise.bean;

/**
 * Created by Arun on 29-Oct-15.
 */
public class Marketing {
    private boolean status;
    private String bgImage;
    private boolean shown= false;

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String positiveBtnImage;
    private String category;

    public String getNegativeBtnImage() {
        return negativeBtnImage;
    }

    public void setNegativeBtnImage(String negativeBtnImage) {
        this.negativeBtnImage = negativeBtnImage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getPositiveBtnImage() {
        return positiveBtnImage;
    }

    public void setPositiveBtnImage(String positiveBtnImage) {
        this.positiveBtnImage = positiveBtnImage;
    }

    private String negativeBtnImage;


}
