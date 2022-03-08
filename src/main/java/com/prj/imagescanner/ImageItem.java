package com.prj.imagescanner;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
    private String desc;
    private String rowid;
    private String imagename;

    public ImageItem(Bitmap image, String title, String desc, String rowid, String imagename) {
        super();
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.rowid = rowid;
        this.imagename = imagename;
    }

    public ImageItem(Bitmap bitmap, String filename) {
        super();
        this.image = bitmap;
        this.imagename = filename;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {return desc; }

    public void setDesc(String desc) {this.desc = desc;}

    public String getRowid() { return rowid;}
    public void setRowid(String rowid) {this.rowid = rowid;}

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
