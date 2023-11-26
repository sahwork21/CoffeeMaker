var app = angular.module('myApp', []);
app.controller('AddRecipeController', function($scope, $http, $q) {
	// Recipe object that is to be sent in POST
	$scope.recipe = {
		name: '',
		price: '',
		ingredients: []
	}



	$scope.selectedIngredient = null;
	$scope.allIngredients = []; // All the ingredients that exist in the inventory
	$scope.availableIngredients = [];	// The ingredients to be presented in dropdown (excludes already added ingredients)
	$scope.invalid = {} // Mark fields in the form as invalid. Ingredients are read as invalid[ingredient.name]


	$scope.hasIngredient = function(name) {
		let ingredients = $scope.recipe.ingredients;
		for (let index = 0; index < ingredients.length; index++) {
			let ingredient = ingredients[index];
			if (ingredient.name == name) {
				return true;
			}
		}
		return false;
	}


	$scope.ingredientSelected = function() {
		if ($scope.selectedIngredient == undefined) return;
		if ($scope.hasIngredient($scope.selectedIngredient.name)) return;

		$scope.invalid.minimumIngredient = false;

		$scope.recipe.ingredients.push({
			name: $scope.selectedIngredient.name,
			amount: '',
			isInventory: false
		})


		$scope.selectedIngredient = undefined;
		$scope.validateIngredients();
		$scope.filterAvailableIngredients();
	}

	$scope.filterAvailableIngredients = function() {
		let availableIngredients = [];
		for (ingredient of $scope.allIngredients) {
			if (!$scope.hasIngredient(ingredient.name)) {
				availableIngredients.push(ingredient);
			}
		}

		$scope.availableIngredients = availableIngredients;

		let dropdown = document.getElementById("ingredientDropdown");
		dropdown.value = null;
	}

	$scope.removeIngredient = function(ingredient) {
		let ingredients = $scope.recipe.ingredients;
		let index = ingredients.indexOf(ingredient);
		if (index < 0) return;

		ingredients.splice(index, 1);
		
		$scope.validateIngredients();
		$scope.filterAvailableIngredients();
	}



	$http.get("/api/v1/inventory").then(function(response) {
		let ingredients = response.data.ingredients;
		console.log(response.data);
		for (let i = 0; i < ingredients.length; i++) {
			$scope.allIngredients[i] = {
				'id': ingredients[i].id,
				'name': ingredients[i].name,
				'display': ingredients[i].name
			}
		}

		$scope.filterAvailableIngredients();
	})

	$scope.addRecipe = function() {
		console.log($scope.recipe)
		$scope.success = false;
		$scope.failure = false;

		if ($scope.recipe.ingredients <= 0) return;

		$http.post("/api/v1/recipes", $scope.recipe).then(
			function(success) {
				$scope.reset();
				$scope.success = true;
				$scope.failure = false;
			}, function(rejection) {
				console.log(rejection.data);
				$scope.failure = rejection.data.message;
			});
	}

	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}



	// VALIDATION

	onValidate = function(field, condition) {
		$scope.invalid[field] = !condition;
		return condition;
	}

	$scope.validateName = function() { return onValidate('name', $scope.recipe.name != null && $scope.recipe.name.length > 0) };

	$scope.validatePrice = function() { return onValidate('price', $scope.recipe.price > 0) };

	$scope.validateIngredients = function() { return onValidate('ingredients', $scope.recipe.ingredients.length > 0) }

	$scope.validateIngredient = function(ingredient) { return onValidate(ingredient.name, ingredient.amount > 0) }


	$scope.validateRecipe = function(recipe) {
		isValid = true;
		if (!$scope.validateName()) isValid = false;
		if (!$scope.validatePrice()) isValid = false;
		if (!$scope.validateIngredients()) isValid = false;
		
		for (ingredient of recipe.ingredients) {
			if (!$scope.validateIngredient(ingredient)) isValid = false;
		};
		
		return isValid;
	}



	// VALIDATION




	$scope.submit = function() {
		if (!$scope.validateRecipe($scope.recipe)) return;
		$scope.addRecipe();
	}

	// Resets the form
	$scope.reset = function() {
		$scope.recipe = {
			name: '',
			price: '',
			ingredients: []
		};
		
		$scope.filterAvailableIngredients();
	}

	$scope.reset();
});