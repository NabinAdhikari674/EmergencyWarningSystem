package com.leoindustries.emergencywarningsystem;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class ewsAlertDataModel implements Parcelable {

    private List<Head> heads;
    private List<Row> rows;

    public List<Head> getHeads() {
        return heads;
    }

    public List<Row> getRows() {
        return rows;
    }

    protected ewsAlertDataModel(Parcel in) {
        heads = in.createTypedArrayList(Head.CREATOR);
        rows = in.createTypedArrayList(Row.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(heads);
        dest.writeTypedList(rows);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ewsAlertDataModel> CREATOR = new Creator<ewsAlertDataModel>() {
        @Override
        public ewsAlertDataModel createFromParcel(Parcel in) {
            return new ewsAlertDataModel(in);
        }

        @Override
        public ewsAlertDataModel[] newArray(int size) {
            return new ewsAlertDataModel[size];
        }
    };

    public static class Head implements Parcelable {

        private int totalCount;
        private int numOfRows;
        private int pageNo;
        private RESULT result;

        public int getTotalCount() {
            return totalCount;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public int getPageNo() {
            return pageNo;
        }

        public RESULT getResult() {
            return result;
        }

        protected Head(Parcel in) {
            totalCount = in.readInt();
            numOfRows = in.readInt();
            pageNo = in.readInt();
            result = in.readParcelable(RESULT.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(totalCount);
            dest.writeInt(numOfRows);
            dest.writeInt(pageNo);
            dest.writeParcelable(result, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Head> CREATOR = new Creator<Head>() {
            @Override
            public Head createFromParcel(Parcel in) {
                return new Head(in);
            }

            @Override
            public Head[] newArray(int size) {
                return new Head[size];
            }
        };

        public static class RESULT implements Parcelable {

            private String resultCode;
            private String resultMsg;

            public String getResultCode() {
                return resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }

            protected RESULT(Parcel in) {
                resultCode = in.readString();
                resultMsg = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(resultCode);
                dest.writeString(resultMsg);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<RESULT> CREATOR = new Creator<RESULT>() {
                @Override
                public RESULT createFromParcel(Parcel in) {
                    return new RESULT(in);
                }

                @Override
                public RESULT[] newArray(int size) {
                    return new RESULT[size];
                }
            };
        }
    }

    public static class Row implements Parcelable {
        private String create_date;
        private String location_id;
        private String location_name;
        private String md101_sn;
        private String msg;
        private String send_platform;

        public String getCreate_date() {
            return create_date;
        }

        public String getLocation_id() {
            return location_id;
        }

        public String getLocation_name() {
            return location_name;
        }

        public String getMd101_sn() {
            return md101_sn;
        }

        public String getMsg() {
            return msg;
        }

        public String getSend_platform() {
            return send_platform;
        }

        protected Row(Parcel in) {
            create_date = in.readString();
            location_id = in.readString();
            location_name = in.readString();
            md101_sn = in.readString();
            msg = in.readString();
            send_platform = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(create_date);
            dest.writeString(location_id);
            dest.writeString(location_name);
            dest.writeString(md101_sn);
            dest.writeString(msg);
            dest.writeString(send_platform);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Row> CREATOR = new Creator<Row>() {
            @Override
            public Row createFromParcel(Parcel in) {
                return new Row(in);
            }

            @Override
            public Row[] newArray(int size) {
                return new Row[size];
            }
        };
    }
}
