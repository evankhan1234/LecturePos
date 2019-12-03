package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Customer;

public class CustomerRepository implements ICustomerDataSource {
    public ICustomerDataSource ICustomerDataSource;
    public CustomerRepository(ICustomerDataSource ICustomerDataSource){
        this.ICustomerDataSource=ICustomerDataSource;
    }
    private static  CustomerRepository instance;

    public static CustomerRepository getInstance(ICustomerDataSource iCartDataSource){
        if(instance==null)
            instance= new CustomerRepository(iCartDataSource);
        return instance;

    }
    
    @Override
    public Flowable<List<Customer>> getCustomerItems() {
        return ICustomerDataSource.getCustomerItems();
    }

    @Override
    public Flowable<List<Customer>> getCustomerItemById(int CustomerItemId) {
        return ICustomerDataSource.getCustomerItemById(CustomerItemId);
    }

    @Override
    public Flowable<List<Customer>> getCustomer(int favoriteid) {
        return ICustomerDataSource.getCustomer(favoriteid);
    }

    @Override
    public void emptyCustomer() {
        ICustomerDataSource.emptyCustomer();
    }

    @Override
    public int size() {
        return ICustomerDataSource.size();
    }

    @Override
    public void insertToCustomer(Customer... Customers) {
        ICustomerDataSource.insertToCustomer(Customers);
    }

    @Override
    public void updateCustomer(Customer... Customers) {
        ICustomerDataSource.updateCustomer(Customers);
    }

    @Override
    public void deleteCustomerItem(Customer... Customers) {
        ICustomerDataSource.deleteCustomerItem(Customers);
    }
}
