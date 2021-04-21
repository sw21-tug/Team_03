require('dotenv').config();
var mysql = require('mysql2');
var sha512 = require('js-sha512').sha512;

const DB_PW = process.env.MYSQL_ROOT_PASSWORD || 5000;

var con = mysql.createConnection({
  host: "mysqldb",
  database: "example_db",
  user: "root",
  password: DB_PW
});

con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
});

const express = require('express');

const app = express();

app.use(express.json());

app.get('/', (req, res) => {
  res.send("Hello from the API!"); 
});

app.post('/register', (req, res) => {
  console.log("register called!");
  console.log(req.body.username);
  console.log(req.body.plain_pw);
  let name = req.body.username;
  let password = req.body.plain_pw;
  con.beginTransaction(function(err) {
    if (err) { throw err; }
    // first check if user with given name already exists
    con.query('SELECT * FROM User WHERE name=\'?\'', name, function(err, result) {
      if (err) { 
        con.rollback(function() {
          throw err;
        });
      }
      console.log(result);
      if (result.length > 0) {
        // user already exists
        console.log("User with name="+name+" already exists!");
        res.send({
          success: false
        });
        return;
      }
      else {
        console.log(name);
        console.log(password);
        var que = 'INSERT into User (name, password) values (\''+name+'\',\''+password+'\')';
        console.log(que);
        // create new user
        con.query(que, function (err, result) {
          // check if mysql throws an error
          if (err) {
            res.send({
              success: false
            });
            return;
          }

          // success!
          res.send({
            success: true
          });

          return;
        });
      }
    });
  });
});

const port = process.env.NODEJS_LOCAL_PORT || 3000;
app.listen(port, () => {
	console.log(`Worker: process ${process.pid} is up on port ${port}`);
});
