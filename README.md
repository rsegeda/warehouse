# Generic SQL API

The following repository contains very basic implementation of generic data requesting.

Pros:
- projections based on the provided array of fields - SELECT keyword
- predicates to create filtered queries with WHERE keyword
- grouping by the given fields - GROUP BY
- paginated API
- basic schema validation
- db populating scripts
- reading csv data from the url (encoded url) as a GET request param

Cons:
- low test coverage
- vulnerable sql queries - should be typed, however generic approach makes it more difficult
- lack of docs (swagger at least)
- overcomplicated implementation of sql queries part


##Example Requests:

###extract csv file and populate the db with new metrics
GET http://localhost:8080/extractor/extract?url=http%3A%2F%2Fdomain.com%2Fdata.csv

###delete all metrics
DELETE http://localhost:8080/extractor/tabularasa?token=XXXX

secured with the token configurable from the env vars

###query data
POST /metrics/query
Request body: (example)
3 main parts:
 - aggregators/selectors - Select
 - filters/predicates - WHERE
 - groupAggregators/ Group By

````json
{
  "aggregators": [
    "dataSource",
    "clicks",
    "daily",
    "campaign",
    "impressions"
  ],
  "filters": [
    {
      "dimension": "dataSource",
      "filterConditions": [
        {
          "filterType": "=",
          "firstValue": "'Twitter Ads'"
        }
      ]
    },
    {
      "dimension": "daily",
      "filterConditions": [
        {
          "filterType": ">",
          "firstValue": "2019-10-10"
        }
      ]
    }
  ],
  "groupAggregators": [
    "clicks",
    "daily"
  ],
  "page": 0,
  "pageSize": 2
}
````
Example response:
```json
{
  "successful": true,
  "result": {
    "content": [
      [
        "Twitter Ads",
        83.00,
        "Adventmarkt Touristik",
        39614.00
      ],
      [
        "Twitter Ads",
        6.00,
        "Adventmarkt Touristik",
        1460.00
      ],
      [
        "Twitter Ads",
        6.00,
        "Adventmarkt Touristik",
        1393.00
      ],
      [
        "Twitter Ads",
        2.00,
        "Adventmarkt Touristik",
        660.00
      ],
      [
        "Twitter Ads",
        2.00,
        "Adventmarkt Touristik",
        423.00
      ]
    ],
    "pageSize": 5,
    "pageNumber": 1,
    "totalPages": 798,
    "totalElements": 3987
  }
}

