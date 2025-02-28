Football Results Tracking App
Overview
This Android application helps track and manage football (soccer) match results. Users can add, edit, and delete match data, view team statistics, and search for matches by team. The app automatically calculates and maintains team statistics based on match results.
Features

Match Management: Add, edit, and delete football match results
Team Statistics: Automatically calculated statistics for each team
Sorting: Sort team statistics by points
Search: Find matches by team
Reports: Generate match and team statistics reports

Technical Details
Architecture
The app follows a simple architecture with separate components for:

Activities (UI screens)
Adapters (connecting data to UI elements)
Database management (SQLite)
Models (data classes)
Utilities (helper classes)

Database Structure
The app uses SQLite with two main tables:

Matches Table: Stores match details including date, city, teams, and scores
Team Statistics Table: Stores calculated team statistics including points, wins, draws, losses, etc.

Key Components

MainActivity: The entry point of the application that provides navigation to other screens
MatchEntryActivity: Screen for adding or editing match data
TeamStatsActivity: Shows team statistics table with sorting capability
SearchActivity: Allows searching for matches by team
ReportActivity: Generates reports for viewing

Getting Started
Prerequisites

Android Studio
Android SDK (minimum API level 21)

Installation

Clone this repository
Open the project in Android Studio
Build and run the application on an emulator or physical device

Usage

Adding a Match: Click "Add Match" on the main screen and fill in the match details
Viewing Matches: Click "View Matches" to see all recorded matches
Team Statistics: Click "Team Statistics" to view automatically calculated statistics
Searching: Click "Search Matches" to find matches by team

Screenshots
[Screenshots would be included here]
Project Structure

com.example.footballresults/
├── activities/
│   ├── MainActivity.java
│   ├── MatchEntryActivity.java
│   ├── TeamStatsActivity.java
│   ├── SearchActivity.java
│   └── ReportActivity.java
├── adapters/
│   ├── MatchAdapter.java
│   └── TeamStatsAdapter.java
├── database/
│   ├── DatabaseHelper.java
│   ├── DatabaseSeeder.java
│   ├── MatchDao.java
│   └── TeamStatsDao.java
├── models/
│   ├── Match.java
│   └── TeamStats.java
└── utils/
    ├── DateFormatter.java
    └── StatisticsCalculator.java
Implementation Details

The app supports right-to-left (RTL) layout for Hebrew language
Pre-populated sample data is included to demonstrate functionality
Match statistics are automatically recalculated when match data changes
Team data is automatically removed when all matches for a team are deleted

Contributors

Tomer Harari , Yuval Boker - Initial work

License
This project is licensed under the MIT License - see the LICENSE file for details
Acknowledgments

Project developed as part of [Course/Android]
Dr. Eyad Suliman - Course Instructor