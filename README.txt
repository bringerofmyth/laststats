DATA STRUCTURE 

I designed a custom linked list named BucketLinkedList with a fixed size. I accept each
second as a bucket, If input comes within the same second(the time input), I just update
values in the bucket, not store the amount data itself, only min, max, sum and count are
updated. Therefore, the data amount does not grows, it is always constant.


The bucket linked list is sorted according to time(in seconds) in descending order. So, if
some bucket data is out of date, I basically cut it off from the previous node by setting
next bucket to null.

To sum up, we have always have maximum number of 61(from second 0 slot to second 60 slot)
buckets. When we want to calculate the statistics, we calculate all valid buckets, up to 
61 buckets. Therefore, the space and time do not depend on the data amount, we store the
data in constant space and calculate it in constant time, so we can say it will be O(1)
space and O(1) time.

CONCURRENCY

I used ReentrantReadWriteLock in order to allow multiple reads and single write at the same
time. I avoid read/write and write/write conflicts but allow read/read operations. 

BUILD & RUN
-Built with gradle.
-Basically type once you change to project directory:
	o ./gradlew clean build
	o java -jar /build/libs/laststats-0.0.1-SNAPSHOT.jar
	o It must start running.
	
TEST

1- POST "http://localhost:8080/transactions" 

with Body:
{
	"amount": 100,
	"timestamp": 1495289330116

}
Response: Status Code 201
Body: Data saved

2- GET "http://localhost:8080/statistics"

Response: 
{
  "max": 100,
  "min": 100,
  "sum": 100,
  "count": 1,
  "avg": 100
}