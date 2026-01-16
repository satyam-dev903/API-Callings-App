# 🚀 User API Explorer (Search + Pagination + Swipe Refresh)

An Android application built to practice real-world API consumption patterns such as pagination, debounced search, DiffUtil-based RecyclerView updates, and swipe-to-refresh. This project focuses strictly on **architecture, data flow, and performance optimization** rather than UI aesthetics.

---

## 📌 Project Overview
This project fetches a list of users from a remote API and displays them using a `RecyclerView`. It serves as a playground for:
* **Local (offline) search** with debounce logic.
* **Paginated data loading** (Page size: 5).
* **Swipe-to-refresh** for full data reloads.
* **Clean MVVM Architecture** for separation of concerns.

## 🧠 What I Learned
### 1. RecyclerView + DiffUtil (Performance)
* Implemented `DiffUtil` to calculate list differences on a background thread, preventing UI stutters.
* Handled immutable list patterns to ensure the adapter only updates when data actually changes.

### 2. Search Debounce (Optimization)
* Implemented a custom `SearchDebounce` utility using `Handler` + `Runnable`.
* Reduced unnecessary CPU/filtering load by waiting for the user to finish typing before executing search logic.

### 3. Pagination Logic
* **Strategy:** Page-based loading using `pageNumber` and `pageSize`.
* **Triggers:** Next page loads when the user reaches the bottom of the list.
* **Resets:** Pagination state resets automatically on search queries or swipe-to-refresh actions.

### 4. Swipe to Refresh
* Managed UI state transitions between `ProgressBar` (initial load) and `SwipeRefreshLayout` (manual reload).
* Ensured data consistency by clearing the local cache and resetting the page index before fetching fresh data.

## 🛠 Tech Stack
* **Language:** Kotlin
* **Architecture:** MVVM (ViewModel, Repository)
* **UI Components:** RecyclerView, DiffUtil, SwipeRefreshLayout
* **Concurrency:** Handler/Runnable (for Debounce)

## 🔍 Current Search Implementation
* **Type:** Offline (local filtering).
* **Why:** The current API does not support server-side search parameters.
* **Process:** The full dataset is maintained in the ViewModel, and filtering is applied to the cached list using a debounced input listener.

## 🔜 Future Roadmap
- [ ] Implement **Server-side Search** (Online).
- [ ] Integrate **Retrofit/Ktor** for real network calls.
- [ ] Add **Room Database** for persistent offline storage.
- [ ] Enhance UI using **Material Design 3** components.

---

## ⚙️ How to Add Your Screenshots
1.  Create a folder named `screenshots` in your project root.
2.  Add your `.png` or `.jpg` files there.
3.  In the `README.md` code above, replace `YOUR_USERNAME` and `YOUR_REPO_NAME` with your actual GitHub details.
4.  Commit and push the images to GitHub.

---

## 📄 License
Copyright © 2026. This project is open-source and available under the MIT License.
