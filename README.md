# LearnCards App

## Overview
LearnCards is an Android application designed to help users create, manage, and practice flashcards to facilitate learning. The app allows users to:
- Create and manage projects for organizing flashcards.
- Add, review, and track flashcards with correctness and difficulty levels.
- Use drawing functionality to enhance visual learning.
- Monitor progress and improve learning efficiency.

## Features
- **Project Management**: Users can create, edit, delete, and manage flashcard projects.
- **Flashcard Management**: View, edit, mark correctness, and delete flashcards.
- **Drawing and Customization**: Users can draw on flashcards for visual learning, save their drawings, and track drawing progress.
- **Progress Tracking**: Monitor correctness and difficulty levels of flashcards to improve learning outcomes.

## Classes & Functionality

### MainActivity
- **Purpose**: Entry point of the application that manages the project selection, creation, and navigation.
- **Key Features**:
  - Displays the list of available projects.
  - Navigates to other activities like `ManageCardsActivity` and `LearnCardsActivity`.
  - Handles project-related operations such as selecting, creating, deleting, and renaming projects.

#### Methods
- **`onCreate`**: Initializes UI components and sets up button listeners.
- **`initializeViews`**: Binds views like buttons, ListView, etc.
- **`loadProjectList`**: Fetches and displays the list of projects from internal storage.

---

### ManageActivity
- **Purpose**: Manages and displays flashcards for a selected project.
- **Key Features**:
  - Displays the list of flashcards.
  - Provides navigation to `LearnCardsActivity` and `AddCardActivity`.
  - Shows loading screen during data fetching.

#### Methods
- **`onCreate`**: Initializes `FlashcardManager`, fetches flashcard data, and displays the loading screen.
- **`navigateToActivity`**: Redirects to other activities like `LearnCardsActivity` or `AddCardActivity`.
- **`showLoadingScreen`**: Displays a loading screen while data is being fetched.

---

### AddCardActivity
- **Purpose**: Allows users to add new flashcards to a project.
- **Key Features**:
  - Provides UI to input flashcard sides.
  - Saves new flashcards using `FlashcardManager`.
  
#### Methods
- **`onCreate`**: Initializes views and `FlashcardManager`.
- **`handleAddCardButton`**: Adds flashcards by capturing user input.
- **`clearInputFields`**: Clears input fields after a flashcard is added.

---

### LearnCardsActivity
- **Purpose**: Facilitates flashcard review, learning, and drawing functionality.
- **Key Features**:
  - Displays flashcards for review and supports flipping between front and back.
  - Allows marking flashcards as correct or incorrect.
  - Supports drawing on flashcards, saving drawings, and displaying previously saved drawings.
  - Manages progress tracking and filters flashcards based on correctness.

#### Methods
- **`onCreate`**: Initializes views, loads flashcards, and sets up UI listeners.
- **`showNextCard`**: Displays the next flashcard for review.
- **`flipCard`**: Toggles between front and back of the flashcard.
- **`saveDrawing`**: Captures and saves user drawings on flashcards.
- **`togglePaintView`**: Toggles visibility of the drawing canvas.

---

### ManageCardsActivity
- **Purpose**: Handles the management of flashcards in a project.
- **Key Features**:
  - Displays flashcards and provides options to reset or delete flashcards.
  - Refreshes the flashcard list after performing operations.

#### Methods
- **`onCreate`**: Initializes `FlashcardManager` and loads flashcards.
- **`showCardOptions`**: Displays a dialog to reset or delete flashcards.
- **`refreshCardList`**: Reloads flashcards and updates the list.

---

### FlashcardManager
- **Purpose**: Manages flashcard data, including saving, retrieving, and tracking flashcard correctness.
- **Key Methods**:
  - **`retrieveCards`**: Fetches all flashcards for a project.
  - **`markCorrect`**: Marks a flashcard as correctly answered.
  - **`markIncorrect`**: Marks a flashcard as incorrectly answered.
  - **`resetCard`**: Resets the correct count for a flashcard.
  - **`deleteCard`**: Deletes a flashcard from the project.

---

## UI Components & Layouts
- **`activity_main.xml`**: Main activity layout.
- **`activity_manage.xml`**: Layout for managing flashcards.
- **`activity_add_card.xml`**: Layout for adding flashcards.
- **`activity_learn_cards.xml`**: Layout for flashcard learning and drawing.

---

## Shared Preferences
- **LearnCardsPreferences**: Used to store user preferences like component visibility and drawing mode.

---

## Known Issues
- Flashcard drawing feature may cause performance issues on older devices due to high memory usage.
- No feedback mechanism is currently implemented.

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/LearnCards.git
