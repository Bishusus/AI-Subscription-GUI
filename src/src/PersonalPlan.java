
/**
 * This class represents a personal AI model plan that manages a user's token usage.
 * 
 * It extends the AIModel class and adds functionality specific to individual users,
 * such as tracking available tokens, processing prompts, and allowing users to
 * purchase additional tokens when needed.
 *
 * The class also ensures that token usage stays within limits and handles cases
 * where the user tries to exceed their available tokens or the model’s context window.
 */
public class PersonalPlan extends AIModel
{
    private int availableTokens;
    
    // Constructor for initializing the values
    public PersonalPlan(String modelName, double price, int count, 
                        int contextWindow, int availableTokens){
        super(modelName, price, count, contextWindow); // call parent constructor
        this.availableTokens = availableTokens;
    }
    
    // Getter for availableTokens
    public int getAvailableTokens(){
        return this.availableTokens;
    }
    
    // Purchase additional prompts
    public String purchaseTokens(int tokens){
        if (tokens < 0)
        {
            return "User must enter a positive value or user must upgrade to pro plan";
        }
        this.availableTokens += tokens;
        return "Purchase successful" + "\n" + "Remaining prompts: " + this.availableTokens + "\n";
    }
    
    // Users prompt message
    public String enterPrompt(String text, int tokens){
        int totalTokens = 0;
        // Error handling
        try {
            totalTokens = calculateTotalToken(text, tokens);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        int valid = this.availableTokens - totalTokens;
        if (valid < 0)
        {
            return "Not enough tokens remaining";
        }
        else{
            this.availableTokens -= totalTokens;
        }
        if (this.availableTokens == 0)
        {
            return "Prompt message: " + text + "\n" +
                "Expected output tokens: " + tokens + "\n" +
                "Remaining tokens: "  + this.availableTokens + "\n" +
                "Monthly plan has been reached";
        } 
        return "Prompt message: " + text + "\n" +
                "Expected output tokens: " + tokens + "\n" +
                "Total tokens usage: " + totalTokens + "\n" +
                "Remaining tokens: "  + this.availableTokens + "\n";
    }
    
    // Returning the values
    @Override
    public String toString(){
        return "Model Name: " + getModelName() + "\n" +
                "Price: " + getPrice() + "\n" +
                "Model Parameter Count: " + getCount() + "\n" +
                "Context Window: " + getContextWindow() + "\n" +
                "Remaining prompts: " + this.availableTokens + "\n";
    }
}