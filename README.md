# Placement Predictor (Android)

A modern Android application built with **Kotlin** and **Jetpack Compose** that predicts student placement chances, estimated salary packages (LPA), and factor score contributions based on academic and technical profiles.

---

## ✨ Features Ported & Enhanced

- **Interactive Profile Evaluator**: Real-time evaluation of the 8 core student metrics:
  - CGPA (0.0 – 10.0)
  - 10th Standard Marks (%)
  - 12th Standard Marks (%)
  - Internship Completion Status (Yes/No)
  - Number of Completed Projects (0 – 20)
  - Communication Skills (0 – 10)
  - Technical Skills & Problem Solving (0 – 10)
  - Active Backlogs Count
- **Machine Learning & Rule Engine**:
  - Predicts placement eligibility (`PLACED` vs. `NOT PLACED`)
  - Placement Probability Percentage (%) and Confidence
  - Expected Package (LPA) Estimation
  - Root-cause diagnostics and personalized improvement recommendations
- **Factor Contribution & Visualizations**:
  - Interactive Canvas **Donut / Pie Chart** for proportional factor weights
  - **Factor Scores Bar Chart** (0 – 100 normalized score breakdown)
  - Backlog penalty indicators
  - Salary package tier brackets
- **What-If Career Simulator**:
  - Live sensitivity testing (e.g. effect of clearing backlogs, raising CGPA, boosting technical skills)
- **Dataset Explorer**:
  - Interactive browser for the historical campus recruitment dataset (29 records)
  - One-tap profile loading into the predictor
- **Quick Preset Profiles**:
  - High Achiever, Average Good, Borderline Profile, and Needs Focus presets

---

## 🛠️ Architecture & Tech Stack

- **Framework**: Jetpack Compose (Material Design 3)
- **Language**: Kotlin 2.1.0 (JVM 21)
- **Architecture**: MVVM (Model-View-ViewModel) with Kotlin StateFlow
- **Graphics**: Custom hardware-accelerated Compose Canvas charts
- **Build System**: Gradle 9.3.1 with Kotlin DSL and Version Catalog (`gradle/libs.versions.toml`)
- **Android Target**: SDK 36 (Min SDK 26)
