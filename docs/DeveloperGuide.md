---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `NetworkBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `NetworkBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `NetworkBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

Here is an overview of what the other classes in `Logic` do:
* `ArgumentMultiMap` and `ArgumentTokeniser` are used to map the parameters of 
the user's input into key-value pairs, where the keys are specified using `ArgumentTokeniser`
* `CliSyntax` is where command-specific keywords are stored. It is used as the arguments for `ArgumentTokeniser`
to process the user input into: `{keyword : parameter}` pairs.
  * Example usage: The text `1 /name John Doe /phone 98765432` when
    mapped using `ArgumentTokeniser` with the keywords `/name`
    and `/phone` produces:
    * `{1}, {/name : John Doe}, {/phone : 98765432}`
    * Any text that appears before the first possible keyword is stored
      in its own entry.
  * `ArgumentMultiMap` can then perform specific operations, including
    but not limited to:
    * Retrieve all values/only a specific value from the set.
    * Check that a certain key only appears once, or exactly once.
* `ParserUtil` contains useful commands for parsing text such as removing
  leading/trailing whitespace from text, verifying that there are no 
  duplicate entries in the text, and so on.

The activity diagram below describes the workflow of `AddressBookParser`
when determining which `Parser` to use:
* `TO BE IMPLEMENTED IN 1.4`

The sequence diagram below illustrates the interactions within the
`FilterCommand` class to generate a `FilterCommand` object, using
`ArgumentMultiMap` and `ArgumentTokeniser`:
* `TO BE IMPLEMENTED IN 1.4`

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniqueList<Person>` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `NetworkBook`, which `Person` references. This allows `NetworkBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `NetworkBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.networkbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedNetworkBook`. It extends `NetworkBook` with an undo/redo history, stored internally as an `networkBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedNetworkBook#commit()` — Saves the current address book state in its history.
* `VersionedNetworkBook#undo()` — Restores the previous address book state from its history.
* `VersionedNetworkBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitNetworkBook()`, `Model#undoNetworkBook()` and `Model#redoNetworkBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedNetworkBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitNetworkBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `networkBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitNetworkBook()`, causing another modified address book state to be saved into the `networkBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitNetworkBook()`, so the address book state will not be saved into the `networkBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoNetworkBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial NetworkBook state, then there are no previous NetworkBook states to restore. The `undo` command uses `Model#canUndoNetworkBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoNetworkBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `networkBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone NetworkBook states to restore. The `redo` command uses `Model#canRedoNetworkBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitNetworkBook()`, `Model#undoNetworkBook()` or `Model#redoNetworkBook()`. Thus, the `networkBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitNetworkBook()`. Since the `currentStatePointer` is not pointing at the end of the `networkBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is an NUS computing student or computing professional
* has a need to manage a significant number of contacts
* has a need to manage a lot of details for each contact
* is looking into networking with other computing students and professionals
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps


**Value proposition**: As computing students and professionals network with alumni to expand their career prospects, our app keeps a list of contacts of people that each user networks with.

* Sort users by priority, courses taken/taking, specialization(s) taken/intending to take, graduation year
* Offline, with a static online page that contains user manual and download link
* Search by any details, i.e. phone number, email, specialisations, courses taken, graduation year.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                 | I want to …​                                                                               | So that I can…​                                                          |
|---------|-------------------------|--------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `* * *` | new user                | see usage instructions in the app                                                          | refer to instructions when I forget how to use the app                |
| `* * *` | user                    | create a new contact                                                                       | keep a record of individuals in my network                              |
| `* * *` | user                    | add more details about an existing contact                                                 | store information about my contacts for future reference                |
| `* * *` | user                    | edit details of a contact                                                                  | replace outdated details with more accurate information                 |
| `* * *` | user                    | delete a contact                                                                           | remove individuals I no longer keep contact with |
| `* *`   | user                    | find a contact by name                                                                     | locate details of contacts without having to go through the entire list |
| `* *`   | user with many contacts | sort contacts by their details                                                             | locate contacts with special characteristics that I am looking for      |
| `*`     | user with many contacts | filter contacts based on their details                                                     | locate contacts who fulfil certain conditions that I am looking for     |
| `* *`   | new user                | use commonly-available keyboard shortcuts (e.g. ctrl-c for copy, ctrl-v for paste)         | provide input more efficiently with shortcuts I am accustomed to |
| `* *`   | user                    | use simple and easy-to-press shortcuts                                                     | remember and execute the shortcuts more easily |
| `* *`   | user                    | open my email app by clicking on my contact's email                                        | send emails to my contacts more efficiently |
| `* *`   | user                    | open the relevant website by clicking on my contact's social link                          | conveniently access their social links when needed |
| `* *`   | user                    | an easily accessible and static online page containing a download link to the mobile app   | quickly download the app on my device when needed                       |
| `* *`   | new user                | have a quick-start guide                                                                   | start using the basic functionality of the app as soon as possible |
| `* *`   | user                    | visit an online page containing the complete user manual                                   | refer to the full set of instructions when needed            |
| `*`     | user                    | navigate to the relevant section of the online manual directly from the catalogue | quickly find instructions on the feature I want to use       |
| `*`     | user                    | export my contacts in the form of readable text                                            | easily share my contacts with others                                    |
| `*`     | user with many devices  | import data from my exported contacts                                                      | sync my contact details across different devices |

### Use cases

(For all use cases below, the **System** is the `NetworkBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Create a new contact**

**MSS**

1. User requests to create a new contact with a name.

2. NetworkBook creates a new contact with the name.

   Use case ends.

**Extensions**

- 1a. User does not include the contact's name in the request.

  - 1a1. NetworkBook shows an error message.

    Use case ends.

- 1b. User includes more details about the contact in the request.

  - 1ba. All the details provided are in the correct format.

    - 1ba1. NetworkBook creates a contact with all included details.

      Use case ends.

  - 1bb. Some of the details provided are not correctly formatted.

    - 1bb1. NetworkBook shows an error message.

      Use case ends.

- 1c. The name is not unique.

  - 1c1. NetworkBook requests user to select from one of three options: create a new contact, delete the old contact and add the new one, or abort the current create operation.

    - 1c1a. User selects option to create a new contact.

      Use case resumes at step 2.

    - 1c1b. User selects option to delete the old contact and add the new one.

      - 1c1b1. NetworkBook deletes the old contact.

      - 1c1b2. NetworkBook adds the new contact with all included details.

        Use case ends.

    - 1c1c. User selects option to abort the current add operation.

      Use case ends.

**Use case: Add phone number to contact**

(This use case is also applicable to adding **email, specialisation, tag** to a contact. For each contact, each of these fields is recorded by a list, and new entries added to a field will be appended to the field's list.)

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to add a phone number to a specific contact in the list.

4. NetworkBook adds the new phone number to the contact's list of phone numbers.

5. NetworkBook informs user of the contact's new list of phone numbers.

   Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The given index is invalid.

  - 3a1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3b. User does not give a phone number.

  - 3b1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3c. The given phone number is in an invalid format.

  - 3c1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3c. The given phone number is already present in the contact's list of phone numbers.

  - Use case resumes at step 5.

**Use case: Add graduation year to a contact**

(This use case is also applicable to adding **priority** to a contact. For each contact, each of these fields is a single value instead of a list. They cannot be added if the value is already present.)

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to add graduation year to a specific contact in the list.

4. NetworkBook adds the graduation year to the contact.

5. NetworkBook informs user of the contact's new graduation year.

   Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The given index is invalid.

  - 3a1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3b. User does not give a graduation year.

  - 3b1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3c. The given graduation year is in an invalid format.

  - 3c1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3d. The contact already has a graduation year.

  - 3d1. NetworkBook shows an error message.

    Use case resumes at step 2.

**Use case: Add course to a contact**

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to add course to a specific contact in the list.

4. NetworkBook adds the course to the contact's list of courses.

5. NetworkBook informs user of the contact's new list of courses.

   Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The given index is invalid.

  - 3a1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3b. User does not give a course code.

  - 3b1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3c. The given course code is in an invalid format.

  - 3c1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3d. User does not give a start date.

  - 3d1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3e. The given start/end date is in an invalid format.

  - 3e1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3f. User gives an end date in the correct format.

  - 3f1. NetworkBook includes end date in the new course's details.

  - 3f2. NetworkBook adds the new course to the contact's list of courses.

    Use case resumes at step 5.

- 3g. The given course code is already present in the contact's list of courses.

  - 3g1. NetworkBook updates the given course with the new course details provided.

    Use case resumes at step 5.

**Use case: Add social link to a contact**

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to add social link a specific contact in the list.

4. NetworkBook adds the social link to the contact's list of links.

5. NetworkBook informs user of the contact's new list of links.

   Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The given index is invalid.

  - 3a1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3b. User does not give a social link.

  - 3b1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3c. The given social link is in an invalid format.

  - 3c1. NetworkBook shows an error message.

    Use case resumes at step 2.

- 3d. User provides additional note after the social link.

  - 3d1. NetworkBook includes the note in the link's details.

  - 3d2. NetworkBook adds the new social link to the contact's list of links.

    Use case resumes at step 5.

**Use case: Edit a contact**

**MSS**

1.  User requests to list contacts.
2.  NetworkBook shows a list of contacts.
3.  User requests to edit some field of a specific contact in the list.
4.  NetworkBook updates the contact.

    Use case ends.

**Extensions**

* 2a. The list is empty.
  
  Use case ends.

* 3a. The given index is invalid.

    * 3a1. NetworkBook shows an error message.

      Use case resumes at step 2.

* 3b. Some of the fields provided are not correctly formatted.

    * 3b1. NetworkBook shows an error message.

      Use case resumes at step 2.

**Use case: Delete a contact**

**MSS**

1.  User requests to list contacts.
2.  NetworkBook shows a list of contacts.
3.  User requests to delete a specific contact in the list.
4.  NetworkBook deletes the contact.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. NetworkBook shows an error message.

      Use case resumes at step 2.


**Use case: Sort contacts**

**MSS**

1.  User chooses to sort based on a field (e.g. name, graduation year, priority) and an order (ascending or descending).

2.  NetworkBook shows list of user’s contacts, sorted by the specified field and in the specified order.

**Extensions**

* 1a. User has no contacts to sort.

    * 1a1. NetworkBook shows an empty contact list.

      Use case ends.

* 1b. User does not specify a field.

    * 1b1. NetworkBook shows an error message.

      Use case ends.

* 1c. The specified field is invalid.

    * 1c1. NetworkBook shows an error message.

      Use case ends.

* 1d. User does not specify an order.

    * 1d1. Networkbook shows list of user's contacts, sorted by the specified field and ascending order by default.

        Use case ends.

* 1e. The specified sorting order is invalid.

    * 1e1. NetworkBook shows an error message.

      Use case ends.

* 1f. Certain contacts do not have any data in the specified field (e.g. no email address stored)

    * 1f1. NetworkBook shows sorted list of user's contacts who have data in the specified field. Any contacts without that field specified are put at the end of the list.

      Use case ends.

**Use case: Search contacts by name**

**MSS**

1.  User specifies the text they would like to search.

2.  NetworkBook shows list of user’s contacts with names containing the searched text.

**Extensions**

* 1a. The user has no contacts to search.

    * 1a1. NetworkBook shows an empty contact list.

      Use case ends.

* 1b. The search text is not specified.

    * 1b1. NetworkBook shows an error message.

      Use case ends.

 * 1c. There are no contacts with names containing the searched text.

    * 1c1. NetworkBook shows a message saying that there are no matches.

      Use case ends.


**Use case: Open email app from NetworkBook**

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to email a specific contact in the list.

4.  NetworkBook loads the default email app of the user.

5.  NetworkBook pre-fills the contact's email in the recipient field.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The user has not logged in to his default email app.

    * 4a1. User will be taken to the sign-in page of his default email app.

      Use case ends.

**Use case: Open social link from NetworkBook**

**MSS**

1. User requests to list contacts.

2. NetworkBook shows a list of contacts.

3. User requests to open a social link of a contact stored in the app.

4.  NetworkBook loads the default browser app of the user.

5.  NetworkBook visits the requested social link in the browser.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The social link is invalid.

    * 4a1. The user will see the error page displayed by the browser used to load the page link.

      Use case ends.

* 4b. The page is valid but fails to load.

    * 4b1. The user will see the error page displayed by the browser used to load the page link.

      Use case ends.

* 4c. The social link directs to a social media app which the user has not logged in.

    * 4c1. The user will be taken to the sign-in page of the social media app.

      Use case ends.

**Use case: Download mobile app from online page**

**MSS**

1. User requests to visit the online page. 
2. The online page renders.
3. User selects link to download the mobile app.
4. Mobile app starts downloading on the user's device.

    Use case ends.

**Extensions**

* 1a. The online page fails to load.

    * 1a1. The user will see the error page displayed by the browser used to load the page link.

      Use case ends.

**Use case: Access user manual on online page**

**MSS**

1.  User requests to visit the online page.
2. The online page renders.
3. User selects link to visit the user manual.
4. The user manual page renders.

   Use case ends.

**Extensions**

* 1a. The online page fails to load.

    * 1a1. The user will see the error page displayed by the browser used to load the page link.

      Use case ends.
  
* 3a. The user manual page fails to load.

    * 3a1. The user will see the error page displayed by the browser used to load the page link.

      Use case ends.

**Use case: Navigate from catalogue to relevant section of online manual**

**Preconditions:** User is on user manual page.

**MSS**

1. User chooses a manual section title within the catalogue.
2. Browser navigates to display relevant section of online manual. 

    Use case ends.

**Use case: Export contact in readable text**

**MSS**

1.  User requests to list contacts.
2.  NetworkBook shows a list of contacts.
3.  User requests to export a specific contact in the list.
4.  NetworkBook exports a text file storing user details in a readable format.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

**Use case: Import data from exported contacts**

**MSS**

1. User requests to import contacts data.

2. NetworkBook loads the user's file explorer.

3. User selects a file containing exported contacts.

4. NetworkBook creates new contacts with details specified in the export file.

   Use case ends.

**Extensions**

* 2a. User exits the file explorer without selecting any file.

  * 2a1. NetworkBook shows an error message.

    Use case ends.

* 3a. The file chosen is not in the correct format.

  * 3a1. NetworkBook shows an error message.

    Use case ends.

* 3b. NetworkBook does not have permission to read the file.

  * 3b1. NetworkBook shows an error message.

    Use case ends.

- 3c. The file contains contacts with same names as some existing contacts.

  - 3c1. NetworkBook skips these contacts and only creates new contacts whose names are not present yet.

  - 3c2. NetworkBook shows the skipped contacts.

    Use case ends.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
1. Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
1. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
1. A new user should be able to familiarise him/herself with most of the basic features of the app upon finishing going through the quick-start guide.
1. A user should be able to use commonly available and easy-to-remember keyboard shorcuts
   * Ctrl+C: copy text
   * Ctrl+V: paste text
   * Ctrl+N: new contact detail
   * Ctrl+W: exit the app
   * Ctrl+F: find a contact
   * Ctrl+H/Ctrl+R/Ctrl+G: edit a contact
   * Up/down arrow keys: access previous commands
1. A new user should be able to understand the meaning of a command just by looking at the keywords used in the command.


### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X.
* **Command**: a string keyed in by the user in the GUI text input that signifies an action to be done by the app.
* **Contact**: a contact of the user whose information is stored in the app, which includes name, phone numbers, emails, links, graduation year, courses taken, specialisations, priority level and tags of/associated with the person.
* **Field**: an attribute of a contact that describes information about the contact and can take different values. Possible fields of a contact are elaborated in the **contact** term above.
* **Course taken**: a module that a person has taken in university or outside (for e.g. CS2103T module in NUS).
* **Specialisation**: the specialisation a person can take in their computing degree in NUS (e.g. Software Engineering, Artificial Intelligence).
* **Graduation year**: the year and semester that a person will graduate / has graduated from NUS (e.g. AY2526-S2, meaning the second semester of the academic year spanning from 2025 to 2026).
* **Link**: a web link which directs to a contact's profile page on a social platform (e.g. LinkedIn, GitHub).
* **Tag**: an annotation to a person. This can be anything memorable of the person.
* **Priority**: the priority level of a contact set by the user. Its value can be either high, medium or low.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Editing a person

1. Editing a person's name while all persons are being shown
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   
   2. Test case: `update /name Test 1`
      
      Expected: Name of the first contact should change to `Test`. NetworkBook should show details of the old and new name of the contact.
   
   3. Test case: `update /name Test 0`

      Expected: No person is updated. Error details shown in the status message.
2. Editing other attributes of a person
   1. Prerequisites: List all persons using the 'list' command. Multiple persons in the list.

   2. Test case: `update /phone 12345678 1`

      Expected: Phone number of first contact should change to `12345678`. NetworkBook should show details of old and new phone number.

   3. Test case: `update /email test@email.com 1`

      Expected: Email of first contact should change to `test@email.com`. NetworkBook should show details of the old and new email of the contact.

   4. _{ Likewise for `course`, `specialisation`, `link`, `grad`, `priority`, `tag` }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
