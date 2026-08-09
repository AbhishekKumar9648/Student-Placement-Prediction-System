import tkinter as tk
from tkinter import messagebox, ttk
import pickle
import matplotlib.pyplot as plt


# Load trained model
with open("model/placement_model.pkl", "rb") as file:
    model = pickle.load(file)


# Store last computed values for chart display
last_values = {}


# Calculate package (LPA) based on placement chance percentage
def get_package(percentage):
    if percentage >= 95:
        return 12.0
    elif percentage >= 90:
        return 10.0
    elif percentage >= 85:
        return 9.0
    elif percentage >= 80:
        return 7.5
    elif percentage >= 75:
        return 6.0
    elif percentage >= 70:
        return 5.2
    elif percentage >= 65:
        return 4.5
    elif percentage >= 60:
        return 4.0
    else:
        return 0.0


# Show pie chart and bar chart
def show_charts():
    if not last_values:
        messagebox.showerror(
            "Error",
            "Please run a prediction first."
        )
        return

    factors = [
        "CGPA",
        "10th Marks",
        "12th Marks",
        "Internship",
        "Projects",
        "Communication",
        "Technical",
        "Backlogs"
    ]
    values = [
        last_values["cgpa_score"],
        last_values["marks10"],
        last_values["marks12"],
        last_values["intern_score"],
        last_values["project_score"],
        last_values["communication_score"],
        last_values["technical_score"],
        last_values["backlogs_penalty"]
    ]

    # Pie chart - factor contributions
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 5))

    # Convert to positive values for pie (backlogs shown as penalty)
    pie_values = [max(v, 0) for v in values[:7]]  # exclude backlogs from pie
    pie_labels = factors[:7]

    ax1.pie(
        pie_values,
        labels=pie_labels,
        autopct="%1.1f%%",
        startangle=90
    )
    ax1.set_title("Factor Contribution (Pie Chart)")

    # Bar chart - factor scores
    colors = ["blue", "green", "orange", "purple", "cyan", "magenta", "red"]
    bars = ax2.bar(factors[:7], pie_values, color=colors)
    ax2.set_title("Factor Scores (Bar Chart)")
    ax2.set_ylabel("Score (0-100)")
    ax2.set_ylim(0, 100)
    ax2.tick_params(axis="x", rotation=30)

    # Add value labels on bars
    for bar in bars:
        ax2.text(
            bar.get_x() + bar.get_width() / 2,
            bar.get_height() + 2,
            str(round(bar.get_height())),
            ha="center",
            va="bottom",
            fontsize=9
        )

    plt.tight_layout()
    plt.show()


# Prediction function
def predict_placement():
    try:
        cgpa = float(cgpa_entry.get())
        marks10 = float(marks10_entry.get())
        marks12 = float(marks12_entry.get())
        internship = int(internship_entry.get())
        projects = int(projects_entry.get())
        communication = float(communication_entry.get())
        technical = float(technical_entry.get())
        backlogs = int(backlogs_entry.get())

        # Basic validation
        if cgpa < 0 or cgpa > 10:
            messagebox.showerror("Error", "CGPA should be between 0 and 10")
            return

        if not (0 <= marks10 <= 100 and 0 <= marks12 <= 100):
            messagebox.showerror(
                "Error",
                "10th and 12th marks should be between 0 and 100"
            )
            return

        if internship not in [0, 1]:
            messagebox.showerror(
                "Error",
                "Internship must be 0 or 1"
            )
            return

        if projects < 0:
            messagebox.showerror(
                "Error",
                "Projects cannot be negative"
            )
            return

        if not (0 <= communication <= 10):
            messagebox.showerror(
                "Error",
                "Communication Skills should be between 0 and 10"
            )
            return

        if not (0 <= technical <= 10):
            messagebox.showerror(
                "Error",
                "Technical Skills should be between 0 and 10"
            )
            return

        if backlogs < 0:
            messagebox.showerror(
                "Error",
                "Backlogs cannot be negative"
            )
            return

# Calculate factor scores (kept for chart display)
        cgpa_score = cgpa * 10                       # 0-100
        intern_score = 100 if internship == 1 else 0
        project_score = min(projects, 5) * 20        # 0-100
        communication_score = communication * 10    # 0-100
        technical_score = technical * 10             # 0-100
        backlogs_penalty = min(backlogs, 5) * 10     # 0-50 penalty

        # Store values for charts
        last_values.update({
            "cgpa_score": cgpa_score,
            "marks10": marks10,
            "marks12": marks12,
            "intern_score": intern_score,
            "project_score": project_score,
            "communication_score": communication_score,
            "technical_score": technical_score,
            "backlogs_penalty": backlogs_penalty
        })

        # --- Placement rules (applied when Predict is clicked) ---
        # PLACED only if ALL conditions are satisfied:
        #   CGPA >= 7, 10th >= 60, 12th >= 60, Projects >= 2,
        #   Communication >= 5, Technical >= 3, Backlogs == 0
        if (cgpa >= 7 and
                marks10 >= 60 and
                marks12 >= 60 and
                projects >= 2 and
                communication >= 5 and
                technical >= 3 and
                backlogs == 0):

            # High chance for eligible students (85% - 95%)
            percentage = round(85 + (cgpa - 7) * 5 + (technical / 10) * 2)
            percentage = max(85, min(95, percentage))

            # Expected package between 4 and 8 LPA based on percentage
            package = round(4 + (percentage - 85) * 0.5, 1)

            result_value.config(text="PLACED", fg="#27ae60")
            result_sub.config(text="Congratulations! You are eligible.", fg="#27ae60")
            chance_value.config(text=f"{percentage}%", fg="#2980b9")
            progress_bar["value"] = percentage
            package_value.config(text=f"₹{package} LPA", fg="#8e44ad")
        else:
            # Low chance for students who fail any condition (10% - 40%)
            percentage = round(10 + (cgpa * 2) + (technical * 2) + (communication * 1))
            percentage = max(10, min(40, percentage))

            result_value.config(text="NOT PLACED", fg="#e74c3c")

            # Determine the reason for the failed condition
            if cgpa < 7:
                reason = "Reason: CGPA < 7"
            elif marks10 < 60:
                reason = "Reason: 10th Marks < 60%"
            elif marks12 < 60:
                reason = "Reason: 12th Marks < 60%"
            elif projects < 2:
                reason = "Reason: Projects < 2"
            elif communication < 5:
                reason = "Reason: Communication < 5"
            elif technical < 3:
                reason = "Reason: Technical Skills < 3"
            elif backlogs != 0:
                reason = "Reason: Backlogs present"
            else:
                reason = "Keep improving!"
            result_sub.config(text=reason, fg="#e67e22")

            chance_value.config(text=f"{percentage}%", fg="#e67e22")
            progress_bar["value"] = percentage
            package_value.config(text="Not Applicable", fg="#95a5a6")

    except ValueError:
        messagebox.showerror(
            "Error",
            "Please enter valid numeric values."
        )


# Clear fields function
def clear_fields():
    cgpa_entry.delete(0, tk.END)
    marks10_entry.delete(0, tk.END)
    marks12_entry.delete(0, tk.END)
    internship_entry.delete(0, tk.END)
    projects_entry.delete(0, tk.END)
    communication_entry.delete(0, tk.END)
    technical_entry.delete(0, tk.END)
    backlogs_entry.delete(0, tk.END)
    result_value.config(text="---", fg="#7f8c8d")
    result_sub.config(text="Waiting for prediction", fg="#7f8c8d")
    chance_value.config(text="-- %", fg="#7f8c8d")
    package_value.config(text="-- LPA", fg="#7f8c8d")
    progress_bar["value"] = 0
    last_values.clear()


# Create decorative education illustration on a canvas
def draw_illustration(canvas):
    c = canvas
    w = 300
    h = 400
    # Background gradient-ish box
    c.create_rectangle(0, 0, w, h, fill="#f4f6f9", outline="")
    # Graduation cap
    c.create_polygon(60, 130, 170, 80, 280, 130, 170, 180,
                     fill="#5b2c6f", outline="#5b2c6f")
    c.create_polygon(60, 130, 170, 180, 280, 130, 170, 150,
                     fill="#8e44ad", outline="#8e44ad")
    # Tassel
    c.create_line(170, 150, 170, 200, fill="#e67e22", width=3)
    c.create_oval(163, 198, 177, 212, fill="#e67e22", outline="")
    # Open book
    c.create_rectangle(90, 240, 250, 290, fill="#3498db", outline="#2471a3", width=2)
    c.create_line(170, 240, 170, 290, fill="#2471a3", width=2)
    c.create_arc(90, 235, 170, 290, start=0, extent=180, style="arc", outline="#2471a3", width=2)
    c.create_arc(170, 235, 250, 290, start=0, extent=180, style="arc", outline="#2471a3", width=2)
    # Bulb (idea)
    c.create_oval(120, 20, 220, 120, fill="#f1c40f", outline="#d4ac0d", width=2)
    c.create_rectangle(160, 120, 180, 140, fill="#f1c40f", outline="#d4ac0d")
    c.create_rectangle(150, 140, 190, 155, fill="#f1c40f", outline="#f1c40f")
    # Sparkles
    c.create_text(60, 60, text="✦", fill="#8e44ad", font=("Segoe UI", 20, "bold"))
    c.create_text(290, 40, text="✦", fill="#3498db", font=("Segoe UI", 14, "bold"))
    c.create_text(30, 190, text="✦", fill="#e67e22", font=("Segoe UI", 14, "bold"))
    c.create_text(285, 200, text="✦", fill="#27ae60", font=("Segoe UI", 16, "bold"))
    # Caption (kept fully visible within the canvas)
    c.create_text(150, 330, text="Smart Placement", fill="#2c3e50",
                  font=("Segoe UI", 17, "bold"))
    c.create_text(150, 355, text="Prediction Model", fill="#8e44ad",
                  font=("Segoe UI", 13, "bold"))


# =================== GUI ===================
root = tk.Tk()
root.title("Student Placement Prediction System")
root.resizable(True, True)
root.configure(bg="#eef1f6")

# Colors
PRIMARY = "#5b2c6f"
CARD_BG = "#ffffff"
FIELD_BG = "#f4f6f9"
BORDER = "#d5dbe3"
TEXT_DARK = "#2c3e50"
TEXT_LIGHT = "#7f8c8d"


# Center the window on screen
def center_window(win, w, h):
    win.update_idletasks()
    screen_w = win.winfo_screenwidth()
    screen_h = win.winfo_screenheight()
    x = (screen_w - w) // 2
    y = (screen_h - h) // 2
    win.geometry(f"{w}x{h}+{x}+{y}")


# Toggle fullscreen with Escape key
def toggle_fullscreen(event=None):
    root.attributes("-fullscreen", not root.attributes("-fullscreen"))


# Exit application
def exit_app():
    root.destroy()


root.bind("<Escape>", toggle_fullscreen)

# Root grid: header (row 0), body (row 1, expands), results (row 2, pinned)
root.grid_rowconfigure(1, weight=1)
root.grid_columnconfigure(0, weight=1)

# ===== Header =====
header = tk.Frame(root, bg=PRIMARY, height=100)
header.grid(row=0, column=0, sticky="ew")
header.grid_propagate(False)

# Decorative gradient-like strips
tk.Frame(header, bg="#8e2de2", height=6).place(x=0, y=0, relwidth=0.5)
tk.Frame(header, bg="#4a00e0", height=6).place(x=0, y=0, relwidth=1)

title_label = tk.Label(
    header,
    text="Student Placement Prediction System",
    font=("Segoe UI", 24, "bold"),
    bg=PRIMARY,
    fg="#ffffff"
)
title_label.pack(pady=(14, 0))

sub_label = tk.Label(
    header,
    text="Predict your placement chances based on your profile",
    font=("Segoe UI", 12),
    bg=PRIMARY,
    fg="#e8d5ff"
)
sub_label.pack(pady=(0, 0))

# ===== Body (compact, grid-based) =====
body = tk.Frame(root, bg="#eef1f6")
body.grid(row=1, column=0, sticky="nsew", padx=20, pady=12)
body.grid_columnconfigure(0, weight=1)   # input card
body.grid_columnconfigure(1, weight=0)   # buttons
body.grid_columnconfigure(2, weight=0)   # illustration
body.grid_rowconfigure(0, weight=1)

# ===== Input Card (left) =====
card = tk.LabelFrame(
    body,
    text=" Student Input Details ",
    font=("Segoe UI", 14, "bold"),
    fg=PRIMARY,
    bg=CARD_BG,
    relief="solid",
    bd=1,
    highlightbackground=BORDER,
    highlightthickness=1,
    padx=20,
    pady=12
)
card.grid(row=0, column=0, sticky="nsew", padx=(0, 15))

# Two-column grid inside the card for the 8 fields
card.grid_columnconfigure(0, weight=1)
card.grid_columnconfigure(2, weight=1)

LABEL_FONT = ("Segoe UI", 11)
ENTRY_FONT = ("Segoe UI", 12)


def make_field(parent, row, col, text, ref_list):
    lbl = tk.Label(
        parent,
        text=text,
        font=LABEL_FONT,
        bg=CARD_BG,
        fg=TEXT_DARK,
        anchor="w"
    )
    lbl.grid(row=row, column=col, sticky="w", padx=(5, 5), pady=(6, 1))
    ent = tk.Entry(
        parent,
        font=ENTRY_FONT,
        width=20,
        bg=FIELD_BG,
        fg=TEXT_DARK,
        relief="solid",
        bd=1,
        highlightthickness=1,
        highlightbackground=BORDER,
        justify="center"
    )
    ent.grid(row=row + 1, column=col, sticky="ew", padx=(5, 5), pady=(0, 6), ipady=5)
    ref_list.append(ent)
    return ent


entries = []

# Column 0 (left)
cgpa_entry = make_field(card, 0, 0, "CGPA", entries)
marks10_entry = make_field(card, 2, 0, "10th Marks (%)", entries)
marks12_entry = make_field(card, 4, 0, "12th Marks (%)", entries)
internship_entry = make_field(card, 6, 0, "Internship (1 = Yes, 0 = No)", entries)

# Column 2 (right)
projects_entry = make_field(card, 0, 2, "Number of Projects", entries)
communication_entry = make_field(card, 2, 2, "Communication (0-10)", entries)
technical_entry = make_field(card, 4, 2, "Technical Skills (0-10)", entries)
backlogs_entry = make_field(card, 6, 2, "Backlogs", entries)


# ===== Buttons (middle) =====
btn_frame = tk.Frame(body, bg="#eef1f6")
btn_frame.grid(row=0, column=1, sticky="ns", padx=10)


def make_button(parent, text, command, color):
    return tk.Button(
        parent,
        text=text,
        command=command,
        font=("Segoe UI", 12, "bold"),
        bg=color,
        fg="#ffffff",
        activebackground=color,
        activeforeground="#ffffff",
        width=15,
        pady=8,
        relief="flat",
        bd=0,
        cursor="hand2"
    )


# Center buttons vertically
btn_frame.grid_rowconfigure(0, weight=1)
btn_frame.grid_rowconfigure(6, weight=1)

predict_button = make_button(btn_frame, "Predict Placement", predict_placement, "#8e44ad")
predict_button.grid(row=1, column=0, pady=6)

clear_button = make_button(btn_frame, "Clear", clear_fields, "#e67e22")
clear_button.grid(row=2, column=0, pady=6)

charts_button = make_button(btn_frame, "Show Charts", show_charts, "#2980b9")
charts_button.grid(row=3, column=0, pady=6)

exit_button = make_button(btn_frame, "Exit", exit_app, "#e74c3c")
exit_button.grid(row=4, column=0, pady=6)


# ===== Right column: illustration =====
right_col = tk.Frame(body, bg="#eef1f6")
right_col.grid(row=0, column=2, sticky="ns", padx=(15, 0))
right_col.grid_rowconfigure(0, weight=1)

illustration_canvas = tk.Canvas(
    right_col,
    width=300,
    height=400,
    bg="#f4f6f9",
    highlightthickness=1,
    highlightbackground=BORDER,
    bd=0
)
illustration_canvas.grid(row=1, column=0)
draw_illustration(illustration_canvas)


# ===== Results Card (pinned at bottom, always visible) =====
result_card = tk.LabelFrame(
    root,
    text=" Prediction Results ",
    font=("Segoe UI", 14, "bold"),
    fg=PRIMARY,
    bg=CARD_BG,
    relief="solid",
    bd=1,
    highlightbackground=BORDER,
    highlightthickness=1,
    padx=15,
    pady=10
)
result_card.grid(row=2, column=0, sticky="ew", padx=20, pady=(0, 15))

# Three result sections side by side
result_card.grid_columnconfigure(0, weight=1)
result_card.grid_columnconfigure(1, weight=1)
result_card.grid_columnconfigure(2, weight=1)


def make_result_section(parent, col, title, initial):
    section = tk.Frame(parent, bg=CARD_BG)
    section.grid(row=0, column=col, padx=15, pady=3, sticky="nsew")
    ttl = tk.Label(section, text=title, font=("Segoe UI", 12, "bold"),
                   bg=CARD_BG, fg=TEXT_LIGHT)
    ttl.pack()
    val = tk.Label(section, text=initial, font=("Segoe UI", 26, "bold"),
                   bg=CARD_BG, fg="#7f8c8d")
    val.pack(pady=2)
    return val


# Column 0: Prediction
result_value = make_result_section(result_card, 0, "Prediction", "---")
result_sub = tk.Label(result_card, text="Waiting for prediction",
                      font=("Segoe UI", 10), bg=CARD_BG, fg=TEXT_LIGHT)
result_sub.grid(row=1, column=0, padx=15, pady=(0, 3))

# Column 1: Placement Chance
chance_value = make_result_section(result_card, 1, "Placement Chance", "-- %")

# Progress bar under chance
progress_bar = ttk.Progressbar(
    result_card,
    orient="horizontal",
    length=200,
    mode="determinate",
    maximum=100
)
progress_bar.grid(row=1, column=1, padx=15, pady=(3, 3))

# Column 2: Expected Package
package_value = make_result_section(result_card, 2, "Expected Package", "-- LPA")


# Set window size and center it (after building UI so it fits content)
center_window(root, 1150, 700)
root.minsize(1100, 700)


# Start application
root.mainloop()
