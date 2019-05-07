/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class mainApp extends JFrame implements ActionListener,MouseListener{
	
	private static boolean changeMade = false;	//checks if a change (sale/order) was made in order to prompt the user to save his progress
	private static boolean productClassToggle = true;	//false when viewing class instances (devices), true when viewing classes (categories, e.g. Camera, Television)
	private static String currProdType = "none";	//a string with the name of the currently chosen product
	private static ArrayList <Device> device_list = new ArrayList <>();	//list of products in stock
	private static ArrayList <Order> orders_list = new ArrayList <>();	//list of orders 
	private static ArrayList <Sale> sales_list = new ArrayList <>();	//list of sales
	private static File products,sales,orders;	//the .txt files, empty by default
	private JMenuBar menuBar;	//contains "File", "More"
	private JMenu menuFile;	//"File" menu
	private JMenu menuMore;	//"More" menu
	private JMenuItem menuAbout;	//"About" menu
	private JMenuItem menuLoad;	//"Load" menu
	private JMenuItem menuSave;	//"Save" menu
	private JMenuItem menuSaveAs;	//"Save as" menu
	private JMenuItem menuExit;	//"Exit" menu
	private JButton btnSale;	//"Buy product" button
	private JButton btnOrder;	//"Order product" button
	private JButton btnOrderState;	//"Change state of order" button
	//private JButton btnSearchSales;	//"Search by name" button
	//private JButton btnSearchOrders;	//"Search by name" button
	private JFileChooser fc;	//a file browser
	private DefaultListModel productsModel;	//The DefaultList model for the productsList
	private JList productsList;	//the available devices list that appears to the program user
	private DefaultListModel salesModel;	//The DefaultList model for the salesList
	private JList salesList;	//the sales list that appears to the program user
	private DefaultListModel ordersModel;	//The DefaultList model for the ordersList
	private JList ordersList;	//the orders list that appears to the program user
	private JTabbedPane tabbedPane;	//creates our Products, Orders, Sales tabs
	private JTextField findSalesText;	//the "Search by Name:" text field in the "Sales" tab
	private JTextField findOrdersText;	//the "Search by Name:" text field in the "Orders" tab
	private JLabel orderLabel;	//the label containing an image preview of the ordered product
	private JLabel saleLabel;	//the label containing an image preview of the sold product
	
	public mainApp(){
		setTitle("Online Electronics Shop");	//set the frame title at the Title Bar
		drawFrame();
		//add Action Listeners to each button
		menuLoad.addActionListener(this);
		menuSave.addActionListener(this);
		menuSaveAs.addActionListener(this);
		menuExit.addActionListener(this);
		menuAbout.addActionListener(this);
		btnSale.addActionListener(this);
		btnOrder.addActionListener(this);
		btnOrderState.addActionListener(this);
		//add Mouse Listeners
		productsList.addMouseListener(this);
		ordersList.addMouseListener(this);
		salesList.addMouseListener(this);
		//add Change Listener
		tabbedPane.addChangeListener(new ChangeListener(){	//actions performed when we choose a tab

			@Override
			public void stateChanged(ChangeEvent e) {	//resets the following states

				btnSale.setEnabled(false);	//disables the "Buy product" button
				btnOrderState.setEnabled(false);	//disables the "Change state of order" button
				btnOrder.setEnabled(false);		//disables the "Order product" button
				productsList.clearSelection();	//the previously focused item in the "productsList" is deselected
				salesList.clearSelection();		//the previously focused item in the "salesList" is deselected
				ordersList.clearSelection();	//the previously focused item in the "ordersList" is deselected
				findOrdersText.setText("");	//set the default text in the search bar bar of the "Orders" tab as empty
				findSalesText.setText("");	//set the default text in the search bar bar of the "Sales" tab as empty
				orderLabel.setIcon(null);	//remove the image from the label in the "Orders" tab
				saleLabel.setIcon(null);	//remove the image from the label in the "Sales" tab
			}
			
		});
		findSalesText.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			//replace the contents of the "salesModel" list with the search results
			public void insertUpdate(DocumentEvent e) {	//update the search process with the addition of a character in the search box
					String name = findSalesText.getText().trim();	//the term in the search box
					salesModel.clear();	//remove all elements from the "salesModel" list
					for(Sale s: sales_list){	//scan the "sales_list" list
						if(s.getName().toUpperCase().startsWith(name.toUpperCase())){	//if the first letters of the buyer's name start with the term in the search box
							salesModel.addElement("Sale No." + s.getSaleID());	//add the specific device in the "salesModel" list
						}
					}
			}
			
			@Override
			//replace the contents of the "salesModel" list with the search results
			public void removeUpdate(DocumentEvent e) {	//update the search process with the removal of a character in the search box
					String name = findSalesText.getText().trim();	//the term in the search box
					salesModel.clear();	//remove all elements from the "salesModel" list
					for(Sale s: sales_list){	//scan the "sales_list" list
						if(s.getName().toUpperCase().startsWith(name.toUpperCase())){	//if the first letters of the buyer's name start with the term in the search box
							salesModel.addElement("Sale No." + s.getSaleID());	//add the specific device in the "salesModel" list
						}
					}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});

		findOrdersText.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			//replace the contents of the "ordersModel" list with the search results
			public void insertUpdate(DocumentEvent e) {	//update the search process with the addition of a character in the search box
					String name = findOrdersText.getText().trim();	//the term in the search box
					ordersModel.clear();	//remove all elements from the "ordersModel" list	
					for(Order o: orders_list){	//scan the "sales_list" list
						if(o.getName().toUpperCase().startsWith(name.toUpperCase())){	//if the first letters of the buyer's name start with the term in the search box
							ordersModel.addElement("Order No." + o.getOrderID() + " -- "+((o.isExecuted())?"Completed":"Pending"));	//add the specific device in the "ordersModel" list
						}
					}
			}
			
			@Override
			//replace the contents of the "ordersModel" list with the search results
			public void removeUpdate(DocumentEvent e) {	//update the search process with the removal of a character in the search box
				String name = findOrdersText.getText().trim();	//the term in the search box
				btnOrderState.setEnabled(false);	//disable the "Change state of order" button
				ordersModel.clear();	//remove all elements from the "ordersModel" list
				for(Order o: orders_list){	//scan the "sales_list" list
					if(o.getName().toUpperCase().startsWith(name.toUpperCase())){	//if the first letters of the buyer's name start with the term in the search box
						ordersModel.addElement("Order No." + o.getOrderID() + " -- "+((o.isExecuted())?"Completed":"Pending"));	//add the specific device in the "ordersModel" list
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		
		setVisible(true);	//show our window
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	//call this method for any interaction with the user
		if(e.getSource()==menuLoad){	//if the "Load files" menu item is clicked
			if(changeMade && products != null && sales != null && orders != null){	//if a sale/order has occurred and the files are not empty
				int choice = JOptionPane.showConfirmDialog(this, "You have made changes in the files.\nWould you like to save them?");	//prompt the user to save his progress
				if(choice == JOptionPane.YES_OPTION){	//if the user accepts to save
					saveProgress(products, sales, orders);	//saves all changes made
					JOptionPane.showMessageDialog(this, "Files have been saved succesfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				}else if(choice == JOptionPane.CANCEL_OPTION){	//if the user presses cancel
					return;	//Abandon the process of load
				}
			}//if(changeMade && products != null && sales != null && orders != null)
			products = null;sales = null;orders = null;	//set our files as empty by default
			JOptionPane.showMessageDialog(this, "Please choose the products file!");	//prompts the user to choose a specific text file
			int returnVal = fc.showOpenDialog(this);	//opens the directory for the user to choose the file
			if(returnVal == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "OK"
				products = fc.getSelectedFile();	//the selected file is assigned as the "products" file
				JOptionPane.showMessageDialog(this, "Please choose the sales file!");	//prompts the user to choose a specific text file
				returnVal = fc.showOpenDialog(this);	//opens the directory for the user to choose the file
				if(returnVal == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "OK"
					sales = fc.getSelectedFile();	//the selected file is assigned as the "sales" file
					JOptionPane.showMessageDialog(this, "Please choose the orders file!");	//prompts the user to choose a specific text file
					returnVal = fc.showOpenDialog(this);	//opens the directory for the user to choose the file
					if(returnVal == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "OK"
						orders = fc.getSelectedFile();	//the selected file is assigned as the "orders" file
					}
				}
			}
			if(products!=null && sales!= null && orders!=null){	//if the files are not empty
				if(checkFiles(products,sales,orders)){	//if the files comply to the rules
					loadProgress(products,sales,orders);	//load the content of the files
					JOptionPane.showMessageDialog(this, "Files have been loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					//Disable buttons to avoid potential invalid sales/orders after the new files have been loaded
					btnOrder.setEnabled(false);	//disable the "Order product" button
					btnSale.setEnabled(false);	//disable the "Buy product" button
					btnOrderState.setEnabled(false);	//disable the "Change state of order" button
					changeMade = false;	//no save prompt at exit
					productsModel.clear();	//remove all elements from the "productsModel" list
					salesModel.clear();	//remove all elements from the "salesModel" list
					ordersModel.clear();	//remove all elements from the "ordersModel" list
					for(Device d : device_list){	//scan the "device_list" list
						
						if(!productsModel.contains(d.getClass().getName())){	//if the "productsModel" list does not contain the device "d"
							productsModel.addElement(d.getClass().getName());	//add it to that list
						}
					}
					for(Sale s : sales_list){	//scan the "sales_list" list
						salesModel.addElement("Sale No." + s.getSaleID());	//add the purchase to the "salesModel" list
					}
					for(Order o : orders_list){	//scan the "orders_list" list
						//add the purchase to the "ordersModel" list and show whether it has been executed or not
						ordersModel.addElement("Order No." + o.getOrderID() + " -- "+((o.isExecuted())?"Completed":"Pending"));
					}
				}else{	//if any of the files has not passed the check
					JOptionPane.showMessageDialog(this, "Files have not been loaded!", "Warning", JOptionPane.WARNING_MESSAGE);	//inform the user that no load occurred
				}
			}else{//if any the files is empty
				JOptionPane.showMessageDialog(this, "Files have not been loaded!", "Warning", JOptionPane.WARNING_MESSAGE);	//inform the user that no load occurred
			}
		}//if(e.getSource()==menuLoad)
		else if(e.getSource() == menuSave){	//if the "Save files" menu item is clicked
			if(products!=null && sales!= null && orders!=null){	//if the files are not empty
				saveProgress(products, sales, orders);	//save the changes made 
				JOptionPane.showMessageDialog(this, "Files have been saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);	//inform the user that the save is done
				changeMade = false;	//no save prompt at exit
			}else{//if any of the files is empty
				JOptionPane.showMessageDialog(this, "Nothing to save!", "Warning", JOptionPane.INFORMATION_MESSAGE);	//inform the user that no save occurred
			}
		}
		else if(e.getSource() == menuSaveAs){	//if the "Save as" menu item is clicked
			if(products!=null && sales!= null && orders!=null){	//if the files are not empty
				JOptionPane.showMessageDialog(this, "Please choose where to save products!");	//ask the user where to save the products list
				fc.setSelectedFile(new File("products.txt"));	//set the currently selected directory and default file name for the products list
				if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "Save"
					products = fc.getSelectedFile();	//the selected file is assigned as the "products" file
					JOptionPane.showMessageDialog(this, "Please choose where to save sales!");	//ask the user where to save the sales list
					fc.setSelectedFile(new File("sales.txt"));	//set the currently selected directory and default file name for the sales list
					if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "Save"
						sales = fc.getSelectedFile();	//the selected file is assigned as the "sales" file
						JOptionPane.showMessageDialog(this, "Please choose where to save orders!");	//ask the user where to save the orders list
						fc.setSelectedFile(new File("orders.txt"));	//set the currently selected directory and default file name for the orders list
						if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){	//if the user accepts by clicking "Save"
							orders = fc.getSelectedFile();	//the selected file is assigned as the "orders" file
							saveProgress(products, sales, orders);	//save the changes made 
							JOptionPane.showMessageDialog(this, "Files have been saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);	//inform the user that the save is done
							changeMade = false;	//no save prompt at exit
						}else{
							JOptionPane.showMessageDialog(this, "Files have not been saved!", "Warning", JOptionPane.WARNING_MESSAGE);	//inform the user that no save occurred
						}
					}else{
						JOptionPane.showMessageDialog(this, "Files have not been saved!", "Warning", JOptionPane.WARNING_MESSAGE);	//inform the user that no save occurred
					}
				}else{
					JOptionPane.showMessageDialog(this, "Files have not been saved!", "Warning", JOptionPane.WARNING_MESSAGE);	//inform the user that no save occurred
				}
			}else{
				JOptionPane.showMessageDialog(this, "Nothing to save!", "Warning", JOptionPane.INFORMATION_MESSAGE);	//inform the user that no save occurred
			}
		}//else if(e.getSource() == menuSaveAs)
		else if(e.getSource() == menuExit){	//if the "Exit" menu item is clicked
			if(changeMade && products!=null && sales!=null && orders!=null){	//if a sale/order is made and the files are not empty	
				int choice = JOptionPane.showConfirmDialog(this, "You have made changes in the files.\nWould you like to save them?");
				if(choice == JOptionPane.YES_OPTION){	//if the user accepts by clicking "Yes"
					saveProgress(products,sales,orders);	//save the changes made 
					dispose();	//close the current window
				}else if(choice == JOptionPane.NO_OPTION){	//if the user accepts by clicking "No"
					dispose();	//close the current window
				}
			}else{	//if no sale/order is made 
				dispose();	//close the current window
			}
		}//else if(e.getSource() == menuExit)
		else if(e.getSource() == menuAbout){	//if the "About" menu item is clicked
			JOptionPane.showMessageDialog(this, "mainApp,\ncopyright AUEB 2016\n\nAuthors:\nAnastasiou Iwannhs, AM: 3150232\nSavvidis Kwnstantinos, AM: 3150229\nStavropoulos Petros, AM: 3150230","About",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == btnSale){	//if the "Buy product" button is clicked
			int i = 0;	//the counter for all devices (class instances)
			int exitCount = 0;	//the counter for devices of a specific category (instances of one class)
			int focusedItem = productsList.getSelectedIndex();	//the index of the item we have clicked on
			for(Device d : device_list){	//scan the "device_list" list
				i++;
				if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
					exitCount++;
					if(exitCount > focusedItem) break;	//stop searching after the class instances counter exceeds the position of the selected device
				}
			}
			if(makeSale(device_list.get(i-1))){	//if the sale is successful
				JOptionPane.showMessageDialog(this, "The sale has been completed!\nFinal Price: " + sales_list.get(sales_list.size()-1).getFinalPrice());
				if(device_list.get(i-1).getQuantity() == 0){	//if there are no more devices available
					btnSale.setEnabled(false);	//disable the "Buy product" button
					btnOrder.setEnabled(true);	//activate the "Order product" button
				}
				salesModel.addElement("Sale No." + sales_list.get(sales_list.size()-1).getSaleID());	//add the purchase to the "salesModel" list
			}else{
				JOptionPane.showMessageDialog(this, "The sale was aborted!","Warning",JOptionPane.WARNING_MESSAGE);	//inform the user that no sale occurred
			}
		}//else if(e.getSource() == btnSale)
		else if(e.getSource() == btnOrder){	//if the "Order product" button is clicked
			int i = 0;	//the counter for all devices (class instances)
			int exitCount = 0;	//the counter for devices of a specific category (instances of one class)
			int focusedItem = productsList.getSelectedIndex();	//the index of the item we have clicked on
			for(Device d : device_list){	//scan the "device_list" list
				i++;	//count all devices in our device_list
				if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
					exitCount++;
					if(exitCount > focusedItem) break;	//stop searching after the class instances counter exceeds the position of the selected device
				}
			}
			if(makeOrder(device_list.get(i-1))){	//if the order is successful
				JOptionPane.showMessageDialog(this, "The order has been completed!\nFinal Price: " + orders_list.get(orders_list.size()-1).getFinalPrice());
			
				ordersModel.addElement("Order No." + orders_list.get(orders_list.size()-1).getOrderID() + " -- " + ((orders_list.get(orders_list.size()-1).isExecuted())?"Completed":"Pending"));	//add the order to the "ordersModel" list
			}else{
				JOptionPane.showMessageDialog(this, "The order was aborted!","Warning",JOptionPane.WARNING_MESSAGE);	//inform the user that no order occurred
			}
		}//else if(e.getSource() == btnOrder)
		else if(e.getSource() == btnOrderState){	//if the "Change state of order" is clicked	
			int focusedItem = ordersList.getSelectedIndex();	//the index of the item we have clicked on
			if(focusedItem != -1){	//if we have actually clicked on a device
				int item = Integer.parseInt(ordersModel.getElementAt(focusedItem).toString().replaceAll("^.*\\.(\\d+).*$", "$1")); //find the index of the real object using regular expressions

				Order o = orders_list.get(item-1);	//orders in the "ordersModel" list are shown to the user counting from 1, whereas in the "orders_list" from 0
				o.setExecuted(true);	//change the order state from "pending" to "executed"
				Sale s = new Sale(o.getOrderProduct(),o.getName(),o.getSurname(),o.getPhoneNumber(),o.getOrderDueDate()); //Date of sales is due date of order (date of arrival)
				sales_list.add(s);	//add the sale in the "sales_list" list
				JOptionPane.showMessageDialog(this, "The Order's state has been changed!\nA new Sale was added, with the information of the Order.","Success",JOptionPane.INFORMATION_MESSAGE);
				btnOrderState.setEnabled(false);	//disable the "Change state of order" button
				ordersModel.remove(item-1); //Remove last entry because it is updated
				ordersModel.add(item-1,"Order No." + o.getOrderID() + " -- "+((o.isExecuted())?"Completed":"Pending")); //update OrdersListModel
				salesModel.addElement("Sale No." + sales_list.get(sales_list.size()-1).getSaleID()); //update SalesListModel
				changeMade = true;	//prompt to save at exit
			}//if(focusedItem != -1)
		}//else if(e.getSource() == btnOrderState)
	}//public void actionPerformed(ActionEvent e)
	
	public boolean makeSale(Device productToSell){	//the method called when clicking the "Buy product" button
		String clName = null;
		String clSurname = null;
		String clPhone = null;
		clName = JOptionPane.showInputDialog(this, "Enter Name");	//request the name of the user
		if(clName == null) return false;	//when the user clicks "Cancel"
		clSurname = JOptionPane.showInputDialog(this, "Enter Surname");	//request the surnamename of the user
		if(clSurname == null) return false;	//when the user clicks "Cancel"
		clPhone = JOptionPane.showInputDialog(this, "Enter Phone");	//request the phone number of the user
		if(clPhone == null) return false;	//when the user clicks "Cancel"
		Date today = new Date();	//current date
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");	//format the date to be dd/MM/yyyy
		String clDate = null;
		clDate = JOptionPane.showInputDialog(this,"Enter Date", formatter.format(today));
		if(clDate == null) return false;	//when the user clicks "Cancel"
		Sale productSale = new Sale(productToSell, clName, clSurname, clPhone, clDate);
		sales_list.add(productSale);	//add the sale in the "sales_list" list
		productToSell.setQuantity(productToSell.getQuantity()-1);	//take away 1 from the product quantity
		changeMade = true;	//prompt to save at exit
		return true;
	}
	
	public boolean makeOrder(Device productToSell){	//the method called when clicking the "Order product" button
		String clName = null;
		String clSurname = null;
		String clPhone = null;
		String clDueDate = null;
		clName = JOptionPane.showInputDialog(this, "Enter Name");	//request the name of the user
		if(clName == null) return false;	//when the user clicks "Cancel"
		clSurname = JOptionPane.showInputDialog(this, "Enter Surname");	//request the surname of the user
		if(clSurname == null) return false;	//when the user clicks "Cancel"
		clPhone = JOptionPane.showInputDialog(this, "Enter Phone");	//request the phone number of the user
		if(clPhone == null) return false;	//when the user clicks "Cancel"
		Date today = new Date();	//the current date
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");	//format it to be dd/MM/yyyy
		String clDate = null;
		clDate = JOptionPane.showInputDialog(this,"Enter Date", formatter.format(today));
		if(clDate == null) return false;	//when the user clicks "Cancel"
		Calendar sampleDate = Calendar.getInstance();	//get today's date using the specified time zone and specified locale
		sampleDate.setTime(today);	//assign the date from the specified time zone and specified locale to "today"
		sampleDate.add(Calendar.DATE, 14);	//set the default arrival time to 2 weeks after the order
		clDueDate = JOptionPane.showInputDialog(this,"Enter Arrival Date", formatter.format(sampleDate.getTime()));
		if(clDueDate == null) return false;	//cancel the order if no due date is entered
		Order productOrder = new Order(productToSell,clName,clSurname,clPhone,clDate,clDueDate,false);	
		orders_list.add(productOrder);	//add the order in the "orders_list" list
		changeMade = true;	//prompt to save at exit
		return true;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {	//an event which indicates that a mouse button is clicked (pressed and released)
		int focusedItem;
		if(e.getSource() == productsList){	//if the object we clicked on belongs to the "productsList"
			focusedItem = productsList.getSelectedIndex();	//the index of the item we have clicked on
			if(!productClassToggle){	//if we are now viewing the class instances (devices of a category)
				if(focusedItem != -1){	//if we have actually clicked on a device
					if(focusedItem == productsModel.size()-1){	//if we have clicked on the last option ("Back")
						btnSale.setEnabled(false);	//disable the "Buy product" button
						btnOrder.setEnabled(false);	//disable the "Order product" button
					}else{	//if we have not clicked the "Back" option
						int i = 0;	//counter of instances/devices in "device_list"
						int exitCount = 0;	//counter of instances/devices in "device_list" of a specific class/category
						for(Device d : device_list){	//scan the "device_list" list
							i++;
							if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
								exitCount++;
								if(exitCount > focusedItem) break;	//stop searching after the class instances counter exceeds the position of the selected device
							}
						}
						if(device_list.get(i-1).getQuantity()>0){	//if the device is available for sale, activate or disable the according buttons
							btnSale.setEnabled(true);	//activate the "Buy product" button
							btnOrder.setEnabled(false);	//disable the "Order product" button
						}else{
							btnSale.setEnabled(false);	//disable the "Buy product" button
							btnOrder.setEnabled(true);	//activate the "Order product" button
						}
					}
				}//if(focusedItem != -1)
				if(e.getClickCount()==2){	//if we have double-clicked a class instance (device)
					if(focusedItem != -1){	//if we have actually clicked on a device
						if(focusedItem == productsModel.size()-1){	//if we have clicked on the last option ("Back")
							productsModel.clear();	//remove all elements from the "productsModel" list
							for(Device d : device_list){	//scan the "device_list" list
								if(!productsModel.contains(d.getClass().getName())){	//if the "productsModel" list does not contain the device "d"
									productsModel.addElement(d.getClass().getName());	//add it to that list
								}
							}
							productClassToggle = true;	//we are now viewing the classes (categories)
						}else{	//if we have not clicked on the last option ("Back")
							int i = 0;	//counter of instances/devices in "device_list"
							int exitCount = 0;	//counter of instances/devices in "device_list" of a specific class/category
							for(Device d : device_list){	//scan the "device_list" list
								i++;
								if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
									exitCount++;
									if(exitCount > focusedItem) break;	//stop searching after the class instances counter exceeds the position of the selected device
								}
							}
							Device dev = device_list.get(i-1);	//get the specific class instance/device
							ImageIcon icon = new ImageIcon(dev.getImagePath());	//create a new icon using the image path from the "Device" class
							if(icon.getIconWidth() == -1){	//if the icon does not exist
								icon = new ImageIcon("../Images/unavailable.png");	//set a default icon
							}
							
							if(dev.getQuantity() > 0){	//if the device is available for sale
								Object[] choices = new Object[2];	//an array containing the option buttons for sales
								choices[0] = "Buy";	//option to buy a device
								choices[1] = "Cancel";	//option to cancel the purchase

								
								//create an OptonDialog for the specific device with its model name, icon and options
								int ans = JOptionPane.showOptionDialog(this, dev, dev.getModelName(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, choices, null);	
								if(ans != -1){	//if the user chooses one of the available options
									if(ans == 0){	//if the user chooses to "Buy" a device
										if(makeSale(dev)){	//if the sale is successful
											JOptionPane.showMessageDialog(this, "The sale has been completed!\nFinal Price: " + sales_list.get(sales_list.size()-1).getFinalPrice());
											if(dev.getQuantity() == 0){	//if there are no more devices available
												btnSale.setEnabled(false);	//disable the "Buy" button
												btnOrder.setEnabled(true);	//activate the "Order" button
											}	
											salesModel.addElement("Sale No." + sales_list.get(sales_list.size()-1).getSaleID());	//add the purchase to the "salesModel" list	
										}else{	//if the sale is not successful
											JOptionPane.showMessageDialog(this, "The sale was aborted!","Warning",JOptionPane.WARNING_MESSAGE);
										}
									}//if(ans == 0)
								}//if(ans != -1)
							}//if(dev.getQuantity() > 0)
							else{	////if the device is not available for sale
								Object[] choices = new Object[2];
								choices[0] = "Order";	//option to order a device
								choices[1] = "Cancel";	//option to cancel the purchase
								//create an OptonDialog for the specific device with its model name, icon and options
								int ans = JOptionPane.showOptionDialog(this, dev, dev.getModelName(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, choices, null);
								if(ans != -1){	//if the user chooses one of the available options
									if(ans == 0){	//if the user chooses to "Order" a device
										if(makeOrder(dev)){	//if the order is successful
											JOptionPane.showMessageDialog(this, "The order has been completed!");
											//add the purchase to the "ordersModel" list and show whether it has been executed or not
											ordersModel.addElement("Order No." + orders_list.get(orders_list.size()-1).getOrderID() + " -- " + ((orders_list.get(orders_list.size()-1).isExecuted())?"Completed":"Pending"));
										}else{	//if the sale is not successful
											JOptionPane.showMessageDialog(this, "The order was aborted!","Warning",JOptionPane.WARNING_MESSAGE);
										}
									}//if(ans == 0)
								}//if(ans != -1)
							}//else
						}//else
					}//if(focusedItem != -1)
				}//if(e.getClickCount()==2)
			}//if(!productClassToggle)
			else if(e.getClickCount()==2){	//if we are now viewing the classes (categories) and we double-click one of them
				if(focusedItem != -1){	//if we have actually clicked on a device	
					String producType = productsModel.getElementAt(focusedItem).toString();	//the type of the focused device
					currProdType = producType;
					productsModel.clear();	//remove all elements from the "productsModel" list
					for(Device d : device_list){	//scan the "device_list" list
						if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
							productsModel.addElement("Device ID: " + d.getID() + " Model: " + d.getModelName());	//add it in the "productsModel" list
						}
					}
					productsModel.addElement("Back");	//add an option in the "productsModel" list to escape it and view the initial classes (categories)
					productClassToggle = false;	//we are now viewing the class instances (devices)
				}//if(focusedItem != -1)
			}//else if(e.getClickCount()==2)
		}//if(e.getSource() == productsList)
		
		else if(e.getSource() == salesList){	//if the object we clicked on belongs to the "salesList"
			
			focusedItem = salesList.getSelectedIndex();	//the index of the item we have clicked on
			if(focusedItem != -1){	//if we have actually clicked on a device
				int item;
				if(findSalesText.getText().trim().equals("")){ //If there is no name for search
					item = focusedItem; //keep the original index
				}else{

					item = Integer.parseInt(salesModel.getElementAt(focusedItem).toString().substring(8)) - 1; //find the index of the real object using substring
				}
				ImageIcon icon = new ImageIcon(sales_list.get(item).getSaleProduct().getImagePath());	//create a new icon using the image path from the "Device" class
				if(icon.getIconWidth() == -1){	//if the icon does not exist
					icon = new ImageIcon("../Images/unavailable.png");	//set a default icon
				}
				//resize the picture for preview next to the orders list
				Image img = icon.getImage();
				Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newImg);
				saleLabel.setIcon(icon);
					
				if(e.getClickCount()==2){	//if we have double-clicked a sale
					JOptionPane.showMessageDialog(this, sales_list.get(item),salesModel.getElementAt(focusedItem).toString(), JOptionPane.PLAIN_MESSAGE);
				}
			}//if(focusedItem != -1)
		}//else if(e.getSource() == salesList)
		
		else if(e.getSource() == ordersList){	//if the object we clicked on belongs to the "ordersList"
			focusedItem = ordersList.getSelectedIndex();	//the index of the item we have clicked on
			if(focusedItem != -1){	//if we have actually clicked on a device
				int item;
				if(findOrdersText.getText().trim().equals("")){ //If there is no name for search
					item = focusedItem; //keep the original index
				}else{
					item = Integer.parseInt(ordersModel.getElementAt(focusedItem).toString().replaceAll("^.*\\.(\\d+).*$", "$1")) - 1; //find the index of the real object using regular expressions

				}
				ImageIcon icon = new ImageIcon(orders_list.get(item).getOrderProduct().getImagePath());	//create a new icon using the image path from the "Device" class
				if(icon.getIconWidth() == -1){	//if the icon does not exist
					icon = new ImageIcon("../Images/unavailable.png"); //set a default icon
				}
				//resize the picture for preview next to the orders list
				Image img = icon.getImage();
				Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newImg);
				orderLabel.setIcon(icon);
								
				if(!orders_list.get(item).isExecuted()){	//if the order we clicked on is not executed
					btnOrderState.setEnabled(true);	//activate the "Change state of order" button
				}else{
					btnOrderState.setEnabled(false);	//disable the "Change state of order" button
				}
				if(e.getClickCount()==2){	//if we have double-clicked an order
				JOptionPane.showMessageDialog(this, orders_list.get(item),ordersModel.getElementAt(focusedItem).toString(), JOptionPane.PLAIN_MESSAGE);
				}	
			}//if(focusedItem != -1)
		}//else if(e.getSource() == ordersList)
	}//public void mouseClicked(MouseEvent e)

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	//implement this method to avoid the bugs created when clicking the mouse on a device and releasing it on another one
	public void mouseReleased(MouseEvent e) {	

		int focusedItem;
		if(e.getSource() == productsList){	//if the object we clicked on belongs to the "productsList"
			focusedItem = productsList.getSelectedIndex();	//the index of the item we have clicked on
			if(!productClassToggle){	//if we are now viewing the class instances (devices)
				if(focusedItem != -1){	//if we have actually clicked on a device
					if(focusedItem == productsModel.size()-1){	//if we have clicked on the last option ("Back")
						btnSale.setEnabled(false);	//disable the "Buy product" button
						btnOrder.setEnabled(false);	//disable the "Order product" button
					}else{	//if we have not clicked the "Back" option
						int i = 0;	//counter of instances/devices in "device_list"
						int exitCount = 0;	//counter of instances/devices in "device_list" of a specific class/category
						for(Device d : device_list){	//scan the "device_list" list
							i++;
							if(d.getClass().getName().equals(currProdType)){	//if the instance/device belongs to a specific class/category
								exitCount++;
								if(exitCount > focusedItem) break;	//stop searching after the class instances counter exceeds the position of the selected device
							}
						}
						if(device_list.get(i-1).getQuantity()>0){	//if the device is available for sale, activate or disable the according buttons
							btnSale.setEnabled(true);	//activate the "Buy product" button
							btnOrder.setEnabled(false);	//disable the "Order product" button
						}else{
							btnSale.setEnabled(false);	//disable the "Buy product" button
							btnOrder.setEnabled(true);	//activate the "Order product" button
						}
					}//else
				}//if(focusedItem != -1)
			}//if(!productClassToggle
		}//if(e.getSource() == productsList)
		
		else if(e.getSource() == ordersList){	//if the object we clicked on belongs to the "ordersList"
			focusedItem = ordersList.getSelectedIndex();	//the index of the item we have clicked on
			if(focusedItem != -1){	//if we have actually clicked on a device
				int item;
				if(findOrdersText.getText().trim().equals("")){ //If there is no name for search
					item = focusedItem; //keep the original index
				}else{
					item = Integer.parseInt(ordersModel.getElementAt(focusedItem).toString().replaceAll("^.*\\.(\\d+).*$", "$1")) - 1; //find the index of the real object using regular expressions

				}
				if(!orders_list.get(item).isExecuted()){	//if the order we clicked on is not executed
					btnOrderState.setEnabled(true);	//activate the "Change state of order" button
				}else{
					btnOrderState.setEnabled(false);	//disable the "Change state of order" button
				}
				
				ImageIcon icon = new ImageIcon(orders_list.get(item).getOrderProduct().getImagePath());	//create a new icon using the image path from the "Device" class
				if(icon.getIconWidth() == -1){	//if the icon does not exist
					icon = new ImageIcon("../Images/unavailable.png");	//set a default icon
				}
				//resize the picture for preview next to the orders list
				Image img = icon.getImage();
				Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon( newImg );
				orderLabel.setIcon(icon);
				
			}//if(focusedItem != -1)
		}//else if(e.getSource() == ordersList)
		else if(e.getSource() == salesList){
			focusedItem = salesList.getSelectedIndex();	//the index of the item we have clicked on
				if(focusedItem != -1){	//if we have actually clicked on a device
					int item;
					if(findSalesText.getText().trim().equals("")){ //If there is no name for search
						item = focusedItem; //keep the original index
					}else{
					item = Integer.parseInt(salesModel.getElementAt(focusedItem).toString().substring(8)) - 1; //find index of the real object using substring
					}
					ImageIcon icon = new ImageIcon(sales_list.get(item).getSaleProduct().getImagePath());	//create a new icon using the image path from the "Device" class
					if(icon.getIconWidth() == -1){	//if the icon does not exist
						icon = new ImageIcon("../Images/unavailable.png");	//set a default icon
					}
					//resize the picture for preview next to the sales list
					Image img = icon.getImage();
					Image newImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH ) ;  
					icon = new ImageIcon(newImg);
					saleLabel.setIcon(icon);
				}
		}
	}//public void mouseReleased(MouseEvent e)

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawFrame(){	//draws our main frame
		setSize(600,400);	//set the width and height
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//terminate the program when clicking the "X" button	
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuMore = new JMenu("More");
		menuAbout = new JMenuItem("About");
		menuLoad = new JMenuItem("Load");
		menuSave = new JMenuItem("Save");
		menuSaveAs = new JMenuItem("Save as");
		menuExit = new JMenuItem("Exit");
		menuFile.add(menuLoad);
		menuFile.add(menuSave);
		menuFile.add(menuSaveAs);
		menuFile.addSeparator();
		menuFile.add(menuExit);
		menuMore.add(menuAbout);
		menuBar.add(menuFile);
		menuBar.add(menuMore);
		
		productsModel = new DefaultListModel();
		
		productsList = new JList(productsModel){
			private boolean processEvent(MouseEvent e) {	//Create a method for JList that returns true only if the item selected is valid (index!=-1) and the mouse point is withing the cell bounds of the JList
				int index = this.locationToIndex(e.getPoint());
				return index > -1 && this.getCellBounds(index, index).contains(e.getPoint());
			}
			
			@Override
			protected void processMouseEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
				if(processEvent(e)){
					super.processMouseEvent(e);
				}
			}
			
			@Override
            protected void processMouseMotionEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
                if (processEvent(e)) {
                    super.processMouseMotionEvent(e);
                }
            }
		};
		
		salesModel = new DefaultListModel();

		salesList = new JList(salesModel){
			private boolean processEvent(MouseEvent e) {	//Create a method for JList that returns true only if the item selected is valid (index!=-1) and the mouse point is withing the cell bounds of the JList
				int index = this.locationToIndex(e.getPoint());	
				return index > -1 && this.getCellBounds(index, index).contains(e.getPoint());	
			}
			
			@Override
			protected void processMouseEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
				if(processEvent(e)){
					super.processMouseEvent(e);
				}
			}
			
			@Override
            protected void processMouseMotionEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
                if (processEvent(e)) {
                    super.processMouseMotionEvent(e);
                }
            }
		};
		
		ordersModel = new DefaultListModel();

		ordersList = new JList(ordersModel){
			private boolean processEvent(MouseEvent e) {	//Create a method for JList that returns true only if the item selected is valid (index!=-1) and the mouse point is withing the cell bounds of the JList
				int index = this.locationToIndex(e.getPoint());
				return index > -1 && this.getCellBounds(index, index).contains(e.getPoint());
			}
			
			@Override
			protected void processMouseEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
				if(processEvent(e)){
					super.processMouseEvent(e);
				}
			}
			
			@Override
            protected void processMouseMotionEvent(MouseEvent e) {	//Override the method that triggers the events of the mouse and let it trigger the event only if the previous method returns true
                if (processEvent(e)) {
                    super.processMouseMotionEvent(e);
                }
            }
		};
		
		btnSale = new JButton("Buy product");
		btnOrder = new JButton("Order product");
		btnOrderState = new JButton("Change state of order");	//set an order as "executed" from "pending"
		//btnSearchSales = new JButton("Search by name");
		//btnSearchOrders = new JButton("Search by name");
		btnSale.setEnabled(false);	//disable the "Buy product" button
		btnOrder.setEnabled(false);	//disable the "Order product" button
		btnOrderState.setEnabled(false);	//disable the "Change state of order" button
		//
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		//
		Container cp = getContentPane();	//Set the container to be the root pane
		tabbedPane = new JTabbedPane();	//Initialize the tabbedPane for the tabs
		
		JPanel productPanel = new JPanel();
		productPanel.setLayout(new BorderLayout());
		JScrollPane prScrPane = new JScrollPane(productsList);
		prScrPane.setPreferredSize(new Dimension(600, 350));
		productPanel.add(prScrPane,BorderLayout.CENTER);

		
		JPanel salesPanel = new JPanel();
		salesPanel.setLayout(new BorderLayout());
		JScrollPane salScrPane = new JScrollPane(salesList);
		salScrPane.setPreferredSize(new Dimension(600, 350));
		salesPanel.add(salScrPane,BorderLayout.CENTER);
		
		saleLabel = new JLabel();
		salesPanel.add(saleLabel, BorderLayout.EAST);
		
		JPanel ordersPanel = new JPanel();
		ordersPanel.setLayout(new BorderLayout());
		JScrollPane ordScrPane = new JScrollPane(ordersList);
		ordScrPane.setPreferredSize(new Dimension(600, 350));
		ordersPanel.add(ordScrPane,BorderLayout.CENTER);
		
		orderLabel = new JLabel();
		ordersPanel.add(orderLabel, BorderLayout.EAST);
		
		JPanel commandPanelPr = new JPanel();
		commandPanelPr.setLayout(new FlowLayout());	//insert the buttons one next to the other
		commandPanelPr.add(btnSale);
		commandPanelPr.add(btnOrder);
		productPanel.add(commandPanelPr, BorderLayout.SOUTH);
		
		JPanel commandPanelSl = new JPanel();
		findSalesText = new JTextField("", 10);
		commandPanelSl.setLayout(new FlowLayout());
		commandPanelSl.add(new JLabel("Search by Name:"));
		commandPanelSl.add(findSalesText);

		salesPanel.add(commandPanelSl, BorderLayout.SOUTH);
		
		JPanel commandPanelOr = new JPanel();
		findOrdersText = new JTextField("",10);
		commandPanelOr.setLayout(new FlowLayout());
		commandPanelOr.add(new JLabel("Search by Name:"));
		commandPanelOr.add(findOrdersText);

		commandPanelOr.add(btnOrderState);	//add the "Change state of order" button to the panel
		ordersPanel.add(commandPanelOr, BorderLayout.SOUTH);
		
		cp.setLayout(new BorderLayout());	//Set the layout of the Container to BorderLayout

		tabbedPane.addTab("Products", productPanel);
		tabbedPane.addTab("Orders", ordersPanel);
		tabbedPane.addTab("Sales", salesPanel);
		JPanel mainPanel = new JPanel();	
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(tabbedPane,BorderLayout.CENTER);
		cp.add(mainPanel, BorderLayout.CENTER);
		
		setJMenuBar(menuBar);
		setMinimumSize(new Dimension(300, 200));	//set the minimum size of the window
		pack();	//arrange the window size according to the contents
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();	//the dimension of the user's screen
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);	//center the window in the user's screen
		
	}
	
	public static void main(String[] args){
		new mainApp();
		
		
	}//end of main method
	
	//***********//STATIC METHODS USED IN MAIN//***********//
	
			public static void saveProgress(File products,File sales,File orders){
				//Initialize files and readers
				File f1 = products;
				File f2 = sales;
				File f3 = orders;
				BufferedWriter writer1 = null;
				BufferedWriter writer2 = null;
				BufferedWriter writer3 = null;
				
				try{
					//Open the writer of each file
					writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1)));
					writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2)));
					writer3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f3)));
				}
				catch (FileNotFoundException e){
					System.err.println("Error opening file for writing!");
				}
				try{
					//Save products
					writer1.write("ITEM_LIST\n{\n");
					
					//For every device in product list write its fields in the file in the correct structure
					for(Device d : device_list){	
                                            if(d instanceof Camera){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE CAMERA\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+                    
                                                    "\t\tCAMERA_TYPE "+((Camera)d).getCamera_type()+"\n"+
                                                    "\t\tMEGAPIXELS "+((Camera)d).getMegapixels()+"\n"+
                                                    "\t\tOPTICAL_ZOOM "+((Camera)d).getOpt_zoom()+"\n"+
                                                    "\t\tDIGITAL_ZOOM "+((Camera)d).getDig_zoom()+"\n"+
                                                    "\t\tSCREEN_DIMENSION "+((Camera)d).getScreen_dimension()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                    "\t}\n");
                                            }else if(d instanceof Television){
                                                writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE TELEVISION\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+                               
                                                    "\t\tTV_TYPE "+((Television)d).getTv_type()+"\n"+ 
                                                    "\t\tDIMENSION "+((Television)d).getDimension()+"\n"+ 
                                                    "\t\tRESOLUTION "+((Television)d).getResolution()+"\n"+
                                                    "\t\tPORTS_TYPE "+((Television)d).getPorts_type()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                    "\t}\n");
                                            }else if(d instanceof MoviePlayer){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE MOVIE_PLAYER\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+                                
                                                    "\t\tMEDIA_TYPE "+((MoviePlayer)d).getMedia_type()+"\n"+
                                                    "\t\tRESOLUTION "+((MoviePlayer)d).getResolution()+"\n"+
                                                    "\t\tFORMAT "+((MoviePlayer)d).getFormat()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                "\t}\n");
                                            }else if(d instanceof Console){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE CONSOLE\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+                                                    
                                                    "\t\tPROCESSOR "+((Console)d).getProcessor()+"\n"+
                                                    "\t\tGRAPHICS "+((Console)d).getGraphics()+"\n"+
                                                    "\t\tSOUND "+((Console)d).getSound()+"\n"+
                                                    "\t\tDISK_CAPACITY "+((Console)d).getDisk_capacity()+"\n"+
                                                    "\t\tCONSOLE_TYPE "+((Console)d).getConsole_type()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                     "\t}\n");
                                            }else if(d instanceof PortableConsole){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE PORTABLE_CONSOLE\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+
                                                    "\t\tPORTABLE_TYPE "+((PortableConsole)d).getPortable_type()+"\n"+
                                                    "\t\tPROCESSOR "+((PortableConsole)d).getProcessor()+"\n"+
                                                    "\t\tGRAPHICS "+((PortableConsole)d).getGraphics()+"\n"+
                                                    "\t\tSOUND "+((PortableConsole)d).getSound()+"\n"+
                                                    "\t\tDISK_CAPACITY "+((PortableConsole)d).getDisk_capacity()+"\n"+                           
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                    "\t}\n");
                                            }else if(d instanceof Refrigerator){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE REFRIGERATOR\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+                         
                                                    "\t\tENERGY_CLASS "+((Refrigerator)d).getEnergy_class()+"\n"+
                                                    "\t\tREFR_TYPE "+((Refrigerator)d).getRefr_type()+"\n"+
                                                    "\t\tPRESERVE_CAPACITY "+((Refrigerator)d).getPreserve_capacity()+"\n"+
                                                    "\t\tFREEZE_CAPACITY "+((Refrigerator)d).getFreeze_capacity()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                    "\t}\n");
                                                        
                                            }else if(d instanceof WashingMachine){
						writer1.write("\tITEM\n\t{\n\t\tITEM_TYPE WASHING_MACHINE\n"+
                                                    "\t\tID "+d.getID()+"\n"+
                                                    "\t\tMODEL_NAME "+d.getModelName()+"\n"+
                                                    "\t\tMODEL_YEAR "+d.getModelYear()+"\n"+
                                                    "\t\tMANUFACTURER "+d.getManufacturer()+"\n"+
                                                    "\t\tPRICE "+d.getPrice()+"\n"+
                                                    "\t\tENERGY_CLASS "+((WashingMachine)d).getEnergy_class()+"\n"+
                                                    "\t\tCAPACITY "+((WashingMachine)d).getCapacity()+"\n"+
                                                    "\t\tRPM "+((WashingMachine)d).getRpm()+"\n"+
                                                    "\t\tQUANTITY "+d.getQuantity()+"\n"+
                                                    "\t}\n");                        
						}
					}
					writer1.write("}");
					
					//Save orders
					writer3.write("ORDER_LIST\n{\n");
					//For every order write its fields to the file
					for(Order o : orders_list){
						writer3.write("\tORDER\n\t{\n\t\tORDER_ID " + o.getOrderID() + 
								"\n\t\tNAME " + o.getName() + 
								"\n\t\tSURNAME " + o.getSurname() +
								"\n\t\tPHONE_NUMBER " + o.getPhoneNumber() + 
								"\n\t\tORDER_DATE " + o.getOrderDate() + 
								"\n\t\tORDER_DUE_DATE " + o.getOrderDueDate() +
								"\n\t\tFINAL_PRICE " + o.getFinalPrice() + 
								"\n\t\tEXECUTED " + (o.isExecuted()?"YES":"NO") +
								"\n\t\tITEM_TYPE " + o.getOrderProduct().getClass().getName().toUpperCase() +
								"\n\t\tPRODUCT_ID " + o.getOrderProduct().getID() +
								"\n\t\tMODEL_NAME " + o.getOrderProduct().getModelName() +
								"\n\t}\n");
					}
					writer3.write("}");
					//Save sales
					writer2.write("SALES_LIST\n{\n");
					//For every sale write its fields to the file
					for(Sale s : sales_list){
						writer2.write("\tSALE\n\t{\n\t\tSALE_ID " + s.getSaleID() + 
								"\n\t\tNAME " + s.getName() + 
								"\n\t\tSURNAME " + s.getSurname() +
								"\n\t\tPHONE_NUMBER " + s.getPhoneNumber() + 
								"\n\t\tSALE_DATE " + s.getSaleDate() + 
								"\n\t\tFINAL_PRICE " + s.getFinalPrice() + 
								"\n\t\tITEM_TYPE " + s.getSaleProduct().getClass().getName().toUpperCase() + 
								"\n\t\tPRODUCT_ID " + s.getSaleProduct().getID() + 
								"\n\t\tMODEL_NAME " + s.getSaleProduct().getModelName() +
								"\n\t}\n");
					}
					writer2.write("}");
				}
				catch(IOException e){
					System.err.println("Write error!");
				}
				
				try{
					//Close the writers
					writer1.close();
					writer2.close();
					writer3.close();
				}
				catch(IOException e){
					System.err.println("Error closing file.");
				}
				
			}
			
			public static void loadProgress(File products,File sales,File orders){
				//Initialize files and readers
				File f1 = products;
				File f2 = orders;
				File f3 = sales;
				BufferedReader reader1 = null;
				BufferedReader reader2 = null;
				BufferedReader reader3 = null;
				//Clear the lists so that there will be no duplicate entities
				//if the user loads again without exiting the program
				device_list.clear();
				orders_list.clear();
				sales_list.clear();
				//Set the view to be the types of the devices
				productClassToggle = true;
				try{
					//Open readers for each file
					reader1 = new BufferedReader(new FileReader(f1));
					reader2 = new BufferedReader(new FileReader(f2));
					reader3 = new BufferedReader(new FileReader(f3));
				}
				catch (FileNotFoundException e){
					System.err.println("Error opening file for reading!");
					return;
				}				
				//Products
				try {
					System.out.println("Loading file: " + f1 + " ...");
					String line;
					int linec = 0,itemc = 0;
					line = reader1.readLine();
					linec++;
					if(line.equals("ITEM_LIST")){
						if((line = reader1.readLine()).equals("{")){
							linec++;
							//Loop until end of file
							while((line = reader1.readLine()) != null){
								linec++;
								if(line.trim().equals("ITEM")){
									//For each ITEM tag,load the item
									itemc++;
									ArrayList<String> lines = new ArrayList<String>();
									//Default variables for the item
									String type = null;
									int code = -1;
									String model = null;
									if((line = reader1.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of ITEM tag
										while(!(line = reader1.readLine()).trim().equals("}")){
											linec++;
											//Add every line to the Arraylist of lines
											lines.add(line.trim());
										}
										//Find the 3 required fields for the item regardless of the position of the tags
										//in the file
										for(String s : lines){
											if(s.toUpperCase().startsWith("ITEM_TYPE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												type = parts[1];
											}
											if(s.toUpperCase().startsWith("ID")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													code = Integer.parseInt(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on ID input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
												}
											}
											if(s.toUpperCase().startsWith("MODEL_NAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(parts[i] + ((i==parts.length-1)?"":" "));
												}
												model = sb.toString().trim();
											}
										}
										//Check that the item has these fields comparing the variables to the default ones
										//If not then the item cannot be loaded
										if(type!=null && code!=-1 && model!= null){
											//Default variables that apply to items of all types
											int modelyear = -1;
											String manufacturer = "N/A";
											double price = -1.0;
											int quantity = 0;
											//For each item type set the default variables, load the variables from the file
											//and add the item in the product list
											if(type.equals("CAMERA")){
												int megapixels=-1,opt_zoom=-1,dig_zoom= -1;
												double screen_dimension = -1.0;
												CameraType camera_type = CameraType.N_A;
												
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(parts[i] + ((i==parts.length-1)?"":" "));
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("CAMERA_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															camera_type = CameraType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on CAMERA_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MEGAPIXELS")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															megapixels = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MEGAPIXELS input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("OPTICAL_ZOOM")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															opt_zoom = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on OPTICAL_ZOOM input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("DIGITAL_ZOOM")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															dig_zoom = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on DIGITAL_ZOOM input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("SCREEN_DIMENSION")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															screen_dimension = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on SCREEN_DIMENSION input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new Camera(code, model, modelyear, manufacturer, price, camera_type, megapixels, opt_zoom, dig_zoom, screen_dimension, quantity));
											}else if(type.equals("MOVIE_PLAYER")){
												String resolution = "N/A";
												MoviePlayerType media_type = MoviePlayerType.N_A;
												FormatType format = FormatType.N_A;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("RESOLUTION")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														resolution = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MEDIA_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															media_type = MoviePlayerType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on MEDIA_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("FORMAT")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															format = FormatType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on FORMAT input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new MoviePlayer(code, model, modelyear, manufacturer, price, media_type, resolution, format, quantity));
											}else if(type.equals("TELEVISION")){
												TvType tv_type = TvType.N_A;
												double dimension = -1.0;
												String resolution = "N/A";
												PortType ports_type = PortType.N_A;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("RESOLUTION")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														resolution = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("TV_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															tv_type = TvType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on TV_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("DIMENSION")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															dimension = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on DIMENSION input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("PORTS_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															ports_type = PortType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on PORTS_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new Television(code, model, modelyear, manufacturer, price, tv_type, dimension, resolution, ports_type, quantity));
											}else if(type.equals("CONSOLE")){
												ConsoleType console_type = ConsoleType.N_A;
												String processor = "N/A",graphics = "N/A",sound = "N/A";
												int disk_capacity = -1;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("DISK_CAPACITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															disk_capacity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on DISK_CAPACITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("PROCESSOR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														processor = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("GRAPHICS")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														graphics = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("SOUND")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														sound = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("CONSOLE_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															console_type = ConsoleType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on CONSOLE_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new Console(code, model, modelyear, manufacturer, price, console_type, processor, graphics, sound, disk_capacity, quantity));
											}else if(type.equals("PORTABLE_CONSOLE")){
												PortableConsoleType portable_type = PortableConsoleType.N_A;
												String processor = "N/A",graphics = "N/A",sound = "N/A";
												int disk_capacity = -1;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("DISK_CAPACITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															disk_capacity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on DISK_CAPACITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("PROCESSOR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														processor = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("GRAPHICS")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														graphics = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("SOUND")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														sound = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PORTABLE_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															portable_type = PortableConsoleType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on PORTABLE_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new PortableConsole(code, model, modelyear, manufacturer, price, portable_type, processor, graphics, sound, disk_capacity, quantity));
											}else if(type.equals("REFRIGERATOR")){
												String energy_class = "N/A";
												FridgeType refr_type = FridgeType.N_A;
												double preserve_capacity = -1.0,freeze_capacity = -1.0;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("ENERGY_CLASS")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														energy_class = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRESERVE_CAPACITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															preserve_capacity = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRESERVE_CAPACITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("FREEZE_CAPACITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															freeze_capacity = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on FREEZE_CAPACITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("REFR_TYPE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															refr_type = FridgeType.valueOf(parts[1]);
														}catch(IllegalArgumentException e){
															System.err.println("Error on REFR_TYPE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new Refrigerator(code, model, modelyear, manufacturer, price, energy_class, refr_type, preserve_capacity, freeze_capacity, quantity));												
											}else if(type.equals("WASHING_MACHINE")){
												String energy_class = "N/A";
												double capacity = -1.0;
												int rpm = -1;
												for(String s : lines){
													if(s.toUpperCase().startsWith("MODEL_YEAR")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															modelyear = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on MODEL_YEAR input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("MANUFACTURER")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														manufacturer = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("PRICE")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															price = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on PRICE input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("QUANTITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															quantity = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on QUANTITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("ENERGY_CLASS")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														StringBuffer sb = new StringBuffer();
														for(int i=1;i<=parts.length-1;i++){
															sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
														}
														energy_class = sb.toString().trim();
													}else if(s.toUpperCase().startsWith("CAPACITY")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															capacity = Double.parseDouble(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on CAPACITY input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}else if(s.toUpperCase().startsWith("RPM")){
														String[] parts = s.split("\\s+");
														if(parts.length == 1) continue;
														try{
															rpm = Integer.parseInt(parts[1]);
														}catch(NumberFormatException e){
															System.err.println("Error on RPM input : " + f1 + "::" + "line " + (linec-lines.size()+lines.indexOf(s)+1*itemc));
														}
													}
												}
												device_list.add(new WashingMachine(code, model, modelyear, manufacturer, price, energy_class, capacity, rpm, quantity));
											}
										}
										else{
												//If one of the required information is missing then dont load the item
												//and continue to the next
												if(type == null){
													System.err.println("Item TYPE missing in Product " + itemc + ": Product not loaded!");
												}
												if(code == -1){
													System.err.println("Item ID missing in Product " + itemc + ": Product not loaded!");
												}
												if(model == null){
													System.err.println("Item MODEL_NAME missing in Product " + itemc + ": Product not loaded!");
												}
										}
									}
								}
							}
						}
					}
				} catch (IOException e1) {
					System.err.println("An error occurred while reading file " + f1 + "!");
				}
				
				//Orders
				try{
					System.out.println("Loading file: " + f2 + " ...");
					String line;
					int orderc = 0,linec=0;
					line = reader2.readLine();
					linec++;
					if(line.equals("ORDER_LIST")){
						if((line = reader2.readLine()).equals("{")){
							linec++;
							//Loop until end of file
							while((line = reader2.readLine()) != null){
								linec++;
								if(line.trim().equals("ORDER")){
									//For every ORDER tag,increase the order count,set default variables
									orderc++;
									ArrayList<String> lines = new ArrayList<String>();
									String type = null;
									int code = -1,order_id = -1;
									String model = null,phonenum = null;
									String name = null,surname = null,order_date = null,order_due = null;
									boolean executed = false;
									double fprice = -1.0;
									
									if((line = reader2.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of ORDER tag
										while(!(line = reader2.readLine()).trim().equals("}")){
											linec++;
											//Add every line to the Arraylist
											lines.add(line.trim());
										}
										//Loop in the lines of the ORDER to fill the variables from the file
										//using the specific tags
										for(String s : lines){
											if(s.toUpperCase().startsWith("ITEM_TYPE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												type = parts[1];
											}else if(s.toUpperCase().startsWith("PRODUCT_ID")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													code = Integer.parseInt(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on PRODUCT_ID input : " + f2 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*orderc));
												}
											}else if(s.toUpperCase().startsWith("MODEL_NAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												model = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("NAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												name = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("SURNAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												surname = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("PHONE_NUMBER")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												phonenum = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("ORDER_ID")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													order_id = Integer.parseInt(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on ORDER_ID input : " + f2 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*orderc));
												}
											}else if(s.toUpperCase().startsWith("ORDER_DATE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												order_date = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("ORDER_DUE_DATE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												order_due = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("FINAL_PRICE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													fprice = Double.parseDouble(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on FINAL_PRICE input : " + f2 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*orderc));
												}
											}else if(s.toUpperCase().startsWith("EXECUTED")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												executed = parts[1].trim().equals("YES")?true:false;
											}
										}
										if(order_id == -1){
											//If order is missing the ID then dont load it 
											System.err.println("Error loading Order " + orderc + ", missing ORDER_ID");
										}else{
											//Check that the product of the order exists in the product catalogue
											if(type!=null && code!=-1 && model!= null){
												boolean foundP = false;
												for(Device d : device_list){
													if(d.getClass().getName().toUpperCase().equals(type) && (d.getID()==code) &&
															d.getModelName().equals(model)){
														orders_list.add(new Order(order_id,d, name, surname, phonenum, order_date, order_due, executed,fprice));
														foundP = true;
														break;
													}
												}
												if(!foundP){
													System.err.println("The Product of the Order " + order_id + " doesn't exist in the product list!");
												}
											}else{
												System.err.println("The Product of the Order " + order_id + " doesn't have the required info!");
											}
										}
									}
								}
							}
						}
					}
					//Set the static variable of the Order to the order count
					//so that the next order made from the program doesn't start again from 1
					Order.setNumOfOrders(orderc);
				}catch(IOException e){
					System.err.println("An error occurred while reading file " + f2 + "!");
				}
				//Sales
				try{
					System.out.println("Loading file: " + f3 + " ...");
					String line;
					line = reader3.readLine();
					int salec = 0,linec =0;
					linec++;
					if(line.equals("SALES_LIST")){
						if((line = reader3.readLine()).equals("{")){
							linec++;
							//Loop until end of file
							while((line = reader3.readLine()) != null){
								linec++;
								if(line.trim().equals("SALE")){
									//For every SALE tag,increase the order count,set default variables
									salec++;
									ArrayList<String> lines = new ArrayList<String>();
									String type = null;
									int code = -1,sale_id = -1;
									String model = null,phonenum = null;
									String name = null,surname = null,sale_date = null;
									double fprice = -1.0;
									
									if((line = reader3.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of SALE tag
										while(!(line = reader3.readLine()).trim().equals("}")){
											linec++;
											//Add every line to the Arraylist
											lines.add(line.trim());
										}
										//Loop in the lines of the SALE to fill the variables from the file
										//using the specific tags
										for(String s : lines){
											if(s.toUpperCase().startsWith("ITEM_TYPE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												type = parts[1];
											}else if(s.toUpperCase().startsWith("PRODUCT_ID")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													code = Integer.parseInt(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on PRODUCT_ID input : " + f3 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*salec));
												}
											}else if(s.toUpperCase().startsWith("MODEL_NAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												model = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("NAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												name = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("SURNAME")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												surname = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("PHONE_NUMBER")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												phonenum = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("SALE_ID")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													sale_id = Integer.parseInt(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on SALE_ID input : " + f3 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*salec));
												}
											}else if(s.toUpperCase().startsWith("SALE_DATE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												StringBuffer sb = new StringBuffer();
												for(int i=1;i<=parts.length-1;i++){
													sb.append(((i%2) == 0)?" " + parts[i] + " ":parts[i]);
												}
												sale_date = sb.toString().trim();
											}else if(s.toUpperCase().startsWith("FINAL_PRICE")){
												String[] parts = s.split("\\s+");
												if(parts.length == 1) continue;
												try{
													fprice = Double.parseDouble(parts[1]);
												}catch(NumberFormatException e){
													System.err.println("Error on FINAL_PRICE input : " + f3 + "::line " + (linec-lines.size()+lines.indexOf(s)+1*salec));
												}
											}
										}
										if(sale_id == -1){
											//If SALE doesn't have the ID then dont load it
											System.err.println("Error loading Sale " + salec + ", missing SALE_ID");
										}else{
											//Check that the product of the SALE exists in the product catalogue
											if(type!=null && code!=-1 && model!= null){
												boolean foundP = false;
												for(Device d : device_list){
													if(d.getClass().getName().toUpperCase().equals(type) && (d.getID()==code) &&
															d.getModelName().equals(model)){
														sales_list.add(new Sale(sale_id,d, name, surname, phonenum, sale_date,fprice));	//add the sale in the "sales_list" list
														foundP = true;
														break;
													}
												}
												if(!foundP){
													System.err.println("The Product of the Sale " + sale_id + " doesn't exist in the product list!");
												}
											}else{
												System.err.println("The Product of the Sale " + sale_id + " doesn't have the required info!");
											}
										}
									}
								}
							}
						}
					}
					//Set the static variable of the Sale to the sales count
					//so that the next order made from the program doesn't start again from 1
					Sale.setNumofSales(salec);
				}catch(IOException e){
					System.err.println("An error occurred while reading file " + f3 + "!");
				}
				
				//Closing readers
				try{
					reader1.close();
					reader2.close();
					reader3.close();
				}
				catch(IOException e){
					System.err.println("Error closing file.");
				}
			}
			
			public static boolean checkFiles(File products,File sales,File orders){
				//Check structure of files
				File f1 = products;
				File f2 = orders;
				File f3 = sales;
				BufferedReader reader1 = null;
				BufferedReader reader2 = null;
				BufferedReader reader3 = null;
				try{
				//Open the readers of each file
					reader1 = new BufferedReader(new FileReader(f1));
					reader2 = new BufferedReader(new FileReader(f2));
					reader3 = new BufferedReader(new FileReader(f3));
				}
				catch (FileNotFoundException e){
					System.err.println("Error opening file for reading!");
					return false;
				}
				//Checking procedure
				
				//Products
				try{
					System.out.println("Checking file: " + f1 + " ...");
					String line;
					int linec=0,itemc=0;
					boolean lastbr = false;
					line = reader1.readLine();
					linec++;
					//Check for ITEM_LIST tag
					if(line.equals("ITEM_LIST")){
						//Check for brackets after tag
						if((line = reader1.readLine()).equals("{")){
							linec++;
							//Loop until end of file (until line == null)
							while((line = reader1.readLine()) != null){
								linec++;
								//Check for ITEM tag
								if(line.trim().equals("ITEM")){
									itemc++;
									//Check for bracket {
									if((line = reader1.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of ITEM with }
										while(!(line = reader1.readLine()).equals("\t}")){
											linec++;
											if(line.trim().equals("{") || line.equals("}")){
												System.err.println("Error: Structure of " + f1 + " is invalid!");
												System.err.println("near line " + (linec-2+1*itemc) + ": Missing '}'");
												//Closing readers
												try{
													reader1.close();
													reader2.close();
													reader3.close();
												}
												catch(IOException e){
													System.err.println("Error closing file.");
												}
												return false;
											}
										}
									}else{
										//No { bracket after ITEM tag
										System.err.println("Error: Structure of " + f1 + " is invalid!");
										System.err.println("near line " + (linec+1*itemc) + ": Missing '{'");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
									}
								}else if(!line.trim().equals("{") && !line.trim().equals("}")){
										System.err.println("Error: Structure of " + f1 + " is invalid!");
										System.err.println("near line " + (linec+1*itemc) + ": Wrong tag!");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
								}
								if(line.equals("}")){
									lastbr = true;
								}
							}
							if(!lastbr){
								//The ending bracket } is missing error
								System.err.println("Error: Structure of " + f1 + " is invalid!");
								System.err.println("last line : Missing '}'");
								//Closing readers
								try{
									reader1.close();
									reader2.close();
									reader3.close();
								}
								catch(IOException e){
									System.err.println("Error closing file.");
								}
								return false;
							}
						}else{
							//The bracket { is missing error
							System.err.println("Error: Structure of " + f1 + " is invalid!");
							System.err.println("line " + (linec+1) + ": Missing '{'");
							//Closing readers
							try{
								reader1.close();
								reader2.close();
								reader3.close();
							}
							catch(IOException e){
								System.err.println("Error closing file.");
							}
							return false;
						}
					}else{
						//The ITEM_LIST tag is missing error
						System.err.println("Error: Structure of " + f1 + " is invalid!");
						System.err.println("first line : Wrong tag!");
						//Closing readers
						try{
							reader1.close();
							reader2.close();
							reader3.close();
						}
						catch(IOException e){
							System.err.println("Error closing file.");
						}
						return false;
					}
				}catch(IOException e){
					System.err.println("An error occurred while reading file " + f1 + "!");
				}catch(NullPointerException e1){
					System.err.println("Error: Structure of " + f1 + " is invalid!");
					System.err.println("last line : Missing '}'");
					//Closing readers
					try{
						reader1.close();
						reader2.close();
						reader3.close();
					}
					catch(IOException e){
						System.err.println("Error closing file.");
					}
					return false;
				}
				
				//Orders
				try{
					System.out.println("Checking file: " + f2 + " ...");
					String line;
					int linec=0,orderc=0;
					boolean lastbr = false;
					line = reader2.readLine();
					linec++;
					//Check for ORDER_LIST tag
					if(line.equals("ORDER_LIST")){
						//Check for bracket { after ORDER_LIST tag
						if((line = reader2.readLine()).equals("{")){
							linec++;
							//Loop until end of file (until line == null)
							while((line = reader2.readLine()) != null){
								linec++;
								//Check for ORDER tag
								if(line.trim().equals("ORDER")){
									orderc++;
									//Check for bracket { after ORDER tag
									if((line = reader2.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of ORDER tag with }
										while(!(line = reader2.readLine()).equals("\t}")){
											linec++;
											//If there is a second { bracket in ORDER tag or the ending } bracket error missing }
											if(line.trim().equals("{") || line.equals("}")){
												System.err.println("Error: Structure of " + f2 + " is invalid!");
												System.err.println("near line " + (linec-2+1*orderc) + ": Missing '}'");
												//Closing readers
												try{
													reader1.close();
													reader2.close();
													reader3.close();
												}
												catch(IOException e){
													System.err.println("Error closing file.");
												}
												return false;
											}
										}
									}else{
										//Missing bracket { error after ORDER tag
										System.err.println("Error: Structure of " + f2 + " is invalid!");
										System.err.println("near line " + (linec+1*orderc) + ": Missing '{'");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
									}
								}else if(!line.trim().equals("{") && !line.trim().equals("}")){
										System.err.println("Error: Structure of " + f2 + " is invalid!");
										System.err.println("near line " + (linec+1*orderc) + ": Wrong tag!");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
								}
								if(line.equals("}")){
									lastbr = true;
								}
							}
							//Check for ending bracket }
							if(!lastbr){
								System.err.println("Error: Structure of " + f2 + " is invalid!");
								System.err.println("last line : Missing '}'");
								//Closing readers
								try{
									reader1.close();
									reader2.close();
									reader3.close();
								}
								catch(IOException e){
									System.err.println("Error closing file.");
								}
								return false;
							}
						}else{
							//Missing { after ORDER_LIST tag
							System.err.println("Error: Structure of " + f2 + " is invalid!");
							System.err.println("line " + (linec+1) + ": Missing '{'");
							//Closing readers
							try{
								reader1.close();
								reader2.close();
								reader3.close();
							}
							catch(IOException e){
								System.err.println("Error closing file.");
							}
							return false;
						}
					}else{
						//Missing ORDER_LIST tag
						System.err.println("Error: Structure of " + f2 + " is invalid!");
						System.err.println("first line : Wrong tag!");
						//Closing readers
						try{
							reader1.close();
							reader2.close();
							reader3.close();
						}
						catch(IOException e){
							System.err.println("Error closing file.");
						}
						return false;
					}
				}catch(IOException e){
					System.err.println("An error occurred while reading file " + f2 + "!");
				}catch(NullPointerException e1){
					System.err.println("Error: Structure of " + f2 + " is invalid!");
					System.err.println("last line : Missing '}'");
					//Closing readers
					try{
						reader1.close();
						reader2.close();
						reader3.close();
					}
					catch(IOException e){
						System.err.println("Error closing file.");
					}
					return false;
				}
				
				//Sales
				try{
					System.out.println("Checking file: " + f3 + " ...");
					String line;
					int linec=0,salec=0;
					boolean lastbr = false;
					line = reader3.readLine();
					linec++;
					//Check for SALES_LIST tag
					if(line.equals("SALES_LIST")){
						//Check for bracket { after SALES_LIST tag
						if((line = reader3.readLine()).equals("{")){
							linec++;
							//Loop until end of file (until line == null)
							while((line = reader3.readLine()) != null){
								linec++;
								//Check SALE tag
								if(line.trim().equals("SALE")){
									salec++;
									//Check bracket { after SALE tag
									if((line = reader3.readLine()).trim().equals("{")){
										linec++;
										//Loop until end of SALE tag
										while(!(line = reader3.readLine()).equals("\t}")){
											linec++;
											//Missing bracket } if second { bracket in SALE tag or ending } bracket
											if(line.trim().equals("{") || line.equals("}")){
												System.err.println("Error: Structure of " + f3 + " is invalid!");
												System.err.println("near line " + (linec-2+1*salec) + ": Missing '}'");
												//Closing readers
												try{
													reader1.close();
													reader2.close();
													reader3.close();
												}
												catch(IOException e){
													System.err.println("Error closing file.");
												}
												return false;
											}
										}
									}else{
										//No { bracket after SALE tag error
										System.err.println("Error: Structure of " + f3 + " is invalid!");
										System.err.println("near line " + (linec+1*salec) + ": Missing '{'");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
									}
								}else if(!line.trim().equals("{") && !line.trim().equals("}")){
										System.err.println("Error: Structure of " + f3 + " is invalid!");
										System.err.println("near line " + (linec+1*salec) + ": Wrong tag!");
										//Closing readers
										try{
											reader1.close();
											reader2.close();
											reader3.close();
										}
										catch(IOException e){
											System.err.println("Error closing file.");
										}
										return false;
								}
								if(line.equals("}")){
									lastbr = true;
								}
							}
							//Check for ending bracket }
							if(!lastbr){
								System.err.println("Error: Structure of " + f3 + " is invalid!");
								System.err.println("last line : Missing '}'");
								//Closing readers
								try{
									reader1.close();
									reader2.close();
									reader3.close();
								}
								catch(IOException e){
									System.err.println("Error closing file.");
								}
								return false;
							}
						}else{
							//Missing bracket { after SALES_LIST error
							System.err.println("Error: Structure of " + f3 + " is invalid!");
							System.err.println("line " + (linec+1) + ": Missing '{'");
							//Closing readers
							try{
								reader1.close();
								reader2.close();
								reader3.close();
							}
							catch(IOException e){
								System.err.println("Error closing file.");
							}
							return false;
						}
					}else{
						//Missing SALES_LIST tag error
						System.err.println("Error: Structure of " + f3 + " is invalid!");
						System.err.println("first line : Wrong tag!");
						//Closing readers
						try{
							reader1.close();
							reader2.close();
							reader3.close();
						}
						catch(IOException e){
							System.err.println("Error closing file.");
						}
						return false;
					}
				}catch(IOException e){
					System.err.println("An error occurred while reading file " + f3 + "!");
				}catch(NullPointerException e1){
					System.err.println("Error: Structure of " + f3 + " is invalid!");
					System.err.println("last line : Missing '}'");
					//Closing readers
					try{
						reader1.close();
						reader2.close();
						reader3.close();
					}
					catch(IOException e){
						System.err.println("Error closing file.");
					}
					return false;
				}
				//Closing readers
				try{
					reader1.close();
					reader2.close();
					reader3.close();
				}
				catch(IOException e){
					System.err.println("Error closing file.");
				}
				return true;
			}

			
}//end of MainApp class