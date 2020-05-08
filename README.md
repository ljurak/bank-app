# Bank app

Console app simulating bank operations. It enables creating accounts, performing money operations,
checking card numbers validity. Application uses SQLite as data storage.

## How to start

```bash
git clone https://github.com/ljurak/bank-app.git
cd bank-app
mvn clean package
cd target
java -jar bank-app.jar -filename file_with_database (e.g. java -jar bank-app.jar -filename data.db)
```