package xact.idea.lecturepos.Model;

public class CustomerModel {

    public String StoreId;

    public String RetailerCode;

    public String Name;

    public String Address;

    public String MobileNumber;

    public CustomerModel(String storeId, String retailerCode, String name, String address, String mobileNumber) {
        StoreId = storeId;
        RetailerCode = retailerCode;
        Name = name;
        Address = address;
        MobileNumber = mobileNumber;
    }
}