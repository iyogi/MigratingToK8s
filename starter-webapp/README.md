### Milestone 3 - Starting application
The following command asssumes a single node minikube server

0. Prerequisites for PersistentVolume and PersistentVolumeClaims
    a. Start the minikube server
        * `minikube start`
    a. Ensure that following path exists in the minikube node
        * `minikube ssh`
        * `mkdir -p /tmp/manning/app/data /tmp/manning/mysql/data`

1. Create a new namespace and switch to this new namespace
    * `kubectl apply -f deployment-1-namespace.yml`
    * `kubectl config set-context --current --namespace=migrate-2-k8s`
            
2. Create the PersistentVolume and PersistentVolumeClaims for mysql database and profiles app
    * `kubectl apply -f deployment-2-pv+pvc.yml`
    
3. Create the necessary secrets and configmaps for mysql (for MYSQL_ROOT_PASSWORD and initialising profiles database respectively)
    * `kubectl apply -f deployment-3-configmaps+secrets.yml`
    
4. Deploy mysql database along with the service to be able to lookup the database with single dns   
    * `kubectl apply -f deployment-4-mysql.yml`

5. Deploy the profiles app with a LoadBalancer service to be able to access it from outside minikube
    * `kubectl apply -f deployment-5-profiles-app.yml`
    * `minikube service profiles -n migrate-2-k8s`

5. Add horizontal pod autoscaler (HPA) to automatically scale the profile pod automatically if more than 50% configured cpu request is utilized
    * `kubectl apply -f deployment-6-horizontal-autoscaler.yml`
    * Note that you may have to enable metrics-server addon in minikube using `minikube addons enable metrics-server` if the autoscaler is unable to read metrics
        * This error will be shown as "<unknown>/50%" target with description of the HPA giving error 
        * "The HPA was unable to compute the replica count: unable to get metrics for resource cpu: no metrics returned from resource metrics API"
        * You may also have to re-deploy the profiles app if this error occurs 
    * You can test this HPA by login into the profiles pod and spiking the cpu
        * `kubectl exec -it pod/profiles-<pod-id> -- bash`
        * `bash-4.4# dd if=/dev/zero of=/dev/null`  
        * This should automatically spin up two more pods
        * Stopping the spike command (using CTRL-C) should cause the profile pods to be scaled back to one after short time. 


### Milestone 3 - Deliverable

1. Files
    * `deployment-1-namespace.yml`
    * `deployment-2-pv+pvc.yml`
    * `deployment-3-configmaps+secrets.yml`
    * `deployment-4-mysql.yml`
    * `deployment-5-profiles-app.yml` 
    * `deployment-6-horizontal-autoscaler.yml`
    
-----------------------------------------------------
-----------------------------------------------------


### Milestone 2 - Starting application

1. Ensure that the following paths exists to store the data and images outside of the containers
    * `mkdir -p /tmp/manning/profiles-app/images /tmp/manning/profiles-app/data`
    
1. Change dir to project root
    * `cd /absolute/path/to/starter-webapp`
    
2. Start the application (consisting of both containers - profiles springboot and mysql database)    
    * `docker-compose up -d`

3. Confirm that the containers have been properly started
    * `docker ps`

### Milestone 2 - Deliverable

1. Files
    * `Dockerfile`
    * `docker-compose.yml` 
    
-----------------------------------------------------
-----------------------------------------------------

### Milestone 1 - Starting application

1. Start MySQL database server before starting this application
2. Create jar: `mvn package spring-boot:repackage`
3. Run jar: `java -jar target/profiles.jar`


### Milestone 1 - Deliverable
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


#### 3. fetch non-existent user profile (returns 404) 
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