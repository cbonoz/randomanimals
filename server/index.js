// index.js
// https://scotch.io/tutorials/a-realtime-animal-chat-app-using-node-webkit-socket-io-and-mean
// Import all our dependencies
var express  = require('express');
var mongoose = require('mongoose');
var app      = express();
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

var server   = require('http').Server(app);
var io       = require('socket.io')(server);

var DB_NAME = "random-animals";
var PLAY_EVENT = "playevent";
var JOIN_EVENT = "joinevent";
var LEADER_EVENT = "leaderevent";

mongoose.connect("mongodb://127.0.0.1:27017/" + DB_NAME);

// create a schema for animal sounds.
var AnimalSchema = mongoose.Schema({
  userid: {type: String, unique: true}, 
  username: String,
  animal: String,
  created: { type: Date, default: Date.now },
  count: { type: Number, default: 1} 
});

// create a model from the animal sounds schema
var Animal = mongoose.model('Animal', AnimalSchema);

// allow CORS
app.all('*', function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-type,Accept,X-Access-Token,X-Key');
  if (req.method == 'OPTIONS') {
    res.status(200).end();
  } else {
    next();
  }
});

//This route produces a list of users that have listened to the particular animal. 
app.post('/animal', function(req, res) {
  //Find
  Animal.find({
    'animal': req.body.animal.toLowerCase()
  }).exec(function(err, msgs) {
    res.json(msgs);
  });
});

app.post('/userid', function(req, res) {
  //Find
  console.log(req.body)
  Animal.find({
    'userid': req.body.userid.toLowerCase()
  }).exec(function(err, msgs) {
      console.log('callback of userid: ' + msgs);
    res.json(msgs);
  });
});

app.post('/increment', function(req, res) {
  //Find
  var targetObj = {
    'userid': req.body.userid.toLowerCase(),
    'animal': req.body.animal.toLowerCase()
  };
  Animal.findOneAndUpdate(targetObj, 
        { $set: { "userid" : targetObj.userid, "animal" : targetObj.animal, "username": req.body.username}, $inc : { "count" : 1 } },
        { sort: { "count" : 1 }, upsert: true, setDefaultsOnInsert: true, returnNewDocument : true })
        .exec(function(err, db_res) { 
            if (err) { 
                throw err; 
            } else { 
                console.log(db_res); 
            } 
        });
 res.json({"status": "received increment for " + JSON.stringify(targetObj)});
});

/*||||||||||||||||SOCKET|||||||||||||||||||||||*/
//Listen for connection
io.on('connection', function(socket) {

    var defaultAnimal = "cat"
  //Emit the animals array
//   socket.emit('setup', {
//     animals: animals
//   });

  Animal.find({
        'animal': data.animal 
    }).exec(function(err, msgs) {
        socket.emit("playevent", {
            animals: msgs
        });
  });

  //Listens for new user
  socket.on(LEADER_EVENT, function(data) {
    data.animal = defaultAnimal;
    //New user joins the default animal
    socket.join(defaultAnimal);
    //Tell all those in the animal that a new user joined
    io.in(defaultAnimal).emit('user joined', data);
    Animal.find({
        'animal': data.animal 
    }).exec(function(err, msgs) {
        io.in()
    })
  });

  //Listens for switch animal
  socket.on('switch animal', function(data) {
    //Handles joining and leaving animals
    //console.log(data);
    socket.leave(data.oldAnimal);
    socket.join(data.newAnimal);
    io.in(data.oldAnimal).emit('user left', data);
    io.in(data.newAnimal).emit('user joined', data);

  });

  //Listens for a new chat message
  socket.on(PLAY_EVENT, function(data) {
    var existingCount = 0;
    var targetObj = {"userid": data.userid, "animal": data.animal};
    Animal.findOneAndUpdate(targetObj, 
    { $set: { "userid" : data.userid, "animal" : data.animal, "username": data.username}, $inc : { "count" : 5 } },
    { sort: { "count" : 1 }, upsert:true, returnNewDocument : true });
    // //Save it to database
    // newMsg.save(function(err, msg){
    //     //Send message to those connected in the animal
    //     io.in(msg.animal).emit(PLAY_EVENT, msg);
    // });
    //Create message
    
  
  });
});
/*||||||||||||||||||||END SOCKETS||||||||||||||||||*/

// index.js
var PORT = 3001;
server.listen(PORT);
console.log('Server started on port: ' + PORT);
