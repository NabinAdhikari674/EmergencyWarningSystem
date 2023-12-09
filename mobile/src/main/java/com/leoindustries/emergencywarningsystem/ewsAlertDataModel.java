package com.leoindustries.emergencywarningsystem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class ewsAlertDataModel implements Parcelable {

    private String name;
    private String description;
    private String url;
    private List<String> keywords;
    private String license;
    private String dateCreated;
    private String dateModified;
    private String datePublished;
    private Creator creator;
    private List<Distribution> distribution;

    // Getters for all the fields

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getLicense() {
        return license;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public Creator getCreator() {
        return creator;
    }

    public List<Distribution> getDistribution() {
        return distribution;
    }

    public static class Creator {
        private String name;
        private ContactPoint contactPoint;

        public String getName() {
            return name;
        }

        public ContactPoint getContactPoint() {
            return contactPoint;
        }
    }

    public static class ContactPoint {
        private String contactType;
        private String telephone;

        public String getContactType() {
            return contactType;
        }

        public String getTelephone() {
            return telephone;
        }
    }

    public static class Distribution {
        private String encodingFormat;
        private String contentUrl;

        public String getEncodingFormat() {
            return encodingFormat;
        }

        public String getContentUrl() {
            return contentUrl;
        }
    }

    public static final Parcelable.Creator<ewsAlertDataModel> CREATOR = new Parcelable.Creator<ewsAlertDataModel>() {
        public ewsAlertDataModel createFromParcel(Parcel in) {
            return new ewsAlertDataModel(in);
        }

        public ewsAlertDataModel[] newArray(int size) {
            return new ewsAlertDataModel[size];
        }
    };

    protected ewsAlertDataModel(Parcel in) {
        // Read data from Parcel and populate your object's fields
        name = in.readString();
        description = in.readString();
        url = in.readString();
        in.readStringList(keywords);
        license = in.readString();
        dateCreated = in.readString();
        dateModified = in.readString();
        datePublished = in.readString();
//        creator = in.readParcelable(Creator.class.getClassLoader());
        in.readList(distribution, Distribution.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeStringList(keywords);
        dest.writeString(license);
        dest.writeString(dateCreated);
        dest.writeString(dateModified);
        dest.writeString(datePublished);
//        dest.writeParcelable(creator, flags);
        dest.writeList(distribution);
    }
}
