package xact.idea.lecturepos.Database.Datasources;


import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Customer;

public interface ICustomerDataSource {
    Flowable<List<Customer>> getCustomerItems();


    Flowable<List<Customer>> getCustomerItemById(int CustomerItemId);


    Flowable<List<Customer>> getCustomer(int favoriteid);

    void emptyCustomer();
    int size();


    void insertToCustomer(Customer... Customers);


    void updateCustomer(Customer... Customers);


    void deleteCustomerItem(Customer... Customers);
}
