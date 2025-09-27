import java.util.*;

// -------- MOVIE BOOKING (PER SCREEN) --------
class MovieBooking {
    String name;
    int rows, cols;
    int availableSeats;
    boolean[][] seats;
    String showTime;
    int ticketsBooked;
    int basePrice; // base price
    int revenue;

    MovieBooking(String name, int rows, int cols, String showTime, int basePrice) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.availableSeats = rows * cols;
        this.seats = new boolean[rows][cols];
        this.showTime = showTime;
        this.ticketsBooked = 0;
        this.basePrice = basePrice;
        this.revenue = 0;
    }

    void displaySeats() {
        System.out.println("\n🪑 Seat Layout for " + name + " (" + showTime + "):");
        for (int r = 0; r < rows; r++) {
            char rowChar = (char) ('A' + r);
            System.out.print(rowChar + ": ");
            for (int c = 0; c < cols; c++) {
                String seat = rowChar + "" + (c + 1);
                if (seats[r][c]) System.out.print("[❌] ");
                else System.out.print("[🎟️" + seat + "] ");
            }
            System.out.println();
        }
        System.out.println("💡 Example: Enter seats as A1, B2, etc. Type 'back' to return.");
    }

    boolean isValidSeat(String seatName) {
        if (seatName.length() < 2) return false;
        char rowChar = seatName.charAt(0);
        int row = rowChar - 'A';
        try {
            int col = Integer.parseInt(seatName.substring(1)) - 1;
            return row >= 0 && row < rows && col >= 0 && col < cols;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean isSeatBooked(String seatName) {
        char rowChar = seatName.charAt(0);
        int row = rowChar - 'A';
        int col = Integer.parseInt(seatName.substring(1)) - 1;
        return seats[row][col];
    }

    int bookSeat(String seatName) {
        char rowChar = seatName.charAt(0);
        int row = rowChar - 'A';
        int col = Integer.parseInt(seatName.substring(1)) - 1;
        seats[row][col] = true;
        availableSeats--;
        ticketsBooked++;
        revenue += basePrice;
        return basePrice;
    }

    void cancelSeat(String seatName) {
        char rowChar = seatName.charAt(0);
        int row = rowChar - 'A';
        int col = Integer.parseInt(seatName.substring(1)) - 1;
        seats[row][col] = false;
        availableSeats++;
        ticketsBooked--;
    }
}

// -------- SCREEN --------
class Screen {
    int screenNumber;
    MovieBooking movie;

    Screen(int screenNumber, MovieBooking movie) {
        this.screenNumber = screenNumber;
        this.movie = movie;
    }
}

// -------- THEATRE --------
class Theatre {
    String name;
    ArrayList<Screen> screens;

    Theatre(String name) {
        this.name = name;
        this.screens = new ArrayList<>();
    }

    void addScreen(Screen s) {
        for (Screen sc : screens) {
            if (sc.screenNumber == s.screenNumber) {
                System.out.println("⚠️ Screen already has a movie assigned!");
                return;
            }
        }
        screens.add(s);
    }
}

// -------- SNACK --------
class Snack {
    String name;
    int smallPrice, mediumPrice, largePrice;

    Snack(String name, int s, int m, int l) {
        this.name = name;
        this.smallPrice = s;
        this.mediumPrice = m;
        this.largePrice = l;
    }
}

// -------- USER --------
class User {
    String username;
    String password;
    ArrayList<String> tickets;
    int moneySpent;

    User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tickets = new ArrayList<>();
        this.moneySpent = 0;
    }
}

// -------- MAIN SYSTEM --------
public class MultiTheatreBookingSystem {
    private static ArrayList<Theatre> theatres = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Snack> snacks = new ArrayList<>();
    private static int snackRevenue = 0;
    private static Scanner sc = new Scanner(System.in);

    // ----------- ADMIN FUNCTIONS -----------
    private static void adminMenu() {
        while (true) {
            System.out.println("\n===== 🛠️ ADMIN MENU =====");
            System.out.println("1. ➕ Add Theatre");
            System.out.println("2. 🗑️ Delete Theatre");
            System.out.println("3. ➕ Add Screen & Movie");
            System.out.println("4. 🗑️ Delete Screen/Movie");
            System.out.println("5. 🎭 Show Theatres");
            System.out.println("6. ➕ Add Snack");
            System.out.println("7. 🗑️ Delete Snack");
            System.out.println("8. 📊 View Revenue");
            System.out.println("9. 🔍 Search Theatre/Movie");
            System.out.println("10. 🔙 Back");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addTheatre();
                case 2 -> deleteTheatre();
                case 3 -> addScreenWithMovie();
                case 4 -> deleteScreen();
                case 5 -> showTheatres();
                case 6 -> addSnack();
                case 7 -> deleteSnack();
                case 8 -> viewRevenue();
                case 9 -> searchTheatreMovieAdmin();
                case 10 -> { return; }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    private static void addTheatre() {
        System.out.print("🎭 Enter Theatre Name (or 'back' to return): ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("back")) return;
        theatres.add(new Theatre(name));
        System.out.println("✅ Theatre added! 🎉");
    }

    private static void deleteTheatre() {
        if (theatres.isEmpty()) { System.out.println("⚠️ No theatres!"); return; }
        showTheatres();
        System.out.print("🗑️ Enter theatre index to delete (or 0 to go back): ");
        int idx = sc.nextInt();
        sc.nextLine();
        if (idx == 0) return;
        if (idx < 1 || idx > theatres.size()) { System.out.println("❌ Invalid!"); return; }
        theatres.remove(idx - 1);
        System.out.println("❌ Theatre deleted! 🗑️");
    }

    private static void addScreenWithMovie() {
        if (theatres.isEmpty()) { System.out.println("⚠️ No theatres!"); return; }
        showTheatres();
        System.out.print("🎬 Select Theatre index (or 0 to go back): ");
        int idx = sc.nextInt();
        sc.nextLine();
        if (idx == 0) return;
        if (idx < 1 || idx > theatres.size()) { System.out.println("❌ Invalid!"); return; }
        Theatre t = theatres.get(idx - 1);
        addMovieToTheatre(t);
    }

    private static void addMovieToTheatre(Theatre t) {
        System.out.print("🎦 Enter screen number: ");
        int screenNo = sc.nextInt();
        sc.nextLine();
        System.out.print("🎬 Enter movie name: ");
        String movieName = sc.nextLine();
        System.out.print("🪑 Enter rows of seats: ");
        int rows = sc.nextInt();
        System.out.print("🪑 Enter cols of seats: ");
        int cols = sc.nextInt();
        sc.nextLine();
        System.out.print("⏰ Enter show time: ");
        String time = sc.nextLine();
        System.out.print("💸 Enter ticket price: ");
        int price = sc.nextInt();
        sc.nextLine();

        MovieBooking movie = new MovieBooking(movieName, rows, cols, time, price);
        t.addScreen(new Screen(screenNo, movie));
        System.out.println("✅ Screen with movie added! 🎬");
    }

    private static void deleteScreen() {
        if (theatres.isEmpty()) { System.out.println("⚠️ No theatres!"); return; }
        showTheatres();
        System.out.print("🗑️ Enter theatre index (or 0 to go back): ");
        int tidx = sc.nextInt();
        sc.nextLine();
        if (tidx == 0) return;
        if (tidx < 1 || tidx > theatres.size()) { System.out.println("❌ Invalid!"); return; }
        Theatre t = theatres.get(tidx - 1);

        if (t.screens.isEmpty()) { System.out.println("⚠️ No screens in theatre!"); return; }
        System.out.print("🗑️ Enter screen number to delete (or 0 to go back): ");
        int sNo = sc.nextInt();
        sc.nextLine();
        if (sNo == 0) return;

        t.screens.removeIf(s -> s.screenNumber == sNo);
        System.out.println("❌ Screen deleted! 🗑️");
    }

    private static void addSnack() {
        System.out.print("🍿 Snack name (or 'back' to return): ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("back")) return;
        System.out.print("🥤 Small price: ");
        int s = sc.nextInt();
        System.out.print("🥤 Medium price: ");
        int m = sc.nextInt();
        System.out.print("🥤 Large price: ");
        int l = sc.nextInt();
        sc.nextLine();
        snacks.add(new Snack(name, s, m, l));
        System.out.println("✅ Snack added! 🍿");
    }

    private static void deleteSnack() {
        if (snacks.isEmpty()) { System.out.println("⚠️ No snacks!"); return; }
        showSnacks();
        System.out.print("🗑️ Enter snack index to delete (or 0 to go back): ");
        int idx = sc.nextInt();
        sc.nextLine();
        if (idx == 0) return;
        if (idx < 1 || idx > snacks.size()) { System.out.println("❌ Invalid!"); return; }
        snacks.remove(idx - 1);
        System.out.println("❌ Snack deleted! 🗑️");
    }

    private static void viewRevenue() {
        System.out.println("\n📊 Revenue Report:");
        int total = snackRevenue;
        for (Theatre t : theatres) {
            for (Screen s : t.screens) {
                total += s.movie.revenue;
                System.out.println("🎬 Movie: " + s.movie.name + " | Revenue: ₹" + s.movie.revenue);
            }
        }
        System.out.println("🍿 Snacks Revenue: ₹" + snackRevenue);
        System.out.println("💰 TOTAL Revenue: ₹" + total);
    }

    private static void showTheatres() {
        if (theatres.isEmpty()) { System.out.println("⚠️ No theatres!"); return; }
        System.out.println("\n🎭 Theatres:");
        for (int i = 0; i < theatres.size(); i++) {
            Theatre t = theatres.get(i);
            System.out.println((i + 1) + ". " + t.name + " (" + t.screens.size() + " screens)");
            for (Screen s : t.screens) {
                System.out.println("   - 🎬 Screen " + s.screenNumber + ": " + s.movie.name +
                        " | ⏰ " + s.movie.showTime + " | 🪑 Seats Left: " + s.movie.availableSeats);
            }
        }
    }

    private static void showSnacks() {
        if (snacks.isEmpty()) { System.out.println("⚠️ No snacks!"); return; }
        System.out.println("\n🍿 Snacks Menu:");
        for (int i = 0; i < snacks.size(); i++) {
            Snack sn = snacks.get(i);
            System.out.println((i + 1) + ". " + sn.name +
                    " | 🥤 Small: ₹" + sn.smallPrice +
                    " | 🥤 Medium: ₹" + sn.mediumPrice +
                    " | 🥤 Large: ₹" + sn.largePrice);
        }
    }

    // -------- ADMIN SEARCH & ADD MOVIE --------
    private static void searchTheatreMovieAdmin() {
        System.out.print("🔍 Search by 1.Movie 2.Theatre (0 to back): ");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice == 0) return;

        System.out.print("🔎 Enter search query: ");
        String query = sc.nextLine();
        boolean found = false;

        for (Theatre t : theatres) {
            if (choice == 2 && t.name.toLowerCase().contains(query.toLowerCase())) {
                System.out.println("🎭 Theatre found: " + t.name);
                found = true;
                System.out.print("➕ Do you want to add a movie to this theatre? (yes/no): ");
                String ans = sc.nextLine();
                if (ans.equalsIgnoreCase("yes")) {
                    addMovieToTheatre(t);
                }
            }
            for (Screen s : t.screens) {
                if (choice == 1 && s.movie.name.toLowerCase().contains(query.toLowerCase())) {
                    System.out.println("🎬 Movie found: " + s.movie.name + " in Theatre: " + t.name);
                    found = true;
                }
            }
        }

        if (!found) System.out.println("⚠️ No results found!");
    }

    // -------- USER MENU --------
    private static void userMenu(User currentUser) {
        while (true) {
            System.out.println("\n===== 👤 USER MENU (" + currentUser.username + ") =====");
            System.out.println("1. 🎭 Show Theatres & Movies");
            System.out.println("2. 🎟️ Book Tickets");
            System.out.println("3. ❌ Cancel Ticket");
            System.out.println("4. 🍿 Buy Snacks");
            System.out.println("5. 🔍 Search Theatre/Movie");
            System.out.println("6. 🔙 Back");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> showTheatres();
                case 2 -> bookTickets(currentUser);
                case 3 -> cancelTicket(currentUser);
                case 4 -> buySnacks(currentUser);
                case 5 -> searchTheatreMovie(currentUser, true);
                case 6 -> { return; }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // -------- USER SEARCH & BOOK --------
    private static void searchTheatreMovie(User currentUser, boolean allowBooking) {
        System.out.print("🔍 Search by 1.Movie 2.Theatre (0 to back): ");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice == 0) return;

        System.out.print("🔎 Enter search query: ");
        String query = sc.nextLine();
        boolean found = false;
        ArrayList<Screen> matchedScreens = new ArrayList<>();

        for (Theatre t : theatres) {
            if (choice == 2 && t.name.toLowerCase().contains(query.toLowerCase())) {
                System.out.println("\n🎭 Theatre found: " + t.name);
                for (Screen s : t.screens) {
                    System.out.println("   - 🎬 Screen " + s.screenNumber + ": " + s.movie.name +
                            " | ⏰ " + s.movie.showTime + " | 🪑 Seats Left: " + s.movie.availableSeats);
                    matchedScreens.add(s);
                }
                found = true;
            }
            for (Screen s : t.screens) {
                if (choice == 1 && s.movie.name.toLowerCase().contains(query.toLowerCase())) {
                    System.out.println("\n🎬 Movie found: " + s.movie.name +
                            " in Theatre: " + t.name +
                            " | Screen " + s.screenNumber +
                            " | ⏰ " + s.movie.showTime +
                            " | 🪑 Seats Left: " + s.movie.availableSeats);
                    matchedScreens.add(s);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("⚠️ No results found!");
            return;
        }

        if (allowBooking && !matchedScreens.isEmpty()) {
            System.out.print("\n🎟️ Do you want to book tickets from search results? (yes/no): ");
            String ans = sc.nextLine();
            if (ans.equalsIgnoreCase("yes")) {
                if (matchedScreens.size() == 1) {
                    bookTicketsFromScreen(currentUser, matchedScreens.get(0));
                } else {
                    System.out.println("Choose which screen to book from:");
                    for (int i = 0; i < matchedScreens.size(); i++) {
                        Screen s = matchedScreens.get(i);
                        System.out.println((i + 1) + ". " + s.movie.name +
                                " | ⏰ " + s.movie.showTime +
                                " | 🎭 Theatre: " + findTheatreOfScreen(s).name);
                    }
                    int idx = sc.nextInt();
                    sc.nextLine();
                    if (idx >= 1 && idx <= matchedScreens.size()) {
                        bookTicketsFromScreen(currentUser, matchedScreens.get(idx - 1));
                    }
                }
            }
        }
    }

    private static void bookTicketsFromScreen(User user, Screen chosen) {
        MovieBooking movie = chosen.movie;
        movie.displaySeats();
        System.out.print("🎟️ How many seats? (0 to go back): ");
        int count = sc.nextInt();
        sc.nextLine();
        if (count == 0) return;
        int totalCost = 0;
        Theatre t = findTheatreOfScreen(chosen);

        for (int i = 0; i < count; i++) {
            System.out.print("Enter seat name (or 'back' to stop): ");
            String seatName = sc.nextLine().toUpperCase();
            if (seatName.equalsIgnoreCase("back")) break;
            if (!movie.isValidSeat(seatName)) { System.out.println("⚠️ Invalid seat!"); i--; continue; }
            if (movie.isSeatBooked(seatName)) { System.out.println("❌ Already booked!"); i--; continue; }
            int price = movie.bookSeat(seatName);
            totalCost += price;
            String ticketInfo = "🎟️ " + t.name + " | Screen " + chosen.screenNumber +
                    " | " + movie.name + " | Seat: " + seatName +
                    " | ⏰ " + movie.showTime + " | ₹" + price;
            user.tickets.add(ticketInfo);
            user.moneySpent += price;
            System.out.println("✅ " + ticketInfo);
        }
        System.out.println("💸 Total Cost: ₹" + totalCost);
    }

    private static Theatre findTheatreOfScreen(Screen s) {
        for (Theatre t : theatres) if (t.screens.contains(s)) return t;
        return null;
    }

    // -------- USER BOOKING --------
    private static void bookTickets(User user) {
        if (theatres.isEmpty()) { System.out.println("⚠️ No theatres!"); return; }
        showTheatres();
        System.out.print("🎭 Select Theatre index (or 0 to go back): ");
        int tidx = sc.nextInt();
        sc.nextLine();
        if (tidx == 0) return;
        if (tidx < 1 || tidx > theatres.size()) { System.out.println("❌ Invalid!"); return; }
        Theatre t = theatres.get(tidx - 1);
        if (t.screens.isEmpty()) { System.out.println("⚠️ No movies!"); return; }
        System.out.print("🎬 Select screen number (or 0 to go back): ");
        int sNo = sc.nextInt();
        sc.nextLine();
        if (sNo == 0) return;
        Screen chosen = null;
        for (Screen s : t.screens) if (s.screenNumber == sNo) chosen = s;
        if (chosen == null) { System.out.println("❌ Invalid!"); return; }
        bookTicketsFromScreen(user, chosen);
    }

    // -------- CANCEL TICKET --------
    private static void cancelTicket(User user) {
        if (user.tickets.isEmpty()) { System.out.println("⚠️ No tickets!"); return; }
        System.out.println("\n🎟️ Your Tickets:");
        for (int i = 0; i < user.tickets.size(); i++) {
            System.out.println((i + 1) + ". " + user.tickets.get(i));
        }
        System.out.print("❌ Enter ticket index to cancel (or 0 to go back): ");
        int idx = sc.nextInt();
        sc.nextLine();
        if (idx == 0) return;
        if (idx < 1 || idx > user.tickets.size()) { System.out.println("❌ Invalid!"); return; }

        String ticket = user.tickets.remove(idx - 1);
        System.out.println("❌ Cancelled: " + ticket);
    }

    // -------- SNACK PURCHASE --------
    private static void buySnacks(User user) {
        if (snacks.isEmpty()) { System.out.println("⚠️ No snacks!"); return; }
        showSnacks();
        System.out.print("🍿 Enter snack index (or 0 to go back): ");
        int idx = sc.nextInt();
        sc.nextLine();
        if (idx == 0) return;
        if (idx < 1 || idx > snacks.size()) { System.out.println("❌ Invalid!"); return; }
        Snack sn = snacks.get(idx - 1);

        System.out.print("🥤 Size (small/medium/large): ");
        String size = sc.nextLine().toLowerCase();
        int price = switch (size) {
            case "small" -> sn.smallPrice;
            case "medium" -> sn.mediumPrice;
            case "large" -> sn.largePrice;
            default -> 0;
        };
        if (price == 0) { System.out.println("❌ Invalid size!"); return; }
        snackRevenue += price;
        user.moneySpent += price;
        System.out.println("✅ Bought " + size + " " + sn.name + " for ₹" + price + " 🍿");
    }

    // -------- AUTH --------
    private static User loginUser() {
        System.out.print("👤 Enter username: ");
        String u = sc.nextLine();
        System.out.print("🔒 Enter password: ");
        String p = sc.nextLine();
        for (User user : users) {
            if (user.username.equals(u) && user.password.equals(p)) return user;
        }
        System.out.println("⚠️ Invalid login!");
        return null;
    }

    private static void registerUser() {
        System.out.print("👤 Choose username: ");
        String u = sc.nextLine();
        System.out.print("🔒 Choose password: ");
        String p = sc.nextLine();
        users.add(new User(u, p));
        System.out.println("✅ User registered! 👤");
    }

    // -------- MAIN --------
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== 🎟️ MULTI-THEATRE BOOKING 🎟️ =====");
            System.out.println("1. 🛠️ Admin");
            System.out.println("2. 👤 User");
            System.out.println("3. 🚪 Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> {
                    System.out.println("1. 🔑 Login  2. 📝 Register  3. 🔙 Back");
                    int c = sc.nextInt();
                    sc.nextLine();
                    if (c == 1) {
                        User currentUser = loginUser();
                        if (currentUser != null) userMenu(currentUser);
                    } else if (c == 2) {
                        registerUser();
                    }
                }
                case 3 -> { System.out.println("Exiting... 👋"); return; }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
}