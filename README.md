# csc326-OBP-201-1

*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)


# CoffeeMaker API Endpoint Design

Includes new ingredients endpoints and existing updated endpoints
Overview

# Resources

# Input JSON Examples
{"id":1,"name":"Coffee","isInventory":true,"amount":99}
{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}
{"id":1,"ingredients":[{"name":"Coffee","isInventory":false,"amount":10},{"name":"Milk","isInventory":false,"amount":5}]}


# Route: GET /ingredients
Function: getIngredients()

Description: This API endpoints returns a list of ingredients with a status of 200 (OK).

Data Details: Calling the endpoint should result in:
```
API endpoint: localhost:8080/api/v1/ingredients
{"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}
```

# Route: PUT /ingredients
Function: createIngredient()

Description: This API endpoint should create the ingredient with the given name and state. If an ingredient with the name already exists, expect a status 400.

```
API endpoint: localhost:8080/api/v1/ingredients

Data details: If CoffeeMaker already contains:


{"id":1,"name":"Coffee","isInventory":false,"amount":10},

Calling the endpoint with the following data
{"name":"Milk","isInventory":false,"amount":5}

Should result in a status 200 as flour does not exist. We would expect Ingredients to be updated to {
{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}}
```

# Route: GET /ingredients/{name}
Function: getIngredient(String name)

Description: This API endpoint should fetch the ingredient that matches the given name. If no match is found, then a status of 404 (NOT FOUND).

Data details: If we have a ingredient with the name Coffee and call endpoint GET /ingredients/Coffee, the API should return status 200 with the following 

Data details:
```
API endpoint: localhost:8080/api/v1/ingredients/Coffee

{"id":1,"name":"Coffee","isInventory":false,"amount":10}
```
If we call endpoint GET /ingredients/idontexist then the API should return status 404 (NOT FOUND) with no response JSON data.


# Route: PUT /ingredients/{name}
Function: updateIngredient(String name)
Description: The API endpoint should update the Ingredient with the matched name to the given units. If there is no match, a status 404 (NOT FOUND) should be expected with no response JSON data.

```
API endpoint: localhost:8080/api/v1/ingredients/Coffee

Data details: If CoffeeMaker already contains:
{"id":1,"name":"Coffee","isInventory":false,"amount":10}

And endpoint PUT ingredients/apple is called with the data:
{"name":"Coffee","isInventory":false,"amount":99}

We should expect CoffeeMaker to be updated to
{"id":1,"name":"Coffee","isInventory":false,"amount":99}
```
And return the same content as a response.
If instead PUT ingredients/idontexist is called, expect a status 404 with no response data.


# Route: GET /recipes
Function: getRecipes()

Description: The API endpoint should fetch and return all the recipes that currently exist with status 200 (OK).

```
API endpoint: localhost:8080/api/v1/recipes

Data details: Calling the API endpoint GET /recipes should result in status 200 with content such as:


{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}

```

# Route: POST /recipes
Function: createRecipe(String name)

Description: The API endpoint should create a recipe with the given name and state. The API shall return status 200 and the created recipe on success. If a recipe already exists with the name, a status 400 is returned with no response data.


```
Data details: If CoffeeMaker already contains:
API endpoint: localhost:8080/api/v1/recipes

	
{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}

```


And the API endpoint POST /recipes is called with the following data:
```

{"id":2,"name":"Bev","price":15,"ingredients":[{"id":3,"name":"Sugar","isInventory":false,"amount":10}]}

```


We should expect a status 200 (OK) and the response to contain the newly created recipe. CoffeeMaker would then contain:
```

{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}, {"id":2,"name":"Bev","price":15,"ingredients":[{"id":3,"name":"Sugar","isInventory":false,"amount":10}]}

```
We should expect a status 400 and no response data, as ‘aRecipe’ already exists. 


# Route: GET /recipes/{name}
Function: getRecipe(String name)

Description: The API endpoint should return a status 200 (OK) and the content of the recipe with the matched name. If no match is found, return a status of 404 (NOT FOUND) without response data.

```
API endpoint: localhost:8080/api/v1/recipes/Drink

Data details: Calling the endpoint GET /recipes/arecipe should result in status 200 and response data:
{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}
```

# Route: PUT /recipes/{name}
Function: editRecipe(String name)
Description: API endpoint will edit the Recipe object with the corresponding identifier. The inputs will be a recipe with a matching name, updates the Recipe’s ingredients and price, and finally output a status 200 with the updated recipe object. If no recipes exist with the identifier, return an empty response with a status 404 (NOT FOUND). If the identifier and Recipe name do not match, return an empty response with a status 400 (Bad Request).

```
API endpoint: localhost:8080/api/v1/recipes/Drink

Data details: If CoffeeMaker already contains:


{"id":1,"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Coffee","isInventory":false,"amount":10},{"id":2,"name":"Milk","isInventory":false,"amount":5}]}, {"id":2,"name":"Bev","price":15,"ingredients":[{"id":3,"name":"Sugar","isInventory":false,"amount":10}]}

```
And the API endpoint PUT /recipes/Drink is called with the following data:
```
{"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Sugar","isInventory":false,"amount":1}]}
```
We should expect a status 200 (OK) and the response to contain the updated recipe. CoffeeMaker would contain and return:
```
{"name":"Drink","price":15,"ingredients":[{"id":1,"name":"Sugar","isInventory":false,"amount":1}]}

If instead a call to API endpoint POST /recipes/idontexist is made, we should expect
a status 400 and no response data, as ‘idontexist’ does not exist.

If instead a call to API endpoint POST /recipes/Drink is done with the following data
{"name":"Bev","price":15,"ingredients":[{"id":1,"name":"Sugar","isInventory":false,"amount":1}]}

A status of 400 and no response data is expected since the names do not match.


```



# Route GET /inventory 
Function: getInventory()
Description: Gets an inventory object. If no inventory exists it creates a new one.

Data details:
No inputs are needed for this
```
API endpoint: localhost:8080/api/v1/inventory


Calling this endpoint should produce the following JSON

{"id":12431,"ingredients":[]}
```


# Route PUT /inventory
Function putInventory()
Description: Create an initial inventory of ingredients that is empty. If no Inventory exists then create a new Inventory with the input ingredients. It will add the ingredients in the input inventory into the new inventory object.

Data details:

```
API endpoint: localhost:8080/api/v1/inventory
Calling this endpoint will need the input:


{"ingredients":[{"name":"Milk","isInventory":false,"amount":5}, {"name":"Coffee","isInventory":false,"amount":5}]}

The Inventory will look like this:
{"id":12431,"ingredients":[{"name":"Coffee","isInventory":false,"amount":10}]}


Then will become this:
{"id":12431,"ingredients":[{"name":"Coffee","isInventory":false,"amount":15},{"name":"Milk","isInventory":false,"amount":5}]}

```

# Route PUT /inventory/{ingredient}
Function: putInventoryIngredient()
Description: Create an ingredient and upload it to the inventory. No duplicate 

Data details:
```
API endpoint: localhost:8080/api/v1/inventory

Calling this endpoint will need the input of an ingredient:
{"name":"Milk","isInventory":false,"amount":5}

The Inventory will look like this:
{"id":12431,"ingredients":[{"name":"Coffee","isInventory":false,"amount":10}]}


Then will become this:
{"id":12431,"ingredients":[{"name":"Coffee","isInventory":false,"amount":10},{"name":"Milk","isInventory":false,"amount":5}]}

```
