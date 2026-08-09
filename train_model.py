import pandas as pd
import os
import pickle

from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score


# ==============================
# 1. Load Dataset
# ==============================

data = pd.read_csv("placement_data.csv")

print("Dataset Loaded Successfully!")
print(data.head())


# ==============================
# 2. Select Features
# ==============================

X = data[
    [
        "CGPA",
        "10th_Marks",
        "12th_Marks",
        "Internship",
        "Projects",
        "Communication_Skills",
        "Technical_Skills",
        "Backlogs"
    ]
]


# ==============================
# 3. Target Variable
# ==============================

y = data["Placement"]


# ==============================
# 4. Split Dataset
# ==============================

X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y
)


# ==============================
# 5. Create ML Model
# ==============================

model = RandomForestClassifier(
    n_estimators=100,
    random_state=42
)


# ==============================
# 6. Train Model
# ==============================

model.fit(X_train, y_train)

print("Model Training Completed!")


# ==============================
# 7. Check Accuracy
# ==============================

y_pred = model.predict(X_test)

accuracy = accuracy_score(y_test, y_pred)

print("Model Accuracy:", accuracy)


# ==============================
# 8. Create Model Folder
# ==============================

os.makedirs("model", exist_ok=True)


# ==============================
# 9. Save Trained Model
# ==============================

with open("model/placement_model.pkl", "wb") as file:
    pickle.dump(model, file)


print("Model saved successfully!")
print("File: model/placement_model.pkl")