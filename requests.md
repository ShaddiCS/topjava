 ####users
 curl http://localhost:8080/topjava/rest/admin/users
 ####get user by email
 curl http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com
 #### get 1 user
 curl http://localhost:8080/topjava/rest/admin/users/100001
 #### delete user
 curl -X DELETE http://localhost:8080/topjava/rest/admin/users/100001
 #### get all meals
 curl http://localhost:8080/topjava/rest/meals
 #### get one meal
 curl http://localhost:8080/topjava/rest/meals/100003
 #### delete meal
 curl -X DELETE http://localhost:8080/topjava/rest/meals/100003
 #### filter meals
 curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=13:00:00&endDate=2020-01-30&endTime=20:01:00"
 #### create meal
 curl -X POST -d '{"description":"newMeal","calories":300,"dateTime":"2020-03-31T20:00"}' -H 'Content-Type:application/json' http://localhost:8080/topjava/rest/meals
 #### update meal
 curl -X PUT -d '{"id":100003,"description":"updatedMeal","calories":333,"dateTime":"2020-03-31T20:00"}' -H 'Content-Type:application/json' http://localhost:8080/topjava/rest/meals/100003