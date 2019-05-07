/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class for orders
public class Order {
	//instance variables
	private static int numOfOrders = 0;//this variable will be assigned as unique id of an order
	private int orderID;
	private Device orderProduct;
	private String name,surname,phoneNumber;
	private String orderDate,orderDueDate;
	private double finalPrice;
	private boolean executed;
	
	//constructor
	public Order(Device orderProduct,String name,String surname,String phoneNumber,
			String orderDate,String orderDueDate,boolean executed) {
		numOfOrders++;
		setOrderID(numOfOrders);
		setOrderProduct(orderProduct);
		setName(name);
		setSurname(surname);
		setPhoneNumber(phoneNumber);
		setOrderDate(orderDate);
		setOrderDueDate(orderDueDate);
		setExecuted(executed);
		setFinalPrice();
	}
	public Order(int ID,Device orderProduct,String name,String surname,String phoneNumber,
			String orderDate,String orderDueDate,boolean executed,double fprice) {
		setOrderID(ID);
		setOrderProduct(orderProduct);
		setName(name);
		setSurname(surname);
		setPhoneNumber(phoneNumber);
		setOrderDate(orderDate);
		setOrderDueDate(orderDueDate);
		setExecuted(executed);
		setFinalPrice(fprice);
	}
	
	//method-sets the final price to the created order in accordance with the discount of every category
	private void setFinalPrice(){
		if(orderProduct instanceof VideoSoundDevice){
			finalPrice = orderProduct.getPrice()*0.75;
		}else if(orderProduct instanceof GamingDevice){
			finalPrice = orderProduct.getPrice()*0.9;
		}else{
			finalPrice = orderProduct.getPrice()*0.8;
		}
	}

	//setters and getters
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Device getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(Device orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDueDate() {
		return orderDueDate;
	}

	public void setOrderDueDate(String orderDueDate) {
		this.orderDueDate = orderDueDate;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public double getFinalPrice() {
		return finalPrice;
	}
	
	public static int getNumOfOrders(){
		return numOfOrders;
	}
	
	public static void setNumOfOrders(int n){
		numOfOrders = n;
	}
	
	//toString method, represents an order
	public String toString(){
		return("Order No." + getOrderID() + "\nName: " + getName() + "\nSurname: "+getSurname() + "\nPhoneNumber: " + getPhoneNumber() + 
				"\nOrder Date: " + getOrderDate() + "\nOrder Due: "+ getOrderDueDate()+ "\nFinal Price: "+ getFinalPrice()+ "\nExecuted: "+ 
				((executed)?"YES":"NO") + "\n-----PRODUCT----- \n" + orderProduct.toString());		
	}
}
