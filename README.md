Conference Management System

Duration: Aug. 2025 – Dec. 2025  
Author: Vamsi Gaddipati  

Description:
- The Conference Management System is a Java-based application used to organize conferences by managing sessions and accepted items. Items are added directly as accepted items (there is no review process) and are assigned to a session within a conference.
- Each conference contains multiple sessions, and each session contains one or multiple accepted items. Sessions have a fixed time limit, and accepted items consume time within the session. The system ensures that items can only be added if the session has sufficient remaining time.
- Accepted items can be one of three types: Paper, Lightning Talk, or Panel, each with a duration defined by the user or constrained by type rules.

Key Concepts:
- A Conference contains many sessions.
- A Session has a time limit and contains many accepted items.
- An Accepted Item belongs to exactly one session.
- Accepted items are added directly (no approval or review workflow).
- Time constraints are enforced at the session level.

Features:
- Create and manage conferences
- Add multiple sessions to a conference, each with a time limit
- Add accepted items directly to sessions
- Support for three accepted item types:
  - Paper
  - Lightning Talk
  - Panel
- Enforced session capacity based on total item duration
- Prevents invalid operations (overfilling sessions, duplicates, invalid durations)
- Save and load conference data using file I/O

Accepted Item Types:
| Type           | Description |
| Paper          | Standard presentation with user-defined duration |
| Lightning Talk | Short presentation with a fixed short duration |
| Panel          | Long-form discussion with a fixed larger duration |

Design Principles:
- Encapsulation: Conference, session, and item data are private and managed through methods.
- Abstraction: External code interacts with conferences and sessions using high-level operations.
- Inheritance & Polymorphism: Accepted item types share a common abstract base class.
- Robust Error Handling: Invalid actions (over-capacity sessions, invalid inputs) are prevented through exceptions and validation.

Testing:
- Unit tests validate:
  - Session time limits
  - Accepted item duration constraints
  - Proper assignment of items to sessions
- Automated tests (Jenkins) ensure:
  - Only valid state changes occur
  - System stability under invalid input scenarios

Technologies Used:
- Language: Java  
- IDE: Eclipse  
- Testing: JUnit/Jenkins  
- Persistence: File I/O  

Example Workflow:
1. Create a conference.
2. Add multiple sessions, each with a time limit.
3. Add accepted items to the conference.
4. Add desired accepted items to desired sessions.
5. System checks time availability and other constraints before adding items.
6. Save or load conference data using files.
