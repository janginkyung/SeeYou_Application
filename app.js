var express=require('express') ;
var app=express();
var bodyParser = require('body-parser');
var session = require('express-session')
var mysql=require('mysql') ;

app.use(session({
  secret: 'keyboard cat'
, cookie: { maxAge: 60000 }}))
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

var connection =mysql.createConnection({
host:'localhost',
port: 3306,
user:'root' ,
password:'wkd30518!!',
database:'SeeYou'
});

connection.connect(function(){
    console.error("SeeYou connection");

});

//회원 가입시에 유저의 정보가 DB에 저장된다.
app.post("/signup",function(req,res){
  var table="USER";
  var userId=req.body.userId || req.query.userId;
  var userName=req.body.userName || req.query.userName;
  var userEmail=req.body.userEmail || req.query.userEmail;
  var userPhotoURL=req.body.userPhotoURL || req.query.userPhotoURL;

  var InsertUser="INSERT INTO "+table+" (userId,userName,userEmail,userPhotoUrl) VALUES " + "('" +userId+ "','" +userName+ "','" + userEmail + "','" +userPhotoURL + "')";

  connection.query(InsertUser,function(err,rows){
    if(err) {
      console.log('/signup: USER err가 발생하여 삽입할 수 없습니다.');
      res.send("USER 테이블에 삽입하였습니다.");
    } else {
      console.log(rows+"를 USER 테이블에 삽입하였습니다.");
      res.send("USER 테이블에 삽입하였습니다.");
    }
  })
});

//유저의 정보를 db로부터 읽어와서 안드로이드로 보내준다.
app.get("/users",function(req,res){

  var GetUser="select * from USER";

  connection.query(GetUser,function(err,rows){
    if(err) {
      console.log('err');
      res.send("/users: USER 테이블에 err가 발생하여 참조 할 수 없습니다.");
    } else {
      console.log(rows+"를 USER 테이블을 참조하였습니다.");
      res.send(rows);
    }
  })
})

//LOCATION 테이블에 요청하여 자신의 위치 행 생성
app.post("/location/user",function(req,res){
  var table='LOCATION';

  var userId=req.body.userId|| req.query.userId;
  var latitude=req.body.latitude || req.query.latitude;
  var longtitude=req.body.lontitude || req.query.lontitude;

  if (typeof latitude == "undefined") {
    latitude=null,longtitude=null;
  }
  var Insertlocation="INSERT INTO "+table+" (userId,latitude, lontitude) VALUES "+"('"+userId+"',"
  +latitude+","+longtitude+")";
console.log(Insertlocation);
  connection.query(Insertlocation, function(err,rows) {
    if(err) {
      res.send(res.sta);
      console.log('이미 행에 삽입되었습니다.');
    } else {
      console.log(rows+"를 LOCATION 테이블에 삽입하였습니다.");
    }
  });
})
//LOCATION 테이블에 나의 위치를 갱신한다.
app.put('/location/user',function (req, res) {
  console.log("put /User/location 발생");
  var table='LOCATION';
  var userId=req.body.userId || req.query.userId;
  var latitude=req.body.latitude || req.query.latitude;
  var longtitude=req.body.lontitude || req.query.lontitude;

  var Updatelocation = "UPDATE "+table+" SET latitude="+latitude+",lontitude="+ longtitude+" WHERE userId='" + userId+"'";

  connection.query(Updatelocation, function(err,rows) {
    if(err) {
      console.log('/location/user: LOCATION err가 발생하여 갱신할 수 없습니다.');
    } else {
      console.log(rows+" LOCATION의 행을 갱신하였습니다");
    }
  })
})

//LOCATION 테이블을 통하여 친구의 위치를 가져온다.
app.get('/location/:fri_id',function (req, res) {
  var table='LOCATION';
  var fri_id=req.params.fri_id;

  var getlocationquery = "SELECT latitude,lontitude FROM "+table+" WHERE userId='"+fri_id+"'";

  connection.query(getlocationquery, function(err,rows) {
    if(err) {
      console.log('/location : LOCATION err가 발생하여 참조할 수 없습니다.');
    } else {
      console.log(rows+"를 LOCATION 테이블 참조하였습니다.");
      res.send(rows);
    }
  })
})


app.delete('/UserId/:UserId', function (req, res) {
  console.log("location get발생");
  var table='LOCATION';
  var userId=req.params.UserId;

  var deletelocationquery = "DELETE FROM "+table+" WHERE userId='"+userId+"'";

  connection.query(deletelocationquery, function(err,rows) {
    if(err) {
      console.log('/location : LOCATION err가 발생하여 참조할 수 없습니다.');
    } else {
      console.log(rows+"를 LOCATION 테이블 참조하였습니다.");
      res.send(rows);
    }
  })
})
var server=app.listen(23023);
