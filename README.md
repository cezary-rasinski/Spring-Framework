## [Task 2](CarRental2/)

- **Projekt**  
  Utwórz nowy projekt Maven i skopiuj do niego kod źródłowy z poprzednich zajęć.

- **Interfejsy i przygotowanie danych**  
  - Plik CSV (samodzielnie przygotowany) z danymi pojazdów i użytkowników.  
  - Dodaj interfejs `IVehicleRepository` z metodami:
    - `addVehicle(Vehicle v)`
    - `removeVehicle(String id)`
    - `getVehicle(String id)`  
  - Dodaj interfejs `IUserRepository` z metodami:
    - `getUser(String login)`
    - `getUsers()`
    - `save(User u)`

- **Modele**  
  - **User**: `login`, `password` (hash SHA-256), `role`, `rentedVehicle` (odwołanie do `Vehicle`)  
  - **Vehicle**: jak dotychczas

- **Autoryzacja**  
  - Klasa `Authentication` z metodą weryfikującą dane logowania.  
  - Wdrożenie logowania użytkownika.

- **Funkcjonalność**  
  - **User**: logowanie, wypożyczenie pojazdu, zwrot, podgląd własnych danych i wypożyczonego pojazdu.  
  - **Admin**: dodawanie/usuwanie pojazdów, przeglądanie listy pojazdów, podgląd listy użytkowników wraz z ich wypożyczeniami.

> *Uwaga:* Aplikacja działa jednolitym modułem (bez podziału na klient/serwer).

## [Zadanie 3](CarRental3/)

- **Modele domenowe:**  
  - Przebudować klasy `Vehicle` i `User`  
  - Dodać klasę `Rental` reprezentującą wypożyczenie pojazdu

- **Repozytoria:**  
  - Zaimplementować `VehicleRepository`, `UserRepository` i `RentalRepository` (CRUD + własne metody)

- **Autoryzacja (`AuthService`):**  
  - Przenieść logikę logowania i rejestracji do `AuthService`  
  - Użyć BCrypt (jbcrypt) do hashowania i weryfikacji haseł

- **Logika wypożyczeń:**  
  - Serwisy obsługujące wypożyczanie i zwracanie pojazdów przy pomocy nowych repozytoriów  
  - **Admin:** dodawanie/usuwanie pojazdów, przeglądanie wszystkich pojazdów (w tym wypożyczonych)  
  - **Użytkownik:** przeglądanie tylko dostępnych pojazdów, wypożyczenie i zwrot pojazdu

- **Warstwa serwisów:**  
  - Opakować dostęp do repozytoriów w serwisy (np. `VehicleService`, `UserService`, `RentalService`)  
  - Zaproponować sygnatury metod dla serwisów (np. `listAvailable()`, `rentVehicle()`, `deleteUser()`)


## [Task 4](CarRental4/)
- **Konto Neon**: załóż na neon.tech  
- **Baza danych**: utwórz tabele wg. `init.sql`, uzupełnij danymi  
- **Szablon aplikacji**: 
  `https://github.com/umcsuser/jdbstudent.git`

-[Task 5](CarRentalHibernate/)
- **Konfiguracja Hibernate**  
  Stwórz klasę konfiguracyjną Hibernate (SessionFactory, ustawienia połączenia z bazą).

- **Encje JPA**  
  Oznacz klasy `Vehicle`, `Rental` i `User` jako `@Entity`, z mapowaniem pól na kolumny.

- **Interfejsy repozytoriów**  
  Wyodrębnij interfejsy dla repozytoriów (VehicleRepository, RentalRepository, UserRepository) z metodami otwierania/zamykania sesji.

- **Implementacja Hibernate**  
  Zaimplementuj repozytoria używające Hibernate (SessionFactory → Session → CRUD).

- **Integracja z serwisami**  
  Zmodyfikuj istniejące serwisy, aby korzystały z nowych repozytoriów Hibernate przy obsłudze wypożyczeń pojazdów.

## [Task 6](lb6/)

- **Serwisy:** zaimplementuj interfejsy `VehicleService` i `RentalService`  
  - `VehicleService`: `findAll()`, `findAllActive()`, `findById()`, `save()`, `findAvailableVehicles()`, `findRentedVehicles()`, `isAvailable()`, `deleteById()` (soft delete)  
  - `RentalService`: `isVehicleRented()`, `findActiveRentalByVehicleId()`, `rent()`, `returnRental()`, `findAll()`

- **Kontrolery:** stwórz endpointy i użyj serwisów do:  
  - Dodawania pojazdów  
  - Usuwania pojazdów (soft delete)  
  - Wypożyczania i zwracania pojazdów  
  - Przeglądania listy pojazdów i wypożyczeń

> **Uwaga:** Bez logowania, rejestracji i podziału na role w tej części zadania.


## [Task 7](lb7/)

- **Logowanie & zabezpieczenie**  
  Dodaj Spring Security + JWT, zabezpiecz wszystkie endpointy.

- **Wypożyczenia & zwroty**  
  Użyj ID zalogowanego użytkownika do operacji rent/return; admin może dodawać pojazdy i robić soft-delete.

- **Zarządzanie użytkownikami & rolami**  
  - Implementacji wielu ról użytkownika (wiele do wielu) + rejestracja nowych użytkowników  
  - Admin → soft-delete user (blokada dostępu), lista userów, nadawanie/odbieranie ról

- **Płatności & wycena**  
  Integracja Stripe (testowe płatności) oraz metoda obliczająca bieżącą cenę wypożyczenia  

