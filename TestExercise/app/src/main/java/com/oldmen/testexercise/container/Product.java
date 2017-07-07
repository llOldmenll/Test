package com.oldmen.testexercise.container;


import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    /**
     * id : 1
     * title : product1
     * img : img1.png
     * text : lorem ipsum 1
     */

    private int id;
    private String title;
    private String img;
    private String text;
    private double rating;


    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        img = in.readString();
        text = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(img);
        dest.writeString(text);
        dest.writeDouble(rating);
    }
}

