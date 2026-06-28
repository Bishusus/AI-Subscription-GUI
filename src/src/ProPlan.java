import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents a Pro Plan version of an AI model.
 * 
 * It extends the AIModel class and adds team management features,
 * allowing users to add and remove members within a limited number of slots.
 *
 * It also allows user to process their prompts beforehand and uses a binary search algorithm
 * to efficiently find and manage team members in a sorted list.
 *
 * The class keeps track of available slots and updates them whenever
 * members are added or removed.
 */
public class ProPlan extends AIModel
{
    private ArrayList<String> team;
    private int slots;
    
    // Constructor for initializing the values
    public ProPlan(String modelName, double price, int count, 
                        int contextWindow, int slots){
        super(modelName, price, count, contextWindow); // call parent constructor
        this.slots = slots;
        team =  new ArrayList<>();
    }
    
    // Getter for slots
    public int getRemainingSlots() {
        return this.slots;
    }

    // Getter for ArrayList<String> team
    public ArrayList<String> getTeam() {
        return team;
    }
    
    // Add members
    public String addMember(String memberName){
        if (slots < 1)
        {
            return "Not enough slots";
        }
        team.add(memberName);
        slots -= 1;
        return "Member: " + memberName + "\n" + "Succesfully added";
    }
    
    // Remove members
    public String removeMember(String memberName){
        // sorting the ArrayList to later use in binary search algorithm
        Collections.sort(team, String.CASE_INSENSITIVE_ORDER);
        int index = binarySearch(memberName, team);
        if (index < 0)
        {
            return "There's no such member with the given name";
        }
        else {
            team.remove(index);
        }
        slots++;
        return "Member: " + memberName + "\n" + "Succesfully removed" + "\n" +
        "Remaining slots:" + slots;
    }

    // Users prompt message
    public String enterPrompt(String text, int tokens){
        int totalTokens = 0;
        try {
            totalTokens = calculateTotalToken(text, tokens);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return "Prompt message: " + text + "\n" +
                "Expected output tokens: " + tokens + "\n" +
                "Total tokens usage: " + totalTokens + "\n";
    }
    
    // Binary Search algorithm to find the index of the team member 
    private int binarySearch(String memberName, ArrayList<String> team){
        int low = 0;
        int high = team.size() - 1;
        while (low <= high){
            int mid = (low + high) / 2;
            int index = team.get(mid).compareToIgnoreCase(memberName);
            if (index == 0){
                return mid;
            }
            else if (index < 0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return -1;
    }

    // Returning the values
    @Override
    public String toString(){
        return "Model Name: " + getModelName() + "\n" +
                "Price: " + getPrice() + "\n" +
                "Model Parameter Count: " + getCount() + "\n" +
                "Context Window: " + getContextWindow() + "\n" +
                "Remaining slots: " + this.slots + "\n" +
                "Members added: " + team.toString().replace("[", "").replace("]", "") + "\n";
    }
    
}