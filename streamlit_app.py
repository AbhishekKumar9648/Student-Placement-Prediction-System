import streamlit as st
import pickle
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# ---------- Page configuration ----------
st.set_page_config(
    page_title="Student Placement Prediction System",
    page_icon="🎓",
    layout="wide",
    initial_sidebar_state="expanded"
)


# ---------- Load trained model ----------
@st.cache_resource
def load_model():
    with open("model/placement_model.pkl", "rb") as f:
        return pickle.load(f)


model = load_model()

MODEL_FEATURES = [
    "CGPA",
    "10th_Marks",
    "12th_Marks",
    "Internship",
    "Projects",
    "Communication_Skills",
    "Technical_Skills",
    "Backlogs",
]

# ---------- Header ----------
st.markdown(
    """
    <style>
        .main-header {
            background: linear-gradient(90deg, #5b2c6f, #8e44ad);
            padding: 28px 24px;
            border-radius: 14px;
            color: white;
            text-align: center;
            margin-bottom: 10px;
        }
        .main-header h1 { margin: 0; font-size: 34px; }
        .main-header p { margin: 6px 0 0 0; font-size: 16px; opacity: 0.9; }
    </style>
    <div class="main-header">
        <h1>🎓 Student Placement Prediction System</h1>
        <p>Predict your placement chances based on your profile using Machine Learning</p>
    </div>
    """,
    unsafe_allow_html=True,
)

st.divider()

# ---------- Input section ----------
st.subheader("📋 Student Input Details")

col1, col2 = st.columns(2)

with col1:
    cgpa = st.number_input(
        "CGPA (0-10)",
        min_value=0.0,
        max_value=10.0,
        value=7.5,
        step=0.1,
        help="Your cumulative grade point average",
    )

    marks_10 = st.number_input(
        "10th Marks (%)",
        min_value=0.0,
        max_value=100.0,
        value=80.0,
        step=1.0,
    )

    marks_12 = st.number_input(
        "12th Marks (%)",
        min_value=0.0,
        max_value=100.0,
        value=78.0,
        step=1.0,
    )

    internship = st.selectbox(
        "Internship",
        ["Yes", "No"],
        help="Have you completed an internship?",
    )

with col2:
    projects = st.number_input(
        "Number of Projects",
        min_value=0,
        max_value=20,
        value=3,
        step=1,
    )

    communication = st.slider(
        "Communication Skills (0-10)",
        min_value=0,
        max_value=10,
        value=7,
    )

    technical = st.slider(
        "Technical Skills (0-10)",
        min_value=0,
        max_value=10,
        value=7,
    )

    backlogs = st.number_input(
        "Backlogs",
        min_value=0,
        max_value=20,
        value=0,
        step=1,
    )

# ---------- Predict button ----------
col_btn = st.columns([1, 2, 1])
with col_btn[1]:
    predict_clicked = st.button("🔮 Predict Placement", use_container_width=True)

if predict_clicked:
    # Build feature vector in the exact order the model expects
    features = [
        cgpa,
        marks_10,
        marks_12,
        1 if internship == "Yes" else 0,
        projects,
        communication,
        technical,
        backlogs,
    ]

    input_df = pd.DataFrame([features], columns=MODEL_FEATURES)

    # Model prediction (1 = Placed, 0 = Not Placed)
    prediction = model.predict(input_df)[0]
    probability = model.predict_proba(input_df)[0]

    # Probability of being placed (class 1)
    placed_prob = probability[1] * 100
    not_placed_prob = probability[0] * 100

    st.divider()
    st.subheader("📊 Prediction Results")

    if prediction == 1:
        result_text = "PLACED"
        result_color = "#27ae60"
        result_msg = "🎉 Congratulations! The model predicts you are likely to be placed."
    else:
        result_text = "NOT PLACED"
        result_color = "#e74c3c"
        result_msg = "📚 The model predicts you may not be placed. Focus on improving your profile."

    # ---- Result cards ----
    c1, c2, c3 = st.columns(3)

    with c1:
        st.markdown("**Prediction**")
        st.markdown(
            f"<h2 style='color:{result_color}; margin:0;'>{result_text}</h2>",
            unsafe_allow_html=True,
        )

    with c2:
        st.markdown("**Placement Chance**")
        st.markdown(
            f"<h2 style='color:#2980b9; margin:0;'>{placed_prob:.1f}%</h2>",
            unsafe_allow_html=True,
        )
        st.progress(int(placed_prob))

    with c3:
        st.markdown("**Expected Package**")

        # Estimate package based on placement probability
        if placed_prob >= 95:
            pkg = 12.0
        elif placed_prob >= 90:
            pkg = 10.0
        elif placed_prob >= 85:
            pkg = 9.0
        elif placed_prob >= 80:
            pkg = 7.5
        elif placed_prob >= 75:
            pkg = 6.0
        elif placed_prob >= 70:
            pkg = 5.2
        elif placed_prob >= 65:
            pkg = 4.5
        elif placed_prob >= 60:
            pkg = 4.0
        else:
            pkg = 0.0

        if pkg > 0:
            st.markdown(
                f"<h2 style='color:#8e44ad; margin:0;'>₹{pkg} LPA</h2>",
                unsafe_allow_html=True,
            )
        else:
            st.markdown(
                f"<h2 style='color:#95a5a6; margin:0;'>Not Estimated</h2>",
                unsafe_allow_html=True,
            )

    st.info(result_msg)

    # ---------- Charts ----------
    st.divider()
    st.subheader("📈 Factor Contribution Analysis")

    # Factor scores (0-100 scale) for visualization
    cgpa_score = cgpa * 10
    intern_score = 100 if internship == "Yes" else 0
    project_score = min(projects, 5) * 20
    communication_score = communication * 10
    technical_score = technical * 10
    backlogs_penalty = min(backlogs, 5) * 10

    factors = [
        "CGPA",
        "10th Marks",
        "12th Marks",
        "Internship",
        "Projects",
        "Communication",
        "Technical",
        "Backlogs",
    ]
    values = [
        cgpa_score,
        marks_10,
        marks_12,
        intern_score,
        project_score,
        communication_score,
        technical_score,
        backlogs_penalty,
    ]

    chart_col1, chart_col2 = st.columns(2)

    # Pie chart
    with chart_col1:
        st.markdown("**Factor Contribution (Pie Chart)**")
        pie_values = [max(v, 0) for v in values[:7]]
        pie_labels = factors[:7]
        fig1, ax1 = plt.subplots(figsize=(6, 5))
        ax1.pie(
            pie_values,
            labels=pie_labels,
            autopct="%1.1f%%",
            startangle=90,
            colors=["#5b2c6f", "#3498db", "#27ae60", "#e67e22",
                    "#8e44ad", "#2980b9", "#f1c40f"],
        )
        ax1.set_title("Factor Contribution (Pie Chart)")
        st.pyplot(fig1)

    # Bar chart
    with chart_col2:
        st.markdown("**Factor Scores (Bar Chart)**")
        colors = ["#5b2c6f", "#3498db", "#27ae60", "#e67e22",
                  "#8e44ad", "#2980b9", "#f1c40f", "#e74c3c"]
        fig2, ax2 = plt.subplots(figsize=(6, 5))
        bars = ax2.bar(factors, values, color=colors)
        ax2.set_title("Factor Scores (Bar Chart)")
        ax2.set_ylabel("Score (0-100)")
        ax2.set_ylim(0, 100)
        ax2.tick_params(axis="x", rotation=30)
        for bar in bars:
            ax2.text(
                bar.get_x() + bar.get_width() / 2,
                bar.get_height() + 2,
                str(round(bar.get_height())),
                ha="center",
                va="bottom",
                fontsize=9,
            )
        fig2.tight_layout()
        st.pyplot(fig2)

    # ---------- Model confidence ----------
    st.divider()
    st.subheader("🔍 Model Confidence")
    conf_c1, conf_c2 = st.columns(2)
    with conf_c1:
        st.metric("Placed Probability", f"{placed_prob:.1f}%")
    with conf_c2:
        st.metric("Not Placed Probability", f"{not_placed_prob:.1f}%")

else:
    st.info("👆 Fill in your details and click **Predict Placement** to see the result.")

# ---------- Footer ----------
st.divider()
st.caption("🎓 Student Placement Prediction System | Machine Learning Project (Random Forest Classifier)")
