
 # Purely functional API using http4s, circe and quill for cassandra. 

You can use this docker compose => [Apache Cassandra docker composer](https://github.com/Javac7/my-compose/blob/master/cassandra-docker-compose.yml)

## Create the environment:

```cql
    CREATE KEYSPACE IF NOT EXISTS "rent_object"
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
    CREATE TYPE rent_object.address (
        street text,
        city text,
        state text,
        neighborhood text,
        zip_code int,
        number text,
        reference_point text
    )

 CREATE TABLE "rent_object"."users" (
     user_id uuid,
     first_name text,
     last_name text,
     nickname text,
     email text,
     cpf text, 
     phone_number LIST<text>,
     birth_date date,
     user_password text,
     created_at timestamp,
     enabled boolean, 
     profile_image_url text,
     address list<frozen<address>>,
     PRIMARY KEY(user_id, created_at, email, user_password)
 )
-- drop table "rent_object"."users"
SELECT * FROM "rent_object"."users"
```

You can use this curl of example: 
curl --location --request POST 'localhost:8080/user' 
--header 'Content-Type: application/json' 
--data-raw 
```json
{
	"first_name":"my name ",
	"last_name":" yeah",
	"nickname":"spike",
	"email":"key@gmail.com",
	"cpf":"-----------------",
	"phone_number":["28997586548, 87546214874"],
	"birth_date":"1992-02-01",
	"user_password":"123456",
	"enabled": false,
	"profile_image_url":"/path",
	"address": [{
		"street": "high street",
		"city": "berlin",
		"state": "BL",
		"neighborhood": "haking",
		"zip_code": 64329040,
		"number": "SN",
		"reference_point": "next park"
	}]
}
```
