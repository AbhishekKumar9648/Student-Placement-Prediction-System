import streamlit as st

# Page configuration
st.set_page_config(
    page_title="Student Placement Prediction System",
    page_icon="🎓",
    layout="wide"
)

# Title
st.title("🎓 Student Placement Prediction System")
st.write("Predict your placement chances based on your profile.")

st.divider()

# Input section
st.subheader("📋 Student Input Details")

col1, col2 = st.columns(2)

with col1:
    cgpa = st.number_input(
        "CGPA",
        min_value=0.0,
        max_value=10.0,
        value=7.0,
        step=0.1
    )

    marks_10 = st.number_input(
        "10th Marks (%)",
        min_value=0.0,
        max_value=100.0,
        value=70.0,
        step=1.0
    )

    marks_12 = st.number_input(
        "12th Marks (%)",
        min_value=0.0,
        max_value=100.0,
        value=70.0,
        step=1.0
    )

    internship = st.selectbox(
        "Internship",
        ["Yes", "No"]
    )

with col2:
    projects = st.number_input(
        "Number of Projects",
        min_value=0,
        max_value=20,
        value=2,
        step=1
    )

    communication = st.slider(
        "Communication (0-10)",
        min_value=0,
        max_value=10,
        value=7
    )

    technical = st.slider(
        "Technical Skills (0-10)",
        min_value=0,
        max_value=10,
        value=7
    )

    backlogs = st.number_input(
        "Backlogs",
        min_value=0,
        max_value=20,
        value=0,
        step=1
    )

# Prediction function
def calculate_score():
    score = 0

    # CGPA
    if cgpa >= 8.5:
        score += 20
    elif cgpa >= 7.5:
        score += 17
    elif cgpa >= 6.5:
        score += 14
    elif cgpa >= 5.5:
        score += 10
    else:
        score += 5

    # 10th marks
    if marks_10 >= 80:
        score += 10
    elif marks_10 >= 70:
        score += 8
    elif marks_10 >= 60:
        score += 6
    else:
        score += 3

    # 12th marks
    if marks_12 >= 80:
        score += 10
    elif marks_12 >= 70:
        score += 8
    elif marks_12 >= 60:
        score += 6
    else:
        score += 3

    # Internship
    if internship == "Yes":
        score += 10

    # Projects
    score += min(projects * 4, 12)

    # Communication
    score += communication

    # Technical skills
    score += technical

    # Backlogs penalty
    score -= min(backlogs * 5, 15)

    # Keep score between 0 and 100
    score = max(0, min(score, 100))

    return score


def get_package(percentage):
    if percentage >= 80:
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


# Predict button
if st.button("🔮 Predict Placement", use_container_width=True):

    percentage = calculate_score()
    package = get_package(percentage)

    if percentage >= 70:
        prediction = "Likely to be Placed"
    elif percentage >= 50:
        prediction = "Moderate Placement Chance"
    else:
        prediction = "Low Placement Chance"

    st.divider()

    st.subheader("📊 Prediction Results")

    col1, col2, col3 = st.columns(3)

    with col1:
        st.metric("Prediction", prediction)

    with col2:
        st.metric("Placement Chance", f"{percentage:.1f}%")

    with col3:
        if package > 0:
            st.metric("Expected Package", f"{package:.1f} LPA")
        else:
            st.metric("Expected Package", "Not Estimated")

    if percentage >= 70:
        st.success("🎉 Good placement chances based on the entered profile.")
    elif percentage >= 50:
        st.warning("⚠️ Moderate placement chances. Improve your skills and profile.")
    else:
        st.error("📚 Placement chances are currently low. Focus on improving your profile.")


# Footer
st.divider()
st.caption("Student Placement Prediction System | College Project")