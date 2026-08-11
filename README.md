# Student Placement Prediction System

A Machine Learning project that predicts a student's placement chances based on their profile (CGPA, marks, skills, projects, etc.) using a Random Forest Classifier.

---

## 🚀 How to Run (Web App)

The web version is **`streamlit_app.py`**. Run it with:

```bash
streamlit run streamlit_app.py
```

Or:

```bash
python -m streamlit run streamlit_app.py
```

Then open your browser at: **http://localhost:8501**

> ⚠️ **Note:** `app.py` is a **desktop (tkinter) GUI** application. It opens as a desktop window and **cannot run in the web browser**. Use `streamlit_app.py` for the web version.

---

## 📁 Project Structure

| File | Description |
|------|-------------|
| `streamlit_app.py` | Web app (Streamlit) — uses the trained ML model |
| `app.py` | Desktop GUI (tkinter) version |
| `train_model.py` | Script to train and save the ML model |
| `placement_data.csv` | Training dataset |
| `model/placement_model.pkl` | Trained Random Forest model |

---

## 🧠 Model

- **Algorithm:** Random Forest Classifier
- **Features:** CGPA, 10th Marks, 12th Marks, Internship, Projects, Communication Skills, Technical Skills, Backlogs
- **Target:** Placement (1 = Placed, 0 = Not Placed)

To retrain the model:

```bash
python train_model.py
```

---

## 📦 Requirements

- Python 3.8+
- streamlit
- pandas
- numpy
- scikit-learn
- matplotlib

Install with:

```bash
pip install streamlit pandas numpy scikit-learn matplotlib
```

---

Student Placement Prediction System | College Project 🎓
