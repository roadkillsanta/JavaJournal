# JavaJournal

# [Pitch]
## Features
- Task Queue
- - There is a sidebar next to weeks holding all the tasks
  - Task can be set completed from here
- Categories
- - Each week has categories that can be assigned to Events and Tasks
  - Different categories are separated by colors
- Mini Viewer
- - Each task and event can be pressed to show a screen showing the details of the occurrence
- Takesie-backsies
- - The Mini-Viewer screen allows for the tasks and events to be deleted
- Custom Ordering
- - Each occurrence has an up and down button to sort it in its day
- Mind Changes
- - The Mini-Viewer allows for the occurrences to be edited
- Week Tabs
- - Each Week has an associated tabS
  - Week Tabs can be deleted and created
- Splash Screen
- - There is a screen at the start of the journal that fades in and out
- Private Lock
- - Saved bujo files can be encrypted to a password 
- Weekly Starters
- - The user can select a default bujo template to start the week
- Visual Flourish(Simple and customizable with category color etc)

<img width="901" alt="image" src="https://github.com/CS-3500-OOD/pa05-indoortonguebubble/assets/22507817/a38de52b-1e61-4925-bd6b-018b8f1ddb76">

# [SOLID] 
## S - Single responsibility
Every class has its own purpose. For example, each controller was responsible for its own window elements: Controller is solely responsible for the main window; WeekController is responsible for the week view.

## O - Open/closed principle
This project is open for extension but closed for modification. It demonstrates this by extensively implementing the MVC paradigm. For example, the AbstractOccurrence class creates a template for an occurrence for the program. It can be easily extended by creating a new class which extends it, and this functionality can be easily dropped into the relevant controllers.

## L - Liskov substitution
The AbstractOccurrence class, the Event class, and the Task class adhere to this principle. Wherever an AbstractOccurrence appears, an Event or a Task can easily be used in its place with no errors.

## I - Interface segregation principle
There were no interfaces used in this project. This adheres to this principle because in a typical MVC project, the controller has an interface which allows it to be extended. However, each controller in this project is responsible for its own windows and elements, thus they share very few (if any) common methods.

## D - Dependency inversion
The sole examples of this in this project are within the controller classes, which is dependent on the Abstract version of Events and Tasks, namely the AbstractOccurrence. 

## Extending the project
Many additional features can be easily implemented with just a few modifications to the project. For example, "Quotes and Notes" can be added as a new data structure which can be linked into the Week and WeekRecord classes. Loading and rending these would be handled in the WeekController.
