#### 1. create new profile
```
$ curl -v -X POST -H 'Content-Type: application/json' http://localhost:8080/profile/unamerkel -d '{"username":"unamerkel","password":"changeme","firstName":"Una","lastName":"Merkel","email":"unamerkel@example.com"}'
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> POST /profile/unamerkel HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Content-Type: application/json
> Content-Length: 116
>
* upload completely sent off: 116 out of 116 bytes
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 26 May 2020 21:33:03 GMT
<
* Connection #0 to host localhost left intact
{"id":11,"username":"unamerkel","password":"changeme","firstName":"Una","lastName":"Merkel","email":"unamerkel@example.com","imageFileName":null,"imageFileContentType":null}* Closing connection 0
```

#### 2. fetch user profile (as JSON)
```
$ curl -v -X GET localhost:8080/profile/unamerkel
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /profile/unamerkel HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 26 May 2020 21:33:11 GMT
<
* Connection #0 to host localhost left intact
{"id":11,"username":"unamerkel","password":"changeme","firstName":"Una","lastName":"Merkel","email":"unamerkel@example.com","imageFileName":null,"imageFileContentType":null}* Closing connection 0
```


#### 3. etch non-existent user profile (returns 404) 
```
$ curl -v -X GET localhost:8080/profile/russcolombo
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /profile/russcolombo HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 404
< Content-Length: 0
< Date: Tue, 26 May 2020 21:33:18 GMT
<
* Connection #0 to host localhost left intact
* Closing connection 0
```


#### 4. upload image named 'una_merkel.jpg' in current directory 
```
$ curl -v -X POST http://localhost:8080/profile/unamerkel/image -F "file=@una_merkel.jpg"
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> POST /profile/unamerkel/image HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Content-Length: 85542
> Content-Type: multipart/form-data; boundary=------------------------ed21eb67c7e7cb2c
> Expect: 100-continue
>
< HTTP/1.1 100
* We are completely uploaded and fine
< HTTP/1.1 200
< Content-Length: 0
< Date: Tue, 26 May 2020 21:33:24 GMT
<
* Connection #0 to host localhost left intact
* Closing connection 0
```

#### 5. download image file (will return a 200 and a message) 
```
$ curl -v -X GET -H 'Accept: image/jpeg' http://localhost:8080/profile/unamerkel/image --output una_merkel_download.jpg
Note: Unnecessary use of -X or --request, GET is already inferred.
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /profile/unamerkel/image HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: image/jpeg
>
< HTTP/1.1 200
< Content-Type: image/jpeg
< Content-Length: 85350
< Date: Tue, 26 May 2020 21:34:10 GMT
<
{ [8089 bytes data]
100 85350  100 85350    0     0  6945k      0 --:--:-- --:--:-- --:--:-- 6945k
* Connection #0 to host localhost left intact
* Closing connection 0