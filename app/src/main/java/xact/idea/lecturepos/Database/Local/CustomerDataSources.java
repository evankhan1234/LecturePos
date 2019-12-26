package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ICustomerDataSource;
import xact.idea.lecturepos.Database.Model.Customer;

public class CustomerDataSources implements ICustomerDataSource {

    private CustomerDao CustomerDao;
    private static CustomerDataSources instance;

    public CustomerDataSources(CustomerDao CustomerDao){
        this.CustomerDao=CustomerDao;
    }
    public static CustomerDataSources getInstance(CustomerDao CustomerDao){
        if(instance==null)
            instance = new CustomerDataSources(CustomerDao);
        return instance;

    }



    @Override
    public Flowable<List<Customer>> getCustomerItems() {
        return CustomerDao.getCustomerItems();
    }

    @Override
    public Flowable<List<Customer>> getCustomerItemById(int CustomerItemId) {
        return CustomerDao.getCustomerItemById(CustomerItemId);
    }

    @Override
    public Flowable<List<Customer>> getCustomer(int favoriteid) {
        return CustomerDao.getCustomer();
    }

    @Override
    public Customer getCustomePhoe(String Name) {
        return CustomerDao.getCustomePhoe(Name);
    }

    @Override
    public void emptyCustomer() {
        CustomerDao.emptyCustomer();
    }

    @Override
    public int size() {
        return CustomerDao.value();
    }

    @Override
    public Customer getCustomeRetailCode(String Name) {
        return CustomerDao.getCustomeRetailCode(Name);
    }

    @Override
    public Customer getCustomerss(String Name) {
        return CustomerDao.getCustomerss(Name);
    }

    @Override
    public void insertToCustomer(Customer... carts) {

        CustomerDao.insertToCustomer(carts);
    }

    @Override
    public Customer getCustomeName(String Name) {
        return CustomerDao.getCustomeName(Name);
    }

    @Override
    public void updateCustomer(Customer... carts) {

        CustomerDao.updateCustomer(carts);
    }

    @Override
    public void deleteCustomerItem(Customer... carts) {

        CustomerDao.deleteCustomerItem(carts);
    }
}
