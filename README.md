# ProjectPilot

ProjectPilot is an Android application designed to help users manage academic group projects in a clean, structured, and mobile-friendly way.  
It provides a lightweight workspace for tracking project tasks, meeting notes, submission readiness, and project-level information such as course and deadline.

Unlike a simple single-workspace task app, ProjectPilot supports **multiple independent projects** inside the same application.  
Each project has its own:

- tasks
- meetings
- checklist progress
- dashboard summary

This makes the app suitable for students or teams who are handling more than one coursework project at the same time.

---

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [Screens and Workflow](#screens-and-workflow)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [Database Design](#database-design)
- [Current Project Mechanism](#current-project-mechanism)
- [UI and Design Notes](#ui-and-design-notes)
- [How to Run](#how-to-run)
- [Git Workflow](#git-workflow)
- [Current Functional Scope](#current-functional-scope)
- [Future Improvements](#future-improvements)
- [Repository](#repository)
- [Author](#author)
- [License](#license)

---

## Overview

ProjectPilot is built as a local Android project management tool for group coursework and team collaboration scenarios.

The app is centered around a **current active project**.  
At any time, the user is working inside one selected project workspace.  
All major modules read and write data based on that active project.

This means:

- the Home dashboard shows information for the current project
- the Tasks page only shows tasks for the current project
- the Meetings page only shows meetings for the current project
- the Checklist page only shows checklist progress for the current project

The app also includes a custom project switcher dialog so users can quickly switch between different project workspaces.

---

## Core Features

### 1. Multi-project support

ProjectPilot supports multiple projects inside the same app.

Each project stores its own:

- project name
- course
- deadline
- tasks
- meeting records
- checklist state

This solves the common problem of all records being mixed together globally.

---

### 2. Home dashboard

The home screen serves as the central overview page for the active project.

It displays:

- project name
- course/workspace label
- deadline
- task count
- meeting count
- task activity summary
- meeting summary
- checklist completion summary
- recent task information
- recent meeting information

The goal is to let the user understand project status at a glance.

---

### 3. Task management

The app supports project-specific task management.

Users can:

- add a task
- edit a task
- update assignee
- update due date
- update task status

Supported task statuses:

- `To Do`
- `Doing`
- `Done`

Tasks are stored in Room and linked to a project through `projectId`.

---

### 4. Meeting management

The app supports project-specific meeting note management.

Users can:

- add a meeting note
- edit a meeting note
- store meeting title
- store meeting date
- store notes / summary text

Meetings are also linked to a specific project through `projectId`.

---

### 5. Submission checklist

Each project has an independent checklist state.

Checklist items include:

- Report completed
- Source code ready
- README ready
- Slides ready
- Demo ready
- Video ready

Checklist progress is shown:

- on the Checklist page
- on the Home dashboard

---

### 6. Project switcher

The Home page includes a custom project switcher dialog.

Users can:

- see all existing projects
- identify the current project
- switch to another project
- create a new project
- edit the current project

This makes the multi-project architecture visible and practical in the user interface.

---

### 7. Create / edit project form

Project creation and editing use a unified custom form dialog.

Users can manage:

- project name
- course
- deadline

The deadline uses a date picker, so the interaction style matches the date selection used in tasks and meetings.

---

### 8. Refined custom UI

The app has been gradually refined beyond default Android system dialogs.

Custom UI elements include:

- project switcher dialog
- project form dialog
- custom project cards
- current project badge
- custom button backgrounds
- custom input backgrounds
- polished dashboard typography and spacing

The visual direction aims for a minimal, modern, workspace-oriented mobile interface.

---

## Screens and Workflow

### Home

The Home page is the dashboard for the active project.

It provides:

- project-level summary
- quick overview of progress
- recent task / meeting information
- access to the project switcher

This is the main entry point for users.

---

### Tasks

The Tasks page shows only tasks belonging to the active project.

Main actions:

- view tasks
- add task
- edit task

When the active project changes, the task list updates accordingly.

---

### Meetings

The Meetings page shows only meetings belonging to the active project.

Main actions:

- view meetings
- add meeting
- edit meeting

Meeting data is isolated by project.

---

### Checklist

The Checklist page reads and writes checklist state for the active project only.

Main actions:

- mark deliverables as ready
- update completion progress
- sync checklist summary with Home dashboard

---

### Project switching flow

A typical project switching flow is:

1. Open Home
2. Tap project name / project summary area
3. Open project switcher dialog
4. Select another project card
5. App updates current project id
6. Dashboard reloads using the selected project

---

### Project creation flow

A typical project creation flow is:

1. Open project switcher
2. Tap **New project**
3. Fill in:
    - project name
    - course
    - deadline
4. Save
5. App creates:
    - a new `Project`
    - a new `ProjectChecklistState`
6. App automatically switches to the new project

---

### Project editing flow

A typical project editing flow is:

1. Open project switcher
2. Tap **Edit current**
3. Modify:
    - project name
    - course
    - deadline
4. Save
5. Home dashboard updates immediately

---

## Tech Stack

- **Language:** Java
- **Platform:** Android
- **Database:** Room
- **UI:** XML layouts
- **Persistence:** Local database + lightweight current project preference
- **Architecture style:** feature-based package structure with local state

---

## Project Structure

    ProjectPilot/
    ├── README.md
    ├── .gitignore
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── gradle/
    │   └── wrapper/
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    └── app/
        ├── build.gradle.kts
        ├── proguard-rules.pro
        └── src/
            └── main/
                ├── AndroidManifest.xml
                ├── java/
                │   └── com/
                │       └── example/
                │           └── groupproject/
                │               ├── CurrentProjectManager.java
                │               ├── MainActivity.java
                │               ├── MotionUtils.java
                │               ├── data/
                │               │   ├── db/
                │               │   │   ├── AppDatabase.java
                │               │   │   ├── MeetingDao.java
                │               │   │   ├── ProjectChecklistDao.java
                │               │   │   ├── ProjectDao.java
                │               │   │   └── TaskDao.java
                │               │   └── model/
                │               │       ├── Meeting.java
                │               │       ├── Project.java
                │               │       ├── ProjectChecklistState.java
                │               │       └── Task.java
                │               └── ui/
                │                   ├── checklist/
                │                   │   └── ChecklistFragment.java
                │                   ├── home/
                │                   │   └── HomeFragment.java
                │                   ├── meetings/
                │                   │   ├── AddMeetingActivity.java
                │                   │   ├── EditMeetingActivity.java
                │                   │   ├── MeetingAdapter.java
                │                   │   └── MeetingsFragment.java
                │                   └── tasks/
                │                       ├── AddTaskActivity.java
                │                       ├── EditTaskActivity.java
                │                       ├── TaskAdapter.java
                │                       └── TasksFragment.java
                └── res/
                    ├── anim/
                    ├── color/
                    ├── drawable/
                    ├── layout/
                    ├── menu/
                    ├── mipmap/
                    ├── values/
                    └── xml/

### Structure Notes

- `README.md`  
  Provides the project overview, feature summary, setup steps, and development notes.

- `.gitignore`  
  Defines files and folders that should not be tracked by Git.

- `build.gradle.kts`  
  Stores the root-level Gradle build configuration for the project.

- `settings.gradle.kts`  
  Declares the Gradle modules included in the project.

- `gradle/`  
  Contains Gradle wrapper files required to build the project consistently across machines.

- `app/`  
  Main Android application module.

- `app/build.gradle.kts`  
  Stores module-level Android and dependency configuration.

- `AndroidManifest.xml`  
  Declares core app metadata, activities, permissions, and application configuration.

- `CurrentProjectManager.java`  
  Stores and retrieves the currently active project id.

- `MainActivity.java`  
  Hosts the main navigation structure and bottom navigation switching.

- `MotionUtils.java`  
  Contains shared UI interaction helpers and motion-related utility logic.

- `data/db/`  
  Contains Room database classes and DAO interfaces.

- `AppDatabase.java`  
  Defines the Room database and exposes DAO access methods.

- `TaskDao.java`  
  Handles task-related database operations.

- `MeetingDao.java`  
  Handles meeting-related database operations.

- `ProjectDao.java`  
  Handles project-related database operations.

- `ProjectChecklistDao.java`  
  Handles project-specific checklist state operations.

- `data/model/`  
  Contains entity classes used by Room and the app’s data layer.

- `Task.java`  
  Defines the task entity, including task metadata and project association.

- `Meeting.java`  
  Defines the meeting entity, including title, date, notes, and project association.

- `Project.java`  
  Defines the project entity, including project name, course, deadline, and description.

- `ProjectChecklistState.java`  
  Defines the checklist state entity for a specific project.

- `ui/home/`  
  Contains the dashboard logic for the currently active project.

- `HomeFragment.java`  
  Controls the main dashboard view, project switching, project creation/editing dialogs, and summary display.

- `ui/tasks/`  
  Contains task list, add task, edit task, and adapter logic.

- `TasksFragment.java`  
  Displays tasks for the current project.

- `AddTaskActivity.java`  
  Handles creation of a new task for the active project.

- `EditTaskActivity.java`  
  Handles editing of an existing task.

- `TaskAdapter.java`  
  Binds task data to RecyclerView items.

- `ui/meetings/`  
  Contains meeting list, add meeting, edit meeting, and adapter logic.

- `MeetingsFragment.java`  
  Displays meetings for the current project.

- `AddMeetingActivity.java`  
  Handles creation of a new meeting record for the active project.

- `EditMeetingActivity.java`  
  Handles editing of an existing meeting record.

- `MeetingAdapter.java`  
  Binds meeting data to RecyclerView items.

- `ui/checklist/`  
  Contains project-specific checklist logic.

- `ChecklistFragment.java`  
  Displays and updates the checklist state for the active project.

- `res/layout/`  
  Contains XML layouts for screens, dialogs, cards, and list items.

- `res/drawable/`  
  Contains custom backgrounds, badges, buttons, and card styling resources.

- `res/color/`  
  Contains color state lists and color-related UI resources.

- `res/anim/`  
  Contains animation resources used for screen transitions and UI motion.

- `res/menu/`  
  Contains menu definitions such as bottom navigation items.

- `res/mipmap/`  
  Contains launcher icons and app icon resources.

- `res/values/`  
  Contains shared values such as colors, strings, dimensions, and themes.

- `res/xml/`  
  Contains additional XML configuration files used by the Android app.

## Data Model

### Project

Represents a single project workspace.

**Typical fields include:**

- `id`
- `name`
- `course`
- `deadline`
- `description`

**Purpose:**

- acts as the parent entity for project-specific data
- provides top-level metadata for the dashboard
- supports multi-project organization inside the app

---

### Task

Represents a task belonging to one project.

**Typical fields include:**

- `id`
- `projectId`
- `title`
- `assignee`
- `dueDate`
- `status`

**Purpose:**

- stores actionable work items
- supports progress tracking
- links directly to one project through `projectId`

---

### Meeting

Represents a meeting record belonging to one project.

**Typical fields include:**

- `id`
- `projectId`
- `title`
- `meetingDate`
- `notes`

**Purpose:**

- stores project discussion summaries
- captures meeting notes and decisions
- links directly to one project through `projectId`

---

### ProjectChecklistState

Represents checklist completion state for one project.

**Typical fields include:**

- `id`
- `projectId`
- `reportCompleted`
- `sourceCodeReady`
- `readmeReady`
- `slidesReady`
- `demoReady`
- `videoReady`

**Purpose:**

- tracks submission readiness
- stores independent checklist state per project
- drives checklist summary shown on the Home page

---

## Database Design

ProjectPilot uses **Room** for local persistence.

### AppDatabase

`AppDatabase` is the central Room database class.

It currently manages the following entities:

- `Task`
- `Meeting`
- `Project`
- `ProjectChecklistState`

It also exposes the following DAO interfaces:

- `taskDao()`
- `meetingDao()`
- `projectDao()`
- `projectChecklistDao()`

---

### TaskDao

**Typical responsibilities:**

- insert task
- update task
- get task by id
- get tasks by `projectId`

This is how the Tasks page becomes project-specific.

---

### MeetingDao

**Typical responsibilities:**

- insert meeting
- update meeting
- get meeting by id
- get meetings by `projectId`

This is how the Meetings page becomes project-specific.

---

### ProjectDao

**Typical responsibilities:**

- insert project
- update project
- get all projects
- get project by id
- count projects

This supports project creation, project editing, and project switching.

---

### ProjectChecklistDao

**Typical responsibilities:**

- insert checklist state
- update checklist state
- get checklist state by `projectId`

This is how each project gets its own independent checklist record.

---

## Current Project Mechanism

The app keeps track of the active project through `CurrentProjectManager`.

This component stores the current project id using lightweight local preference storage.

### Why this matters

Without a current project mechanism, all pages would read global data.  
With this mechanism, each screen knows which project it should display.

### What it affects

The current project id is used by:

- `HomeFragment`
- `TasksFragment`
- `MeetingsFragment`
- `ChecklistFragment`
- `AddTaskActivity`
- `AddMeetingActivity`
- project switcher logic

### Result

Once the current project changes:

- Home dashboard reloads
- tasks displayed are different
- meetings displayed are different
- checklist state displayed is different

This is the key feature that transforms the app from single-workspace to multi-workspace.

---

## UI and Design Notes

ProjectPilot has undergone several UI refinements to improve consistency and product feel.

### Custom dialogs

The app now uses custom dialogs for:

- project switching
- project creation
- project editing

This replaces plain system dialogs with more unified workspace-oriented UI.

---

### Project switcher UI

The project switcher includes:

- custom title and subtitle
- project cards
- current project badge
- selected project highlight
- action buttons for project management

This makes project switching more visible and more polished.

---

### Project form UI

New project and edit project now share the same custom form layout.

This ensures consistent:

- title hierarchy
- input spacing
- button style
- date input behavior

---

### Dashboard refinement

The Home dashboard includes:

- clearer information hierarchy
- formatted deadline display
- improved project summary text
- more product-like card content for tasks, meetings, and checklist

---

### Custom resources

Examples of custom resources used in the app include:

- `dialog_project_switcher.xml`
- `dialog_project_form.xml`
- `item_project_option.xml`
- `bg_project_option.xml`
- `bg_project_option_selected.xml`
- `bg_project_badge_soft.xml`
- `bg_button_quiet.xml`
- `bg_button_primary.xml`
- `bg_input_surface.xml`
- `bg_card_surface.xml`

---

## How to Run

### Requirements

- Android Studio
- Android SDK
- Android emulator or physical Android device

### Steps

1. Clone the repository:

~~~bash
git clone https://github.com/GeniusNefelibata/ProjectPilot.git
~~~

2. Open the project in Android Studio.

3. Sync Gradle files.

4. Build and run the app on an emulator or Android device.

---

## Current Functional Scope

The current version of ProjectPilot already supports:

- multiple projects inside one app
- current project switching
- creating new projects
- editing current project
- project-specific task storage
- project-specific meeting storage
- project-specific checklist storage
- project-specific home dashboard
- custom project switcher dialog
- custom project form dialog
- date picker support for deadline / task / meeting dates

---
