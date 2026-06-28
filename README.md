# AI-Subscription-GUI

One-sentence summary
A small Java Swing desktop application to manage AI-model subscription plans (Personal and Pro), simulate prompt/token usage, manage team members for Pro plans, and persist records to a simple text export file.

Overview
This application provides a GUI for creating and managing two subscription types:
- PersonalPlan — per-user token quota (prompt usage and token purchase).
- ProPlan — team-based plan with a limited number of slots and member management.
The app estimates token usage from prompt text, enforces model context window limits, and supports exporting/loading records to/from a plain text file.

Features
- Create and store multiple subscriptions in-memory: PersonalPlan and ProPlan.
- PersonalPlan:
  - Track available tokens (quota).
  - Submit a prompt (consumes tokens) with a requested response length.
  - Purchase additional tokens (in-session or by updating an exported file record).
- ProPlan:
  - Add and remove team members (slots-limited).
  - Remove uses sorting + binary search for lookup.
- Export current session plans to a text file with persistent unique indices.
- Load and display records from the export file in a simple table.
- Update token counts in exported PersonalPlan records (file-based purchase).
- Simple UI with dedicated buttons for all main operations (add plans, prompt, add/remove member, export, load, etc).

Algorithm / How it works (core logic)
- AIModel (abstract base)
  - Fields: modelName, price, count (parameters), contextWindow.
  - Method calculateTotalToken(promptText, expectedOutputTokens):
    - Estimates input tokens by splitting promptText on spaces (inputTokens = number of words).
    - totalTokens = inputTokens + expectedOutputTokens.
    - Throws IllegalArgumentException if totalTokens > contextWindow.
- PersonalPlan extends AIModel
  - Field: availableTokens.
  - enterPrompt(text, tokens):
    - Uses calculateTotalToken to compute total token usage for the prompt.
    - If availableTokens >= totalTokens, reduces availableTokens by totalTokens and returns a summary string.
    - If not, returns "Not enough tokens remaining".
  - purchaseTokens(tokens) adds tokens to availableTokens (with a small validation).
- ProPlan extends AIModel
  - Fields: ArrayList<String> team, int slots.
  - addMember(name): adds to team if slots > 0 and decrements slots.
  - removeMember(name): sorts team (case-insensitive) then uses binary search to find index and remove; increments slots back.
  - enterPrompt(text, tokens): computes tokens via calculateTotalToken but does not consume a quota (ProPlan prompts are informational in this code).
  - Binary search implementation:
    - Standard iterative binary search on the sorted ArrayList<String>, using compareToIgnoreCase for comparisons.
    - Returns index if found, -1 otherwise.
- Persistence & file format (exportTo.txt)
  - Export writes blocks in plain text per plan:
    - Type: <PersonalPlan|ProPlan>
    - Index: <persistent integer>
    - Then writes plan.toString() (which contains lines like "Model Name:", "Remaining prompts:" or "Remaining slots:" and so on).
    - Ends block with a separator line `------------------------------------`.
  - A separate counter file (recordCounter.txt) stores the next persistent index for exported records.
  - Loading reads the file, filters by Type, and parses key-value lines to populate a file table view.
  - Updating tokens in-file:
    - The app locates the block by Type and Index, finds "Remaining prompts:" line, parses and updates the integer, then rewrites the file.

Project structure (key files)
```
TextFiles/                 # expected target for exports (see Notes)
src/src/
  AIModel.java             # abstract base: token accounting & context-window check
  PersonalPlan.java        # Personal plan logic and token quota management
  ProPlan.java             # Pro plan logic, team management + binary search
  SubscriptionGUI.java     # Swing GUI, button handlers, export/load/update file logic
.idea/                     # IntelliJ project files (ignored by build steps)
out/                       # build / compiled output (if present locally)
```

How it fits together
- The GUI (SubscriptionGUI) keeps an ArrayList<AIModel> subscriptions for the current session.
- Button handlers in SubscriptionGUI create PersonalPlan or ProPlan objects and add them to this list.
- Prompts and member operations dispatch to the appropriate subclass (PersonalPlan or ProPlan) via instanceof checks and casts.
- Export and load operations are text-file based: export appends human-readable blocks to an export file and the record counter ensures unique persistent indices across runs.

How to run (development / quickstart)
This is a plain Java Swing application using the JDK standard library (no external dependencies). Build and run from the repository root.

1) Ensure you have a JDK installed (Java 8+ recommended).
2) Create the TextFiles directory the app expects (or update the file path constants in SubscriptionGUI):
   - Recommended: create a folder at repository-root named `TextFiles`.
   - Then create two empty files inside it:
     - TextFiles/exportTo.txt
     - TextFiles/recordCounter.txt
   - SubscriptionGUI currently references:
     - data_file = "../../TextFiles/exportTo.txt"
     - counter_file = "../../TextFiles/recordCounter.txt"
    If running from the project root, you may need to adjust these paths or change the working directory so the program finds the files (see Notes below).

3) Compile:
```bash
# from repository root
javac -d out/ ./src/src/*.java
```

4) Run:
```bash
java -cp out/ SubscriptionGUI
```

Notes about file paths
- SubscriptionGUI uses relative paths ("../../TextFiles/…"). The correct placement depends on the working directory used when launching the app. Easiest options:
  - Run from the project root and change the constants to:
    - "./TextFiles/exportTo.txt" and "./TextFiles/recordCounter.txt"
  - Or create the TextFiles folder such that the path referenced by the code resolves (two directories up from where the class files are loaded).
- If you package as a JAR, update SubscriptionGUI's data_file and counter_file constants to an absolute or project-relative path.

Basic usage / workflow (GUI)
1. Launch the app. The main window exposes:
   - Left-side buttons (create Personal Plan, create Pro Plan, Display All, Display By Plan, Prompt, Add/Remove Member, Purchase Token, Export to File, Load From File, etc).
   - Center form to enter plan details (Name, Price, Parameters, Context, Tokens (Personal), Slots (Pro), Prompt & Response length, Member).
   - Bottom area shows a textual display log and a hidden table that appears for file/table displays.

2. Add a Personal Plan:
   - Select "Personal Plan" or ensure the radio button is set, fill Name, Price, Parameter, Context, Tokens, then click "Personal Plan".
   - The plan is added to the in-memory list.

3. Use a prompt (PersonalPlan only):
   - Click "Prompt", enter an index (the numeric index printed in Display All), type a prompt, and the expected output token count, then submit.
   - The app calculates input tokens (word count) + expected output tokens, ensures it does not exceed context window, and deducts tokens from availableTokens.

4. Add members to a ProPlan:
   - Add a ProPlan, then use "Add Member" (choose plan index). Removal uses binary search over a sorted team list.

5. Export session:
   - Click "Export to File" to append all current subscriptions to TextFiles/exportTo.txt. The file will contain Type:, Index:, plan info, and separators. recordCounter.txt will be updated.

6. Load from File:
   - Click "Load From File", select Personal or Pro; a new window opens listing records found in the export file.

7. Purchase tokens from file:
   - Use "Purchase Token" → choose "Past Operations (File)", enter the persistent file index shown in the file view and token amount; the code will locate the block, update Remaining prompts:, and rewrite the file.

Limitations & suggestions
- Token estimation is naive (split by spaces) — consider integrating a tokenizer if you need accurate token accounting (e.g., GPT-style tokenizers).
- The file format is human-friendly but brittle — using a structured format (JSON or CSV) would simplify parsing and updates.
- File path constants are fragile (relative paths). Consider using a config file or allowing the user to pick a file via a JFileChooser.
- ProPlan.team removal sorts the list on every remove; for larger lists, maintain sorted insertion or use a TreeSet (if duplicates are not allowed) or more robust data structure.

Developer notes / important places in code
- src/src/AIModel.java — calculateTotalToken() core check
- src/src/PersonalPlan.java — enterPrompt(), purchaseTokens()
- src/src/ProPlan.java — addMember(), removeMember(), binarySearch()
- src/src/SubscriptionGUI.java — GUI layout, all button handlers, export/load/update file logic, readRecordCounter(), writeRecordCounter(), updateTokensInFile()

Try asking
- How should I change the file path constants so export/load use `./TextFiles` when running from the project root?
- Can you add JSON-based export/import instead of the current plain-text format and show the code changes to SubscriptionGUI?
- Could we replace the simple "word count" token estimator with a real tokenizer (example: using a small tokenizer lib or integrating an OpenAI tokenizer)?

License & author
- Add a LICENSE file to the repo (MIT is a common choice) and an AUTHORS or CONTRIBUTING file if you want contributions.
