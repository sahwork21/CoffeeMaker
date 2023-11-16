var app = angular.module('myApp', []);
app.controller('EditRecipeController', function($scope, $http) {
	$scope.allRecipes = [];
	$scope.selectedRecipe = null;
	$scope.invalid = {
		minimumIngredient: false // Set to true to highlight add ingredient and show error
	}

	$scope.allIngredients = [];
	$scope.availableIngredients = [];


	$scope.getRecipes = function() {
		$http.get("/api/v1/recipes").then(function(response) {
			$scope.allRecipes = response.data;
		})
	}

	$scope.putRecipe = function() {
		$scope.success = false;
		$scope.failure = false;
		console.log($scope.selectedRecipe);

		$http.put("/api/v1/recipes/" + $scope.selectedRecipe.name, $scope.selectedRecipe).then(
			function(response) {
				$scope.success = true;
				$scope.failure = false;
				
				$scope.getRecipes();
			}, function(rejection) {
				$scope.failure = true;
				$scope.success = false;
				$scope.error = "Failed to update recipe";
				console.error("Error while updating inventory");
			});

	}

	$scope.getIngredients = function() {
		$http.get("/api/v1/inventory").then(function(response) {
			$scope.allIngredients = response.data.ingredients;
		})
	}



	$scope.filterAvailableIngredients = function() {
		$scope.availableIngredients = $scope.allIngredients.filter(function(ingredient) {
			return !$scope.hasIngredient(ingredient.name);
		})
	}

	$scope.hasIngredient = function(name) {
		let ingredients = $scope.selectedRecipe.ingredients;
		for (let index = 0; index < ingredients.length; index++) {
			let ingredient = ingredients[index];
			if (ingredient.name == name) {
				return true;
			}
		}
		return false;
	}


	$scope.recipeSelected = function() {
		$scope.clearErrors();
		
		if ($scope.selectedRecipe == undefined) return;
		$scope.filterAvailableIngredients();
	}

	$scope.ingredientSelected = function() {
		if ($scope.selectedIngredient == undefined) return;
		if ($scope.hasIngredient($scope.selectedIngredient.name)) return;
		
		$scope.invalid.minimumIngredient = false;

		$scope.selectedRecipe.ingredients.push({
			name: $scope.selectedIngredient.name,
			amount: '',
			isInventory: false
		})


		$scope.selectedIngredient = undefined;
		$scope.noIngredients = false;
		$scope.filterAvailableIngredients();
	}

	$scope.removeIngredient = function(ingredient) {
		let ingredients = $scope.selectedRecipe.ingredients;
		let index = ingredients.indexOf(ingredient);
		if (index < 0) return;

		ingredients.splice(index, 1);

		if (ingredients.length <= 0) {
			$scope.noIngredients = true;
		}

		$scope.filterAvailableIngredients();
	}

	$scope.submit = function() {
		if ($scope.selectedRecipe.ingredients.length <= 0) {
			$scope.invalid.minimumIngredient = true;
			return;
		}
		
		$scope.putRecipe();
	}
	
	$scope.clearErrors = function() {
		$scope.success = false;
		$scope.failure = false;
		$scope.invalid.minimumIngredient = false;
	}

	$scope.reset = function() {
		$scope.selectedRecipe = undefined;
		$scope.clearErrors();
	}

	$scope.reset();
	$scope.getRecipes();
	$scope.getIngredients();

	console.log($scope);

});