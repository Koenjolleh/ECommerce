public class Product {

    private static int productId = 0;

    private int id;
    private String name;
    private String description;
    private double cost;
    private String category;

    public Product(String name, String description, double cost, String category) {
        this.id = productId++;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }

    public void setProductId(int maxIdInInventory){
        productId = maxIdInInventory+1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){
        return "Id: " + this.id + "\nName: " + this.name + "\nCost: " + this.cost
                + "\nCategory: " + this.category + "\nDescription:\n" + this.description;
    }
}
