require('dotenv').config();
var mysql = require('mysql2');
var sha512 = require('js-sha512').sha512;
const util = require('util');

const DB_PW = process.env.MYSQL_ROOT_PASSWORD || 5000;

var con = mysql.createConnection({
  host: "mysqldb",
  database: "example_db",
  user: "root",
  password: DB_PW
});

const query = util.promisify(con.query).bind(con);

con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
});

const express = require('express');

// needed for parsing JSON from request
const app = express();
app.use(express.json());

app.get('/', (req, res) => {
  res.send("Hello from the API!"); 
});

app.post('/register', (req, res) => {
  console.log("register called!");
  let name = req.body.name;
  let password = req.body.password_hash;
  con.beginTransaction(function(err) {
    if (err) { throw err; }
    // first check if user with given name already exists
    con.query('SELECT * FROM User WHERE name=\''+name+'\'', function(err, result) {
      if (err) { 
        con.rollback(function() {
          throw err;
        });
      }
      if (result.length > 0) {
        // user already exists
        console.log("User with name="+name+" already exists!");
        res.send({
          success: false
        });
        return;
      }
      else {
        // create new user
        con.query('INSERT into User (name, password, is_admin) values (\''+name+'\',\''+password+'\', 0)', function (err, result) {
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

app.post('/login', (req, res) => {
  console.log("login called!");
  let name = req.body.name;
  let password = req.body.password_hash;
  con.beginTransaction(function(err) {
    if (err) { throw err; }
    // first check if user with given name already exists
    var query = 'SELECT * FROM User WHERE name=\''+name+'\' AND password=\''+password+'\'';
    console.log(query);
    con.query(query, function(err, result) {
      if (err) { 
        con.rollback(function() {
          throw err;
        });
      }
      if (result.length > 0) {
        // user found
        console.log("login for user with id="+result[0].id+" worked fine!");
        res.send({
          user_id: result[0].id
        });
        return;
      }
      else {
        // user not found or password was wrong
        console.log("login failed -> wrong username or password");
        res.send({
          user_id: -1
        });
        return;
      }
    });
  });
});

app.get('/get-recipes', (req, res) => {
  console.log("get-recipes called!");
  con.beginTransaction(function(err) {
    if (err) { throw err; }
    // select all recipes
    con.query('SELECT r.name as recipe_name, r.preptime_minutes, r.difficulty, r.instruction, r.created_at, u.name as user_name FROM Recipe r JOIN User u on r.creator_id = u.id', function(err, result) {
      if (err) { 
        con.rollback(function() {
          throw err;
        });
      }
      console.log(result);
      var out = [];
      for (var i in result) {
        out.push({
          name: result[i].recipe_name,
          preptime_minutes: result[i].preptime_minutes,
          difficulty: result[i].difficulty,
          instruction: result[i].instruction,
          creation_time: result[i].created_at,
          creator_user: result[i].user_name
        });
      }

      res.send(out);
    });
  });
});

app.post('/add-recipe', async (req, res) => {
  console.log("add-recipe called!");
  let user_id = req.body.user_id;
  let name = req.body.name;
  let preptime_minutes = req.body.preptime_minutes;
  let difficulty = req.body.difficulty;
  let instruction = req.body.instruction;
  let ingredient_names = req.body.ingredient_names;

  const recipe = await query('SELECT * FROM Recipe WHERE name=\''+name+'\'');
  if (recipe.length > 0) {
    // recipe with given name already exists
    console.log("recipe with name="+recipe_result[0].name+" already exists!");
    res.send({
      success: false
    });
    return;
  } else {
    await query('INSERT INTO Recipe (name, preptime_minutes, difficulty, instruction, creator_id) VALUES (\''+name+'\', '+preptime_minutes+', '+difficulty+', \''+instruction+'\', '+user_id+')');
    console.log("inserted recipe");
    for (var i in ingredient_names) {
      // check if ingredient already in db
      const ingredient_result = await query('SELECT * FROM Ingredient WHERE name=\''+ingredient_names[i]+'\'');
      console.log(ingredient_result);
      if (ingredient_result.length == 0) {
        console.log("creaing new ingredient");
        // create new ingredient
        await query('INSERT INTO Ingredient (name) VALUES (\''+ingredient_names[i]+'\')');
      }
    }


    var ingredient_ids = [];
    for (var i in ingredient_names) {
      console.log(ingredient_names[i]);
      var inner_result = await query('SELECT * FROM Ingredient where name=\''+ingredient_names[i]+'\'');
      ingredient_ids.push(inner_result[0].id);
    }

    var recipe_result = await query('SELECT * FROM Recipe WHERE name=\''+name+'\'');
    var recipe_id = recipe_result[0].id;

    var values = "";
    for (var i in ingredient_ids) {
      values += '('+ingredient_ids[i]+', '+recipe_id+', null),';
    }
    values = values.substring(0, values.length-1);
    console.log(values);
    await query('INSERT INTO RecipeIngredient (ingredient_id, recipe_id, amount) VALUES '+values);
    res.send({
      success: true
    });
    return;
  }
})

const port = process.env.NODEJS_LOCAL_PORT || 3000;
app.listen(port, () => {
	console.log(`Worker: process ${process.pid} is up on port ${port}`);
});
