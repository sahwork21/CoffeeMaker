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
		
		if (!$scope.validateRecipe($scope.selectedRecipe)) return;
	
	
		// You have to delete the id from the ingredients because the backend considers ALL ingredients as new ingredients.
		for (let i = 0; i < $scope.selectedRecipe.ingredients.length; i++) {
			console.log(i, $scope.selectedRecipe.ingredients[i]);
			$scope.selectedRecipe.ingredients[i].id = null;
		}


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
		if ($scope.selectedRecipe == undefined) {
			$scope.failure = false;
			return;
		}
		$scope.clearErrors();
		
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
	
	// VALIDATION

	onValidate = function(field, condition) {
		$scope.invalid[field] = !condition;
		return condition;
	}

	$scope.validatePrice = function() { return onValidate('price', $scope.selectedRecipe.price > 0) };

	$scope.validateIngredients = function() { return onValidate('ingredients', $scope.selectedRecipe.ingredients.length > 0) }

	$scope.validateIngredient = function(ingredient) { return onValidate(ingredient.name, ingredient.amount > 0) }



	
	$scope.validateRecipe = function() {
		isValid = true;
		console.log($scope.selectedRecipe.price, $scope.selectedRecipe.price > 0, $scope.validatePrice())
		if (!$scope.validatePrice()) isValid = false;
		if (!$scope.validateIngredients()) isValid = false;
		for (ingredient of $scope.selectedRecipe.ingredients) {
			if (!$scope.validateIngredient(ingredient)) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	$scope.submit = function() {
		if (!$scope.validateRecipe()) return;
		
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