
var app = angular.module('myApp', []);

app.controller('AddInventoryController', function($scope, $http) {

	//Inventory of ingredient objects we want to show off
	$scope.inventoryIngredients = {};

	$scope.inventory = {};





	//Get the inventory of ingredients
	$scope.getInventory = function() {
		$http.get("/api/v1/inventory").then(function(response) {
			let ingredients = response.data.ingredients;
			console.log(ingredients);
			//Now push on the objects to the other inventory
			for (let i = 0; i < ingredients.length; i++) {
				//This also takes care of resets
				$scope.inventoryIngredients[ingredients[i].name] = {
					'id': ingredients[i].id,
					'name': ingredients[i].name,
					'amount': ingredients[i].amount,
					'display': ingredients[i].name,
					'newAmount': ''
				}
			}
		}, function(rejection) {
			console.error("Error while getting Inventory");
		});

		console.log($scope.inventoryIngredients);
	}
	

	$scope.updateInventory = function() {
		$scope.success = false;
		$scope.failure = false;

		//Allow null values for ingredients
		//We can do this by changing the amount to equal newAmount and ignoring empties
		for (ingredient in $scope.inventoryIngredients) {

			//Iterate over the keys and push new ojects to our list 
			console.log(ingredient);
			let newValue = $scope.inventoryIngredients[ingredient].newAmount;
			if ($scope.inventoryIngredients[ingredient].newAmount != null) {
				$scope.inventory.ingredients.push({
					name: ingredient,
					amount: newValue,
					isInventory: true
				})
			}
			else {
				$scope.inventory.ingredients.push({
					name: ingredient,
					amount: 0,
					isInventory: true
				})
			}
		}

		console.log($scope.inventory);



		$http.put("/api/v1/inventory", $scope.inventory).then(
			function(response) {
				$scope.getInventory();
				$scope.success = true;
				$scope.failure = false;
				console.log("Inventory Added")
			}, function(rejection) {
				$scope.failure = true;
				$scope.success = false;
				console.error("Error while updating Inventory!");
			});
	}

	$scope.submit = function() {
		$scope.updateInventory();

		$scope.reset();

	}


	//Get the inventory of ingredients on start
	//$scope.getInventory();


	$scope.reset = function() {
		//Reset our submission inventory placeholder
		$scope.inventory = {
			'ingredients': []
		};
		if (undefined != $scope.addInventoryForm) {
			$scope.addInventoryForm.$setPristine(); //reset Form
		}
		$scope.getInventory();

	}

	$scope.reset();



});