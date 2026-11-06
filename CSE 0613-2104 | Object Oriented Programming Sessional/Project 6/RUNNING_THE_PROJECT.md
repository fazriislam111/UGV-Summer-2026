# Running the Project

This project requires Java 25, Maven, and a running MySQL server. Run the commands below from a terminal in the project folder (the folder containing `pom.xml`).

## 1. Install the prerequisites

### macOS (Apple silicon, including M1/M2/M3/M4)

If Homebrew is not installed, install it from [brew.sh](https://brew.sh). Then run:

```bash
brew install openjdk@25 maven mysql@8.4
brew services start mysql@8.4
```

Set Java 25 for the current terminal:

```bash
export JAVA_HOME="$(brew --prefix openjdk@25)/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$(brew --prefix mysql@8.4)/bin:$PATH"
```

### Windows

Install the following:

- JDK 25 (for example, Microsoft Build of OpenJDK or Eclipse Temurin)
- Apache Maven
- MySQL Server 8.x

During installation, configure Java and Maven on `PATH` if the installers offer that option. Start the MySQL service from Windows Services if it is not already running.

Open a new PowerShell window and verify the tools:

```powershell
java -version
mvn -version
mysql --version
```

The Java and Maven version output should show Java 25. If Java 25 is installed but not selected, set `JAVA_HOME` to its actual installation directory and add its `bin` folder to `PATH` for the current PowerShell window:

```powershell
$env:JAVA_HOME = "C:\Path\To\Your\JDK-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

Replace the example path with the JDK 25 location on your computer.

## 2. Open the project folder

Change directory to the project folder, the one containing `pom.xml`.

macOS:

```bash
cd "/path/to/Project 6"
```

Windows PowerShell:

```powershell
cd "C:\path\to\Project 6"
```

Replace the example path with the folder where you saved or cloned the project.

## 3. Create the database

Run the included SQL setup script. It creates the `barishal_launch_ghat` database and its `vessels` table.

macOS:

```bash
mysql -u root -p < database/setup.sql
```

Windows PowerShell:

```powershell
Get-Content -Raw database/setup.sql | mysql -u root -p
```

Enter the MySQL password when prompted. If your MySQL account uses a different username, replace `root` with that username.

## 4. Configure the database connection

Create a local configuration file from the example.

macOS:

```bash
cp db.properties.example db.properties
```

Windows PowerShell:

```powershell
Copy-Item db.properties.example db.properties
```

Edit `db.properties` and set `db.user` and `db.password` to your MySQL account credentials. Leave `db.url` pointing to `barishal_launch_ghat` on your MySQL host and port (the default is `localhost:3306`). Keep this local file private; it is excluded from Git. Do not put a real password in `db.properties.example` or share it publicly. Alternatively, the app supports `DB_URL`, `DB_USER`, and `DB_PASSWORD` environment variables.

## 5. Build and run

From the project folder, run:

```bash
mvn clean compile exec:java
```

Maven downloads the required MySQL Connector/J dependency the first time. The vessel manager window should open. Keep the terminal open while using the app; press `Ctrl+C` in the terminal to stop it.

## Troubleshooting

- **`mvn` or `mysql` is not recognized/not found:** Install the tool and ensure it is on `PATH`, then open a new terminal.
- **Unsupported class file or Java release error:** Check `java -version` and `mvn -version`; both should use Java 25.
- **Unknown database:** Confirm the MySQL server is running, then run `database/setup.sql` as shown above.
- **Access denied:** Check the MySQL username/password in `db.properties`.
- **Communications link failure:** Ensure MySQL is running locally on port `3306`, or update `db.url` to match your server.
