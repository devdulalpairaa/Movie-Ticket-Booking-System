# 🎬 Movie Ticket Booking System

A complete **Java Swing desktop application** for booking movie tickets — built with
pure Java (no database, no Spring Boot, no JavaFX, no web technologies) to demonstrate
Object-Oriented Programming principles and hand-written Data Structures & Algorithms.

Built for a 3rd-year B.Tech CSE **OOP + DSA** university project / submission.

---

## 1. Project Overview

The application lets a customer **register/login → choose a city → choose a theatre →
choose a movie → choose a show → pick seats from a 2D seat map → pay (simulated) →
receive a ticket → view booking history**, while an **Admin** can log in separately to
add/update/delete movies, and view revenue, users and booking statistics.

Every major action (login, seat selection, payment, ticket generation, etc.) is logged
to the **console** in addition to being shown in the GUI, e.g.:

```
[10:02:15] >> User Logged In: rahul (CUSTOMER)
[10:02:20] >> City Selected: Delhi
[10:02:24] >> Theatre Selected: PVR Vegas Mall
[10:02:29] >> Movie Selected: Interstellar
[10:02:33] >> Show Selected: 7:30 PM
[10:02:41] >> Seat Selected: A5
[10:02:55] >> Payment Successful: Rs. 300.0 via UPI
[10:02:55] >> Ticket Generated: TCK9F3A21B0
```

Data is persisted in simple pipe-delimited **text files** under a `data/` folder that is
created automatically next to the running application, so bookings, tickets and user
accounts survive between runs.

---

## 2. Folder Structure

```
MovieTicketBookingSystem/
├── pom.xml
├── README.md
├── data/                              <- auto-created at runtime (users/movies/bookings/tickets .txt)
├── src/
│   └── main/
│       ├── java/com/movieticket/
│       │   ├── Main.java              <- application entry point
│       │   ├── model/                 <- entities (Person, Movie, Theatre, Screen, Seat, Show, Booking, Ticket, Payment...)
│       │   ├── exception/             <- custom checked exceptions
│       │   ├── dsa/                   <- CustomStack, CustomQueue, Searching, Sorting (hand-written)
│       │   ├── util/                  <- FileManager, Validator, Logger
│       │   ├── service/               <- AuthenticationService, MovieService, BookingService,
│       │   │                             PaymentService, TicketService, HistoryService
│       │   └── gui/                   <- all Swing screens + MainFrame (CardLayout)
│       └── resources/
│           └── icons/                 <- placeholder for optional custom icons (emoji used by default)
└── out/ (generated)                   <- compiled .class files (mvn clean install / target/)
```

---

## 3. Class Diagram (ASCII)

```
                         ┌───────────────────┐
                         │   Person (abstract)│
                         │  - userId, name...  │
                         │  + getRole() abstract│
                         └─────────┬──────────┘
                    Inheritance ▲  │  ▲ Inheritance
                    ┌────────────┘  └────────────┐
             ┌──────┴──────┐               ┌──────┴──────┐
             │  Customer   │               │    Admin    │
             │ bookingIds[]│               │             │
             └──────┬──────┘               └─────────────┘
                     │ Association (1..*)
                     ▼
             ┌───────────────┐        Composition       ┌────────────┐
             │    Booking    │────────────────────────▶│   Ticket   │
             │ seatNumbers[] │                           └────────────┘
             │ status,amount │
             └───────┬───────┘
                      │ Association
                      ▼
             ┌────────────────┐   Association    ┌────────────┐
             │      Show       │─────────────────▶│   Movie    │
             │ showTime, price │                   │ title, ... │
             └───────┬────────┘                    └─────┬──────┘
                      │ Association                       │ Aggregation
                      ▼                                    ▼
             ┌────────────────┐   Composition     ┌────────────────┐
             │     Screen      │─────────────────▶│    Theatre      │
             │  seatMatrix[][] │                    │ screens: List<> │
             └───────┬────────┘                    └────────────────┘
                      │ Composition
                      ▼
                 ┌─────────┐
                 │  Seat   │
                 └─────────┘

             ┌───────────────────────┐
             │   Payment (abstract)   │   Polymorphism / Abstraction
             │  + process() abstract  │
             └──┬────┬─────┬─────┬────┘
        ┌────────┘    │     │     └────────┐
   ┌────┴───┐ ┌────────┴─┐ ┌┴──────────┐ ┌──┴───────┐
   │  Upi   │ │  Credit  │ │  Debit    │ │  Cash    │
   │Payment │ │  Card    │ │  Card     │ │ Payment  │
   └────────┘ └──────────┘ └───────────┘ └──────────┘
```

---

## 4. Use Case Diagram (ASCII)

```
                     ┌───────────────────────────────────┐
                     │      Movie Ticket Booking System    │
                     │                                     │
   (Customer) ──────▶│  Register / Login                   │
        │            │  Browse / Search / Sort Movies       │
        │            │  Choose City -> Theatre -> Show      │
        │            │  Select Seats (2D layout)            │
        │            │  Make Payment (simulated)             │
        │            │  Generate & View Ticket               │
        │            │  View Booking History                 │
        │            │  Cancel / Undo Last Booking            │
        │            │  Join Waiting List (show full)         │
        │            │                                       │
   (Admin) ──────────▶│  Admin Login                          │
                     │  Add / Update / Delete Movie          │
                     │  View All Bookings                    │
                     │  View Revenue / Users / Movie Stats    │
                     └───────────────────────────────────┘
```

---

## 5. Sequence Diagram — Booking a Ticket (ASCII)

```
Customer   LoginScreen   AuthService   MovieService  SeatLayoutPanel  BookingService  PaymentService  TicketService
   |            |              |             |               |               |               |              |
   |--login---->|              |             |               |               |               |              |
   |            |--login()---->|             |               |               |               |              |
   |            |<--Person-----|             |               |               |               |              |
   |--pick city/theatre/movie/show---------->|               |               |               |              |
   |            |              |             |--getShows()-->|               |               |              |
   |--select seats------------------------------------------>|               |               |              |
   |            |              |             |               |--createBooking()------------->|              |
   |            |              |             |               |<--Booking(PENDING)-------------|              |
   |--enter payment details----------------------------------------------------------------->|              |
   |            |              |             |               |               |--processPayment()---------->|
   |            |              |             |               |               |<--Payment(SUCCESS)-----------|
   |            |              |             |               |--confirmBooking()------------->|              |
   |            |              |             |               |               |               |--generateTicket()->|
   |            |              |             |               |               |               |<--Ticket-----------|
   |<---------------------------------- Ticket displayed on screen ----------------------------------------------|
```

---

## 6. How to Run

### Prerequisites
- **JDK 17 or later** (JDK 21 recommended) installed and on your `PATH`
- **Maven 3.6+**

### Build & Run with Maven
```bash
cd MovieTicketBookingSystem
mvn clean install
java -jar target/MovieTicketBookingSystem.jar
```

### Run directly from an IDE (IntelliJ IDEA / VS Code / Eclipse)
1. Open the folder as a Maven project.
2. Let it resolve dependencies (there are none beyond the JDK itself — it is pure `java.*`/`javax.swing.*`).
3. Run `com.movieticket.Main`.

### First run
- A `data/` folder with `users.txt`, `movies.txt`, `bookings.txt`, `tickets.txt` is created
  automatically next to wherever you run the app from, and pre-seeded with 5 demo movies,
  4 theatres across 4 cities, and several shows.
- A default **Admin** account is created automatically:
  - Username: `admin`
  - Password: `admin123`
- Customers can register their own account from the **Register** screen.

---

## 7. Features

### Customer
- Register / Login
- Browse movies by city → theatre
- **Search** movies by title (custom **linear search**)
- **Sort** movies by rating (custom **bubble sort**) or title (custom **quick sort**)
- Choose show time & screen
- Interactive **2D seat map** (rows × columns, VIP/Premium/Regular pricing tiers)
- Simulated **payment** (UPI / Credit Card / Debit Card / Cash)
- Auto-generated **ticket** with full booking details, also printed to console
- **Booking history** table
- **Undo / cancel last booking** (custom **Stack**)
- **Join waiting list** when a show is fully booked (custom **Queue**)

### Admin
- Separate Admin login
- Add / Update / Delete movies
- View all bookings across all customers
- View total revenue, total registered users, and per-movie booking statistics

### Engineering
- Full **OOP**: encapsulation, inheritance, polymorphism, abstraction, association,
  aggregation, composition (see Class Diagram above and inline Javadoc comments in the code)
- Hand-written **DSA**: `CustomStack`, `CustomQueue`, linear search, binary search,
  bubble sort, quick sort — no `java.util.Stack`/`Queue`/`Collections.sort` used for these
- Custom checked **exceptions**: `InvalidLoginException`, `SeatAlreadyBookedException`,
  `InvalidPaymentException`, `MovieNotFoundException`, `UserAlreadyExistsException`,
  `NoBookingFoundException`
- Layered architecture: `model` / `service` / `gui` / `util` / `dsa` / `exception`
  cleanly separated, GUI never touches file I/O or business rules directly
- Every operation double-logged: Swing dialogs/screens **and** console (`Logger` utility)

---

## 8. Future Scope

- Replace flat-file storage with an embedded database (e.g. SQLite) while keeping the
  same service-layer interfaces
- Add seat-hold expiry (release seats automatically if payment isn't completed in time)
- Email/SMS ticket delivery (simulated) with QR-code generation for the ticket
- Multi-language UI and theming
- Charts (JFreeChart) for admin revenue/movie statistics
- Automatic waiting-list promotion when a booked seat is cancelled
- Unit tests (JUnit) for the `service` and `dsa` packages

---

## 9. Author Notes for Viva

- Start explaining from `Main.java` → `MainFrame` (CardLayout) → `AppContext` (holds all
  services + session state) → walk through one full booking flow screen by screen.
- Point to `Person` (abstract) for **abstraction/inheritance**, `Payment` hierarchy for
  **polymorphism**, `Screen`/`Seat` for **composition**, `Movie`/`Theatre` for
  **aggregation**, and `Customer`/`Booking` for **association**.
- Point to `dsa/CustomStack.java` and `dsa/CustomQueue.java` to show they're built on a
  plain `Object[]` array, not `java.util` classes.
- Show `data/*.txt` files updating live as you use the app to demonstrate file persistence.
