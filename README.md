# KLogger - Simple Logging Utility for Java

KLogger is a straightforward and efficient logging utility designed for Java applications. It provides a range of functionalities to help developers log messages with varying severity levels, manage log file configurations, and handle exceptions gracefully.

## Features
- **Logging Messages:** Log messages with different severity levels such as `INFO`, `DEBUG`, `WARNING`, `ERROR`, and `CRITICAL`.
- **Debug Mode:** Enable or disable debug mode to control the verbosity of logs.
- **Append Mode:** Choose whether to append logs to an existing file or overwrite it.
- **Configuration:** Set the log file path and format the log messages with custom date patterns.
- **Exception Handling:** Log exceptions with detailed stack traces and terminate the program on critical errors.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Usage

#### Installation
Before using KLog, you need to set the log file path and configure the logger:

```java
    KLog.setOutPut("<path>/logfile.log");
```

---

#### Logging Messages
You can log messages with different severity levels as follows:

```java
    KLog.log("This is an informational message.");
    KLog.debug("This is a debug message.");
    KLog.warning("This is a warning message.");
    KLog.error("This is an error message.");
    KLog.critical("This is a critical error message.");
```

---

#### Enabling Debug Mode
To enable debug mode, use the following method:

```java
    KLog.setDebugMode(true);
```

---

#### Logging Exceptions
You can log exceptions with detailed stack traces:

```java
    try {
        // Your code here
    } catch (Exception e) {
        KLog.error("An error occurred", e);
    }
```

For critical errors that require program termination:

```java
    try {
    // Your code here
    } catch (Exception e) {
        KLog.critical("A critical error occurred", e);
    }
```

---

### Configuration

#### Setting the Log File Path
You can set the log file path using the setOutPut method:

```java
    KLog.setOutPut("<path>/exemple.log");
```

---

#### Loading Configuration from a File
You can load the logger configuration from a `JSON` file using the setConfig method:

```java
    try {
        KLog.setConfig("<path>/config.json");
    } catch (IOException e) {
        e.printStackTrace();
    }
```

---

### Custom Format
You can customize the format of several aspects of the logging output:

- **Date Format:** Customize the date format used in log entries.
- **File Log Format:** Customize the format of log messages written to the log file.
- **Console Log Format:** Customize the format of log messages displayed in the console.

---

#### Customizing the Date Format
To customize the date format, you need to modify the dateFormat property in the LogConfig class. The default date format is `"yyyy-MM-dd HH:mm:ss"`. You can change it to any valid date format pattern supported by Java's DateTimeFormatter.

---

#### Customizing the File Log Format
To customize the format of log messages written to the log file, you need to modify the fileFormat property in the LogConfig class. The default file log format is `"{date} [{type}][{context}]: {message}"`. You can use placeholders like {date}, {type}, {context}, and {message} to define your custom format.

---

#### Customizing the Console Log Format
To customize the format of log messages displayed in the console, you need to modify the consoleFormat property in the LogConfig class. The default console log format is `"[{type}][{context}]: {message}"`. You can use placeholders like {type}, {context}, and {message} to define your custom format.

---

#### Placeholders in Configuration File
You can customize the format of several aspects of the logging output using specific placeholders. The available placeholders are:

- **`{context}`:** The context in which the log message was generated (e.g., class name and method name).
- **`{type}`:** The type of log message (e.g., INFO, DEBUG, WARNING, ERROR, CRITICAL).
- **`{date}`:** The date and time when the log message was generated, formatted according to the specified date format.
- **`{message}`:** The actual log message content.

---

#### Example Configuration File in JSON
You can configure the KLog utility using a JSON configuration file. Below is an example of what the configuration file might look like:

```json
    {
        "debug": true,
        "append": false,
        "formats": {
            "date": "yyyy-MM-dd HH:mm:ss",
            "file": "{date} [{type}][{context}]: {message}",
            "console": "[{type}][{context}]: {message}"
        },
        "colors": {
            "INFO":     "#00AAFF",
            "DEBUG":    "#AAAAAA",
            "WARNING":  "#FFFF00",
            "ERROR":    "#FF2222",
            "CRITICAL": "#FF00FF",
            "context":  "#AAAAAA",
            "date":     "#00AA00",
            "message":  "#FFFFFF"
        }
    }
```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.