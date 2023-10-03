---
layout: page
title: User Guide
---

NetworkBook is a **desktop contact book application**. You can use it to network with other computing students and professionals from NUS.

## Features

### **<u>Category 1 - Add contact information</u>**

### Add email to a contact: `add /email /index`

You can add an email to an existing contact.
A new email will be added to the contact's list of emails,
and no new contact will be created.

Format: `add /email [email] /index [index]`

Example usage:
* `add /email nknguyentdn@gmail.com /index 1`
* `add /email test@example.com /index 2`

Parameters:
* `[email]` is a valid email (`@` (at sign) must be present, 
and `.` (period) must be present after `@` (at sign)).
* `[index]` is the index of the contact in the list.

When the command succeeds:
* `add /email nknguyentdn@gmail.com /index 1`

`Noted, I have added email nknguyentdn@gmail.com to the contact at index 1.`

* `add /email test@example.com /index 2`

`Noted, I have added email test@example.com to the contact at index 2.`

![add email success](images/add-remark/add-email.png)



### Add link to a contact: `add /link /index`

You can add a social link to an existing contact.
A new link will be added to the contact's list of links,
and no new contact will be created.

Format: `add /link [link] [note] /index [index]`

Example usage:
* `add /link https://nknguyenhc.github.io/ website /index 1`
* `add /link https://www.linkedin.com/in/nguyen-khoi-nguyen-6279341a8/ /index 2`

Parameters:
* `[link]` is a valid URL linking to a contact’s social media page.
* `[note]` is a note on the URL for your own reference. 
This parameter is optional, and should be separated.
* `[index]` is the index of the person in the list.

When the command succeeds:
* `add /link https://nknguyenhc.github.io/ /note website /index 1`

`Noted, I have added https://nknguyenhc.github.io/ 
as the “website” of your contact at index 1.`

* `add /link https://www.linkedin.com/in/nguyen-khoi-nguyen-6279341a8/ /index 2`

`Noted, I have added https://www.linkedin.com/in/nguyen-khoi-nguyen-6279341a8/ 
as a link to your contact at index 2.`

![add link success](images/add-remark/add-link.png)



### **Assign priority levels:** **`add /priority /index`** 

You can set the priority level of a contact, 

so that you can easily filter them by priority for future reference.

Format: `add /priority [priority level] /index [index]`

Example usage:

- `add /priority high /index 1`
- `add /index 10 /priority L`
- `add /priority m /index 23`

Parameters:

- `[priority level]` is a word or letter representing the priority level to be assigned to the contact.

  There are three priority levels, **high, medium and low**, 

  represented by either the word itself (e.g. "high") or the first letter ("h"), 

  and they are not case-sensitive.

- `[index]` is the index of the person in the list

When the command succeeds:

- `add /priority high /index 3

`Noted. I have set the priority level to High for contact at index 3.`

![add priority success](images/add-remark/add-priority.png)

When the command fails:

- `add /priority hhhhh /index 1`

`OOPS, to assign a priority level, use:`

`add /priority [priority level] /index [index]`

- `add /priority h /index 100000`

`OOPS, no matching contact with index 100000.`



### **Add tag to a contact: **  **`add /tag /index`** 

You can use the `tag` command to associate a custom category with a contact, 

so that you can filter them by unique criteria for easier searching.

Format: `add /tag [tag name] /index [index]`

Example usage:

- `add /index 1 /tag data analytics`
- `add /tag internship /index 18`

Parameters:

- `[tag name]` is the name of the tag to associate the contact with
- `[index]` is the index of the person in the list

When the command succeeds:

- `add /index 1 /tag data analytics`

`Noted. I have added "data analytics" tag to contact at index 1.`

When the command fails:

- `add /tag internship`

`OOPS, to add tag to a contact, use:`

`add /tag [tag name] /index [index]`

- `add /tag internship /index 100000`

`OOPS, no matching contact with index 100000.`



### **<u>Category 2 - Edit contact details</u>**

### Update contact detail : `update /field /index`

You can update contact details of existing contacts in your book.

Format: `update /[parameter name] [new parameter value] /index [index]`

Example usage:
* `update /name nkn /index 1`
* `update /link https://nknguyenhc.github.io/ /index 1`

Parameters:

* `[parameter name]` refers to the name of the parameters. Names can be:
  * `name`
  * `phone`
  * `course`
  * `specialisation`
  * `email`
  * `link`
  * `grad`
  * `priority`
  * `tag`
* `[new parameter value]` is the new value of the parameter. The new value must follow the formatting of the parameter.
* `[index]` is the index of the contact in the list.

When command succeeds:
* `update /name nkn /index 1`

`OK, the name of the contact at index 1 (Nguyen) has been updated to “nkn”.`

* `update /link https://nknguyenhc.github.io/ /index 1`

```
There are multiple links to the contact at index 1. Which one do you wish to update?
https://nknguyenhc.github.io/ip
https://www.linkedin.com/in/nguyen-khoi-nguyen-6279341a8/
```

Assume that you in 1:

`OK, a link of the contact at index 1 (Nguyen) has been updated
from https://nknguyenhc.github.io/ip to https://nknguyenhc.github.io/.`

![update success](images/edit/edit.png)



### **Delete a contact**:  **`delete`** 

You can remove a contact from your NetworkBook using the `delete` command, 

so that your book only contains contact details of those relevant.

Format: `delete [index]`

Example usage:

- `delete 1`
- `delete 16`

Parameters:

- `[index]` is the index of the person in the list

When the command succeeds:

- `delete 1`

`Noted. I have deleted contact at index 1.`

When the command fails:

- `delete`

`OOPS, to delete a contact, use:`

`delete [index]`

- `delete 100000`

`OOPS, no matching contact with index 100000.`



### **<u>Category 3 - Find contacts</u>**





## Command summary

Category | Format, Examples
--------|------------------
**Add** | `add /email [email] /index [index]` <br> e.g., `add /email test@example.com /index 2`<br><br>`add /link [link] [note] /index [index]`<br>e.g., `add /link https://nknguyenhc.github.io/ website /index 1`<br><br>`add /priority [priority level] /index [index]`<br>e.g., `add /priority high /index 1`<br><br>`add /tag [tag name] /index [index]`<br>e.g., `add /index 1 /tag data analytics` 
**Edit** | `update /[parameter name] [new parameter value] /index [index]`<br> e.g.,`update /name nkn /index 1`<br><br>`delete [index]`<br>e.g., `delete 1` 
**Find** |  
