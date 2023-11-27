var app = angular.module('myApp', []);
app.controller('AddIngredientController', function($scope, $http, $q) {
	// Recipe object that is to be sent in POST
	$scope.ingredient = {
		name: '',
		amount: '',
		isInventory: false
	}
	$scope.invalid = {};


	$scope.addIngredient = function() {
		$scope.success = false;
		$scope.failure = false;

		$http.put("/api/v1/inventory/ingredients", $scope.ingredient).then(
			function(response) {
				$scope.success = true;
				$scope.reset();

			}, function(rejection) {
				$scope.failure = rejection.data.message;
				$scope.success = false;
				console.error("Error while updating inventory");
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

	$scope.validateName = function() { return onValidate('name', $scope.ingredient.name && $scope.ingredient.name.length > 0) };

	$scope.validateAmount = function() { console.log($scope); return onValidate('amount', $scope.ingredient.amount > 0) };


	$scope.validateIngredient = function() {
		isValid = true;
		if (!$scope.validateName()) isValid = false;
		if (!$scope.validateAmount()) isValid = false;
		
		
		return isValid;
	}

	// VALIDATION




	$scope.submit = function() {
		if (!$scope.validateIngredient()) return;
		$scope.addIngredient();
	}

	// Resets the form
	$scope.reset = function() {
		$scope.ingredient = {
			name: '',
			amount: '',
			isInventory: false
		};

	}

	$scope.reset();

});