import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Main GUI application for managing AI model subscriptions.
 * Handles Personal and Pro plans, prompts, team members,
 * and file export/import operations.
 */
public class SubscriptionGUI extends JFrame implements ActionListener
{
    private ArrayList<AIModel> subscriptions;

    // File names for storing data permanently
    private static final String data_file     = "../../TextFiles/exportTo.txt";
    private static final String counter_file  = "../../TextFiles/recordCounter.txt";

    // Panels to hold labels and textfields
    JPanel headerPanel, westPanel, textPanel, radioPanel,
            namePanel, pricePanel, parameterPanel, contextPanel,
            quotaPanel, slotsPanel, promptPanel, responseLengthPanel,
            memberPanel, requiredPanel, optionalPanel, southPanel;

    JLabel headerLabel, requiredLabel, optionalLabel;

    JRadioButton personalRadio, proRadio;

    ButtonGroup planGroup;

    JTextArea displayArea;

    JTextField nameField, priceField, parameterField, contextField,
            quotaField, slotsField, promptField, responseLengthField,
            memberField;

    JButton personalPlanButton, proPlanButton, displayAllButton,
            clearButton, promptButton, addMemberButton, checkTypeButton, exportToButton, loadFromButton,
            purchaseTokenButton, removeMemberButton, displayByButton;

    // Table components
    private JTable planTable, loadTable;

    // Models for making table dynamic
    private DefaultTableModel tableModel, fileModel;
    private JScrollPane tableScrollPane, logScrollPane, buttonScrollPane, contentScrollPane;

    public SubscriptionGUI()
    {
        setTitle("Subscription GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        setBackground(new Color(240, 242, 245));

        subscriptions = new ArrayList<>();

        // Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 42, 56));
        headerPanel.setPreferredSize(new Dimension(0, 75));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerLabel = new JLabel("Welcome to Subscription GUI", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Required and optional labels for holding required and optional fields
        requiredLabel = new JLabel("Required Fields");
        requiredLabel.setFont(new Font("Arial", Font.BOLD, 14));

        optionalLabel = new JLabel("Optional Fields");
        optionalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // West button panel
        westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        westPanel.setBackground(new Color(250, 250, 250));

        Dimension btnSize = new Dimension(140, 35);

        personalPlanButton  = new JButton("Personal Plan");
        proPlanButton       = new JButton("Pro Plan");
        displayAllButton    = new JButton("Display All");
        clearButton         = new JButton("Clear");
        promptButton        = new JButton("Prompt");
        addMemberButton     = new JButton("Add Member");
        checkTypeButton     = new JButton("Check Type");
        exportToButton      = new JButton("Export to File");
        loadFromButton      = new JButton("Load From File");
        purchaseTokenButton = new JButton("Purchase Token");
        removeMemberButton  = new JButton("Remove Member");
        displayByButton     = new JButton("Display By Plan");

        JButton[] buttons = {
                personalPlanButton, proPlanButton, displayAllButton, displayByButton,
                clearButton, promptButton, addMemberButton, removeMemberButton, checkTypeButton,
                purchaseTokenButton, exportToButton, loadFromButton
        };

        // Defining equal properties for all the buttons
        for (JButton btn : buttons)
        {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusable(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(40, 40, 40));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.addActionListener(this);
        }

        westPanel.add(Box.createVerticalStrut(15));
        for (JButton btn : buttons)
        {
            westPanel.add(btn);
            westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        westPanel.add(Box.createVerticalGlue());

        buttonScrollPane = new JScrollPane(westPanel);
        buttonScrollPane.setBorder(BorderFactory.createTitledBorder("Buttons"));
        buttonScrollPane.setPreferredSize(new Dimension(180, 0));

        // Radio buttons
        radioPanel = new JPanel();
        radioPanel.setMaximumSize(new Dimension(500, 30));
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioPanel.setBackground(Color.WHITE);

        personalRadio = new JRadioButton("Personal Plan", true);
        personalRadio.setFocusable(false);
        personalRadio.setBackground(Color.WHITE);
        proRadio = new JRadioButton("Pro Plan");
        proRadio.setFocusable(false);
        proRadio.setBackground(Color.WHITE);

        planGroup = new ButtonGroup();
        planGroup.add(personalRadio);
        planGroup.add(proRadio);

        radioPanel.add(Box.createHorizontalGlue());
        radioPanel.add(personalRadio);
        radioPanel.add(Box.createHorizontalStrut(5));
        radioPanel.add(proRadio);
        radioPanel.add(Box.createHorizontalGlue());

        personalRadio.addActionListener(this);
        proRadio.addActionListener(this);

        // Centre text / form panel
        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        textPanel.setBackground(Color.WHITE);

        nameField           = new JTextField(20);
        priceField          = new JTextField(20);
        parameterField      = new JTextField(20);
        contextField        = new JTextField(20);
        quotaField          = new JTextField(20);
        slotsField          = new JTextField(20);
        promptField         = new JTextField(20);
        responseLengthField = new JTextField(20);
        memberField         = new JTextField(20);

        JTextField[] fields = {
                nameField, priceField, parameterField, contextField,
                quotaField, slotsField, promptField, responseLengthField, memberField
        };

        for (JTextField f : fields)
        {
            f.setBackground(Color.WHITE);
            f.setForeground(new Color(30, 30, 30));
            f.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }

        namePanel           = createRow("Name:",     nameField);
        pricePanel          = createRow("Price:",    priceField);
        parameterPanel      = createRow("Parameter:", parameterField);
        contextPanel        = createRow("Context:",  contextField);
        quotaPanel          = createRow("Tokens:",   quotaField);
        slotsPanel          = createRow("Slots:",    slotsField);
        promptPanel         = createRow("Prompt:",   promptField);
        responseLengthPanel = createRow("Response:", responseLengthField);
        memberPanel         = createRow("Member:",   memberField);

        textPanel.add(radioPanel);
        textPanel.add(Box.createVerticalStrut(10));

        requiredPanel = new JPanel();
        requiredPanel.setLayout(new BoxLayout(requiredPanel, BoxLayout.Y_AXIS));
        requiredPanel.setBorder(BorderFactory.createTitledBorder("Required"));
        requiredPanel.add(namePanel);
        requiredPanel.add(pricePanel);
        requiredPanel.add(parameterPanel);
        requiredPanel.add(contextPanel);
        requiredPanel.add(quotaPanel);
        requiredPanel.add(slotsPanel);

        optionalPanel = new JPanel();
        optionalPanel.setLayout(new BoxLayout(optionalPanel, BoxLayout.Y_AXIS));
        optionalPanel.setBorder(BorderFactory.createTitledBorder("Optional"));
        optionalPanel.add(promptPanel);
        optionalPanel.add(responseLengthPanel);
        optionalPanel.add(memberPanel);

        textPanel.add(requiredPanel);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(optionalPanel);

        JScrollPane textPane = new JScrollPane(textPanel);
        textPane.setBorder(null);
        textPane.getVerticalScrollBar().setUnitIncrement(10);

        // Display area (text log)
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        logScrollPane = new JScrollPane(displayArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Display Area"));
        logScrollPane.setPreferredSize(new Dimension(0, 200));

        // JTable for "Display By Plan"
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        planTable = new JTable(tableModel);
        planTable.setFillsViewportHeight(true);
        planTable.setFont(new Font("Arial", Font.PLAIN, 12));
        planTable.setRowHeight(22);
        planTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        planTable.setSelectionBackground(new Color(184, 207, 229));

        tableScrollPane = new JScrollPane(planTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Plan Table"));
        tableScrollPane.setPreferredSize(new Dimension(0, 150));
        tableScrollPane.setVisible(false);   // hidden until "Display By Plan" is used
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // South panel holds both the log and the table
        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(logScrollPane);
        southPanel.add(tableScrollPane);

        // Add everything to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(buttonScrollPane,   BorderLayout.WEST);
        add(textPane,    BorderLayout.CENTER);
        add(southPanel,  BorderLayout.SOUTH);

        setVisible(true);
        updatePlanUI();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    // Helper for building textPanels
    private JPanel createRow(String labelText, JTextField field)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(600, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));

        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(field);
        return panel;
    }

    // Making the UI dynamic based on which plan is selected
    private void updatePlanUI()
    {
        boolean isPersonal = personalRadio.isSelected();

        if (isPersonal) {
            requiredPanel.remove(slotsPanel);
            optionalPanel.remove(memberPanel);
            requiredPanel.add(quotaPanel);
        } else {
            requiredPanel.add(slotsPanel);
            optionalPanel.add(memberPanel);
            requiredPanel.remove(quotaPanel);
        }

        // part which makes the UI dynamic
        textPanel.revalidate();
        textPanel.repaint();
    }

    // Index validation
    private int getValidIndex()
    {
        int displayNumber = -1;
        String input = JOptionPane.showInputDialog("Enter the index number:");
        if (input == null) return displayNumber;

        // Error handling
        try {
            int index = Integer.parseInt(input.trim());
            if (index >= 0 && index < subscriptions.size())
                displayNumber = index;
            else
                JOptionPane.showMessageDialog(this, "Error: Index is out of range.", "Invalid Index", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer.", "Invalid Index", JOptionPane.ERROR_MESSAGE);
        }
        return displayNumber;
    }

    // Validating required fields whenever users try purchasing a specific plan
    private boolean validateRequiredFields(boolean isPersonal)
    {
        if (nameField.getText().isEmpty() ||
                priceField.getText().isEmpty() ||
                parameterField.getText().isEmpty() ||
                contextField.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill out all required fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        if (isPersonal && quotaField.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill out the quota field.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        if (!isPersonal && slotsField.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill out the slots field.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        return false;
    }

    /**
     * Reads the persistent record counter from file.
     * Returns 0 if the counter file does not exist yet.
     */
    private int readRecordCounter()
    {
        File f = new File(counter_file);
        if (!f.exists()) return 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f)))
        {
            String line = br.readLine();
            if (line != null) return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException ignored) {}
        return 0;
    }

    // writes the updated record counter to file
    private void writeRecordCounter(int value)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(counter_file)))
        {
            bw.write(String.valueOf(value));
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Warning: could not update record counter.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ── Action dispatch ───────────────────────────────────────────────────────

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if      (e.getSource() == personalPlanButton)  handlePersonalPlan();
        else if (e.getSource() == proPlanButton)        handleProPlan();
        else if (e.getSource() == clearButton)          clearFields();
        else if (e.getSource() == displayAllButton)     handleDisplayAll();
        else if (e.getSource() == promptButton)         handlePrompt();
        else if (e.getSource() == addMemberButton)      handleAddMember();
        else if (e.getSource() == checkTypeButton)      handleCheckType();
        else if (e.getSource() == personalRadio || e.getSource() == proRadio) updatePlanUI();
        else if (e.getSource() == exportToButton)       handleExportTo();
        else if (e.getSource() == loadFromButton)       handleLoadTo();
        else if (e.getSource() == displayByButton)      handleDisplayByPlan();
        else if (e.getSource() == removeMemberButton)   handleRemoveMember();
        else if (e.getSource() == purchaseTokenButton)  handlePurchaseToken();
    }

    // ── Button handlers ───────────────────────────────────────────────────────

    // Method for handling personal plan subscription
    private void handlePersonalPlan()
    {
        if (validateRequiredFields(true)) return;
        try {
            String name      = nameField.getText();
            double price     = Double.parseDouble(priceField.getText());
            int parameter    = Integer.parseInt(parameterField.getText());
            int context      = Integer.parseInt(contextField.getText());
            int tokens       = Integer.parseInt(quotaField.getText());

            PersonalPlan plan = new PersonalPlan(name, price, parameter, context, tokens);
            subscriptions.add(plan);
            JOptionPane.showMessageDialog(this, "Personal Plan added successfully!");
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method for handling pro plan subscription
    private void handleProPlan()
    {
        if (validateRequiredFields(false)) return;
        try {
            String name   = nameField.getText();
            double price  = Double.parseDouble(priceField.getText());
            int parameter = Integer.parseInt(parameterField.getText());
            int context   = Integer.parseInt(contextField.getText());
            int slots     = Integer.parseInt(slotsField.getText());

            ProPlan plan = new ProPlan(name, price, parameter, context, slots);
            subscriptions.add(plan);
            JOptionPane.showMessageDialog(this, "Pro Plan added successfully!");
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method for displaying the information about the purchased plans all at once
    private void handleDisplayAll()
    {
        displayArea.setText("");
        for (int i = 0; i < subscriptions.size(); i++)
        {
            AIModel plan = subscriptions.get(i);
            displayArea.append("Index: " + i + "\n");
            displayArea.append(plan.toString());
            displayArea.append("------------------------------------\n\n");
        }
    }

    // Method to make a prompt if personal plan instance
    private void handlePrompt()
    {
        int index = getValidIndex();
        if (index == -1) return;

        AIModel model = subscriptions.get(index);
        String prompt = promptField.getText();
        int responseLength;

        try {
            responseLength = Integer.parseInt(responseLengthField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Response length must be a valid number", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (model instanceof PersonalPlan) {
            PersonalPlan personal = (PersonalPlan) model;
            String result = personal.enterPrompt(prompt, responseLength);
            displayArea.setText(result + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Error: This operation is only available for Personal Plan subscriptions.", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Adding a member to the team if pro plan instance
    private void handleAddMember()
    {
        int index = getValidIndex();
        if (index == -1) return;

        AIModel model = subscriptions.get(index);
        String memberName = memberField.getText();

        if (model instanceof ProPlan) {
            ProPlan pro = (ProPlan) model;
            String message = pro.addMember(memberName);
            JOptionPane.showMessageDialog(this, message);
        } else {
            JOptionPane.showMessageDialog(this, "Error: Team collaboration is only available for Pro Plan subscriptions.", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to check type of specific plan in the ArrayList
    private void handleCheckType()
    {
        int index = getValidIndex();
        if (index == -1) return;
        AIModel model = subscriptions.get(index);

        if      (model instanceof PersonalPlan) JOptionPane.showMessageDialog(this, "Personal Plan");
        else if (model instanceof ProPlan)      JOptionPane.showMessageDialog(this, "Pro Plan");
        else    JOptionPane.showMessageDialog(this, "Unknown Plan Type", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Method to display the plan information based on whether personal or pro plan was selected
    private void handleDisplayByPlan()
    {
        logScrollPane.setPreferredSize(new Dimension(0, 150));
        String[] options = {"Personal Plan", "Pro Plan"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select the plan type to display:",
                "Display By Plan",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) return;   // user dismissed

        boolean showPersonal = (choice == 0);

        // Build column headers based on plan type
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Display personal plan info else display pro plan info
        if (showPersonal) {
            tableModel.setColumnIdentifiers(new String[]{
                    "Index", "Name", "Price", "Parameters (B)", "Context (K)", "Token Quota" // table header for personal plan
            });
            for (int i = 0; i < subscriptions.size(); i++) {
                if (subscriptions.get(i) instanceof PersonalPlan) {
                    PersonalPlan p = (PersonalPlan) subscriptions.get(i);
                    tableModel.addRow(new Object[]{
                            i,
                            p.getModelName(),
                            String.format("Rs.%.2f", p.getPrice()),
                            p.getCount(),
                            p.getContextWindow(),
                            p.getAvailableTokens()
                    });
                }
            }
        } else {
            tableModel.setColumnIdentifiers(new String[]{
                    "Index", "Name", "Price", "Parameters (B)", "Context (K)", "Slots", "Members" // table header for pro plan
            });
            for (int i = 0; i < subscriptions.size(); i++) {
                if (subscriptions.get(i) instanceof ProPlan) {
                    ProPlan p = (ProPlan) subscriptions.get(i);
                    tableModel.addRow(new Object[]{
                            i,
                            p.getModelName(),
                            String.format("Rs%.2f", p.getPrice()),
                            p.getCount(),
                            p.getContextWindow(),
                            p.getRemainingSlots(),
                            p.getTeam().size()
                    });
                }
            }
        }

        // Show the table and refresh layout
        tableScrollPane.setVisible(true);
        southPanel.revalidate();
        southPanel.repaint();
    }

    // Method to remove a member from a pro plan
    private void handleRemoveMember()
    {
        int index = getValidIndex();
        if (index == -1) return;

        AIModel model = subscriptions.get(index);

        if (model instanceof ProPlan) {

            ProPlan pro = (ProPlan) model;
            String memberName = memberField.getText().trim();

            if (memberName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter the member name in the Member field.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String proMember = pro.removeMember(memberName);
            JOptionPane.showMessageDialog(this, proMember);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: Remove Member is only available for Pro Plan subscriptions.",
                    "Invalid Operation",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Purchase Token (split: current session vs. past / file) ──────────────

    // Method to add tokens to the personal plan based on the source the user choose
    private void handlePurchaseToken()
    {
        // Asking the users which source they want to operate on
        String[] sourceOptions = {"Current Session", "Past Operations (File)"};
        int sourceChoice = JOptionPane.showOptionDialog(
                this,
                "Where do you want to purchase tokens?",
                "Purchase Token – Select Source",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                sourceOptions,
                sourceOptions[0]
        );

        if (sourceChoice == JOptionPane.CLOSED_OPTION) return;   // user cancelled

        if (sourceChoice == 0)
        {
            // Current session
            handlePurchaseTokenCurrentSession();
        }
        else
        {
            // Past operations stored in the file
            handlePurchaseTokenFromFile();
        }
    }

    /**
     * Original purchase-token logic: operates on the in-memory subscriptions list.
     */
    private void handlePurchaseTokenCurrentSession()
    {
        int index = getValidIndex();
        if (index == -1) return;

        AIModel model = subscriptions.get(index);

        if (model instanceof PersonalPlan) {
            String tokenInput = JOptionPane.showInputDialog(this, "Enter the number of tokens to purchase:");
            if (tokenInput == null) return;

            try {
                int tokenAmount = Integer.parseInt(tokenInput.trim());
                if (tokenAmount <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Token amount must be a positive number.",
                            "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PersonalPlan personal = (PersonalPlan) model;
                personal.purchaseTokens(tokenAmount);
                JOptionPane.showMessageDialog(this,
                        tokenAmount + " tokens purchased successfully!\n" +
                                "New token quota: " + personal.getAvailableTokens());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid integer for the token amount.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: Purchase Token is only available for Personal Plan subscriptions.",
                    "Invalid Operation",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Asks the user for a file-level index, validates that the
     * corresponding record is a PersonalPlan, then increases its token count
     * and rewrites the file.
     */
    private void handlePurchaseTokenFromFile()
    {
        // Check if the file exists
        File dataFile = new File(data_file);
        if (!dataFile.exists())
        {
            JOptionPane.showMessageDialog(this,
                    "No exported data file found. Please export records first.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asking for the file-index of the record to update
        String indexInput = JOptionPane.showInputDialog(this,
                "Enter the file index of the Personal Plan to add tokens to:");
        if (indexInput == null) return;

        int targetIndex;
        try {
            targetIndex = Integer.parseInt(indexInput.trim());
            if (targetIndex < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid non-negative integer for the index.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tokenInput = JOptionPane.showInputDialog(this,
                "Enter the number of tokens to purchase:");
        if (tokenInput == null) return;

        int tokenAmount;
        // Error Handling
        try {
            tokenAmount = Integer.parseInt(tokenInput.trim());
            if (tokenAmount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Token amount must be a positive integer.",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read all records, update the matching one, rewrite the file
        boolean updated = updateTokensInFile(targetIndex, tokenAmount);

        if (updated)
        {
            JOptionPane.showMessageDialog(this,
                    tokenAmount + " tokens added to file record #" + targetIndex + " successfully.");
        }
    }

    /**
     * Reads exportTo.txt, finds the record whose "Index:" line matches
     * Verifies if it's a PersonalPlan, and adds token if it is
     * writing the entire file with the updated content.
     * Returns true if file update was successful otherwise returns false
     */
    private boolean updateTokensInFile(int targetIndex, int tokensToAdd)
    {
        // Read every raw line into memory
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_file)))
        {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int blockStartLine = -1;   // line index of "Type:" for the target block
        String blockType   = null;

        for (int i = 0; i < lines.size(); i++)
        {
            String trimmed = lines.get(i).trim();

            if (trimmed.startsWith("Type:"))
            {
                String ahead = lines.get(i+1).trim();
                if (ahead.startsWith("Index:"))
                {
                    int idx = Integer.parseInt(ahead.replace("Index:", "").trim());
                    if (idx == targetIndex)
                    {
                        blockStartLine = i;
                        blockType = trimmed.replace("Type:", "").trim();
                    }
                }
            }
            if (blockStartLine != -1) break;
        }

        // Validate: record must exist
        if (blockStartLine == -1)
        {
            JOptionPane.showMessageDialog(this,
                    "No record with index " + targetIndex + " found in the file.",
                    "Index Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate: must be PersonalPlan
        if (!"PersonalPlan".equals(blockType))
        {
            JOptionPane.showMessageDialog(this,
                    "Record #" + targetIndex + " is a " + blockType +
                            ".\nPurchase Token is only available for Personal Plan records.",
                    "Invalid Plan Type",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Find and update the "Remaining prompts:" line within this block
        boolean tokenLineFound = false;
        for (int i = blockStartLine + 1; i < lines.size(); i++)
        {
            String trimmed = lines.get(i).trim();
            if (trimmed.startsWith("Remaining prompts:"))
            {
                String valueStr = trimmed.replace("Remaining prompts:", "").trim();
                try {
                    int currentTokens = Integer.parseInt(valueStr);
                    int newTokens     = currentTokens + tokensToAdd;
                    // Preserve original indentation
                    String indent = lines.get(i).substring(0, lines.get(i).indexOf("Remaining prompts:"));
                    lines.set(i, indent + "Remaining prompts: " + newTokens);
                    tokenLineFound = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Could not parse token count in file.", "Parse Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            }
        }

        if (!tokenLineFound)
        {
            JOptionPane.showMessageDialog(this,
                    "Could not find token field for record #" + targetIndex + " in the file.",
                    "Field Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Rewrite the file with the updated lines
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(data_file)))
        {
            for (String line : lines)
            {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing updated data to file.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Clear ─────────────────────────────────────────────────────────────────

    private void clearFields()
    {
        nameField.setText("");
        priceField.setText("");
        parameterField.setText("");
        contextField.setText("");
        quotaField.setText("");
        slotsField.setText("");
        promptField.setText("");
        responseLengthField.setText("");
        memberField.setText("");
    }

    // ── Export ────────────────────────────────────────────────────────────────

    /**
     * Appends the current session's subscriptions to the data file, assigning
     * each new record a unique persistent index drawn from recordCounter.txt.
     * Existing records already in the file are preserved unchanged.
     */
    private void handleExportTo()
    {
        // Read the current counter (total records written in all previous runs)
        int counter = readRecordCounter();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(data_file, true)))  // append mode
        {
            for (AIModel plan : subscriptions)
            {
                bw.write("Type: " + plan.getClass().getSimpleName() + "\n");
                bw.write("Index: " + counter + "\n");   // persistent, unique index
                bw.write(plan.toString());
                bw.write("------------------------------------\n\n");
                counter++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Persist the updated counter so the next run continues from here
        writeRecordCounter(counter);

        JOptionPane.showMessageDialog(this,
                "Exported " + subscriptions.size() + " record(s).\n" +
                        "Total records in file: " + counter);
    }

    // ── Load From File ────────────────────────────────────────────────────────

    // Dynamically load from file based on what option the user selects
    private void handleLoadTo()
    {
        String[] options = {"Personal Plan", "Pro Plan"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select the plan type to display:",
                "Display By Plan",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        boolean isPersonal = (choice == 0);
        if (choice == JOptionPane.CLOSED_OPTION) return;

        createGUI();
        fileModel.setRowCount(0);
        fileModel.setColumnCount(0);

        if (isPersonal) {
            fileModel.setColumnIdentifiers(new String[]{
                    "File Index", "Name", "Price", "Parameters (B)", "Context (K)", "Token Quota"
            });
        } else {
            fileModel.setColumnIdentifiers(new String[]{
                    "File Index", "Name", "Price", "Parameters (B)", "Context (K)", "Slots", "Members"
            });
        }

        try (BufferedReader br = new BufferedReader(new FileReader(data_file))) {
            String line;
            String currentType  = null;
            String currentIndex = null;
            ArrayList<String> info = new ArrayList<>();

            while ((line = br.readLine()) != null)
            {
                line = line.trim();

                if (line.startsWith("Type:"))
                {
                    currentType  = line.replace("Type:", "").trim();
                    currentIndex = null;
                    info.clear();

                    if (isPersonal  && !currentType.equals("PersonalPlan")) currentType = null;
                    if (!isPersonal && !currentType.equals("ProPlan"))      currentType = null;
                    continue;
                }

                if (line.startsWith("Index:"))
                {
                    // Capture file-level index regardless of whether currentType is set
                    if (currentType != null)
                        currentIndex = line.replace("Index:", "").trim();
                    continue;
                }

                if (currentType != null) {
                    if (line.startsWith("---")) {
                        addRowToTable(currentType, info, currentIndex);
                        info.clear();
                        currentIndex = null;
                        continue;
                    }
                    if (!line.isEmpty()) {
                        info.add(line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not available!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while reading file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a row to the file table.  The fileIndex parameter is the
     * persistent index stored in the file .
     */
    private void addRowToTable(String type, ArrayList<String> data, String fileIndex)
    {
        if (type == null || data.isEmpty()) return;

        String displayIndex = (fileIndex != null) ? fileIndex : "?";
        String name    = extractValue(data, "Model Name:");
        String price   = extractValue(data, "Price:");
        String param   = extractValue(data, "Model Parameter Count:");
        String context = extractValue(data, "Context Window:");

        if (type.equals("PersonalPlan"))
        {
            String tokens = extractValue(data, "Remaining prompts:");
            fileModel.addRow(new Object[]{
                    displayIndex, name, price, param, context, tokens
            });
        }
        else if (type.equals("ProPlan"))
        {
            String slots  = extractValue(data, "Remaining slots:");
            int memberCount = extractValue(data, "Members added:").split(",").length;
            fileModel.addRow(new Object[]{
                    displayIndex, name, price, param, context, slots, memberCount
            });
        }
    }

    // Extracts the cell values to be stored in the table
    private String extractValue(ArrayList<String> data, String key)
    {
        for (String line : data)
        {
            if (line.startsWith(key))
                return line.replace(key, "").trim();
        }
        return "";
    }

    // Create a GUI for representing file information
    private void createGUI()
    {
        JFrame newFrame = new JFrame("File Contents");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(720, 720);
        newFrame.setResizable(false);
        newFrame.setLocationRelativeTo(null);

        JPanel headPanel = new JPanel(new BorderLayout());
        headPanel.setPreferredSize(new Dimension(0, 75));
        headPanel.setBackground(new Color(30, 42, 56));

        JLabel title = new JLabel("File Contents");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        headPanel.add(title, BorderLayout.CENTER);

        fileModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        loadTable = new JTable(fileModel);
        loadTable.setFillsViewportHeight(true);
        loadTable.setFont(new Font("Arial", Font.PLAIN, 12));
        loadTable.setRowHeight(22);
        loadTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        loadTable.setSelectionBackground(new Color(184, 207, 229));

        contentScrollPane = new JScrollPane(loadTable);
        contentScrollPane.setPreferredSize(new Dimension(0, 150));

        newFrame.add(headPanel, BorderLayout.NORTH);
        newFrame.add(contentScrollPane, BorderLayout.CENTER);
        newFrame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new SubscriptionGUI());
    }
}