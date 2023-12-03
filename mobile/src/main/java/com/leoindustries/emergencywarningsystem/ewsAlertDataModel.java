package com.leoindustries.emergencywarningsystem;

import java.util.List;

public class ewsAlertDataModel {

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
}
