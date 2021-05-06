require('dotenv').config();
var mysql = require('mysql2');
var sha512 = require('js-sha512').sha512;
const util = require('util');

const DB_USER = process.env.MYSQL_USER || "root";
const DB_PW = process.env.MYSQL_PASSWORD || "root";
const DB_NAME = process.env.MYSQL_DATABASE || "example_db";
const DB_SERVER = process.env.MYSQL_SERVER || "mysqldb";

function connectDB() {
  var con = mysql.createConnection({
    host: DB_SERVER,
    database: DB_NAME,
    user: DB_USER,
    password: DB_PW
  });

  con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
  });

  return con;
}



const express = require('express');
const { runInNewContext } = require('vm');

// needed for parsing JSON from request
const app = express();
app.use(express.json());

app.post('/register', async (req, res) => {
  console.log("register called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let name = req.body.name;
  let password = req.body.password_hash;
  // first check if user with given name already exists
  var result = await query('SELECT * FROM User WHERE name=\''+name+'\'');
  if (result.length > 0) {
    // user already exists
    console.log("User with name="+name+" already exists!");
    res.send({
      success: false,
      message: "Username "+name+" already exists"
    });
    return;
  }
  else {
    // create new user
    await query('INSERT into User (name, password, is_admin) values (\''+name+'\',\''+password+'\', 0)');
    
    // success!
    res.send({
      success: true,
      message: null
    });
    return;
  }
});

app.post('/login', async (req, res) => {
  console.log("login called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let name = req.body.name;
  let password = req.body.password_hash;
    // first check if user with given name already exists
    var result = await query('SELECT * FROM User WHERE name=\''+name+'\' AND password=\''+password+'\''); 
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

app.post('/get-ingredients', async (req, res) => {
  console.log("get-ingredients called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  // select all recipes
  var result = await query('SELECT * FROM Ingredient');
  
  var out = [];
  for (var i in result) {
    out.push({
      name: result[i].name,
    });
  }

  var finalJson = { "ingredients": out };
  res.send(finalJson);
});

app.post('/get-recipes', async (req, res) => {
  console.log("get-recipes called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  // select all recipes
  var result = await query('SELECT r.id, r.name as recipe_name, r.preptime_minutes, r.difficulty, r.instruction, r.created_at, u.name as user_name, SUM(r2.value) / SUM(1) as rating FROM Recipe r JOIN User u on r.creator_id = u.id LEFT JOIN Rating r2 on r.id = r2.recipe_id GROUP BY r.id, r.name, r.preptime_minutes, r.difficulty, r.instruction, r.created_at, u.name');
  
  var out = [];
  for (var i in result) {
    out.push({
      id: result[i].id,
      name: result[i].recipe_name,
      preptime_minutes: result[i].preptime_minutes,
      difficulty: result[i].difficulty,
      instruction: result[i].instruction,
      creation_time: result[i].created_at,
      creator_user: result[i].user_name,
      rating: result[i].rating
    });
  }

  var finalJson = { "recipes": out };
  res.send(finalJson);
});

app.post('/get-recipe', async (req, res) => {
  console.log("get-recipe called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let recipe_id = req.body.recipe_id;
  let user_id = req.body.user_id;

  var recipe = await query('SELECT * FROM Recipe WHERE id=\''+recipe_id+'\'');
  if (recipe.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "Recipe with id="+recipe_id+" not found"
    });
    return;
  }

  // is liked?
  var liked_result = await query('SELECT COUNT(*) as hasLiked FROM Favourite WHERE user_id = ' + user_id + ' and recipe_id = ' + recipe_id);
  let hasLiked = liked_result[0].hasLiked;

  // get my rating
  var myRating_result = await query('SELECT value FROM Rating WHERE user_id = ' + user_id + ' and recipe_id = ' + recipe_id);
  var myRating = null;
  for (var i in myRating_result) {
    myRating = myRating_result[i].value; 
  }

  // get ingredients
  var ingr_result = await query('SELECT i.id, i.name, amount FROM RecipeIngredient ri JOIN Ingredient i ON i.id = ri.ingredient_id WHERE ri.recipe_id = ' + recipe_id);

  var ingredient = [];
  for (var i in ingr_result) {
    ingredient.push({
        id: ingr_result[i].id,
        name: ingr_result[i].name,
        amount: ingr_result[i].amount,
      });
  }

  // get receipt
  var result = await query('SELECT r.name as recipe_name, r.preptime_minutes, r.difficulty, r.instruction, r.created_at, u.name as user_name, u.id as user_id, SUM(r2.value) / SUM(1) as rating FROM Recipe r JOIN User u on r.creator_id = u.id LEFT JOIN Rating r2 on r.id = r2.recipe_id WHERE r.id = ' + recipe_id + ' GROUP BY r.name, r.preptime_minutes, r.difficulty, r.instruction, r.created_at, u.name');
  
  var isMine = 0;
  if (result[0].user_id == user_id) {
    isMine = 1;
  }

  var receip = {
    name: result[0].recipe_name,
    preptime_minutes: result[0].preptime_minutes,
    difficulty: result[0].difficulty,
    instruction: result[0].instruction,
    ingredients: ingredient,
    creation_time: result[0].created_at,
    creator_user: result[0].user_name,
    creator_id: result[0].user_id,
    liked: hasLiked,
    is_mine: isMine, 
    my_rating: myRating,
    rating: result[0].rating
  };
  
  var finalJson = { "recipe": receip };
  res.send(finalJson);
});

app.post('/add-recipe', async (req, res) => {
  console.log("add-recipe called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let name = req.body.name;
  let preptime_minutes = req.body.preptime_minutes;
  let difficulty = req.body.difficulty;
  let instruction = req.body.instruction;
  let ingredient_names = req.body.ingredient_names;

  const recipe = await query('SELECT * FROM Recipe WHERE name=\''+name+'\'');
  if (recipe.length > 0) {
    // recipe with given name already exists
    res.send({
      success: false,
      message: "Recipe "+name+" already exists"
    });
    return;
  } else {
    await query('INSERT INTO Recipe (name, preptime_minutes, difficulty, instruction, creator_id) VALUES (\''+name+'\', '+preptime_minutes+', '+difficulty+', \''+instruction+'\', '+user_id+')');
    for (var i in ingredient_names) {
      // check if ingredient already in db
      const ingredient_result = await query('SELECT * FROM Ingredient WHERE name=\''+ingredient_names[i]+'\'');
      if (ingredient_result.length == 0) {
        // create new ingredient
        await query('INSERT INTO Ingredient (name) VALUES (\''+ingredient_names[i]+'\')');
      }
    }

    var ingredient_ids = [];
    for (var i in ingredient_names) {
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
    await query('INSERT INTO RecipeIngredient (ingredient_id, recipe_id, amount) VALUES '+values);
    res.send({
      success: true,
      message: null
    });
  }
});

app.post('/rate-recipe', async (req, res) => {
  console.log("rate-recipe called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let recipe_id = req.body.recipe_id;
  let value = req.body.value;

  if (value < 0 || value > 5) {
    res.send({
      success: false,
      message: "Value must be between 0 and 5"
    });
    return;
  }

  var user = await query('SELECT * FROM User WHERE id=\''+user_id+'\'');
  if (user.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "User with id="+user_id+" not found"
    });
    return;
  }

  var recipe = await query('SELECT * FROM Recipe WHERE id=\''+recipe_id+'\'');
  if (recipe.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "Recipe with id="+recipe_id+" not found"
    });
    return;
  }

  var rating = await query('SELECT * FROM Rating WHERE recipe_id='+recipe_id+' AND user_id='+user_id);
  if (rating.length > 0) {
    // update
    await query('UPDATE Rating SET value='+value+' WHERE id='+rating[0].id);
  }
  else {
    // insert
    await query('INSERT INTO Rating (recipe_id, user_id, value) VALUES ('+recipe_id+','+user_id+','+value+')');
  }
  res.send({
    success: true,
    message: null
  });
});

app.post('/change-password', async (req, res) => {
  console.log("change password called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let old_password_hash = req.body.old_password_hash;
  let new_password_hash = req.body.new_password_hash;

  var user = await query('SELECT * FROM User WHERE id='+user_id);
  if (user.length == 0) {
    res.send({
      success: false,
      message: "User with id="+user_id+" not found"
    });
    return;
  }

  var user_with_password = await query('SELECT * FROM User WHERE id='+user_id+' AND password=\''+old_password_hash+'\'');
  if (user_with_password.length == 0) {
    res.send({
      success: false,
      message: "Password is wrong"
    });
    return;
  }

  await query('UPDATE User SET password=\''+new_password_hash+'\' WHERE id='+user_id);
  res.send({
    success: true,
    message: null
  });
});

app.post('/delete-recipe', async (req, res) => {
  console.log("delete receip called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let recipe_id = req.body.recipe_id;
  
  var user = await query('SELECT * FROM User WHERE id='+user_id);
  if (user.length == 0) {
    res.send({
      success: false,
      message: "User with id="+user_id+" not found"
    });
    return;
  }

  var recipe = await query('SELECT * FROM Recipe WHERE id='+recipe_id);
  if (recipe.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "Recipe with id="+recipe_id+" not found"
    });
    return;
  }

  if (user[0].is_admin == 0) {
    recipe = await query('SELECT * FROM Recipe WHERE id='+recipe_id+' AND creator_id='+user_id);
    if (recipe.length == 0) {
      // user or recipe not found!
      res.send({
        success: false,
        message: "User not permitted to delete Recipe"
      });
      return;
    }
  }
  
  await query('DELETE FROM RecipeIngredient WHERE recipe_id='+recipe_id);
  await query('DELETE FROM Rating WHERE recipe_id='+recipe_id);
  await query('DELETE FROM Favourite WHERE recipe_id='+recipe_id);
  await query('DELETE FROM Recipe WHERE id='+recipe_id);
  res.send({
    success: true,
    message: null
  });
});

app.post('/like-recipe', async (req, res) => {
  console.log("like called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let recipe_id = req.body.recipe_id;
  
  var user = await query('SELECT * FROM User WHERE id='+user_id);
  if (user.length == 0) {
    res.send({
      success: false,
      message: "User with id="+user_id+" not found"
    });
    return;
  }

  var recipe = await query('SELECT * FROM Recipe WHERE id=\''+recipe_id+'\'');
  if (recipe.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "Recipe with id="+recipe_id+" not found"
    });
    return;
  }

  var favourite = await query('SELECT * FROM Favourite WHERE recipe_id='+recipe_id+' AND user_id='+user_id);
  if (favourite.length > 0) {
    res.send({
      success: false,
      message: "Recipe already liked"
    });
    return;
  }

  await query('INSERT INTO Favourite (recipe_id, user_id) VALUES ('+recipe_id+','+user_id+')');
  res.send({
    success: true,
    message: null
  });
});

app.post('/unlike-recipe', async (req, res) => {
  console.log("unlike called!");
  var con = connectDB();
  const query = util.promisify(con.query).bind(con);

  let user_id = req.body.user_id;
  let recipe_id = req.body.recipe_id;
  
  var user = await query('SELECT * FROM User WHERE id='+user_id);
  if (user.length == 0) {
    res.send({
      success: false,
      message: "User with id="+user_id+" not found"
    });
    return;
  }

  var recipe = await query('SELECT * FROM Recipe WHERE id=\''+recipe_id+'\'');
  if (recipe.length == 0) {
    // user or recipe not found!
    res.send({
      success: false,
      message: "Recipe with id="+recipe_id+" not found"
    });
    return;
  }

  var favourite = await query('SELECT * FROM Favourite WHERE recipe_id='+recipe_id+' AND user_id='+user_id);
  if (favourite.length == 0) {
    res.send({
      success: false,
      message: "Recipe not liked"
    });
    return
  }

  await query('DELETE FROM Favourite WHERE recipe_id='+recipe_id+' AND user_id='+user_id);
  res.send({
    success: true,
    message: null
  });
});

const port = process.env.PORT || 3000;
app.listen(port, () => {
	console.log(`Worker: process ${process.pid} is up on port ${port}`);
});
