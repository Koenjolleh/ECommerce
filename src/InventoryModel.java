import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class InventoryModel {

    private ArrayList<Product> inventory;
    private Map<Product, Integer> shoppingCart;

    public InventoryModel(){

        inventory = new ArrayList<>();
        shoppingCart = new HashMap<>();

        try {
            XStream xstream = new XStream(new DomDriver());

            ObjectInputStream is = xstream.createObjectInputStream

                    (new FileReader("products.xml"));

            inventory = (ArrayList<Product>) is.readObject();

            is.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        //Sort list after id so that the largest id is always the last position in the inventory ArrayList
        for(int i=1; i<inventory.size(); i++){
            for(int j=0; j<inventory.size()-i; j++){
                if(inventory.get(j).getId()>inventory.get(j+1).getId()){
                    Product temp = inventory.get(j);
                    inventory.set(j, inventory.get(j+1));
                    inventory.set(j+1,temp);
                }
            }
        }
        //This line of code sets the static productId attribute of the Product class to be
        //that of the highest id number of the inventory ArrayList + 1, which we after the sort
        //knows is in the last position of the inventory ArrayList. Therefore making sure
        //that we always get a unique id number when creating new products.
        inventory.get(inventory.size()-1).setProductId(inventory.get(inventory.size()-1).getId());
    }

    //Add a product to the inventory ArrayList and afterwards saves the content
    //of inventory to the products.xml file
    public void createProduct(String name, String description, double cost, String category){

        inventory.add(new Product(name,description,cost,category));

        try {
            XStream xstream = new XStream(new DomDriver());

            ObjectOutputStream out = xstream.createObjectOutputStream

                    (new FileWriter("products.xml"));

            out.writeObject(inventory);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Searches for an product with an id matching the one passed as a parameter
    //if one is found it removes it from the inventory ArrayList and saves it to the
    //products.xml file gives feedback on weather or not the deletion was successful
    public String deleteProduct(int id){

        String feedback = "Product not found";

        for (int i = 0; i<inventory.size(); i++){
            if (inventory.get(i).getId() == id){
                inventory.remove(i);
                feedback = "Product successfully deleted";
                try {
                    XStream xstream = new XStream(new DomDriver());

                    ObjectOutputStream out = xstream.createObjectOutputStream

                            (new FileWriter("products.xml"));

                    out.writeObject(inventory);

                    out.close();
                } catch (IOException e) {
                    System.out.println("I am an Exception");
                }
            }
        }
        return feedback;
    }

    //If a matching id is found the method puts the parameters into setters for the found product
    //and then gives feedback that the product was successfully changed, otherwise returns feedback
    //that a product with the id was not found.
    public String updateProduct(int id, String name, String description, double cost, String category){

        String feedback = "Product with matching id not found";

        for (int i=0; i<inventory.size(); i++){
            if (inventory.get(i).getId() == id){
                inventory.get(i).setName(name);
                inventory.get(i).setDescription(description);
                inventory.get(i).setCost(cost);
                inventory.get(i).setCategory(category);
                feedback = "Product successfully updated";
                try {
                    XStream xstream = new XStream(new DomDriver());

                    ObjectOutputStream out = xstream.createObjectOutputStream

                            (new FileWriter("products.xml"));

                    out.writeObject(inventory);

                    out.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return feedback;
    }

    //Using stream on the inventory ArrayList. Uses filter method to filter out all the products which
    //doesn't match the id parameter. Uses the map method to convert it to it's toString value.
    //Then uses reduce to concat that toString value to a string and return it
    public String readProduct(int id){
        return inventory.stream().filter(product -> product.getId() == id).map(product -> product.toString()).reduce("",String::concat);
    }

    //Displays all the content of the inventory ArrayList which
    //category matches the comboValue parameter, if the value is "All"
    //everything in the ArrayList is displayed
    public String readInventory(String comboValue){

        String displayProducts = "" + comboValue + " Products:";

        for (Product item: inventory){

            if(comboValue == null){
                displayProducts += "This is null lull";
            } else if (comboValue.equals("All")){
                displayProducts += "\n Id #" + item.getId() + ": " + item.getName() + ". Price: " + item.getCost();
            } else if(item.getCategory().equals(comboValue)){
                displayProducts += "\n Id #" + item.getId() + ": " + item.getName() + ". Price: " + item.getCost();
            }

        }

        return displayProducts;
    }

    //Goes through the inventory ArrayList to find a match for id parameter
    //if a match is found and the quantity parameter is over 0 and the shopping cart map
    //does not already contain the key else it replaces it and adds the quantity to the
    //value of the key. The feedback
    //String is changed to acknowledge that the product was added. Otherwise
    //it returns the instantiated value of feedback
    public String addToCart(int id, int quantity){
        
        String feedback = "Product not added to cart. Product not found or quantity less than 1";

        for (Product item : inventory) {
            if (item.getId() == id && quantity > 0 && !shoppingCart.containsKey(item)){
                shoppingCart.put(item, quantity);
                feedback = "Product added to cart";
            } else if (item.getId() == id && quantity > 0){
                shoppingCart.replace(item, quantity + shoppingCart.get(item));
                feedback = "Product added to cart";
            }
        }
        return feedback;
    }

    //Iterates through the shoppingCart map and if it finds a product that has the same id as the parameter
    //and the current quantity of that product minus the quantity parameter is above 0 it sets the products
    //quantity to be that value and returns feedback that an amount of the quantity parameter was removed from the cart
    //if the parameter quantity minus the products quantity is less than 0 it completely removes the product and quantity
    //pair from the map
    public String deleteFromCart(int id, int quantity){

        String feedback = "Product not deleted from cart. Product not found";

        //Used a for each loop, but sometimes got a ConcurrentModificationException
        //when trying to delete from the map, so i found out i needed to use an iterator when
        //trying to delete
        Iterator<Map.Entry<Product,Integer>> iterator = shoppingCart.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Product,Integer> item = iterator.next();
            if(item.getKey().getId() == id && (item.getValue() - quantity) > 0){
                shoppingCart.replace(item.getKey(),item.getValue() - quantity);
                feedback = "Removed " + quantity + " " + item.getKey().getName() + " from the shopping cart";
            }else if (item.getKey().getId() == id){
                iterator.remove();
                feedback = "" + item.getKey().getName() + " removed from the shopping cart";
            }
        }

        return feedback;
    }

    //displays the contents of the shoppingCart map, shows price for each product times the quantity
    //and lastly display the total for all products in the shopping cart
    public String displayShoppingCart(){
        String displayCart = "Shopping cart:";
        double total = 0;

        ArrayList<Map.Entry<Product,Integer>> sortingList = new ArrayList<>(shoppingCart.entrySet());

        //Sorts the ArrayList by the name of the Product keys in the map
        Collections.sort(sortingList, (k2,k1) -> (k1.getKey().getName()).compareTo(k2.getKey().getName()));
        //Reverses sortingList before printing
        Collections.reverse(sortingList);

        for (Map.Entry<Product, Integer> item: sortingList) {
            displayCart += "\nProduct: " + item.getKey().getName() + ". Quantity: " + item.getValue() +
                    ". Price: " + String.format("%.2f", item.getValue() * item.getKey().getCost());
            total += item.getValue() * item.getKey().getCost();
        }

        //String.format("%.2f") makes it so the value will only be displayed with 2 decimal numbers
        displayCart += "\nTotal : " + String.format("%.2f", total);

        return displayCart;
    }
}
