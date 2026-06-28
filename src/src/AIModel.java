
/**
 * Represents an abstract AI model with attributes such as model name, price,
 * usage count, and context window size.
 * 
 * It provides functionality to manage model details and calculate token usage
 * while ensuring it does not exceed the allowed context window.
 *
 * This class serves as a base class for different AI model implementations.
 *
 */
public abstract class AIModel
{
    private String modelName;
    private double price;
    private int count;
    private int contextWindow;
    
    // Constructor for initializing the values
    public AIModel(String modelName, double price, int count, int contextWindow){
        this.modelName = modelName;
        this.price = price;
        this.count = count;
        this.contextWindow = contextWindow;
    }
    
    // Getter and Setter for modelName
    public String getModelName(){
        return modelName;
    }

    public void setModelName(String modelName){
        this.modelName = modelName;
    }

    // Getter and Setter for price
    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    // Getter and Setter for count
    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

    // Getter and Setter for contextWindow
    public int getContextWindow(){
        return contextWindow;
    }

    public void setContextWindow(int contextWindow){
        this.contextWindow = contextWindow;
    }

    // Returning the values
    public abstract String toString();
    
    // Calculate total tokens usage when making a prompt
    public int calculateTotalToken(String promptText, int expectedOutputTokens) {
        String[] text = promptText.split(" ");
        int inputTokens = text.length;
        int totalTokens = inputTokens +  expectedOutputTokens;
        if (totalTokens > getContextWindow())
        {
            throw new IllegalArgumentException(totalTokens + " is greater than " + getContextWindow());
        }
        return totalTokens;
    }
}