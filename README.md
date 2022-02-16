# spring-data-elastic-issue
Code for recreating the issue with custom routing

spring boot version: 2.5.8
spring-data-elastic version: 4.2.7
ElasticSearch version: 7.15.2

## PREREQUSITE
1. Elastic-Search is up and running on URL localhost:9200.

## Issue description

### Steps to reproduce
1. Setup a Document entity class and use `@Routing` to set custom routing, define the number of shards and enable index creation.
2. Save some documents.
3. Use `ReactiveElasticsearchOperations` to query data.
4. Use `withRouting` method of `ReactiveElasticsearchOperations` to specify routing.
5. Use `searchForPage` or `count` method to query data (search query can be anything).

While examine the result you will notice that custom routing is not taken into consideration. Also when you debug you will notice that `SearchRequest` which is created does not have any routing information associated with it its `routing` variable is null. IF you set the routing variable to value you want everything to start working correctly.

NOTE: The workaround of this issue is to create a replica of `ReactiveElasticsearchTemplate` and modify the methods to add set `routing` on `SearchRequest` and everything will work fine(this is a long route because many classes used by `ReactiveElasticsearchTemplate` directly or indirectly are not public so you have to create a replica of every class). I have also tried subclassing `ReactiveElasticsearchTemplate` and overriding the required methods and providing my custom subclass while configuring things by extending class `AbstractReactiveElasticsearchConfiguration` but somehow it always ends up calling the parent methods (maybe I am doing something wrong or missing a core concept).

### Specific steps to reproduce(as per checked-in code)
1. Start elastic search at localhost:9200 or change the URL in class `ReactiveElasticRestClientConfiguration`.
2. Start the spring boot app, it will be up and running on port 8081, or change the port.
3. When the application starts it automatically saves 4 documents under index `test`. The documents are as follows.
  -       "id" : "id3",
          "routVar" : "rout2",
          "testVar" : 3,
          "searchStr" : "search2"
  -       "id" : "id4",
          "routVar" : "rout2",
          "testVar" : 4,
          "searchStr" : "search2"
  -       "id" : "id1",
          "routVar" : "rout1",
          "testVar" : 1,
          "searchStr" : "search1"
  -       "id" : "id2",
          "routVar" : "rout1",
          "testVar" : 2,
          "searchStr" : "search1"
4. Check elastic with the request, this will give you 2 documents as it should, the query is below: 
  - ` GET test/_search?routing=rout2
{
  "track_total_hits": true,
  "explain": true,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "searchStr": "search2"
          }
        }
      ]
    }
  }
} `
5. Now change the request to below one, it will give you 0 result as it should(routing is wrong, there are no document having search2 on rout1): 
  - ` GET test/_search?routing=rout1
{
  "track_total_hits": true,
  "explain": true,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "searchStr": "search2"
          }
        }
      ]
    }
  }
} `
6. Now we will try the same thing using `ReactiveElasticsearchOperations`'s `withRouting`, `count` methods.
7. There are two endpoints this application exposes `/test/entity` which take `searchStr` and `routVar` as query param and returns the matched documents, another endpoint is `/test/entity/count`  which take `searchStr` and `routVar` as query param and returns the count of matched documents.
8. `TestProcessor` class is where all logic is, it is using `ReactiveElasticsearchOperations` `withRouting`, `count` and `searchForPage`, where I am filtering on `searchStr` and routing on `routVar`.
9. You can use endpoints to try out things or debug as you like.

doc with `searchStr` search2 and search2 will be on rout2 and `searchStr` search1 and search1 will be on rout1.
