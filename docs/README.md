# Cherry User Guide

**Cherry** is a desktop task management app that works both as a Command Line Interface (CLI) application and as a Graphical User Interface (GUI) application. Whether you prefer typing commands quickly or clicking through a visual interface, Cherry adapts to your workflow.

---

## Quick Start

### Option 1: GUI Mode (Recommended for Beginners)

1. **Ensure you have Java 17 or above** installed on your computer.
    - Mac users: Ensure you have the precise JDK version as prescribed.

2. **Download the latest `.jar` file** from [here](https://github.com/YOUR_USERNAME/ip/releases).

3. **Double-click the jar file** to launch the GUI, or open a command terminal and run:
```
   java -jar cherry.jar
```

4. **A GUI similar to the below should appear** in a few seconds:
   ![Cherry GUI](Ui.png)

5. **Type a command** in the text field at the bottom and press Enter or click Send to execute it. Example commands:
    - `list` : Lists all tasks
    - `todo read book` : Adds a todo task "read book"
    - `bye` : Exits the app

6. **Your messages appear on the right** (coral/pink background), and **Cherry's responses appear on the left** (light pink background).

7. Refer to the [Features](#features) section for details of each command.

### Option 2: CLI Mode (For Advanced Users)

1. **Ensure you have Java 17 or above** installed.

2. **Download the latest `.jar` file** from [here](https://github.com/YOUR_USERNAME/ip/releases).

3. **Open a command terminal**, navigate to the folder containing the jar file, and run:
```
   java -jar cherry.jar
```

4. **Type commands directly** in the terminal and press Enter.

5. Cherry will display responses in the terminal with formatted text boxes.

---

## Features

### 📝 Notes about the command format:

- **Works identically in both GUI and CLI modes** - all commands below work the same way
- **Words in `UPPER_CASE`** are parameters to be supplied by you.
    - Example: In `todo DESCRIPTION`, `DESCRIPTION` can be used as `todo read book`

- **Items in square brackets are optional.**
    - Example: `update INDEX [/desc DESCRIPTION]` can be used as `update 1 /desc new description` or just `update 1`

- **Date format:** Dates must be in `yyyy-MM-dd` format (e.g., `2025-02-15`)

- **Task numbers** must be positive integers from 1 to 100

⚠️ **Warning:** If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines, as space characters may be omitted.

---

## Commands

All commands work identically in both GUI and CLI modes. Simply type the command and press Enter.

### Viewing help: `help`

Shows a help message explaining available commands.

**Format:** `help`

**GUI:** Type in the text field and press Enter or click Send  
**CLI:** Type in the terminal and press Enter

---

### Adding a Todo: `todo`

Adds a todo task to your task list.

**Format:** `todo DESCRIPTION`

**Examples:**
- `todo read book`
- `todo finish assignment`
- `todo buy groceries`

**Expected output:**
```
New Task: (T) [ ] read book
Now you have 1 tasks in the list.
```

**GUI:** Response appears in a light pink bubble on the left  
**CLI:** Response appears in a formatted text box

---

### Adding a Deadline: `deadline`

Adds a task with a deadline.

**Format:** `deadline DESCRIPTION /by DATE`

- `DATE` must be in `yyyy-MM-dd` format

**Examples:**
- `deadline return book /by 2025-03-15`
- `deadline submit report /by 2025-12-31`

**Expected output:**
```
New Task: (D) [ ] return book (by: Mar 15 2025)
Now you have 2 tasks in the list.
```

---

### Adding an Event: `event`

Adds an event with a start and end time.

**Format:** `event DESCRIPTION /from START /to END`

**Examples:**
- `event project meeting /from 2pm /to 4pm`
- `event team lunch /from Monday 12pm /to Monday 2pm`

**Expected output:**
```
New Task: (E) [ ] project meeting (from: 2pm to: 4pm)
Now you have 3 tasks in the list.
```

---

### Listing all tasks: `list`

Shows a list of all tasks.

**Format:** `list`

**Expected output:**
```
Here are the tasks in your list:
1. (T) [ ] read book
2. (D) [ ] return book (by: Mar 15 2025)
3. (E) [ ] project meeting (from: 2pm to: 4pm)
```

---

### Finding tasks: `find`

Finds tasks containing the specified keyword.

**Format:** `find KEYWORD`

- The search is **case-sensitive** (e.g., `book` will not match `Book`)
- Searches in task descriptions only
- Only exact substring matches are supported

**Examples:**
- `find book` returns tasks with "book" in the description
- `find meeting` returns tasks with "meeting" in the description

**Expected output:**
```
Here are the related tasks in your list:
1. (T) [ ] read book
2. (D) [ ] return book (by: Mar 15 2025)
```

---

### Marking a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark INDEX`

- `INDEX` refers to the task number shown in the task list
- `INDEX` must be a positive integer: 1, 2, 3, ...

**Examples:**
- `list` followed by `mark 2` marks the 2nd task as done

**Expected output:**
```
Good job! I've marked this task as done:
(D) [X] return book (by: Mar 15 2025)
```

---

### Unmarking a task: `unmark`

Marks the specified task as not done.

**Format:** `unmark INDEX`

**Examples:**
- `unmark 2` unmarks the 2nd task

**Expected output:**
```
Alright, I've unmarked this task:
(D) [ ] return book (by: Mar 15 2025)
```

---

### Deleting a task: `delete`

Deletes the specified task from your task list.

**Format:** `delete INDEX`

**Examples:**
- `list` followed by `delete 3` deletes the 3rd task
- `find book` followed by `delete 1` deletes the 1st task in the search results

**Expected output:**
```
Alright, I've deleted this task:
(E) [ ] project meeting (from: 2pm to: 4pm)
Now you have 2 tasks in the list.
```

---

### Exiting the program: `bye`

Exits the application.

**Format:** `bye`

**Expected output:**
```
See you next time, goodbye!
```

**GUI:** The window will remain open - you can close it manually  
**CLI:** The program will exit immediately

---

## GUI-Specific Features

### Visual Indicators

- **Your messages:** Appear on the right side with a pink background and rounded corners
- **Cherry's responses:** Appear on the left side with a light pink background

### Circular Profile Pictures

- **Your profile picture** appears on the right
- **Cherry's profile picture** appears on the left
- Both images are circular with subtle drop shadows

### Scrolling

- The chat scrolls automatically as new messages appear
- Scroll up to view older messages

### Input Field

- Type commands in the text field at the bottom
- Press **Enter** or click **Send** to execute
- The input field clears automatically after sending

---

## Data Management

### Saving the data

Your task data is **saved automatically** to the hard disk after any command that changes the data. There is no need to save manually.

**Data location:** `[JAR file location]/data/cherry.txt`

**This works the same in both GUI and CLI modes.**

---

### Editing the data file

Cherry data is saved as a text file at `./data/cherry.txt`. Advanced users are welcome to update data directly by editing that file.

**Format:** Each line represents one task:
```
(T) | [ ] | task description
(D) | [X] | task description | 2025-12-31
(E) | [ ] | task description | start time | end time
```

⚠️ **Caution:**
- If your changes make the file format invalid, Cherry will skip corrupted lines and display a warning in the console
- Ensure you follow the exact format above with the pipe `|` separator
- It is recommended to backup the file before editing

---

## FAQ

**Q: How do I transfer my data to another computer?**

A: Copy the `data/cherry.txt` file to the same location on the new computer. Works for both GUI and CLI modes.

---

**Q: Can I switch between GUI and CLI mode?**

A: Yes! Both modes read and write to the same `data/cherry.txt` file. You can use GUI one day and CLI the next without any issues.

---

**Q: Which mode should I use?**

A:
- **GUI mode** is recommended for beginners or if you prefer visual feedback
- **CLI mode** is great for advanced users who type quickly and prefer keyboard-only interaction

---

**Q: What happens if I enter an invalid date?**

A: Cherry will show an error message: "Invalid date format. Please use yyyy-MM-dd (E.g. 2025-12-31)"  
This appears in a dialog bubble (GUI) or text box (CLI).

---

**Q: Can I have tasks with the same description?**

A: Yes, Cherry allows duplicate task descriptions. Each task is identified by its position in the list.

---

**Q: Does the GUI have keyboard shortcuts?**

A: Yes! Press **Enter** in the text field to send your command without clicking the Send button.

---

## Known Issues

1. **Multi-monitor setup:** When using multiple screens, if you move Cherry to a secondary screen and later switch to only the primary screen, the GUI may open off-screen. **Remedy:** Delete the `preferences.json` file before running again.

2. **Long task descriptions:** Very long task descriptions may cause text wrapping in the GUI. **Remedy:** Keep descriptions under 100 characters for best display.

3. **Font rendering:** If the San Francisco font doesn't load properly, Cherry will fall back to Helvetica.

---

## Command Summary

| Action | Format | Example |
|--------|--------|---------|
| **Add Todo** | `todo DESCRIPTION` | `todo read book` |
| **Add Deadline** | `deadline DESCRIPTION /by DATE` | `deadline submit report /by 2025-12-31` |
| **Add Event** | `event DESCRIPTION /from START /to END` | `event meeting /from 2pm /to 4pm` |
| **List** | `list` | `list` |
| **Find** | `find KEYWORD` | `find book` |
| **Mark** | `mark INDEX` | `mark 2` |
| **Unmark** | `unmark INDEX` | `unmark 2` |
| **Delete** | `delete INDEX` | `delete 3` |
| **Exit** | `bye` | `bye` |

**All commands work identically in both GUI and CLI modes.**

---

## Tips for Fast Task Management 💡

### For GUI Users:
1. **Use the Enter key** instead of clicking Send for faster input
2. **Scroll up** to review past commands and responses
3. **Look for color coding** to quickly identify different command types

### For CLI Users:
1. **Use short, descriptive task names** for quick scanning
2. **Leverage your terminal's command history** (up arrow) to repeat commands

### For Both Modes:
1. **Use `find`** to quickly locate tasks instead of scrolling through the list
2. **Mark tasks immediately** after completion to track your progress
3. **Use consistent date formats** (yyyy-MM-dd) to avoid errors
4. **Keep your task list organized** by deleting completed tasks regularly

---

**Enjoy using Cherry! 🍒**

*Whether you prefer clicking or typing, Cherry has you covered.*